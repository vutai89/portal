CREATE TABLESPACE "EBANKING" DATAFILE
'/u01/app/oracle/oradata/MBF/MBFPDB01/ebanking.dbf' SIZE 512M AUTOEXTEND ON NEXT 128M
LOGGING ONLINE PERMANENT BLOCKSIZE 8192
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 128K SEGMENT SPACE MANAGEMENT AUTO;

alter session set "_ORACLE_SCRIPT"=true;
CREATE USER ebanking
   IDENTIFIED BY ebanking
   DEFAULT TABLESPACE ebanking
   QUOTA unlimited ON ebanking
   TEMPORARY TABLESPACE temp
     PROFILE DEFAULT;
  -- 1 System Privilege for DIP 
  GRANT connect, resource TO ebanking;

CREATE TABLESPACE "MCPORTAL_DATA" DATAFILE
'/u01/app/oracle/oradata/MBF/MBFPDB01/mcportal_data01.dbf' SIZE 512M AUTOEXTEND ON NEXT 128M
LOGGING ONLINE PERMANENT BLOCKSIZE 8192
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 128K SEGMENT SPACE MANAGEMENT AUTO;

CREATE TABLESPACE "MCPORTAL_LOB" DATAFILE
'/u01/app/oracle/oradata/MBF/MBFPDB01/mcportal_lob01.dbf' SIZE 512M AUTOEXTEND ON NEXT 128M
LOGGING ONLINE PERMANENT BLOCKSIZE 8192
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 128K SEGMENT SPACE MANAGEMENT AUTO;

CREATE TABLESPACE "MCPORTAL_INDEX" DATAFILE
'/u01/app/oracle/oradata/MBF/MBFPDB01/mcportal_index01.dbf' SIZE 512M AUTOEXTEND ON NEXT 128M
LOGGING ONLINE PERMANENT BLOCKSIZE 8192
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 128K SEGMENT SPACE MANAGEMENT AUTO;

-- expdp DMPDIR=/u01/app/oracle/admin/MBF/dpdump
export ORACLE_BASE=/u01/app/oracle
export ORACLE_HOME=/u01/app/oracle/product/12.2.0/dbhome_1
export PATH=$PATH:$ORACLE_HOME/bin
export ORACLE_SID=MBFPDB01
export expDate=$(date +%Y%m%d_%H%M%S)
expdp system/slXrrWEmydmA3vg9@MBFPDB01 SCHEMAS=mcportal PARALLEL=3 DIRECTORY=DMPDIR DUMPFILE=mcportal.$expDate.%U.dmp logfile=mcportal.$expDate.log

-- delete old dump file
cd /u01/app/oracle/admin/MBF/dpdump
find . -name 'mcportal*.dmp' -mtime +10 -exec rm -f {} \;

-- impdp
impdp system/slXrrWEmydmA3vg9@MBFPDB01 schemas=mcportal remap_schema=mcportal:sit_mcp directory=DMPDIR dumpfile=mcportal.20180705_162208.%U.dmp parallel=3 logfile=imp_mcportal.log

impdp system/password@MBFPDB01 schemas=sit_mcp remap_schema=sit_mcp:mcportaluser directory=DMPDIR dumpfile=sit_mcp.20180719.%U.dmp parallel=1 logfile=imp_sitportal.log

alter session set "_ORACLE_SCRIPT"=true;
CREATE USER mcportal
   IDENTIFIED BY mcportal
   DEFAULT TABLESPACE MCPORTAL_DATA
   QUOTA unlimited ON MCPORTAL_DATA
   TEMPORARY TABLESPACE temp
     PROFILE DEFAULT;
  -- 1 System Privilege for DIP 
  GRANT connect, resource TO mcportal;
alter user mcportal QUOTA unlimited ON MCPORTAL_LOB;
alter user mcportal QUOTA unlimited ON MCPORTAL_INDEX;
grant create synonym to mcportal;
grant create view to mcportal;
grant create database link to mcportal;


CREATE USER SIT_MCP
  IDENTIFIED BY mcportal
  DEFAULT TABLESPACE MCPORTAL_DATA
  TEMPORARY TABLESPACE TEMP
  PROFILE DEFAULT
  ACCOUNT UNLOCK;
  -- 2 Roles for SIT_MCP 
  GRANT CONNECT TO SIT_MCP;
  GRANT RESOURCE TO SIT_MCP;
  ALTER USER SIT_MCP DEFAULT ROLE ALL;
  -- 5 System Privileges for SIT_MCP 
  GRANT CREATE DATABASE LINK TO SIT_MCP;
  GRANT CREATE SYNONYM TO SIT_MCP;
  GRANT CREATE VIEW TO SIT_MCP;
  GRANT DEBUG ANY PROCEDURE TO SIT_MCP;
  GRANT DEBUG CONNECT SESSION TO SIT_MCP;
  -- 3 Tablespace Quotas for SIT_MCP 
  ALTER USER SIT_MCP QUOTA UNLIMITED ON MCPORTAL_DATA;
  ALTER USER SIT_MCP QUOTA UNLIMITED ON MCPORTAL_INDEX;
  ALTER USER SIT_MCP QUOTA UNLIMITED ON MCPORTAL_LOB;
	GRANT EXECUTE ON SYS.DBMS_DEBUG TO SIT_MCP;

