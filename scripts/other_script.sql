CREATE OR REPLACE FUNCTION long2varchar (
    schemaName IN VARCHAR2,
    tableName IN VARCHAR2,
    columnName IN VARCHAR2
    
) RETURN VARCHAR2 IS
    l_ret VARCHAR2(4000);
    l_long LONG;
BEGIN
    select data_default into l_long from all_tab_columns
    where owner = schemaName and table_name = tableName and column_name = columnName;
    l_ret := SUBSTR(l_long, 1, 4000);
    return l_ret;
END;
/

-- Check null ID when insert
select 'CREATE OR REPLACE TRIGGER TRG_'||substr(SEQUENCE_NAME,5,length(SEQUENCE_NAME)-7)||'_INS'||CHR(10)||
 'BEFORE INSERT ON '||substr(SEQUENCE_NAME,5,length(SEQUENCE_NAME)-7)||'             FOR EACH ROW'||CHR(10)||
 'BEGIN'||CHR(10)||
 'IF :new.ID is null THEN'||CHR(10)||
 'select '||SEQUENCE_NAME||'.nextval into :new.ID from dual;'||CHR(10)||
 'END IF;'||CHR(10)||
 'END;'||CHR(10)||
 '/'||CHR(10)
 from user_sequences
where SEQUENCE_NAME not in ('SEQ_MESSAGE_LOG_TRANS_ID');

-- insert to mcsync.SYNC_LOG
insert into SYNC_LOG(TABLE_NAME,LAST_UPDATE_DATE,LAST_SYNC,STATUS)
select table_name,sysdate,sysdate,'S' from user_tables ut where table_name not in ('SYNC_LOG')
and not exists (select 1 from SYNC_LOG sl where sl.table_name = ut.table_name);

-- gen trigger for mcsync tables
select 'CREATE OR REPLACE TRIGGER TRG_'||table_name||'_COM'||chr(10)
    ||'BEFORE INSERT OR UPDATE ON '||table_name||' FOR EACH ROW '||chr(10)
    ||'BEGIN '||chr(10)
    ||'update SYNC_LOG set LAST_UPDATE_DATE = sysdate, '||chr(10)
    ||'STATUS = ''N'', '||chr(10)
    ||'ERROR_MSG = null '||chr(10)
    ||'where TABLE_NAME = '''||table_name||'''; '||chr(10)||chr(10)
    ||decode(table_name,'APP_CACHE_VIEW',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
        'APPROVE',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_ENTRY_SALES',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_ENTRY_2',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_ENTRY',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_APP_TRANSACTION',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_APPROVE',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_CORRECTOR',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_CASE_STATUS',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_CALL_APPRAISAL_REASON_FOR_RETURN',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_CORRECTOR_REASON_FOR_REJECT',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_CORRECTOR_REASON_FOR_RETURN',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_ENTRY_2_REASON_FOR_RETURN',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_DATA_ENTRY_REASON_FOR_RETURN',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_OPERATION_REASON_FOR_RETURN',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_CALL_APPRAISAL',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_FIELD_APPRAISAL_HOME',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_FIELD_APPRAISAL_OFFICE',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'DE_FIELD_APPRAISAL_SELF_EMPLOYED',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'POS_UPLOAD_CREDIT',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'OPERATION_CHECK_DOC',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
		'OPERATION_APPROVE',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
        'CALL_APPRAISAL',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
        'DATA_CORRECTOR',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
        'DATA_CORRECTOR_REASON_FOR_RETURN',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
        'DATA_ENTRY',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
        'FIELD_APPRAISAL_HOME',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),
        'FIELD_APPRAISAL_OFFICE',':new.LAST_UPDATE_DATE := sysdate;'||chr(10),'')
    ||'END; '||chr(10)
    ||'/'||chr(10)
from user_tables where table_name not in ('SYNC_LOG','APP_TRANSACTION_HIST','MAPING_CONTRACT_ESB','CREATE_LOAN_DEPOSIT');

-- gen trigger convert number to date on mcsync
select case when utc.column_id = mmc.min_col and utc.column_id = mmc.max_col then
'CREATE OR REPLACE TRIGGER TRG_'||utc.table_name||'_CONV '||chr(10)
||'BEFORE INSERT OR UPDATE ON '||utc.table_name||' FOR EACH ROW '||chr(10)
||'BEGIN '||chr(10)
||'IF :new.'||utc.column_name||'_TS is not null THEN '||chr(10)
||'select (TO_DATE(''19700101'',''yyyymmdd'') + :new.'||utc.column_name||'_TS/86400000) into :new.'||utc.column_name||' from dual;'||chr(10)
||'END IF; '||chr(10)
||'EXCEPTION '||chr(10)
||'    WHEN OTHERS THEN '||chr(10)
||'        null; '||chr(10)
||'END; '||chr(10)
||'/'||chr(10)||chr(10)
when utc.column_id = mmc.min_col then
'CREATE OR REPLACE TRIGGER TRG_'||utc.table_name||'_CONV '||chr(10)
||'BEFORE INSERT OR UPDATE ON '||utc.table_name||' FOR EACH ROW '||chr(10)
||'BEGIN '||chr(10)
||'IF :new.'||utc.column_name||'_TS is not null THEN '||chr(10)
||'select (TO_DATE(''19700101'',''yyyymmdd'') + :new.'||utc.column_name||'_TS/86400000) into :new.'||utc.column_name||' from dual;'||chr(10)
||'END IF; '||chr(10)
when utc.column_id > mmc.min_col and utc.column_id = mmc.max_col then
'IF :new.'||utc.column_name||'_TS is not null THEN '||chr(10)
||'select (TO_DATE(''19700101'',''yyyymmdd'') + :new.'||utc.column_name||'_TS/86400000) into :new.'||utc.column_name||' from dual;'||chr(10)
||'END IF; '||chr(10)
||'EXCEPTION '||chr(10)
||'    WHEN OTHERS THEN '||chr(10)
||'        null; '||chr(10)
||'END; '||chr(10)
||'/'||chr(10)||chr(10)
else
'IF :new.'||utc.column_name||'_TS is not null THEN '||chr(10)
||'select (TO_DATE(''19700101'',''yyyymmdd'') + :new.'||utc.column_name||'_TS/86400000) into :new.'||utc.column_name||' from dual;'||chr(10)
||'END IF; '||chr(10)
end
from USER_TAB_COLUMNS utc 
inner join (select table_name,max(column_id) max_col,min(column_id) min_col 
from USER_TAB_COLUMNS where data_type= 'DATE' and column_name <> 'LAST_UPDATE_DATE'
group by table_name) mmc on utc.table_name = mmc.table_name
where data_type= 'DATE' and utc.table_name not in ('SYNC_LOG','APP_TRANSACTION','APP_TRANSACTION_HIST','MAPING_CONTRACT_ESB','CREATE_LOAN_DEPOSIT') 
and utc.column_name <> 'LAST_UPDATE_DATE'
order by utc.table_name, utc.column_id;


-- Do not check null ID when insert
select 'CREATE OR REPLACE TRIGGER TRG_'||substr(SEQUENCE_NAME,5,length(SEQUENCE_NAME)-7)||'_INS'||CHR(10)||
 'BEFORE INSERT ON '||substr(SEQUENCE_NAME,5,length(SEQUENCE_NAME)-7)||'             FOR EACH ROW'||CHR(10)||
 'BEGIN'||CHR(10)||
 'select '||SEQUENCE_NAME||'.nextval into :new.ID from dual;'||CHR(10)||
 'END;'||CHR(10)||
 '/'||CHR(10)
 from user_sequences
where SEQUENCE_NAME not in ('SEQ_MESSAGE_LOG_TRANS_ID');

-- truncate table
select 'truncate table '||table_name||';' from user_tables
where table_name not in ('CALENDAR','CODE_TABLE','COMM_BRAND','FIELDS','FUNCTIONS','GLOBAL_CHECKLIST','GLOBAL_DOCUMENT','INTEREST_TABLE','MENU',
'NOTIFICATION_TEMPLATE','PARAMETER_DETAIL','PARAMETERS','PARTNER','POSTING_CONFIGURATION',
'PRODUCT_CHECKLIST_DOCUMENT','PRODUCT_SCOPE','PRODUCT_SCOPE_ADDITIONAL','PRODUCTS','ROLE_DETAILS','ROLES','SCHEDULER',
'SERVICES','TEAM_MEMBER','TEAMS','UPL_BPM_CODE','UPL_BPM_PRODUCTS','UPL_BPM_SYSDETAIL','UPL_CC_STATEMENT',
'UPL_CORE_CONTRACT','USERS','USERS_ROLE_MAPPING');

-- drop constraint
select 'ALTER TABLE '||A.TABLE_NAME||' DROP CONSTRAINT '||A.CONSTRAINT_NAME||';' 
from user_constraints a where A.CONSTRAINT_TYPE = 'U';

-- get unique index
select * from user_indexes a
where not exists (select 1 from user_constraints b where B.CONSTRAINT_NAME = A.INDEX_NAME)
and A.UNIQUENESS = 'UNIQUE';

-- Check table structure between DEV and SIT
select * from (
select 'TABLE' as type,dev.table_name,null as column_name, 
'Create table '||dev.table_name||' on SIT' as action, 'Extract script create table from tool' as SQL_statement 
from all_tables dev where owner='MCPORTAL'
and not exists (select 1 from all_tables sit where owner='SIT_MCP' and dev.table_name = sit.table_name)
union all
select 'COLUMN' as type,dev.table_name,dev.column_name,'Add column '||dev.column_name||' to table '||dev.table_name||' on SIT' as action,
'alter table '||dev.table_name||' add column '||dev.column_name||' '||dev.data_type||' '||
decode(dev.data_type,'VARCHAR2','('||dev.data_length||')','NUMBER','('||nvl(dev.data_precision,dev.data_length)||','||dev.data_scale||')','')||
decode(dev.default_on_null,'YES',' DEFAULT ON NULL '||long2varchar('MCPORTAL',dev.table_name,dev.column_name),'')||
decode(dev.nullable,'N',' NOT NULL ','')||';' as SQL_statement
from all_tab_columns dev where owner='MCPORTAL'
and not exists (select 1 from all_tab_columns sit where owner='SIT_MCP' 
and dev.table_name = sit.table_name and dev.column_name = sit.column_name)
and exists (select 1 from all_tables sit where owner='SIT_MCP' and dev.table_name = sit.table_name)
union all
select 'COLUMN' as type,dev.table_name,dev.column_name,
decode(dev.data_type,sit.data_type,'Column '||dev.column_name||' on DEV has difference length from SIT',
'Column '||dev.column_name||' on DEV has data type '||dev.data_type||' while SIT data type is '||sit.data_type) as action,
decode(dev.data_type,sit.data_type,'DEV has '||dev.data_type||'('||decode(dev.data_type,'NUMBER',nvl(dev.data_precision,dev.data_length)||','||dev.data_scale,dev.data_length)||') while SIT has '||sit.data_type||'('||decode(sit.data_type,'NUMBER',nvl(sit.data_precision,sit.data_length)||','||sit.data_scale,sit.data_length)||')',null) as SQL_statement
from all_tab_columns dev inner join all_tab_columns sit 
on dev.table_name = sit.table_name and dev.column_name = sit.column_name
where dev.owner='MCPORTAL' and sit.owner='SIT_MCP'
and ((dev.data_type <> sit.data_type) 
or (dev.data_type = sit.data_type and (dev.data_length <> sit.data_length 
    or nvl(dev.data_precision,0) <> nvl(sit.data_precision,0) or nvl(dev.data_scale,0) <> nvl(sit.data_scale,0))))
) order by type,table_name,column_name;


-- insert missing parent code from temp table into code table - same schema
insert into CODE_TABLE(CREATED_BY,LAST_UPDATED_BY,PARENT_ID,PRODUCT_CAT_ID,PRODUCT_GROUP_ID,PRODUCT_ID,CODE_GROUP,
CATEGORY,CODE_VALUE1,CODE_VALUE2,CODE_MESSAGE_KEY,DESCRIPTION1,DESCRIPTION2,STATUS,REFERENCE)
select CREATED_BY,LAST_UPDATED_BY,PARENT_ID,PRODUCT_CAT_ID,PRODUCT_GROUP_ID,PRODUCT_ID,CODE_GROUP,
CATEGORY,CODE_VALUE1,CODE_VALUE2,CODE_MESSAGE_KEY,DESCRIPTION1,DESCRIPTION2,STATUS,REFERENCE
from tmp_code_table c where exists (select 1 from (select * from tmp_code_table a where not exists (select 1 from CODE_TABLE b 
    where B.CATEGORY = A.CATEGORY and B.CODE_VALUE1 = A.CODE_VALUE1)
and A.PARENT_ID is not null) d where c.ID = d.PARENT_ID);

-- insert missing code from temp table into code table - same schema
insert into CODE_TABLE(CREATED_BY,LAST_UPDATED_BY,PARENT_ID,PRODUCT_CAT_ID,PRODUCT_GROUP_ID,PRODUCT_ID,CODE_GROUP,
CATEGORY,CODE_VALUE1,CODE_VALUE2,CODE_MESSAGE_KEY,DESCRIPTION1,DESCRIPTION2,STATUS,REFERENCE)
select CREATED_BY,LAST_UPDATED_BY,decode(nvl(a.PARENT_ID,-1),-1,null,(select ID from CODE_TABLE g 
    where exists (select 1 from (select * from tmp_code_table where ID = a.PARENT_ID) h
    where G.CATEGORY = h.CATEGORY and G.CODE_VALUE1 = h.CODE_VALUE1))),PRODUCT_CAT_ID,PRODUCT_GROUP_ID,PRODUCT_ID,CODE_GROUP,
CATEGORY,CODE_VALUE1,CODE_VALUE2,CODE_MESSAGE_KEY,DESCRIPTION1,DESCRIPTION2,STATUS,REFERENCE
from tmp_code_table a where not exists (select 1 from CODE_TABLE b 
    where B.CATEGORY = A.CATEGORY and B.CODE_VALUE1 = A.CODE_VALUE1);

-- insert CODE_TABLE from category
select 'Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('''||
CODE_GROUP||''','''||CATEGORY||''','''||CODE_VALUE1||''','''||DESCRIPTION1||''');'
from CODE_TABLE where CATEGORY = 'CALL_STAT';

-- insert CODE_TABLE from category with parent id
select 'Insert into CODE_TABLE(PARENT_ID,CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('||
'(select id from CODE_TABLE where CATEGORY=''CALL_STAT'' and CODE_VALUE1='''||(select CODE_VALUE1 from CODE_TABLE where ID = a.PARENT_ID)||'''),'''||
CODE_GROUP||''','''||CATEGORY||''','''||CODE_VALUE1||''','''||DESCRIPTION1||''');'
from CODE_TABLE a where CATEGORY = 'CALL_RSLT';

-- insert CODE_TABLE from sales_network.sln_country
select concat('Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, CODE_VALUE2, DESCRIPTION1,DESCRIPTION2,REFERENCE) Values(''LOCA'',''COUNTRY'',''',
substr(COUNTRY_CODE,1,2),''',''',substr(COUNTRY_CODE,4,3),''',''',COUNTRY_NAME,''',''',ISO_CODE,''',''',ID,''');')
from sales_network.sln_country;

-- insert CODE_TABLE from customdb.step_status_define
select concat('Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, CODE_VALUE2, DESCRIPTION1,DESCRIPTION2,REFERENCE) Values(''STEP'',''STEP_STAT'',''',
stepCode,''',''','',''',''',stepName,''',''',stepNameDisplay,''',''',ID,''');')
from customdb.step_status_define;

-- Delete existing Rule on UAT
delete from RULE_PARAMETER_LINK rpl where exists (select 1 from RULES r where r.ID = rpl.RULE_ID 
and r.RULE_CODE in ('CHECKLIST_ENTRY','CHECKLIST_EXPT'));

delete from RULE_OUTPUT_DETAIL rod where exists (select 1 from RULE_OUTPUT ro where ro.ID = rod.RULE_OUTPUT_ID
and exists (select 1 from RULES r where r.ID = ro.RULE_ID
and r.RULE_CODE in ('CHECKLIST_ENTRY','CHECKLIST_EXPT')));

delete from RULE_OUTPUT ro where exists (select 1 from RULES r where r.ID = ro.RULE_ID 
and r.RULE_CODE in ('CHECKLIST_ENTRY','CHECKLIST_EXPT'));

delete from RULE_DETAIL rd where exists (select 1 from RULES r where r.ID = rd.RULE_ID 
and r.RULE_CODE in ('CHECKLIST_ENTRY','CHECKLIST_EXPT'));

delete from RULES where RULE_CODE in ('CHECKLIST_ENTRY','CHECKLIST_EXPT');

truncate table RULE_PARAMETER_DETAIL;

truncate table RULE_PARAMETERS;

-- Export Rule from SIT
select * from RULES where RULE_CODE IN ('CHECKLIST_ENTRY','CHECKLIST_EXPT');

select * from RULE_PARAMETERS;

select * from RULE_PARAMETER_DETAIL;

select rpl.* from RULE_PARAMETER_LINK rpl 
where exists (select 1 from RULES r where r.ID = rpl.RULE_ID 
and r.RULE_CODE IN ('CHECKLIST_ENTRY','CHECKLIST_EXPT'));

select ro.* from RULE_OUTPUT ro 
where exists (select 1 from RULES r where r.ID = ro.RULE_ID 
and r.RULE_CODE IN ('CHECKLIST_ENTRY','CHECKLIST_EXPT'));

select rod.* from RULE_OUTPUT_DETAIL rod where exists (select 1 from RULE_OUTPUT ro where ro.ID = rod.RULE_OUTPUT_ID
and exists (select 1 from RULES r where r.ID = ro.RULE_ID
and r.RULE_CODE in ('CHECKLIST_ENTRY','CHECKLIST_EXPT')));

select rd.* from RULE_DETAIL rd
where exists (select 1 from RULES r where r.ID = rd.RULE_ID 
and r.RULE_CODE IN ('CHECKLIST_ENTRY','CHECKLIST_EXPT'));

-- Setup Rule
insert into RULE_PARAMETER_DETAIL(PARAM_ID,PARAM_DETAIL_ID,PARAM_DETAIL_NAME,PARAM_DETAIL_TYPE,JAVA_CLASS_NAME)
select ID,null,PARAM_LIST_TYPE||to_char(ID),null,null from RULE_PARAMETERS
where PARAM_NAME like 'SP_PRG%';
-- from RULE_PARAMETERS
insert into RULE_PARAMETER_LINK(RULE_ID,RULE_DETAIL_ID,RULE_PARAM_ID)
select 67,45,ID from RULE_PARAMETERS
where PARAM_NAME like 'SP_PRG%';
-- from RULE_PARAMETER_LINK
insert into RULE_PARAMETER_LINK(RULE_ID,RULE_DETAIL_ID,RULE_PARAM_ID)
select 112,RULE_DETAIL_ID,RULE_PARAM_ID from RULE_PARAMETER_LINK
where RULE_ID = 67;
-- RULE_OUTPUT from RULE_OUTPUT
insert into RULE_OUTPUT(RULE_ID,RULE_COMBINATION_KEY,RULE_PARAM_ID,RULE_PARAM_DETAIL_ID,OUTPUT_KEY,OUTPUT_DATA_TYPE,OUTPUT_VALUE)
select 147,RULE_COMBINATION_KEY,RULE_PARAM_ID,RULE_ID+RULE_COMBINATION_KEY,OUTPUT_KEY,OUTPUT_DATA_TYPE,OUTPUT_VALUE from RULE_OUTPUT
where RULE_ID >= 67;
-- RULE_OUTPUT from 
insert into RULE_OUTPUT(RULE_ID,RULE_COMBINATION_KEY,RULE_PARAM_ID,RULE_PARAM_DETAIL_ID,OUTPUT_KEY,OUTPUT_DATA_TYPE,OUTPUT_VALUE)
select 147,4,null,rpd.ID,nvl(rpd.PARAM_DETAIL_NAME,to_char(rpd.PARAM_DETAIL_ID)),'S','N/A' from RULE_PARAMETERS rp 
inner join RULE_PARAMETER_DETAIL rpd on rp.ID = rpd.PARAM_ID
where rp.PARAM_NAME like 'SP_PRG%' and rp.PARAM_TYPE <> 'C'
and (rpd.PARAM_DETAIL_NAME in ('E0000010','POS100969','Điều hòa','Sam Sung','Nữ','30')
or rpd.PARAM_DETAIL_ID in (6))
and not exists (select 1 from RULE_OUTPUT ro where ro.RULE_ID = 147
    and ro.RULE_COMBINATION_KEY = 4 and ro.RULE_PARAM_DETAIL_ID = rpd.ID);
-- Check Rule
select r.RULE_CODE,ro.*, rp.PARAM_NAME,rp.PARAM_TYPE,rp.PARAM_VALUE, rpd.*
from RULES r inner join RULE_OUTPUT ro on r.ID = ro.RULE_ID
inner join RULE_PARAMETER_DETAIL rpd on rpd.ID = ro.RULE_PARAM_DETAIL_ID
inner join RULE_PARAMETERS rp on rp.ID = rpd.PARAM_ID
where r.RULE_CODE = 'RM_PILOT'
--and ro.ID is not null
order by ro.RULE_COMBINATION_KEY,rpd.PARAM_ID,rpd.ID
-- check rule link
select r.RULE_CODE,rp.ID,rp.PARAM_NAME,rp.PARAM_TYPE,rp.PARAM_VALUE--, rpd.*
from RULES r inner join RULE_PARAMETER_LINK rpl on r.ID = rpl.RULE_ID
inner join RULE_PARAMETERS rp on rp.ID = rpl.RULE_PARAM_ID
--inner join RULE_PARAMETER_DETAIL rpd on rp.ID = rpd.PARAM_ID
where r.RULE_CODE = 'SR_PROGRAM'
--and ro.ID is not null
order by rp.ID


-- Export data from DE customdb
SELECT null,'INST','BRAND',id,brandCode,brandName,null from brand
union all
SELECT null,'INST','COMM',id,goodsCode,goodsName,null from goods
union all
SELECT null,'CUST','INDUS',id,industryCode,industryName,industryCodeT24 from industry
union all
SELECT null,'INSU','ISTAF',id,insuranceStaffId,insuranceStaffName,null from insurance_staff
union all
select null, 'LOCA', 'PROVINCE', 	id, provinceId, provinceName,null from province
union all
select provinceId, 'LOCA', 'DISTRICT', 	id, districtId, districtName,null from district
union all
select districtId, 'LOCA', 'WARD', 	id, wardId, wardName,null from ward;

/*
script file:
	replace INSERT INTO ``(``, ``, ``, ``, ``, ``, ``) by INSERT INTO CODE_TABLE(REFERENCE,CODE_GROUP,CATEGORY,DESCRIPTION2,CODE_VALUE1,DESCRIPTION1,CODE_VALUE2)
	set escape \
	replace & by \&
	replace \' by ''
*/

-- Update parent id
update code_table b set
    b.PARENT_ID = (select ID from code_table a where A.CODE_GROUP = 'LOCA' and A.CATEGORY = 'PROVINCE' and A.CODE_VALUE1 = B.REFERENCE)
where b.CODE_GROUP = 'LOCA' and b.CATEGORY = 'DISTRICT';

update code_table b set
    b.PARENT_ID = (select ID from code_table a where A.CODE_GROUP = 'LOCA' and A.CATEGORY = 'DISTRICT' and A.CODE_VALUE1 = B.REFERENCE)
where b.CODE_GROUP = 'LOCA' and b.CATEGORY = 'WARD';
commit;


CREATE TABLE UPL_BPM_CODE
   (CODE_GROUP varchar2(5),
	CATEGORY varchar2(10),
	CODE_CAT varchar2(100),
    CODE_JSON CLOB
    CONSTRAINT ensure_json CHECK (CODE_JSON IS JSON));

select VAR_NAME, VAR_ACCEPTED_VALUES from process_variables where VAR_ACCEPTED_VALUES <> '[]' 
and PRJ_UID = (select PRJ_UID from bpmn_project where PRJ_NAME = 'Concentrating Data Entry')
--and PRJ_UID = '46997778359b23c7d855ce6051135589'

/*
script file:
	replace INSERT INTO ``(`VAR_NAME`, `VAR_ACCEPTED_VALUES`) by INSERT INTO UPL_BPM_CODE(CODE_CAT, CODE_JSON)
	replace \" by "
*/

begin
	parse_BPMCode_json;
	commit;
end;
/

update CODE_TABLE set 
	CODE_VALUE2 = 'Y',
	DESCRIPTION2 = 'Đúng'
where REFERENCE = 'customerIsDirector' and CATEGORY = 'BOOLEAN' and CODE_VALUE1 = '1';

update CODE_TABLE set 
	CODE_VALUE2 = 'N',
	DESCRIPTION2 = 'Sai'
where REFERENCE = 'customerIsDirector' and CATEGORY = 'BOOLEAN' and CODE_VALUE1 = '0';

delete from CODE_TABLE where REFERENCE in ('bypassTempAddressDocsPass',
'cicReportAvailable',
'customerAddressValid',
'otherCreditLoan',
'hasInsurrance_de2',
'hasBusinessRegistration',
'fraudAppraisal',
'override',
'customerLivingAtAddress',
'otherCreditLoanSpouse',
'addressValid',
'hasInsurrance',
'isInternetBanking',
'companyAddressValid',
'annualFeeDe2',
'insuranceFeeCardDe',
'annualFeeDe',
'issueFeeDe',
'insuranceFeeCardDe2',
'cardReceiveAddress_de2',
'loanPurpose_de2',
'loanTenorApprover',
'changingDateContractApprove',
'channelName_de2',
'familyBookSeriesType_de2',
'disbursementChannel_de2',
'duplicate_dc',
'frequencyOfAnnualFeeDe2',
'frequencyOfInsuranceFeeDe2',
'gender_de2',
'issuePlaceCitizenID_de2',
'insuranceCompany_de2',
'insuranceTerm_de2',
'maritalStatus_de2',
'spousePosition',
'relationshipWithBorrower1_de2',
'relationshipWithBorrower2',
'relationshipWithBorrower2_de2',
'relationshipWithBorrower3',
'relationshipWithBorrower3_de2',
'relationshipWithBorrower4',
'relationshipWithBorrower4_de2',
'relationshipWithApplicant_de2',
'temporaryResidence_de2',
'loanTenor_de2',
'loanTermApproverOther');

CREATE TABLE UPL_BPM_SYSDETAIL (
	ID INT,
	CODE_GROUP VARCHAR(5),
	CATEGORY VARCHAR(10),
	BPM_CATEGORY_CODE VARCHAR(50),
	SYS_NAME VARCHAR(200),
	SYS_VALUE VARCHAR(200),
	SYS_CAT_ID INT,
	DESCRIPTION VARCHAR(200)
);

select id,brandCode,goodsCode,1000 from brand_in_goods;
/*
script file:
	replace customdb.brand_in_goods(id, brandCode, goodsCode) by UPL_BPM_SYSDETAIL(ID,SYS_NAME,SYS_VALUE,SYS_CAT_ID)
	set escape \
	replace & by \&
*/

insert into COMM_BRAND(BRAND_ID, COMM_ID) select (select ID from CODE_TABLE where CODE_GROUP = 'INST' and CATEGORY = 'BRAND' and CODE_VALUE1=a.SYS_NAME),
	(select ID from CODE_TABLE where CODE_GROUP = 'INST' and CATEGORY = 'COMM' and CODE_VALUE1=a.SYS_VALUE) 
from (select distinct SYS_NAME,SYS_VALUE from UPL_BPM_SYSDETAIL)a;
commit;

select b.id,a.category_code,b.`name`,b.`value`,b.sys_category_id,b.description from sys_category a inner join sys_category_detail b
on a.id = b.sys_category_id
order by a.category_code, b.`name`;

/*
script file:
	replace ``(`id`, `category_code`, `name`, `value`, `sys_category_id`, `description`) by UPL_BPM_SYSDETAIL(ID,BPM_CATEGORY_CODE,SYS_NAME,SYS_VALUE,SYS_CAT_ID,DESCRIPTION)
*/

-- Top most parent for reason
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1) VALUES('APPR','RSRET_REJ','APS_REJECT','Từ chối của thẩm định');
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1) VALUES('APPR','RSRET_REJ','APV_REJECT','Từ chối khi phê duyệt');
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1) VALUES('APPR','RSRET_REJ','CARS_RETSA','Lý do Call trả về Sales');
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1) VALUES('APPR','RSRET_REJ','DCRS_RETDE','Lý do DC trả về DE');
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1) VALUES('APPR','RSRET_REJ','DCRS_RETSA','Lý do DC trả về Sales');
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1) VALUES('APPR','RSRET_REJ','DERS_RETSA','Lý do DE trả về Sales');
commit;
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPR','APS_REJECT','APS_REJ01','Từ chối_Vấn đề tài chính KH',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APS_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPR','APS_REJECT','APS_REJ02','Từ chối_Các chỉ số tài chính không thỏa',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APS_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPR','APS_REJECT','APS_REJ03','Từ chối_Thông tin LSTD',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APS_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPR','APS_REJECT','APS_REJ04','Từ chối_Nghi ngờ giả mạo',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APS_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPR','APS_REJECT','APS_REJ05','Từ chối_Vấn đề nhân thân KH',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APS_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPR','APS_REJECT','APS_REJ06','Từ chối_Vấn đề nghề nghiệp, công việc KH',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APS_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPR','APS_REJECT','APS_REJ07','Từ chối_Thông tin Sale/POS/Hub',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APS_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ01','Từ chối_Vấn đề tài chính KH',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ02','Từ chối_Các chỉ số tài chính không thỏa',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ03','Từ chối_Thông tin LSTD',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ04','Từ chối_Nghi ngờ giả mạo',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ05','Từ chối_Vấn đề nhân thân KH',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ06','Từ chối_Vấn đề nghề nghiệp, công việc KH',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ07','Từ chối_Thông tin Sale/POS/Hub',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('APPV','APV_REJECT','APV_REJ08','Từ chối_Lý do từ Field',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'APV_REJECT'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','CARS_RETSA','CARS_RES00','Lý do Call trả về Sales',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'CARS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','CARS_RETSA','CARS_RES01','KH cân nhắc thêm về khoản vay',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'CARS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','CARS_RETSA','CARS_RES02','Không thể liên lạc với khách hàng, người tham chiếu',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'CARS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','CARS_RETSA','CARS_RES03','Thiếu giấy tờ',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'CARS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','CARS_RETSA','CARS_RES04','Chứng từ quá thời hạn quy định',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'CARS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','CARS_RETSA','CARS_RES05','Cần bổ sung giấy tờ để đánh giá thẩm định',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'CARS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE00','Lý  do DC trả về DE',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE01','Không đúng chính sách sản phẩm.',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE02','Chứng từ không hợp lệ.',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE03','Nhập liệu thiếu thông tin.',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE04','Nhập liệu sai thông tin.',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE05','Thiếu chứng từ.',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE06','Chất lượng scan không đảm bảo.',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE07','File đính kèm không đúng qui đinh.',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE08','Lý do khác',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETDE','DCRSRTDE09','Sai thông tin KH, SP vay',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETDE'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETSA','DCRSRTSA00','Lý do DC trả về Sale',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETSA','DCRSRTSA01','Sai thông tin KH, SP vay',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETSA','DCRSRTSA02','Chứng từ không hợp lệ',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETSA','DCRSRTSA03','Thiếu chứng từ',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETSA','DCRSRTSA04','Chất lượng scan không đảm bảo',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETSA','DCRSRTSA05','File đính kèm không đúng quy định',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DCRS_RETSA','DCRSRTSA06','Lý do khác',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DCRS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DERS_RETSA','DERSRTSA00','Lý do DE trả về Sale',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DERS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DERS_RETSA','DERSRTSA01','Sai thông tin KH, SP vay',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DERS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DERS_RETSA','DERSRTSA02','Chứng từ không hợp lệ',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DERS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DERS_RETSA','DERSRTSA03','Thiếu chứng từ',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DERS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DERS_RETSA','DERSRTSA04','Chất lượng scan không đảm bảo',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DERS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DERS_RETSA','DERSRTSA05','File đính kèm không đúng quy định',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DERS_RETSA'));
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,PARENT_ID) VALUES('RSRET','DERS_RETSA','DERSRTSA06','Lý do khác',(select ID from CODE_TABLE where CATEGORY = 'RSRET_REJ' and CODE_VALUE1 = 'DERS_RETSA'));
commit;
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'APS_REJ01' where BPM_CATEGORY_CODE = 'APPRAISAL_REJECT_01';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'APS_REJ02' where BPM_CATEGORY_CODE = 'APPRAISAL_REJECT_02';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'APS_REJ03' where BPM_CATEGORY_CODE = 'APPRAISAL_REJECT_03';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'APS_REJ04' where BPM_CATEGORY_CODE = 'APPRAISAL_REJECT_04';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'APS_REJ05' where BPM_CATEGORY_CODE = 'APPRAISAL_REJECT_05';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'APS_REJ06' where BPM_CATEGORY_CODE = 'APPRAISAL_REJECT_06';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'APS_REJ07' where BPM_CATEGORY_CODE = 'APPRAISAL_REJECT_07';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ01' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_01';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ02' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_02';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ03' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_03';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ04' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_04';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ05' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_05';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ06' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_06';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ07' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_07';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPV', CATEGORY = 'APV_REJ08' where BPM_CATEGORY_CODE = 'APPROVAL_REJECT_08';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'INST', CATEGORY = 'BRAND' where BPM_CATEGORY_CODE = 'BRAND_CODE';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'APPR', CATEGORY = 'CAAPS_DECI' where BPM_CATEGORY_CODE = 'CALL_APPRAISAL_DECISION';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'CARS_RES00' where BPM_CATEGORY_CODE = 'CA_REASON_RETURN_SALES';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'CARS_RES01' where BPM_CATEGORY_CODE = 'CA_REASON_RETURN_SALES_01';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'CARS_RES02' where BPM_CATEGORY_CODE = 'CA_REASON_RETURN_SALES_02';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'CARS_RES03' where BPM_CATEGORY_CODE = 'CA_REASON_RETURN_SALES_03';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'CARS_RES04' where BPM_CATEGORY_CODE = 'CA_REASON_RETURN_SALES_04';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'CARS_RES05' where BPM_CATEGORY_CODE = 'CA_REASON_RETURN_SALES_05';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSREJ', CATEGORY = 'DCRS_REJ' where BPM_CATEGORY_CODE = 'DC_REASON_REJECT';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE00' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE01' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_01';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE02' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_02';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE03' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_03';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE04' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_04';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE05' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_05';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE06' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_06';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE07' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_07';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE08' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_08';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTDE09' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_DE_09';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTSA00' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_SALES';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTSA01' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_SALES_01';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTSA02' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_SALES_02';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTSA03' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_SALES_03';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTSA04' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_SALES_04';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTSA05' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_SALES_05';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DCRSRTSA06' where BPM_CATEGORY_CODE = 'DC_REASON_RETURN_SALES_06';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSREJ', CATEGORY = 'DERS_REJ' where BPM_CATEGORY_CODE = 'DE_REASON_REJECT';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DERSRTSA00' where BPM_CATEGORY_CODE = 'DE_REASON_RETURN_SALES';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DERSRTSA01' where BPM_CATEGORY_CODE = 'DE_REASON_RETURN_SALES_01';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DERSRTSA02' where BPM_CATEGORY_CODE = 'DE_REASON_RETURN_SALES_02';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DERSRTSA03' where BPM_CATEGORY_CODE = 'DE_REASON_RETURN_SALES_03';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DERSRTSA04' where BPM_CATEGORY_CODE = 'DE_REASON_RETURN_SALES_04';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DERSRTSA05' where BPM_CATEGORY_CODE = 'DE_REASON_RETURN_SALES_05';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'DERSRTSA06' where BPM_CATEGORY_CODE = 'DE_REASON_RETURN_SALES_06';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'CUST', CATEGORY = 'INDUS' where BPM_CATEGORY_CODE = 'INDUSTRY';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'MISC', CATEGORY = 'INSU_TERM' where BPM_CATEGORY_CODE = 'INSURANCE_TERM';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'MISC', CATEGORY = 'LINS_COMP' where BPM_CATEGORY_CODE = 'LIFE_INSURANCE_COMPANY';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'PROD', CATEGORY = 'PROD' where BPM_CATEGORY_CODE = 'PRODUCT';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'PROD', CATEGORY = 'PROD_CS' where BPM_CATEGORY_CODE = 'PRODUCT_CS';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'LOCA', CATEGORY = 'PROVINCE' where BPM_CATEGORY_CODE = 'PROVINCE';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSREJ', CATEGORY = 'RSRJ_APS' where BPM_CATEGORY_CODE = 'REASON_REJECT_APPRAISAL';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSREJ', CATEGORY = 'RSRJ_APV' where BPM_CATEGORY_CODE = 'REASON_REJECT_APPROVAL';
update UPL_BPM_SYSDETAIL set CODE_GROUP = 'RSRET', CATEGORY = 'RSRT_DE' where BPM_CATEGORY_CODE = 'REASON_RETURN_DE';
commit;
INSERT INTO CODE_TABLE(CODE_GROUP,CATEGORY,CODE_VALUE1,DESCRIPTION1,DESCRIPTION2,REFERENCE,PARENT_ID)
select CODE_GROUP,CATEGORY,SYS_NAME,SYS_VALUE,DESCRIPTION,BPM_CATEGORY_CODE,
(select ID from CODE_TABLE a where exists (select 1 from CODE_TABLE c where c.CATEGORY = 'RSRET_REJ' and c.CODE_VALUE1 = a.CATEGORY) and a.CODE_VALUE1 = B.CATEGORY)
from UPL_BPM_SYSDETAIL b
where BPM_CATEGORY_CODE in
('APPRAISAL_REJECT_01',
'APPRAISAL_REJECT_02',
'APPRAISAL_REJECT_03',
'APPRAISAL_REJECT_04',
'APPRAISAL_REJECT_05',
'APPRAISAL_REJECT_06',
'APPRAISAL_REJECT_07',
'APPROVAL_REJECT_01',
'APPROVAL_REJECT_02',
'APPROVAL_REJECT_03',
'APPROVAL_REJECT_04',
'APPROVAL_REJECT_05',
'APPROVAL_REJECT_06',
'APPROVAL_REJECT_07',
'APPROVAL_REJECT_08',
'CALL_APPRAISAL_DECISION',
'CA_REASON_RETURN_SALES',
'CA_REASON_RETURN_SALES_01',
'CA_REASON_RETURN_SALES_02',
'CA_REASON_RETURN_SALES_03',
'CA_REASON_RETURN_SALES_04',
'CA_REASON_RETURN_SALES_05',
'DC_REASON_REJECT',
'DC_REASON_RETURN_DE',
'DC_REASON_RETURN_DE_01',
'DC_REASON_RETURN_DE_02',
'DC_REASON_RETURN_DE_03',
'DC_REASON_RETURN_DE_04',
'DC_REASON_RETURN_DE_05',
'DC_REASON_RETURN_DE_06',
'DC_REASON_RETURN_DE_07',
'DC_REASON_RETURN_DE_08',
'DC_REASON_RETURN_DE_09',
'DC_REASON_RETURN_SALES',
'DC_REASON_RETURN_SALES_01',
'DC_REASON_RETURN_SALES_02',
'DC_REASON_RETURN_SALES_03',
'DC_REASON_RETURN_SALES_04',
'DC_REASON_RETURN_SALES_05',
'DC_REASON_RETURN_SALES_06',
'DE_REASON_REJECT',
'DE_REASON_RETURN_SALES',
'DE_REASON_RETURN_SALES_01',
'DE_REASON_RETURN_SALES_02',
'DE_REASON_RETURN_SALES_03',
'DE_REASON_RETURN_SALES_04',
'DE_REASON_RETURN_SALES_05',
'DE_REASON_RETURN_SALES_06',
'INDUSTRY',
'LIFE_INSURANCE_COMPANY',
'REASON_REJECT_APPRAISAL',
'REASON_REJECT_APPROVAL',
'REASON_RETURN_DE');
commit;

-- get position
select distinct concat('Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values(''EMPL'',''EMP_POS'',''',POSITION_CODE,''',''',POSITION_CODE,''');')
from sales_network.sln_staff_position;

-- get staff
select concat('insert into EMPLOYEES(FULL_NAME,EMAIL,HR_CODE,MOBILE_PHONE,EXT_PHONE,START_EFF_DATE,END_EFF_DATE,STAFF_ID_IN_BPM,EMP_CODE) values(''',
NAME,''',''',IFNULL(EMAIL,''),''',''',IFNULL(HR_CODE,''),''',''',IFNULL(MOBILE_PHONE,''),''',''',IFNULL(EXT_PHONE,''),
''',to_date(''',IFNULL(ACTIVE_DATE,''),''',''YYYY-MM-DD HH24:MI:SS''),to_date(''',IFNULL(DEACTIVE_DATE,''),''',''YYYY-MM-DD HH24:MI:SS''),',ID,',',ID,');')
from  sales_network.sln_staff ss

-- get position
select concat('insert into EMPLOYEE_LINK(EMP_ID,EMP_POSITION,DEPARTMENT,EMP_CODE) values((select ID from EMPLOYEES where STAFF_ID_IN_BPM=',STAFF_ID,
'),(select ID from CODE_TABLE where category like ''EM%'' and substr(category,8,2)=''',BUSINESS_LINE,''' and CODE_VALUE1=''',POSITION_CODE,
'''),',DEPARTMENT_ID,',''',STAFF_CODE,''');')
from  sales_network.sln_staff_position ssp 

-- insert role details
insert into ROLE_DETAILS(OBJECT_TYPE,ROLE_ID,OBJECT_ID,ACCESS_RIGHT,STATUS)
select 'F',a.ID,b.ID,decode(B.FUNCTION_CODE,'TSSEARCH','NNNNYNNNN','TSCALL_CUS','NNNNYNNNN','NYYYYNNNN'),
'A' from ROLES a, FUNCTIONS b
where a.ROLE_CODE = 'TS_SUP' --and b.FUNCTION_CODE not in ();

-- insert user role mapping
insert into USERS_ROLE_MAPPING(USER_ID,OBJECT_TYPE,OBJECT_ID,ACCESS_RIGHT,STATUS)
select u.ID,'R',r.ID,null,'A' from USERS u, ROLES r
where u.LOGIN_ID = 'quanlh.ho'
and r.ROLE_CODE in ('TS_SUP','TS_TSA');

Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('MISC','UPL_SRC','TELESALE','Tele sale');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('MISC','UPL_SRC','T24','T24 core');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('MISC','UPL_SRC','WAY4','Way4 credit card');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('MISC','UPL_SRC','CS','Customer service');

Insert into CODE_TABLE(PARENT_ID, CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) 
Values((select id from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1='TELESALE'),'MISC','UPL_TYPE','GC','Campaign chung');
Insert into CODE_TABLE(PARENT_ID, CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) 
Values((select id from CODE_TABLE where CATEGORY='UPL_SRC' and CODE_VALUE1='TELESALE'),'MISC','UPL_TYPE','PC','Campaign riêng');

Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) select 'CUST','IDPLACE',DESCRIPTION1, DESCRIPTION1
from CODE_TABLE where CODE_GROUP = 'LOCA' and CATEGORY = 'PROVINCE';

Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1,REFERENCE) values('LOCA','TRAN_OFF',DESCRIPTION1, DESCRIPTION1)

Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CALL','ALCTYPE_TL','F','Fresh');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CALL','ALCTYPE_TL','W','WIP');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CALL','ALCTYPE_TL','C','Closed');

Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CALL','ALCTYPE_SP','N','New');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CALL','ALCTYPE_SP','A','Allocated all');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CALL','ALCTYPE_SP','P','Partial allocation');

Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('PROD','PRD_GROUP','IS','Installment Loan');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('PROD','PRD_GROUP','CS','Cash Loan');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('PROD','PRD_GROUP','TS','TeleSale');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('PROD','PRD_GROUP','CC','Credit Card');

Insert into CODE_TABLE(PARENT_ID, CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) 
Values((select id from CODE_TABLE where CATEGORY='PRD_GROUP' and CODE_VALUE1='TS'),'EMPL','EM_POS_TS','BDS','Trưởng nhóm bán hàng qua điện thoại');
Insert into CODE_TABLE(PARENT_ID, CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) 
Values((select id from CODE_TABLE where CATEGORY='PRD_GROUP' and CODE_VALUE1='TS'),'EMPL','EM_POS_TS','TSS','Nhập liệu bán hàng qua điện thoại');

delete from CODE_TABLE where CATEGORY = 'CARD_STAT';
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','00','Card OK');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','01','Card Do not honor');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','37','Card No Renewal - Badebt');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','02','PIN tries execeeded');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','14 ','Card Closed');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','76','Card Expired/Zcode 76');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','04','PickUp 04');
Insert into CODE_TABLE(CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) Values('CARD','CARD_STAT','41','PickUp L 41');

Insert into CODE_TABLE(PARENT_ID, CODE_GROUP, CATEGORY, CODE_VALUE1, DESCRIPTION1) 
Values((select id from CODE_TABLE where CATEGORY='PROVINCE' and CODE_VALUE1='77'),'LOCA','DISTRICT','755','Côn Đảo');

Insert into SYSTEM_DEFINE_FIELDS(SYSTEM,CODE_TABLE_ID,CATEGORY,CODE_TABLE_VALUE,SYSTEM_VALUE,DESCRIPTION) 
select 'W4',ID,CATEGORY,CODE_VALUE1,CODE_VALUE2,DESCRIPTION1
from CODE_TABLE where CATEGORY = 'GENDER';
Insert into SYSTEM_DEFINE_FIELDS(SYSTEM,CODE_TABLE_ID,CATEGORY,CODE_TABLE_VALUE,SYSTEM_VALUE,DESCRIPTION) 
select 'T24',ID,CATEGORY,CODE_VALUE1,decode(CODE_VALUE1,'1','MALE','FEMALE'),DESCRIPTION1
from CODE_TABLE where CATEGORY = 'GENDER';


Values((select id from CODE_TABLE where CATEGORY='PRD_GROUP' and CODE_VALUE1='TS'),'EMPL','EM_POS_TS','BDS','Trưởng nhóm bán hàng qua điện thoại');

select 

COMMIT;


CREATE TABLE UPL_BPM_PRODUCTS (
	ID INT,
	CATEGORY_ID INT,
	CATEGORY_NAME VARCHAR(200),
	PRODUCT_CODE VARCHAR(200),
	PRODUCT_NAME VARCHAR(200),
	PTI number(13,5),
	MONTH_INTEREST number(13,5),
	YEAR_INTEREST number(13,5),
	LATE_PENALTY_INTEREST number(13,2),
	LATE_PENALTY_FEE number(13,2),
	MAX_OF_GOODS int,
	STATUS int,
	APPLIED_DATE date,
	EXPIRED_DATE date
);

select ID,CATEGORY_ID,CATEGORY_NAME,PRODUCT_CODE,PRODUCT_NAME,PTI,MONTH_INTEREST,YEAR_INTEREST,
LATE_PENALTY_INTEREST,LATE_PENALTY_FEE,MAX_OF_GOODS,STATUS,APPLIED_DATE,EXPIRED_DATE
from products;

/*
script file:
	replace products by UPL_BPM_PRODUCTS
	replace 00:00:00 by empty
*/

update CREDIT_APP_COMMODITIES a set
BRAND_ID = (select ct.ID from code_table ct, (select cgi.* from CDB_GOODS_INFOMATION cgi inner join 
            (select APPID,COLUMN_ORDER,max(HISTORYINDEX) max_his from CDB_GOODS_INFOMATION
            where STATUS = 1
            group by APPID,COLUMN_ORDER) cgi1
            on cgi.APPID = cgi1.APPID and cgi.COLUMN_ORDER = cgi1.COLUMN_ORDER and cgi.HISTORYINDEX = cgi1.max_his
            where cgi.STATUS = 1) CGI,
            credit_app_bpm cab
where cab.BPM_APP_ID = cgi.appid and cab.CREDIT_APP_ID = a.CREDIT_APP_ID
and a.COMM_SEQ = cgi.COLUMN_ORDER
and ct.category = 'BRAND' and ct.CODE_VALUE1=cgi.BRANDCODE)
where a.BRAND_ID is null;

-- delete parameter of Cash Loan attached with Product of Installment Loan
delete from PRODUCT_CONFIG_PARAMETERS a
where exists (select 1 from PRODUCTS b where b.ID = A.PRODUCT_ID
and B.PRODUCT_CODE in ('M0000004',
'M0000003',
'M0000009',
'M0000011',
'M0000002',
'M0000001',
'E0000010'))
and exists (select 1 from PROCESS_PARAMETERS c where c.ID = A.PARAMETER_ID and C.PROCESS_ID ='357116699583cdc94981987059206300');

-- delete workflow Cash Loan attached with Product of Installment Loan
delete from PROCESS_PRODUCTS a
where exists (select 1 from PRODUCTS b where b.ID = A.PRODUCT_ID
and B.PRODUCT_CODE in ('M0000004',
'M0000003',
'M0000009',
'M0000011',
'M0000002',
'M0000001',
'E0000010'))
and a.PROCESS_ID ='357116699583cdc94981987059206300';

-- delete parameter of Installment Loan attached with Product of Cash Loan
delete from PRODUCT_CONFIG_PARAMETERS a
where exists (select 1 from PRODUCTS b where b.ID = A.PRODUCT_ID
and B.PRODUCT_CODE in ('C0000021',
'C0000030',
'C0000014',
'C0000001',
'C0000008',
'JCBCC'))
and exists (select 1 from PROCESS_PARAMETERS c where c.ID = A.PARAMETER_ID and C.PROCESS_ID ='92489624057e0e0abafb278083044537');

-- delete workflow Installment Loan attached with Product of Cash Loan
delete from PROCESS_PRODUCTS a
where exists (select 1 from PRODUCTS b where b.ID = A.PRODUCT_ID
and B.PRODUCT_CODE in ('C0000021',
'C0000030',
'C0000014',
'C0000001',
'C0000008',
'JCBCC'))
and a.PROCESS_ID ='92489624057e0e0abafb278083044537';

-- delete wrong document mapping
delete from PROCESS_HAS_DOCUMENTS a where (process_id,group_id,document_id) in 
    (select ppd.process_id,ppd.group_id,ppd.document_id from PROCESS_HAS_DOCUMENTS ppd 
        group by ppd.process_id,ppd.group_id,ppd.document_id having count(1) > 1)
and ID = (select min(ID) from PROCESS_HAS_DOCUMENTS b 
where b.process_id = a.process_id and b.group_id = a.group_id and b.document_id = a.document_id);
