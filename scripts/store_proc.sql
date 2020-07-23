CREATE OR REPLACE procedure          insert_products is
begin
    insert into INTEREST_TABLE(ID,RATE_CODE,RATE_NAME,MONTHLY_RATE,YEARLY_RATE,STATUS,START_EFF_DATE,END_EFF_DATE)
    select ID, 'F'||lpad(ID,5,'0'), 'LS '||PRODUCT_NAME,MONTH_INTEREST,YEAR_INTEREST,'A',nvl(APPLIED_DATE,trunc(sysdate)),EXPIRED_DATE
    from UPL_BPM_PRODUCTS;
    insert into PRODUCTS(ID,PRODUCT_CATEGORY_ID,PRODUCT_GROUP_ID,PRODUCT_CODE,PRODUCT_NAME,CCY,PTI,
        RATE_INDEX,STATUS,START_EFF_DATE,END_EFF_DATE)
    select ID,CATEGORY_ID,CATEGORY_ID,PRODUCT_CODE,PRODUCT_NAME,'VND',PTI,
        ID,'A',APPLIED_DATE,EXPIRED_DATE from UPL_BPM_PRODUCTS;
    commit;
exception
    when others then
        DBMS_OUTPUT.PUT_LINE('Exception roi ' || sqlerrm);
end;
/

CREATE OR REPLACE procedure maintenance_task is
    l_msg varchar2(200);
    l_schema varchar2(100);
begin

    -- delete unused records
    l_msg := 'delete from SCHEDULE_INSTANCE';
    delete from SCHEDULE_INSTANCE where CREATED_DATE < add_months(sysdate,-1) and STATUS = 'S';
    delete from SCHEDULE_INSTANCE where CREATED_DATE < add_months(sysdate,-3) and STATUS <> 'S';
    l_msg := 'delete from SYNC_LOG_HIST';
    delete from SYNC_LOG_HIST where START_TIME < add_months(sysdate,-1);
    commit;
    -- Rebuild all index
    FOR i IN (select index_name,table_name from user_indexes where index_type <> 'LOB')
    LOOP
        l_msg := 'Rebuild '||i.index_name||' on '||i.table_name||' table.';
        EXECUTE IMMEDIATE ('alter index '||i.index_name||' rebuild TABLESPACE MCPORTAL_INDEX');
    END LOOP;
    -- Run tables statistic
    select sys_context('userenv','current_schema') into l_schema from dual;
    FOR i IN (select table_name from user_tables)
    LOOP
        l_msg := 'Gather statistics of '||i.table_name||' table.';
        dbms_stats.gather_table_stats(l_schema,i.table_name,cascade=>TRUE);
    END LOOP;
    
    
exception
    when others then
        rollback;
        RAISE_APPLICATION_ERROR(-20010,'Exception in maintenance_task. Message='||l_msg||'. Err='||sqlerrm);
end;
/

CREATE OR REPLACE procedure proc_example is
    ret int := 0;
begin
    update PARAMETERS a set A.PARAM_VALUE = 'DEF' where A.PARAM_NAME = 'TEST';
exception
    when others then
        DBMS_OUTPUT.PUT_LINE('Exception roi ' || sqlerrm);
end;
/

CREATE OR REPLACE procedure update_card_statement is
begin
    -- Update static data once
    MERGE INTO CREDIT_APP_LMS CAL USING
     (select * from UPL_CC_STATEMENT) UCS
    ON (CAL.CORE_LN_APP_ID = UCS.CARD_ID)
    WHEN MATCHED THEN
    UPDATE SET 
      CAL.ACTUAL_CLOSED_DATE = UCS.NGAY_DONG_THE
    WHERE CAL.ACTUAL_CLOSED_DATE is null;

    -- Update static data once
    MERGE INTO CREDIT_APP_ADDITIONAL CAA USING
     (select CAL.credit_app_id,UCS.* from CREDIT_APP_LMS CAL 
        left join UPL_CC_STATEMENT UCS on CAL.CORE_LN_APP_ID = UCS.CARD_ID) UCS
    ON (CAA.credit_app_id = UCS.credit_app_id)
    WHEN MATCHED THEN
    UPDATE SET 
      CAA.card_class = UCS.HANG_THE,
      CAA.card_type = UCS.LOAI_THE,
      CAA.card_activate_date = UCS.NGAY_KICH_HOAT_THE,
      CAA.card_scheme_id = (select id from CODE_TABLE where CATEGORY = 'CARD_SCHEM' and CODE_VALUE1 = UCS.SCHEME_CODE),
      CAA.card_status_id = (select id from CODE_TABLE where CATEGORY = 'CARD_STAT' and CODE_VALUE1 = UCS.CARD_STATUS_CODE),
      CAA.card_branch_code = UCS.MA_CN,
      CAA.card_issue_date = UCS.NGAY_MO_THE,
      CAA.card_expire_date = decode(nvl(UCS.CARD_EXPIRE,'XYZ'),'XYZ',null,substr(UCS.CARD_EXPIRE,3,2)||'/'||substr(UCS.CARD_EXPIRE,1,2)),
      CAA.CARD_CONTRACT_NUMBER = UCS.CONTRACT_NUMBER
    WHERE CAA.card_issue_date is null;

    MERGE INTO CREDIT_APP_LMS CAL USING
     (select * from UPL_CC_STATEMENT) UCS
    ON (CAL.CORE_LN_APP_ID = UCS.CARD_ID)
    WHEN MATCHED THEN
    UPDATE SET 
      CAL.contract_status_id = (select id from CODE_TABLE where CATEGORY = 'CONT_STAT' and CODE_VALUE2 = UCS.CONTRACT_STATUS_CODE),
      CAL.day_past_due = UCS.SO_NGAY_QUA_HAN_TREN_WAY4,
      CAL.loan_group_id = (select id from CODE_TABLE where CATEGORY = 'LN_GROUP' and CODE_VALUE1 = UCS.NHOM_NO_WAY4);

      
     MERGE INTO CREDIT_APP_OUTSTANDING CAO USING
     (select CAL.credit_app_id,UCS.* from CREDIT_APP_LMS CAL 
        left join UPL_CC_STATEMENT UCS on CAL.CORE_LN_APP_ID = UCS.CARD_ID) UCS
    ON (CAO.credit_app_id = UCS.credit_app_id)
    WHEN NOT MATCHED THEN 
    INSERT (
      CREDIT_APP_ID,REMAIN_LIMIT,CURRENT_BALANCE,DUE_BALANCE,NEXT_DUE_BALANCE,MIN_PAYMENT_AMOUNT,
        REMAIN_MIN_AMOUNT,PRINCIPAL_AMOUNT,DUE_INT_AMOUNT,PENALTY_INT_AMOUNT,FEE_AMOUNT,
        OVERDUE_PRINCIPAL_AMOUNT,DUE_FEE_AMOUNT,LOYALTY_AMOUNT,PAYMENT_AMOUNT)
    VALUES (
        UCS.credit_app_id,
        nvl(UCS.HAN_MUC_CON_LAI,0),
        nvl(UCS.DU_NO_THOI_DIEM_BC,0),
        sign(nvl(UCS.DU_NO_DEN_HAN_TT,0)) * ceil(abs(nvl(UCS.DU_NO_DEN_HAN_TT,0))),
        nvl(UCS.DU_NO_CHUA_DEN_HAN_TT,0),
        nvl(UCS.SO_TIEN_CAN_TT_TOI_THIEU,0),
        sign(nvl(UCS.SO_TIEN_MIN_CON_LAI,0)) * ceil(abs(nvl(UCS.SO_TIEN_MIN_CON_LAI,0))),
        nvl(UCS.GOC_TAI_THOI_DIEM_BC,0),
        nvl(UCS.LAI_TRONG_HAN,0),
        nvl(UCS.LAI_PHAT,0),
        nvl(UCS.TONG_PHI,0),
        nvl(UCS.GOC_QUA_HAN_TAI_THOI_DIEM_BC,0),
        nvl(UCS.TONG_PHI_DEN_HAN_TT,0),
        nvl(UCS.SO_DIEM_LOYALTY_CUOI_KY,0),
        0)
    WHEN MATCHED THEN
    UPDATE SET 
        CAO.remain_limit = nvl(UCS.HAN_MUC_CON_LAI,0),
        CAO.current_balance = nvl(UCS.DU_NO_THOI_DIEM_BC,0),
        CAO.due_balance = sign(nvl(UCS.DU_NO_DEN_HAN_TT,0)) * ceil(abs(nvl(UCS.DU_NO_DEN_HAN_TT,0))),
        CAO.next_due_balance = nvl(UCS.DU_NO_CHUA_DEN_HAN_TT,0),
        CAO.min_payment_amount = nvl(UCS.SO_TIEN_CAN_TT_TOI_THIEU,0),
        CAO.remain_min_amount = sign(nvl(UCS.SO_TIEN_MIN_CON_LAI,0)) * ceil(abs(nvl(UCS.SO_TIEN_MIN_CON_LAI,0))),
        CAO.principal_amount = nvl(UCS.GOC_TAI_THOI_DIEM_BC,0),
        CAO.due_int_amount = nvl(UCS.LAI_TRONG_HAN,0),
        CAO.penalty_int_amount = nvl(UCS.LAI_PHAT,0),
        CAO.fee_amount = nvl(UCS.TONG_PHI,0),
        CAO.overdue_principal_amount = nvl(UCS.GOC_QUA_HAN_TAI_THOI_DIEM_BC,0),
        CAO.due_fee_amount = nvl(UCS.TONG_PHI_DEN_HAN_TT,0),
        CAO.loyalty_amount = nvl(UCS.SO_DIEM_LOYALTY_CUOI_KY,0),
        CAO.payment_amount = 0;

    insert into CREDIT_APP_TRANSACTION(credit_app_id,trans_date,remain_limit,current_balance,due_balance,
        next_due_balance,min_payment_amount,remain_min_amount,principal_amount,due_int_amount,penalty_int_amount,
        fee_amount,overdue_principal_amount,due_fee_amount,loyalty_amount,payment_amount,contract_status_id,
        card_status_id,loan_group_id,day_past_due)
    select CAL.credit_app_id,UCS.REPORT_DATE,UCS.HAN_MUC_CON_LAI,UCS.DU_NO_THOI_DIEM_BC,UCS.DU_NO_DEN_HAN_TT,UCS.DU_NO_CHUA_DEN_HAN_TT,
        UCS.SO_TIEN_CAN_TT_TOI_THIEU,UCS.SO_TIEN_MIN_CON_LAI,UCS.GOC_TAI_THOI_DIEM_BC,UCS.LAI_TRONG_HAN,UCS.LAI_PHAT,
        UCS.TONG_PHI,UCS.GOC_QUA_HAN_TAI_THOI_DIEM_BC,UCS.TONG_PHI_DEN_HAN_TT,UCS.SO_DIEM_LOYALTY_CUOI_KY,0,
        (select id from CODE_TABLE where CATEGORY = 'CONT_STAT' and CODE_VALUE2 = UCS.CONTRACT_STATUS_CODE),
        (select id from CODE_TABLE where CATEGORY = 'CARD_STAT' and CODE_VALUE1 = UCS.CARD_STATUS_CODE),
        (select id from CODE_TABLE where CATEGORY = 'LN_GROUP' and CODE_VALUE1 = UCS.NHOM_NO_WAY4),
        UCS.SO_NGAY_QUA_HAN_TREN_WAY4
        from CREDIT_APP_LMS CAL 
        left join UPL_CC_STATEMENT UCS on CAL.CORE_LN_APP_ID = UCS.CARD_ID;

    commit;
exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE procedure update_core_contract is
begin

    MERGE INTO CREDIT_APP_LMS CAL USING
     (select CAR.ID credit_app_id,UCC.* from CREDIT_APP_REQUEST CAR 
        inner join UPL_CORE_CONTRACT UCC on CAR.MC_CONTRACT_NUMBER = UCC.CONTRACT_REF
        where UCC.STATUS_LD <> 'CANCEL' and not exists (select 1 from (select CONTRACT_REF from UPL_CORE_CONTRACT 
                where STATUS_LD <> 'CANCEL' group by CONTRACT_REF having count(1) > 1) a
                        where a.CONTRACT_REF = UCC.CONTRACT_REF)) UCC
    ON (CAL.credit_app_id = UCC.credit_app_id)
    WHEN NOT MATCHED THEN 
    INSERT (CREDIT_APP_ID,CORE_LN_APP_ID,DISBURSEMENT_DATE,DISBURSEMENT_STATUS,FIRST_PAYMENT_DATE,
        LAST_PAYMENT_DATE,DISBURSEMENT_AMOUNT,DAY_PAST_DUE,LOAN_GROUP_ID,CONTRACT_STATUS_ID,ACTUAL_CLOSED_DATE,MAT_DATE)
    VALUES (
        UCC.credit_app_id,UCC.LD_ID,UCC.VAL_DATE,null,null,null,UCC.AMT,null,null,
        (select ID from CODE_TABLE where CATEGORY = 'CONT_STAT' and CODE_VALUE1 = UCC.STATUS),
        null,UCC.MATDATE_TH)
    WHEN MATCHED THEN
    UPDATE SET 
      CAL.LAST_UPDATED_DATE = sysdate,
      CAL.CORE_LN_APP_ID = UCC.LD_ID,
      CAL.DISBURSEMENT_DATE = UCC.VAL_DATE,
      CAL.MAT_DATE = UCC.MAT_DATE_BD,
      CAL.DISBURSEMENT_AMOUNT = UCC.AMT,
      CAL.ACTUAL_CLOSED_DATE = nvl(UCC.MATDATE_TH, CAL.ACTUAL_CLOSED_DATE),
      CAL.CONTRACT_STATUS_ID = (select ID from CODE_TABLE where CATEGORY = 'CONT_STAT' and CODE_VALUE1 = UCC.STATUS);


    MERGE INTO CREDIT_APP_LMS CAL USING
     (select CAR.ID credit_app_id,UCC.* from CREDIT_APP_REQUEST CAR 
        inner join UPL_CORE_CONTRACT UCC on CAR.MC_CONTRACT_NUMBER = UCC.CONTRACT_REF
        where UCC.STATUS_LD not in ('CANCEL','CLOSE') and exists (select 1 from (select CONTRACT_REF from UPL_CORE_CONTRACT 
                where STATUS_LD <> 'CANCEL' group by CONTRACT_REF having count(1) > 1) a
                        where a.CONTRACT_REF = UCC.CONTRACT_REF)) UCC
    ON (CAL.credit_app_id = UCC.credit_app_id)
    WHEN NOT MATCHED THEN 
    INSERT (CREDIT_APP_ID,CORE_LN_APP_ID,DISBURSEMENT_DATE,DISBURSEMENT_STATUS,FIRST_PAYMENT_DATE,
        LAST_PAYMENT_DATE,DISBURSEMENT_AMOUNT,DAY_PAST_DUE,LOAN_GROUP_ID,CONTRACT_STATUS_ID,ACTUAL_CLOSED_DATE,MAT_DATE)
    VALUES (
        UCC.credit_app_id,UCC.LD_ID,UCC.VAL_DATE,null,null,null,UCC.AMT,null,null,
        (select ID from CODE_TABLE where CATEGORY = 'CONT_STAT' and CODE_VALUE1 = UCC.STATUS),
        null,UCC.MATDATE_TH)
    WHEN MATCHED THEN
    UPDATE SET 
      CAL.LAST_UPDATED_DATE = sysdate,
      CAL.CORE_LN_APP_ID = UCC.LD_ID,
      CAL.DISBURSEMENT_DATE = UCC.VAL_DATE,
      CAL.MAT_DATE = UCC.MAT_DATE_BD,
      CAL.DISBURSEMENT_AMOUNT = UCC.AMT,
      CAL.ACTUAL_CLOSED_DATE = nvl(UCC.MATDATE_TH, CAL.ACTUAL_CLOSED_DATE),
      CAL.CONTRACT_STATUS_ID = (select ID from CODE_TABLE where CATEGORY = 'CONT_STAT' and CODE_VALUE1 = UCC.STATUS);


    commit;
exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE PROCEDURE USERS_SESSION_UPSERT 
(
  LOGINID IN VARCHAR2 
, SESSIONKEY IN VARCHAR2 
) AS 
    l_count number(2) := 0;
begin
    select count(1) into l_count from USERS_SESSION where LOGIN_ID = LOGINID;
    IF l_count > 0 THEN
      update USERS_SESSION 
      set    SESSION_KEY = SESSIONKEY,LAST_UPDATE_DATE = sysdate
      where  LOGIN_ID = LOGINID;
    ELSE
       insert into USERS_SESSION (LOGIN_ID, SESSION_KEY,LAST_UPDATE_DATE) 
          values (LOGINID, SESSIONKEY,sysdate);
    END IF;
exception
   when others then
        RAISE_APPLICATION_ERROR(-20004,'Exception in USERS_SESSION_UPSERT. Message='||sqlerrm);
end;
/

CREATE OR REPLACE PROCEDURE "VALIDATE_UPLOAD_CUSTOMER" (uplDetailId IN INT) is
begin

    -- Kiem tra SDT bi duplicate voi du lieu cu da import tu truoc
    update UPL_CUSTOMER a set RESPONSE_CODE = 'Y' where UPL_DETAIL_ID = uplDetailId 
        and exists (select 1 from (select mobile from UPL_CUSTOMER 
            where UPL_DETAIL_ID != uplDetailId and RESPONSE_CODE='OK') b where b.mobile = a.mobile);
    update UPL_CUSTOMER set RESPONSE_CODE='N' where UPL_DETAIL_ID = uplDetailId and RESPONSE_CODE is null;

    -- Kiem tra SDT bi duplicate trong cung file upload
    update UPL_CUSTOMER a set RESPONSE_CODE=RESPONSE_CODE||'Y' where UPL_DETAIL_ID = uplDetailId 
        and exists (select 1 from (select mobile from UPL_CUSTOMER where UPL_DETAIL_ID = uplDetailId 
        group by mobile having count(1) > 1) b where b.mobile = a.mobile);
    update UPL_CUSTOMER a set RESPONSE_CODE=substr(RESPONSE_CODE,1,1)||'N' where UPL_DETAIL_ID = uplDetailId 
        and length(RESPONSE_CODE) = 2 and exists (select 1 from (select min(ID) minid, mobile 
            from UPL_CUSTOMER where UPL_DETAIL_ID = uplDetailId and length(RESPONSE_CODE) = 2 group by mobile) b 
            where b.minid = a.ID and b.mobile = a.mobile);
    update UPL_CUSTOMER set RESPONSE_CODE=RESPONSE_CODE||'N' where UPL_DETAIL_ID = uplDetailId and length(RESPONSE_CODE)=1;
    -- Kiem tra SDT do khong bat dau bang so 0
    update UPL_CUSTOMER set RESPONSE_CODE=RESPONSE_CODE||'Y' where UPL_DETAIL_ID = uplDetailId 
    and substr(mobile,1,1) <> '0';
    -- Kiem tra SDT bi sai length < 10 hoac length > 11
    update UPL_CUSTOMER set RESPONSE_CODE=RESPONSE_CODE||'Y' where UPL_DETAIL_ID = uplDetailId 
   -- and (length(mobile) < 10 or length(mobile) > 11);
   -- chi cho so dien thoai 10 so 
      and ((length(mobile) <> 10) or (substr(mobile,1,3) not in ('032','033','034','035','036','037','038','039','052','056','058','059','070','076','077','078','079',
    '081','082','083','084','085','086','088','089','090','091','092','093','094','095','096','097','098','099')));
     --and (length(mobile) <> 10)
   -- Danh sách d?u s? validate: 032,033,034,035,036,037,038,039,052,056,058,059,070,076,077,078,079,081,082,083,084,085,086,088,089,(090,091,092,093,094,095,096,097,098,099) 
    --or  NOT REGEXP_LIKE(mobile, '^((03[2|3|4|5|6|7|8|9])|(05[2|6|8|9])|(07[0|6|7|8|9])|(08[1|2|3|4|5|6|8|9])|(09[0-9]))+([0-9]{7})$') ; 
    -- Cap nhat thanh cong cho tat ca cac ban ghi OK sau validate
    update UPL_CUSTOMER set RESPONSE_CODE='OK' where UPL_DETAIL_ID = uplDetailId and RESPONSE_CODE='NN';

exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE FUNCTION create_json (
    strin IN VARCHAR2,
    rec_id in number
) RETURN VARCHAR2 IS
    l_str   VARCHAR2(32000) := 'SELECT JSON_OBJECT (''CATEGORY'' VALUE CATEGORY,''DAY'' VALUE DAY,''MONTH'' VALUE MONTH,''YEAR'' VALUE YEAR) FROM calendar where ID = :id';
    l_ret   VARCHAR2(32000);
BEGIN
    EXECUTE IMMEDIATE l_str into l_ret using rec_id;
    --insert into tmp_test values(0,l_ret);
    return l_ret;
END;
/

CREATE OR REPLACE FUNCTION findstr (
    strin IN VARCHAR
) RETURN number IS
    l_num   number;
BEGIN
    select to_number(strin) into l_num from dual;
    return 1;
exception
    when others then
        return 0;
END;
/

CREATE OR REPLACE FUNCTION get_clob (
    clobVal IN CLOB
) RETURN VARCHAR2 IS
    l_len number(10) := 4000;
    l_size number(10) := 0;
BEGIN
    l_size := lengthb(dbms_lob.substr(clobVal, l_len, 1));
    while (l_size > 4000)
    loop
        l_len := l_len - 50;
        l_size := lengthb(dbms_lob.substr(clobVal, l_len, 1));
    end loop;
    return dbms_lob.substr(clobVal, l_len, 1);
END;
/

CREATE OR REPLACE FUNCTION get_code_save_wh (
    doctype IN VARCHAR
) RETURN VARCHAR2 IS
    codesavewh   VARCHAR2(50);
    countsum     NUMBER;
    countt       NUMBER;
    countz        NUMBER;
    counths       NUMBER;
    countCv      NUMBER;
    counttStr    VARCHAR(10);
    countzStr     VARCHAR(10);
    counthsStr    VARCHAR(10);
BEGIN
    --Loai truyen vao la hop dong, hop dong loi hay thu cam on
    IF ((upper(doctype) = 'HÐ') or (upper(doctype) = 'HÐL') or (upper(doctype) = 'TCO')) THEN
        SELECT COUNT(1) INTO countsum FROM wh_code a where a.code like '%'||doctype||'%' ;
        countz := floor(countsum / 20000) + 1;
        if (LENGTH(countz) = 1) then countzStr := '00' || countz;
        elsif (LENGTH(countz) = 2) then countzStr := '0' || countz;
        else
        countzStr := countz;        
        end if;

        countt := floor(MOD(countsum,20000) / 200) + 1;
        if (LENGTH(countt) = 1) then counttStr:= '00' || countt;
        elsif (LENGTH(countt) = 2) then counttStr:= '0' || countt; 
        else
        counttStr := countt;
        end if;

        counths := MOD(MOD(countsum,20000),200) + 1;
        if (LENGTH(counths) = 1) then counthsStr:= '00' || counths;
        elsif (LENGTH(counths) = 2) then counthsStr:= '0' || counths;
        else
        counthsStr := counths;
        end if;  
        codesavewh := countzStr || '-' || upper(doctype) || '-' || counttStr || '-' || counthsStr || '###0';
    ELSIF (upper(doctype) = 'CV') THEN --Loai truyen vao la cavet
        --Tim xem ma cavet nao trong thi gan khong thi sinh ma moi
        select COUNT(1) INTO countCv from wh_code a, code_table b where a.status=b.id and b.code_group='WH' and b.category ='WH_LOGDE' and b.code_value1='WH_LODGED_COMPLETE_RETURN' 
        and ROWNUM=1 ORDER BY a.id;
        IF (countCv = 0) THEN
                SELECT COUNT(1) INTO countsum FROM wh_code a where a.code like '%'||doctype||'%' ;                
                countz := floor(countsum / 15000) + 1;
                if (LENGTH(countz) = 1) then countzStr := '00' || countz;
                elsif (LENGTH(countz) = 2) then countzStr := '0' || countz;
                else
                countzStr := countz;        
                end if;

                countt := floor(MOD(countsum,15000) / 150) + 1;
                if (LENGTH(countt) = 1) then counttStr:= '00' || countt;
                elsif (LENGTH(countt) = 2) then counttStr:= '0' || countt; 
                else
                counttStr := countt;
                end if;

                counths := MOD(MOD(countsum,15000),150) + 1;
                if (LENGTH(counths) = 1) then counthsStr:= '00' || counths;
                elsif (LENGTH(counths) = 2) then counthsStr:= '0' || counths;
                else
                counthsStr := counths;
                end if;
                codesavewh := countzStr || '-' || upper(doctype) || '-' || counttStr || '-' || counthsStr || '###0'; 
        ELSE
            select a.code INTO codesavewh from wh_code a, code_table b where a.status=b.id and b.code_group='WH' and b.category ='WH_LOGDE' and b.code_value1='WH_LODGED_COMPLETE_RETURN' 
        and ROWNUM=1 ORDER BY a.id;
            codesavewh := codesavewh || '###1';
        END IF;       
    ELSE
        DBMS_OUTPUT.PUT_LINE('Doc type is Err'); 
    END IF;        

    RETURN codesavewh;
END;
/

CREATE OR REPLACE FUNCTION get_md5 (
    idNum IN VARCHAR,
    birthDate IN DATE
) RETURN VARCHAR2 IS
    l_ret VARCHAR2(100);
    l_long number(20) := 0;
BEGIN
    IF birthDate is not null THEN
        select ((birthDate - TO_DATE('19700101','yyyymmdd'))-7/24)*86400000 into l_long from dual;
    END IF;
    select lower(DBMS_CRYPTO.HASH(RAWTOHEX(idNum||'-'||to_char(l_long)),2)) into l_ret from dual;
    return l_ret;
END;
/

CREATE OR REPLACE FUNCTION get_string (
    strVal IN VARCHAR2,
    maxLen IN number
) RETURN VARCHAR2 IS
    l_len number(5) := maxLen;
    l_size number(5) := 0;
    l_step number(5) := trunc(maxLen/50) + 1;
BEGIN
    l_size := lengthb(strVal);
    IF l_size <= maxLen THEN
        return strVal;
    ELSE
        while (l_size > maxLen)
        loop
            l_len := l_len - l_step;
            l_size := lengthb(substr(strVal, 1, l_len));
        end loop;
        return substr(strVal, 1, l_len);
    END IF;
END;
/

CREATE OR REPLACE FUNCTION is_number (
    strin IN VARCHAR
) RETURN number IS
    l_num   number;
BEGIN
    select to_number(strin) into l_num from dual;
    return 1;
exception
    when others then
        return 0;
END;
/
