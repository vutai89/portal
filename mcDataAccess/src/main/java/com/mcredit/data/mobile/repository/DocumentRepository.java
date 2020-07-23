package com.mcredit.data.mobile.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IRepository;
import com.mcredit.model.object.mobile.dto.DocumentDTO;

public class DocumentRepository extends BaseRepository implements IRepository<DocumentDTO>{

	public DocumentRepository(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentDTO> listDocument(){
		List<DocumentDTO> results = new ArrayList<DocumentDTO>();
		List lst =	this.session.getNamedNativeQuery("getListDocument").list();
		if (lst != null && !lst.isEmpty()){
			results = transformList(lst, DocumentDTO.class);
		}
		return results;
	}

}
