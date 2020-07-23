package com.mcredit.data.mobile.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.data.repository.IRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.object.mobile.dto.ReturnedCaseDTO;

public class UplCreditAppFilesRepository extends BaseRepository implements IRepository<UplCreditAppFiles>, IUpsertRepository<UplCreditAppFiles>{
	public UplCreditAppFilesRepository(Session session) {
		super(session);
	}
	
	public void add(UplCreditAppFiles appFiles) {
		this.session.save("UplCreditAppFiles", appFiles);
	}

	@Override
	public void upsert(UplCreditAppFiles item) {
		// TODO Auto-generated method stub
		this.session.update(item);
		
	}

	@SuppressWarnings("unchecked")
	public List<ReturnedCaseDTO> getMaxDocumentVersion(List<Long> lstDocument, Long id) {
		NativeQuery<?> query = this.session.getNamedNativeQuery("getMaxDocumentVersion");
		List<?> res = (List<?>) query
				.setParameter("id1", id)
				.setParameter("id", id)
				.setParameterList("document_ids", lstDocument.toArray(new Long[0]))
				.getResultList();
		List<ReturnedCaseDTO> lst = new ArrayList<>();
		if (null != res) {
			lst = transformList(res, ReturnedCaseDTO.class);			
		}
		return lst;
	}
	
	@SuppressWarnings("unchecked")
	public List<UplCreditAppFiles> getFileForSync(Long id) {
		return this.session.getNamedQuery("getFileForSync").setParameter("id", id).getResultList();
	}
}
