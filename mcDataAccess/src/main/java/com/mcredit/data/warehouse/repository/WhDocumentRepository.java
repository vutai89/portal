package com.mcredit.data.warehouse.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.LongType;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.warehouse.entity.WhDocument;
import com.mcredit.model.dto.warehouse.GoodsDTO;
import com.mcredit.model.dto.warehouse.WareHousePayBackCavetDTO;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.enums.WHStep;
import com.mcredit.model.object.SearchCaseInput;
import com.mcredit.model.object.warehouse.CheckRecordsCavetInfo;
import com.mcredit.model.object.warehouse.RemainDocument;
import com.mcredit.model.object.warehouse.ReturnDocumentInfo;
import com.mcredit.model.object.warehouse.WareHouseCodeTableCacheDTO;
import com.mcredit.model.object.warehouse.WareHouseHistory;
import com.mcredit.model.object.warehouse.WareHouseMatrix;
import com.mcredit.model.object.warehouse.WareHousePaperReceipt;
import com.mcredit.model.object.warehouse.WareHousePayBackCavet;
import com.mcredit.model.object.warehouse.WareHousePayBackLetter;
import com.mcredit.model.object.warehouse.WareHouseSeachObject;
import com.mcredit.model.object.warehouse.WareHouseThankLetter;
import com.mcredit.model.object.warehouse.WhDocumentDTO;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.PartitionListHelper;
import com.mcredit.util.StringUtils;

public class WhDocumentRepository extends BaseRepository implements IUpsertRepository<WhDocument>,IUpdateRepository<WhDocument>,IAddRepository<WhDocument> {

	public WhDocumentRepository(Session session) {
		super(session);
	}
	
	@Override
	public void update(WhDocument item) {
		this.session.saveOrUpdate("WhDocument", item);
                this.session.flush();
                this.session.clear();
	}
	
	@Override
	public void upsert(WhDocument item) {
		if (item.getId() == null) {
			this.session.save("WhDocument", item);
		} else {
			this.session.merge("WhDocument", item);
		}
		
		this.session.flush();
		this.session.clear();
	}
	
	@Override
	public void add(WhDocument item) {
		this.session.save("WhDocument", item);
                this.session.flush();
                this.session.clear();
	}

	public WhDocument getById(Long id) {
		return this.session.find(WhDocument.class, id);
	}
	
	public void clone(WhDocument clone) {
		this.session.detach(clone);
		this.session.persist(clone);
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> checkIdWHDocument(List<Long> idDocuments) {
		List<Long> listId = this.session.getNamedNativeQuery("checkIdWHDocument").setParameter("idDocuments", idDocuments).getResultList();
		return listId != null ? listId : new ArrayList<>();
	}
	
	public void updateStatusReturnDoc(Long idDocument, Long statusValue) {
		// TODO Auto-generated method stub
		NativeQuery<?> query = session.getNamedNativeQuery("updateStatusReturnDoc");
		query.setParameter("status", statusValue);
		query.setParameter("docId", idDocument);
		query.executeUpdate();
	}
		
	public Object searchCaseInput(String batchId, Boolean count , WHStep _step ,SearchCaseInput seachCase, String loginId,WareHouseCodeTableCacheDTO wareHouseCodeTableID,Integer pageSize, Integer pageNum) {
		List<?> lst = null;
		String colums = " SELECT " ;
		String extQuerry = " WHERE car.mc_contract_number is not null ";
		String dateFormat = ",'" + DateFormatTag.DATEFORMAT_dd_MM_yyyy.value() + "') ";
		String tables = "" ;
		String dateAdd = "+ 1";
		String orderby = " ORDER BY whd.id ASC  ";
		HashMap<String, Object> parameterList = new HashMap<>();
		Query query = null  ;

		/* thong in Warehouse */
		if(WHStep.THANK_LETTER != _step){
		colums = colums + " whd.id as whId, whd.created_by AS importer, whd.created_date AS importdate, whd.doc_type AS doctypeid, null AS doctypecode, null AS doctypename, whd.status as statusWH, null AS statuswhcode "
			+ ", null AS statuswhname, whc.code as whCode, whd.created_date as actualReceiptDate, whd.last_updated_date AS whlastupdate, whd.last_updated_by AS whlastupdateby, whd.batch_id as batchId, whd.version version, whd.order_by as orderBy, whd.wh_code_id as whCodeId,whd.wh_lodge_by as whLoggeBy "
			+ ", whd.wh_lodge_date as whLodgeDate, whd.estimate_date as estimateDate, whd.contract_cavet_type AS contractcavettype,CASE contract_cavet_type  WHEN 1 THEN 'C?p nh?t l?i cho h? s? kho?n vay'  WHEN 2 THEN 'C?p nh?t l?i cho CAVET' ELSE '' END as contractCavetName "
			+ ", whd.bill_code as billCode,whd.delivery_error as deliveryError, whd.IS_ACTIVE as isActive, whd.IS_ORIGINAL as isOriginal, whd.note as errorNote , whd.appendix_contract as appendixContract " ;
			if(WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step || WHStep.THANK_LETTER == _step){
				colums = colums + ", whExpected.EXPECTED_DATE as expectedDate " ;
			}else{
				colums = colums + ", null as expectedDate " ;
			}
		}else {
			colums = colums + " null as whId,null as  importer,null as  importdate,null as  doctypeid,null as  doctypecode,null as  doctypename,null as  statusWH,null as  statuswhcode,null as  statuswhname,null as  whCode,null as  actualReceiptDate,null as  whlastupdate,null as  whlastupdateby,null as  batchId,null as  version,null as  orderBy"
					+ ",null as  whCodeId,null as  whLoggeBy,null as  whLodgeDate,null as  estimateDate,null as  contractcavettype,null as  contractCavetName,null as  billCode,null as  deliveryError,null as  isActive,null as  isOriginal,null as  errorNote ,null as  appendixContract,null as  expectedDate  " ;
		}
		/* thong tin BPM*/
		colums = colums + ", car.mc_contract_number as contractNum, car.contract_date as contractdate, " + (WHStep.THANK_LETTER == _step ? "cal.actual_closed_date as actualcloseddate " :" null as  actualcloseddate ") +", cpi.cust_name as custName,cab.bpm_app_number as caseNum, cab.credit_app_id as creditAppId, car.trans_office_id as postCodeId, null AS postcode, null AS postcodename " ;
			
		/* so chung minh thu nhan danh */
		if(WHStep.INPUTCASE == _step ||WHStep.FOLLOWCASE == _step || WHStep.THANK_LETTER == _step ){
			colums = colums + ", CI.IDENTITY_NUMBER as indentityNum, CI.IDENTITY_ISSUE_DATE as indentityIssueDate, CI.IDENTITY_EXPIRY_DATE as indentityExpiryDate, CI.IDENTITY_ISSUE_PLACE as indentityIssuePlace,CI.IDENTITY_ISSUE_PLACE_TEXT as indentityIssuePlaceText "
			+ ", ci.IDENTITY_TYPE_ID as indentityTypeId , null as indentityTypeCode , null as indentityTypeName "
			+( WHStep.THANK_LETTER == _step ? " ,cai.address as address , cai.ward as wardId , cai.district as districtId ,cai.province as provinceId ,null as  addressAll ,cci.contact_value as custMobile" : " ,null as address ,null as wardId ,null as districtId ,null as provinceId ,null as  addressAll ,cci.contact_value as custMobile") ;
		}else{
			colums = colums + ", null as indentityNum, null as indentityIssueDate, null as indentityExpiryDate, null as indentityIssuePlace, null as indentityIssuePlaceText , null as indentityTypeId, null as indentityTypeCode , null as indentityTypeName ,null as address , null as wardId , null as districtId ,null as provinceId ,null as  addressAll , null as custMobile ";
		}
		
		/* thong tin Sale*/
	    if(WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step || WHStep.OPERATOR_TWO == _step  || WHStep.LODGE_CONTRACT == _step || WHStep.LODGE_CAVET == _step || WHStep.LOAD_BATCH == _step || WHStep.BORROW_CONTRACT == _step || WHStep.BORROW_CAVET == _step || WHStep.RETURN_CAVET == _step || WHStep.APPROVE == _step){
	    	colums = colums + ", car.sale_id, emp.full_name AS salename, emp.mobile_phone AS sale_mobile, emp.email AS sale_email, car.status AS statusbpm, null AS statusbpmcode, null AS statusbpmname , cab.last_updated_date AS approvedDateBPM, empl_bsd.full_name as bdsFullName, empl_bsd.email as bdsEmail, empl_bsd.mobile_phone AS bdsMobile" ; 
	    } else {
	    	colums = colums + ", null as saleId,null AS saleName,null AS mobileSale,null AS saleEmail,null AS statusbpm,null AS statusbpmcode,null AS statusbpmname, null AS approvedDateBPM,null as bdsFullName,null as bdsEmail,null as bdsMobile ";
	    }
	    			
	    /* thong tin san pham */
	    if( WHStep.LODGE_CONTRACT == _step || WHStep.LODGE_CAVET == _step || WHStep.BORROW_CONTRACT == _step || WHStep.BORROW_CAVET == _step  || WHStep.FOLLOWCASE == _step || WHStep.RETURN_CAVET == _step){
	    colums = colums + ", car.product_id as productId, pro.product_code as productCode, pro.product_name as productName, pro.product_group_id as productGroupId, null AS productgroupcode,null AS productgroupname, cab.workflow AS workflow , null AS workflowid, null AS workflowdesc1, null AS workflowdesc2 " ;
	    }else {
	    	colums = colums + ", null as productId, null as productCode, null as productName, null as productGroupId, null AS productgroupcode,null AS productgroupname, null AS workflow, null AS workflowid,null AS workflowdesc1,null AS workflowdesc2 " ;	
		}
    
	    /*  phan bo */ 
	    if(WHStep.ALLOCATION == _step || WHStep.OPERATOR_TWO == _step || WHStep.LODGE_CONTRACT == _step || WHStep.LODGE_CAVET == _step ){
	        colums = colums + ", CASE WHEN ad.last_updated_by IS NULL THEN ad.created_by ELSE ad.last_updated_by END AS allocator, CASE WHEN ad.last_updated_date IS NULL THEN ad.created_date ELSE ad.last_updated_date END  AS allocationdate , ad.status AS allocatestatus, null AS allocatecode, null AS allocatedes, ad.note AS allocatenote, ad.assignee_id AS assigneeid , user_assig.login_id AS assigneelogin, user_assig.usr_full_name AS assigneename " 
	    	+ ", ad.assignee_id AS useroperator2id, user_assig.usr_full_name AS useroperator2fullname, DECODE(whd.PROCESS_STATUS,0,'Ch\u01B0a x\u1EED l\u00FD.','\u0110\u00E3 x\u1EED l\u00FD.') AS processop2status, whd.PROCESS_DATE AS processop2date ";
	    }else{
	    	 colums = colums + ", null as allocator,null as allocationDate, null AS allocatestatus,null AS allocatecode,null AS allocatedes,null AS allocatenote, null AS assigneeid, null AS assigneelogin,null AS assigneename,null AS useroperator2id,null AS useroperator2fullname, null AS processop2status,null AS processop2date ";
	    }
		
		if (WHStep.LODGE_CAVET == _step || WHStep.BORROW_CAVET == _step || WHStep.FOLLOWCASE == _step || WHStep.THANK_LETTER == _step) {
		/* hang hoa */
			colums = colums + ", cac.frame_number, cac.serial_number, cac.number_plate, cac.comm_id AS goodsid, null AS goodscode, null AS goodsdesc, cac.brand_id AS brandid "
				+ ", null AS brandcode, null AS brandname, cac.model_id AS modelid, null AS modeltypecode, cac.MODEL AS modeltypedesc , cac.comm_id as commodityId , null as commodityCode , DECODE(cac.comm_id ,null,cac.COMM_OTHER,NULL) AS commoditydesc " ;
		} else {
			colums = colums + ", null as frame_number,null as serial_number,null as NUMBER_PLATE , null as goodsId,null as  goodsCode, null as goodsDESC, null as brandId, null as brandCode, null as brandName, null as modelId, null as modelTypeCode, null as modelTypeDESC , null as commodity , null as commodityCode , null as commodityDESC " ;
		}	
		
		if (WHStep.BORROW_CONTRACT == _step || WHStep.BORROW_CAVET == _step || WHStep.LODGE_CAVET == _step || WHStep.RETURN_CAVET == _step || WHStep.APPROVE == _step) {
			/* cho muon xuat tra */
			if (WHStep.RETURN_CAVET == _step) {
				colums = colums
						+ ", whrd.object_to AS borrowerid, nvl(users_whrd.usr_full_name,( SELECT full_name FROM employees WHERE id = users_whrd.emp_id )) AS borrower,users_whrd.login_id as borrowerUsername, whrd.BORROWED_DATE AS borrowdate, whrd.department_name AS departmentname "
						+ ", whrd.appointment_date AS appoinmentdate, whrd.extension_date AS extensionborrowdate, whrd.reject_reason AS rejectreason, whrd.approve_status AS statusapprove,whrd.CREATED_DATE AS toApproveDate, whrd.approve_date AS approvalDate "
						+ ", null AS statusapprovecode, null AS statusapprovedesc ";
			} else {
				colums = colums
						+ ", whbd.object_to AS borrowerid, nvl(users_whbd.usr_full_name,( SELECT full_name FROM employees WHERE id = users_whbd.emp_id )) AS borrower,users_whbd.login_id as borrowerUsername, whbd.BORROWED_DATE AS borrowdate, whbd.department_name AS departmentname "
						// + ", whrd.object_to AS returneeid, nvl(users_whrd.usr_full_name,( SELECT full_name FROM employees WHERE id = users_whrd.emp_id )) AS returnee,users_whrd.login_id as returneeUsername, whrd.RETURN_DATE AS returneedate "
						+ ", whbd.appointment_date AS appoinmentdate, whbd.extension_date AS extensionborrowdate, whbd.reject_reason AS rejectreason, whbd.approve_status AS statusapprove,whbd.CREATED_DATE AS toApproveDate, whbd.approve_date AS approvalDate "
						+ ", null AS statusapprovecode, null AS statusapprovedesc ";
			}

			if (WHStep.RETURN_CAVET == _step) {
				colums = colums + ", whrd.return_date as returnDate";
			} else if (WHStep.APPROVE == _step) {
				colums = colums + ", whbd.return_date as returnDate";
			} else {
				colums = colums + ", whrd.return_date as returnDate ";
			}
		} else {
			colums = colums
					+ ", null AS borrowerid,null AS borrower,null as borrowerUsername,null AS borrowdate,null AS departmentname ,null AS appoinmentdate,null AS extensionborrowdate,null AS rejectreason,null AS statusapprove,null AS toApproveDate,null AS approvalDate,null AS statusapprovecode,null AS statusapprovedesc , null as returnDate ";
		}
		
		/* thong tin cavet tren cavet  */
		if ( WHStep.BORROW_CAVET == _step || WHStep.LODGE_CAVET == _step || WHStep.APPROVE == _step || WHStep.THANK_LETTER == _step || WHStep.RETURN_CAVET == _step) {
			colums = colums + ", cavet_info.ID AS cavetInfoId,cavet_info.created_by as  cavetInfoCreatedBy,cavet_info.last_updated_date as cavetInfoLastUpDate,cavet_info.last_updated_by as cavetInfoLastUpDateBy "					
			+ ", cavet_info.created_date as cavetInfoCreatedDate,cavet_info.wh_doc_id as cavetInfoWhDocId,cavet_info.brand as cavetInfoBrand,cavet_info.model_code as cavetInfoModelCode,cavet_info.color as cavetInfoColor "
			+ ", cavet_info.engine as cavetInfoEngine,cavet_info.chassis as cavetInfoChassis,cavet_info.n_plate as cavetInfoNPlate,cavet_info.cavet_number as cavetInfoCavetNum,cavet_info.type as cavetInfoType,cavet_info.version as cavetInfoVersion "
			/*  thong tin cavet tren hop dong  */
			+ ", cavet_appendix.ID as cavetAppendixId,cavet_appendix.CREATED_BY as cavetAppendixCreatedBy,cavet_appendix.last_updated_date as cavetAppendixLastUpDate,cavet_appendix.last_updated_by as	cavetAppendixLastUpDateBy "
			+ ", cavet_appendix.created_date as cavetAppendixCreatedDate,cavet_appendix.wh_doc_id as cavetAppendixWhDocId,cavet_appendix.brand as cavetAppendixBrand,cavet_appendix.model_code as cavetAppendixModelCode,cavet_appendix.color as cavetAppendixColor "
			+ ", cavet_appendix.ENGINE as cavetAppendixEngine,cavet_appendix.chassis as cavetAppendixChassis,cavet_appendix.n_plate as cavetAppendixNPlate,cavet_appendix.cavet_number as cavetAppendixCavetNum,cavet_appendix.type as cavetAppendixType,cavet_appendix.version as cavetAppendixVersion ";
		}
		
		if (WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step || WHStep.THANK_LETTER == _step) {
			tables = " FROM credit_app_bpm cab " + " LEFT JOIN credit_app_request car  ON car.id = cab.credit_app_id "
					+ "	LEFT JOIN credit_app_lms cal ON car.id = cal.credit_app_id "
					+ "	LEFT JOIN wh_document whd ON car.id = whd.credit_app_id ";

			if (WHStep.THANK_LETTER == _step) {
				tables = tables + " AND whd.IS_ORIGINAL = 1 AND whd.DOC_TYPE =:doctypeCavet ";
				parameterList.put("doctypeCavet", wareHouseCodeTableID.getWhDocTypeCavet());
			}

			if (seachCase.getDocTypeId() != null && !seachCase.getDocTypeId().equals(0l)) {
				tables = tables + " AND whd.doc_type = :docTypeId ";
				parameterList.put("docTypeId", seachCase.getDocTypeId());
			}

		} else {
			tables = " FROM  wh_document whd " + " INNER JOIN credit_app_request car ON car.id = whd.credit_app_id  "
					+ " INNER JOIN  credit_app_bpm cab ON cab.credit_app_id = car.id ";

			if (seachCase.getDocTypeId() != null && !seachCase.getDocTypeId().equals(0l)) {
				extQuerry = extQuerry + " AND whd.doc_type = :docTypeId ";
				parameterList.put("docTypeId", seachCase.getDocTypeId());
			}
		}
		
		if (WHStep.LODGE_CAVET == _step || WHStep.BORROW_CAVET == _step || WHStep.FOLLOWCASE == _step || WHStep.RETURN_CAVET == _step || WHStep.THANK_LETTER == _step) {
			/* bang hang hoa */
			tables = tables + "  LEFT JOIN credit_app_commodities cac ON car.id = cac.credit_app_id ";
		}
		
		/* bang khach hang */
		tables = tables + " LEFT JOIN cust_personal_info cpi  ON cpi.id = car.cust_id "
		/* bang thong tin tren BPM */
				+ "	LEFT JOIN employees emp ON car.sale_id = emp.id "
				+ "	LEFT JOIN employee_link empl ON emp.id = empl.emp_id and empl.EMP_POSITION IN (:lstEmPosTs) "
				+ " LEFT JOIN employees empl_bsd ON ( empl.manager_id = empl_bsd.id and empl.status = 'A') ";

		if (WHStep.THANK_LETTER == _step) {
			tables = tables
					+ " LEFT JOIN credit_app_lms cal ON car.id = cal.credit_app_id LEFT JOIN  cust_addr_info cai ON cai.cust_id = cpi.id AND  cai.addr_type =:addType ";
			parameterList.put("addType", wareHouseCodeTableID.getAddType());
		}

		parameterList.put("lstEmPosTs", wareHouseCodeTableID.getLstEM_POS_TS());

		/* so chung minh thu nhan danh */
		if (WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step || WHStep.THANK_LETTER == _step) {
			tables = tables + " LEFT join cust_identity ci on cpi.identity_id = ci.id "
					+ "LEFT JOIN cust_contact_info cci ON  cpi.id = cci.cust_id AND cci.contact_priority = 1 AND cci.contact_type =:contactType  AND cci.CONTACT_CATEGORY =:contactCategory";
			
			parameterList.put("contactType", wareHouseCodeTableID.getContactType());
			parameterList.put("contactCategory", wareHouseCodeTableID.getContactCategory());
		}

		/* bang wareHouse */
		tables = tables + "  LEFT JOIN wh_code whc ON whd.wh_code_id = whc.id  ";

		if (WHStep.INPUTCASE == _step || WHStep.FOLLOWCASE == _step || WHStep.THANK_LETTER == _step) {
			tables = tables
					+ " LEFT JOIN wh_expected_date whExpected ON whExpected.credit_app_id = whd.credit_app_id AND whExpected.doc_type = whc.doc_type ";
		}

		/* thong tin product */
		tables = tables + " LEFT JOIN products pro ON car.product_id = pro.id ";

		if (WHStep.THANK_LETTER != _step) {
			tables = tables
					+ " LEFT JOIN allocation_detail ad ON whd.id = ad.upl_object_id AND ad.upl_detail_id =:uplDetaiId  LEFT JOIN users user_assig ON user_assig.id = ad.assignee_id ";

			parameterList.put("uplDetaiId", wareHouseCodeTableID.getWhUplDetailId());
		}
			  
		  
		if (WHStep.LODGE_CAVET == _step || WHStep.BORROW_CAVET == _step || WHStep.BORROW_CONTRACT == _step || WHStep.RETURN_CAVET == _step) {
			tables = tables
					+ "	LEFT JOIN wh_borrowed_return_document whbd ON whbd.wh_doc_id = whd.id AND whbd.record_status = 'A' AND whbd.type =:typeBorrow  "
					+ " LEFT JOIN wh_borrowed_return_document whrd ON whrd.wh_doc_id = whd.id AND whrd.record_status = 'A' AND whrd.type =:typeReturn"
					+ " LEFT JOIN users users_whbd ON users_whbd.id = whbd.object_to "
					+ " LEFT JOIN users users_whrd ON users_whrd.id = whrd.object_to ";

			parameterList.put("typeBorrow", wareHouseCodeTableID.getTypeBorrow());
			parameterList.put("typeReturn", wareHouseCodeTableID.getTypeReturn());
		}

		if (WHStep.LODGE_CAVET == _step || WHStep.BORROW_CAVET == _step || WHStep.RETURN_CAVET == _step || WHStep.THANK_LETTER == _step || WHStep.APPROVE == _step) {
			tables = tables
					+ " LEFT JOIN wh_cavet_info cavet_info ON cavet_info.wh_doc_id = whd.id AND cavet_info.type = :whCaveTypeInCavet"
					// + " AND cavet_info.VERSION = ( SELECT MAx(VERSION ) FROM wh_cavet_info WHERE WH_DOC_ID = whd.id ) " //version new
					+ " LEFT JOIN wh_cavet_info cavet_appendix ON cavet_appendix.wh_doc_id = whd.id AND cavet_appendix.type = :whCaveTypeInAppendix ";
					// + " AND cavet_info.VERSION = ( SELECT MAx(VERSION ) FROM  wh_cavet_info WHERE WH_DOC_ID = whd.id ) "; //version new

			parameterList.put("whCaveTypeInCavet", wareHouseCodeTableID.getWhCaveTypeInCavet());
			parameterList.put("whCaveTypeInAppendix", wareHouseCodeTableID.getWhCaveTypeInAppendix());
		}
	
		if (WHStep.APPROVE == _step) {
			tables = tables
					+ "	LEFT JOIN wh_borrowed_return_document whbd ON whbd.wh_doc_id = whd.id AND whbd.record_status = 'A'  "
					+ " LEFT JOIN users users_whbd ON users_whbd.id = whbd.object_to ";

			extQuerry = extQuerry + " AND whd.doc_type =:whDocTypeCavet AND is_original = 1 ";
			extQuerry = extQuerry + " AND whd.status IN (:whdStatusBorrowReturnCavet) ";
			extQuerry = extQuerry + " AND (whbd.approve_status IN (:whApprovalStatusBorrowCavet) OR whbd.approve_status IN (:whApprovalStatusReturnCavet))";

			parameterList.put("whDocTypeCavet", wareHouseCodeTableID.getWhDocTypeCavet());
			parameterList.put("whdStatusBorrowReturnCavet", wareHouseCodeTableID.getWhcStatusBorrowCavet());
			parameterList.put("whApprovalStatusBorrowCavet", wareHouseCodeTableID.getWhApprovalStatusBorrowCavet());
			parameterList.put("whApprovalStatusReturnCavet", wareHouseCodeTableID.getWhApprovalStatusReturnCavet());
		}

		if (!StringUtils.isNullOrEmpty(seachCase.getContractNum())) {
			ArrayList<List<String>> tmp = new ArrayList<List<String>>(PartitionListHelper.partition(Arrays.asList(seachCase.getContractNum().split("\\s*,\\s*")), 1000));
			for (int i = 0; i < tmp.size(); i++) {
				if (i == 0) {
					extQuerry = extQuerry + " AND CAR.mc_contract_number IN(:contractNum" + i + " )";
					parameterList.put("contractNum" + i, tmp.get(i));
				} else {
					extQuerry = extQuerry + " OR CAR.mc_contract_number IN(:contractNum" + i + " )";
					parameterList.put("contractNum" + i, tmp.get(i));
				}
			}
		}
		
		if (WHStep.INPUTCASE == _step) {
			extQuerry = extQuerry + " AND car.contract_date IS NOT NULL  AND NVL(car.status,-1) NOT IN (:carStatus) ";
			orderby = " order by importdate desc ";
			
			parameterList.put("carStatus", wareHouseCodeTableID.getCarStatus());
		}

		if (WHStep.ALLOCATION == _step) {
			extQuerry = extQuerry + " AND whd.status IN (:whStatusAllocation) ";
			parameterList.put("whStatusAllocation", wareHouseCodeTableID.getWhStatusAllocation());

			if (seachCase.getStatusProcess() != null && seachCase.getStatusProcess().equals(0)) {
				extQuerry = extQuerry + " AND whd.process_status = 0 ";
			}

			if (seachCase.getStatusProcess() != null && seachCase.getStatusProcess().equals(1)) {
				extQuerry = extQuerry + " AND whd.process_status = 1 ";
			}

			orderby = " order by doctypeid asc, whd.created_date asc, whd.id asc ";
		}

		if (WHStep.OPERATOR_TWO == _step) {
			extQuerry = extQuerry + " AND ad.status IN (:statusAllocation)  AND whd.status IN (:lstIdCodetabeWH_LOGDE) ";

			parameterList.put("statusAllocation", wareHouseCodeTableID.getStatusAllocation());
			parameterList.put("lstIdCodetabeWH_LOGDE", wareHouseCodeTableID.getLstIdCodetabeWH_LOGDE());

			if (seachCase.getStatusProcess() != null && seachCase.getStatusProcess().equals(0)) {
				extQuerry = extQuerry + " AND whd.PROCESS_STATUS = 0  ";
			}

			if (seachCase.getStatusProcess() != null && seachCase.getStatusProcess().equals(1)) {
				extQuerry = extQuerry + " AND whd.PROCESS_STATUS = 1  ";
			}

			if (!StringUtils.isNullOrEmpty(seachCase.getProcessDateFrom())) {
				extQuerry = extQuerry + "AND whd.process_date >= TO_DATE(:processDateFrom " + dateFormat;
				
				parameterList.put("processDateFrom", seachCase.getProcessDateFrom());
			}

			if (!StringUtils.isNullOrEmpty(seachCase.getProcessDateTo())) {
				extQuerry = extQuerry + "AND whd.process_date < TO_DATE(:processDateTo " + dateFormat + dateAdd;
				
				parameterList.put("processDateTo", seachCase.getProcessDateTo());
			}

			orderby = " order by assigneelogin asc, allocationdate asc, whd.id asc ";
		}

		if (WHStep.LODGE_CONTRACT == _step) {
			extQuerry = extQuerry + "  AND whd.status IN (:whStatusLodgeContract)";
			
			parameterList.put("whStatusLodgeContract", wareHouseCodeTableID.getWhStatusLodgeContract());
		}

		if (WHStep.LODGE_CAVET == _step) {
			extQuerry = extQuerry + " AND whd.doc_type IN (:whdocTypeForLodgeCavet) AND WHD.status IN (:whStatusLodgeCavet) ";
			
			parameterList.put("whdocTypeForLodgeCavet", wareHouseCodeTableID.getWhdocTypeForLodgeCavet());
			parameterList.put("whStatusLodgeCavet", wareHouseCodeTableID.getWhStatusLodgeCavet());
		}

		if (WHStep.LODGE_CONTRACT == _step || WHStep.LODGE_CAVET == _step ) {
			if (!StringUtils.isNullOrEmpty(seachCase.getStorageStatus())
					&& seachCase.getStorageStatus().split(",").length == 1) {
				if (wareHouseCodeTableID.getDateOperation2ASC().contains(Integer.parseInt(seachCase.getStorageStatus()))) {
					orderby = " order by assigneelogin asc, processOP2Date asc ";
				}

				if (wareHouseCodeTableID.getDateStorageDESC().contains(Integer.parseInt(seachCase.getStorageStatus()))) {
					orderby = " order by assigneelogin asc, whLodgeDate desc, whCode desc ";
				}

				if (wareHouseCodeTableID.getDateBorrowASC().contains(Integer.parseInt(seachCase.getStorageStatus()))) {
					orderby = " order by assigneelogin asc, borrowDate asc, whd.id asc ";
				}

				if (wareHouseCodeTableID.getDateReturnDESC().contains(Integer.parseInt(seachCase.getStorageStatus()))) {
					orderby = " order by assigneelogin asc, returnDate desc, whd.id asc ";
				}

				if (wareHouseCodeTableID.getDateReceiveASC().contains(Integer.parseInt(seachCase.getStorageStatus()))) {
					orderby = " order by assigneelogin asc, importdate asc, whd.id asc ";
				}
			}
			
		}
		
		if (WHStep.LODGE_CONTRACT == _step || WHStep.BORROW_CONTRACT == _step) {
			extQuerry = extQuerry + "  AND  WHD.DOC_TYPE = :whDocTypeLoanDoc ";
			
			parameterList.put("whDocTypeLoanDoc", wareHouseCodeTableID.getWhDocTypeLoanDoc());
		}

		if (WHStep.BORROW_CONTRACT == _step) {
			extQuerry = extQuerry + " AND whd.status IN (:whStatusBorrowContract) ";
			parameterList.put("whStatusBorrowContract", wareHouseCodeTableID.getWhStatusBorrowContract());
			
			if (!StringUtils.isNullOrEmpty(seachCase.getStorageStatus()) && seachCase.getStorageStatus().split(",").length == 1) {
				if (wareHouseCodeTableID.getDateStorageDESC().contains(Integer.parseInt(seachCase.getStorageStatus()))) {
					orderby = " order by whLodgeDate desc, whCode desc ";
				} else if (wareHouseCodeTableID.getDateBorrowASC().contains(Integer.parseInt(seachCase.getStorageStatus()))) {
					orderby = " order by borrowDate asc, whCode asc ";
				}
			} else {
				orderby = " order by whLodgeDate asc, whCode asc ";
			}
		}

		if (WHStep.BORROW_CAVET == _step) {
			extQuerry = extQuerry + " AND whd.doc_type =:whDocTypeCavet ";
			extQuerry = extQuerry + " AND whd.status IN (:whcStatusBorrowCavet)  ";

			parameterList.put("whDocTypeCavet", wareHouseCodeTableID.getWhDocTypeCavet());
			parameterList.put("whcStatusBorrowCavet", wareHouseCodeTableID.getWhcStatusBorrowCavet());

			if (!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow()) && seachCase.getStatusBorrow().equals(wareHouseCodeTableID.getWhStatusBorrowWait().toString())) {
				orderby = " ORDER BY whbd.created_date asc, whCode asc ";
			} else if (!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow()) && wareHouseCodeTableID.getWhStatusBorrowedAndRerected().contains(Integer.parseInt(seachCase.getStatusBorrow()))) {
				orderby = " ORDER BY whbd.APPROVE_DATE asc, whCode asc";
			} else {
				orderby = " order by whLodgeDate asc, whCode asc ";
			}
		}
		
		if (WHStep.RETURN_CAVET == _step) {
			extQuerry = extQuerry + " AND whd.doc_type =:whDocTypeCavet AND whd.status IN (:whcStatusReturnCavet) ";
			
			if (!StringUtils.isNullOrEmpty(seachCase.getStatusAppPayBack()) && seachCase.getStatusAppPayBack().equals("0")) {
				extQuerry = extQuerry + " AND whrd.approve_status is null";
			} else {
				extQuerry = extQuerry
						+ " AND (whrd.approve_status is null or whrd.approve_status IN (:whStatusNotBorrowCavet))";
				
				parameterList.put("whStatusNotBorrowCavet", wareHouseCodeTableID.getWhStatusNotBorrowCavet());
			}
			
			if (!StringUtils.isNullOrEmpty(seachCase.getStatusAppPayBack()) && !seachCase.getStatusAppPayBack().equals("0")) {
				extQuerry = extQuerry + " AND whrd.approve_status = :approveStatus";

				parameterList.put("approveStatus", seachCase.getStatusAppPayBack());
			}
			
			if (!StringUtils.isNullOrEmpty(seachCase.getAppPayBackDateFrom())) {
				extQuerry = extQuerry + " AND whrd.return_date >= TO_DATE(:returnDateFrom " + dateFormat;
				parameterList.put("returnDateFrom", seachCase.getAppPayBackDateFrom());
			}
			
			if (!StringUtils.isNullOrEmpty(seachCase.getAppPayBackDateTo())) {
				extQuerry = extQuerry + " AND whrd.return_date <= TO_DATE(:returnDateTo " + dateFormat;
				parameterList.put("returnDateTo", seachCase.getAppPayBackDateTo());
			}
			
			parameterList.put("whDocTypeCavet", wareHouseCodeTableID.getWhDocTypeCavet());
			parameterList.put("whcStatusReturnCavet", wareHouseCodeTableID.getWhcStatusReturnCavet());

			if (!StringUtils.isNullOrEmpty(seachCase.getStatusAppPayBack()) && seachCase.getStatusAppPayBack().equals(wareHouseCodeTableID.getWhStatusReturnWait().toString())) {
				orderby = " ORDER BY whrd.created_date asc, whd.id asc ";
			} else if (!StringUtils.isNullOrEmpty(seachCase.getStatusAppPayBack()) && wareHouseCodeTableID.getWhStatusReturnAndRerected().contains(Integer.parseInt(seachCase.getStatusAppPayBack()))) {
				orderby = " ORDER BY whrd.APPROVE_DATE asc, whCode asc";
			} else {
				orderby = " order by whLodgeDate asc, whCode asc ";
			}
		}

		if (WHStep.APPROVE == _step) {
			if (!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow())
					&& seachCase.getStatusBorrow().equals(wareHouseCodeTableID.getWhStatusBorrowWait().toString())
					|| (!StringUtils.isNullOrEmpty(seachCase.getStatusAppPayBack()) && seachCase.getStatusAppPayBack().equals(wareHouseCodeTableID.getWhStatusReturnWait().toString()))) {
				orderby = " ORDER BY whbd.created_date asc, whCode asc ";
			} else if (!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow()) && (wareHouseCodeTableID.getWhStatusBorrowedAndRerected().contains(Integer.parseInt(seachCase.getStatusBorrow()))
					|| (wareHouseCodeTableID.getWhStatusReturnAndRerected().contains(Integer.parseInt(seachCase.getStatusBorrow()))))) {
				orderby = " ORDER BY whbd.APPROVE_DATE asc, whCode asc";
			} else {
				orderby = " order by whLodgeDate asc, whCode asc ";
			}
		}
		
		if (!StringUtils.isNullOrEmpty(seachCase.getIdentityNum())){
			extQuerry = extQuerry + " AND  LOWER(TRIM(CI.identity_number)) = :identityNum ";
			parameterList.put("identityNum", seachCase.getIdentityNum().trim().toLowerCase());
		}

		if (!StringUtils.isNullOrEmpty(seachCase.getAppNum())){
			extQuerry = extQuerry + " AND CAB.bpm_app_number = :appNum ";
			parameterList.put("appNum", seachCase.getAppNum().trim());
		}

		if (!StringUtils.isNullOrEmpty(seachCase.getWorkFlow())){
			extQuerry = extQuerry + " AND cab.WORKFLOW  = :workFlow ";
			parameterList.put("workFlow", seachCase.getWorkFlow().trim());
		}

		if (!StringUtils.isNullOrEmpty(seachCase.getFrameNum())){
			if( WHStep.BORROW_CAVET != _step && WHStep.LODGE_CAVET != _step){
				extQuerry = extQuerry + " AND ( exists (select 1 from credit_app_commodities cac where cac.credit_app_id = car.id and frame_number is not null and LOWER(TRIM(frame_number)) = :frameNum1 "
					+ "union all SELECT 1 FROM wh_cavet_info wci WHERE wci.CHASSIS is not null AND wci.WH_DOC_ID = whd.id and LOWER(TRIM(wci.chassis)) = :frameNum2 ))" ;
			} else {
				extQuerry = extQuerry + " AND (LOWER(cac.frame_number) = :frameNum1 OR LOWER(TRIM(wcinfo.chassis)) = :frameNum2 )";
			}
			
			parameterList.put("frameNum1", seachCase.getFrameNum().trim().toLowerCase());
			parameterList.put("frameNum2", seachCase.getFrameNum().trim().toLowerCase());
		}
			
		if (!StringUtils.isNullOrEmpty(seachCase.getNumPlate())) {
			if( WHStep.BORROW_CAVET != _step && WHStep.LODGE_CAVET != _step){
				extQuerry = extQuerry + " AND ( exists (select 1 from credit_app_commodities cac where cac.credit_app_id = car.id and number_plate is not null and LOWER(TRIM(number_plate)) = :numPlate1 "
				+ "  union all SELECT 1 FROM wh_cavet_info wci WHERE wci.engine is not null AND wci.wh_doc_id = whd.id and LOWER(TRIM(wci.engine)) = :numPlate2 ))" ;
			} else {
				extQuerry = extQuerry + " AND (LOWER(TRIM(cac.number_plate)) = :numPlate1 OR LOWER(TRIM(wcinfo.engine)) = :numPlate2 )";
			}
			
			parameterList.put("numPlate1", seachCase.getNumPlate().trim().toLowerCase());
			parameterList.put("numPlate2", seachCase.getNumPlate().trim().toLowerCase());
		}

		if (!StringUtils.isNullOrEmpty(seachCase.getReceiveDateTo())){
			extQuerry = extQuerry + " AND whd.created_date < TO_DATE(:receiveDateTo " + dateFormat + dateAdd;
			parameterList.put("receiveDateTo", seachCase.getReceiveDateTo());
		}
		if (!StringUtils.isNullOrEmpty(seachCase.getReceiveDateFrom())){
			extQuerry = extQuerry + " AND whd.created_date >= TO_DATE( :receiveDateFrom " + dateFormat;
			parameterList.put("receiveDateFrom", seachCase.getReceiveDateFrom());
		}
		/* begin allocation */
		if (seachCase.getAssigneeId() != null && !seachCase.getAssigneeId().equals(0)){
			extQuerry = extQuerry + "  AND ad.assignee_id = :assigneeId ";
			parameterList.put("assigneeId", seachCase.getAssigneeId());
		}
		if (!StringUtils.isNullOrEmpty(seachCase.getImporter())){
			extQuerry = extQuerry + " AND whd.created_by = :importer ";
			parameterList.put("importer", seachCase.getImporter());
		}
		if (!StringUtils.isNullOrEmpty(seachCase.getAssignDateFrom())){
			extQuerry = extQuerry + " AND ad.last_updated_date >= TO_DATE(:assignDateFrom " + dateFormat;
			parameterList.put("assignDateFrom",seachCase.getAssignDateFrom());
		}
		
		if (!StringUtils.isNullOrEmpty(seachCase.getAssignDateTo())){
			extQuerry = extQuerry + " AND ad.last_updated_date < TO_DATE(:assignDateTo " + dateFormat + dateAdd;
			parameterList.put("assignDateTo",seachCase.getAssignDateTo());
		}

		if (seachCase.getStatusAllocate() != null && !seachCase.getStatusAllocate().equals(0)){
			extQuerry = extQuerry + "  AND ad.status = :statusAllocate ";
			parameterList.put("statusAllocate", seachCase.getStatusAllocate());
		}
		
		if (WHStep.ALLOCATION == _step && seachCase.getAssignType() != null ){			
			if( !seachCase.getAssignType().equals(0)) {			
				extQuerry = extQuerry + " AND ad.status = :assignType ";	
				parameterList.put("assignType", seachCase.getAssignType());
			}else if (seachCase.getAssignType().equals(0)) {
				extQuerry = extQuerry + " AND ad.status IS NULL ";
			}
		}
		/* end allocation */	
		if(!StringUtils.isNullOrEmpty(seachCase.getStorageStatus()) ){
			extQuerry = extQuerry + " AND whd.status IN ( :storageStatus ) ";
			parameterList.put("storageStatus",seachCase.getStorageStatus());
		}
		/* begin lodge or begin Borrow */
		if (!StringUtils.isNullOrEmpty(seachCase.getLodgeDateFrom())){
			extQuerry = extQuerry + " AND whd.wh_lodge_date >= TO_DATE(:lodgeDateFrom " + dateFormat;
			parameterList.put("lodgeDateFrom",seachCase.getLodgeDateFrom());
		}
		if (!StringUtils.isNullOrEmpty(seachCase.getLodgeDateTo())){
			extQuerry = extQuerry + " AND whd.wh_lodge_date < TO_DATE(:lodgeDateTo " + dateFormat + dateAdd;
			parameterList.put("lodgeDateTo",seachCase.getLodgeDateTo());
		}
		
		if (!StringUtils.isNullOrEmpty(seachCase.getProcessDateFrom())){
			extQuerry = extQuerry + " AND whd.process_date >= TO_DATE(:processDateFrom " + dateFormat;
			parameterList.put("processDateFrom",seachCase.getProcessDateFrom());
		}
		if (!StringUtils.isNullOrEmpty(seachCase.getProcessDateTo())){
			extQuerry = extQuerry + " AND whd.process_date < TO_DATE(:processDateTo " + dateFormat + dateAdd;
			parameterList.put("processDateTo",seachCase.getProcessDateTo());
		}
		/* end lodge or and lodge */
		/* begin Borrow */
		// Check if cavet send return of approve return will not show
		if (!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow()) && seachCase.getStatusBorrow().equals("0")) {
			if (WHStep.RETURN_CAVET == _step) {
				extQuerry = extQuerry + " AND whrd.approve_status is null";
			} else {
				extQuerry = extQuerry + " AND whbd.approve_status is null";
			}
		} 
		
		if (!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow()) && !seachCase.getStatusBorrow().equals("0")) {
			if (WHStep.RETURN_CAVET == _step) {
				extQuerry = extQuerry + " AND whrd.approve_status NOT IN (:whStatusNotBorrowCavet)"
						+ " AND whrd.approve_status = :approveStatus";

				parameterList.put("whStatusNotBorrowCavet", wareHouseCodeTableID.getWhStatusNotBorrowCavet());
				parameterList.put("approveStatus", seachCase.getStatusBorrow());
			} else {
				if (WHStep.APPROVE != _step && WHStep.BORROW_CAVET != _step) {
					extQuerry = extQuerry + " AND whbd.approve_status NOT IN (:whStatusNotBorrowCavet)"
							+ " AND whbd.approve_status = :approveStatus";

					parameterList.put("whStatusNotBorrowCavet", wareHouseCodeTableID.getWhStatusNotBorrowCavet());
					parameterList.put("approveStatus", seachCase.getStatusBorrow());
				}
				
				extQuerry = extQuerry + " AND whbd.approve_status = :approveStatus";

				parameterList.put("approveStatus", seachCase.getStatusBorrow());
			}
		}
		
		
		/*if(!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow()) && !("0").equals(seachCase.getStatusBorrow())){	
			extQuerry = extQuerry + " AND whbd.approve_status IN (:statusBorrow) ";
			parameterList.put("statusBorrow", seachCase.getStatusBorrow());
		}
		
		if(!StringUtils.isNullOrEmpty(seachCase.getStatusBorrow()) && ("0").equals(seachCase.getStatusBorrow()))			
			extQuerry = extQuerry + " AND whbd.approve_status IS NULL ";*/
		
		if (WHStep.BORROW_CAVET == _step || WHStep.BORROW_CONTRACT == _step){
			if (!StringUtils.isNullOrEmpty(seachCase.getBorrowDateFrom())){
				extQuerry = extQuerry + " AND whbd.borrowed_date >= TO_DATE(:borrowDateFrom " + dateFormat;
				parameterList.put("borrowDateFrom", seachCase.getBorrowDateFrom());
			}
			if (!StringUtils.isNullOrEmpty(seachCase.getBorrowDateTo())){
				extQuerry = extQuerry + " AND whbd.borrowed_date < TO_DATE(:borrowDateTo " + dateFormat + dateAdd;
				parameterList.put("borrowDateTo", seachCase.getBorrowDateTo());
			}
			
			extQuerry = extQuerry + " AND whd.IS_ORIGINAL = 1";
		}
		
		if (!StringUtils.isNullOrEmpty(seachCase.getBorrowApproveDateFrom())){
			extQuerry = extQuerry + " AND whbd.approve_date >= TO_DATE(:borrowApproveDateFrom " + dateFormat;
			parameterList.put("borrowApproveDateFrom", seachCase.getBorrowApproveDateFrom());
		}

		if (!StringUtils.isNullOrEmpty(seachCase.getBorrowApproveDateTo())){
			extQuerry = extQuerry + " AND whbd.approve_date < TO_DATE(:borrowApproveDateTo " + dateFormat + dateAdd;
			parameterList.put("borrowApproveDateTo", seachCase.getBorrowApproveDateTo());
		}
		
		if (!StringUtils.isNullOrEmpty(seachCase.getToApproveDateTo())){
			extQuerry = extQuerry + " AND whbd.created_date < TO_DATE(:toApproveDateTo " + dateFormat + dateAdd;
			parameterList.put("toApproveDateTo", seachCase.getToApproveDateTo());
		}
		
		if (!StringUtils.isNullOrEmpty(seachCase.getToApproveDateFrom())){
			extQuerry = extQuerry + " AND whbd.created_date >= TO_DATE(:toApproveDateForm " + dateFormat + dateAdd;
			parameterList.put("toApproveDateForm", seachCase.getToApproveDateFrom());
		}
		/* end Borrow */
		 /*begin Approve*/
		if (!StringUtils.isNullOrEmpty(seachCase.getAppDateFrom())){
			extQuerry = extQuerry + " AND whbd.APPROVE_DATE >= TO_DATE(:appDateFrom " + dateFormat + dateAdd;
			parameterList.put("appDateFrom", seachCase.getAppDateFrom());
		}
		
		if (!StringUtils.isNullOrEmpty(seachCase.getAppDateTo())){
			extQuerry = extQuerry + " AND whbd.APPROVE_DATE < TO_DATE(:appDateTo " + dateFormat + dateAdd;
			parameterList.put("appDateTo", seachCase.getAppDateTo());
		}
		/*end Approve*/
		/* for batchId */
		if (!StringUtils.isNullOrEmpty(batchId)){
			extQuerry = extQuerry + " AND whd.batch_id  = :batchId ";			
			parameterList.put("batchId", batchId);
		} 
		 System.out.println( " parameterList SeachCase ---> " + JSONConverter.toJSON(parameterList));
		if (count) {
			colums = " SELECT count( whd.id ) ";
			 query = this.session.createNativeQuery(colums + tables + extQuerry).setHibernateFlushMode(FlushMode.ALWAYS);
			 for (String key : parameterList.keySet()) {
			      query.setParameter(key, parameterList.get(key));
			   }
			return ((BigDecimal) query.getSingleResult()).intValue();
		}

		if (!count) {
			if (WHStep.THANK_LETTER == _step) {
				orderby = "";
			}
			
			query = this.session.createNativeQuery(colums + tables + extQuerry + orderby).setHibernateFlushMode(FlushMode.ALWAYS);
			
			for (String key : parameterList.keySet()) {
				query.setParameter(key, parameterList.get(key));
			}
			if (pageSize != null && !pageSize.equals(0) && pageNum != null && !pageNum.equals(0)) {
			query.setFirstResult((pageNum - 1) * pageSize).setMaxResults(pageSize);
			}
			
			lst = query.getResultList();
			List<WareHouseSeachObject> retList = new ArrayList<WareHouseSeachObject>();

			if (lst != null && !lst.isEmpty()) {
				for (Object o : lst) {
					retList.add(transformWareHouseSeach(o, wareHouseCodeTableID));
				}
			}

			return retList;
		}

		return null;
	}

	public int lodge(Long newStatus, Long documentId) {
		
		CriteriaBuilder builder = this.session.getCriteriaBuilder();
		CriteriaUpdate<WhDocument> whDocumentCriteria = builder.createCriteriaUpdate(WhDocument.class);
		Root<WhDocument> whDocumentRoot = whDocumentCriteria.from(WhDocument.class);
		whDocumentCriteria.set(whDocumentRoot.get("status"), newStatus);
		whDocumentCriteria.where(builder.equal(whDocumentRoot.get("id"), documentId));
		
		return this.session.createQuery(whDocumentCriteria).executeUpdate();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<RemainDocument> findRemainDocumentAllocation() {
		
		List<RemainDocument> results = new ArrayList<>();
		List lst = this.session.getNamedNativeQuery("findRemainDocumentAllocation").list();
		if (lst != null && !lst.isEmpty())
			results =  transformList(lst, RemainDocument.class);
		
		return results;
	}
	
	public String findLodgeCodeByDocId(Long documentId) {
		List<?> lst = this.session.getNamedNativeQuery("findLodgeCodeByDocId")
						.setParameter("documentId", documentId).list();
		if( lst!=null && lst.size()>0 )
			return lst.get(0).toString();
		return "";
	}
	
	public WhDocument findById(Long i) {
		return (WhDocument) this.session.getNamedQuery("findWhDocumentById").setParameter("id", i).getSingleResult();
	}
	
	/*public List<WhDocument> getListDocumentByCreditAppId(Long creditAppId, Long docType) {
		return this.session.createQuery("from WhDocument where creditAppId = :creditAppId and docType = :docType ", WhDocument.class)
					.setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
	}*/
	
	//version : 0 : k co version , 1 : version NULL , 2 : version NOTNULL
	public WhDocument getDocumentByCreditAppId(Long creditAppId, Long docType, Long version) {
		WhDocument item = null;
		List<?> lst = new ArrayList<>();

		if (version == null) {
			lst = this.session
					.createQuery(
							"from WhDocument where creditAppId = :creditAppId and docType = :docType and version is null",
							WhDocument.class)
					.setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
		} else {
			lst = this.session
					.createQuery(
							"from WhDocument where creditAppId = :creditAppId and docType = :docType and version is not null",
							WhDocument.class)
					.setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
		}

		if (lst != null && lst.size() > 0)
			item = (WhDocument) lst.get(0);

		return item;
	}
	
	public WhDocument getDocumentByCreditAppId1(Long creditAppId, Long docType, Long version) {
		WhDocument item = new WhDocument();
		List<?> lst = new ArrayList<>();
		
		if (version == null) {
			lst = this.session
					.createNativeQuery("SELECT a.ID,a.VERSION,a.CREDIT_APP_ID,a.DOC_TYPE,a.BATCH_ID,a.ORDER_BY,a.STATUS,a.ESTIMATE_DATE,a.WH_CODE_ID,a.WH_LODGE_BY,a.WH_LODGE_DATE,a.CONTRACT_CAVET_TYPE,a.CREATED_BY,a.CREATED_DATE,a.LAST_UPDATED_BY,a.LAST_UPDATED_DATE from wh_document a  Where a.credit_App_Id =:creditAppId and a.doc_Type =:docType and a.VERSION is null")
					.setHibernateFlushMode(FlushMode.ALWAYS).setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
		} else {
			lst = this.session
					.createNativeQuery("SELECT a.ID,a.VERSION,a.CREDIT_APP_ID,a.DOC_TYPE,a.BATCH_ID,a.ORDER_BY,a.STATUS,a.ESTIMATE_DATE,a.WH_CODE_ID,a.WH_LODGE_BY,a.WH_LODGE_DATE,a.CONTRACT_CAVET_TYPE,a.CREATED_BY,a.CREATED_DATE,a.LAST_UPDATED_BY,a.LAST_UPDATED_DATE from wh_document a  Where a.credit_App_Id =:creditAppId and a.doc_Type =:docType and a.VERSION is not null")
					.setHibernateFlushMode(FlushMode.ALWAYS).setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
		}

		if (lst != null && lst.size() > 0)
			item= transformObject(lst.get(0), WhDocument.class);

		return item;
	}

	public WhDocument getDocumentByCreditAppIdAndContractCavetType(Long creditAppId, Long docType, Long version,
			Long contractCavetType) {
		WhDocument item = null;
		List<?> lst = new ArrayList<>();

		if (contractCavetType == null) {
			if (version == null) {
				lst = this.session
						.createQuery(
								"from WhDocument where creditAppId = :creditAppId and docType = :docType and version is null",
								WhDocument.class)
						.setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
			} else {
				lst = this.session
						.createQuery(
								"from WhDocument where creditAppId = :creditAppId and docType = :docType and version is not null",
								WhDocument.class)
						.setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
			}
		} else {
			if (version == null) {
				lst = this.session
						.createQuery(
								"from WhDocument where creditAppId = :creditAppId and docType = :docType and contractCavetType = :contractCavetType and version is null",
								WhDocument.class)
						.setParameter("creditAppId", creditAppId).setParameter("docType", docType)
						.setParameter("contractCavetType", contractCavetType).list();
			} else {
				lst = this.session
						.createQuery(
								"from WhDocument where creditAppId = :creditAppId and docType = :docType and contractCavetType = :contractCavetType and version is not null",
								WhDocument.class)
						.setParameter("creditAppId", creditAppId).setParameter("docType", docType)
						.setParameter("contractCavetType", contractCavetType).list();
			}
		}

		if (lst != null && lst.size() > 0) {
			item = (WhDocument) lst.get(0);
		}

		return item;
	}
		
	/*public WhDocument getMaxVersionDocumentByCreditAppId(Long creditAppId, Long docType) {
		WhDocument item = null;
		List<?> lst = new ArrayList<>();
		lst = this.session.createQuery("from WhDocument where creditAppId = :creditAppId and docType = :docType "
				+ "and version = (select max(version) from WhDocument where creditAppId = :creditAppId and docType = :docType) ", WhDocument.class)
					.setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();
		if( lst!=null && !lst.isEmpty() )
			item = (WhDocument) lst.get(0);
		
		return item;
	}*/
//	SELECT COUNT(ID) FROM WH_DOCUMENT WHERE CREDIT_APP_ID = '447' AND DOC_TYPE = '1'
	public Long getVersionDocumentByAppId(Long creditAppId, Long docType) {
		Object out = this.session.getNamedNativeQuery("getVersionDocumentByAppId")
				.setParameter("CREDIT_APP_ID", creditAppId).setParameter("DOC_TYPE", docType).getSingleResult();
		if (out != null)
			return Long.valueOf(out.toString());
		return null;
	}

	public Long getCurrentOrderBy(Long creditAppId, Long docType) {
		Object out = this.session.getNamedNativeQuery("getCurrentOrderBy").setParameter("CREDIT_APP_ID", creditAppId)
				.setParameter("DOC_TYPE", docType).getSingleResult();
		if (out != null)
			return Long.valueOf(out.toString());
		return null;
	}

	public CheckRecordsCavetInfo getCheckRecordsCavet(Long whId, Integer upldetaiId) {
		CheckRecordsCavetInfo result = new CheckRecordsCavetInfo();
		try {
			Object obj = this.session.getNamedNativeQuery("getCheckRecordsCavet").setParameter("whId", whId).setParameter("upldetaiId", upldetaiId).getSingleResult();
			if (obj != null)
				result = transformObject(obj, CheckRecordsCavetInfo.class);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}

		return result;
	}

	public void merge(WhDocument whDocument) {
		this.session.merge(whDocument);
                this.session.flush();
                this.session.clear();
	}

	public String getCurrentBatchId(String loginId) {
		try {
			Object out = this.session.getNamedNativeQuery("getCurrentBatchId").setParameter("loginId", loginId).getSingleResult() ;
			if(out != null) {
				return String.valueOf(out);
			}
			return null;
		} catch(Exception ex) {
			return null;
		}
	}
	
    public List<ReturnDocumentInfo> getLstReturnDocument(Long whId) {
        List<ReturnDocumentInfo> result = new ArrayList<>();
        List<?> lst = session.getNamedNativeQuery("getLstReturnDocument").setParameter("whDocId", whId).list();
        if (lst != null && !lst.isEmpty()) {
            for (Object obj : lst) {
                result.add(transformObject(obj, ReturnDocumentInfo.class));
            }
        }

        return result;
    }
	

	@SuppressWarnings("unchecked")
    public List<WareHousePayBackCavet> getLstSearchPayBackCavet(WareHousePayBackCavetDTO backCavet) {
        List<WareHousePayBackCavet> results = new ArrayList<>();
        
        final int RETURN_CAVET = 1;
        final int APPROVE_BORROW_RETURN_CAVET = 2;
        
        try {
            if (backCavet != null) {
                StringBuilder strQuery = new StringBuilder();
                strQuery.append(" SELECT DISTINCT    whd.id as idWhDocument,       "
                        + "  car.mc_contract_number as contractNumber,   "
                        + "  ct_wh.code_value1 AS docTypeCode,  "
                        + "  ct_wh.description1 AS docTypeName,  "
                        + "  cpi.cust_name as custName,  "
                        // + "  decode(cab.WORKFLOW,'ConcentratingDataEntry','DE','InstallmentLoan','IL','CashLoan','CL',null) as flow,  "
                        + "  cab.WORKFLOW as flow,  "
                        + "  ct_car.code_value1 AS postCode,   "
                        + "  emp.full_name AS saleName,   "
                        + "  emp.mobile_phone AS saleMobile,   "
                        + "  emp.email as emailSale,  "
                        + "  empl_bsd.email as emailBDS,      "
                        + "  ct_comm.description1 as commodity,   "
                        + "  wci.brand as brand,   "
                        + "  wci.model_code as modelCode,  "
                        + "  wci.color as color,  "
                        + "  wci.chassis as chassis,  "
                        + "  wci.engine as engine,  "
                        + "  cab.bpm_app_number as caseNum,   "
                        + "  wci.n_plate as nPlate,  "
                        + "  wci.cavet_number as cavetNumber,  "
                        + "  ct_whd_status.description1 AS statusWhName,   "
                        + "  ct_whd_status.code_value1 AS statusWhCode,  "
                        + "  (SELECT cbswh.created_by "
                        + "         FROM (SELECT  a.created_date,a.created_by,RANK() OVER( "
                        + "                         PARTITION BY a.wh_doc_id "
                        + "                         ORDER BY "
                        + "                             a.created_date DESC "
                        + "                     ) dest_rank "
                        + "                 FROM "
                        + "                     wh_map_doc_code a "
                        + "                 WHERE "
                        + "                     a.wh_doc_id = whd.id) cbswh where cbswh.dest_rank = 1) as createdBySaveWH, "
                        + "  TO_CHAR((SELECT dswh.created_date "
                        + "         FROM (SELECT  a.created_date,RANK() OVER( "
                        + "                         PARTITION BY a.wh_doc_id "
                        + "                         ORDER BY "
                        + "                             a.created_date DESC "
                        + "                     ) dest_rank "
                        + "                 FROM "
                        + "                     wh_map_doc_code a "
                        + "                 WHERE "
                        + "                     a.wh_doc_id = whd.id) dswh where dswh.dest_rank = 1),'dd/mm/yyyy HH24:MI:SS') as createdDateSaveWH, userBr.FULL_NAME as borrowedUser , whc.code as whCode, ");
                if (backCavet.getTypeScreen() == RETURN_CAVET) {
                    strQuery.append("(select ct_app_status.description1 from code_table ct_app_status   "
                            + "   where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS') AS statusAppPayBack,  "
                            + "   TO_CHAR((select wdc.CREATED_DATE from  code_table ct_app_status   "
                            + "   where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS' and (ct_app_status.code_value1 = 'WH_OK_RETURN' or ct_app_status.code_value1 ='WH_REJECT_RETURN') ),'dd/mm/yyyy HH24:MI:SS') as appPayBackDate, "
                            + "   (select wdc.note from  code_table ct_app_status   "
                            + "   where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS' and ct_app_status.code_value1= 'WH_REJECT_RETURN' ) as rejectPayBack,  "
                            + "   TO_CHAR((select wdc.CREATED_DATE from code_table ct_app_status   "
                            + "   where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS' and ct_app_status.code_value1= 'WH_RETURN' ),'dd/mm/yyyy HH24:MI:SS') as payBackOrFwAppPayBackDate ");
                } else if (backCavet.getTypeScreen() == APPROVE_BORROW_RETURN_CAVET) {
                    strQuery.append("(select ct_app_status.description1 from code_table ct_app_status   "
                            + " where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and (ct_app_status.category='WH_RE_STATUS' or ct_app_status.category='WH_APR_STATUS')) AS statusAppPayBack, "
                            + " TO_CHAR((select wdc.CREATED_DATE from  code_table ct_app_status   "
                            + " where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ((ct_app_status.category='WH_RE_STATUS' and (ct_app_status.code_value1 = 'WH_OK_RETURN' or ct_app_status.code_value1 ='WH_REJECT_RETURN')) or (ct_app_status.category='WH_APR_STATUS' and (ct_app_status.code_value1 = 'WH_OK' or ct_app_status.code_value1 ='WH_REJECT')) )),'dd/mm/yyyy HH24:MI:SS') as appPayBackDate,  "
                            + " (select wdc.note from  code_table ct_app_status   "
                            + " where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ((ct_app_status.category='WH_RE_STATUS' and ct_app_status.code_value1= 'WH_REJECT_RETURN') or (ct_app_status.category='WH_APR_STATUS' and ct_app_status.code_value1= 'WH_REJECT'))) as rejectPayBack,"
                            + " TO_CHAR((select pbd.created_date from (SELECT a.created_date, RANK() OVER(PARTITION BY a.wh_doc_id ORDER BY a.id DESC) dest_rank "
                            + " FROM wh_document_change a, code_table b where a.wh_doc_id= whd.id and b.id = a.id_code_table and ((b.category = 'WH_RE_STATUS' "
                            + " AND b.code_value1 = 'WH_WAIT_RETURN') OR (b.category = 'WH_APR_STATUS' AND b.code_value1 = 'WH_WAIT'))) pbd where pbd.dest_rank=1 ),'dd/mm/yyyy HH24:MI:SS') AS payBackOrFwAppPayBackDate  ");
                }

                strQuery.append("  FROM cust_personal_info cpi  "
                        + "  LEFT JOIN credit_app_request car ON cpi.id = car.cust_id  "
                        + "  LEFT JOIN credit_app_bpm cab ON car.id = cab.credit_app_id  "
                        + "  LEFT JOIN credit_app_commodities cac ON car.id = cac.credit_app_id  "
                        + "  LEFT JOIN code_table ct_car ON car.trans_office_id = ct_car.id  "
                        + "  LEFT JOIN code_table ct_comm ON cac.comm_id = ct_comm.id  "
                        + "  LEFT JOIN employees emp ON car.sale_id = emp.id  "
                        + "  LEFT JOIN employee_link empl ON emp.id = empl.emp_id  "
                        + "  LEFT JOIN employees empl_bsd ON ( empl.manager_id = empl_bsd.id and empl.status = 'A') "
                        + "  JOIN wh_document whd ON car.id = whd.credit_app_id  "
                        + "  LEFT JOIN code_table ct_wh ON whd.doc_type = ct_wh.id  "
                        + "  LEFT JOIN code_table ct_whd_status ON whd.status = ct_whd_status.id  "
                        + "  LEFT JOIN wh_code whc ON whd.wh_code_id = whc.id  "
                        + "  LEFT JOIN WH_CAVET_INFO wci ON wci.WH_DOC_ID = whd.id  "
                        + " JOIN code_table cti ON  cti.id  = wci.type and cti.category = 'WH_CAVET_TYPE' AND cti.code_value1 = 'WH_IN_CAVET' "
                        + "  LEFT JOIN ( SELECT  ID  ,WH_DOC_ID ,ID_CODE_TABLE,CREATED_DATE,note, "
                        + "          RANK() OVER( "
                        + "              PARTITION BY WH_DOC_ID "
                        + "              ORDER BY "
                        + "   ID DESC "
                        + "          ) dest_rank "
                        + "      FROM "
                        + "          wh_document_change  "
                        + "      ) wdc ON wdc.WH_DOC_ID = whd.id  and  wdc.dest_rank=1 "
                        + "   LEFT JOIN (select ep.full_name, wd.id "
                        + "      from wh_document wd "
                        + "      JOIN code_table ct_status ON wd.status = ct_status.id and ct_status.code_group='WH' and ct_status.category='WH_LOGDE' and ct_status.code_value1 in ('WH_LODGED_ERR_UPDATE_BORROW','WH_LODGE_COMPLETE_BORROW') "
                        + "      JOIN ( SELECT  ID  ,WH_DOC_ID ,OBJECT_TO, "
                        + "          RANK() OVER( "
                        + "              PARTITION BY WH_DOC_ID "
                        + "              ORDER BY "
                        + "   ID DESC "
                        + "          ) dest_rank "
                        + "      FROM "
                        + "          WH_BORROWED_RETURN_DOCUMENT "
                        + "      ) wbd ON wbd.WH_DOC_ID = wd.id "
                        + "      LEFT JOIN employees ep ON ep.id = wbd.OBJECT_TO "
                        + "      where wbd.dest_rank=1) userBr ON whd.id = userBr.id        "
                        + "      where ct_wh.code_value1 in ('WH_CAVET','WH_THANKS_LETTER') "
                        + " and ct_whd_status.code_value1 in ('WH_LODGED_COMPLETE','WH_LODGED_ERR_UPDATE','WH_LODGE_COMPLETE_BORROW','WH_LODGED_ERR_UPDATE_BORROW') "
                        + " and cab.workflow in ('ConcentratingDataEntry','InstallmentLoan','CashLoan') "
                        + "  AND whd.doc_type IN ( SELECT ID FROM code_table WHERE code_group = 'WH' AND category = 'WH_DOC_TYPE' AND code_value1 IN ('WH_CAVET')) "
                );
                
                String[] strStatusApp = null;
                List<String> lstParamStatus = new ArrayList<String>();
                if (backCavet.getTypeScreen() == RETURN_CAVET) {
                	// Check if cavet is wait approve or approve borrow not show
                	strQuery.append("AND (SELECT  id_code_table  FROM  wh_document_change   " +
                                        "    WHERE  wh_doc_id = whd.id " +
                                        "    ORDER BY " +
                                        "    created_date DESC " +
                                        "    FETCH FIRST 1 ROW ONLY " +
                                        "    ) NOT IN ( SELECT  id FROM code_table WHERE  code_group = 'WH' AND category = 'WH_APR_STATUS' AND ( code_value1 = 'WH_OK'  OR code_value1 = 'WH_WAIT' ))");
                    
                    if (!StringUtils.isNullOrEmpty(backCavet.getStatusAppPayBack())) {
                        if (backCavet.getStatusAppPayBack().trim().equalsIgnoreCase("0")) {
                            strQuery.append(" AND (select ct_app_status.code_value1 from code_table ct_app_status  "
                                    + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS') is null ");
                        } else {
                            strQuery.append(" AND UPPER((select ct_app_status.code_value1 from code_table ct_app_status  "
                                    + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS')) = :statusAppPayBack ");
                        }
                    }

                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateFrom())) {
                        strQuery.append(" AND (TRUNC((select wdc.CREATED_DATE from  code_table ct_app_status "
                                + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS' and (ct_app_status.code_value1 = 'WH_OK_RETURN' or ct_app_status.code_value1 ='WH_REJECT_RETURN'))) >= to_date(:appPayBackDateFrom,'dd/mm/yyyy') ) ");
                    }
                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateTo())) {
                        strQuery.append(" AND (TRUNC((select wdc.CREATED_DATE from  code_table ct_app_status "
                                + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ct_app_status.category='WH_RE_STATUS' and (ct_app_status.code_value1 = 'WH_OK_RETURN' or ct_app_status.code_value1 ='WH_REJECT_RETURN'))) <= to_date(:appPayBackDateTo,'dd/mm/yyyy') ) ");
                    }
                } else if (backCavet.getTypeScreen() == APPROVE_BORROW_RETURN_CAVET) {
                    if (!StringUtils.isNullOrEmpty(backCavet.getStatusAppPayBack())) {
                        if (backCavet.getStatusAppPayBack().trim().equalsIgnoreCase("0")) {
                            strQuery.append(" AND (select ct_app_status.code_value1 from code_table ct_app_status  "
                                    + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and (ct_app_status.category='WH_RE_STATUS' or ct_app_status.category='WH_APR_STATUS')) is null ");
                        } else {
                            strStatusApp = backCavet.getStatusAppPayBack().trim().split(",");
                            strQuery.append(" AND UPPER((select ct_app_status.code_value1 from code_table ct_app_status  "
                                    + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and (ct_app_status.category='WH_RE_STATUS' or ct_app_status.category='WH_APR_STATUS'))) IN (:statusAppPayBack) ");
                            for (int i = 0; i < strStatusApp.length; i++) {
                                lstParamStatus.add(strStatusApp[i].trim());
                            }
                        }
                    }

                    if (!StringUtils.isNullOrEmpty(backCavet.getFwAppPayBackDateFrom())) {
                        strQuery.append(" AND (TRUNC(( "
                                + "        SELECT "
                                + "            pbd.created_date "
                                + "        FROM "
                                + "            ( "
                                 + " SELECT "
                                 + "     a.created_date,RANK() OVER( "
                                 + "         PARTITION BY a.wh_doc_id "
                                 + "         ORDER BY "
                                 + "             a.id DESC "
                                 + "     ) dest_rank "
                                 + " FROM "
                                 + "     wh_document_change a,code_table b "
                                 + " WHERE "
                                 + "     a.wh_doc_id = whd.id "
                                 + "     AND b.id = a.id_code_table "
                                 + "     AND( (b.category = 'WH_RE_STATUS' "
                                 + "             AND b.code_value1 = 'WH_WAIT_RETURN') "
                                 + "           OR(b.category = 'WH_APR_STATUS' "
                                 + "                AND b.code_value1 = 'WH_WAIT') ) "
                                + "            ) pbd "
                                + "        WHERE "
                                + "            pbd.dest_rank = 1 "
                                + "    )) >=  to_date(:fwAppPayBackDateFrom,'dd/mm/yyyy') ) ");
                    }
                    
                    if (!StringUtils.isNullOrEmpty(backCavet.getFwAppPayBackDateTo())) {
                        strQuery.append(" AND (TRUNC(( "
                                + "        SELECT "
                                + "            pbd.created_date "
                                + "        FROM "
                                + "            ( "
                                 + " SELECT "
                                 + "     a.created_date,RANK() OVER( "
                                 + "         PARTITION BY a.wh_doc_id "
                                 + "         ORDER BY "
                                 + "             a.id DESC "
                                 + "     ) dest_rank "
                                 + " FROM "
                                 + "     wh_document_change a,code_table b "
                                 + " WHERE "
                                 + "     a.wh_doc_id = whd.id "
                                 + "     AND b.id = a.id_code_table "
                                 + "     AND( (b.category = 'WH_RE_STATUS' "
                                 + "             AND b.code_value1 = 'WH_WAIT_RETURN') "
                                 + "           OR(b.category = 'WH_APR_STATUS' "
                                 + "                AND b.code_value1 = 'WH_WAIT') ) "
                                + "            ) pbd "
                                + "        WHERE "
                                + "            pbd.dest_rank = 1 "
                                + "    )) <= to_date(:fwAppPayBackDateTo,'dd/mm/yyyy') ) ");
                    }

                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateFrom())) {
                        strQuery.append(" AND (TRUNC((select wdc.CREATED_DATE from  code_table ct_app_status  "
                                + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ((ct_app_status.category='WH_RE_STATUS' and (ct_app_status.code_value1 = 'WH_OK_RETURN' or ct_app_status.code_value1 ='WH_REJECT_RETURN')) or (ct_app_status.category='WH_APR_STATUS' and (ct_app_status.code_value1 = 'WH_OK' or ct_app_status.code_value1 ='WH_REJECT'))))) >= to_date(:appPayBackDateFrom,'dd/mm/yyyy') ) ");
                    }
                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateTo())) {
                        strQuery.append(" AND (TRUNC((select wdc.CREATED_DATE from  code_table ct_app_status  "
                                + "  where ct_app_status.id = wdc.id_code_table and ct_app_status.code_group='WH' and ((ct_app_status.category='WH_RE_STATUS' and (ct_app_status.code_value1 = 'WH_OK_RETURN' or ct_app_status.code_value1 ='WH_REJECT_RETURN')) or (ct_app_status.category='WH_APR_STATUS' and (ct_app_status.code_value1 = 'WH_OK' or ct_app_status.code_value1 ='WH_REJECT'))))) <= to_date(:appPayBackDateTo,'dd/mm/yyyy') ) ");
                    }
                }

                if (!StringUtils.isNullOrEmpty(backCavet.getChassis())) {
                    strQuery.append(" and UPPER(wci.chassis) = :chassis  ");
                }
                
                if (!StringUtils.isNullOrEmpty(backCavet.getEngine())) {
                    strQuery.append(" and UPPER(wci.engine) = :engine  ");
                }
                
                if (backCavet.getCaseNum() != null && !backCavet.getCaseNum().equals(0)) {
                    strQuery.append(" and UPPER(cab.bpm_app_number) = :caseNum  ");
                }

                if (!StringUtils.isNullOrEmpty(backCavet.getStatusWhCode())) {
                    strQuery.append(" and UPPER(ct_whd_status.code_value1) = :statusWhCode  ");
                }
                if (!StringUtils.isNullOrEmpty(backCavet.getFlow())) {
                    strQuery.append(" and UPPER(decode(cab.WORKFLOW,'ConcentratingDataEntry','WH_DE','InstallmentLoan','WH_IL','CashLoan','WH_CL',null)) = :flow  ");
                }
                String[] strContractNumber = null;
                List<String> lstParam = new ArrayList<String>();
                if (backCavet.getContractNumber() != null && !backCavet.getContractNumber().trim().equals("")) {
                    strContractNumber = backCavet.getContractNumber().trim().split(",");
                    strQuery.append(" and car.mc_contract_number in (:lstContract) ");
                    for (int i = 0; i < strContractNumber.length; i++) {
                        lstParam.add(strContractNumber[i].trim());
                    }
                }
                if (backCavet.getTypeScreen() == RETURN_CAVET) {
                    strQuery.append(" order by appPayBackDate ");
                } else if (backCavet.getTypeScreen() == APPROVE_BORROW_RETURN_CAVET) {
                    strQuery.append(" AND (SELECT ct_app_status.description1 FROM code_table ct_app_status "
                            + "  WHERE ct_app_status.id = wdc.id_code_table "
                            + "   AND ct_app_status.code_group = 'WH' "
                            + "  AND ( ct_app_status.category = 'WH_RE_STATUS' "
                            + "    OR ct_app_status.category = 'WH_APR_STATUS' )) is not null order by payBackOrFwAppPayBackDate  ");
                }

                NativeQuery query = this.session.createNativeQuery(strQuery.toString());

                
                if (backCavet.getTypeScreen() == RETURN_CAVET) {
                    if (!StringUtils.isNullOrEmpty(backCavet.getStatusAppPayBack())) {
                        if (!backCavet.getStatusAppPayBack().trim().equalsIgnoreCase("0")) {
                            query.setParameter("statusAppPayBack", backCavet.getStatusAppPayBack().trim().toUpperCase());
                        }
                    }
                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateFrom())) {
                        query.setParameter("appPayBackDateFrom", backCavet.getAppPayBackDateFrom());
                    }

                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateTo())) {
                        query.setParameter("appPayBackDateTo", backCavet.getAppPayBackDateTo());
                    }

                } else if (backCavet.getTypeScreen() == APPROVE_BORROW_RETURN_CAVET) {
                    if (!StringUtils.isNullOrEmpty(backCavet.getStatusAppPayBack())) {
                        if (!backCavet.getStatusAppPayBack().trim().equalsIgnoreCase("0")) {
                            query.setParameterList("statusAppPayBack", lstParamStatus);
                        }
                    }
                    
                    if (!StringUtils.isNullOrEmpty(backCavet.getFwAppPayBackDateFrom())) {
                        query.setParameter("fwAppPayBackDateFrom", backCavet.getFwAppPayBackDateFrom());
                    }

                    if (!StringUtils.isNullOrEmpty(backCavet.getFwAppPayBackDateTo())) {
                        query.setParameter("fwAppPayBackDateTo", backCavet.getFwAppPayBackDateTo());
                    }

                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateFrom())) {
                        query.setParameter("appPayBackDateFrom", backCavet.getAppPayBackDateFrom());
                    }

                    if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateTo())) {
                        query.setParameter("appPayBackDateTo", backCavet.getAppPayBackDateTo());
                    }

                }

                if (!StringUtils.isNullOrEmpty(backCavet.getChassis())) {
                    query.setParameter("chassis", backCavet.getChassis().trim().toUpperCase());
                }
                if (!StringUtils.isNullOrEmpty(backCavet.getEngine())) {
                    query.setParameter("engine", backCavet.getEngine().trim().toUpperCase());
                }
                
                if (backCavet.getCaseNum() != null && !backCavet.getCaseNum().equals(0)) {
                	query.setParameter("caseNum", backCavet.getCaseNum());
                }
                
                if (!StringUtils.isNullOrEmpty(backCavet.getStatusWhCode())) {
                    query.setParameter("statusWhCode", backCavet.getStatusWhCode().trim().toUpperCase());
                }
                if (!StringUtils.isNullOrEmpty(backCavet.getFlow())) {
                    query.setParameter("flow", backCavet.getFlow().trim().toUpperCase());
                }
                if (!StringUtils.isNullOrEmpty(backCavet.getContractNumber())) {
                    query.setParameterList("lstContract", lstParam);
                }

                List lst = query.list();
                if (lst != null && lst.size() > 0) {
                    results = transformList(lst, WareHousePayBackCavet.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<WareHousePayBackLetter> getLstSearchPayBackLetter(WareHousePayBackLetter letter) {

        List<WareHousePayBackLetter> results = new ArrayList<>();
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT DISTINCT car.mc_contract_number as contractNumber,  "
        		 + "     cab.bpm_app_number as caseNum,   "
                 + "     TO_CHAR(car.contract_date,'dd/mm/yyyy') as contractDate,  "
                 + "     TO_CHAR(cal.actual_closed_date,'dd/mm/yyyy') as actualClosedDate,  "
                 + "     cpi.cust_name as custName,  "
                 + "     ci.identity_number AS identityNumber,  "
                 + "     TO_CHAR(ci.identity_issue_date,'dd/mm/yyyy') AS identityIssueDate,  "
                 + "     DECODE(ci.identity_issue_place, null, ci.identity_issue_place_text, (select a.DESCRIPTION1 from  code_table a where a.id = ci.identity_issue_place)) AS identityissueplace,"
                 + "     addr.ADDRESS as address,   "
                 + "     cci.CONTACT_VALUE as contactValue  "
                 + "  FROM  credit_app_request car "
                 + "     JOIN cust_personal_info cpi ON cpi.id = car.cust_id  "
                 + "     INNER JOIN CODE_TABLE ct ON (car.STATUS = ct.ID and ct.CODE_VALUE1 not like '%ABORT')  "
                 + "     LEFT JOIN credit_app_lms cal ON car.id = cal.credit_app_id  "
                 + "  	 LEFT JOIN credit_app_bpm cab ON car.id = cab.credit_app_id  "
                + "    LEFT JOIN ( "
                + "        SELECT id, "
                + "            cust_id, "
                + "            identity_number, "
                + "            identity_issue_date, "
                + "            identity_issue_place_text,"
                + "            identity_issue_place "
                + "        FROM "
                + "            cust_identity        "
                + "    ) ci ON ci.id = cpi.identity_id "
                 + "     JOIN (SELECT CAIP.CUST_ID "
                 + "      ,(CAIT.ADDRESS|| DECODE(ctw2.DESCRIPTION1, null, '',', ' || ctw2.DESCRIPTION1) || DECODE(ctd2.DESCRIPTION1, null, '',', ' || ctd2.DESCRIPTION1)|| DECODE(ctp2.DESCRIPTION1, null, '',', ' || ctp2.DESCRIPTION1))  as ADDRESS "
                 + "  FROM CUST_ADDR_INFO CAIP "
                 + "     LEFT JOIN CUST_ADDR_INFO CAIT ON CAIP.CUST_ID = CAIT.CUST_ID "
                 + "     LEFT JOIN CODE_TABLE ctw2 ON CAIT.WARD = ctw2.ID "
                 + "     LEFT JOIN CODE_TABLE ctd2 ON CAIT.DISTRICT = ctd2.ID "
                 + "     LEFT JOIN CODE_TABLE ctp2 ON CAIT.PROVINCE = ctp2.ID WHERE  CAIT.ADDR_TYPE = (SELECT ID FROM CODE_TABLE WHERE CODE_VALUE1 = 'PERMANENT' and CATEGORY = 'ADDR_TYPE' )) addr "
                 + "     ON addr.CUST_ID = cpi.ID "
                 + "     JOIN cust_contact_info cci ON ( cpi.id = cci.cust_id  "
                 + "      AND cci.CONTACT_PRIORITY=1  AND cci.contact_type = (  "
                 + "         SELECT  "
                 + "             id  "
                 + "         FROM  "
                 + "             code_table  "
                 + "         WHERE  "
                 + "             code_group = 'CONT'  "
                 + "             AND category = 'CONTAC_TYP'  "
                 + " AND code_value1 = 'MOBILE'  "
                 + "     ) )  "
                 + "  WHERE car.mc_contract_number is not null ");

        if (letter.getIdentityNumber() != null && !letter.getIdentityNumber().trim().equals("")) {
            strQuery.append(" AND ci.identity_number = :identityNumber");
        }

        if (letter.getCaseNum() != null && !letter.getCaseNum().equals(0)) {
            strQuery.append(" AND cab.bpm_app_number = :caseNum");
        }
        
        String[] strContractNumber = null;
        List<String> lstParam = new ArrayList();
        if (letter.getContractNumber() != null && !letter.getContractNumber().trim().equals("")) {
            strContractNumber = letter.getContractNumber().trim().split(",");
            strQuery.append(" and car.mc_contract_number in (:lstContract) ");
            for (int i = 0; i < strContractNumber.length; i++) {
                lstParam.add(strContractNumber[i].trim());
            }
        }

        NativeQuery query = this.session.createNativeQuery(strQuery.toString());
        if (letter.getIdentityNumber() != null && !letter.getIdentityNumber().trim().equals("")) {
            query.setParameter("identityNumber", letter.getIdentityNumber().trim().toUpperCase());
        }
        
        if (letter.getCaseNum() != null && !letter.getCaseNum().equals(0)) {
            query.setParameter("caseNum", letter.getCaseNum());
        }
        
        if (letter.getContractNumber() != null && !letter.getContractNumber().trim().equals("")) {
            query.setParameterList("lstContract", lstParam);
        }

        List lst = query.list();
        if (lst != null && lst.size() > 0) {
            results = transformList(lst, WareHousePayBackLetter.class);
        }
        return results;
    }

    public List<WareHousePaperReceipt> getLstPaperReceipt(String contractNumber) {

        List<WareHousePaperReceipt> results = new ArrayList<>();
        if (contractNumber != null && !contractNumber.trim().equals("")) {
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(" SELECT DISTINCT          car.mc_contract_number    AS contractnumber,  "
                    + " cab.bpm_app_number        AS bpmappnumber,  "
                    + " cpi.cust_name             AS custname,  "
                    + " ci.identity_number        AS identitynumber,  "
                    + " TO_CHAR(ci.identity_issue_date,'dd/mm/yyyy') AS identityissuedate,  "
                    + " DECODE(ci.identity_issue_place, null, ci.identity_issue_place_text, (select a.DESCRIPTION1 from  code_table a where a.id = ci.identity_issue_place)) AS identityissueplace,  "
                    + " (cai.address || DECODE(war.description1, null, '',', ' || war.description1)|| DECODE(dis.description1, null, '',', ' || dis.description1) || DECODE(pr.description1, null, '',', ' || pr.description1))  AS address,  "
                    + " cci.contact_value         AS contactvalue,  "
                    + " wci.brand                 AS brand,  "
                    + " wci.model_code            AS modelcode,  "
                    + " wci.color                 AS color,  "
                    + " wci.chassis               AS chassis,  "
                    + " wci.engine                AS engine,  "
                    + " wci.n_plate               AS nplate,  "
                    + " wci.cavet_number          AS cavetnumber,  "
                    + " TO_CHAR(cal.MAT_DATE + 14,'dd/mm/yyyy') AS actualcloseddate,  "
                    + " ct9.description1    AS brandbpm,  "
                    + " ct6.description1 AS modelcodebpm,  "
                    + " cac.color                 AS colorbpm,  "
                    + " cac.frame_number          AS chassisbpm,  "
                    + " cac.number_plate         AS enginebpm,  "
                    + " null          AS nplatebpm,  "
                    + " null            AS cavetnumberbpm,"
                    + " whd.credit_app_id As creditAppId "
                    + "  FROM cust_personal_info cpi  "
                    + " LEFT JOIN credit_app_request car ON cpi.id = car.cust_id  "
                    + " LEFT JOIN credit_app_bpm cab ON car.id = cab.credit_app_id  "
                    + " LEFT JOIN credit_app_lms cal ON car.id = cal.credit_app_id  "
                    + "   LEFT JOIN ( "
                    + "        SELECT id, "
                    + "            cust_id, "
                    + "            identity_number, "
                    + "            identity_issue_date, "
                    + "            identity_issue_place_text,"
                    + "            identity_issue_place "
                    + "        FROM "
                    + "            cust_identity        "
                    + "    ) ci ON ci.id = cpi.identity_id  "
                    + " JOIN cust_addr_info cai ON ( cpi.id = cai.cust_id  "
                    + "    AND cai.addr_type = ( SELECT id FROM code_table  "
                    + "     WHERE  category = 'ADDR_TYPE'  "
                    + "         AND code_value1 = 'PERMANENT' ) )  "
                    + " JOIN cust_contact_info cci ON ( cpi.id = cci.cust_id  "
                    + " AND cci.contact_priority = 1  AND cci.contact_type = (  "
                    + "     SELECT id FROM code_table WHERE category = 'CONTAC_TYP'  "
                    + "         AND code_value1 = 'MOBILE')"
                    + " AND cci.contact_category  = (SELECT id FROM code_table WHERE  "
                    + "              category = 'CONTAC_CAT' "
                    + "             AND code_value1 = 'CUSTOMER') ) "
                    + " JOIN wh_document whd ON car.id = whd.credit_app_id  "
                    + " LEFT JOIN (select a.* from wh_cavet_info a, code_table b where b.id  = a.type and b.category = 'WH_CAVET_TYPE' AND b.code_value1 = 'WH_IN_APPENDIX') wci ON wci.wh_doc_id = whd.id   "
                    + " LEFT JOIN credit_app_commodities cac ON car.id = cac.credit_app_id  "
                    + " JOIN code_table ct ON ct.id = whd.doc_type  "
                    + "   AND ct.category = 'WH_DOC_TYPE'  "
                    + "  AND ct.code_value1 = 'WH_CAVET'  "
                    + " LEFT JOIN CODE_TABLE ct6 ON cac.model_id = ct6.id  "
                    + " LEFT JOIN CODE_TABLE ct9 ON cac.brand_id = ct9.id  "
                    + " LEFT JOIN CODE_TABLE pr ON pr.id = cai.PROVINCE  "
                    + " LEFT JOIN CODE_TABLE dis ON dis.id = cai.DISTRICT "
                    + " LEFT JOIN CODE_TABLE war ON war.id = cai.WARD "
                    + " WHERE car.mc_contract_number is not null ");

            String[] strContractNumber = null;
            List<String> lstParam = new ArrayList<String>();

            strContractNumber = contractNumber.trim().split(",");
            strQuery.append(" and car.mc_contract_number in (:lstContract) ");
            for (int i = 0; i < strContractNumber.length; i++) {
                lstParam.add(strContractNumber[i].trim());
            }

            NativeQuery query = this.session.createNativeQuery(strQuery.toString());
            query.setParameterList("lstContract", lstParam);
            List lst = query.list();
            if (lst != null && lst.size() > 0) {
                results = transformList(lst, WareHousePaperReceipt.class);
            }
        }
        return results;
    }

    public List<WareHouseThankLetter> getLstThankLetter(String contractNumber, int typeExport) {

        List<WareHouseThankLetter> results = new ArrayList<>();
        if (contractNumber != null && !contractNumber.trim().equals("")) {
            StringBuilder strQuery = new StringBuilder();

            strQuery.append(" SELECT DISTINCT "
                    + "    car.id                    AS creditAppId,"
                    + "    car.mc_contract_number    AS contractNumber, "
                    + "    cpi.cust_name             AS custName, "
                    + "    ci.identity_number AS identityNumber,  "
                    + "    TO_CHAR(ci.identity_issue_date,'dd/mm/yyyy') AS identityIssueDate,  "
                    + "    DECODE(ci.identity_issue_place, null, ci.identity_issue_place_text, (select a.DESCRIPTION1 from  code_table a where a.id = ci.identity_issue_place)) AS identityissueplace,"
                    + "    addr.address               AS address, "
                    + "    cci.contact_value         AS contactValue, "
                    + "    TO_CHAR(car.contract_date,'dd/mm/yyyy') as actualOpenedDate, "
                    + "    TO_CHAR(cal.actual_closed_date,'dd/mm/yyyy') as actualClosedDate,");
                    if (typeExport == 2 || typeExport == 3){
                        strQuery.append(" wci.n_plate  AS nplate  ");
                    } else {
                        strQuery.append(" null  AS nplate  ");
                    }
                    
                   strQuery.append(" FROM cust_personal_info cpi "
                    + "    LEFT JOIN credit_app_request car ON cpi.id = car.cust_id "
                    + "    LEFT JOIN credit_app_lms cal ON car.id = cal.credit_app_id "
                    + "   LEFT JOIN ( "
                    + "        SELECT id, "
                    + "            cust_id, "
                    + "            identity_number, "
                    + "            identity_issue_date, "
                    + "            identity_issue_place_text,"
                    + " identity_issue_place "
                    + "        FROM "
                    + "            cust_identity        "
                    + "    ) ci ON ci.id = cpi.identity_id  "
                    + "    JOIN (SELECT CAIP.CUST_ID"
                    + "     ,(CAIT.ADDRESS|| DECODE(ctw2.DESCRIPTION1, null, '',', ' || ctw2.DESCRIPTION1) || DECODE(ctd2.DESCRIPTION1, null, '',', ' || ctd2.DESCRIPTION1)|| DECODE(ctp2.DESCRIPTION1, null, '',', ' || ctp2.DESCRIPTION1))  as ADDRESS"
                    + " FROM CUST_ADDR_INFO CAIP"
                    + "    LEFT JOIN CUST_ADDR_INFO CAIT ON CAIP.CUST_ID = CAIT.CUST_ID"
                    + "    LEFT JOIN CODE_TABLE ctw2 ON CAIT.WARD = ctw2.ID"
                    + "    LEFT JOIN CODE_TABLE ctd2 ON CAIT.DISTRICT = ctd2.ID"
                    + "    LEFT JOIN CODE_TABLE ctp2 ON CAIT.PROVINCE = ctp2.ID WHERE  CAIT.ADDR_TYPE = (SELECT ID FROM CODE_TABLE WHERE CODE_VALUE1 = 'PERMANENT' and CATEGORY = 'ADDR_TYPE' )) addr"
                    + "    ON addr.CUST_ID = cpi.ID"
                    + "    JOIN cust_contact_info cci ON ( cpi.id = cci.cust_id "
                    + "     AND cci.CONTACT_PRIORITY=1  AND cci.contact_type = ( "
                    + "        SELECT "
                    + "            id "
                    + "        FROM "
                    + "            code_table "
                    + "        WHERE category = 'CONTAC_TYP' "
                    + " AND code_value1 = 'MOBILE' "
                    + "    ) AND cci.contact_category  = (SELECT id FROM code_table WHERE category = 'CONTAC_CAT' AND code_value1 = 'CUSTOMER') ) ");
                    if (typeExport == 2 || typeExport == 3){
                        strQuery.append(" LEFT JOIN wh_document whd ON car.id = whd.credit_app_id "
                                + " LEFT JOIN (select a.* from wh_cavet_info a, code_table b where b.id  = a.type and b.category = 'WH_CAVET_TYPE' AND b.code_value1 = 'WH_IN_CAVET') wci ON wci.wh_doc_id = whd.id "
                                + " JOIN code_table ct ON ct.id = whd.doc_type  AND ct.category = 'WH_DOC_TYPE'  AND ct.code_value1 = 'WH_CAVET' ");
                    }
                    
                    strQuery.append( " WHERE car.mc_contract_number  is not null ");
                    if (typeExport == 2 || typeExport == 3){
                         strQuery.append( " and whd.IS_ORIGINAL = 1 ");
                    }

            String[] strContractNumber = null;
            List<String> lstParam = new ArrayList<String>();

            strContractNumber = contractNumber.trim().split(",");
            strQuery.append(" and car.mc_contract_number in (:lstContract) ");
            for (int i = 0; i < strContractNumber.length; i++) {
                lstParam.add(strContractNumber[i].trim());
            }

            NativeQuery query = this.session.createNativeQuery(strQuery.toString());
            query.setParameterList("lstContract", lstParam);
            List lst = query.list();
            if (lst != null && lst.size() > 0) {
                results = transformList(lst, WareHouseThankLetter.class);
            }

        }
        return results;
    }

    public List<WareHouseMatrix> getOutputValidation(String contractStatus, String cavetStatus, String cavetErr, String letterStatus, int docType) {

        String strQuery = " select b.description as description, a.expected_rule_output as expectedRuleOutput, "
                + " a.action_type as actionType,a.action as action,"
                + " d.url as url,d.type as type, d.type_err as typeErr "
                + " from state_transaction_matrix a "
                + "JOIN functions b on b.id = a.function_id "
                + "JOIN input_matrix c on c.function_id = b.id and b.status='A' "
                + "left JOIN matrix_detail d on d.state_transation_matrix_id = a.id "
                + "where a.status='A' and c.doc_type = :docType ";

        if (contractStatus != null && !contractStatus.equals("")) {
            strQuery = strQuery + " and UPPER(c.CONTRACT_STATUS) = :contractStatus  ";
        } else {
            strQuery = strQuery + " and c.CONTRACT_STATUS is null  ";
        }
        if (cavetErr != null && !cavetErr.equals("")) {
            strQuery = strQuery + " and  UPPER(c.cavet_err) = :cavetErr ";
        } else {
            strQuery = strQuery + " and c.cavet_err is null  ";
        }
        if (cavetStatus != null && !cavetStatus.equals("")) {
            strQuery = strQuery + " and UPPER(c.CAVET_STATUS) = :cavetStatus ";
        } else {
            strQuery = strQuery + " and c.CAVET_STATUS is null  ";
        }
        if (letterStatus != null && !letterStatus.equals("")) {
            strQuery = strQuery + " and UPPER(c.letter_status) = :letterStatus ";
        } else {
            strQuery = strQuery + " and c.letter_status is null  ";
        }

        NativeQuery query = this.session.createNativeQuery(strQuery);
        query.setParameter("docType", docType);
        if (contractStatus != null && !contractStatus.equals("")) {
            query.setParameter("contractStatus", contractStatus.trim().toUpperCase());
        }
        if (cavetErr != null && !cavetErr.equals("")) {
            query.setParameter("cavetErr", cavetErr.trim().toUpperCase());
        }
        if (cavetStatus != null && !cavetStatus.equals("")) {
            query.setParameter("cavetStatus", cavetStatus.trim().toUpperCase());
        }
        if (letterStatus != null && !letterStatus.equals("")) {
            query.setParameter("letterStatus", letterStatus.trim().toUpperCase());
        }

        List<WareHouseMatrix> results = new ArrayList<>();
        List lst = query.list();
        if (lst != null && lst.size() > 0) {
            results = transformList(lst, WareHouseMatrix.class);
        }
        return results;
    }

	public List<WhDocument> getCheckDoc(Long creditAppId, Long docType) {
		List<?> lst = session.getNamedNativeQuery("getCheckDoc").setParameter("creditAppId", creditAppId).setParameter("docType", docType).list();

        List<WhDocument> retList = new ArrayList<WhDocument>();

        if (lst != null && !lst.isEmpty()) {
            for (Object o : lst) {
                retList.add(transformObject(o, WhDocument.class));
            }
        }

        return retList;
	}

	public void deleteDocument(List<String> lstParam) {	

			this.session.getNamedNativeQuery("deleteDocuments").setParameterList("whIds", lstParam).executeUpdate();
	}

	public void updateBatch(List<String> lstParam, Long batch_id) {
		this.session.getNamedNativeQuery("updateBatch").setParameterList("whIds", lstParam)
				.setParameter("batch_id", batch_id).executeUpdate();
	}
	
	public void updateActive(Long creditAppId, Long docType) {
		this.session.getNamedNativeQuery("updateActive").setParameter("creditAppId", creditAppId).setParameter("docType", docType).executeUpdate();
	}
        
	public List<Long> getLstIdDoc(Long whId) {
		@SuppressWarnings("unchecked")
		List<Long> lst = session.getNamedNativeQuery("getLstIdDocument").setParameter("whDocId", whId).addScalar("id", LongType.INSTANCE).list();
		return lst;
	}
	
	public List<Long> getLstIdCavet(Long whId) {
		@SuppressWarnings("unchecked")
		List<Long> lst = session.getNamedNativeQuery("getLstIdCavet").setParameter("whDocId", whId).addScalar("id", LongType.INSTANCE).list();
		return lst;
	}

	public List<ReturnDocumentInfo> getLstReturnDoc(List<Long> lstWhId) {
		
		List<ReturnDocumentInfo> result = new ArrayList<>();
		if(lstWhId != null && lstWhId.size() > 0){
			List<?> lst = session.getNamedNativeQuery("getLstDocId").setParameter("whDocId", lstWhId).list();
			if (lst != null && !lst.isEmpty()) {
				for (Object obj : lst) {
					result.add(transformObject(obj, ReturnDocumentInfo.class));
				}
			}
		}
		return result;
	}
        
        public List<BigDecimal> getLstAttributeRootDoc(Long whId) {
		@SuppressWarnings("unchecked")
		List<BigDecimal> lst = session.getNamedNativeQuery("getLstAttributeRootDoc").setParameter("whDocId", whId).list();
		return lst;
	}

	public List<WhDocument> getListDocByBatchId(String loginId) {
		List<WhDocument> result = new ArrayList<>();
		List<?> lst = session.getNamedNativeQuery("getListDocByBatchId").setParameter("loginId", loginId).getResultList();

		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				result.add(transformObject(obj, WhDocument.class));
			}
		}
		return result;
	}

        
    public HashMap<String, WhDocument> getRootUpdateErr(Long whDocId) {
        HashMap<String, WhDocument> maplstRoot = new HashMap<String, WhDocument>();
        
        List lstLoanDoc = session.getNamedNativeQuery("getRootUpdateErr").setParameter("whDocId", whDocId).setParameter("contractCavetType", "WH_LOAN_DOC").list();
        List<WhDocument> retListDoc = new ArrayList<WhDocument>();
        if (lstLoanDoc != null && !lstLoanDoc.isEmpty()) {
            for (Object o : lstLoanDoc) {
                retListDoc.add(transformObject(o, WhDocument.class));
            }
        }
        if (retListDoc != null && retListDoc.size() > 0){
                if (!retListDoc.isEmpty()) {
                    maplstRoot.put("rootLoanDoc", retListDoc.get(0));
                }
        }
        
        List lstCavet = session.getNamedNativeQuery("getRootUpdateErr").setParameter("whDocId", whDocId).setParameter("contractCavetType", "WH_CAVET").list();
        List<WhDocument> retListCavet = new ArrayList<WhDocument>();
        if (lstCavet != null && !lstCavet.isEmpty()) {
            for (Object o : lstCavet) {
                retListCavet.add(transformObject(o, WhDocument.class));
            }
        }
        if (retListCavet != null && retListCavet.size() > 0){
                if (!retListCavet.isEmpty()) {
                    maplstRoot.put("rootCavet", retListCavet.get(0));
                }
        }
        
        return maplstRoot;
    }

    //Lay ho so goc cua giay to update loi
    @SuppressWarnings("unchecked")
    public List<Long> getOriginWhDocument(WhDocument whErr) {
        StringBuilder strQuery = new StringBuilder();

        if (whErr.getContractCavetType().equals(1L)) {
            //Ho so goc la HSKV	
            strQuery.append("select id from wh_document wd where wd.doc_type = (select id from code_table ct where ct.category = 'WH_DOC_TYPE' and ct.code_value1 = 'WH_LOAN_DOC' ) and wd.credit_app_id = :creditAppId");

        } else {
            //Ho so goc la cavet
            if (whErr.getContractCavetType().equals(2L)) {
                strQuery.append("select id from wh_document wd where wd.doc_type = (select id from code_table ct where ct.category = 'WH_DOC_TYPE' and ct.code_value1 = 'WH_CAVET' ) and wd.credit_app_id = :creditAppId");
            }
        }

        NativeQuery<?> query = this.session.createNativeQuery(strQuery.toString());
        List<Long> result = (List<Long>) query.setParameter("creditAppId", whErr.getCreditAppId()).addScalar("id", LongType.INSTANCE).getResultList();

        return result;
    }
    
    public List<WhDocument> getWhDocumentListByCreAppId (Long creAppId) {
    	
    	List<WhDocument> result = new ArrayList<>();
    	StringBuilder strQuery = new StringBuilder();
    	strQuery.append("select a.id,a.version,a.credit_app_id,a.doc_type,a.batch_id,a.order_by,a.estimate_date,a.wh_code_id,a.wh_lodge_date,a.created_date,a.created_by,a.last_updated_by,a.last_updated_date,a.contract_cavet_type,a.status,a.wh_lodge_by,a.process_status,a.bill_code,a.delivery_error,a.process_date,a.is_active,a.is_original from wh_document a where a.credit_app_id = :creditAppId");
    	NativeQuery<?> query = this.session.createNativeQuery(strQuery.toString());
        List<?> lst = query.setParameter("creditAppId", creAppId).getResultList();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				result.add(transformObject(obj, WhDocument.class));
			}
		}
		return result;
    }
    
    public Integer getCountDocumentByLoadBatchAndOriginal(String loginId, Long docType, Long creditAppId) {
		Object out = this.session.getNamedNativeQuery("getCountDocumentByLoadBatchAndOriginal")
				.setParameter("loginId", loginId).setParameter("docType", docType).setParameter("creditAppId", creditAppId).getSingleResult();
		
		if(out != null) {
			return Integer.valueOf(out.toString());
		}
			
		return null;
	}
    
    public Integer getCountDocumentByLoadBatchAndActive(String loginId, Long docType, Long creditAppId) {
		Object out = this.session.getNamedNativeQuery("getCountDocumentByLoadBatchAndActive")
				.setParameter("loginId", loginId).setParameter("docType", docType).setParameter("creditAppId", creditAppId).getSingleResult();
		
		if(out != null) {
			return Integer.valueOf(out.toString());
		}
			
		return null;
	}
    
	public Integer getCountDocumentByLoadBatch(Long docType, Long creditAppId) {
		Object out = this.session.getNamedNativeQuery("getCountDocumentByLoadBatch").setParameter("docType", docType)
				.setParameter("creditAppId", creditAppId).getSingleResult();

		if (out != null) {
			return Integer.valueOf(out.toString());
		}

		return null;
	}
    
    public List<WareHouseHistory> getLstHistoryWarehouse(Long whId, String nameQuery) {
        List<WareHouseHistory> results = new ArrayList<>();
        List lst = session.getNamedNativeQuery(nameQuery).setParameter("whId", whId).list();
        if (lst != null && !lst.isEmpty()) {
            results = transformList(lst, WareHouseHistory.class);
        }
        return results;
    }

	public List<WhDocumentDTO> getOriginalByCreditAppId(List<Long> creditAppIdLst) {
		
		
		
		/* List<WhDocumentDTO> results = new ArrayList<>();
	        List<?> lst = session.getNamedNativeQuery("getOriginalByCreditAppId").setParameter("creditAppIdLst", creditAppIdLst).list();
	        if (lst != null && !lst.isEmpty()) {
				for (Object obj : lst) {
					results.add(transformObject(obj, WhDocumentDTO.class));
				}
	        }
	        return results;*/
		
		if(null == creditAppIdLst  || creditAppIdLst.isEmpty() ){
			return new ArrayList<WhDocumentDTO>();
		}
		
		AtomicInteger counter = new AtomicInteger(0);
		 ArrayList  tmp =  new ArrayList(creditAppIdLst.parallelStream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 1000)).values());
		String stQerry = " SELECT whd.ID,whd.VERSION,whd.CREDIT_APP_ID,whd.DOC_TYPE,ctDocType.CoDE_VALUE1,whd.BATCH_ID,whd.ORDER_BY,whd.ESTIMATE_DATE,whd.WH_CODE_ID,whd.WH_LODGE_DATE,whd.CREATED_DATE,whd.CREATED_BY,"
				+ " whd.LAST_UPDATED_BY,whd.LAST_UPDATED_DATE,whd.CONTRACT_CAVET_TYPE,whd.STATUS,whd.WH_LODGE_BY,whd.PROCESS_STATUS,whd.BILL_CODE,whd.DELIVERY_ERROR,whd.PROCESS_DATE,whd.IS_ACTIVE,whd.IS_ORIGINAL "
				+ " FROM WH_DOCUMENT  whd INNER JOIN CODE_TABLE ctDocType on whd.DOC_TYPE = ctDocType.ID  Where whd.CREDIT_APP_ID in (:praram0) ";
			
		 for (int i = 1 ; i< tmp.size(); i++) {
			 stQerry  = stQerry + " OR whc.CREDIT_APP_ID IN(:praram" + i+ ")";
		}
		 
		 Query query = this.session.createNativeQuery(stQerry).setHibernateFlushMode(FlushMode.ALWAYS);	 
		
		 for (int i = 0 ; i< tmp.size(); i++) {
			 query.setParameter("praram"+i, tmp.get(i));
		} 
		
		List<?> lst = query.getResultList();
		List<WhDocumentDTO> retList = new ArrayList<WhDocumentDTO>();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				retList.add(transformObject(obj, WhDocumentDTO.class));
			}
		}
		
		return retList;
		
	}
	
	
	
	
	public WhDocument getDocumentByActive(Long creditAppId, Long docType) {
		return transformObject( this.session.getNamedNativeQuery("getDocumentByActive").setParameter("creditAppId", creditAppId).setParameter("docType", docType).getSingleResult(),WhDocument.class);
	}
	
	public Long getWhIdByOriginal(Long creditAppId, Long docType) {
		try {
			Object out = this.session.getNamedNativeQuery("getWhIdByOriginal").setParameter("creditAppId", creditAppId).setParameter("docType", docType).getSingleResult();
			return Long.valueOf(out.toString());
		} catch (Exception e) {
			return null;
		}
	}
	
	public Boolean getWhIdByStatusProcess(Long creditAppId, Long docType) {
		try {
			Object out = this.session.getNamedNativeQuery("getWhIdByStatusProcess").setParameter("creditAppId", creditAppId).setParameter("docType", docType).getSingleResult();	
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<GoodsDTO> getLstGoods(Long whId) {
		
		List<GoodsDTO> result = new ArrayList<>();
		List<?> lst = session.getNamedNativeQuery("getLstGoods").setParameter("whId", whId).list();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				result.add(transformObject(obj, GoodsDTO.class));
			}
		}

		return result;
	}
	
	public List<ReturnDocumentInfo> getLstReturnDocument2(Long whId) {

		List<ReturnDocumentInfo> result = new ArrayList<>();
		List<?> lst = session.getNamedNativeQuery("getLstReturnDocument2").setParameter("whId", whId).list();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				result.add(transformObject(obj, ReturnDocumentInfo.class));
			}
		}

		return result;
	}
}
