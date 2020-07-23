DROP TRIGGER TRG_MESSAGE_LOG_INS;

CREATE OR REPLACE TRIGGER TRG_MESSAGE_LOG_INS
	    BEFORE INSERT ON MESSAGE_LOG 	        FOR EACH ROW
BEGIN
			IF :new.msg_type = 'R' THEN
	            IF :new.trans_id IS NULL OR :new.trans_id = 0 THEN
					select to_number(to_char(sysdate,'RRRRDDD')||lpad(seq_message_log_trans_id.nextval,10,'0')) into :new.trans_id from dual;
                END IF;
			END IF;
END;
/

DROP TRIGGER TRG_CODE_TABLE_INS;

CREATE OR REPLACE TRIGGER TRG_CODE_TABLE_INS
	    BEFORE INSERT ON CODE_TABLE 	        FOR EACH ROW
BEGIN
			IF :new.ID is null THEN
				select SEQ_CODE_TABLE_ID.nextval into :new.ID from dual;
			END IF;
END;
/

CREATE OR REPLACE TRIGGER TRG_PARTNER_INS
BEFORE INSERT ON PARTNER             FOR EACH ROW
BEGIN
IF :new.ID is null THEN
select SEQ_COMMON_ID.nextval into :new.ID from dual;
END IF;
END;
/

