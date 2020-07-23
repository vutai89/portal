CREATE OR REPLACE procedure update_sync_app_transaction is
    l_ca_id CREDIT_APP_BPM.CREDIT_APP_ID%TYPE;
    l_sync_date date;
    l_sync_de_date date;
    l_id    number(15);
    l_trail number(10) := 0;
    l_msg varchar2(2000);
    l_upl_car_msg varchar2(1000);
    l_curTask varchar2(2);
    l_comment varchar2(4000);
    l_prod_id int;
    l_tenor int;
    l_amount    number;
    l_sale_code varchar2(40);
    l_rate  number;
    l_workflow varchar2(50);
    l_prodgrp varchar2(50);
    l_custName varchar2(200);
    l_citizenId varchar2(50);
    l_cust_name varchar2(200);
    l_cust_id number(13);
    l_birthdate date := null;
    l_issuedate date := null;
    l_identity_id int;
    l_app_status int := 0;
    l_new_status int := 0;
    l_upl_id number(15);
    l_upl_status varchar2(5) := null;
begin

      select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'APP_TRANSACTION';
      select LAST_SYNC into l_sync_de_date from CDC_SYNC_LOG where TABLE_NAME = 'DE_APP_TRANSACTION';
      -- Update app_transaction
      --FOR i IN (select * from CDB_APP_TRANSACTION where LAST_UPDATE_DATE >= l_sync_date and appID is not null)
      FOR i IN (select 1 AS WF,ID,APPNUMBER,APPID,PROCESSID,PROCESSNAME,CREATEBY,CREATEDATE,UPDATEBY,UPDATEDATE,
        FINISHDATE,APPSTATUS,CURRENTTASK,CURRENTUSER,CURRENTDECISION,CURRENTCOMMENT,ABORTPROCESSREASON,
        ABORTPROCESSCOMMENT,BUSINESSLINE,TYPEOFFLOW,PRODUCTGROUP,LAST_UPDATE_DATE
        from CDB_APP_TRANSACTION
        where LAST_UPDATE_DATE >= (select LAST_SYNC from CDC_SYNC_LOG where TABLE_NAME = 'APP_TRANSACTION') 
        and appID is not null and PROCESSID <> '46997778359b23c7d855ce6051135589'
        union all
        select 2 AS WF,ID,APPNUMBER,APPID,PROCESSID,PROCESSNAME,CREATEBY,CREATEDATE,UPDATEBY,UPDATEDATE,
        FINISHDATE,APPSTATUS,CURRENTTASK,CURRENTUSER,CURRENTDECISION,CURRENTCOMMENT,ABORTPROCESSREASON,
        ABORTPROCESSCOMMENT,BUSINESSLINE,null as TYPEOFFLOW,null as PRODUCTGROUP,LAST_UPDATE_DATE
        from DE_APP_TRANSACTION
        where LAST_UPDATE_DATE >= (select LAST_SYNC from CDC_SYNC_LOG where TABLE_NAME = 'DE_APP_TRANSACTION') 
        and appID is not null and PROCESSID = '46997778359b23c7d855ce6051135589')
      LOOP
        -- check credit app
        begin
            l_id := i.ID;
            l_ca_id := 0;
            select CREDIT_APP_ID into l_ca_id from CREDIT_APP_BPM where BPM_APP_ID = i.appID;
        exception
            when no_data_found then
                l_ca_id := 0;
            when others then
                raise;
        end;
        
        IF i.processId = '92489624057e0e0abafb278083044537' THEN
            l_workflow := 'InstallmentLoan';
        ELSIF i.processId = '357116699583cdc94981987059206300' THEN
            l_workflow := 'CashLoan';
        ELSE
            l_workflow := 'ConcentratingDataEntry';
        END IF;
        
        l_prodgrp := i.PRODUCTGROUP;
        IF l_prodgrp is null THEN
            IF i.processId = '92489624057e0e0abafb278083044537' THEN
                l_prodgrp := 'InstallmentLoan';
            ELSIF i.processId = '357116699583cdc94981987059206300' THEN
                l_prodgrp := 'CashLoan';
            ELSE
                l_prodgrp := 'CashLoan';
            END IF;
        END IF;

        -- update CREDIT_APP_REQUEST and CREDIT_APP_BPM
        IF l_ca_id > 0 THEN
            update CREDIT_APP_REQUEST set
                STATUS = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
                completed_date = i.FINISHDATE,
                SALE_CHANNEL  = (select ID from CODE_TABLE where CATEGORY = 'SALE_CHAN' and CODE_VALUE1 = i.BUSINESSLINE),
                PRODUCT_GROUP = (select ID from CODE_TABLE where CATEGORY = 'PROD_GRP' and CODE_VALUE1 = l_prodgrp),
                CREATED_DATE = nvl(i.CREATEDATE,CREATED_DATE),
                CREATED_BY = nvl(substr(i.CREATEBY,1,30),CREATED_BY),
                LAST_UPDATED_DATE = nvl(i.UPDATEDATE,sysdate),
                LAST_UPDATED_BY = substr(i.UPDATEBY,1,30)
            where ID = l_ca_id;
            
            update CREDIT_APP_BPM set
                WORKFLOW = l_workflow
            where CREDIT_APP_ID = l_ca_id;
        ELSE
            select SEQ_CREDIT_APP_REQUEST_ID.nextval into l_ca_id from dual;
            -- insert CREDIT_APP_REQUEST
            l_prod_id := 0;
            l_tenor := 0;
            l_amount := 0;
            l_sale_code := null;
            l_rate := 0;
            l_cust_id := 0;
            l_custName := null;
            l_citizenId := null;
            l_issuedate := null;
            begin
                select p1.ID,de.LOANTENOR,to_number(de.LOANAMOUNT),de.SALECODE,it.YEARLY_RATE,CUSTOMERNAME,CITIZENID,ISSUEDATECITIZENID
                    into l_prod_id,l_tenor,l_amount,l_sale_code,l_rate,l_custName,l_citizenId, l_issuedate
                from DE_DATA_ENTRY_SALES de 
                inner join PRODUCTS p1 on p1.PRODUCT_CODE = de.SCHEMEPRODUCT
                inner join INTEREST_TABLE it on IT.ID = p1.RATE_INDEX
                where p1.START_EFF_DATE = (select max(START_EFF_DATE) from PRODUCTS b
                    where b.PRODUCT_CODE = de.SCHEMEPRODUCT and b.START_EFF_DATE <= de.CREATEDDATE)
                and APPID = i.appID;
            exception
                when others then
                    null;
            end;
            -- update customer
            IF l_citizenId is not null THEN
                begin
                    select ID,BIRTH_DATE into l_cust_id,l_birthdate  from (
                        select cpi.ID,cpi.BIRTH_DATE from CUST_PERSONAL_INFO cpi 
                            inner join CUST_IDENTITY ci on cpi.ID = ci.CUST_ID
                        where ci.IDENTITY_NUMBER in (nvl(l_citizenId,'$$$'))
                        order by nvl(cpi.BIRTH_DATE,to_date('99991231','YYYYMMDD')))
                    where rownum=1;

                    IF l_cust_id > 0 AND l_birthdate is not null THEN
                        select ID into l_cust_id from CUST_PERSONAL_INFO where MC_CUST_CODE = get_md5(l_citizenId, l_birthdate);
                    END IF;

                exception
                    when others then
                        l_cust_id := 0;
                end;

                IF l_cust_id = 0 THEN
                    select SEQ_CUST_PERSONAL_INFO_ID.nextval into l_cust_id from dual;

                    INSERT INTO CUST_IDENTITY(CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,IDENTITY_NUMBER,
                        IDENTITY_ISSUE_DATE,IDENTITY_EXPIRY_DATE,IDENTITY_TYPE_ID)
                    VALUES (l_cust_id, i.CREATEDATE, nvl(i.UPDATEDATE,sysdate), substr(i.CREATEBY,1,30), 
                        substr(i.UPDATEBY,1,30), nvl(l_citizenId,'NO_ID'), l_issuedate, null,
                        (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '1'));

                    select ID into l_identity_id from CUST_IDENTITY where CUST_ID = l_cust_id
                        and IDENTITY_TYPE_ID = (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '1');

                    Insert into CUST_PERSONAL_INFO(ID,cust_name,MC_CUST_CODE,last_updated_date,last_updated_by,CREATED_DATE,CREATED_BY,IDENTITY_ID)
                    select l_cust_id,nvl(l_custName,'INVALID NAME'),get_md5(l_citizenId, null),
                        nvl(i.UPDATEDATE,sysdate),substr(i.UPDATEBY,1,30),i.CREATEDATE,substr(i.CREATEBY,1,30),l_identity_id
                    from dual;
                END IF;
            END IF;

            insert into CREDIT_APP_REQUEST(ID,CUST_ID,CREATED_DATE,CREATED_BY,LAST_UPDATED_DATE,LAST_UPDATED_BY,
                INT_RATE,LN_AMOUNT,LN_TENOR,PRODUCT_ID,sale_code,sale_id,STATUS,SALE_CHANNEL,PRODUCT_GROUP,completed_date)
            select l_ca_id,l_cust_id,nvl(i.CREATEDATE,sysdate),substr(i.CREATEBY,1,30),nvl(i.UPDATEDATE,sysdate),substr(i.UPDATEBY,1,30),
            l_rate, l_amount, l_tenor, l_prod_id, l_sale_code,
            (select EMP_ID from EMPLOYEE_LINK where EMP_CODE = l_sale_code),
            (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
            (select ID from CODE_TABLE where CATEGORY = 'SALE_CHAN' and CODE_VALUE1 = i.BUSINESSLINE),
            (select ID from CODE_TABLE where CATEGORY = 'PROD_GRP' and CODE_VALUE1 = l_prodgrp),
            i.FINISHDATE from dual;
            -- insert CREDIT_APP_BPM
            insert into CREDIT_APP_BPM(CREDIT_APP_ID,CREATED_DATE,CREATED_BY,BPM_ID,BPM_APP_ID,BPM_APP_NUMBER,WORKFLOW)
            select l_ca_id,nvl(i.CREATEDATE,sysdate),substr(i.CREATEBY,1,30),0,nvl(i.APPID,i.ID),i.APPNUMBER,l_workflow from dual;
        END IF;
        

        -- logging
        insert into MESSAGE_LOG(MSG_TYPE,FROM_CHANNEL,TO_CHANNEL,RELATION_ID,TRANS_ID,TRANS_TYPE,MSG_REQUEST)
        values('D','BPM','MCP',i.appId,l_upl_id,'debugSyncData',
            '1;l_workflow='||l_workflow||';l_ca_id='||l_ca_id||';i.APPSTATUS='||i.APPSTATUS);

        IF l_workflow = 'ConcentratingDataEntry' THEN
            -- Update UPL_CREDIT_APP_REQUEST
            begin
                l_app_status := 0;
                l_new_status := 0;
                l_upl_status := null;
                l_upl_car_msg := 'bpmid='||i.id;
                select ID, nvl(APP_STATUS,0) into l_upl_id, l_app_status from UPL_CREDIT_APP_REQUEST where APP_ID = i.appId;
                l_upl_car_msg := l_upl_car_msg||';uplid='||l_upl_id||';bpmstatus='||i.APPSTATUS;
                select ID into l_new_status from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS;
                l_upl_car_msg := l_upl_car_msg||';statusid='||l_new_status;
                IF i.APPSTATUS in ('SALE_RETURN_OPEN','SALE_RETURN_PENDING','DE_RETURN_OPEN','DE_RETURN_PENDING') THEN
                    l_upl_status := 'R';
                ELSIF i.APPSTATUS like '%REJECT' THEN
                    l_upl_status := 'J';
                ELSIF i.APPSTATUS like '%ABORT' AND i.APPSTATUS not in ('SALE_ABORT','DE_ABORT') THEN
                    l_upl_status := 'B';
                ELSIF i.APPSTATUS = 'DONE' THEN
                    l_upl_status := 'V';
                END IF;
                IF l_new_status <> l_app_status THEN
                    -- insert into MESSAGE_LOG
                    insert into MESSAGE_LOG(MSG_TYPE,FROM_CHANNEL,TO_CHANNEL,RELATION_ID,TRANS_ID,TRANS_TYPE,SERVICE_NAME,MSG_REQUEST,TASK_ID)
                    values('R','BPM','MCP',i.appId,l_upl_id,'notifyLOSApplication',null,'['||to_char(l_new_status)||','||to_char(l_app_status)||']',0);
                    l_upl_car_msg := l_upl_car_msg||';Done MESSAGE_LOG';
                    update UPL_CREDIT_APP_REQUEST set
                        CREDIT_APP_ID = l_ca_id,
                        PREVIOUS_APP_STATUS = l_app_status,
                        APP_STATUS = l_new_status,
                        STATUS = nvl(l_upl_status, STATUS)
                    where ID = l_upl_id;
                END IF;
            exception
                when no_data_found then
                    null;
                when others then
                    l_upl_car_msg := l_upl_car_msg||';'||sqlcode||' - '||sqlerrm;
                    insert into MESSAGE_LOG(MSG_TYPE,FROM_CHANNEL,TO_CHANNEL,RELATION_ID,TRANS_ID,TRANS_TYPE,SERVICE_NAME,RESPONSE_ERROR_DESC)
                    values('I','BPM','MCP',i.appId,l_upl_id,'syncLOStoMCPortal',null,substr(l_upl_car_msg,1,255));
            end;
        END IF;
        
        -- update CREDIT_APP_TRAIL
        IF i.APPSTATUS in ('SALE_OPEN','DE_OPEN') THEN
        -- open new case
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = l_ca_id and TRAIL_SEQ = 1 
            and TRAIL_ORDER = 4000 and CURRENT_TASK = 'SA';
            
            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = nvl(i.CREATEDATE,CREATED_DATE),
                    LAST_UPDATED_DATE = nvl(i.UPDATEDATE,sysdate),
                    CREATED_BY = substr(i.CREATEBY,1,30),
                    LAST_UPDATED_BY = substr(i.UPDATEBY,1,30),
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
                    TASK_ID = i.CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'OPEN'),
                    STEP_CODE = decode(nvl(instr(i.APPSTATUS,'_'),0),0,i.APPSTATUS,substr(i.APPSTATUS,1,instr(i.APPSTATUS,'_')-1)),
                    FROM_USER_CODE = substr(i.CREATEBY,1,30)
                where CREDIT_APP_ID = l_ca_id and nvl(TRAIL_SEQ,0) = 1 
                    and nvl(TRAIL_ORDER,0) = 4000 and CURRENT_TASK = 'SA';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,STEP_CODE,FROM_USER_CODE,CURRENT_TASK)
                VALUES (l_ca_id,1,4000,nvl(i.CREATEDATE,sysdate),nvl(i.CREATEDATE,sysdate),substr(i.CREATEBY,1,30),substr(i.CREATEBY,1,30),
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
                        i.CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'OPEN'),
                        decode(nvl(instr(i.APPSTATUS,'_'),0),0,i.APPSTATUS,substr(i.APPSTATUS,1,instr(i.APPSTATUS,'_')-1)),
                        substr(i.CREATEBY,1,30),'SA');
            END IF;
        ELSIF i.APPSTATUS = 'DONE' THEN
            -- complete
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = l_ca_id and TRAIL_SEQ = 1 
            and TRAIL_ORDER = 4100 and CURRENT_TASK = 'SA';
            
            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = nvl(i.CREATEDATE,CREATED_DATE),
                    LAST_UPDATED_DATE = nvl(i.UPDATEDATE,sysdate),
                    CREATED_BY = substr(i.CREATEBY,1,30),
                    LAST_UPDATED_BY = substr(i.UPDATEBY,1,30),
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
                    TASK_ID = i.CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'DONE'),
                    STEP_CODE = decode(nvl(instr(i.APPSTATUS,'_'),0),0,i.APPSTATUS,substr(i.APPSTATUS,1,instr(i.APPSTATUS,'_')-1)),
                    FROM_USER_CODE = substr(i.CREATEBY,1,30)
                where CREDIT_APP_ID = l_ca_id and nvl(TRAIL_SEQ,0) = 1 
                    and nvl(TRAIL_ORDER,0) = 4100 and CURRENT_TASK = 'SA';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,STEP_CODE,FROM_USER_CODE,CURRENT_TASK)
                VALUES (l_ca_id,1,4100,nvl(i.CREATEDATE,sysdate),nvl(i.CREATEDATE,sysdate),substr(i.CREATEBY,1,30),substr(i.CREATEBY,1,30),
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
                        i.CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'DONE'),
                        decode(nvl(instr(i.APPSTATUS,'_'),0),0,i.APPSTATUS,substr(i.APPSTATUS,1,instr(i.APPSTATUS,'_')-1)),
                        substr(i.CREATEBY,1,30),'SA');
            END IF;
        ELSIF i.APPSTATUS like '%ABORT%' THEN
        -- abort case
            begin
                IF nvl(i.PROCESSNAME,'$$$') = 'Concentrating Data Entry' THEN
                    -- get comment from DE
                    select get_clob(decode(de2.ABORTPROCESSREASON||de2.ABORTPROCESSCOMMENT,null,
                        de.ABORTPROCESSREASON || decode(de.ABORTPROCESSCOMMENT,null,null,' - '||de.ABORTPROCESSCOMMENT),
                        de2.ABORTPROCESSREASON || decode(de2.ABORTPROCESSCOMMENT,null,null,' - '||de2.ABORTPROCESSCOMMENT))) 
                        into l_comment 
                    from DE_DATA_ENTRY_2 de2 left join DE_DATA_ENTRY de on de2.APPID = de.APPID
                    where de2.APPID = i.APPID;
                ELSE
                    select get_clob(de.ABORTPROCESSREASON || decode(de.ABORTPROCESSCOMMENT,null,null,' - '||de.ABORTPROCESSCOMMENT))
                        into l_comment from CDB_DATA_ENTRY de where de.APPID = i.APPID;
                END IF;
            exception
                    when others then
                        l_comment := null;
            end;
            IF i.APPSTATUS = 'CA_ABORT' THEN
                l_curTask := 'CA';
            ELSIF i.APPSTATUS = 'AP_ABORT' THEN
                l_curTask := 'AP';
            ELSE
                l_curTask := 'SA';
            END IF;
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = l_ca_id and TRAIL_SEQ = 1 
            and TRAIL_ORDER = 4200 and CURRENT_TASK = l_curTask;
            
            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = nvl(i.CREATEDATE,CREATED_DATE),
                    LAST_UPDATED_DATE = nvl(i.UPDATEDATE,sysdate),
                    CREATED_BY = substr(i.CREATEBY,1,30),
                    LAST_UPDATED_BY = substr(i.UPDATEBY,1,30),
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
                    TASK_ID = i.CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'ABORT'),
                    STEP_CODE = decode(nvl(instr(i.APPSTATUS,'_'),0),0,i.APPSTATUS,substr(i.APPSTATUS,1,instr(i.APPSTATUS,'_')-1)),
                    USER_COMMENT = l_comment,
                    FROM_USER_CODE = substr(i.CREATEBY,1,30)
                where CREDIT_APP_ID = l_ca_id and nvl(TRAIL_SEQ,0) = 1 
                    and nvl(TRAIL_ORDER,0) = 4200 and CURRENT_TASK = l_curTask;
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,STEP_CODE,USER_COMMENT,FROM_USER_CODE,CURRENT_TASK)
                VALUES (l_ca_id,1,4200,nvl(i.CREATEDATE,sysdate),nvl(i.CREATEDATE,sysdate),substr(i.CREATEBY,1,30),substr(i.CREATEBY,1,30),
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = i.APPSTATUS),
                        i.CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'ABORT'),
                        decode(nvl(instr(i.APPSTATUS,'_'),0),0,i.APPSTATUS,substr(i.APPSTATUS,1,instr(i.APPSTATUS,'_')-1)),
                        l_comment,substr(i.CREATEBY,1,30),l_curTask);
            END IF;
        END IF;

      END LOOP;

exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20003,'Exception in update_sync_app_transaction. ID='||l_id||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure update_sync_bpm_variable is
    l_msg varchar2(200);
begin

      -- Update code from BPM VAR - DE
      l_msg := 'sync BPM_PROCESS_VARIABLES - DE';
    FOR i IN (select * from BPM_PROCESS_VARIABLES bpm 
        inner join UPL_BPM_CODE ubc on bpm.VAR_NAME = ubc.code_cat_cs and bpm.PRJ_UID = ubc.wf_cs
        order by bpm.VAR_NAME)
    LOOP
        l_msg := 'sync BPM_PROCESS_VARIABLES - DE - '||i.VAR_UID;
        MERGE INTO CODE_TABLE ct USING
         (select * from (with json as
            ( select i.VAR_ACCEPTED_VALUES doc from   dual)  
            SELECT value, label FROM  json_table( (select doc from json) , '$[*]'  
                COLUMNS ( value PATH '$.value', label PATH '$.label'))) cs
                where cs.value is not null) code
        ON (ct.CATEGORY = i.CATEGORY and ct.CODE_VALUE1 = code.value)
        WHEN NOT MATCHED THEN 
        INSERT (CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
        VALUES (
            i.CODE_GROUP, i.CATEGORY, code.value, null, code.label, null, i.CODE_CAT_CS)
        WHEN MATCHED THEN
        UPDATE SET 
            ct.DESCRIPTION1 = code.label,
            ct.CODE_GROUP = i.CODE_GROUP,
            ct.REFERENCE = i.CODE_CAT_CS;
    END LOOP;

      -- Update code from BPM VAR - IS
      l_msg := 'sync BPM_PROCESS_VARIABLES - IS';
    FOR i IN (select * from BPM_PROCESS_VARIABLES bpm 
        inner join UPL_BPM_CODE ubc on bpm.VAR_NAME = ubc.code_cat_is and bpm.PRJ_UID = ubc.wf_is
        order by bpm.VAR_NAME)
    LOOP
        l_msg := 'sync BPM_PROCESS_VARIABLES - IS - '||i.VAR_UID;
        MERGE INTO CODE_TABLE ct USING
         (select ORA_HASH(il.value,1000000000,1) hash_key,il.*,cs.* from (with json as
            ( select i.VAR_ACCEPTED_VALUES doc from   dual)
            SELECT value, label FROM  json_table( (select doc from json) , '$[*]'  
                COLUMNS ( value PATH '$.value', label PATH '$.label'))) il
            left join code_table cs on cs.CATEGORY = i.CATEGORY and cs.DESCRIPTION1 = il.label
            where il.value is not null) code
        ON (ct.CATEGORY = 'IS_'||i.CATEGORY and ct.DESCRIPTION2 = code.value)
        WHEN NOT MATCHED THEN 
        INSERT (CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
        VALUES (
            i.CODE_GROUP, 'IS_'||i.CATEGORY, nvl(code.CODE_VALUE1,'$'||to_char(code.hash_key)), null, code.label, code.value, i.CODE_CAT_IS)
        WHEN MATCHED THEN
        UPDATE SET 
            ct.DESCRIPTION1 = code.label,
            ct.CODE_GROUP = i.CODE_GROUP,
            ct.REFERENCE = i.CODE_CAT_IS;
    END LOOP;

      -- Update code SYSTEM_DEFINE_FIELDS
    l_msg := 'sync SYSTEM_DEFINE_FIELDS';
    MERGE INTO SYSTEM_DEFINE_FIELDS sdf USING
     (select * from CODE_TABLE where CATEGORY in ('GENDER')) code
    ON (sdf.CATEGORY = code.CATEGORY and sdf.CODE_TABLE_ID = code.ID)
    WHEN NOT MATCHED THEN 
    INSERT (SYSTEM,CODE_TABLE_ID,CATEGORY,CODE_TABLE_VALUE,SYSTEM_VALUE,DESCRIPTION,STATUS)
    VALUES (
        'DEF',code.ID,code.CATEGORY,code.CODE_VALUE1,code.CODE_VALUE1,code.DESCRIPTION1,'A')
    WHEN MATCHED THEN
    UPDATE SET 
        sdf.CODE_TABLE_VALUE = code.CODE_VALUE1;

exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20003,'Exception in update_sync_bpm_variable. Message='||l_msg||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure update_sync_codetable_data is
    l_msg varchar2(200);
begin

      -- Update Province
      l_msg := 'sync SNW_SLN_PROVINCE';
    MERGE INTO CODE_TABLE ct USING
     (select * from SNW_SLN_PROVINCE) pro
    ON (ct.CATEGORY = 'PROVINCE' and ct.CODE_VALUE1 = pro.ISO_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'COUNTRY' and CODE_VALUE1 = 'VN'),
        'LOCA', 'PROVINCE', pro.ISO_CODE, null, pro.PROVINCE_NAME, pro.PROVINCE_NAME, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = pro.PROVINCE_NAME;

      -- Update District
      l_msg := 'sync SNW_SLN_DISTRICT';
    MERGE INTO CODE_TABLE ct USING
     (select sd.*,d.DISTRICTNAME as ALTER_NAME from SNW_SLN_DISTRICT sd 
        left join CDB_DISTRICT d on sd.iso_code = d.DISTRICTID) dis
    ON (ct.CATEGORY = 'DISTRICT' and ct.CODE_VALUE1 = dis.ISO_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and CODE_VALUE1 = dis.PROVINCE_CODE),
        'LOCA', 'DISTRICT', dis.ISO_CODE, null, dis.DISTRICT_NAME, dis.ALTER_NAME, dis.PROVINCE_CODE)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = dis.DISTRICT_NAME,
        ct.DESCRIPTION2 = dis.ALTER_NAME,
        ct.REFERENCE = dis.PROVINCE_CODE,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and CODE_VALUE1 = dis.PROVINCE_CODE);

      -- Update Ward
      l_msg := 'sync SNW_SLN_WARD';
    MERGE INTO CODE_TABLE ct USING
     (select sw.*,wd.wardName as alter_name from SNW_SLN_WARD sw left join CDB_WARD wd on sw.iso_code = wd.WARDID) ward
    ON (ct.CATEGORY = 'WARD' and ct.CODE_VALUE1 = ward.ISO_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and CODE_VALUE1 = ward.DISTRICT_CODE),
        'LOCA', 'WARD', ward.ISO_CODE, null, ward.WARD_NAME, ward.alter_name, ward.DISTRICT_CODE)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = ward.WARD_NAME,
        ct.DESCRIPTION2 = ward.ALTER_NAME,
        ct.REFERENCE = ward.DISTRICT_CODE,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and CODE_VALUE1 = ward.DISTRICT_CODE);
        
    -- Patch invalid data
    update CODE_TABLE set DESCRIPTION2 = 'Th? xã Tân Thành' where CATEGORY = 'WARD' and CODE_VALUE1 in ('32026');
    
    update CODE_TABLE set DESCRIPTION1 = DESCRIPTION2
    where (PARENT_ID,DESCRIPTION1) in (select PARENT_ID,DESCRIPTION1 from CODE_TABLE
        where CATEGORY in ('DISTRICT','WARD') group by PARENT_ID,DESCRIPTION1 having count(1) > 1);

    -- update POS/SIP
      l_msg := 'sync SNW_SLN_DEPARTMENT POS';
    MERGE INTO CODE_TABLE ct USING
     (select * from SNW_SLN_DEPARTMENT where TYPE = 4) pos
    ON (ct.CATEGORY = 'POS_SIP' and ct.CODE_VALUE1 = pos.DEPARTMENT_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,'SALE', 'POS_SIP', pos.DEPARTMENT_CODE, null, pos.DEPARTMENT_NAME, pos.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = pos.DEPARTMENT_NAME;

    -- update HUB/KIOS
      l_msg := 'sync SNW_SLN_DEPARTMENT HUB';
    MERGE INTO CODE_TABLE ct USING
     (select * from SNW_SLN_DEPARTMENT where TYPE in (6,7)) hub
    ON (ct.CATEGORY = 'TRAN_OFF' and ct.CODE_VALUE1 = hub.DEPARTMENT_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,decode(hub.TYPE,6,'HUB','KIOSK'), 'TRAN_OFF', hub.DEPARTMENT_CODE, null, hub.DEPARTMENT_NAME, hub.ID, null)
    WHEN MATCHED THEN
    UPDATE SET
        ct.CODE_GROUP = decode(hub.TYPE,6,'HUB','KIOSK'),
        ct.DESCRIPTION1 = hub.DEPARTMENT_NAME;

    -- update reason return DE
      l_msg := 'sync DE_SYS_CATEGORY reason for return';
    MERGE INTO CODE_TABLE ct USING
     (select * from DE_SYS_CATEGORY where (CATEGORY_CODE like '%RETURN%')) rs
    ON (ct.CATEGORY = 'RSRT_CODE' and ct.CODE_VALUE1 = rs.CATEGORY_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
        'RSRET', 'RSRT_CODE', rs.CATEGORY_CODE, null, rs.CATEGORY_NAME, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.CATEGORY_NAME,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN');

    -- update reason reject DE
      l_msg := 'sync DE_SYS_CATEGORY reason for reject';
    MERGE INTO CODE_TABLE ct USING
     (select * from DE_SYS_CATEGORY where (CATEGORY_CODE like '%REJECT%')) rs
    ON (ct.CATEGORY = 'RSRJ_CODE' and ct.CODE_VALUE1 = rs.CATEGORY_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
        'RSREJ', 'RSRJ_CODE', rs.CATEGORY_CODE, null, rs.CATEGORY_NAME, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.CATEGORY_NAME,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT');

    -- update reason return
      l_msg := 'sync CDB_SYS_CATEGORY reason for return';
    MERGE INTO CODE_TABLE ct USING
     (select * from CDB_SYS_CATEGORY where (CATEGORY_CODE like '%RETURN%')) rs
    ON (ct.CATEGORY = 'RSRT_CODE' and ct.CODE_VALUE1 = rs.CATEGORY_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
        'RSRET', 'RSRT_CODE', rs.CATEGORY_CODE, null, rs.CATEGORY_NAME, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.CATEGORY_NAME,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN');

    -- update reason reject
      l_msg := 'sync CDB_SYS_CATEGORY reason for reject';
    MERGE INTO CODE_TABLE ct USING
     (select * from CDB_SYS_CATEGORY where (CATEGORY_CODE like '%REJECT%')) rs
    ON (ct.CATEGORY = 'RSRJ_CODE' and ct.CODE_VALUE1 = rs.CATEGORY_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
        'RSREJ', 'RSRJ_CODE', rs.CATEGORY_CODE, null, rs.CATEGORY_NAME, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.CATEGORY_NAME,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT');

    -- update reason detail return DE
      l_msg := 'sync DE_SYS_CATEGORY_DETAIL detail reason for return';
    MERGE INTO CODE_TABLE ct USING
     (select scd.*,sc.CATEGORY_CODE from DE_SYS_CATEGORY_DETAIL scd 
        inner join DE_SYS_CATEGORY sc on sc.ID = scd.SYS_CATEGORY_ID
        where sc.CATEGORY_CODE like '%RETURN%') rs
    ON (ct.CATEGORY = 'RSRTDTL_CD' and ct.CODE_VALUE1 = rs.NAME)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'RSRT_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE),
        'RSRET', 'RSRTDTL_CD', rs.NAME, null, rs.VALUE, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.VALUE,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRT_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE);

    -- update reason detail reject DE
      l_msg := 'sync DE_SYS_CATEGORY_DETAIL detail reason for reject';
    MERGE INTO CODE_TABLE ct USING
     (select scd.*,sc.CATEGORY_CODE from DE_SYS_CATEGORY_DETAIL scd 
        inner join DE_SYS_CATEGORY sc on sc.ID = scd.SYS_CATEGORY_ID
        where sc.CATEGORY_CODE like '%REJECT%') rs
    ON (ct.CATEGORY = 'RSRJDTL_CD' and ct.CODE_VALUE1 = rs.NAME)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'RSRJ_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE),
        'RSREJ', 'RSRJDTL_CD', rs.NAME, null, rs.VALUE, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.VALUE,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRJ_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE);

    -- update reason detail return
      l_msg := 'sync CDB_SYS_CATEGORY_DETAIL detail reason for return';
    MERGE INTO CODE_TABLE ct USING
     (select scd.*,sc.CATEGORY_CODE from CDB_SYS_CATEGORY_DETAIL scd 
        inner join CDB_SYS_CATEGORY sc on sc.ID = scd.SYS_CATEGORY_ID
        where sc.CATEGORY_CODE like '%RETURN%') rs
    ON (ct.CATEGORY = 'RSRTDTL_CD' and ct.CODE_VALUE1 = rs.NAME)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'RSRT_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE),
        'RSRET', 'RSRTDTL_CD', rs.NAME, null, rs.VALUE, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.VALUE,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRT_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE);

    -- update reason detail reject
      l_msg := 'sync CDB_SYS_CATEGORY_DETAIL detail reason for reject';
    MERGE INTO CODE_TABLE ct USING
     (select scd.*,sc.CATEGORY_CODE from CDB_SYS_CATEGORY_DETAIL scd 
        inner join CDB_SYS_CATEGORY sc on sc.ID = scd.SYS_CATEGORY_ID
        where sc.CATEGORY_CODE like '%REJECT%') rs
    ON (ct.CATEGORY = 'RSRJDTL_CD' and ct.CODE_VALUE1 = rs.NAME)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        (select ID from CODE_TABLE where CATEGORY = 'RSRJ_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE),
        'RSREJ', 'RSRJDTL_CD', rs.NAME, null, rs.VALUE, rs.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = rs.VALUE,
        ct.PARENT_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRJ_CODE' and CODE_VALUE1 = rs.CATEGORY_CODE);

    -- Life ins company
      l_msg := 'sync CDB_SYS_CATEGORY_DETAIL life insuarance company';
    MERGE INTO CODE_TABLE ct USING
     (SELECT d.name, d.value FROM de_sys_category c 
        INNER JOIN de_sys_category_detail d ON c.id = d.sys_category_id
        WHERE c.category_code = 'LIFE_INSURANCE_COMPANY' AND d.is_active = 1) lic
    ON (ct.CATEGORY = 'LINS_COMP' and ct.CODE_VALUE1 = lic.name)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,'MISC', 'LINS_COMP', lic.name, null, lic.value, null, null)
    WHEN MATCHED THEN
    UPDATE SET
        ct.DESCRIPTION1 = lic.value;
    
    -- update black/watch list customer
      l_msg := 'sync CDB_BLACKLIST and CDB_WATCHLIST';
    MERGE INTO CUST_MONITOR cm USING
     (select ID,NUMBER_ID,TYPE,NAME,STATUS,CREATE_DATE,CREATE_USER,UPDATE_DATE,UPDATE_USER,'B' mt from CDB_BLACKLIST
        union all
      select ID,NUMBER_ID,TYPE,NAME,STATUS,CREATE_DATE,CREATE_USER,UPDATE_DATE,UPDATE_USER,'W' mt from CDB_WATCHLIST) bw
    ON (cm.CUST_LIST_ID = bw.ID and cm.MONITOR_TYPE = bw.mt)
    WHEN NOT MATCHED THEN 
    INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,
        MONITOR_TYPE,ID_NUMBER,ID_TYPE,CUST_NAME,CUST_LIST_ID,RECORD_STATUS)
    VALUES (
        (select max(cpi.ID) from CUST_PERSONAL_INFO cpi , CUST_IDENTITY ci
            where cpi.IDENTITY_ID = ci.ID and ci.IDENTITY_NUMBER = bw.NUMBER_ID), 
        bw.CREATE_DATE,nvl(bw.UPDATE_DATE,sysdate),bw.CREATE_USER,bw.UPDATE_USER,bw.mt, bw.NUMBER_ID,
        decode(bw.mt,'B',(select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = decode(bw.TYPE,'CMND','1','CCCD','3','TAX','5','0')),null),
        bw.NAME, bw.ID, decode(bw.STATUS,1,'A','C'))
    WHEN MATCHED THEN
    UPDATE SET 
        cm.LAST_UPDATED_DATE = nvl(bw.UPDATE_DATE,sysdate),
        cm.LAST_UPDATED_BY = bw.UPDATE_USER,
        cm.CUST_ID = (select max(cpi.ID) from CUST_PERSONAL_INFO cpi , CUST_IDENTITY ci
            where cpi.IDENTITY_ID = ci.ID and ci.IDENTITY_NUMBER = bw.NUMBER_ID),
        cm.ID_NUMBER = bw.NUMBER_ID,
        cm.ID_TYPE = decode(bw.mt,'B',(select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = decode(bw.TYPE,'CMND','1','CCCD','3','TAX','5','0')),null),
        cm.RECORD_STATUS = decode(bw.STATUS,1,'A','C'),
        cm.CUST_NAME = bw.NAME;


      -- Update Department/Business segment
      l_msg := 'sync SNW_SLN_DEPARTMENT RSM, ASM, BDS';
    MERGE INTO CODE_TABLE ct USING
     (select d.* from SNW_SLN_DEPARTMENT d inner join (select ID,DEPARTMENT_CODE,ACTIVE_DATE,
                ROW_NUMBER() OVER (PARTITION BY DEPARTMENT_CODE ORDER BY DEPARTMENT_CODE,ACTIVE_DATE desc,ID desc) rn 
                from SNW_SLN_DEPARTMENT) d1 on d.ID = d1.ID and d1.rn = 1
        where d.type in (1,2,3)) dep
    ON (ct.CATEGORY = 'BUS_SEGM' and ct.CODE_VALUE1 = dep.DEPARTMENT_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,decode(dep.type,1,'RSM',2,'ASM','BDS'), 'BUS_SEGM', dep.DEPARTMENT_CODE, null, dep.DEPARTMENT_NAME, dep.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.CODE_GROUP = decode(dep.type,1,'RSM',2,'ASM','BDS'),
        ct.DESCRIPTION1 = dep.DEPARTMENT_NAME;
        
      l_msg := 'sync SNW_SLN_DEPARTMENT update DSA manager';
    update CODE_TABLE ct set
        PARENT_ID = (select ct1.ID from SNW_SLN_DEPARTMENT bds 
            inner join (select ID,DEPARTMENT_CODE,ACTIVE_DATE,
                ROW_NUMBER() OVER (PARTITION BY DEPARTMENT_CODE ORDER BY DEPARTMENT_CODE,ACTIVE_DATE desc,ID desc) rn 
                from SNW_SLN_DEPARTMENT) bds1 on bds.ID = bds1.ID and bds1.rn = 1
            inner join SNW_SLN_DEPARTMENT asm on bds.parent_id = asm.ID
            inner join CODE_TABLE ct1 on ct1.CATEGORY = 'BUS_SEGM' and ct1.CODE_GROUP = 'ASM' and ct1.CODE_VALUE1 = asm.DEPARTMENT_CODE
            where bds.type=3 and bds.DEPARTMENT_CODE = ct.CODE_VALUE1)
    where CATEGORY = 'BUS_SEGM' and CODE_GROUP = 'BDS';

      l_msg := 'sync SNW_SLN_DEPARTMENT update BDS, ASM manager';
    update CODE_TABLE ct set
        PARENT_ID = (select ct1.ID from SNW_SLN_DEPARTMENT asm 
            inner join (select ID,DEPARTMENT_CODE,ACTIVE_DATE,
                ROW_NUMBER() OVER (PARTITION BY DEPARTMENT_CODE ORDER BY DEPARTMENT_CODE,ACTIVE_DATE desc,ID desc) rn 
                from SNW_SLN_DEPARTMENT) asm1 on asm.ID = asm1.ID and asm1.rn = 1
            inner join SNW_SLN_DEPARTMENT rsm on asm.parent_id = rsm.ID
            inner join CODE_TABLE ct1 on ct1.CATEGORY = 'BUS_SEGM' and ct1.CODE_GROUP = 'RSM' and ct1.CODE_VALUE1 = rsm.DEPARTMENT_CODE
            where asm.type=2 and asm.DEPARTMENT_CODE = ct.CODE_VALUE1)
    where CATEGORY = 'BUS_SEGM' and CODE_GROUP = 'ASM';

      -- Update Commodities
      l_msg := 'sync CDB_GOODS';
    MERGE INTO CODE_TABLE ct USING
     (select * from CDB_GOODS) goods
    ON (ct.CATEGORY = 'COMM' and ct.CODE_VALUE1 = goods.GOODSCODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,'INST', 'COMM', goods.GOODSCODE, null, goods.GOODSNAME, goods.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = goods.GOODSNAME;

      -- Update brand
      l_msg := 'sync CDB_BRAND';
    MERGE INTO CODE_TABLE ct USING
     (select * from CDB_BRAND a where applied_date = (select max(applied_date) from CDB_BRAND b
        where b.brand_code = a.brand_code)) brand
    ON (ct.CATEGORY = 'BRAND' and ct.CODE_VALUE1 = brand.BRAND_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,'INST', 'BRAND', brand.BRAND_CODE, null, brand.BRAND_NAME, brand.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = brand.BRAND_NAME;

      -- Update brand and commodities
      l_msg := 'sync CDB_BRAND_IN_GOODS';
    MERGE INTO COMM_BRAND cb USING
     (select a.*,com.ID COMM_ID, brand.ID BRAND_ID from CDB_BRAND_IN_GOODS a
        inner join (select ID,goods_code,brand_code,
            ROW_NUMBER() OVER (PARTITION BY goods_code,brand_code ORDER BY goods_code,brand_code,APPLIED_DATE desc,ID desc) rn 
            from CDB_BRAND_IN_GOODS) gb on a.ID = gb.ID and gb.rn = 1 
        inner join code_table com on com.category = 'COMM' and com.CODE_VALUE1 = a.goods_code
        inner join code_table brand on brand.category = 'BRAND' and brand.CODE_VALUE1 = a.brand_code) commBrand
    ON (cb.BRAND_ID = commBrand.BRAND_ID and cb.COMM_ID = commBrand.COMM_ID)
    WHEN NOT MATCHED THEN 
    INSERT (BRAND_ID,COMM_ID,RECORD_STATUS)
    VALUES (
        commBrand.BRAND_ID,commBrand.COMM_ID,decode(commBrand.is_active,1,'A','C'))
    WHEN MATCHED THEN
    UPDATE SET 
        cb.RECORD_STATUS = decode(commBrand.is_active,1,'A','C');

exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20003,'Exception in update_sync_codetable_data. Message='||l_msg||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure update_sync_company_data is
begin

      -- Update Company category
    MERGE INTO CODE_TABLE ct USING
     (select * from PRD_CAT_CATEGORIES_DETAIL) ccat
    ON (ct.CATEGORY = 'CORP_TYPE' and ct.CODE_VALUE1 = ccat.CODE)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,'CORP', 'CORP_TYPE', ccat.CODE, null, ccat.NAME, ccat.ID, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = ccat.NAME;

      -- Update Company category
    MERGE INTO CUST_COMPANY_INFO cci USING
     (select c.*,cc.CAT_TYPE_CODE from PRD_COMPANY c
        left join (select * from PRD_CAT_INFO pci1 where exists (select 1 from (select COMPANY_ID,max(CREATED_DATE) max_date 
        from PRD_CAT_INFO group by COMPANY_ID) pci2 where pci2.COMPANY_ID = pci1.COMPANY_ID and pci2.max_date = pci1.CREATED_DATE)) cc
        on c.ID = cc.COMPANY_ID) ccat
    ON (cci.ID = ccat.ID)
    WHEN NOT MATCHED THEN 
    INSERT (ID,COMP_ADDR_STREET,COMP_NAME,COMP_TAX_NUMBER,OFFICE_PHONE_NUMBER,ESTABLISH_DATE,OPERATION_YEAR,
        COMP_TYPE,IS_TOP_500_1000_COM,IS_TOP_500_1000_BRANCH,TOP_500_1000,CIC_INFO,CIC_CONSULTING_DATE,CAT_TYPE)
    VALUES (ccat.ID,ccat.PLACES,ccat.NAME,ccat.TAX_CODE,ccat.PHONE_NUMBER,ccat.INCORPORATION_DATE,ccat.OPERATION_YEAR,
        (select ID from CODE_TABLE where CATEGORY = 'CORP_TYPE' and CODE_VALUE1 = ccat.TYPE_OF_ENTERPRISE),
        ccat.IS_TOP_500_1000_COM,ccat.IS_TOP_500_1000_BRANCH,ccat.TOP_500_1000,ccat.CIC_INFO,
        ccat.CIC_CONSULTING_DATE,ccat.CAT_TYPE_CODE)
    WHEN MATCHED THEN
    UPDATE SET 
        cci.COMP_ADDR_STREET = ccat.PLACES,
        cci.COMP_NAME = ccat.NAME,
        cci.COMP_TAX_NUMBER = ccat.TAX_CODE,
        cci.OFFICE_PHONE_NUMBER = ccat.PHONE_NUMBER,
        cci.ESTABLISH_DATE = ccat.INCORPORATION_DATE,
        cci.OPERATION_YEAR = ccat.OPERATION_YEAR,
        cci.COMP_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CORP_TYPE' and CODE_VALUE1 = ccat.TYPE_OF_ENTERPRISE),
        cci.IS_TOP_500_1000_COM = ccat.IS_TOP_500_1000_COM,
        cci.IS_TOP_500_1000_BRANCH = ccat.IS_TOP_500_1000_BRANCH,
        cci.TOP_500_1000 = ccat.TOP_500_1000,
        cci.CIC_INFO = ccat.CIC_INFO,
        cci.CIC_CONSULTING_DATE = ccat.CIC_CONSULTING_DATE,
        cci.CAT_TYPE = ccat.CAT_TYPE_CODE;

exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE procedure update_sync_credit_app is
    l_last_sync date := sysdate;
    l_last_de_sync date := sysdate;
    l_last_de2_sync date := sysdate;
    l_ca_id CREDIT_APP_BPM.CREDIT_APP_ID%TYPE;
    l_cust_id number(13);
    l_identity_id int;
    l_cust_exist number(1) := 0;
    l_prod_id   int;
    l_count int;
    l_msg   varchar2(2000);
    l_use_identity varchar2(5) := 1;
    l_birthdate date := null;
    l_cust_name varchar2(200);
    l_province  int := 0;
    l_district  int := 0;
begin
    select LAST_SYNC into l_last_sync from CDC_SYNC_LOG where TABLE_NAME = 'DATA_ENTRY';
    select LAST_SYNC into l_last_de_sync from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_ENTRY';
    select LAST_SYNC into l_last_de2_sync from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_ENTRY_2';

    FOR i IN (select * from 
        (select 1 AS WF,decode(de.CURRENTTASK,'AbortProcess',1,'Abort process',1,'Reject at DC',2,'Reject at Approve',2,'Reject at DE1',2,'Reject at POS',2,'Done',3,4) order_by,
        4 AS order_by2,ID,SCHEMEPRODUCT,SCHEMEPRODUCTCODE,LOANAMOUNT,LOANTENOR,INTEREST,YEARINTEREST,LOANPURPOSE,LOANPURPOSEOTHER,GOODSPRICE,
        OWNEDCAPITAL,INSURANCE,NUMBEROFGOODS,TYPEOFGOODS,BRAND,BRANDOTHER,MODEL,SHOPNAME,SHOPCODE,SHOPADDRESS,CUSTOMERNAME,SHORTCUSTOMERNAME,OTHERCUSTOMERNAME,
        DOB,GENDER,MARITALSTATUS,EDUCATION,CITIZENID,ISSUEDATECITIZENID,ISSUEPLACECITIZENID,CITIZENIDOLD,MILITARYID,ISSUEDATEMILITARYID,ISSUEPLACEMILITARYID,
        EMAIL,MOBILEPHONE,PROFESSIONALSTATUS,POSITION,MILITARYRANK,COMPANYNAME,DEPARTMENT,COMPANYADDRESSSTRESS,COMPANYADDRESSWARD,COMPANYADDRESSWARDCODE,
        COMPANYADDRESSDISTRICT,COMPANYADDRESSDISTRICTCODE,COMPANYADDRESSPROVINCE,COMPANYADDRESSPROVINCECODE,OFFICENUMBER,COMPANYTAXNUMBER,CUSTOMERINCOME,
        TYPEOFLABOURCONTRACT,EXPERIENCEINYEAR,EXPERIENCEINMONTH,SALARYPAYMENTTYPE,PERMANENTRESIDENCE,PERMANENTRESIDENCEWARD,PERMANENTRESIDENCEWARDCODE,
        PERMANENTRESIDENCEDISTRICT,PERMANENTRESIDENCEDISTRICTCODE,PERMANENTRESIDENCEPROVINCE,PERMANENTRESIDENCEPROVINCECODE,FAMILYBOOKSERIESTYPE,FAMILYBOOKSERIES,
        ISSUEDATEFAMILYBOOK,ISSUEPLACEFAMILYBOOK,HOMEPHONE,TEMPORARYRESIDENCE,TEMPORARYADDRESS,TEMPORARYADDRESSWARD,TEMPORARYADDRESSWARDCODE,
        TEMPORARYADDRESSDISTRICT,TEMPORARYADDRESSDISTRICTCODE,TEMPORARYADDRESSPROVINCE,TEMPORARYADDRESSPROVINCECODE,LIVINGTIMEATTEMPADDINYEAR,
        LIVINGTIMEATTEMPADDINMONTH,ACCOMMODATIONTYPE,RELATIONSHIPWITHAPPLICANT,SPOUSENAME,SPOUSEDOB,SPOUSEIDNUMBER,SPOUSEMOBILEPHONE,NUMBEROFDEPENDANTS,
        CURRENTADDRESSSPOUSE,SPOUSEADDRESSDESCRIPTION,SPOUSEADDRESSDESCRIPTIONWARD,SPOUSEADDDESCWARDCODE,SPOUSEADDDESCDISTRICT,SPOUSEADDDESCDISTRICTCODE,
        SPOUSEADDDESCPROVINCE,SPOUSEADDDESCPROVINCECODE,INCOMESPOUSE,FULLNAME1,MOBILEPHONE1,RELATIONSHIPWITHBORROWER1,FULLNAME2,MOBILEPHONE2,
        RELATIONSHIPWITHBORROWER2,OTHERCREDITLOAN,AMOUNTOTHERCREDITLOAN,OTHERCREDITLOANSPOUSE,APPID,APPNUMBER,CONTRACTNUMBER,CREATEDDATE,CREATEDBY,
        LASTUPDATEDDATE,LASTUPDATEDBY,NOOFSERIES,NOOFFRAME,NOOFENGINE,POSCREDITCOMMENT,REPAYMENTDATE,DISBURSEMENTDATE,THEFIRSTPAYMENTDATE,LASTDEUPDATEDDATE,
        LASTDEUPDATEDBY,INSURANCECOMPANY,LOANAMOUNTAFTERINSURRANCE,TYPEOFGOODSOTHER,THELASTPAYMENTDATE,LDCODE,CURRENTTASK,ABORTPROCESSREASON,
        ABORTPROCESSCOMMENT,HASINSURRANCE,TYPEOFLOAN,DISBURSEMENTCHANNEL,DISBURSEMENTSTATUS,CHANNELNAME,ACCOUNTNUMBER,BANKNAME,ATBANK,SALENAME,SALECODE,
        SALEMOBILE,SPOUSECOMPANYNAME,SPOUSEPOSITION,SPOUSEINCOME,INSURRANCEFEE,LOANSTARTDATE,LOANENDDATE,SIGNCONTRACTDATE,INSURANCESTAFFID,
        AVERAGEELECTRICBILL,LIFEINSURANCECOMPANYNAME,INSURANCETERM,INSURANCETERMOTHER,INSURANCETERMFEE,FTREFERENCENUMBER,AVERAGEACCOUNTBALANCE,AGENCYCODE,
        TYPEOFPRODUCT,INSURANCERATE,INSURANCETYPE,PILOTNAME,MOBILEPHONE3,MOBILEPHONE4,REASONABORTFROMDE1,REASONABORTFROMDE1_LABEL,CUSTOMEREXPENSE,
        LAST_UPDATE_DATE,PRIMERATE,PRIMEAGENTINTEREST,null AS TYPEOFDOCPERSON,
        null AS COMPANYADDRESSDISTRICT_LABEL,
        null AS COMPANYADDRESSWARD_LABEL,
        null AS PERMANENTRESDISTRICT_LABEL,
        null AS PERMANENTRESIDENCEWARD_LABEL,
        null AS TEMPORARYADDRESSDISTRICT_LABEL,
        null AS TEMPORARYADDRESSWARD_LABEL,
        null AS SPOUSEADDDESCDISTRICT_LABEL,
        null AS SPOUSEADDDESCWARD_LABEL
        from CDB_DATA_ENTRY de where de.LAST_UPDATE_DATE >= (select LAST_SYNC from CDC_SYNC_LOG where TABLE_NAME = 'DATA_ENTRY') 
        and de.appID is not null and de.typeofLoan <> 'ConcentratingDataEntry'
        union all
        select 2 AS WF,decode(de1.CURRENTTASK,'Abort process',1,'Reject at DC',2,'Reject at Approve',2,'Reject at DE1',2,'Done',3,4) AS order_by,
        decode(de2.CURRENTTASK,'Abort process',1,'Reject at DC',2,'Reject at Approve',2,'Reject at DE1',2,'Done',3,4) AS order_by2,
        nvl(de2.ID,de1.ID) AS ID,nvl(de2.SCHEMEPRODUCT,de1.SCHEMEPRODUCT) AS SCHEMEPRODUCT,
        nvl(de2.SCHEMEPRODUCTCODE,de1.SCHEMEPRODUCTCODE) AS SCHEMEPRODUCTCODE,
        nvl(de2.LOANAMOUNT,de1.LOANAMOUNT) AS LOANAMOUNT,nvl(de2.LOANTENOR,de1.LOANTENOR) AS LOANTENOR,nvl(de2.INTEREST,de1.INTEREST) AS INTEREST,
        nvl(de2.YEARINTEREST,null) AS YEARINTEREST,nvl(de2.LOANPURPOSE,de1.LOANPURPOSE) AS LOANPURPOSE,nvl(de2.LOANPURPOSEOTHER,de1.LOANPURPOSEOTHER) AS LOANPURPOSEOTHER,
        nvl(de2.GOODSPRICE,de1.GOODSPRICE) AS GOODSPRICE,nvl(de2.OWNEDCAPITAL,de1.OWNEDCAPITAL) AS OWNEDCAPITAL,nvl(de2.INSURANCE,de1.INSURANCE) AS INSURANCE,
        null AS NUMBEROFGOODS,nvl(de2.TYPEOFGOODS,de1.TYPEOFGOODS) AS TYPEOFGOODS,null AS BRAND,null AS BRANDOTHER,nvl(de2.MODEL,de1.MODEL) AS MODEL,
        nvl(de2.SHOPNAME,de1.SHOPNAME) AS SHOPNAME,nvl(de2.SHOPCODE,de1.SHOPCODE) AS SHOPCODE,null AS SHOPADDRESS,nvl(de2.CUSTOMERNAME,de1.CUSTOMERNAME) AS CUSTOMERNAME,
        nvl(de2.SHORTCUSTOMERNAME,de1.SHORTCUSTOMERNAME) AS SHORTCUSTOMERNAME,nvl(de2.OTHERCUSTOMERNAME,de1.OTHERCUSTOMERNAME) AS OTHERCUSTOMERNAME,
        nvl(de2.DOB,de1.DOB) AS DOB,nvl(de2.GENDER,de1.GENDER) AS GENDER,nvl(de2.MARITALSTATUS,de1.MARITALSTATUS) AS MARITALSTATUS,
        nvl(de2.EDUCATION,de1.EDUCATION) AS EDUCATION,nvl(de2.CITIZENID,de1.CITIZENID) AS CITIZENID,
        nvl(de2.ISSUEDATECITIZENID,de1.ISSUEDATECITIZENID) AS ISSUEDATECITIZENID,nvl(de2.ISSUEPLACECITIZENID,de1.ISSUEPLACECITIZENID) AS ISSUEPLACECITIZENID,
        nvl(de2.CITIZENIDOLD,de1.CITIZENIDOLD) AS CITIZENIDOLD,nvl(de2.MILITARYID,de1.MILITARYID) AS MILITARYID,
        nvl(de2.ISSUEDATEMILITARYID,de1.ISSUEDATEMILITARYID) AS ISSUEDATEMILITARYID,nvl(de2.ISSUEPLACEMILITARYID,de1.ISSUEPLACEMILITARYID) AS ISSUEPLACEMILITARYID,
        nvl(de2.EMAIL,de1.EMAIL) AS EMAIL,nvl(de2.MOBILEPHONE,de1.MOBILEPHONE) AS MOBILEPHONE,nvl(de2.PROFESSIONALSTATUS,de1.PROFESSIONALSTATUS) AS PROFESSIONALSTATUS,
        nvl(de2.POSITION,de1.POSITION) AS POSITION,null AS MILITARYRANK,nvl(de2.COMPANYNAME,de1.COMPANYNAME) AS COMPANYNAME,
        nvl(de2.DEPARTMENT,de1.DEPARTMENT) AS DEPARTMENT,nvl(de2.COMPANYADDRESSSTRESS,de1.COMPANYADDRESSSTRESS) AS COMPANYADDRESSSTRESS,
        nvl(de2.COMPANYADDRESSWARD,de1.COMPANYADDRESSWARD) AS COMPANYADDRESSWARD,null AS COMPANYADDRESSWARDCODE,
        nvl(de2.COMPANYADDRESSDISTRICT,de1.COMPANYADDRESSDISTRICT) AS COMPANYADDRESSDISTRICT,null AS COMPANYADDRESSDISTRICTCODE,
        nvl(de2.COMPANYADDRESSPROVINCE,de1.COMPANYADDRESSPROVINCE) AS COMPANYADDRESSPROVINCE,null AS COMPANYADDRESSPROVINCECODE,
        nvl(de2.OFFICENUMBER,de1.OFFICENUMBER) AS OFFICENUMBER,nvl(de2.COMPANYTAXNUMBER,de1.COMPANYTAXNUMBER) AS COMPANYTAXNUMBER,
        nvl(de2.CUSTOMERINCOME,de1.CUSTOMERINCOME) AS CUSTOMERINCOME,nvl(de2.TYPEOFLABOURCONTRACT,de1.TYPEOFLABOURCONTRACT) AS TYPEOFLABOURCONTRACT,
        nvl(de2.EXPERIENCEINYEAR,de1.EXPERIENCEINYEAR) AS EXPERIENCEINYEAR,nvl(de2.EXPERIENCEINMONTH,de1.EXPERIENCEINMONTH) AS EXPERIENCEINMONTH,
        nvl(de2.SALARYPAYMENTTYPE,de1.SALARYPAYMENTTYPE) AS SALARYPAYMENTTYPE,nvl(de2.PERMANENTRESIDENCE,de1.PERMANENTRESIDENCE) AS PERMANENTRESIDENCE,
        nvl(de2.PERMANENTRESIDENCEWARD,de1.PERMANENTRESIDENCEWARD) AS PERMANENTRESIDENCEWARD,null AS PERMANENTRESIDENCEWARDCODE,
        nvl(de2.PERMANENTRESIDENCEDISTRICT,de1.PERMANENTRESIDENCEDISTRICT) AS PERMANENTRESIDENCEDISTRICT,null AS PERMANENTRESIDENCEDISTRICTCODE,
        nvl(de2.PERMANENTRESIDENCEPROVINCE,de1.PERMANENTRESIDENCEPROVINCE) AS PERMANENTRESIDENCEPROVINCE,null AS PERMANENTRESIDENCEPROVINCECODE,
        nvl(de2.FAMILYBOOKSERIESTYPE,de1.FAMILYBOOKSERIESTYPE) AS FAMILYBOOKSERIESTYPE,nvl(de2.FAMILYBOOKSERIES,de1.FAMILYBOOKSERIES) AS FAMILYBOOKSERIES,
        nvl(de2.ISSUEDATEFAMILYBOOK,de1.ISSUEDATEFAMILYBOOK) AS ISSUEDATEFAMILYBOOK,nvl(de2.ISSUEPLACEFAMILYBOOK,de1.ISSUEPLACEFAMILYBOOK) AS ISSUEPLACEFAMILYBOOK,
        nvl(de2.HOMEPHONE,de1.HOMEPHONE) AS HOMEPHONE,nvl(de2.TEMPORARYRESIDENCE,de1.TEMPORARYRESIDENCE) AS TEMPORARYRESIDENCE,
        nvl(de2.TEMPORARYADDRESS,de1.TEMPORARYADDRESS) AS TEMPORARYADDRESS,nvl(de2.TEMPORARYADDRESSWARD,de1.TEMPORARYADDRESSWARD) AS TEMPORARYADDRESSWARD,
        null AS TEMPORARYADDRESSWARDCODE,nvl(de2.TEMPORARYADDRESSDISTRICT,de1.TEMPORARYADDRESSDISTRICT) AS TEMPORARYADDRESSDISTRICT,
        null AS TEMPORARYADDRESSDISTRICTCODE,nvl(de2.TEMPORARYADDRESSPROVINCE,de1.TEMPORARYADDRESSPROVINCE) AS TEMPORARYADDRESSPROVINCE,
        null AS TEMPORARYADDRESSPROVINCECODE,nvl(de2.LIVINGTIMEATTEMPADDINYEAR,de1.LIVINGTIMEATTEMPADDINYEAR) AS LIVINGTIMEATTEMPADDINYEAR,
        nvl(de2.LIVINGTIMEATTEMPADDINMONTH,de1.LIVINGTIMEATTEMPADDINMONTH) AS LIVINGTIMEATTEMPADDINMONTH,
        nvl(de2.ACCOMMODATIONTYPE,de1.ACCOMMODATIONTYPE) AS ACCOMMODATIONTYPE,
        nvl(de2.RELATIONSHIPWITHAPPLICANT,de1.RELATIONSHIPWITHAPPLICANT) AS RELATIONSHIPWITHAPPLICANT,
        nvl(de2.SPOUSENAME,de1.SPOUSENAME) AS SPOUSENAME,nvl(de2.SPOUSEDOB,de1.SPOUSEDOB) AS SPOUSEDOB,
        nvl(de2.SPOUSEIDNUMBER,de1.SPOUSEIDNUMBER) AS SPOUSEIDNUMBER,nvl(de2.SPOUSEMOBILEPHONE,de1.SPOUSEMOBILEPHONE) AS SPOUSEMOBILEPHONE,
        nvl(de2.NUMBEROFDEPENDANTS,de1.NUMBEROFDEPENDANTS) AS NUMBEROFDEPENDANTS,nvl(de2.CURRENTADDRESSSPOUSE,de1.CURRENTADDRESSSPOUSE) AS CURRENTADDRESSSPOUSE,
        nvl(de2.SPOUSEADDRESSDESCRIPTION,de1.SPOUSEADDRESSDESCRIPTION) AS SPOUSEADDRESSDESCRIPTION,
        nvl(de2.SPOUSEADDRESSDESCRIPTIONWARD,de1.SPOUSEADDRESSDESCRIPTIONWARD) AS SPOUSEADDRESSDESCRIPTIONWARD,
        null AS SPOUSEADDDESCWARDCODE,nvl(de2.SPOUSEADDDESCDISTRICT,de1.SPOUSEADDDESCDISTRICT) AS SPOUSEADDDESCDISTRICT,
        null AS SPOUSEADDDESCDISTRICTCODE,nvl(de2.SPOUSEADDDESCPROVINCE,de1.SPOUSEADDDESCPROVINCE) AS SPOUSEADDDESCPROVINCE,
        null AS SPOUSEADDDESCPROVINCECODE,nvl(de2.INCOMESPOUSE,de1.INCOMESPOUSE) AS INCOMESPOUSE,
        nvl(de2.FULLNAME1,de1.FULLNAME1) AS FULLNAME1,nvl(de2.MOBILEPHONE1,de1.MOBILEPHONE1) AS MOBILEPHONE1,
        nvl(de2.RELATIONSHIPWITHBORROWER1,de1.RELATIONSHIPWITHBORROWER1) AS RELATIONSHIPWITHBORROWER1,
        nvl(de2.FULLNAME2,de1.FULLNAME2) AS FULLNAME2,nvl(de2.MOBILEPHONE2,de1.MOBILEPHONE2) AS MOBILEPHONE2,
        nvl(de2.RELATIONSHIPWITHBORROWER2,de1.RELATIONSHIPWITHBORROWER2) AS RELATIONSHIPWITHBORROWER2,
        nvl(de2.OTHERCREDITLOAN,de1.OTHERCREDITLOAN) AS OTHERCREDITLOAN,
        nvl(de2.AMOUNTOTHERCREDITLOAN,de1.AMOUNTOTHERCREDITLOAN) AS AMOUNTOTHERCREDITLOAN,
        nvl(de2.OTHERCREDITLOANSPOUSE,de1.OTHERCREDITLOANSPOUSE) AS OTHERCREDITLOANSPOUSE,
        nvl(de2.APPID,de1.APPID) AS APPID,nvl(de2.APPNUMBER,de1.APPNUMBER) AS APPNUMBER,nvl(de2.CONTRACTNUMBER,de1.CONTRACTNUMBER) AS CONTRACTNUMBER,
        nvl(de2.CREATEDDATE,de1.CREATEDDATE) AS CREATEDDATE,nvl(de2.CREATEDBY,de1.CREATEDBY) AS CREATEDBY,
        nvl(de2.LASTUPDATEDDATE,de1.LASTUPDATEDDATE) AS LASTUPDATEDDATE,nvl(de2.LASTUPDATEDBY,de1.LASTUPDATEDBY) AS LASTUPDATEDBY,
        null AS NOOFSERIES,nvl(de2.NOOFFRAME,de1.NOOFFRAME) AS NOOFFRAME,null AS NOOFENGINE,
        nvl(de2.POSCREDITCOMMENT,de1.POSCREDITCOMMENT) AS POSCREDITCOMMENT,nvl(de2.REPAYMENTDATE,de1.REPAYMENTDATE) AS REPAYMENTDATE,
        nvl(de2.DISBURSEMENTDATE,de1.DISBURSEMENTDATE) AS DISBURSEMENTDATE,nvl(de2.THEFIRSTPAYMENTDATE,de1.THEFIRSTPAYMENTDATE) AS THEFIRSTPAYMENTDATE,
        nvl(de2.LASTDEUPDATEDDATE,de1.LASTDEUPDATEDDATE) AS LASTDEUPDATEDDATE,nvl(de2.LASTDEUPDATEDBY,de1.LASTDEUPDATEDBY) AS LASTDEUPDATEDBY,
        nvl(de2.INSURANCECOMPANY,de1.INSURANCECOMPANY) AS INSURANCECOMPANY,
        nvl(de2.LOANAMOUNTAFTERINSURRANCE,de1.LOANAMOUNTAFTERINSURRANCE) AS LOANAMOUNTAFTERINSURRANCE,
        nvl(de2.TYPEOFGOODSOTHER,de1.TYPEOFGOODSOTHER) AS TYPEOFGOODSOTHER,nvl(de2.THELASTPAYMENTDATE,de1.THELASTPAYMENTDATE) AS THELASTPAYMENTDATE,
        nvl(de2.LDCODE,de1.LDCODE) AS LDCODE,nvl(de2.CURRENTTASK,de1.CURRENTTASK) AS CURRENTTASK,
        nvl(de2.ABORTPROCESSREASON,de1.ABORTPROCESSREASON) AS ABORTPROCESSREASON,nvl(de2.ABORTPROCESSCOMMENT,de1.ABORTPROCESSCOMMENT) AS ABORTPROCESSCOMMENT,
        nvl(de2.HASINSURRANCE,de1.HASINSURRANCE) AS HASINSURRANCE,nvl(de2.TYPEOFLOAN,de1.TYPEOFLOAN) AS TYPEOFLOAN,
        nvl(de2.DISBURSEMENTCHANNEL,de1.DISBURSEMENTCHANNEL) AS DISBURSEMENTCHANNEL,nvl(de2.DISBURSEMENTSTATUS,de1.DISBURSEMENTSTATUS) AS DISBURSEMENTSTATUS,
        nvl(de2.CHANNELNAME,de1.CHANNELNAME) AS CHANNELNAME,nvl(de2.ACCOUNTNUMBER,de1.ACCOUNTNUMBER) AS ACCOUNTNUMBER,
        nvl(de2.BANKNAME,de1.BANKNAME) AS BANKNAME,nvl(de2.ATBANK,de1.ATBANK) AS ATBANK,nvl(de2.SALENAME,de1.SALENAME) AS SALENAME,
        nvl(de2.SALECODE,de1.SALECODE) AS SALECODE,nvl(de2.SALEMOBILE,de1.SALEMOBILE) AS SALEMOBILE,
        nvl(de2.SPOUSECOMPANYNAME,de1.SPOUSECOMPANYNAME) AS SPOUSECOMPANYNAME,nvl(de2.SPOUSEPOSITION,de1.SPOUSEPOSITION) AS SPOUSEPOSITION,
        nvl(de2.INCOMESPOUSE,de1.INCOMESPOUSE) AS SPOUSEINCOME,nvl(de2.INSURRANCEFEE,de1.INSURRANCEFEE) AS INSURRANCEFEE,
        nvl(de2.LOANSTARTDATE,de1.LOANSTARTDATE) AS LOANSTARTDATE,nvl(de2.LOANENDDATE,de1.LOANENDDATE) AS LOANENDDATE,
        nvl(de2.SIGNCONTRACTDATE,de1.SIGNCONTRACTDATE) AS SIGNCONTRACTDATE,nvl(de2.INSURANCESTAFFID,de1.INSURANCESTAFFID) AS INSURANCESTAFFID,
        nvl(de2.AVERAGEELECTRICBILL,de1.AVERAGEELECTRICBILL) AS AVERAGEELECTRICBILL,nvl(de2.LIFEINSURANCECOMPANYNAME,de1.LIFEINSURANCECOMPANYNAME) AS LIFEINSURANCECOMPANYNAME,
        nvl(de2.INSURANCETERM,de1.INSURANCETERM) AS INSURANCETERM,nvl(de2.INSURANCETERMOTHER,de1.INSURANCETERMOTHER) AS INSURANCETERMOTHER,
        nvl(de2.INSURANCETERMFEE,de1.INSURANCETERMFEE) AS INSURANCETERMFEE,nvl(de2.FTREFERENCENUMBER,de1.FTREFERENCENUMBER) AS FTREFERENCENUMBER,
        nvl(de2.AVERAGEACCOUNTBALANCE,de1.AVERAGEACCOUNTBALANCE) AS AVERAGEACCOUNTBALANCE,null AS AGENCYCODE,
        nvl(de2.TYPEOFPRODUCT,de1.TYPEOFPRODUCT) AS TYPEOFPRODUCT,nvl(de2.INSURANCERATE,de1.INSURANCERATE) AS INSURANCERATE,
        nvl(de2.INSURANCETYPE,de1.INSURANCETYPE) AS INSURANCETYPE,nvl(de2.PILOTNAME,de1.PILOTNAME) AS PILOTNAME,
        null AS MOBILEPHONE3,null AS MOBILEPHONE4,null AS REASONABORTFROMDE1,null AS REASONABORTFROMDE1_LABEL,null AS CUSTOMEREXPENSE,
        nvl(de2.LAST_UPDATE_DATE,de1.LAST_UPDATE_DATE) AS LAST_UPDATE_DATE,null AS PRIMERATE,null AS PRIMEAGENTINTEREST,
        nvl(de2.TYPEOFDOCPERSON,de1.TYPEOFDOCPERSON) AS TYPEOFDOCPERSON,
        nvl(de2.COMPANYADDRESSDISTRICT_LABEL, de1.COMPANYADDRESSDISTRICT_LABEL) AS COMPANYADDRESSDISTRICT_LABEL,
        nvl(de2.COMPANYADDRESSWARD_LABEL, de1.COMPANYADDRESSWARD_LABEL) AS COMPANYADDRESSWARD_LABEL,
        nvl(de2.PERMANENTRESDISTRICT_LABEL, de1.PERMANENTRESDISTRICT_LABEL) AS PERMANENTRESDISTRICT_LABEL,
        nvl(de2.PERMANENTRESIDENCEWARD_LABEL, de1.PERMANENTRESIDENCEWARD_LABEL) AS PERMANENTRESIDENCEWARD_LABEL,
        nvl(de2.TEMPORARYADDRESSDISTRICT_LABEL, de1.TEMPORARYADDRESSDISTRICT_LABEL) AS TEMPORARYADDRESSDISTRICT_LABEL,
        nvl(de2.TEMPORARYADDRESSWARD_LABEL, de1.TEMPORARYADDRESSWARD_LABEL) AS TEMPORARYADDRESSWARD_LABEL,
        nvl(de2.SPOUSEADDDESCDISTRICT_LABEL, de1.SPOUSEADDDESCDISTRICT_LABEL) AS SPOUSEADDDESCDISTRICT_LABEL,
        nvl(de2.SPOUSEADDDESCWARD_LABEL, de1.SPOUSEADDDESCWARD_LABEL) AS SPOUSEADDDESCWARD_LABEL
        from (select * from DE_DATA_ENTRY where LAST_UPDATE_DATE >= (select LAST_SYNC from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_ENTRY')
            and appID is not null) de1 
        full join (select * from DE_DATA_ENTRY_2 where LAST_UPDATE_DATE >= (select LAST_SYNC from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_ENTRY_2')
            and appID is not null) de2 on de1.APPID = de2.APPID) cde
        order by decode(order_by2,4,order_by,order_by2), ID)
    LOOP
        -- check credit app
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - check credit app';
        begin
            l_ca_id := 0;
            select CREDIT_APP_ID into l_ca_id from CREDIT_APP_BPM where BPM_APP_ID = i.appID;
        exception
            when no_data_found then
                l_ca_id := 0;
            when others then
                raise;
        end;
        -- check customer
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - check customer';
        begin
            l_cust_id := 0;
            begin
                select ID into l_cust_id from CUST_PERSONAL_INFO where MC_CUST_CODE = get_md5(nvl(i.citizenID,i.MILITARYID), i.DOB);
            exception
                when no_data_found then
                    l_cust_id := 0;
            end;
            if (i.DOB is not null) and (l_cust_id = 0) then
                begin
                    l_cust_name := substr(nvl(i.customerName,'$$$$'),1,trunc(length(nvl(i.customerName,'$$$$'))/2))||'%';
                    select distinct cpi.ID,cpi.BIRTH_DATE into l_cust_id,l_birthdate from CUST_PERSONAL_INFO cpi 
                        inner join CUST_IDENTITY ci on cpi.ID = ci.CUST_ID
                    where ci.IDENTITY_NUMBER in (nvl(i.citizenID,'$$$'), nvl(i.MILITARYID,'$$$'))
                    and CUST_NAME like l_cust_name;
                    IF l_birthdate is null THEN
                        update CUST_PERSONAL_INFO set
                            birth_date = i.DOB,
                            MC_CUST_CODE = get_md5(nvl(i.citizenID,i.MILITARYID), i.DOB)
                        where ID = l_cust_id;
                    ELSE
                        IF trunc(l_birthdate) <> trunc(i.DOB) THEN
                            l_cust_id := 0;
                        END IF;
                    END IF;
                exception
                    when others then
                        l_cust_id := 0;
                end;
            end if;
        exception
            when no_data_found then
                l_cust_id := 0;
            when others then
                raise;
        end;

        IF l_cust_id > 0 THEN
            l_cust_exist := 1;
        ELSE
            l_cust_exist := 0;
            select SEQ_CUST_PERSONAL_INFO_ID.nextval into l_cust_id from dual;
        END IF;

        --l_msg := l_msg||';l_cust_id='||l_cust_id;
        
        -- Update/Insert CUST_IDENTITY
        -- Update CMT
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update CMT';
        MERGE INTO CUST_IDENTITY ci USING
         (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
            union all
          select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) idnum
        ON (ci.CUST_ID = l_cust_id and ci.IDENTITY_TYPE_ID = (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '1'))
        WHEN NOT MATCHED THEN 
        INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,IDENTITY_NUMBER,
            IDENTITY_ISSUE_DATE,IDENTITY_EXPIRY_DATE,IDENTITY_TYPE_ID,IDENTITY_ISSUE_PLACE,IDENTITY_ISSUE_PLACE_TEXT)
        VALUES (
            l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), 
            substr(i.lastUpdatedBy,1,30), nvl(i.citizenID,'NO_ID'), i.issueDateCitizenID, null,
            (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '1'),
            nvl((select ID from CODE_TABLE where CATEGORY = 'IDPLACE' and DESCRIPTION1 = i.issuePlaceCitizenID),
                (select ID from CODE_TABLE where CATEGORY = 'IDPLACE' and CODE_VALUE1 = i.issuePlaceCitizenID)),
            i.issuePlaceCitizenID)
        WHEN MATCHED THEN
        UPDATE SET 
            ci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
            ci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
            ci.IDENTITY_NUMBER = nvl(i.citizenID,'NO_ID'),
            ci.IDENTITY_ISSUE_DATE = i.issueDateCitizenID,
            ci.IDENTITY_EXPIRY_DATE = null,
            ci.IDENTITY_ISSUE_PLACE = nvl((select ID from CODE_TABLE where CATEGORY = 'IDPLACE' and DESCRIPTION1 = i.issuePlaceCitizenID),
                (select ID from CODE_TABLE where CATEGORY = 'IDPLACE' and CODE_VALUE1 = i.issuePlaceCitizenID)),
            ci.IDENTITY_ISSUE_PLACE_TEXT = i.issuePlaceCitizenID;

        -- Update CMT Quan doi
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update CMTQD';
        MERGE INTO CUST_IDENTITY ci USING
         (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId) and trim(MILITARYID) is not null
            union all
          select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId) and trim(MILITARYID) is not null) idnum
        ON (ci.CUST_ID = l_cust_id and ci.IDENTITY_TYPE_ID = (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '2'))
        WHEN NOT MATCHED THEN 
        INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,IDENTITY_NUMBER,
            IDENTITY_ISSUE_DATE,IDENTITY_EXPIRY_DATE,IDENTITY_TYPE_ID,IDENTITY_ISSUE_PLACE,IDENTITY_ISSUE_PLACE_TEXT)
        VALUES (
            l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), 
            substr(i.lastUpdatedBy,1,30), nvl(i.MILITARYID,'NO_ID'), i.ISSUEDATEMILITARYID, null,
            (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '2'),
            (select ID from CODE_TABLE where CATEGORY = 'IDPLACE' and CODE_VALUE1 = i.ISSUEPLACEMILITARYID),
            i.ISSUEPLACEMILITARYID)
        WHEN MATCHED THEN
        UPDATE SET 
            ci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
            ci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
            ci.IDENTITY_NUMBER = nvl(i.MILITARYID,'NO_ID'),
            ci.IDENTITY_ISSUE_DATE = i.ISSUEDATEMILITARYID,
            ci.IDENTITY_EXPIRY_DATE = null,
            ci.IDENTITY_ISSUE_PLACE = (select ID from CODE_TABLE where CATEGORY = 'IDPLACE' and CODE_VALUE1 = i.ISSUEPLACEMILITARYID),
            ci.IDENTITY_ISSUE_PLACE_TEXT = i.ISSUEPLACEMILITARYID;
            
        select nvl(trim(i.TYPEOFDOCPERSON),'1') into l_use_identity from dual;
        
        -- Get default identity
        begin
            IF l_use_identity = '1' THEN
                select ID into l_identity_id from CUST_IDENTITY where CUST_ID = l_cust_id
                    and IDENTITY_TYPE_ID = (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '1');
            ELSIF l_use_identity = '2' THEN
                select ID into l_identity_id from CUST_IDENTITY where CUST_ID = l_cust_id
                    and IDENTITY_TYPE_ID = (select ID from CODE_TABLE where CATEGORY = 'IDTYP' and CODE_VALUE1 = '2');
            END IF;
        exception
            when others then
                l_identity_id := 0;
        end;

        -- get province id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.permanentResidenceProvince),
                            (select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and DESCRIPTION2 = i.permanentResidenceProvince))
                into l_province from dual;
        exception
            when others then
                l_province := 0;
        end;
        -- get district id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.permanentResidenceDistrict),
                    (select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and DESCRIPTION2 = i.permanentResidenceDistrict))
              into l_district from dual;
        exception
            when others then
                l_district := 0;
        end;

        -- Update PERMANENT address
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update PERMANENT address';
        MERGE INTO CUST_ADDR_INFO cai USING
         (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
            union all
          select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) addr
        ON (cai.CUST_ID = l_cust_id and ADDR_ORDER = 1
            and cai.ADDR_TYPE = (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'PERMANENT'))
        WHEN NOT MATCHED THEN 
        INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,
            ADDR_TYPE,ADDR_ORDER,ADDRESS,PROVINCE,DISTRICT,WARD,DISTRICT_ADDR,WARD_ADDR)
        VALUES (
            l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
            (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'PERMANENT'), 1, i.permanentResidence,
            l_province,l_district,
            nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.permanentResidenceWard),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and DESCRIPTION2 = i.permanentResidenceWard)),
                get_string(decode(i.WF,1,i.permanentResidenceDistrict,i.PERMANENTRESDISTRICT_LABEL),50), 
                get_string(decode(i.WF,1,i.permanentResidenceWard,i.PERMANENTRESIDENCEWARD_LABEL), 50))
        WHEN MATCHED THEN
        UPDATE SET 
            cai.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
            cai.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
            cai.ADDRESS = i.permanentResidence,
            cai.PROVINCE = l_province,
            cai.DISTRICT = l_district,
            cai.WARD = nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.permanentResidenceWard),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and DESCRIPTION2 = i.permanentResidenceWard)),
            cai.DISTRICT_ADDR = get_string(decode(i.WF,1,i.permanentResidenceDistrict,i.PERMANENTRESDISTRICT_LABEL),50),
            cai.WARD_ADDR = get_string(decode(i.WF,1,i.permanentResidenceWard,i.PERMANENTRESIDENCEWARD_LABEL),50);
        -- get province id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.temporaryAddressProvince),
                            (select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and DESCRIPTION2 = i.temporaryAddressProvince))
                into l_province from dual;
        exception
            when others then
                l_province := 0;
        end;
        -- get district id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.temporaryAddressDistrict),
                    (select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and DESCRIPTION2 = i.temporaryAddressDistrict))
              into l_district from dual;
        exception
            when others then
                l_district := 0;
        end;

        -- Update TEMPORARY address
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update TEMPORARY address';
        MERGE INTO CUST_ADDR_INFO cai USING
         (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
            union all
          select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) addr
        ON (cai.CUST_ID = l_cust_id and ADDR_ORDER = 1
            and cai.ADDR_TYPE = (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'TEMPORARY'))
        WHEN NOT MATCHED THEN 
        INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,
            ADDR_TYPE,ADDR_ORDER,ADDRESS,PROVINCE,DISTRICT,WARD,DISTRICT_ADDR,WARD_ADDR)
        VALUES (
            l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
            (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'TEMPORARY'), 1, i.temporaryAddress,
            l_province,l_district,            
            nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.temporaryAddressWard),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and DESCRIPTION2 = i.temporaryAddressWard)),
                get_string(decode(i.WF,1,i.temporaryAddressDistrict,i.TEMPORARYADDRESSDISTRICT_LABEL),50), 
                get_string(decode(i.WF,1,i.temporaryAddressWard,i.TEMPORARYADDRESSWARD_LABEL), 50))
        WHEN MATCHED THEN
        UPDATE SET 
            cai.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
            cai.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
            cai.ADDRESS = i.temporaryAddress,
            cai.PROVINCE = l_province,
            cai.DISTRICT = l_district,
            cai.WARD = nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.temporaryAddressWard),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and DESCRIPTION2 = i.temporaryAddressWard)),
            cai.DISTRICT_ADDR = get_string(decode(i.WF,1,i.temporaryAddressDistrict,i.TEMPORARYADDRESSDISTRICT_LABEL),50),
            cai.WARD_ADDR = get_string(decode(i.WF,1,i.temporaryAddressWard,i.TEMPORARYADDRESSWARD_LABEL), 50);

        -- get province id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.SPOUSEADDDESCPROVINCE),
                            (select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and DESCRIPTION2 = i.SPOUSEADDDESCPROVINCE))
                into l_province from dual;
        exception
            when others then
                l_province := 0;
        end;
        -- get district id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.SPOUSEADDDESCDISTRICT),
                    (select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and DESCRIPTION2 = i.SPOUSEADDDESCDISTRICT))
              into l_district from dual;
        exception
            when others then
                l_district := 0;
        end;

        -- Update SPOUSE address
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update SPOUSE address';
        MERGE INTO CUST_ADDR_INFO cai USING
         (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
            union all
          select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) addr
        ON (cai.CUST_ID = l_cust_id and ADDR_ORDER = 1
            and cai.ADDR_TYPE = (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'SPOUSE'))
        WHEN NOT MATCHED THEN 
        INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,
            ADDR_TYPE,ADDR_ORDER,ADDRESS,PROVINCE,DISTRICT,WARD,DISTRICT_ADDR,WARD_ADDR)
        VALUES (
            l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
            (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'SPOUSE'), 1, i.spouseAddressDescription,
            l_province,l_district,
            nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.SPOUSEADDRESSDESCRIPTIONWARD),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and DESCRIPTION2 = i.SPOUSEADDRESSDESCRIPTIONWARD)),
                get_string(decode(i.WF,1,i.SPOUSEADDDESCDISTRICT,i.SPOUSEADDDESCDISTRICT_LABEL),50), 
                get_string(decode(i.WF,1,i.SPOUSEADDRESSDESCRIPTIONWARD,i.SPOUSEADDDESCWARD_LABEL), 50))
        WHEN MATCHED THEN
        UPDATE SET 
            cai.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
            cai.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
            cai.ADDRESS = i.spouseAddressDescription,
            cai.PROVINCE = l_province,
            cai.DISTRICT = l_district,
            cai.WARD = nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.SPOUSEADDRESSDESCRIPTIONWARD),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                and DESCRIPTION2 = i.SPOUSEADDRESSDESCRIPTIONWARD)),
            cai.DISTRICT_ADDR = get_string(decode(i.WF,1,i.SPOUSEADDDESCDISTRICT,i.SPOUSEADDDESCDISTRICT_LABEL),50),
            cai.WARD_ADDR = get_string(decode(i.WF,1,i.SPOUSEADDRESSDESCRIPTIONWARD,i.SPOUSEADDDESCWARD_LABEL), 50);

        -- Update SPOUSE_CURRENT address
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update SPOUSE_CURRENT address';
        MERGE INTO CUST_ADDR_INFO cai USING
         (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
            union all
          select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) addr
        ON (cai.CUST_ID = l_cust_id and ADDR_ORDER = 1
            and cai.ADDR_TYPE = (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'SPOUSE_CURRENT'))
        WHEN NOT MATCHED THEN 
        INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,
            ADDR_TYPE,ADDR_ORDER,ADDRESS,PROVINCE,DISTRICT,WARD)
        VALUES (
            l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
            (select ID from CODE_TABLE where CATEGORY = 'ADDR_TYPE' and CODE_VALUE1 = 'SPOUSE_CURRENT'), 1, i.currentAddressSpouse,
            null, null, null)
        WHEN MATCHED THEN
        UPDATE SET 
            cai.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
            cai.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
            cai.ADDRESS = i.currentAddressSpouse;

        -- Update CUSTOMER EMAIL address
        IF i.email is not null THEN
            l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update CUSTOMER EMAIL';
            MERGE INTO CUST_CONTACT_INFO cci USING
             (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
                union all
              select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) cont
            ON (cci.CUST_ID = l_cust_id and CONTACT_PRIORITY = 1
                and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'CUSTOMER')
                and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'EMAIL'))
            WHEN NOT MATCHED THEN 
            INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,CONTACT_TYPE,
                CONTACT_CATEGORY,CONTACT_PRIORITY,CONTACT_VALUE)
            VALUES (
                l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'EMAIL'),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'CUSTOMER'),
                1, i.email)
            WHEN MATCHED THEN
            UPDATE SET 
                cci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                cci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
                cci.CONTACT_VALUE = i.email;
        END IF;

        -- Update CUSTOMER MOBILE number
        IF i.mobilePhone is not null THEN
            l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update CUSTOMER MOBILE';
            MERGE INTO CUST_CONTACT_INFO cci USING
             (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
                union all
              select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) cont
            ON (cci.CUST_ID = l_cust_id and CONTACT_PRIORITY = 1
                and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'CUSTOMER')
                and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'))
            WHEN NOT MATCHED THEN 
            INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,CONTACT_TYPE,
                CONTACT_CATEGORY,CONTACT_PRIORITY,CONTACT_VALUE)
            VALUES (
                l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'CUSTOMER'),
                1, i.mobilePhone)
            WHEN MATCHED THEN
            UPDATE SET 
                cci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                cci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
                cci.CONTACT_VALUE = i.mobilePhone;
        END IF;

        -- Update SPOUSE CUSTOMER MOBILE number
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update SPOUSE CUSTOMER MOBILE';
        IF i.SPOUSEMOBILEPHONE is not null THEN
            MERGE INTO CUST_CONTACT_INFO cci USING
             (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
                union all
              select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) cont
            ON (cci.CUST_ID = l_cust_id and CONTACT_PRIORITY = 1
                and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'SPOUSE_CUST')
                and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'))
            WHEN NOT MATCHED THEN 
            INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,CONTACT_TYPE,
                CONTACT_CATEGORY,CONTACT_PRIORITY,CONTACT_VALUE)
            VALUES (
                l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'SPOUSE_CUST'),
                1, i.SPOUSEMOBILEPHONE)
            WHEN MATCHED THEN
            UPDATE SET 
                cci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                cci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
                cci.CONTACT_VALUE = i.SPOUSEMOBILEPHONE;
        END IF;

        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update REF CUSTOMER MOBILE';
        -- Update REF CUSTOMER MOBILE number 1
        IF i.mobilePhone1 is not null THEN
            MERGE INTO CUST_CONTACT_INFO cci USING
             (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
                union all
              select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) cont
            ON (cci.CUST_ID = l_cust_id and CONTACT_PRIORITY = 1
                and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST')
                and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'))
            WHEN NOT MATCHED THEN 
            INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,CONTACT_TYPE,
                CONTACT_CATEGORY,CONTACT_PRIORITY,CONTACT_VALUE)
            VALUES (
                l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST'),
                1, i.mobilePhone1)
            WHEN MATCHED THEN
            UPDATE SET 
                cci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                cci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
                cci.CONTACT_VALUE = i.mobilePhone1;
        END IF;

        -- Update REF CUSTOMER MOBILE number 2
        IF i.mobilePhone2 is not null THEN
            MERGE INTO CUST_CONTACT_INFO cci USING
             (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
                union all
              select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) cont
            ON (cci.CUST_ID = l_cust_id and CONTACT_PRIORITY = 2
                and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST')
                and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'))
            WHEN NOT MATCHED THEN 
            INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,CONTACT_TYPE,
                CONTACT_CATEGORY,CONTACT_PRIORITY,CONTACT_VALUE)
            VALUES (
                l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST'),
                2, i.mobilePhone2)
            WHEN MATCHED THEN
            UPDATE SET 
                cci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                cci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
                cci.CONTACT_VALUE = i.mobilePhone2;
        END IF;

        -- Update REF CUSTOMER MOBILE number 3
        IF i.mobilePhone3 is not null THEN
            MERGE INTO CUST_CONTACT_INFO cci USING
             (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
                union all
              select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) cont
            ON (cci.CUST_ID = l_cust_id and CONTACT_PRIORITY = 3
                and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST')
                and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'))
            WHEN NOT MATCHED THEN 
            INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,CONTACT_TYPE,
                CONTACT_CATEGORY,CONTACT_PRIORITY,CONTACT_VALUE)
            VALUES (
                l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST'),
                3, i.mobilePhone3)
            WHEN MATCHED THEN
            UPDATE SET 
                cci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                cci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
                cci.CONTACT_VALUE = i.mobilePhone3;
        END IF;

        -- Update REF CUSTOMER MOBILE number 4
        IF i.mobilePhone4 is not null THEN
            MERGE INTO CUST_CONTACT_INFO cci USING
             (select Id,appId,appNumber from CDB_DATA_ENTRY cde where cde.appId = decode(i.WF,2,'$$$',i.appId)
                union all
              select Id,appId,appNumber from DE_DATA_ENTRY de1 where de1.appId = decode(i.WF,1,'$$$',i.appId)) cont
            ON (cci.CUST_ID = l_cust_id and CONTACT_PRIORITY = 4
                and cci.CONTACT_CATEGORY = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST')
                and cci.CONTACT_TYPE = (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'))
            WHEN NOT MATCHED THEN 
            INSERT (CUST_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,CONTACT_TYPE,
                CONTACT_CATEGORY,CONTACT_PRIORITY,CONTACT_VALUE)
            VALUES (
                l_cust_id, i.createdDate, nvl(i.lastUpdatedDate,sysdate), substr(i.createdBy,1,30), substr(i.lastUpdatedBy,1,30),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_TYP' and CODE_VALUE1 = 'MOBILE'),
                (select ID from CODE_TABLE where CATEGORY = 'CONTAC_CAT' and CODE_VALUE1 = 'RELATED_CUST'),
                4, i.mobilePhone4)
            WHEN MATCHED THEN
            UPDATE SET 
                cci.LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                cci.LAST_UPDATED_BY = substr(i.lastUpdatedBy,1,30),
                cci.CONTACT_VALUE = i.mobilePhone4;
        END IF;

        -- get province id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.companyAddressProvince),
                            (select ID from CODE_TABLE where CATEGORY = 'PROVINCE' and DESCRIPTION2 = i.companyAddressProvince))
                into l_province from dual;
        exception
            when others then
                l_province := 0;
        end;
        -- get district id
        begin
            select nvl((select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.companyAddressDistrict),
                    (select ID from CODE_TABLE where CATEGORY = 'DISTRICT' and parent_id = decode(l_province,0,parent_id,l_province)
                and DESCRIPTION2 = i.companyAddressDistrict))
              into l_district from dual;
        exception
            when others then
                l_district := 0;
        end;

        -- Insert/update customer
        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update CUSTOMER info';
        IF l_cust_exist > 0 THEN
        --l_msg := l_msg||';update;cust_name='||nvl(i.customerName,'INVALID NAME');
            update CUST_PERSONAL_INFO set
                cust_name = nvl(i.customerName,'INVALID NAME'),
                short_cust_name = i.shortCustomerName,
                other_cust_name = i.otherCustomerName,
                birth_date = i.DOB,
                gender = nvl((select ID from CODE_TABLE where CATEGORY = 'GENDER' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.gender),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_GENDER' and DESCRIPTION2 = i.gender)),
                marital_status = nvl((select ID from CODE_TABLE where CATEGORY = 'MARITAL' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.maritalStatus),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_MARITAL' and DESCRIPTION2 = i.maritalStatus)),
                household_reg_type_id = nvl((select ID from CODE_TABLE where CATEGORY = 'HHREG_TYPE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.familyBookSeriesType),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_HHREG_TYPE' and DESCRIPTION2 = i.familyBookSeriesType)),
                household_reg_number = i.familyBookSeries,
                household_reg_issue_date = i.issueDateFamilyBook,
                household_reg_issue_place = i.issuePlaceFamilyBook,
                home_phone = i.homePhone,
                last_updated_date = nvl(i.lastUpdatedDate,sysdate),
                last_updated_by = substr(i.lastUpdatedBy,1,30),
                IDENTITY_ID = l_identity_id
            where ID = l_cust_id;
            
            update CUST_ADDL_INFO set
                education = nvl((select ID from CODE_TABLE where CATEGORY = 'EDUCATION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.education),
                    (select ID from CODE_TABLE where CATEGORY = 'EDUCATION' and DESCRIPTION2 = i.education)),
                old_identity_number = i.citizenIdOld,
                military_id = i.militaryId,
                military_issue_date = i.issueDateMilitaryId,
                military_issue_place = i.issuePlaceMilitaryId,
                professional = nvl((select ID from CODE_TABLE where CATEGORY = 'PROFESSION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.professionalStatus),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_PROFESSION' and DESCRIPTION2 = i.professionalStatus)),
                position_in_comp = nvl((select ID from CODE_TABLE where CATEGORY = 'POSITION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.position),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_POSITION' and DESCRIPTION2 = i.position)),
                military_Rank = i.militaryRank,
                company_Name = i.companyName,
                department = substr(i.department,1,200),
                company_Addr = i.companyAddressStress,
                ward_id = nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                        and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.companyAddressWard),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                        and DESCRIPTION2 = i.companyAddressWard)),
                company_ward = decode(i.WF,1,i.companyAddressWard,i.COMPANYADDRESSWARD_LABEL),
                district_id = l_district,
                company_district = decode(i.WF,1,i.companyAddressDistrict,i.COMPANYADDRESSDISTRICT_LABEL),
                province_id = l_province,
                office_phone = i.officeNumber,
                company_Tax = i.companyTaxNumber,
                labour_contract_type = nvl((select ID from CODE_TABLE where CATEGORY = 'LAB_CONT' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.typeOfLabourContract),
                    (select ID from CODE_TABLE where CATEGORY = 'LAB_CONT' and DESCRIPTION2 = i.typeOfLabourContract)),
                year_experience = i.experienceInYear,
                month_experience = i.experienceInMonth,
                payroll_method = nvl((select ID from CODE_TABLE where CATEGORY = 'PAYROLL_M' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.salaryPaymentType),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_PAYROLL_M' and DESCRIPTION2 = i.salaryPaymentType)),
                temp_same_perm_addr = nvl((select ID from CODE_TABLE where CATEGORY = 'TMP_RES' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.temporaryResidence),
                    (select ID from CODE_TABLE where CATEGORY = 'TMP_RES' and DESCRIPTION2 = i.temporaryResidence)),
                lifetime_in_year = i.LIVINGTIMEATTEMPADDINYEAR,
                lifetime_in_month = i.LIVINGTIMEATTEMPADDINMONTH,
                accommodation_type = nvl((select ID from CODE_TABLE where CATEGORY = 'ACCOM_TYPE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.accommodationType),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_ACCOM_TYPE' and DESCRIPTION2 = i.accommodationType)),
                relation_spouse = nvl((select ID from CODE_TABLE where CATEGORY = 'RELAT_SPOU' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.relationshipWithApplicant),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_RELAT_SPOU' and DESCRIPTION2 = i.relationshipWithApplicant)),
                spouse_name = substr(i.spouseName,1,120),
                spouse_DOB = i.spouseDOB,
                spouse_identity_number = i.spouseIDNumber,
                spouse_mobile = i.spouseMobilePhone,
                number_of_dependants = i.numberOfDependants,
                income_spouse = i.incomeSpouse,
                ref_full_name1 = i.fullName1,
                ref_person1_mobile = i.mobilePhone1,
                relation_ref_person1 = nvl((select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.relationshipWithBorrower1),
                    (select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and DESCRIPTION2 = i.relationshipWithBorrower1)),
                ref_full_name2 = i.fullName2,
                ref_person2_mobile = i.mobilePhone2,
                relation_ref_person2 = nvl((select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.relationshipWithBorrower2),
                    (select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and DESCRIPTION2 = i.relationshipWithBorrower2)),
                spouse_company_name = substr(i.spouseCompanyName,255),
                spouse_position = nvl((select ID from CODE_TABLE where CATEGORY = 'POSITION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.spousePosition),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_POSITION' and DESCRIPTION2 = i.spousePosition)),
                last_updated_date = nvl(i.lastUpdatedDate,sysdate),
                last_updated_by = substr(i.lastUpdatedBy,1,30)
            where CUST_ID = l_cust_id;
            
            update CUST_FINANCIAL_INFO set
                cust_income = i.customerIncome,
                credit_in_other_bank = i.otherCreditLoan,
                payment_amount_at_bank = i.amountOtherCreditLoan,
                spouse_credit_in_other_bank = i.otherCreditLoanSpouse,
                account_number_at_bank = substr(trim(i.accountNumber),1,50),
                bank_name = i.bankName,
                bank_branch = i.atBank,
                spouse_Income = i.spouseIncome,
                avg_electric_bill = i.averageElectricBill,
                life_insu_company_id = (select ID from CODE_TABLE where CATEGORY = 'LINS_COMP' and CODE_VALUE1 = i.lifeInsuranceCompanyName),
                insu_term = (select ID from CODE_TABLE where CATEGORY = 'INSU_TERM' and CODE_VALUE1 = i.insuranceTerm),
                insu_term_other = i.insuranceTermOther,
                insu_term_fee = i.insuranceTermFee,
                avg_account_bal = i.averageAccountBalance,
                last_updated_date = nvl(i.lastUpdatedDate,sysdate),
                last_updated_by = substr(i.lastUpdatedBy,1,30),
                customer_Expense = i.customerExpense
            where CUST_ID = l_cust_id;
        ELSE
        --l_msg := l_msg||';insert;cust_name='||nvl(i.customerName,'INVALID NAME');
            -- Create new customer
            Insert into CUST_PERSONAL_INFO(ID,cust_name,short_cust_name,other_cust_name,birth_date,gender,marital_status,
                household_reg_type_id,household_reg_number,household_reg_issue_date,household_reg_issue_place,MC_CUST_CODE,
                home_phone,last_updated_date,last_updated_by,CREATED_DATE,CREATED_BY,IDENTITY_ID)
            select l_cust_id,nvl(i.customerName,'INVALID NAME'),i.shortCustomerName,i.otherCustomerName,i.DOB,
                nvl((select ID from CODE_TABLE where CATEGORY = 'GENDER' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.gender),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_GENDER' and DESCRIPTION2 = i.gender)),
                nvl((select ID from CODE_TABLE where CATEGORY = 'MARITAL' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.maritalStatus),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_MARITAL' and DESCRIPTION2 = i.maritalStatus)),
                nvl((select ID from CODE_TABLE where CATEGORY = 'HHREG_TYPE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.familyBookSeriesType),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_HHREG_TYPE' and DESCRIPTION2 = i.familyBookSeriesType)),
                i.familyBookSeries,i.issueDateFamilyBook,i.issuePlaceFamilyBook,get_md5(nvl(i.citizenID,i.MILITARYID), i.DOB),
                i.homePhone,nvl(i.lastUpdatedDate,sysdate),substr(i.lastUpdatedBy,1,30),i.createdDate,substr(i.createdBy,1,30),l_identity_id
                from dual;
            -- CUST_ADDL_INFO
            Insert into CUST_ADDL_INFO(CUST_ID,education,old_identity_number,military_id,military_issue_date,military_issue_place,professional,position_in_comp,
                military_Rank,company_Name,department,company_Addr,ward_id,company_ward,district_id,company_district,province_id,office_phone,company_Tax,labour_contract_type,
                year_experience,month_experience,payroll_method,temp_same_perm_addr,lifetime_in_year,lifetime_in_month,accommodation_type,
                relation_spouse,spouse_name,spouse_DOB,spouse_identity_number,spouse_mobile,number_of_dependants,income_spouse,ref_full_name1,
                ref_person1_mobile,relation_ref_person1,ref_full_name2,ref_person2_mobile,relation_ref_person2,spouse_company_name,
                spouse_position,last_updated_date,last_updated_by,CREATED_DATE,CREATED_BY)
            select l_cust_id,
                nvl((select ID from CODE_TABLE where CATEGORY = 'EDUCATION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.education),
                    (select ID from CODE_TABLE where CATEGORY = 'EDUCATION' and DESCRIPTION2 = i.education)),
                i.citizenIdOld,i.militaryId,i.issueDateMilitaryId,i.issuePlaceMilitaryId,
                nvl((select ID from CODE_TABLE where CATEGORY = 'PROFESSION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.professionalStatus),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_PROFESSION' and DESCRIPTION2 = i.professionalStatus)),
                nvl((select ID from CODE_TABLE where CATEGORY = 'POSITION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.position),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_POSITION' and DESCRIPTION2 = i.position)),
                i.militaryRank,i.companyName,substr(i.department,1,200),i.companyAddressStress,
                nvl((select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                        and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.companyAddressWard),
                    (select ID from CODE_TABLE where CATEGORY = 'WARD' and parent_id = decode(l_district,0,parent_id,l_district)
                        and DESCRIPTION2 = i.companyAddressWard)),
                decode(i.WF,1,i.companyAddressWard,i.COMPANYADDRESSWARD_LABEL),
                l_district,
                decode(i.WF,1,i.companyAddressDistrict,i.COMPANYADDRESSDISTRICT_LABEL),
                l_province,
                i.officeNumber,i.companyTaxNumber,
                nvl((select ID from CODE_TABLE where CATEGORY = 'LAB_CONT' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.typeOfLabourContract),
                    (select ID from CODE_TABLE where CATEGORY = 'LAB_CONT' and DESCRIPTION2 = i.typeOfLabourContract)),
                i.experienceInYear,i.experienceInMonth,
                nvl((select ID from CODE_TABLE where CATEGORY = 'PAYROLL_M' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.salaryPaymentType),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_PAYROLL_M' and DESCRIPTION2 = i.salaryPaymentType)),
                nvl((select ID from CODE_TABLE where CATEGORY = 'TMP_RES' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.temporaryResidence),
                    (select ID from CODE_TABLE where CATEGORY = 'TMP_RES' and DESCRIPTION2 = i.temporaryResidence)),
                i.LIVINGTIMEATTEMPADDINYEAR,i.LIVINGTIMEATTEMPADDINMONTH,
                nvl((select ID from CODE_TABLE where CATEGORY = 'ACCOM_TYPE' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.accommodationType),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_ACCOM_TYPE' and DESCRIPTION2 = i.accommodationType)),
                nvl((select ID from CODE_TABLE where CATEGORY = 'RELAT_SPOU' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.relationshipWithApplicant),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_RELAT_SPOU' and DESCRIPTION2 = i.relationshipWithApplicant)),
                substr(i.spouseName,1,120),i.spouseDOB,i.spouseIDNumber,i.spouseMobilePhone,i.numberOfDependants,i.incomeSpouse,i.fullName1,i.mobilePhone1,
                nvl((select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.relationshipWithBorrower1),
                    (select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and DESCRIPTION2 = i.relationshipWithBorrower1)),
                i.fullName2,i.mobilePhone2,
                nvl((select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.relationshipWithBorrower2),
                    (select ID from CODE_TABLE where CATEGORY = 'RELAT_REF' and DESCRIPTION2 = i.relationshipWithBorrower2)),
                substr(i.spouseCompanyName,255),
                nvl((select ID from CODE_TABLE where CATEGORY = 'POSITION' and decode(i.WF,2,CODE_VALUE1,DESCRIPTION1) = i.spousePosition),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_POSITION' and DESCRIPTION2 = i.spousePosition)),
                nvl(i.lastUpdatedDate,sysdate),substr(i.lastUpdatedBy,1,30),i.createdDate,substr(i.createdBy,1,30)
                from dual;
                -- CUST_FINANCIAL_INFO
                insert into CUST_FINANCIAL_INFO(CUST_ID,cust_income,credit_in_other_bank,payment_amount_at_bank,spouse_credit_in_other_bank,
                    account_number_at_bank,bank_name,bank_branch,spouse_Income,avg_electric_bill,life_insu_company_id,insu_term,insu_term_other,
                    insu_term_fee,avg_account_bal,last_updated_date,last_updated_by,customer_Expense,CREATED_DATE,CREATED_BY)
                select l_cust_id,i.customerIncome,i.otherCreditLoan,i.amountOtherCreditLoan,i.otherCreditLoanSpouse,
                    substr(trim(i.accountNumber),1,50),i.bankName,i.atBank,i.spouseIncome,i.averageElectricBill,
                    (select ID from CODE_TABLE where CATEGORY = 'LINS_COMP' and CODE_VALUE1 = i.lifeInsuranceCompanyName),
                    (select ID from CODE_TABLE where CATEGORY = 'INSU_TERM' and CODE_VALUE1 = i.insuranceTerm),
                    i.insuranceTermOther,i.insuranceTermFee,i.averageAccountBalance,nvl(i.lastUpdatedDate,sysdate),substr(i.lastUpdatedBy,1,30),
                    i.customerExpense,i.createdDate,substr(i.createdBy,1,30)
                from dual;

        END IF;
        
        begin
            select nvl(max(ID),0) into l_prod_id  from PRODUCTS a where START_EFF_DATE = (select max(START_EFF_DATE) from PRODUCTS b
                where decode(i.schemeProductCode,null,b.PRODUCT_NAME,b.PRODUCT_CODE) = nvl(i.schemeProductCode,i.schemeProduct)
                and b.START_EFF_DATE <= sysdate and b.PRODUCT_CODE = a.PRODUCT_CODE);
        exception
            when others then
                l_prod_id := 0;
        end;

        l_msg := 'ID='||i.ID||' - appId='||i.appId||' - appNumber='||i.appNumber||' - WF='||i.WF||' - Update CREDIT_APP_REQUEST info';
        IF l_ca_id > 0 THEN
            -- Case exists -> Update database
            update CREDIT_APP_REQUEST set
                cust_id = l_cust_id,
                product_id = l_prod_id,
                ln_amount = nvl(i.loanAmount,0),
                ln_tenor = i.loanTenor,
                int_rate = nvl(i.interest,0),
                yearly_int_rate = nvl(i.yearInterest,0),
                ln_purpose = (select ID from CODE_TABLE where CATEGORY = decode(i.WF,2,'CA_PURPOSE','IS_CA_PURPOSE') and decode(i.WF,2,CODE_VALUE1,DESCRIPTION2) = i.loanPurpose),
                ln_other_purpose = i.loanPurposeOther,
                insu_amount = i.insurance,
                mc_contract_number = nvl(i.contractNumber,mc_contract_number),
                payment_date = i.repaymentDate,
                insu_company = nvl((select ID from CODE_TABLE where CATEGORY = 'INSU_COMP' and CODE_VALUE1 = i.insuranceCompany),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_INSU_COMP' and DESCRIPTION2 = i.insuranceCompany)),
                ln_amount_minus_insu = i.loanAmountAfterInsurrance,
                has_insurance = nvl((select ID from CODE_TABLE where CATEGORY = 'BOOLEAN' and CODE_VALUE1 = i.hasInsurrance),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_BOOLEAN' and DESCRIPTION2 = i.hasInsurrance)),
                disbursement_method = (select ID from CODE_TABLE where CATEGORY = 'DIS_METHOD' and CODE_VALUE1 = i.disbursementChannel),
                disbursement_channel = (select ID from CODE_TABLE where CATEGORY = 'DIS_CHANN' and CODE_VALUE1 = i.channelName),
                sale_id = (select EMP_ID from EMPLOYEE_LINK where EMP_CODE = i.saleCode),
                sale_code = i.saleCode,
                trans_office_id = (select ID from CODE_TABLE where CATEGORY in ('TRAN_OFF','POS_SIP') and CODE_VALUE1 = i.shopCode),
                insu_fee = i.insurranceFee,
                ln_start_date = i.loanStartDate,
                ln_end_date = i.loanEndDate,
                contract_date = i.signContractDate,
                insu_staffId = (select ID from CODE_TABLE where CATEGORY = 'INSU_STFID' and CODE_VALUE1 = i.insuranceStaffId),
                apply_to_object = (select ID from CODE_TABLE where CATEGORY = 'APPLY_OBJ' and CODE_VALUE1 = i.typeOfProduct),
                insu_rate = i.insuranceRate,
                insu_type = (select ID from CODE_TABLE where CATEGORY = 'INSU_TYPE' and CODE_VALUE1 = i.insuranceType),
                last_updated_date = nvl(i.lastUpdatedDate,sysdate),
                last_updated_by = substr(i.lastUpdatedBy,1,30)
            where ID = l_ca_id;
            
            update CREDIT_APP_BPM set
                BPM_ID = i.ID,
                workflow = i.typeOfLoan,
                LAST_UPDATED_DATE = nvl(i.lastUpdatedDate,sysdate),
                last_updated_by = substr(i.lastUpdatedBy,1,30)
            where CREDIT_APP_ID = l_ca_id;
            
        ELSE
            -- Case not exists -> Insert database
            select SEQ_CREDIT_APP_REQUEST_ID.nextval into l_ca_id from dual;
            insert into CREDIT_APP_REQUEST(ID,cust_id,product_id,ln_amount,ln_tenor,int_rate,yearly_int_rate,ln_purpose,ln_other_purpose,
                insu_amount,mc_contract_number,payment_date,insu_company,ln_amount_minus_insu,has_insurance,disbursement_method,
                disbursement_channel,sale_id,sale_code,trans_office_id,insu_fee,ln_start_date,ln_end_date,contract_date,insu_staffId,apply_to_object,
                insu_rate,insu_type,created_date,created_by,last_updated_date,last_updated_by)
            select l_ca_id,l_cust_id,l_prod_id,nvl(i.loanAmount,0),i.loanTenor,nvl(i.interest,0),nvl(i.yearInterest,0),
                (select ID from CODE_TABLE where CATEGORY = decode(i.WF,2,'CA_PURPOSE','IS_CA_PURPOSE') and decode(i.WF,2,CODE_VALUE1,DESCRIPTION2) = i.loanPurpose),
                i.loanPurposeOther,i.insurance,i.contractNumber,i.repaymentDate,
                nvl((select ID from CODE_TABLE where CATEGORY = 'INSU_COMP' and CODE_VALUE1 = i.insuranceCompany),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_INSU_COMP' and DESCRIPTION2 = i.insuranceCompany)),
                i.loanAmountAfterInsurrance,
                nvl((select ID from CODE_TABLE where CATEGORY = 'BOOLEAN' and CODE_VALUE1 = i.hasInsurrance),
                    (select ID from CODE_TABLE where CATEGORY = 'IS_BOOLEAN' and DESCRIPTION2 = i.hasInsurrance)),
                (select ID from CODE_TABLE where CATEGORY = 'DIS_METHOD' and CODE_VALUE1 = i.disbursementChannel),
                (select ID from CODE_TABLE where CATEGORY = 'DIS_CHANN' and CODE_VALUE1 = i.channelName),
                (select EMP_ID from EMPLOYEE_LINK where EMP_CODE = i.saleCode),i.saleCode,
                (select ID from CODE_TABLE where CATEGORY in ('TRAN_OFF','POS_SIP') and CODE_VALUE1 = i.shopCode),
                i.insurranceFee,i.loanStartDate,i.loanEndDate,i.signContractDate,
                (select ID from CODE_TABLE where CATEGORY = 'INSU_STFID' and CODE_VALUE1 = i.insuranceStaffId),
                (select ID from CODE_TABLE where CATEGORY = 'APPLY_OBJ' and CODE_VALUE1 = i.typeOfProduct),
                i.insuranceRate,
                (select ID from CODE_TABLE where CATEGORY = 'INSU_TYPE' and CODE_VALUE1 = i.insuranceType),
                i.createdDate,substr(i.createdBy,1,30),nvl(i.lastUpdatedDate,sysdate),substr(i.lastUpdatedBy,1,30)
                from dual;
                --CREDIT_APP_BPM
                insert into CREDIT_APP_BPM(CREDIT_APP_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,
                    LAST_UPDATED_BY,BPM_ID,BPM_APP_ID,BPM_APP_NUMBER,WORKFLOW)
                select l_ca_id,i.createdDate,nvl(i.lastUpdatedDate,sysdate),substr(i.createdBy,1,30),substr(i.lastUpdatedBy,1,30),
                    i.ID,i.APPID,i.APPNUMBER,i.typeOfLoan
                from dual;

        END IF;

        -- CREDIT_APP_COMMODITIES
        MERGE INTO CREDIT_APP_COMMODITIES CAC USING
         (select cgi.* from CDB_GOODS_INFOMATION cgi inner join 
            (select APPID,COLUMN_ORDER,max(HISTORYINDEX) max_his from CDB_GOODS_INFOMATION
            where STATUS = 1 and APPID = i.appID
            group by APPID,COLUMN_ORDER) cgi1
            on cgi.APPID = cgi1.APPID and cgi.COLUMN_ORDER = cgi1.COLUMN_ORDER and cgi.HISTORYINDEX = cgi1.max_his
            where cgi.APPID = i.appID and cgi.STATUS = 1) CGI
        ON (CAC.CREDIT_APP_ID = l_ca_id and CAC.COMM_SEQ = CGI.COLUMN_ORDER)
        WHEN NOT MATCHED THEN 
        INSERT (CREDIT_APP_ID,COMM_SEQ,COMM_ID,FRAME_NUMBER,PRICE,SERIAL_NUMBER,MODEL,SIP_ID,
            DISCOUNT_RATE,DOWN_PAYMENT_RATE,MERCHANT_INT_RATE,MODEL_ID,COLOR,REG_NUMBER,
            NUMBER_PLATE,DOWN_PAYMENT_AMOUNT,QUANTITY,BRAND_ID,BRAND,COMM_OTHER)
        VALUES (l_ca_id,CGI.COLUMN_ORDER,
            (select ID from CODE_TABLE where CATEGORY = 'COMM' and CODE_VALUE1 = CGI.goodCode),
            CGI.NOOFFRAME,CGI.GOODPRICE,CGI.NOOFSERIES,CGI.MODEL,
            (select ID from CODE_TABLE where CATEGORY = 'POS_SIP' and CODE_VALUE1 = i.shopCode),
            null,null,null,null,null,null,CGI.NOOFENGINE,i.ownedCapital,1,
            (select ID from CODE_TABLE where CATEGORY = 'BRAND' and CODE_VALUE1 = CGI.BRANDCODE),
            substr(CGI.BRANDOTHER,1,100),CGI.GOODOTHER
            )
        WHEN MATCHED THEN
        UPDATE SET 
            COMM_ID = (select ID from CODE_TABLE where CATEGORY = 'COMM' and CODE_VALUE1 = CGI.goodCode),
            FRAME_NUMBER = CGI.NOOFFRAME,
            PRICE = CGI.GOODPRICE,
            SERIAL_NUMBER = CGI.NOOFSERIES,
            MODEL = CGI.MODEL,
            SIP_ID = (select ID from CODE_TABLE where CATEGORY = 'POS_SIP' and CODE_VALUE1 = i.shopCode),
            DISCOUNT_RATE = null,
            DOWN_PAYMENT_RATE = null,
            MERCHANT_INT_RATE = null,
            MODEL_ID = null,
            COLOR = null,
            REG_NUMBER = null,
            NUMBER_PLATE = CGI.NOOFENGINE,
            DOWN_PAYMENT_AMOUNT = i.ownedCapital,
            QUANTITY = 1,
            BRAND_ID = (select ID from CODE_TABLE where CATEGORY = 'BRAND' and CODE_VALUE1 = CGI.BRANDCODE),
            BRAND = substr(CGI.BRANDOTHER,1,100),
            COMM_OTHER = CGI.GOODOTHER;

    END LOOP;

    select LAST_SYNC into l_last_sync from CDC_SYNC_LOG where TABLE_NAME = 'POS_UPLOAD_CREDIT';
    select count(1) into l_count from CDB_POS_UPLOAD_CREDIT where LAST_UPDATE_DATE >= l_last_sync;
    IF l_count > 0 THEN
        FOR uu in (select cab.CREDIT_APP_ID, substr(CREATEDBY,1,30) CREATEDBY from (select c1.* from CDB_POS_UPLOAD_CREDIT c1 inner join 
                (select APPID, max(ID) ID from CDB_POS_UPLOAD_CREDIT group by APPID) c2 on c1.ID = c2.ID
                        where LAST_UPDATE_DATE >= l_last_sync) cpuc
                inner join  CREDIT_APP_BPM cab on cab.BPM_APP_ID = cpuc.APPID)
        LOOP
            update CREDIT_APP_REQUEST set credit_doc_uploader = uu.CREATEDBY where ID = uu.CREDIT_APP_ID;
        END LOOP;
        /*
        update CREDIT_APP_REQUEST car set
            credit_doc_uploader = (select substr(CREATEDBY,1,30) from (select c1.* from CDB_POS_UPLOAD_CREDIT c1 inner join 
                (select APPID, max(ID) ID from CDB_POS_UPLOAD_CREDIT group by APPID) c2 on c1.ID = c2.ID) cpuc
                inner join  CREDIT_APP_BPM cab on cab.BPM_APP_ID = cpuc.APPID
                where cab.CREDIT_APP_ID = car.ID)
        where exists (select 1 from CDB_POS_UPLOAD_CREDIT cpuc
                inner join  CREDIT_APP_BPM cab on cab.BPM_APP_ID = cpuc.APPID
                where cpuc.LAST_UPDATE_DATE >= l_last_sync and cab.CREDIT_APP_ID = car.ID);
                */
    END IF;

exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20002,'Exception in update_sync_credit_app. '||l_msg||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure update_sync_credit_app_trail is
    l_sync_date date;
    l_count int;
    
    l_APPSTATUS varchar2(50);
    l_CURRENTTASK varchar2(100);
    l_PRODUCTGROUP varchar2(50);
    l_PROCESSNAME varchar2(100);
    l_trail number(10) := 0;
    l_trail1 number(10) := 0;
    l_cart_id int := 0;
    l_carj_id int := 0;
    l_msg varchar2(200);

begin

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_ENTRY_REASON_FOR_RETURN';
    select count(1) into l_count from DE_DATA_ENTRY_REASON_FOR_RETURN where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
          -- Update from DE_DATA_ENTRY_REASON_FOR_RETURN
        FOR i IN (select der.*,dde.DECOMMENT,dde.DECOMMENTCIC,
                cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID,der.HISTORYINDEX ORDER BY der.ID) trail_order
            from DE_DATA_ENTRY_REASON_FOR_RETURN der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
            left join DE_DATA_ENTRY dde on der.APPID = dde.APPID
            where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'DE_DATA_ENTRY_REASON_FOR_RETURN - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
            and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DE1';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = decode(i.DECOMMENT,null,USER_COMMENT,get_clob(i.DECOMMENT||' -- CIC -- '||i.DECOMMENTCIC)),
                    FROM_USER_CODE = i.FROM_USER,
                    TO_USER_CODE = i.TO_USER
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                    and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DE1';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.HISTORYINDEX,i.TRAIL_ORDER,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                        get_clob(i.DECOMMENT||' -- CIC -- '||i.DECOMMENTCIC),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        i.FROM_USER,i.TO_USER,'DE1');
            END IF;

        END LOOP;

    END IF;

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_ENTRY_2_REASON_FOR_RETURN';
    select count(1) into l_count from DE_DATA_ENTRY_2_REASON_FOR_RETURN where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
      -- Update from DE_DATA_ENTRY_2_REASON_FOR_RETURN
        FOR i IN (select der.*,dde2.DECommentDE2,dde2.DECommentCIC,
            cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID,der.HISTORYINDEX ORDER BY der.ID) trail_order
        from DE_DATA_ENTRY_2_REASON_FOR_RETURN der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
        left join DE_DATA_ENTRY_2 dde2 on der.APPID = dde2.APPID
        where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'DE_DATA_ENTRY_2_REASON_FOR_RETURN - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
            and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DE2';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = decode(i.DECommentDE2,null,USER_COMMENT,get_clob(i.DECommentDE2||' -- CIC -- '||i.DECommentCIC)),
                    FROM_USER_CODE = i.FROM_USER,
                    TO_USER_CODE = i.TO_USER
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                    and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DE2';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.HISTORYINDEX,i.TRAIL_ORDER,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                        get_clob(i.DECommentDE2||' -- CIC -- '||i.DECommentCIC),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        i.FROM_USER,i.TO_USER,'DE2');
            END IF;

        END LOOP;

    END IF;

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'DE_CALL_APPRAISAL_REASON_FOR_RETURN';
    select count(1) into l_count from DE_CALL_APPRAISAL_REASON_FOR_RETURN where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
      -- Update from DE_CALL_APPRAISAL_REASON_FOR_RETURN
        FOR i IN (select der.*,dca.CALLAPPRAISALOTHERCOMMENTS,
            cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID,der.HISTORYINDEX ORDER BY der.ID) trail_order
        from DE_CALL_APPRAISAL_REASON_FOR_RETURN der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
        left join DE_CALL_APPRAISAL dca on der.APPID = dca.APPID
        where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'DE_CALL_APPRAISAL_REASON_FOR_RETURN - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
            and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'CA';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = nvl(get_clob(i.CALLAPPRAISALOTHERCOMMENTS),USER_COMMENT),
                    FROM_USER_CODE = i.FROM_USER,
                    TO_USER_CODE = i.TO_USER
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                    and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'CA';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.HISTORYINDEX,i.TRAIL_ORDER,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                        get_clob(i.CALLAPPRAISALOTHERCOMMENTS),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        i.FROM_USER,i.TO_USER,'CA');
            END IF;

        END LOOP;

    END IF;

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_CORRECTOR_REASON_FOR_RETURN';
    select count(1) into l_count from DE_DATA_CORRECTOR_REASON_FOR_RETURN where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
        -- Update from DE_DATA_CORRECTOR_REASON_FOR_RETURN
        FOR i IN (select der.*,ddc.OTHERCONCLUSION,
                cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID,der.HISTORYINDEX ORDER BY der.ID) trail_order
            from DE_DATA_CORRECTOR_REASON_FOR_RETURN der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
            left join DE_DATA_CORRECTOR ddc on der.APPID = ddc.APPID
            where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'DE_DATA_CORRECTOR_REASON_FOR_RETURN - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
            and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DC';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = nvl(get_clob(i.OTHERCONCLUSION),USER_COMMENT),
                    FROM_USER_CODE = i.FROM_USER,
                    TO_USER_CODE = i.TO_USER
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                    and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DC';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.HISTORYINDEX,i.TRAIL_ORDER,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),get_clob(i.OTHERCONCLUSION),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        i.FROM_USER,i.TO_USER,'DC');
            END IF;

        END LOOP;
    END IF;

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'DE_OPERATION_REASON_FOR_RETURN';
    select count(1) into l_count from DE_OPERATION_REASON_FOR_RETURN where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
      -- Update from DE_OPERATION_REASON_FOR_RETURN
        FOR i IN (select der.*,docd.OPERATIONCOMMENT,
            cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID,der.HISTORYINDEX ORDER BY der.ID) trail_order
        from DE_OPERATION_REASON_FOR_RETURN der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
        left join DE_OPERATION_CHECK_DOC docd on der.APPID = docd.APPID
        where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'DE_OPERATION_REASON_FOR_RETURN - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
            and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'OP';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = nvl(get_clob(i.OPERATIONCOMMENT),USER_COMMENT),
                    TO_USER_CODE = 'Sale',
                    FROM_USER_CODE = i.FROMUSER
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                    and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'OP';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.HISTORYINDEX,i.TRAIL_ORDER,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),get_clob(i.OPERATIONCOMMENT),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        i.FROMUSER,'Sale','OP');
            END IF;

        END LOOP;
    END IF;

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'DE_DATA_CORRECTOR_REASON_FOR_REJECT';
    select count(1) into l_count from DE_DATA_CORRECTOR_REASON_FOR_REJECT where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
      -- Update from DE_DATA_CORRECTOR_REASON_FOR_REJECT
        FOR i IN (select der.*,ddc.OTHERCONCLUSION,
                cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID ORDER BY der.ID) trail_seq
        from DE_DATA_CORRECTOR_REASON_FOR_REJECT der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
        left join DE_DATA_CORRECTOR ddc on der.APPID = ddc.APPID
        where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'DE_DATA_CORRECTOR_REASON_FOR_REJECT - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
            and TRAIL_ORDER = 1000 and CURRENT_TASK = 'DC';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRJDTL_CD' and CODE_VALUE1 = i.REASONREJECTINDC),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = nvl(get_clob(i.OTHERCONCLUSION),USER_COMMENT),
                    FROM_USER_CODE = 'DC'
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
                    and TRAIL_ORDER = 1000 and CURRENT_TASK = 'DC';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.TRAIL_SEQ,1000,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRJDTL_CD' and CODE_VALUE1 = i.REASONREJECTINDC),get_clob(i.OTHERCONCLUSION),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        'DC',null,'DC');
            END IF;

        END LOOP;

   END IF;


    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'DATA_CORRECTOR_REASON_FOR_RETURN';
    select count(1) into l_count from CDB_DATA_CORRECTOR_REASON_FOR_RETURN where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
        -- Update from CDB_DATA_CORRECTOR_REASON_FOR_RETURN
        FOR i IN (select der.*,cdc.OTHERCONCLUSION,
                cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID,der.HISTORYINDEX ORDER BY der.ID) trail_order
            from CDB_DATA_CORRECTOR_REASON_FOR_RETURN der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
            left join CDB_DATA_CORRECTOR cdc on der.APPID = cdc.APPID
            where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'DATA_CORRECTOR_REASON_FOR_RETURN - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DC';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = nvl(get_clob(i.OTHERCONCLUSION),USER_COMMENT),
                    FROM_USER_CODE = 'DC',
                    TO_USER_CODE = i.TOUSER
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                    and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'DC';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.HISTORYINDEX,i.TRAIL_ORDER,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),get_clob(i.OTHERCONCLUSION),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        'DC',i.TOUSER,'DC');
            END IF;

        END LOOP;
    END IF;

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'OPERATION_REASON_FOR_RETURN';
    select count(1) into l_count from CDB_OPERATION_REASON_FOR_RETURN where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
      -- Update from CDB_OPERATION_REASON_FOR_RETURN
        FOR i IN (select der.*,cocd.OPERATIONCOMMENT,
            cab.CREDIT_APP_ID,ROW_NUMBER() OVER (PARTITION BY der.APPID,der.HISTORYINDEX ORDER BY der.ID) trail_order
        from CDB_OPERATION_REASON_FOR_RETURN der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
        left join CDB_OPERATION_CHECK_DOC cocd on der.APPID = cocd.APPID
        where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'OPERATION_REASON_FOR_RETURN - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'OP';

            IF l_trail > 0 THEN
                UPDATE CREDIT_APP_TRAIL SET 
                    CREATED_DATE = i.CREATEDDATE,
                    LAST_UPDATED_DATE = i.CREATEDDATE,
                    CREATED_BY = i.CREATEDBY,
                    LAST_UPDATED_BY = i.CREATEDBY,
                    STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                    TASK_ID = l_CURRENTTASK,
                    ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                    REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),
                    STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                    USER_COMMENT = nvl(get_clob(i.OPERATIONCOMMENT),USER_COMMENT),
                    TO_USER_CODE = 'Sale',
                    FROM_USER_CODE = i.FROMUSER
                where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.HISTORYINDEX 
                    and nvl(TRAIL_ORDER,0) = i.TRAIL_ORDER and CURRENT_TASK = 'OP';
            ELSE
                INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                    TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                VALUES (i.CREDIT_APP_ID,i.HISTORYINDEX,i.TRAIL_ORDER,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                        (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        l_CURRENTTASK,null,null,null,
                        (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        (select ID from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONDETAILCODE),get_clob(i.OPERATIONCOMMENT),
                        decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        i.FROMUSER,'Sale','OP');
            END IF;

        END LOOP;

    END IF;

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'CALL_APPRAISAL';
    select count(1) into l_count from CDB_CALL_APPRAISAL where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
      -- Update from CALL_APPRAISAL
        FOR i IN (select der.*, cab.CREDIT_APP_ID, 
            ROW_NUMBER() OVER (PARTITION BY der.APPID ORDER BY der.ID) trail_seq
        from CDB_CALL_APPRAISAL der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
        where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'CALL_APPRAISAL - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
            and nvl(TRAIL_ORDER,0) = 1 and CURRENT_TASK = 'CA';
            
            select count(1) into l_trail1 from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
            and nvl(TRAIL_ORDER,0) = 1000 and CURRENT_TASK = 'CA';
            
            begin
                select ID into l_cart_id from CODE_TABLE where CATEGORY = 'RSRTDTL_CD' and CODE_VALUE1 = i.REASONRETURNDE;
            exception
                when others then
                    l_cart_id := null;
            end;

            begin
                select ID into l_carj_id from CODE_TABLE where CATEGORY = 'RSRJDTL_CD' and CODE_VALUE1 = i.REASONFORREJECTDETAIL;
            exception
                when others then
                    l_carj_id := null;
            end;

            IF (i.REASONRETURNDE is not null) AND 
                ((l_APPSTATUS like 'DE_RETURN%') OR (l_APPSTATUS like 'CA_RETURN%') OR (l_APPSTATUS like 'POS_RETURN%')) THEN
                IF l_trail > 0 THEN
                    UPDATE CREDIT_APP_TRAIL SET 
                        CREATED_DATE = i.CREATEDDATE,
                        LAST_UPDATED_DATE = i.CREATEDDATE,
                        CREATED_BY = i.CREATEDBY,
                        LAST_UPDATED_BY = i.CREATEDBY,
                        STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        TASK_ID = l_CURRENTTASK,
                        ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                        REASON_ID = l_cart_id,
                        STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        USER_COMMENT = nvl(get_clob(i.CALLAPPRAISALOTHERCOMMENTS),USER_COMMENT),
                        FROM_USER_CODE = 'CA',
                        TO_USER_CODE = 'Sale'
                    where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
                        and nvl(TRAIL_ORDER,0) = 1 and CURRENT_TASK = 'CA' 
                        and nvl(REASON_ID,0) <> nvl(l_cart_id,0);
                ELSE
                    INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                        TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                    VALUES (i.CREDIT_APP_ID,i.trail_seq,1,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                            (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                            l_CURRENTTASK,null,null,null,
                            (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'RETURN'),
                            l_cart_id,null,
                            decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                            'CA','Sale','CA');
                END IF;
            END IF;

            IF (i.REASONFORREJECTDETAIL is not null) THEN
                IF l_trail1 > 0 THEN
                    UPDATE CREDIT_APP_TRAIL SET 
                        CREATED_DATE = i.CREATEDDATE,
                        LAST_UPDATED_DATE = i.CREATEDDATE,
                        CREATED_BY = i.CREATEDBY,
                        LAST_UPDATED_BY = i.CREATEDBY,
                        STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        TASK_ID = l_CURRENTTASK,
                        ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                        REASON_ID = l_carj_id,
                        STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        USER_COMMENT = nvl(get_clob(i.CALLAPPRAISALOTHERCOMMENTS),USER_COMMENT),
                        FROM_USER_CODE = 'CA',
                        TO_USER_CODE = 'Sale'
                    where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
                        and nvl(TRAIL_ORDER,0) = 1000 and CURRENT_TASK = 'CA' 
                        and nvl(REASON_ID,0) <> nvl(l_carj_id,0);
                ELSE
                    INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                        TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                    VALUES (i.CREDIT_APP_ID,i.trail_seq,1000,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                            (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                            l_CURRENTTASK,null,null,null,
                            (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                            l_carj_id,null,
                            decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                            'CA','Sale','CA');
                END IF;
            END IF;

            IF (i.REASONFORREJECT is not null) AND (i.REASONFORREJECTDETAIL is null) THEN
                IF l_trail1 > 0 THEN
                    UPDATE CREDIT_APP_TRAIL SET 
                        CREATED_DATE = i.CREATEDDATE,
                        LAST_UPDATED_DATE = i.CREATEDDATE,
                        CREATED_BY = i.CREATEDBY,
                        LAST_UPDATED_BY = i.CREATEDBY,
                        STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        TASK_ID = l_CURRENTTASK,
                        ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                        REASON_ID = (select ID from CODE_TABLE where CATEGORY = 'RSRJ_CODE' and CODE_VALUE1 = i.REASONFORREJECT),
                        STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        USER_COMMENT = nvl(get_clob(i.CALLAPPRAISALOTHERCOMMENTS),USER_COMMENT),
                        FROM_USER_CODE = 'CA',
                        TO_USER_CODE = 'Sale'
                    where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
                        and nvl(TRAIL_ORDER,0) = 1000 and CURRENT_TASK = 'CA'; 
                ELSE
                    INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                        TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                    VALUES (i.CREDIT_APP_ID,i.trail_seq,1000,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                            (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                            l_CURRENTTASK,null,null,null,
                            (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                            (select ID from CODE_TABLE where CATEGORY = 'RSRJ_CODE' and CODE_VALUE1 = i.REASONFORREJECT),null,
                            decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                            'CA','Sale','CA');
                END IF;
            END IF;

        END LOOP;

    END IF;


exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20003,'Exception in update_sync_credit_app_trail. Message='||l_msg||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure update_sync_credit_other is
    l_sync_date date;
    l_count int;
    
    l_APPSTATUS varchar2(50);
    l_CURRENTTASK varchar2(100);
    l_PRODUCTGROUP varchar2(50);
    l_PROCESSNAME varchar2(100);
    l_trail number(10) := 0;
    l_trail1 number(10) := 0;
    l_cart_id int := 0;
    l_carj_id int := 0;
    l_msg varchar2(200);
begin

    select LAST_SYNC into l_sync_date from CDC_SYNC_LOG where TABLE_NAME = 'APPROVE';
      -- Update Approval
    MERGE INTO CREDIT_APP_APPRAISAL caa USING
     (select ca.*,cab.CREDIT_APP_ID from CDB_APPROVE ca inner join CREDIT_APP_BPM cab on ca.APPID = cab.BPM_APP_ID
        where ca.LAST_UPDATE_DATE >= l_sync_date) apr
    ON (caa.CREDIT_APP_ID = apr.CREDIT_APP_ID)
    WHEN NOT MATCHED THEN 
    INSERT (CREDIT_APP_ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,APPROVED_AMOUNT,
        APPROVED_DATE,APPROVED_USER,INDUSTRY,APPROVED_TENOR,APPROVED_INS_AMOUNT,APPROVED_DTI,APPROVED_PTI,
        APPROVED_DOWNPAYMENT,MONTHLY_PAYMENT_AMOUNT,SCORING_RESULT,POLICY_REGULATION,FRAUD,
        FINAL_APPROVED,INSURRANCE_FEE,AMOUNT_AFTER_INS,APPROVED_DOWNPAYMENT_AMOUNT)
    VALUES (apr.CREDIT_APP_ID,nvl(apr.CREATEDDATE,sysdate),nvl(apr.CREATEDDATE,sysdate),substr(apr.CREATEDBY,1,30),
        substr(apr.CREATEDBY,1,30),to_number(apr.LOANAMOUNTAPPROVER),
        nvl(apr.CREATEDDATE,sysdate),(select ID from USERS where LOGIN_ID = apr.CREATEDBY),null,apr.LOANTENORAPPROVER,
        apr.INSURANCE,apr.DTIAPPROVER,apr.PTIAPPROVER,
        apr.DOWNPAYMENTAPPROVER,to_number(apr.MONTHLYREPAYMENTAMOUNT),substr(apr.SCORINGRESULT,1,50),
        apr.POLICYREGULATION,apr.FRAUD,
        (select ID from CODE_TABLE where CATEGORY = 'APPR_DEC' and CODE_VALUE1 = apr.FINALAPPROVERDECISION),
        apr.INSURRANCEFEE,apr.LOANAMOUNTAFTERINSURRANCE,apr.OWNEDCAPITAL)
    WHEN MATCHED THEN
    UPDATE SET 
        caa.LAST_UPDATED_DATE = sysdate,
        caa.LAST_UPDATED_BY = substr(apr.CREATEDBY,1,30),
        caa.APPROVED_AMOUNT = to_number(apr.LOANAMOUNTAPPROVER),
        caa.APPROVED_DATE = apr.CREATEDDATE,
        caa.APPROVED_USER = (select ID from USERS where LOGIN_ID = apr.CREATEDBY),
        caa.APPROVED_TENOR = apr.LOANTENORAPPROVER,
        caa.APPROVED_INS_AMOUNT = apr.INSURANCE,
        caa.APPROVED_DTI = apr.DTIAPPROVER,
        caa.APPROVED_PTI = apr.PTIAPPROVER,
        caa.APPROVED_DOWNPAYMENT = apr.DOWNPAYMENTAPPROVER,
        caa.MONTHLY_PAYMENT_AMOUNT = to_number(apr.MONTHLYREPAYMENTAMOUNT),
        caa.SCORING_RESULT = substr(apr.SCORINGRESULT,1,50),
        caa.POLICY_REGULATION = apr.POLICYREGULATION,
        caa.FRAUD = apr.FRAUD,
        caa.FINAL_APPROVED = (select ID from CODE_TABLE where CATEGORY = 'APPR_DEC' and CODE_VALUE1 = apr.FINALAPPROVERDECISION),
        caa.INSURRANCE_FEE = apr.INSURRANCEFEE,
        caa.AMOUNT_AFTER_INS = apr.LOANAMOUNTAFTERINSURRANCE,
        caa.APPROVED_DOWNPAYMENT_AMOUNT = apr.OWNEDCAPITAL;

    select count(1) into l_count from CDB_APPROVE where LAST_UPDATE_DATE >= l_sync_date;
    IF l_count > 0 THEN
      -- Update from CDB_APPROVE
        FOR i IN (select der.*, cab.CREDIT_APP_ID, nvl(reasonrejectother,finalreasonforreject) rejectOther,
            nvl(REASONFORREJECTDETAIL, REASONFORREJECT) rejectReason,
            ROW_NUMBER() OVER (PARTITION BY der.APPID ORDER BY der.ID) trail_seq
        from CDB_APPROVE der inner join CREDIT_APP_BPM cab on der.APPID = cab.BPM_APP_ID
        where der.LAST_UPDATE_DATE >= l_sync_date)
        LOOP
            l_msg := 'CDB_APPROVE - CREDIT_APP_ID='||i.CREDIT_APP_ID||' - ID='||i.ID;
            l_trail := 0;
            l_APPSTATUS := null;
            l_CURRENTTASK := null;
            l_PRODUCTGROUP := null;
            l_PROCESSNAME := null;
            begin
                select cat.APPSTATUS,cat.CURRENTTASK,cat.PRODUCTGROUP,cat.PROCESSNAME
                    into l_APPSTATUS,l_CURRENTTASK,l_PRODUCTGROUP,l_PROCESSNAME
                from CDB_APP_TRANSACTION cat
                    inner join (select APPID,max(ID) max_id from CDB_APP_TRANSACTION 
                        where APPNUMBER <> 0 and APPID = i.APPID group by APPID) catmax
                    on cat.ID = catmax.max_id
                where cat.APPID = i.APPID;
            exception
                when others then
                    null;
            end;
            
            select count(1) into l_trail from CREDIT_APP_TRAIL
            where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
            and nvl(TRAIL_ORDER,0) = 1000 and CURRENT_TASK = 'AP';
            
            begin
                select ID into l_carj_id from CODE_TABLE where CATEGORY = decode(i.REASONFORREJECTDETAIL,null,'RSRJ_CODE','RSRJDTL_CD') 
                    and CODE_VALUE1 = decode(i.REASONFORREJECTDETAIL,null,i.REASONFORREJECT,i.REASONFORREJECTDETAIL);
            exception
                when others then
                    l_carj_id := null;
            end;

            IF ((i.rejectReason is not null) OR (i.rejectOther is not null)) AND (l_APPSTATUS = 'AP_REJECT') THEN
                IF l_trail > 0 THEN
                    UPDATE CREDIT_APP_TRAIL SET 
                        CREATED_DATE = i.CREATEDDATE,
                        LAST_UPDATED_DATE = i.CREATEDDATE,
                        CREATED_BY = i.CREATEDBY,
                        LAST_UPDATED_BY = i.CREATEDBY,
                        STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        TASK_ID = l_CURRENTTASK,
                        ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                        REASON_ID = l_carj_id,
                        STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        USER_COMMENT = nvl(get_clob(i.approvecomment),USER_COMMENT),
                        FROM_USER_CODE = 'AP',
                        TO_USER_CODE = 'Sale'
                    where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
                        and nvl(TRAIL_ORDER,0) = 1000 and CURRENT_TASK = 'AP'; 
                ELSE
                    INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                        TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                    VALUES (i.CREDIT_APP_ID,i.trail_seq,1000,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                            (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                            l_CURRENTTASK,null,null,null,
                            (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'REJECT'),
                            l_carj_id,get_clob(i.approvecomment),
                            decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                            'AP','Sale','AP');
                END IF;
            ELSIF l_APPSTATUS = 'AP_ABORT' THEN
                IF l_trail > 0 THEN
                    UPDATE CREDIT_APP_TRAIL SET 
                        CREATED_DATE = i.CREATEDDATE,
                        LAST_UPDATED_DATE = i.CREATEDDATE,
                        CREATED_BY = i.CREATEDBY,
                        LAST_UPDATED_BY = i.CREATEDBY,
                        STEP = (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                        TASK_ID = l_CURRENTTASK,
                        ACTION = (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'ABORT'),
                        REASON_ID = l_carj_id,
                        STEP_CODE = decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                        USER_COMMENT = nvl(get_clob(i.approvecomment),USER_COMMENT),
                        FROM_USER_CODE = 'AP',
                        TO_USER_CODE = 'Sale'
                    where CREDIT_APP_ID = i.CREDIT_APP_ID and nvl(TRAIL_SEQ,0) = i.trail_seq 
                        and nvl(TRAIL_ORDER,0) = 1000 and CURRENT_TASK = 'AP'; 
                ELSE
                    INSERT INTO CREDIT_APP_TRAIL(CREDIT_APP_ID,TRAIL_SEQ,TRAIL_ORDER,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,STEP,
                        TASK_ID,FROM_USER,TO_USER,TO_TEAM,ACTION,REASON_ID,USER_COMMENT,STEP_CODE,FROM_USER_CODE,TO_USER_CODE,CURRENT_TASK)
                    VALUES (i.CREDIT_APP_ID,i.trail_seq,1000,i.CREATEDDATE,i.CREATEDDATE,i.CREATEDBY,i.CREATEDBY,
                            (select ID from CODE_TABLE where CATEGORY = 'STEP_STAT' and CODE_VALUE1 = l_APPSTATUS),
                            l_CURRENTTASK,null,null,null,
                            (select ID from CODE_TABLE where CATEGORY = 'ACTION_CS' and CODE_VALUE1 = 'ABORT'),
                            l_carj_id,get_clob(i.approvecomment),
                            decode(nvl(instr(l_APPSTATUS,'_'),0),0,l_APPSTATUS,substr(l_APPSTATUS,1,instr(l_APPSTATUS,'_')-1)),
                            'AP','Sale','AP');
                END IF;
            END IF;

        END LOOP;

    END IF;


exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20002,'Exception in update_sync_credit_other. '||l_msg||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure update_sync_product_data is
    l_msg varchar2(200);
begin

    -- update product group
    MERGE INTO CODE_TABLE ct USING
     (select product_condition from PRD_PRODUCTS where product_condition is not null group by product_condition) grp
    ON (ct.CATEGORY = 'PRD_GROUP' and ct.CODE_VALUE1 = grp.product_condition)
    WHEN NOT MATCHED THEN 
    INSERT (PARENT_ID,CODE_GROUP,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1,DESCRIPTION2,REFERENCE)
    VALUES (
        null,'PROD', 'PRD_GROUP', grp.product_condition, null, decode(grp.product_condition,'1','Cash Loan','2','Installment Loan','Card'), null, null)
    WHEN MATCHED THEN
    UPDATE SET 
        ct.DESCRIPTION1 = decode(grp.product_condition,'1','Cash Loan','2','Installment Loan','Card');
    
      -- Update Interest
      l_msg := 'sync PRD_PRODUCTS to INTEREST_TABLE';
    MERGE INTO INTEREST_TABLE it USING
     (select * from PRD_PRODUCTS) prod
    ON (it.ID = prod.ID)
    WHEN NOT MATCHED THEN 
    INSERT (ID,RATE_TYPE,RATE_CODE,RATE_NAME,MONTHLY_RATE,YEARLY_RATE,
        SPREAD,MAX_RATE,MIN_RATE,STATUS,START_EFF_DATE,END_EFF_DATE)
    VALUES (prod.ID,'F','F'||lpad(prod.ID,7,'0'),prod.PRODUCT_NAME,prod.MONTH_INTEREST,
        prod.YEAR_INTEREST,null,null,null,'A',prod.APPLIED_DATE,nvl(prod.EXPIRED_DATE,to_date('99991231','YYYYMMDD')))
    WHEN MATCHED THEN
    UPDATE SET 
        it.RATE_NAME = prod.PRODUCT_NAME,
        it.MONTHLY_RATE = prod.MONTH_INTEREST,
        it.YEARLY_RATE = prod.YEAR_INTEREST,
        it.END_EFF_DATE = nvl(prod.EXPIRED_DATE,to_date('99991231','YYYYMMDD'));

      -- Update Product
      l_msg := 'sync PRD_PRODUCTS to PRODUCTS';
    MERGE INTO PRODUCTS pr USING
     (select p.*,tenor.MIN_VALUE MIN_TENOR,tenor.MAX_VALUE MAX_TENOR,
        amt.MIN_VALUE MIN_AMOUNT,amt.MAX_VALUE MAX_AMOUNT,owc.EXP_MIN_VALUE,owc.EXP_MAX_VALUE,
        pp.PROCESS_ID,pp.PROCESS_NAME
        from PRD_PRODUCTS p inner join (select PRODUCT_CODE,APPLIED_DATE,ID,
            ROW_NUMBER() OVER (PARTITION BY PRODUCT_CODE,APPLIED_DATE ORDER BY PRODUCT_CODE,APPLIED_DATE,ID desc) rn
            from PRD_PRODUCTS) p1 on p.ID = p1.ID and p1.rn = 1 
        inner join PRD_PROCESS_PRODUCTS pp on p.ID = pp.PRODUCT_ID 
        and PROCESS_ID in ('92489624057e0e0abafb278083044537','357116699583cdc94981987059206300')
        left join (select pcp.*,ppp.PROCESS_ID from PRD_PRODUCT_CONFIG_PARAMETERS pcp 
        inner join PRD_PROCESS_PARAMETERS ppp on pcp.PARAMETER_ID = ppp.ID and PARAMETER_NAME = 'loanTenor' 
        and exists (select 1 from (select PRODUCT_ID,PARAMETER_ID,max(nvl(APPLIED_DATE,trunc(sysdate))) max_date
        from PRD_PRODUCT_CONFIG_PARAMETERS pcp1 group by PRODUCT_ID,PARAMETER_ID) pcp2
        where pcp2.PRODUCT_ID = pcp.PRODUCT_ID and pcp2.PARAMETER_ID = pcp.PARAMETER_ID 
        and pcp2.max_date = nvl(pcp.APPLIED_DATE,trunc(sysdate)))) tenor
        on p.ID = tenor.PRODUCT_ID and pp.PROCESS_ID = tenor.PROCESS_ID
        left join (select pcp.*,ppp.PROCESS_ID from PRD_PRODUCT_CONFIG_PARAMETERS pcp 
        inner join PRD_PROCESS_PARAMETERS ppp on pcp.PARAMETER_ID = ppp.ID and PARAMETER_NAME = 'ownedCapital' 
        and exists (select 1 from (select PRODUCT_ID,PARAMETER_ID,max(nvl(APPLIED_DATE,trunc(sysdate))) max_date
        from PRD_PRODUCT_CONFIG_PARAMETERS pcp1 group by PRODUCT_ID,PARAMETER_ID) pcp2
        where pcp2.PRODUCT_ID = pcp.PRODUCT_ID and pcp2.PARAMETER_ID = pcp.PARAMETER_ID 
        and pcp2.max_date = nvl(pcp.APPLIED_DATE,trunc(sysdate)))) owc
        on p.ID = owc.PRODUCT_ID and pp.PROCESS_ID = owc.PROCESS_ID
        left join (select pcp.*,ppp.PROCESS_ID from PRD_PRODUCT_CONFIG_PARAMETERS pcp 
        inner join PRD_PROCESS_PARAMETERS ppp on pcp.PARAMETER_ID = ppp.ID and PARAMETER_NAME = 'loanAmountAfterInsurrance' 
        and exists (select 1 from (select PRODUCT_ID,PARAMETER_ID,max(nvl(APPLIED_DATE,trunc(sysdate))) max_date
        from PRD_PRODUCT_CONFIG_PARAMETERS pcp1 group by PRODUCT_ID,PARAMETER_ID) pcp2
        where pcp2.PRODUCT_ID = pcp.PRODUCT_ID and pcp2.PARAMETER_ID = pcp.PARAMETER_ID 
        and pcp2.max_date = nvl(pcp.APPLIED_DATE,trunc(sysdate)))) amt
        on p.ID = amt.PRODUCT_ID and pp.PROCESS_ID = amt.PROCESS_ID) prod
    ON (pr.PRODUCT_CODE = prod.PRODUCT_CODE and pr.START_EFF_DATE = prod.APPLIED_DATE)
    WHEN NOT MATCHED THEN 
    INSERT (PRODUCT_CATEGORY_ID,PRODUCT_GROUP_ID,PRODUCT_CODE,PRODUCT_NAME,CCY,PTI,TENOR,MIN_TENOR,MAX_TENOR,
        RATE_INDEX,LATE_RATE_INDEX,LATE_PENALTY_FEE,PRE_LIQUIDATION_FEE,MAX_QUANTITY_COMMODITIES,
        MAX_LOAN_AMOUNT,MIN_LOAN_AMOUNT,EXP_MIN_VALUE,EXP_MAX_VALUE,STATUS,START_EFF_DATE,END_EFF_DATE)
    VALUES (1,nvl((select ID from CODE_TABLE where CATEGORY = 'PRD_GROUP' and CODE_VALUE1 = prod.product_condition),0), 
        prod.PRODUCT_CODE, prod.PRODUCT_NAME,'VND',prod.PTI,
        null,prod.MIN_TENOR,prod.MAX_TENOR,prod.ID,null,prod.LATE_PENALTY_FEE,prod.EARLY_SETTLEMENT_FEE,prod.MAX_OF_GOODS,
        prod.MAX_AMOUNT,prod.MIN_AMOUNT,prod.EXP_MIN_VALUE,prod.EXP_MAX_VALUE,
        decode(prod.STATUS,1,'A','C'),prod.APPLIED_DATE,nvl(trunc(prod.EXPIRED_DATE),to_date('99991231','YYYYMMDD')))
    WHEN MATCHED THEN
    UPDATE SET 
        pr.PRODUCT_GROUP_ID = nvl((select ID from CODE_TABLE where CATEGORY = 'PRD_GROUP' and CODE_VALUE1 = prod.product_condition),0),
        pr.PRODUCT_NAME = prod.PRODUCT_NAME,
        pr.PTI = prod.PTI,
        pr.MIN_TENOR = prod.MIN_TENOR,
        pr.MAX_TENOR = prod.MAX_TENOR,
        pr.RATE_INDEX = prod.ID,
        pr.LATE_PENALTY_FEE = prod.LATE_PENALTY_FEE,
        pr.PRE_LIQUIDATION_FEE = prod.EARLY_SETTLEMENT_FEE,
        pr.MAX_QUANTITY_COMMODITIES = prod.MAX_OF_GOODS,
        pr.MAX_LOAN_AMOUNT = prod.MAX_AMOUNT,
        pr.MIN_LOAN_AMOUNT = prod.MIN_AMOUNT,
        pr.EXP_MIN_VALUE = prod.EXP_MIN_VALUE,
        pr.EXP_MAX_VALUE = prod.EXP_MAX_VALUE,
        pr.END_EFF_DATE = nvl(trunc(prod.EXPIRED_DATE),to_date('99991231','YYYYMMDD')),
        pr.LAST_UPDATED_DATE = sysdate,
        pr.STATUS = decode(prod.STATUS,1,'A','C');

    -- Update Employee
      l_msg := 'sync SNW_SLN_STAFF to EMPLOYEES';
     MERGE INTO EMPLOYEES emp USING
     (select * from SNW_SLN_STAFF) st
    ON (emp.STAFF_ID_IN_BPM = st.ID)
    WHEN NOT MATCHED THEN 
    INSERT (CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,FULL_NAME,EMAIL,
        HR_CODE,MOBILE_PHONE,EXT_PHONE,STATUS,START_EFF_DATE,END_EFF_DATE,STAFF_ID_IN_BPM)
    VALUES (st.CREATED_DATE,nvl(st.LAST_UPDATED,sysdate),st.CREATED_BY,st.UPDATED_BY,st.NAME,st.EMAIL,
        substr(st.HR_CODE,1,10),st.MOBILE_PHONE,substr(st.EXT_PHONE,1,10),decode(st.STATUS,'1','A','C'),nvl(st.ACTIVE_DATE,trunc(sysdate)),
        nvl(st.DEACTIVE_DATE,to_date('99991231','YYYYMMDD')),st.ID)
    WHEN MATCHED THEN
    UPDATE SET 
        emp.LAST_UPDATED_DATE = nvl(st.LAST_UPDATED,sysdate),
        emp.LAST_UPDATED_BY = st.UPDATED_BY,
        emp.FULL_NAME = st.NAME,
        emp.EMAIL = st.EMAIL,
        emp.HR_CODE = substr(st.HR_CODE,1,10),
        emp.MOBILE_PHONE = st.MOBILE_PHONE,
        emp.EXT_PHONE = substr(st.EXT_PHONE,1,10),
        emp.STATUS = decode(st.STATUS,'1','A','C'),
        emp.START_EFF_DATE = nvl(st.ACTIVE_DATE,nvl(emp.START_EFF_DATE,trunc(sysdate))),
        emp.END_EFF_DATE = nvl(st.DEACTIVE_DATE,to_date('99991231','YYYYMMDD'));
     
    -- Update Users
      l_msg := 'sync SNW_SLN_STAFF to USERS';
     MERGE INTO USERS usr USING
     (select ss.*,emp.ID EMP_ID from SNW_SLN_STAFF ss inner join
        (select max(ID) ID, AD_CODE from SNW_SLN_STAFF a 
            where exists (select 1 from (select max(nvl(ACTIVE_DATE,trunc(sysdate))) ACTIVE_DATE,AD_CODE from SNW_SLN_STAFF 
            where AD_CODE is not null and STATUS = '1' and nvl(ACTIVE_DATE,trunc(sysdate)) <= sysdate
            group by AD_CODE) b where b.ACTIVE_DATE = nvl(a.ACTIVE_DATE,trunc(sysdate))  and b.AD_CODE = a.AD_CODE)
            and STATUS = '1'
            group by AD_CODE) ad on ss.ID = ad.ID
        left join EMPLOYEES emp on ss.ID = emp.STAFF_ID_IN_BPM) st
    ON (usr.LOGIN_ID = lower(st.AD_CODE))
    WHEN NOT MATCHED THEN 
    INSERT (CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,
        LOGIN_ID,EMP_ID,USR_FULL_NAME,USR_TYPE,STATUS,START_EFF_DATE,END_EFF_DATE)
    VALUES (st.CREATED_DATE,nvl(st.LAST_UPDATED,sysdate),st.CREATED_BY,st.UPDATED_BY,st.AD_CODE,
        st.EMP_ID,st.NAME,(select ID from CODE_TABLE where CATEGORY = 'USER_TYPE' and CODE_VALUE1 = 'S')
        ,decode(st.STATUS,'1','A','C'),nvl(st.ACTIVE_DATE,trunc(sysdate)),
        nvl(st.DEACTIVE_DATE,to_date('99991231','YYYYMMDD')))
    WHEN MATCHED THEN
    UPDATE SET 
        usr.LAST_UPDATED_DATE = nvl(st.LAST_UPDATED,sysdate),
        usr.LAST_UPDATED_BY = st.UPDATED_BY,
        usr.EMP_ID = st.EMP_ID,
        usr.USR_FULL_NAME = st.NAME,
        usr.STATUS = decode(st.STATUS,'1','A','C'),
        usr.END_EFF_DATE = nvl(st.DEACTIVE_DATE,to_date('99991231','YYYYMMDD'));

    -- Update Employee Link
      l_msg := 'sync SNW_SLN_STAFF_POSITION to EMPLOYEE_LINK';
     MERGE INTO EMPLOYEE_LINK el USING
     (select ssp.*,emp.ID EMP_ID from SNW_SLN_STAFF_POSITION ssp 
        inner join (select max(ID) ID, staff_code from SNW_SLN_STAFF_POSITION 
            where trunc(sysdate) between nvl(trunc(ACTIVE_DATE),decode(STATUS,1,trunc(sysdate)-1,sysdate+1)) 
            and nvl(DEACTIVE_DATE,to_date('99991231','YYYYMMDD')) group by staff_code) maxsp
        on ssp.ID = maxsp.ID inner join EMPLOYEES emp on ssp.STAFF_ID = emp.STAFF_ID_IN_BPM) st
    ON (el.EMP_CODE = st.STAFF_CODE)
    WHEN NOT MATCHED THEN 
    INSERT (CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,
        EMP_ID,MANAGER_ID,EMP_POSITION,DEPARTMENT,EMP_CODE,STATUS)
    VALUES (st.CREATED_DATE,nvl(st.LAST_UPDATED,sysdate),st.CREATED_BY,st.UPDATED_BY,
        st.EMP_ID,null,
        nvl((select ID from CODE_TABLE where CATEGORY = 'EM_POS_'||st.BUSINESS_LINE and CODE_VALUE1 = st.POSITION_CODE),0),
        (select ID from CODE_TABLE where CATEGORY = 'BUS_SEGM' and CODE_VALUE1 = st.DEPARTMENT_CODE),
        st.STAFF_CODE,decode(st.STATUS,'1',decode(sign(sysdate-st.active_date),-1,'C',
            decode(sign(nvl(st.deactive_date,to_date('99991231','YYYYMMDD'))-trunc(sysdate)),-1,'C','A')),'C'))
    WHEN MATCHED THEN
    UPDATE SET 
        el.LAST_UPDATED_DATE = nvl(st.LAST_UPDATED,sysdate),
        el.LAST_UPDATED_BY = st.UPDATED_BY,
        el.EMP_ID = st.EMP_ID,
        el.EMP_POSITION = nvl((select ID from CODE_TABLE where CATEGORY = 'EM_POS_'||st.BUSINESS_LINE and CODE_VALUE1 = st.POSITION_CODE),nvl(el.EMP_POSITION,0)),
        el.DEPARTMENT = (select ID from CODE_TABLE where CATEGORY = 'BUS_SEGM' and CODE_VALUE1 = st.DEPARTMENT_CODE),
        el.STATUS = decode(st.STATUS,'1',decode(sign(sysdate-st.active_date),-1,'C',
            decode(sign(nvl(st.deactive_date,to_date('99991231','YYYYMMDD'))-trunc(sysdate)),-1,'C','A')),'C');
     
    -- Update Employee Link setup manager ID
      l_msg := 'sync EMPLOYEE_LINK DSA manager ID';
    FOR i IN (select el.*,ct.CODE_VALUE1 from EMPLOYEE_LINK el 
                inner join CODE_TABLE ct on el.EMP_POSITION = ct.ID
            where ct.CODE_VALUE1 in ('BDS','DES')
            order by el.DEPARTMENT, el.EMP_CODE, decode(el.STATUS,'A',2,1))
    LOOP
        update EMPLOYEE_LINK set
            MANAGER_ID = (select EMP_ID from EMPLOYEE_LINK where EMP_CODE = i.EMP_CODE)
        where DEPARTMENT = i.DEPARTMENT and ID <> i.ID;
    END LOOP;
    
      l_msg := 'sync EMPLOYEE_LINK BDS manager ID';
    FOR i IN (select el1.EMP_ID ASM_ID,el1.EMP_CODE ASM_CODE, el.* from EMPLOYEE_LINK el 
        inner join CODE_TABLE ct on el.EMP_POSITION = ct.ID
        inner join CODE_TABLE ct1 on el.DEPARTMENT = ct1.ID
        inner join EMPLOYEE_LINK el1 on el1.DEPARTMENT = ct1.PARENT_ID
        where ct.CODE_VALUE1 = 'BDS')
    LOOP
        update EMPLOYEE_LINK set
            MANAGER_ID = i.ASM_ID
        where ID = i.ID;
    END LOOP;

      l_msg := 'sync EMPLOYEE_LINK ASM manager ID';
    FOR i IN (select el1.EMP_ID RSM_ID,el1.EMP_CODE RSM_CODE, el.* from EMPLOYEE_LINK el 
        inner join CODE_TABLE ct on el.EMP_POSITION = ct.ID
        inner join CODE_TABLE ct1 on el.DEPARTMENT = ct1.ID
        inner join EMPLOYEE_LINK el1 on el1.DEPARTMENT = ct1.PARENT_ID
        where ct.CODE_VALUE1 = 'ASM')
    LOOP
        update EMPLOYEE_LINK set
            MANAGER_ID = i.RSM_ID
        where ID = i.ID;
    END LOOP;

      l_msg := 'sync DOCUMENTS';
     MERGE INTO DOCUMENTS doc USING
     (select * from PRD_DOCUMENTS) pd
    ON (doc.ID = pd.ID)
    WHEN NOT MATCHED THEN 
    INSERT (ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,DOCUMENT_ORIGINALITY,
        DOCUMENT_CODE,DOCUMENT_NAME,DOCUMENT_TYPE,EXTENSIONS,MAX_SIZE,STATUS)
    VALUES (pd.ID,pd.CREATED_DATE,sysdate,pd.CREATED_BY,null,'S',
        pd.DOCUMENT_CODE,pd.DOCUMENT_NAME,1,pd.EXTS,pd.MAX_SIZE,decode(pd.STATUS,'1','A','C'))
    WHEN MATCHED THEN
    UPDATE SET 
        doc.LAST_UPDATED_DATE = sysdate,
        doc.DOCUMENT_NAME = pd.DOCUMENT_NAME,
        doc.EXTENSIONS = pd.EXTS,
        doc.MAX_SIZE = pd.MAX_SIZE,
        doc.STATUS = decode(pd.STATUS,'1','A','C');

      l_msg := 'sync GROUP_DOCUMENTS';
     MERGE INTO CHECKLIST_WORKFLOW chk USING
     (select * from PRD_GROUP_DOCUMENTS) gd
    ON (chk.ID = gd.ID)
    WHEN NOT MATCHED THEN 
    INSERT (ID,CREATED_DATE,LAST_UPDATED_DATE,CREATED_BY,LAST_UPDATED_BY,WORKFLOW,CHECKLIST_NAME,COLUMN_ORDER)
    VALUES (gd.ID,gd.CREATED_DATE,sysdate,gd.CREATED_BY,null,
        decode(gd.PROCESS_ID,'92489624057e0e0abafb278083044537','InstallmentLoan','357116699583cdc94981987059206300','CashLoan','19140873159635a493e08c3077475860','ConcentratingDataEntry','Others'),
        gd.GROUP_NAME,gd.COLUMN_ORDER)
    WHEN MATCHED THEN
    UPDATE SET 
        chk.LAST_UPDATED_DATE = sysdate,
        chk.CHECKLIST_NAME = gd.GROUP_NAME,
        chk.WORKFLOW = decode(gd.PROCESS_ID,'92489624057e0e0abafb278083044537','InstallmentLoan','357116699583cdc94981987059206300','CashLoan','19140873159635a493e08c3077475860','ConcentratingDataEntry','Others'),
        chk.COLUMN_ORDER = gd.COLUMN_ORDER;

      l_msg := 'sync PROCESS_HAS_DOCUMENTS';
     MERGE INTO DOCUMENT_CHECKLIST dc USING
     (select ppd.*,pd.ID DOC_ID from PRD_PROCESS_HAS_DOCUMENTS ppd 
        inner join PRD_DOCUMENTS pd on ppd.DOCUMENT_ID = pd.ID) ppd
    ON (dc.DOCUMENT_ID = ppd.DOC_ID and dc.CHECKLIST_ID = ppd.GROUP_ID
    and dc.WORKFLOW = decode(ppd.PROCESS_ID,'92489624057e0e0abafb278083044537','InstallmentLoan','357116699583cdc94981987059206300','CashLoan','19140873159635a493e08c3077475860','ConcentratingDataEntry','Others'))
    WHEN NOT MATCHED THEN 
    INSERT (DOCUMENT_ID,CHECKLIST_ID,WORKFLOW)
    VALUES (ppd.DOC_ID,ppd.GROUP_ID,
        decode(ppd.PROCESS_ID,'92489624057e0e0abafb278083044537','InstallmentLoan','357116699583cdc94981987059206300','CashLoan','19140873159635a493e08c3077475860','ConcentratingDataEntry','Others'));

exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20003,'Exception in update_sync_product_data. Message='||l_msg||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure update_sync_main is
    ret int := 0;
    l_count int := 0;
    l_errmsg varchar2(1000);
    l_start_date    DATE;
    l_proc_name varchar2(100);
begin
    select sysdate into l_start_date from dual;
    --UPDATE_SYNC_CODETABLE_DATA
    l_proc_name := 'UPDATE_SYNC_CODETABLE_DATA';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('SLN_DEPARTMENT','SLN_DISTRICT','SLN_PROVINCE','SLN_WARD','GOODS','BRAND','BRAND_IN_GOODS',
        'SYS_CATEGORY','DE_SYS_CATEGORY','SYS_CATEGORY_DETAIL','DE_SYS_CATEGORY_DETAIL','BLACKLIST','WATCHLIST','SLN_DEPARTMENT');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('SLN_DEPARTMENT','SLN_DISTRICT',
            'SLN_PROVINCE','SLN_WARD','GOODS','BRAND','BRAND_IN_GOODS',
            'SYS_CATEGORY','DE_SYS_CATEGORY','SYS_CATEGORY_DETAIL','DE_SYS_CATEGORY_DETAIL','BLACKLIST','WATCHLIST','SLN_DEPARTMENT');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_CODETABLE_DATA;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('SLN_DEPARTMENT','SLN_DISTRICT','SLN_PROVINCE','SLN_WARD','GOODS','BRAND','BRAND_IN_GOODS',
                'SYS_CATEGORY','DE_SYS_CATEGORY','SYS_CATEGORY_DETAIL','DE_SYS_CATEGORY_DETAIL','BLACKLIST','WATCHLIST','SLN_DEPARTMENT');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('SLN_DEPARTMENT','SLN_DISTRICT','SLN_PROVINCE','SLN_WARD','GOODS','BRAND','BRAND_IN_GOODS',
                    'SYS_CATEGORY','DE_SYS_CATEGORY','SYS_CATEGORY_DETAIL','DE_SYS_CATEGORY_DETAIL','BLACKLIST','WATCHLIST','SLN_DEPARTMENT');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_CODETABLE_DATA',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;
    --UPDATE_SYNC_BPM_VARIABLE
    l_proc_name := 'UPDATE_SYNC_BPM_VARIABLE';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('PROCESS_VARIABLES');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('PROCESS_VARIABLES');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_BPM_VARIABLE;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('PROCESS_VARIABLES');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('PROCESS_VARIABLES');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_BPM_VARIABLE',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;
    -- UPDATE_SYNC_PRODUCT_DATA
    l_proc_name := 'UPDATE_SYNC_PRODUCT_DATA';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('PRODUCTS','PROCESS_PRODUCTS','PROCESS_PARAMETERS','PRODUCT_CONFIG_PARAMETERS',
        'SLN_STAFF','SLN_STAFF_POSITION','DOCUMENTS','GROUP_DOCUMENTS','PROCESS_HAS_DOCUMENTS');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('PRODUCTS','PROCESS_PRODUCTS',
            'PROCESS_PARAMETERS','PRODUCT_CONFIG_PARAMETERS','SLN_STAFF','SLN_STAFF_POSITION',
            'DOCUMENTS','GROUP_DOCUMENTS','PROCESS_HAS_DOCUMENTS');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_PRODUCT_DATA;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('PRODUCTS','PROCESS_PRODUCTS','PROCESS_PARAMETERS','PRODUCT_CONFIG_PARAMETERS',
                'SLN_STAFF','SLN_STAFF_POSITION','DOCUMENTS','GROUP_DOCUMENTS','PROCESS_HAS_DOCUMENTS');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('PRODUCTS','PROCESS_PRODUCTS','PROCESS_PARAMETERS','PRODUCT_CONFIG_PARAMETERS',
                    'SLN_STAFF','SLN_STAFF_POSITION','DOCUMENTS','GROUP_DOCUMENTS','PROCESS_HAS_DOCUMENTS');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_PRODUCT_DATA',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;
    -- UPDATE_SYNC_COMPANY_DATA
    l_proc_name := 'UPDATE_SYNC_COMPANY_DATA';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('CAT_CATEGORIES_DETAIL','COMPANY','CAT_INFO');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('CAT_CATEGORIES_DETAIL','COMPANY','CAT_INFO');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_COMPANY_DATA;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('CAT_CATEGORIES_DETAIL','COMPANY','CAT_INFO');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('CAT_CATEGORIES_DETAIL','COMPANY','CAT_INFO');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_COMPANY_DATA',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;

    -- UPDATE_SYNC_APP_TRANSACTION
    l_proc_name := 'UPDATE_SYNC_APP_TRANSACTION';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('APP_TRANSACTION','DE_APP_TRANSACTION');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('APP_TRANSACTION','DE_APP_TRANSACTION');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_APP_TRANSACTION;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('APP_TRANSACTION','DE_APP_TRANSACTION');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('APP_TRANSACTION','DE_APP_TRANSACTION');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_APP_TRANSACTION',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;

    -- UPDATE_SYNC_CREDIT_APP
    l_proc_name := 'UPDATE_SYNC_CREDIT_APP';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('DATA_ENTRY','POS_UPLOAD_CREDIT','DE_DATA_ENTRY','DE_DATA_ENTRY_2');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('DATA_ENTRY','POS_UPLOAD_CREDIT','DE_DATA_ENTRY','DE_DATA_ENTRY_2');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_CREDIT_APP;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('DATA_ENTRY','POS_UPLOAD_CREDIT','DE_DATA_ENTRY','DE_DATA_ENTRY_2');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('DATA_ENTRY','POS_UPLOAD_CREDIT','DE_DATA_ENTRY','DE_DATA_ENTRY_2');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_CREDIT_APP',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;

    -- UPDATE_SYNC_CREDIT_APP_TRAIL
    l_proc_name := 'UPDATE_SYNC_CREDIT_APP_TRAIL';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('DATA_CORRECTOR_REASON_FOR_RETURN','OPERATION_REASON_FOR_RETURN','DE_CALL_APPRAISAL_REASON_FOR_RETURN',
        'DE_DATA_CORRECTOR_REASON_FOR_REJECT','DE_DATA_CORRECTOR_REASON_FOR_RETURN','DE_DATA_ENTRY_2_REASON_FOR_RETURN',
        'DE_DATA_ENTRY_REASON_FOR_RETURN','DE_OPERATION_REASON_FOR_RETURN','CALL_APPRAISAL');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('DATA_CORRECTOR_REASON_FOR_RETURN',
        'OPERATION_REASON_FOR_RETURN','DE_CALL_APPRAISAL_REASON_FOR_RETURN','DE_DATA_CORRECTOR_REASON_FOR_REJECT',
        'DE_DATA_CORRECTOR_REASON_FOR_RETURN','DE_DATA_ENTRY_2_REASON_FOR_RETURN',
        'DE_DATA_ENTRY_REASON_FOR_RETURN','DE_OPERATION_REASON_FOR_RETURN','CALL_APPRAISAL');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_CREDIT_APP_TRAIL;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('DATA_CORRECTOR_REASON_FOR_RETURN','OPERATION_REASON_FOR_RETURN',
            'DE_CALL_APPRAISAL_REASON_FOR_RETURN','DE_DATA_CORRECTOR_REASON_FOR_REJECT','DE_DATA_CORRECTOR_REASON_FOR_RETURN',
            'DE_DATA_ENTRY_2_REASON_FOR_RETURN','DE_DATA_ENTRY_REASON_FOR_RETURN','DE_OPERATION_REASON_FOR_RETURN','CALL_APPRAISAL');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('DATA_CORRECTOR_REASON_FOR_RETURN','OPERATION_REASON_FOR_RETURN',
                'DE_CALL_APPRAISAL_REASON_FOR_RETURN','DE_DATA_CORRECTOR_REASON_FOR_REJECT','DE_DATA_CORRECTOR_REASON_FOR_RETURN',
                'DE_DATA_ENTRY_2_REASON_FOR_RETURN','DE_DATA_ENTRY_REASON_FOR_RETURN','DE_OPERATION_REASON_FOR_RETURN','CALL_APPRAISAL');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_CREDIT_APP_TRAIL',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;

    -- UPDATE_SYNC_CREDIT_OTHER
    l_proc_name := 'UPDATE_SYNC_CREDIT_OTHER';
    select count(1) into l_count from CDC_SYNC_LOG where LAST_UPDATE_DATE >= LAST_SYNC
    and TABLE_NAME in ('APPROVE');

    IF l_count > 0 THEN
        l_start_date := sysdate;
        update CDC_SYNC_LOG set status = 'P',ERROR_MSG = null where TABLE_NAME in ('APPROVE');
        commit;
        begin
            l_errmsg := null;
            UPDATE_SYNC_CREDIT_OTHER;
            update CDC_SYNC_LOG set 
                status = 'S',
                LAST_SYNC = l_start_date,
                FINISH_DATE = sysdate
            where TABLE_NAME in ('APPROVE');
        exception
            when others then
                l_errmsg := sqlerrm;
                rollback;
                update CDC_SYNC_LOG set 
                    status = 'E',
                    FINISH_DATE = sysdate,
                    ERROR_MSG = l_errmsg
                where TABLE_NAME in ('APPROVE');
        end;
        insert into SYNC_LOG_HIST(PROC_NAME,START_TIME,END_TIME,STATUS,ERROR_MSG)
        select 'UPDATE_SYNC_CREDIT_OTHER',l_start_date,sysdate,decode(l_errmsg,null,'S','E'),l_errmsg from dual;
        commit;
    END IF;


exception
    when others then
        RAISE_APPLICATION_ERROR(-20001,'Exception at Procedure ='||l_proc_name||'. Err='||sqlerrm);
end;
/
