CREATE OR REPLACE procedure allocate_To_TSA_ByAttributes(allocMasterId IN INT, 
    teamId IN INT, jsonMember IN VARCHAR2) is
    allocmaster ALLOCATION_MASTER%ROWTYPE;
    code CODE_TABLE%ROWTYPE;
    numOfMem int := 1;
    numOfInsert int := 1;
    allocatedLeft int := 0;
    remain4Team int := 0;
    tlAllocated int := 0;
    TYPE CustTabType IS TABLE OF UPL_CUSTOMER%ROWTYPE INDEX BY BINARY_INTEGER;
    cust CustTabType;
    totalAttributes int := 0;
    runTmp int := 0;
begin
    -- Lay ban ghi allocation master cua lan phan bo nay
    select * into allocmaster from ALLOCATION_MASTER where ID = allocMasterId;
    tlAllocated := allocmaster.ALLOCATED_NUMBER;
    -- Lay code table cho allocated to
    select * into code from CODE_TABLE where ID = allocmaster.ALLOCATED_TO;
    -- Kiem tra cach thuc phan do de tien hanh phan bo
    IF code.CODE_VALUE1 = 'ALL_TEAMMEM' THEN
        -- Lay so luong team member duoc phan bo
        select count(1) into numOfMem from TEAM_MEMBER where TEAM_ID = teamId;
    ELSIF code.CODE_VALUE1 = 'ALL_TEAMMEM_EXCEPT' THEN
        -- Lay so luong team member duoc phan bo
        select count(1) into numOfMem from TEAM_MEMBER tm
        where TEAM_ID = teamId and not exists 
        (select 1 from (with json as
                ( select jsonMember doc from   dual) 
                SELECT value FROM json_table((select doc from json) , '$[*]'
                                COLUMNS (value PATH '$')))
        where value = tm.USER_ID);
    END IF;
    
    IF numOfMem <= 0 THEN
        RAISE_APPLICATION_ERROR(-20004,'There is no member to allocate in team. Team id='||teamId);
    END IF;

    -- Main loop dua tren Upload Detail
    FOR i IN (select ad.*,ct.CODE_VALUE1 from ALLOCATION_DETAIL ad 
        inner join UPL_DETAIL ud on ad.UPL_DETAIL_ID = ud.ID
        left join CODE_TABLE ct on ct.ID = ad.STATUS
        where ad.ALLOCATION_MASTER_ID = allocmaster.RELATED_ALLOCATION
        and ad.OBJECT_TYPE = 'T' and ad.ASSIGNEE_ID = teamId
        and ct.CODE_VALUE1 in ('N', 'P')
        order by ud.UPL_SEQ, decode(ct.CODE_VALUE1,'P',1,2))
    LOOP
        EXIT WHEN (tlAllocated <= 0);
        -- So luong chua phan bo con lai cua upload detail cho team
        select (i.ALLOCATED_NUMBER - count(1)) into allocatedLeft
        from ALLOCATION_DETAIL ad
        where ad.OBJECT_TYPE = 'U' and ad.UPL_DETAIL_ID = i.UPL_DETAIL_ID
            and exists (select 1 from ALLOCATION_DETAIL ad1
            inner join ALLOCATION_MASTER am on ad1.ALLOCATION_MASTER_ID = am.RELATED_ALLOCATION 
            where am.ID = ad.ALLOCATION_MASTER_ID and ad1.UPL_DETAIL_ID = ad.UPL_DETAIL_ID
            and ad1.ASSIGNEE_ID = teamId and ad1.UPL_DETAIL_ID = i.UPL_DETAIL_ID and OBJECT_TYPE = 'T');
        -- calculate number can allocate
        select least(tlAllocated,allocatedLeft) into remain4Team from dual;
        -- Loop by attributes
        totalAttributes := 1;
        FOR k in (select uc.pre_product_code,uc.data_source,count(1) subtotal_attr
                ,sum(count(1)) over (partition by 1) total
            from UPL_CUSTOMER uc where not exists (select 1 from ALLOCATION_DETAIL ad
                where ad.UPL_OBJECT_ID = uc.ID and ad.UPL_DETAIL_ID = uc.UPL_DETAIL_ID)
                and uc.UPL_DETAIL_ID = i.UPL_DETAIL_ID
            group by uc.pre_product_code,uc.data_source
            order by uc.pre_product_code,uc.data_source)
        LOOP
            cust(totalAttributes).pre_product_code := k.pre_product_code;
            cust(totalAttributes).data_source := k.data_source;
            cust(totalAttributes).ID := k.subtotal_attr;
            totalAttributes := totalAttributes + 1;
        END LOOP;
        
        LOOP
            EXIT WHEN (remain4Team <= 0);
            runTmp := 1;
            LOOP
                EXIT WHEN (runTmp = totalAttributes) OR (remain4Team <= 0);
                IF cust(runTmp).ID > 0 THEN
                    select least(least(cust(runTmp).ID, numOfMem),remain4Team) into numOfInsert from dual;
                    -- Assign case cho user
                    insert into ALLOCATION_DETAIL(ALLOCATION_MASTER_ID,UPL_DETAIL_ID,ALLOCATED_NUMBER,
                        OBJECT_TYPE,ASSIGNEE_ID,UPL_OBJECT_ID,STATUS)
                    select allocMasterId, i.UPL_DETAIL_ID, 1, 'U', ur.USER_ID, ulc.ID,
                    (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F') from 
                    (select tm.USER_ID,row_number() over (partition by 1 order by tm.USER_ID) row_num 
                        from TEAM_MEMBER tm where TEAM_ID = teamId
                                    and not exists (select 1 from (with json as
                                    ( select jsonMember doc from   dual) 
                                    SELECT value FROM json_table((select doc from json) , '$[*]'
                                                    COLUMNS (value PATH '$')))
                            where value = tm.USER_ID)) ur inner join 
                    (select uc.ID,row_number() over (partition by 1 order by uc.ID) row_num
                    from UPL_CUSTOMER uc where not exists (select 1 from ALLOCATION_DETAIL ad
                        where ad.UPL_OBJECT_ID = uc.ID and ad.UPL_DETAIL_ID = uc.UPL_DETAIL_ID)
                        and uc.UPL_DETAIL_ID = i.UPL_DETAIL_ID and uc.pre_product_code = cust(runTmp).pre_product_code
                        and uc.data_source = cust(runTmp).data_source
                        and rownum <= numOfInsert) ulc on ur.row_num = ulc.row_num;

                    cust(runTmp).ID := cust(runTmp).ID - numOfInsert;
                    remain4Team := remain4Team - numOfInsert;
                    tlAllocated := tlAllocated - numOfInsert;
                    allocatedLeft := allocatedLeft - numOfInsert;
                    runTmp := runTmp + 1;
                END IF;
            END LOOP;
        END LOOP;
        -- update all completed for upload detail
        IF allocatedLeft <= 0 THEN
            update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE 
                where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'A') where ID = i.ID;
        ELSE
            update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE 
                where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'P') where ID = i.ID;
        END IF;
    END LOOP;

    
exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE procedure allocate_To_TSA_detail(allocMasterId IN INT, 
    teamId IN INT, jsonMember IN VARCHAR2) is
    allocmaster ALLOCATION_MASTER%ROWTYPE;
    code CODE_TABLE%ROWTYPE;
    numOfMem int := 1;
    caseAllocated int := 0;
    remain int := 0;
    userAllocated int := 0;
    uploadSrc varchar2(30) := null;
    totalAllocated int := 0;
    lastDetailID int := 0;
    totalUploaded int := 0;
    numOfInsert int := 1;
    allocatedLeft int := 0;
    remain4Team int := 0;
    tlAllocated int := 0;
    TYPE CustTabType IS TABLE OF UPL_CUSTOMER%ROWTYPE INDEX BY BINARY_INTEGER;
    cust CustTabType;
    totalAttributes int := 0;
    runTmp int := 0;
begin
    -- Lay ban ghi allocation master cua lan phan bo nay
    select * into allocmaster from ALLOCATION_MASTER where ID = allocMasterId;
    tlAllocated := allocmaster.ALLOCATED_NUMBER;
    -- Lay code table cho allocated to
    select * into code from CODE_TABLE where ID = allocmaster.ALLOCATED_TO;
    -- Lay nguon upload
    select ct.CODE_VALUE1 into uploadSrc
    from UPL_MASTER um inner join ALLOCATION_MASTER am on um.ID = am.UPL_MASTER_ID
    inner join CODE_TABLE ct on ct.ID = um.FROM_SOURCE
    where am.ID = allocMasterId;
    -- Kiem tra cach thuc phan do de tien hanh phan bo
    IF code.CODE_VALUE1 = 'ALL_TEAMMEM' THEN
        -- Lay so luong team member duoc phan bo
        select count(1) into numOfMem from TEAM_MEMBER where TEAM_ID = teamId and STATUS = 'A';
    ELSIF code.CODE_VALUE1 = 'ALL_TEAMMEM_EXCEPT' THEN
        -- Lay so luong team member duoc phan bo
        select count(1) into numOfMem from TEAM_MEMBER tm
        where TEAM_ID = teamId and tm.STATUS = 'A' and not exists 
        (select 1 from (with json as
                ( select jsonMember doc from   dual) 
                SELECT value FROM json_table((select doc from json) , '$[*]'
                                COLUMNS (value PATH '$')))
        where value = tm.USER_ID);
    ELSIF code.CODE_VALUE1 = 'OWNER' THEN
        insert into ALLOCATION_DETAIL(ALLOCATION_MASTER_ID,UPL_DETAIL_ID,ALLOCATED_NUMBER,
            OBJECT_TYPE,ASSIGNEE_ID,UPL_OBJECT_ID,STATUS)
        select allocMasterId, ud.ID, 1, 'U', um.OWNER_ID, uc.ID,
        (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F')
        from ALLOCATION_DETAIL ad inner join UPL_DETAIL ud on ud.ID = ad.UPL_DETAIL_ID
        inner join UPL_MASTER um on um.ID = ud.UPL_MASTER_ID
        inner join UPL_CUSTOMER uc on uc.UPL_DETAIL_ID = ud.ID
        where ad.ALLOCATION_MASTER_ID = allocmaster.RELATED_ALLOCATION and rownum <= ad.ALLOCATED_NUMBER;
        update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'A') 
            where ALLOCATION_MASTER_ID = allocmaster.RELATED_ALLOCATION 
            and status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'N');
    END IF;
    -- Thuc hien phan bo
    IF code.CODE_VALUE1 in ('ALL_TEAMMEM','ALL_TEAMMEM_EXCEPT') THEN
        IF numOfMem <= 0 THEN
            RAISE_APPLICATION_ERROR(-20004,'There is no member to allocate in team. Team id='||teamId);
        END IF;
        -- Main loop dua tren Upload Detail
        FOR i IN (select ad.*,ct.CODE_VALUE1 from ALLOCATION_DETAIL ad 
            inner join UPL_DETAIL ud on ad.UPL_DETAIL_ID = ud.ID
            left join CODE_TABLE ct on ct.ID = ad.STATUS
            where ad.ALLOCATION_MASTER_ID = allocmaster.RELATED_ALLOCATION
            and ad.OBJECT_TYPE = 'T' and ad.ASSIGNEE_ID = teamId
            and ct.CODE_VALUE1 in ('N','P')
            order by ud.UPL_SEQ, decode(ct.CODE_VALUE1,'P',1,2))
        LOOP
            EXIT WHEN (tlAllocated <= 0);
            -- So luong chua phan bo con lai cua upload detail cho team
            select (i.ALLOCATED_NUMBER - count(1)) into allocatedLeft
            from ALLOCATION_DETAIL ad inner join ALLOCATION_MASTER am on ad.ALLOCATION_MASTER_ID = am.ID
            where ad.OBJECT_TYPE = 'U' and ad.UPL_DETAIL_ID = i.UPL_DETAIL_ID
            and am.ALLOCATED_TYPE = 'T' and am.ASSIGNER_ID = teamId;
            -- Tinh toan so luong case con lai de phan bo
            select least(tlAllocated,allocatedLeft) into remain4Team from dual;
            -- Phan bo chia lam 2 truong hop: cho XSELL(MIS) va cho cac truong hop khac 
            IF uploadSrc = 'MIS' THEN
                -- Loop by attributes
                totalAttributes := 1;
                FOR k in (select uc.pre_product_code,uc.data_source,count(1) subtotal_attr
                        ,sum(count(1)) over (partition by 1) total
                    from UPL_CUSTOMER uc where not exists (select 1 from ALLOCATION_DETAIL ad
                        where ad.UPL_OBJECT_ID = uc.ID and ad.UPL_DETAIL_ID = uc.UPL_DETAIL_ID)
                        and uc.UPL_DETAIL_ID = i.UPL_DETAIL_ID
                    group by uc.pre_product_code,uc.data_source
                    order by uc.pre_product_code,uc.data_source)
                LOOP
                    cust(totalAttributes).pre_product_code := k.pre_product_code;
                    cust(totalAttributes).data_source := k.data_source;
                    cust(totalAttributes).ID := k.subtotal_attr;
                    totalAttributes := totalAttributes + 1;
                END LOOP;
                
                LOOP
                    EXIT WHEN (remain4Team <= 0);
                    runTmp := 1;
                    LOOP
                        EXIT WHEN (runTmp = totalAttributes) OR (remain4Team <= 0);
                        IF cust(runTmp).ID > 0 THEN
                            select least(least(cust(runTmp).ID, numOfMem),remain4Team) into numOfInsert from dual;
                            -- Assign case cho user
                            insert into ALLOCATION_DETAIL(ALLOCATION_MASTER_ID,UPL_DETAIL_ID,ALLOCATED_NUMBER,
                                OBJECT_TYPE,ASSIGNEE_ID,UPL_OBJECT_ID,STATUS)
                            select allocMasterId, i.UPL_DETAIL_ID, 1, 'U', ur.USER_ID, ulc.ID,
                            (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F') from 
                            (select tm.USER_ID,row_number() over (partition by 1 order by tm.USER_ID) row_num 
                                from TEAM_MEMBER tm where TEAM_ID = teamId and tm.STATUS = 'A' 
                                            and not exists (select 1 from (with json as
                                            ( select jsonMember doc from   dual) 
                                            SELECT value FROM json_table((select doc from json) , '$[*]'
                                                            COLUMNS (value PATH '$')))
                                    where value = tm.USER_ID)) ur inner join 
                            (select uc.ID,row_number() over (partition by 1 order by uc.ID) row_num
                            from UPL_CUSTOMER uc where not exists (select 1 from ALLOCATION_DETAIL ad
                                where ad.UPL_OBJECT_ID = uc.ID and ad.UPL_DETAIL_ID = uc.UPL_DETAIL_ID)
                                and uc.UPL_DETAIL_ID = i.UPL_DETAIL_ID and uc.pre_product_code = cust(runTmp).pre_product_code
                                and uc.data_source = cust(runTmp).data_source
                                and rownum <= numOfInsert) ulc on ur.row_num = ulc.row_num;

                            cust(runTmp).ID := cust(runTmp).ID - numOfInsert;
                            remain4Team := remain4Team - numOfInsert;
                            tlAllocated := tlAllocated - numOfInsert;
                            allocatedLeft := allocatedLeft - numOfInsert;
                        END IF;
                        runTmp := runTmp + 1;
                    END LOOP;
                END LOOP;
            ELSE
                remain := remain4Team;
                IF remain4Team > numOfMem THEN
                    -- Tinh so luong case phan bo cho 1 member
                    select trunc(remain4Team/numOfMem) into caseAllocated from dual;
                    -- Tinh so du con lai sau khi chia deu
                    numOfInsert := (caseAllocated * numOfMem);
                    remain := remain4Team - numOfInsert;
                    -- Assign case cho user
                    insert into ALLOCATION_DETAIL(ALLOCATION_MASTER_ID,UPL_DETAIL_ID,ALLOCATED_NUMBER,
                        OBJECT_TYPE,ASSIGNEE_ID,UPL_OBJECT_ID,STATUS)
                    select allocMasterId, i.UPL_DETAIL_ID, 1, 'U', ur.USER_ID, ulc.ID,
                    (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F') from 
                    (select tm.USER_ID,row_number() over (partition by 1 order by tm.USER_ID) row_num 
                        from TEAM_MEMBER tm where TEAM_ID = teamId and tm.STATUS = 'A' 
                                    and not exists (select 1 from (with json as
                                    ( select jsonMember doc from   dual) 
                                    SELECT value FROM json_table((select doc from json) , '$[*]'
                                                    COLUMNS (value PATH '$')))
                            where value = tm.USER_ID)) ur inner join 
                    (select uc.ID,row_number() over (partition by 1 order by uc.ID) row_num
                    from UPL_CUSTOMER uc where not exists (select 1 from ALLOCATION_DETAIL ad
                        where ad.UPL_OBJECT_ID = uc.ID and ad.UPL_DETAIL_ID = uc.UPL_DETAIL_ID)
                        and uc.UPL_DETAIL_ID = i.UPL_DETAIL_ID 
                        and rownum <= numOfInsert) ulc on ur.row_num = mod(ulc.row_num,numOfMem)+1;
                    tlAllocated := tlAllocated - numOfInsert;
                    allocatedLeft := allocatedLeft - numOfInsert;
                END IF;
                -- Assign case con lai cho user
                IF remain > 0 THEN
                    insert into ALLOCATION_DETAIL(ALLOCATION_MASTER_ID,UPL_DETAIL_ID,ALLOCATED_NUMBER,
                        OBJECT_TYPE,ASSIGNEE_ID,UPL_OBJECT_ID,STATUS)
                    select allocMasterId, i.UPL_DETAIL_ID, 1, 'U', ur.USER_ID, ulc.ID,
                    (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F') from 
                    (select tm.USER_ID,row_number() over (partition by 1 order by tm.USER_ID) row_num 
                        from TEAM_MEMBER tm where TEAM_ID = teamId and tm.STATUS = 'A' 
                                    and not exists (select 1 from (with json as
                                    ( select jsonMember doc from   dual) 
                                    SELECT value FROM json_table((select doc from json) , '$[*]'
                                                    COLUMNS (value PATH '$')))
                            where value = tm.USER_ID)) ur inner join 
                    (select uc.ID,row_number() over (partition by 1 order by uc.ID) row_num
                    from UPL_CUSTOMER uc where not exists (select 1 from ALLOCATION_DETAIL ad
                        where ad.UPL_OBJECT_ID = uc.ID and ad.UPL_DETAIL_ID = uc.UPL_DETAIL_ID)
                        and uc.UPL_DETAIL_ID = i.UPL_DETAIL_ID 
                        and rownum <= remain) ulc on ur.row_num = ulc.row_num;
                    tlAllocated := tlAllocated - remain;
                    allocatedLeft := allocatedLeft - remain;
                END IF;
            END IF;

            -- update all completed for upload detail
            IF allocatedLeft <= 0 THEN
                update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE 
                    where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'A') where ID = i.ID;
            ELSE
                update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE 
                    where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'P') where ID = i.ID;
            END IF;
        END LOOP;
    END IF;
    
exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE procedure          allocate_To_TSA_detail_TEST(allocMasterId IN INT, teamId IN INT, jsonMember IN VARCHAR2) is
    allocmaster ALLOCATION_MASTER%ROWTYPE;
    code CODE_TABLE%ROWTYPE;
    numOfMem int := 1;
    caseAllocated int := 0;
    remain int := 0;
    userAllocated int := 0;
    tmp1 int := 0;
    totalAllocated int := 0;
    lastDetailID int := 0;
begin
    -- Lay ban ghi allocation master cua lan phan bo nay
    select * into allocmaster from ALLOCATION_MASTER where ID = allocMasterId;
    -- Lay code table cho allocated to
    select * into code from CODE_TABLE where ID = allocmaster.ALLOCATED_TO;
    -- Kiem tra cach thuc phan do de tien hanh phan bo
    IF code.CODE_VALUE1 = 'ALL_TEAMMEM' THEN
        -- Lay so luong team member duoc phan bo
        select count(1) into numOfMem from TEAM_MEMBER where TEAM_ID = teamId;
    ELSIF code.CODE_VALUE1 = 'ALL_TEAMMEM_EXCEPT' THEN
        -- Lay so luong team member duoc phan bo
        select count(1) into numOfMem from TEAM_MEMBER tm
        where TEAM_ID = teamId and not exists 
        (select 1 from (with json as
                ( select jsonMember doc from   dual) 
                SELECT value FROM json_table((select doc from json) , '$[*]'
                                COLUMNS (value PATH '$')))
        where value = tm.USER_ID);
    ELSIF code.CODE_VALUE1 = 'OWNER' THEN
        insert into ALLOCATION_DETAIL(ALLOCATION_MASTER_ID,UPL_DETAIL_ID,ALLOCATED_NUMBER,
            OBJECT_TYPE,ASSIGNEE_ID,UPL_OBJECT_ID,STATUS)
        select allocMasterId, ud.ID, 1, 'U', um.OWNER_ID, uc.ID,
        (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F')
        from ALLOCATION_DETAIL ad inner join UPL_DETAIL ud on ud.ID = ad.UPL_DETAIL_ID
        inner join UPL_MASTER um on um.ID = ud.UPL_MASTER_ID
        inner join UPL_CUSTOMER uc on (uc.UPL_DETAIL_ID = ud.ID and uc.RESPONSE_CODE = 'OK')
        where ad.ALLOCATION_MASTER_ID = allocmaster.RELATED_ALLOCATION and rownum <= ad.ALLOCATED_NUMBER;
        update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'A') 
            where ALLOCATION_MASTER_ID = allocmaster.RELATED_ALLOCATION 
            and status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'N');
    END IF;
    -- Thuc hien phan bo
    IF code.CODE_VALUE1 in ('ALL_TEAMMEM','ALL_TEAMMEM_EXCEPT') THEN
        -- Tinh so luong case phan bo cho 1 member
        select trunc(allocmaster.ALLOCATED_NUMBER/numOfMem) into caseAllocated from dual;
        -- Tinh so du con lai sau khi chia deu
        remain := allocmaster.ALLOCATED_NUMBER - (caseAllocated * numOfMem);
        -- Lay tong so case da allocate cho cac member cua team nay
        select count(1) into totalAllocated 
        from ALLOCATION_DETAIL ud where exists (select 1 from ALLOCATION_MASTER am inner join ALLOCATION_MASTER am1 
            on am.RELATED_ALLOCATION = am1.RELATED_ALLOCATION where am.ID = ud.ALLOCATION_MASTER_ID and am1.ID = allocMasterId) 
        and OBJECT_TYPE = 'U' and exists (select 1 from TEAM_MEMBER where USER_ID = ud.ASSIGNEE_ID and TEAM_ID = teamId);
        -- Loop all team member
        FOR j in (select * from TEAM_MEMBER tm where TEAM_ID = teamId
                and not exists (select 1 from (with json as
                ( select jsonMember doc from   dual) 
                SELECT value FROM json_table((select doc from json) , '$[*]'
                                COLUMNS (value PATH '$')))
        where value = tm.USER_ID))
        LOOP
            -- Tinh so luong case phan bo cho user
            select caseAllocated + decode(remain,0,0,1) into userAllocated from dual;
            EXIT WHEN userAllocated <= 0;
            -- Lay cac upload detail phan bo cho team
            FOR i IN (select ad.* from ALLOCATION_DETAIL ad 
                inner join UPL_DETAIL ud on AD.UPL_DETAIL_ID = ud.ID
                where AD.ALLOCATION_MASTER_ID = allocmaster.RELATED_ALLOCATION
                and AD.OBJECT_TYPE = 'T' and AD.ASSIGNEE_ID = teamId
                and ad.STATUS in (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' 
                    and CODE_VALUE1 in ('N','P')) and ad.ALLOCATED_NUMBER > totalAllocated
                order by ud.UPL_SEQ)
            LOOP
                -- Assign case cho user
                insert into ALLOCATION_DETAIL(ALLOCATION_MASTER_ID,UPL_DETAIL_ID,ALLOCATED_NUMBER,
                    OBJECT_TYPE,ASSIGNEE_ID,UPL_OBJECT_ID,STATUS)
                select allocMasterId, i.UPL_DETAIL_ID, 1, 'U', j.USER_ID, uc.ID,
                (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F')
                from UPL_CUSTOMER uc where not exists (select 1 from ALLOCATION_DETAIL ad
                    where ad.UPL_OBJECT_ID = uc.ID and ad.UPL_DETAIL_ID = uc.UPL_DETAIL_ID)
                and uc.RESPONSE_CODE = 'OK' and uc.UPL_DETAIL_ID = i.UPL_DETAIL_ID and rownum <= userAllocated;
                -- Kiem tra so luong case da duoc assign cho user
                select count(1) into tmp1 from ALLOCATION_DETAIL where ALLOCATION_MASTER_ID = allocMasterId
                and UPL_DETAIL_ID = i.UPL_DETAIL_ID and OBJECT_TYPE = 'U'
                and ASSIGNEE_ID = j.USER_ID and status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_TL' and CODE_VALUE1 = 'F');
                totalAllocated := totalAllocated + tmp1;
                lastDetailID := i.ID;
                IF i.ALLOCATED_NUMBER <= totalAllocated THEN
                    update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE 
                        where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'A') where ID = i.ID;
                END IF;
                -- Neu so luong case da duoc assign du
                EXIT WHEN tmp1 >= userAllocated;
                -- Update ALLOCATION_DETAIL status
                userAllocated := userAllocated - tmp1;
                totalAllocated := 0;
            END LOOP;
            IF remain > 0 THEN
                remain := remain - 1;
            END IF;
        END LOOP;
        update ALLOCATION_DETAIL set status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'P')
        where ID = lastDetailID and status = (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 = 'N');
    END IF;
    
exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE procedure          allocate_To_TSA_TEST(uplMasterId IN INT, allocNumber IN INT, 
        allocTo IN INT, teamId IN INT, jsonMember IN VARCHAR2) is
    caseAllocated int := 0;
    code CODE_TABLE%ROWTYPE;
    allocSeq int := 0;
    l_allocNumber int := allocNumber;
    masterSeqId int := 0;
    l_allocTo int := allocTo;
    count_except int := 0;
begin
    -- Lay sequence phan bo
    select nvl(max(ALLOCATED_SEQ),0) + 1 into allocSeq from ALLOCATION_MASTER 
    where UPL_MASTER_ID = uplMasterId and ALLOCATED_TYPE = 'T';
    -- Lay code table cho allocated to
    select * into code from CODE_TABLE where ID = allocTo;
/*
    IF code.CODE_VALUE1 <> 'OWNER' THEN
        select count(1) into count_except from TEAM_MEMBER tm 
            left join (with json as
                ( select jsonMember doc
                  from   dual
                ) SELECT value
                FROM json_table((select doc from json) , '$[*]'
                                COLUMNS (value PATH '$'))) js
        on tm.USER_ID = js.value
        where tm.TEAM_ID = teamId and js.value is null;
        IF count_except = 0 THEN
            select ID into l_allocTo from CODE_TABLE ct where ct.CATEGORY = 'ALC2TMP' and ct.CODE_VALUE1 = 'ALL_TEAMMEM';
        ELSE
            select ID into l_allocTo from CODE_TABLE ct where ct.CATEGORY = 'ALC2TMP' and ct.CODE_VALUE1 = 'ALL_TEAMMEM_EXCEPT';
        END IF;
    END IF;
*/
    -- Kiem tra so case chua phan bo con lai cua lan truoc de phan bo cho het
    FOR k IN (select * from 
        (select RELATED_ALLOCATION, sum(ALLOCATED_NUMBER) TOTAL_NUMBER 
        from ALLOCATION_MASTER where ALLOCATED_TYPE = 'T' 
        and UPL_MASTER_ID = uplMasterId group by RELATED_ALLOCATION) am
        inner join ALLOCATION_DETAIL ad on ad.ALLOCATION_MASTER_ID = am.RELATED_ALLOCATION
        where ad.OBJECT_TYPE = 'T' and ad.ASSIGNEE_ID = teamId and ad.STATUS in
            (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 in ('N','P'))
            and ad.ALLOCATED_NUMBER > am.TOTAL_NUMBER)
    LOOP
        select SEQ_ALLOCATION_MASTER_ID.nextval into masterSeqId from dual;
        IF l_allocNumber > (k.ALLOCATED_NUMBER - k.TOTAL_NUMBER) THEN
            caseAllocated := k.ALLOCATED_NUMBER - k.TOTAL_NUMBER;
        ELSE
            caseAllocated := l_allocNumber;
        END IF;
        l_allocNumber := l_allocNumber - caseAllocated;
        -- insert vao master
        insert into ALLOCATION_MASTER(ID, UPL_MASTER_ID,ALLOCATED_SEQ,ALLOCATED_NUMBER,
            ALLOCATED_TO,ALLOCATED_TYPE,RELATED_ALLOCATION)
        values(masterSeqId, uplMasterId, allocSeq, caseAllocated, l_allocTo, 'T', k.RELATED_ALLOCATION);
        -- insert vao detail
        allocate_To_TSA_detail_TEST(masterSeqId, teamId, jsonMember);
        allocSeq := allocSeq + 1;
        EXIT WHEN l_allocNumber <= 0;
    END LOOP;
    
    IF l_allocNumber > 0 THEN
        FOR i IN (select ad.* from ALLOCATION_MASTER am 
            inner join ALLOCATION_DETAIL ad on am.ID = ad.ALLOCATION_MASTER_ID
            where am.UPL_MASTER_ID = uplMasterId and ad.OBJECT_TYPE = 'T' and ad.ASSIGNEE_ID = teamId
            and am.ALLOCATED_TYPE = 'S' and ad.STATUS in 
                (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 in ('N','P')))
        LOOP
            select SEQ_ALLOCATION_MASTER_ID.nextval into masterSeqId from dual;
            IF l_allocNumber > i.ALLOCATED_NUMBER THEN
                caseAllocated := i.ALLOCATED_NUMBER;
            ELSE
                caseAllocated := l_allocNumber;
            END IF;
            l_allocNumber := l_allocNumber - caseAllocated;
            -- insert vao master
            insert into ALLOCATION_MASTER(ID, UPL_MASTER_ID,ALLOCATED_SEQ,ALLOCATED_NUMBER,
                ALLOCATED_TO,ALLOCATED_TYPE,RELATED_ALLOCATION)
            values(masterSeqId, uplMasterId, allocSeq, caseAllocated, l_allocTo, 'T', i.ALLOCATION_MASTER_ID);
            -- insert vao detail
            allocate_To_TSA_detail_TEST(masterSeqId, teamId, jsonMember);
            allocSeq := allocSeq + 1;
            EXIT WHEN l_allocNumber <= 0;
        END LOOP;
    END IF;
    
exception
    when others then
        rollback;
        raise;
end;
/

CREATE OR REPLACE procedure allocate_To_TSA(uplMasterId IN INT, allocNumber IN INT, 
        allocTo IN INT, teamId IN INT, jsonarr IN VARCHAR2) is
    caseAllocated int := 0;
    code CODE_TABLE%ROWTYPE;
    allocSeq int := 0;
    l_allocNumber int := allocNumber;
    masterSeqId int := 0;
    l_allocTo int := allocTo;
    count_except int := 0;
    jsonMember varchar2(1000) := jsonarr;
begin
    -- Lay sequence phan bo
    select nvl(max(ALLOCATED_SEQ),0) + 1 into allocSeq from ALLOCATION_MASTER 
    where UPL_MASTER_ID = uplMasterId and ALLOCATED_TYPE = 'T';
    -- Lay code table cho allocated to
    select * into code from CODE_TABLE where ID = allocTo;
    IF trim(jsonMember) is null THEN
        jsonMember := '[]';
    END IF;
    -- change jsonMember if include
    IF code.CODE_VALUE1 = 'INCLUDE_TEAMMEM' THEN
        jsonMember := '[';
        FOR i IN (select * from TEAM_MEMBER tm
        where TEAM_ID = teamId and tm.STATUS = 'A' and not exists 
        (select 1 from (with json as
                ( select jsonarr doc from   dual) 
                SELECT value FROM json_table((select doc from json) , '$[*]'
                                COLUMNS (value PATH '$')))
        where value = tm.USER_ID))
        LOOP
            jsonMember := jsonMember||i.USER_ID||',';
        END LOOP;
        jsonMember := substr(jsonMember,1,length(jsonMember)-1)||']';
    END IF;
    -- Kiem tra so case chua phan bo con lai cua lan truoc de phan bo cho het
    FOR k IN (select * from 
        (select RELATED_ALLOCATION, sum(ALLOCATED_NUMBER) TEAM_LEAD_ALLOCATED 
        from ALLOCATION_MASTER where ALLOCATED_TYPE = 'T' and ASSIGNER_ID = teamId
        and UPL_MASTER_ID = uplMasterId group by RELATED_ALLOCATION) am 
        inner join (select ALLOCATION_MASTER_ID, sum(ALLOCATED_NUMBER) SUPERVISOR_ALLOCATED 
            from ALLOCATION_DETAIL where OBJECT_TYPE = 'T' and ASSIGNEE_ID = teamId and STATUS in
            (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 in ('N','P'))
            group by ALLOCATION_MASTER_ID) ad on am.RELATED_ALLOCATION = ad.ALLOCATION_MASTER_ID
        where ad.SUPERVISOR_ALLOCATED > am.TEAM_LEAD_ALLOCATED)
    LOOP
        select SEQ_ALLOCATION_MASTER_ID.nextval into masterSeqId from dual;
        IF l_allocNumber > (k.SUPERVISOR_ALLOCATED - k.TEAM_LEAD_ALLOCATED) THEN
            caseAllocated := k.SUPERVISOR_ALLOCATED - k.TEAM_LEAD_ALLOCATED;
        ELSE
            caseAllocated := l_allocNumber;
        END IF;
        l_allocNumber := l_allocNumber - caseAllocated;
        -- insert vao master
        insert into ALLOCATION_MASTER(ID, UPL_MASTER_ID,ALLOCATED_SEQ,ALLOCATED_NUMBER,
            ALLOCATED_TO,ALLOCATED_TYPE,RELATED_ALLOCATION,ASSIGNER_ID)
        values(masterSeqId, uplMasterId, allocSeq, caseAllocated, l_allocTo, 'T', k.RELATED_ALLOCATION, teamId);
        -- insert vao detail
        allocate_To_TSA_detail(masterSeqId, teamId, jsonMember);
        allocSeq := allocSeq + 1;
        EXIT WHEN l_allocNumber <= 0;
    END LOOP;
    
    IF l_allocNumber > 0 THEN
        FOR i IN (select ad.* from ALLOCATION_MASTER am 
            inner join ALLOCATION_DETAIL ad on am.ID = ad.ALLOCATION_MASTER_ID
            where am.UPL_MASTER_ID = uplMasterId and ad.OBJECT_TYPE = 'T' and ad.ASSIGNEE_ID = teamId
            and am.ALLOCATED_TYPE = 'S' and ad.STATUS in 
                (select ID from CODE_TABLE where CATEGORY = 'ALCTYPE_SP' and CODE_VALUE1 in ('N','P')))
        LOOP
            select SEQ_ALLOCATION_MASTER_ID.nextval into masterSeqId from dual;
            IF l_allocNumber > i.ALLOCATED_NUMBER THEN
                caseAllocated := i.ALLOCATED_NUMBER;
            ELSE
                caseAllocated := l_allocNumber;
            END IF;
            l_allocNumber := l_allocNumber - caseAllocated;
            -- insert vao master
            insert into ALLOCATION_MASTER(ID, UPL_MASTER_ID,ALLOCATED_SEQ,ALLOCATED_NUMBER,
                ALLOCATED_TO,ALLOCATED_TYPE,RELATED_ALLOCATION,ASSIGNER_ID)
            values(masterSeqId, uplMasterId, allocSeq, caseAllocated, l_allocTo, 'T', i.ALLOCATION_MASTER_ID,teamId);
            -- insert vao detail
            allocate_To_TSA_detail(masterSeqId, teamId, jsonMember);
            allocSeq := allocSeq + 1;
            EXIT WHEN l_allocNumber <= 0;
        END LOOP;
    END IF;
    
exception
    when others then
        rollback;
        raise;
end;
/
