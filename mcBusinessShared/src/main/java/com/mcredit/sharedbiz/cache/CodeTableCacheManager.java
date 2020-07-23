package com.mcredit.sharedbiz.cache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;

import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.CodeTable;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.CodeTableEliteDTO;
import com.mcredit.model.dto.CodeTableV2EliteDTO;
import com.mcredit.model.dto.KioskDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.DateFormatTag;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.CodeCategory;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public final class CodeTableCacheManager implements IDataCache {

	private ModelMapper modelMapper = new ModelMapper();
	private List<CodeTableDTO> codeCache;
//	private List<CodeTableDTO> codeCacheById;
	private List<CodeCategory> codeCategory;
	private List<CodeTableDTO> locationTree;
	private List<CodeTableDTO> callStatusTree;
	private UnitOfWork uok = null;
	private Map<String, CodeTableDTO> mapCodeTable = null;
	private Map<Integer, CodeTableDTO> mapCodeTableByID = null;
	
	private static CodeTableCacheManager instance;

	private CodeTableCacheManager() {
		initCache();
	}

	public static synchronized CodeTableCacheManager getInstance() {
		if (instance == null) {
			synchronized (CodeTableCacheManager.class) {
				if (null == instance) {
					instance = new CodeTableCacheManager();
				}
			}
		}
		return instance;
	}

	public void initCache() {
		String cdCat = "";
		int i = 0;
		int prodId = -1;
		int prodGrp = -1;
		int prodCat = -1;
		
		
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			List<CodeTable> codeCaches = UnitOfWorkHelper.tryCatch(uok, ()->{
				List<CodeTable> lst = uok.common.codeTableRepo().findActiveCodeTable();
				return lst;
			});
			
//			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
//			List<CodeTable> codeCacheByIds = UnitOfWorkHelper.tryCatch(uok, ()->{
//				return uok.common.codeTableRepo().findActiveCodeTableOrderById();
//			});
			
			
			mapCodeTable = new HashMap<String, CodeTableDTO>();
			mapCodeTableByID = new HashMap<Integer, CodeTableDTO>();
			codeCache = new ArrayList<CodeTableDTO>();
			
			CodeTableDTO tempValue = null;
			for (CodeTable item : codeCaches){
				tempValue = modelMapper.map(item, CodeTableDTO.class);
				codeCache.add(tempValue);
				mapCodeTableByID.put(tempValue.getId(), tempValue);
				mapCodeTable.put(String.format("%s-%s-%s-%s-%s-%s", item.getCodeGroup(),item.getCategory(),item.getCodeValue1(),item.getProductId(),item.getProductGroupId(),item.getProductCatId()), tempValue);
			}

//			codeCacheById = new ArrayList<CodeTableDTO>();
//			for (CodeTable item : codeCacheByIds) {
//				codeCacheById.add(modelMapper.map(item, CodeTableDTO.class));
//			}

			// Build category code list and index
			codeCategory = new ArrayList<CodeCategory>();
			for (i = 0; i < codeCache.size(); i++) {
				CodeTableDTO ct = codeCache.get(i);
				if (!cdCat.equals(ct.getCategory())
					|| (prodId != ct.getProductId())
					|| (prodGrp != ct.getProductGroupId())
					|| (prodCat != ct.getProductCatId())) {
					if (codeCategory.size() > 0) {
						codeCategory.get(codeCategory.size() - 1).setEndIndex(i);
					}
					codeCategory.add(new CodeCategory(ct.getCodeGroup(), ct.getCategory(), 
							ct.getProductId(), ct.getProductGroupId(), ct.getProductCatId(), i));
				}
				cdCat = ct.getCategory();
				prodId = ct.getProductId();
				prodGrp = ct.getProductGroupId();
				prodCat = ct.getProductCatId();
			}
			// Set for last item
			if (codeCategory.size() > 0) {
				codeCategory.get(codeCategory.size() - 1).setEndIndex(i);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public List<CodeTableDTO> getCodeByCategory(String cat, int prodId, int prodGrp, int prdCat) {
		if (codeCache == null) {
			initCache();
		}
		if (cat == null || "".equals(cat)) {
			System.out.println("Code Category cannot be null or empty !");
			return null;
		}

		List<CodeTableDTO> tmp = null;
		for (CodeCategory cc : codeCategory) {
			if (cat.equals(cc.getCategory())
				&& (cc.getProductId() == prodId)
				&& (cc.getProductGroupId() == prodGrp)
				&& (cc.getProductCatId() == prdCat)) {
				tmp = codeCache.subList(cc.getStartIndex(), cc.getEndIndex());
				break;
			}
		}
		if (tmp == null) {
			System.out
					.println("Cannot find matching list with code category = "
							+ cat);
		}
		return tmp;
	}
	
	public List<CodeTableDTO> getCodeByCategory(String cat) {
		return getCodeByCategory(cat, 0, 0, 0);
	}

//	private int findIindex(int codeId, int sIdx, int eIdx) {
//		int index = (eIdx + sIdx)/2;
//		CodeTableDTO ct = codeCacheById.get(index);
//		if(ct.getId() == codeId) {
//			return index;
//		} else if(index <= sIdx) {
//			if(codeCacheById.get(eIdx).getId() == codeId) {
//				return eIdx;
//			}
//			return -1;
//		} else if(codeId > ct.getId()) {
//			return findIindex(codeId, index, eIdx);
//		} else if(codeId < ct.getId()) {
//			return findIindex(codeId, sIdx, index);
//		}
//		return -1;
//	}

	/**
	 * get code_table info from cache
	 * 
	 * @author dongtd.ho
	 * @param codeId: id of code_table table
	 * @return single item from code_table
	 */
	public CodeTableDTO getCodeById(int codeId) {
		if (mapCodeTableByID == null)
			initCache();
		
		if (codeId == 0) {
			System.out.println("CodeId cannot be null or empty or zero !");
			return null;
		}

		CodeTableDTO tmp = mapCodeTableByID.get(codeId);
		if (tmp == null)
			System.out.println("Cannot find matching list with code category = " + codeId);
		
		return tmp;
	}
	
	public CodeTableDTO getCodeById1(Integer codeId) {
		try{
			CodeTableDTO result = this.getCodeById(codeId);
			return result == null ? new CodeTableDTO() : result;
		}catch(Exception ex){
			return new CodeTableDTO();
		}
	}

	public CodeTableDTO getIdByCategoryCodeValue(String cat, String codeValue1) {
		return getIdByCategoryCodeValue(cat, codeValue1, null);
	}
	
	public List<Integer> getIdByListCodeValue(String cat, List<String> codeValue1) {
		List<Integer> resoult = new ArrayList<>();

		for (CodeTableDTO cc : codeCache) {

			if (!StringUtils.isNullOrEmpty(cat) && !codeValue1.isEmpty()) {

				if (cat.equals(cc.getCategory())) {

					for (String code : codeValue1) {
						if (cc.getCodeValue1().equals(code)) {
							resoult.add(cc.getId());
						}
					}
				}
			}
		}

		return resoult;
	}
	
	

	public CodeTableDTO getIdByCategoryCodeValue(String cat, String codeValue1, String codeValue2) {
		if (StringUtils.isNullOrEmpty(cat)) {
			System.out.println("Code Category cannot be null or empty !");
			return null;
		}
		if (StringUtils.isNullOrEmpty(codeValue1) &&StringUtils.isNullOrEmpty(codeValue2)) {
			System.out.println("Code Value cannot be null or empty !");
			return null;
		}
		List<CodeTableDTO> tmp = getCodeByCategory(cat);
		CodeTableDTO ret = null;
		if (tmp != null) {
			for (CodeTableDTO ct : tmp) {
				// Verify codevalue1
				if(!StringUtils.isNullOrEmpty(codeValue1)) {
					if (codeValue1.equals(ct.getCodeValue1())) {
						ret = ct;
					}
				}
				// Verify codevalue2
				if(!StringUtils.isNullOrEmpty(codeValue2)) {
					if(ret != null) {
						if (!codeValue2.equals(ret.getCodeValue2())) {
							ret = null;
						}
						break;
					} else if (codeValue2.equals(ct.getCodeValue2())) {
						ret = ct;
						if(!StringUtils.isNullOrEmpty(codeValue1)) {
							if (!codeValue1.equals(ret.getCodeValue1())) {
								ret = null;
							}
						}
						break;
					}
				}
			}
			if (ret == null) {
				System.out
						.println("Cannot find matching code with code category = "
								+ cat + " and code value1 = " + codeValue1 + " and code value2 = " + codeValue2);
			}
		}
		return ret;
	}
	

//	public  List<CodeTableDTO> getCodeByCategoryAndCodeGroup(String cat , String grp) {
//		
//		if (codeCache == null) {
//			initCache();
//		}
//		if (cat == null || "".equals(cat)) {
//			System.out.println("Code Category cannot be null or empty !");
//			return null;
//		}
//		if (grp == null || "".equals(grp)) {
//			System.out.println("Code Group cannot be null or empty !");
//			return null;
//		}
//
//		List<CodeTableDTO> tmp = null;
//		for (CodeCategory cc : codeCategory) {
//			if (cat.equals(cc.getCategory()) || grp.equals(cc.getCodeGroup())) {
//				tmp = codeCache.subList(cc.getStartIndex(), cc.getEndIndex());
//				break;
//			}
//		}
//		if (tmp == null) {
//			System.out.println("Cannot find matching list with code category = " + cat + " code group = " + grp);
//		}
//		return tmp;
//	}
	
	public List<CodeTableDTO> getByGroupAndCategory(String codeGroup, String cat) {
		if (codeCache == null)
			initCache();

		
		List<CodeTableDTO> tmp = new ArrayList<CodeTableDTO>();
		
		for (CodeTableDTO cc : codeCache) {
			
			if(!StringUtils.isNullOrEmpty(cat) && !StringUtils.isNullOrEmpty(codeGroup)){
				
				if (cat.equals(cc.getCategory()) &&  codeGroup.equals(cc.getCodeGroup()))
					tmp.add(cc);
				
			}else if ((!StringUtils.isNullOrEmpty(cat) && cat.equals(cc.getCategory())) || (!StringUtils.isNullOrEmpty(codeGroup) && codeGroup.equals(cc.getCodeGroup())))
				tmp.add(cc);
		}		
		return tmp;
	}
	
	public List<CodeTableV2EliteDTO> getByGroupAndCategoryV2(String codeGroup, String cat) {
		if (codeCache == null)
			initCache();

		List<CodeTableDTO> tmp = new ArrayList<CodeTableDTO>();
		
		for (CodeTableDTO cc : codeCache) {
			
			if(!StringUtils.isNullOrEmpty(cat) && !StringUtils.isNullOrEmpty(codeGroup)){
				
				if (cat.equals(cc.getCategory()) &&  codeGroup.equals(cc.getCodeGroup()))
					tmp.add(cc);
				
			}else if ((!StringUtils.isNullOrEmpty(cat) && cat.equals(cc.getCategory())) || (!StringUtils.isNullOrEmpty(codeGroup) && codeGroup.equals(cc.getCodeGroup())))
				tmp.add(cc);
		}
		
		List<CodeTableV2EliteDTO> finalLst = new ArrayList<CodeTableV2EliteDTO>();
		
		for (CodeTableDTO item : tmp) {
			finalLst.add(
					new CodeTableV2EliteDTO(
							item.getId(),
							item.getCodeValue1(),
							item.getDescription1()==null?"":item.getDescription1(),
							item.getParentId()==null?0:item.getParentId()
					)
			);
		}
		
		return finalLst;
	}
	
	public List<CodeTableEliteDTO> getByCategory(String cat) {
		if (codeCache == null)
			initCache();

		List<CodeTableDTO> tmp = new ArrayList<CodeTableDTO>();
		
		for (CodeTableDTO cc : codeCache) {
			
			if ( cat.equals(cc.getCategory()) )
				tmp.add(cc);
			
			/*if( !StringUtils.isNullOrEmpty(cat) ) {
				
				if ( cat.equals(cc.getCategory()) )
					tmp.add(cc);
				
			}else if ( !StringUtils.isNullOrEmpty(cat) && cat.equals(cc.getCategory()) )
				tmp.add(cc);*/
		}
		
		List<CodeTableEliteDTO> finalLst = new ArrayList<CodeTableEliteDTO>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatTag.DATEFORMAT_CODE_TABLE.value());
		
		for (CodeTableDTO item : tmp) {
			finalLst.add(
					new CodeTableEliteDTO(
						item.getId(),
						item.getCodeValue1(),
						item.getDescription1()==null?"":item.getDescription1(),
						item.getCodeValue2()==null?"":item.getCodeValue2(),
						item.getDescription2()==null?"":item.getDescription2(),
						item.getParentId()==null?0:item.getParentId(),
						item.getReference()==null?"":item.getReference(),
						item.getStatus()==null?"":item.getStatus(),
						item.getStartEffDate()==null?"":dateFormat.format(item.getStartEffDate()),
						item.getEndEffDate()==null?"":dateFormat.format(item.getEndEffDate())
					)
			);
		}
		
		return finalLst;
	}
	
	public static void main(String[] args) throws Exception {
		
		Date startDate = new SimpleDateFormat(DateFormatTag.DATEFORMAT_CODE_TABLE.value()).parse("05/12/2015 00:00:00");
		Date mainDate = new SimpleDateFormat(DateFormatTag.DATEFORMAT_CODE_TABLE.value()).parse("05/12/2015 00:00:00");
		Date endDate = new SimpleDateFormat(DateFormatTag.DATEFORMAT_CODE_TABLE.value()).parse("05/12/2015 00:00:00");
		
		System.out.println( DateUtil.isBetweenRange(mainDate, startDate, endDate) );
		
		//CodeTableCacheManager.getInstance().initCache();
		/*Date date = new Date();  
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
	    String strDate= formatter.format(date);  
	    System.out.println(strDate);*/
	}
	
	public List<CodeTableEliteDTO> getByCategory(String cat, String effectDate, List<String> swapDescription) {
		if (codeCache == null)
			initCache();

		List<CodeTableDTO> tmp = new ArrayList<CodeTableDTO>();
		
		if( !StringUtils.isNullOrEmpty(effectDate) ) {
			
			Date effectDates = null;
            try {
            	effectDates = new SimpleDateFormat(DateFormatTag.DATEFORMAT_CODE_TABLE.value()).parse(effectDate);
			} catch (ParseException e) {
				try {
					effectDates = new SimpleDateFormat(DateFormatTag.DATEFORMAT_yyyy_MM_dd_HH_mm_ss.value()).parse(effectDate);
				} catch (ParseException ex) {
					System.out.println("CodeTableCacheManager.getByCategory.ex: " + ex.toString());
				}
			}
			
            if( effectDates!=null ) {
            	
            	for (CodeTableDTO cc : codeCache) {
    				if ( cat.equals(cc.getCategory()) && DateUtil.isBetweenRange(effectDates, cc.getStartEffDate(), cc.getEndEffDate()) )
    					tmp.add(cc);
    			}
            	
            }else {
            	// Unfortunately case, return all likes default.
            	for (CodeTableDTO cc : codeCache) {
    				if ( cat.equals(cc.getCategory()) )
    					tmp.add(cc);
    			}
            }
            
		}else {
			for (CodeTableDTO cc : codeCache) {
				if ( cat.equals(cc.getCategory()) )
					tmp.add(cc);
			}
		}
		
		List<CodeTableEliteDTO> finalLst = new ArrayList<CodeTableEliteDTO>();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatTag.DATEFORMAT_CODE_TABLE.value());  
		
		for (CodeTableDTO item : tmp) {
			if (null != swapDescription && swapDescription.contains(cat)) { 
				finalLst.add(
						new CodeTableEliteDTO(
							item.getId(),
							item.getCodeValue1(),
							item.getDescription2()==null?"":item.getDescription2(),
							item.getCodeValue2()==null?"":item.getCodeValue2(),
							item.getDescription1()==null?"":item.getDescription1(),
							item.getParentId()==null?0:item.getParentId(),
							item.getReference()==null?"":item.getReference(),
							item.getStatus()==null?"":item.getStatus(),
							item.getStartEffDate()==null?"":dateFormat.format(item.getStartEffDate()),
							item.getEndEffDate()==null?"":dateFormat.format(item.getEndEffDate())
						)
				);
			} else {
				finalLst.add(
						new CodeTableEliteDTO(
							item.getId(),
							item.getCodeValue1(),
							item.getDescription1()==null?"":item.getDescription1(),
							item.getCodeValue2()==null?"":item.getCodeValue2(),
							item.getDescription2()==null?"":item.getDescription2(),
							item.getParentId()==null?0:item.getParentId(),
							item.getReference()==null?"":item.getReference(),
							item.getStatus()==null?"":item.getStatus(),
							item.getStartEffDate()==null?"":dateFormat.format(item.getStartEffDate()),
							item.getEndEffDate()==null?"":dateFormat.format(item.getEndEffDate())
						)
				);
			}
		}
		
		return finalLst;
	}
	
	public List<CodeTableDTO> getByParentId(String parentId) {
		if (codeCache == null)
			initCache();

		List<CodeTableDTO> tmp = new ArrayList<CodeTableDTO>();
		
		for (CodeTableDTO cc : codeCache) {
			
			if(cc.getParentId() != null && parentId.equalsIgnoreCase(cc.getParentId().toString()))
				tmp.add(cc);
		}		
		return tmp;
	}
	
	public CodeTableDTO getCodeByCategoryCodeValue1(String cat, String codeValue1) {
		if (codeCache == null)
			initCache();

		CodeTableDTO out = new CodeTableDTO();
		
		for (CodeTableDTO cc : codeCache) {
			
			if(!StringUtils.isNullOrEmpty(cat) && !StringUtils.isNullOrEmpty(codeValue1)){
				
				if (cat.equals(cc.getCategory()) && codeValue1.equals(cc.getCodeValue1()))
					out = cc;
			}
		}		
		return out;
	}
	
	public List<CodeTableDTO> findLocation() throws Exception {

		try {
			
			if(this.locationTree != null && this.locationTree.size() > 0)
				return this.locationTree;
			
			this.locationTree = this.getByGroupAndCategory("LOCA","PROVINCE");
			
			if( this.locationTree != null && this.locationTree.size() > 0 ) {
				
				for (CodeTableDTO item : this.locationTree) {
					List<CodeTableDTO> districtLst = this.getByParentId(item.getId().toString());
					
					if(districtLst != null && districtLst.size() > 0)
					{
						item.setChildLst(districtLst);
						for (CodeTableDTO item1 : districtLst){
							
							List<CodeTableDTO> wardLst = this.getByParentId(item1.getId().toString());
							if(wardLst != null && wardLst.size() > 0){
								item1.setChildLst(wardLst);
							}
							
						}
						
					}
				}
			}
			
			return this.locationTree;
			
		} catch (Exception ex) {
			throw new ValidationException(ex.toString());
		}
	}
	
	public List<CodeTableDTO> findCallStatus() throws Exception {

		try {
			
			if(this.callStatusTree != null && this.callStatusTree.size() > 0)
				return this.callStatusTree;
			
			this.callStatusTree = this.getByGroupAndCategory("CALL", "CALL_STAT");
			
			if( this.callStatusTree != null && this.callStatusTree.size() > 0 ) {
				
				List<CodeTableDTO> callResultLst = this.getByGroupAndCategory("CALL", "CALL_RSLT");
				
				if( callResultLst!=null && callResultLst.size() > 0 ) {
				
					callResultLst.addAll(this.callStatusTree);
					
					List<CodeTableDTO> unionLst = new ArrayList<CodeTableDTO>();
					
					List<CodeTableDTO> newLstChild = null;
					
					for (CodeTableDTO oNull : this.callStatusTree) {
						
						newLstChild = new ArrayList<CodeTableDTO>();
						
						for (CodeTableDTO oNotNull : callResultLst) {
							if( oNull.getId().equals(oNotNull.getParentId()) )
								newLstChild.add(oNotNull);
						}
						
						oNull.setChildLst(newLstChild);
						
						unionLst.add(oNull);
					}
					
					return unionLst;
				}
			}
			
			return new ArrayList<CodeTableDTO>();
			
		} catch (Exception ex) {
			throw new ValidationException(ex.toString());
		}
	}
	
	public List<CodeTableDTO> getBy(CTGroup group, CTCat cat) {
		return this.getByGroupAndCategory(group.value(), cat.value());
	}
	
	public CodeTableDTO getbyID(Integer Id) throws ValidationException {
		
		if(Id== null || Id.equals(0))
			throw new ValidationException("ID cannot be null or empty ! ");
			
		if (codeCache == null) {
			initCache();
		}
		
//		for (CodeTableDTO cc : codeCache) {
//			if (Id.equals(cc.getId())) {
//				return cc ;
//			}
//		}
		
		return mapCodeTableByID.containsKey(Id) ? mapCodeTableByID.get(Id) : null;
	}
	
	public CodeTableDTO getBy(CTCodeValue1 codeValue1, CTCat category, CTGroup group) {
		if (codeCache == null)
			initCache();

		return mapCodeTable.get(String.format("%s-%s-%s-%s-%s-%s", group.value(),category.value(),codeValue1.value(),"0","0","0"));
	}
	
	public CodeTableDTO getBy(String codeValue1, CTCat category, CTGroup group) {
		if (codeCache == null)
			initCache();

		CodeTableDTO out = null;
		String cValue1 = codeValue1;
		String cGroup = group.value();
		String cCategory = category.value();
		for (CodeTableDTO cc : codeCache) {

			if (cCategory.equalsIgnoreCase(cc.getCategory()) && cValue1.equalsIgnoreCase(cc.getCodeValue1())
					&& cGroup.equalsIgnoreCase(cc.getCodeGroup())) {
				out = cc;
				break;
			}
		}
		return out;
	}
	
	public CodeTableDTO getBy(CTCodeValue1 codeValue1,CTCat category) {
		if (codeCache == null)
			initCache();

		CodeTableDTO out = null;
		String cValue1 = codeValue1.value();
		String cCategory = category.value();
		for (CodeTableDTO cc : codeCache) {	
			if (cCategory.equalsIgnoreCase(cc.getCategory()) && cValue1.equalsIgnoreCase(cc.getCodeValue1()))
			{
				out = cc;
				break;
			}
		}		
		return out;
	}
	
	public Long getIdBy(CTCodeValue1 codeValue1, CTCat category, CTGroup group) {
		CodeTableDTO out = getBy(codeValue1,category,group);		
		return out == null ? null : out.getId().longValue();
	}
	
	public Long getIdBy(CTCodeValue1 codeValue1,CTCat category) {
		CodeTableDTO out = this.getBy(codeValue1, category);
		return out != null ? out.getId().longValue() : null ;
	}
	
	public Long getIdBy(CTCodeValue1 codeValue1,CTCat category,Integer defaultValue) {
		CodeTableDTO out = this.getBy(codeValue1, category);
		return out != null ? out.getId().longValue() : defaultValue.longValue();
	}
	
	public void refresh() {
		initCache();
	}

	public List<CodeTableDTO> getIdByParent(String[] codeVParent, String catParent) {
		List<Integer> parentIds = new ArrayList<>();

		for (String string : codeVParent) {
			parentIds.add(getIdByCategoryCodeValue(catParent, string, null).getId());
		}

		if (parentIds != null && !parentIds.isEmpty() && parentIds.size() > 0) {
			List<CodeTableDTO> out = new ArrayList<>();
			for (CodeTableDTO codeTableDTO : codeCache) {
				for (Integer parentId : parentIds) {
					if (parentId.equals(codeTableDTO.getParentId())) {
						out.add(codeTableDTO);
					}
				}
			}
			return out;
		}

		return null;
	}

	public HashMap<Long, CodeTableDTO> findCodeTableWareHouseSeach(List<String> listCategoryWHSeach, List<String> listCodeValue1WHSeach) {

		HashMap<Long, CodeTableDTO> resoult = new HashMap<>();

		for (CodeTableDTO cc : codeCache) {

			if (!listCategoryWHSeach.isEmpty() && !listCodeValue1WHSeach.isEmpty()) {

				for (String category : listCategoryWHSeach) {
					if (cc.getCategory().equals(category)) {
						for (String code1 : listCodeValue1WHSeach) {
							if (cc.getCodeValue1().equals(code1)) {
								resoult.put(Long.valueOf(cc.getId()), cc);
							}

						}
					}
				}
			}
		}

		return resoult;

	}
	
	public HashMap<String, CodeTableDTO> findCodeTableByLstGroupCateGory(List<String> listCodeGroup, List<String> listCategory) {

		HashMap<String, CodeTableDTO> resoult = new HashMap<>();

		for (CodeTableDTO cc : codeCache) {

			if (!listCodeGroup.isEmpty() && !listCategory.isEmpty()) {

				for (String group : listCodeGroup) {
					if (cc.getCodeGroup().equals(group)) {
						for (String category : listCategory) {
							if (cc.getCategory().equals(category)) {
								resoult.put(cc.getCodeValue2(), cc);
							}
						}
					}
				}
			}
		}

		return resoult;

	}
	
	public HashMap<Long, CodeTableDTO> hashCodeTablebyCodeGroupCategory(List<String> listCodeGroup, List<String> listCategory) {

		HashMap<Long, CodeTableDTO> resoult = new HashMap<>();

		for (CodeTableDTO cc : codeCache) {

			if (!listCodeGroup.isEmpty() && !listCategory.isEmpty()) {

				for (String codeGroup : listCodeGroup) {
					if (cc.getCodeGroup().equals(codeGroup)) {
						for (String category : listCategory) {
							if (cc.getCategory().equals(category)) {
								resoult.put(Long.valueOf(cc.getId()), cc);
							}

						}
					}
				}
			}
		}

		return resoult;

	}

	public List<String> getTopComp(){
		List<String> lstTopComp = new ArrayList<String>();
		for (CodeTableDTO cc : codeCache) {
			if((CTGroup.CORP == CTGroup.from(cc.getCodeGroup())) && (CTCat.TOP_COMP == CTCat.from(cc.getCategory())))
				lstTopComp.add(cc.getCodeValue1());
		}
		return lstTopComp;
	}
	
	public List<String> getMultiComp(){
		List<String> lstTopComp = new ArrayList<String>();
		for (CodeTableDTO cc : codeCache) {
			if((CTGroup.CORP == CTGroup.from(cc.getCodeGroup())) && (CTCat.MULTINATIONAL_COMP == CTCat.from(cc.getCategory())))
				lstTopComp.add(cc.getCodeValue1());
		}
		return lstTopComp;
	}
	
	public List<CodeTableDTO> getCodeListByCriteria(String cat, 
			Integer prodId, Integer prodGroup, Integer prodCat,
			Object[] objects ) {
		if (StringUtils.isNullOrEmpty(cat)) {
			System.out.println("Code Category cannot be null or empty !");
			return null;
		}
		if (codeCache == null) {
			initCache();
		}
		int pId = (prodId == null) ? 0 : prodId.intValue();
		int pGrp = (prodGroup == null) ? 0 : prodGroup.intValue();
		int pCat = (prodCat == null) ? 0 : prodCat.intValue();
		List<CodeTableDTO> list = getCodeByCategory(cat, pId, pGrp, pCat);
		return list;
	}
	
	public List<KioskDTO> getKosks() {
		if (codeCache == null) {
			initCache();
		}
		
		List<KioskDTO> kiosks = new ArrayList<>();
		Date now = DateUtil.getDateWithoutTime(new Date());
		for (CodeTableDTO cc : codeCache)
			if (cc.getCategory().equals(CTCat.TRAN_OFF.toString()) && 
					(cc.getStartEffDate() == null || !cc.getStartEffDate().after(now)) &&
					(cc.getEndEffDate() == null || !cc.getEndEffDate().before(now))) 
				kiosks.add(new KioskDTO(cc.getId(), cc.getCodeValue1(), cc.getDescription2(), cc.getDescription1()));
			
		return kiosks;
	}

	
}