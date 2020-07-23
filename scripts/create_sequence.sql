create sequence seq_message_log_trans_id
  start with 1
  maxvalue 9999999999
  minvalue 1
  cycle
  cache 30
  order
  nokeep
  global;

create sequence seq_cust_personal_info_id
  start with 1
  maxvalue 9999999999999
  minvalue 1
  nocycle
  cache 30
  order
  nokeep
  global;

create sequence seq_cust_account_link_id
  start with 1
  maxvalue 9999999999999
  minvalue 1
  nocycle
  cache 30
  order
  nokeep
  global;

create sequence seq_message_log_id
  start with 1
  maxvalue 99999999999999999
  minvalue 1
  nocycle
  cache 30
  order
  nokeep
  global;

create sequence seq_credit_app_outstanding_id
  start with 1
  maxvalue 999999999999999
  minvalue 1
  cycle
  cache 30
  order
  nokeep
  global;

create sequence seq_credit_app_transaction_id
  start with 1
  maxvalue 999999999999999
  minvalue 1
  cycle
  cache 30
  order
  nokeep
  global;

