package com.mcredit.data.warehouse.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.Query;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.warehouse.entity.WhDocumentErr;
import com.mcredit.model.object.warehouse.WhDocumentErrDTO;
import com.mcredit.model.object.warehouse.WhLstErrPayBackCavet;
import com.mcredit.util.PartitionListHelper;

public class WhDocumentErrRepository extends BaseRepository implements IUpdateRepository<WhDocumentErr>, IUpsertRepository<WhDocumentErr>, IAddRepository<WhDocumentErr> {

	public WhDocumentErrRepository(Session session) {
		super(session);
	}

	@Override
	public void add(WhDocumentErr item) {
		this.session.save(item);
		this.session.flush();
		this.session.clear();
	}

	@Override
	public void upsert(WhDocumentErr item) {
		this.session.saveOrUpdate("WhDocumentErr", item);
		this.session.flush();
		this.session.clear();
	}

	@Override
	public void update(WhDocumentErr item) {
		this.session.update(item);
		this.session.flush();
		this.session.clear();
	}
	
	public HashMap<Long, List<WhDocumentErrDTO>>  getListWareHouseErrorListCase(List<Long> listWhdocId) {
		if (null == listWhdocId || listWhdocId.isEmpty())
			return new HashMap<>();

		List<List<Long>> tmp = new ArrayList<List<Long>>(PartitionListHelper.partition(listWhdocId, 1000));
		String stQerry = " SELECT wde.ID,wde.WH_DOC_ID,wde.CREATED_BY,wde.CREATED_DATE,wde.ERR_TYPE,wde.UPDATE_REQUEST, ct_type.CODE_VALUE1 as errorCode ,ct_type.DESCRIPTION1 as errorName "
				+ "FROM  Wh_Document_Err wde LEFT JOIN  CODE_TABLE ct_type on ct_type.id = wde.ERR_TYPE Where   ";

		for (int i = 0; i < tmp.size(); i++) {
			if (i == 0) {
				stQerry = stQerry + " wde.WH_DOC_ID in(:praram" + i + ")";
			} else {
				stQerry = stQerry + " OR wde.WH_DOC_ID IN(:praram" + i + ")";
			}
		}
		
		Query query = this.session.createNativeQuery(stQerry).setHibernateFlushMode(FlushMode.ALWAYS);

		for (int i = 0; i < tmp.size(); i++) {
			query.setParameterList("praram" +i, tmp.get(i));
		}

		List<?> lst = query.list();
		HashMap<Long, List<WhDocumentErrDTO>> hashListError = new HashMap<>();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				WhDocumentErrDTO whDocumentErrDTO = transformObject(obj, WhDocumentErrDTO.class);
				List<WhDocumentErrDTO> listErrorByWhdocId = hashListError.get(whDocumentErrDTO.getWhDocId());
				if (listErrorByWhdocId == null) {
					listErrorByWhdocId = new ArrayList<>();
				}
				listErrorByWhdocId.add(whDocumentErrDTO);
				hashListError.put(whDocumentErrDTO.getWhDocId(), listErrorByWhdocId);
			}
		}

		return hashListError;
	}
	
    public void deleteLstErr(Long whDocumentId) {		
                this.session.getNamedNativeQuery("deleteLstErr").setParameter("whDocumentId", whDocumentId).executeUpdate();
	}

	public HashMap<Long, List<WhLstErrPayBackCavet>> getLstErrPayBackCavet(List<Long> listWhdocId) {
		if(null == listWhdocId  || listWhdocId.isEmpty()) return new HashMap<>(); 
		
		   ArrayList< List<Long>> tmp = new ArrayList<List<Long>>(PartitionListHelper.partition(listWhdocId, 1000)) ;
		String stQerry = "SELECT whc.ID,whc.WH_DOC_ID, whc.NOTE as ERR_NAME, ct_error.DESCRIPTION1  as ERR_CODE FROM WH_DOCUMENT_CHANGE  whc LEFT JOIN  CODE_TABLE ct_type on ct_type.id = whc.TYPE "
				+ "LEFT JOIN  CODE_TABLE ct_error on ct_error.id = whc.ID_CODE_TABLE Where ct_type.code_group='WH' and ct_type.category ='WH_CHAN_TYPE' and ct_type.CODE_VALUE1 = 'WH_ERR' ";
			
		for (int i = 0; i < tmp.size(); i++) {
			if (i == 0) {
				stQerry = stQerry + " wde.WH_DOC_ID in(:praram" + i + ")";
			} else {
				stQerry = stQerry + " OR wde.WH_DOC_ID IN(:praram" + i + ")";
			}
		}
		
		Query query = this.session.createNativeQuery(stQerry).setHibernateFlushMode(FlushMode.ALWAYS);

		for (int i = 0; i < tmp.size(); i++) {
			query.setParameterList("praram" +i, tmp.get(i));
		}

		List<?> lst = query.list();
		HashMap<Long, List<WhLstErrPayBackCavet>> hashListError = new HashMap<>();
		if (lst != null && !lst.isEmpty()) {
			for (Object obj : lst) {
				WhLstErrPayBackCavet payBackCavet = transformObject(obj, WhLstErrPayBackCavet.class);
				List<WhLstErrPayBackCavet> listErrorByWhdocId = hashListError.get(payBackCavet.getIdWhDocument());
				if(listErrorByWhdocId == null){
					listErrorByWhdocId = new ArrayList<>();
				}
				listErrorByWhdocId.add(payBackCavet);
				hashListError.put(payBackCavet.getIdWhDocument(), listErrorByWhdocId);
			}
		}
		return hashListError;
	}
        
}
