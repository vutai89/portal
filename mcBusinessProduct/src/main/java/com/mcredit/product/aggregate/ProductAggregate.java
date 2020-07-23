package com.mcredit.product.aggregate;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.ValidationException;

import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.CodeTable;
import com.mcredit.data.product.entity.MappingHierarchy;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.CodeTableComboboxDTO;
import com.mcredit.model.dto.PagingDTO;
import com.mcredit.model.dto.ProdHisDetailsDTO;
import com.mcredit.model.dto.product.CommodityDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.enums.RecordStatus;
import com.mcredit.model.enums.product.ActionData;
import com.mcredit.model.enums.product.MapType;
import com.mcredit.model.enums.product.PageConfig;
import com.mcredit.model.enums.product.ProductCategory;
import com.mcredit.model.enums.product.ProductGroupCodeTable;
import com.mcredit.model.enums.product.SaleChannel;
import com.mcredit.model.enums.product.SaleChannelString;
import com.mcredit.product.dto.ComboboxDTO;
import com.mcredit.product.dto.CommonDTO;
import com.mcredit.product.dto.ProductDTO;
import com.mcredit.product.dto.ProductFileDTO;
import com.mcredit.product.dto.UploadFileComodityDTO;
import com.mcredit.product.utils.Constants;
import com.mcredit.product.utils.JSONFactory;
import com.mcredit.product.utils.ProductUtil;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.util.ModelMapperHelper;
import com.mcredit.util.StringUtils;

/** @author manhnt1.ho
 * @author manhnt1.ho
 * @since 06/2019
 */
public class ProductAggregate {

	private UnitOfWork _uok = null;
	private UserDTO _user = null;
	private static Integer channelIdCD = CodeTableCacheManager.getInstance().getBy(CTCodeValue1.PRD_CHANNEL_CD, CTCat.PROD_CHANNEL, CTGroup.PROD).getId();
	private static Integer channelIdTW = CodeTableCacheManager.getInstance().getBy(CTCodeValue1.PRD_CHANNEL_TW, CTCat.PROD_CHANNEL, CTGroup.PROD).getId();
	private static Integer productOtherId = CodeTableCacheManager.getInstance().getBy(CTCodeValue1.PRD_SC_GROUP_OTHER, CTCat.PROD_SC_GROUP).getId();
	private static Integer productAllId = CodeTableCacheManager.getInstance().getBy(CTCodeValue1.PRD_SC_GROUP_ALL, CTCat.PROD_SC_GROUP).getId();
	private static Integer highTech = CodeTableCacheManager.getInstance().getBy(CTCodeValue1.PRD_SC_GROUP_HT, CTCat.PROD_SC_GROUP).getId();
	private static Integer nonHighTech = CodeTableCacheManager.getInstance().getBy(CTCodeValue1.PRD_SC_GROUP_NHT, CTCat.PROD_SC_GROUP).getId();


	public ProductAggregate(UnitOfWork _uok, UserDTO user) {
		this._uok = _uok;
		_user = user;
	}

	/** 
	 * Tim kiem lich su phan trang server
	 * @author manhnt1.ho
	 * @param pageIndex
	 * @return Tim kiem lich su phan trang server
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Object getHisProdPaging(Integer pageIndex) throws Exception {
		PagingDTO pagingDTO = _uok.common.auditDataChangeRepo().getHisProdPaging(pageIndex, PageConfig.PageSize.value());
		List<ProdHisDetailsDTO> lstDto = (List<ProdHisDetailsDTO>) pagingDTO.getData();
		for(ProdHisDetailsDTO item : lstDto) {
			String actionName = item.getAction() !=null ? CodeTableCacheManager.getInstance().getbyID(item.getAction()).getDescription1() : "";
			item.setsAction(actionName);
		}
		pagingDTO.setData(lstDto);
		return pagingDTO;
	}

	/** 
	 * Tim kiem lich su phan trang server
	 * @author manhnt1.ho
	 * @param pageIndex
	 * @return Tim kiem lich su upload phan trang server
	 */
	public Object findHisUploadPading(Integer pageIndex) {
		Long uplTypeId = CodeTableCacheManager.getInstance().getIdBy(CTCodeValue1.PRD_UPL_TYPE_GOODS, CTCat.UPL_TYPE);
		return _uok.common.commondityRepository().getHisProdUploadPaging(pageIndex, PageConfig.PageSize.value(),uplTypeId);
	}
	
	/** 
	 * Tim kiem danh sach san pham theo cac tham so tim kiem
	 * @author manhnt1.ho
	 * @param categoryId
	 * @param schemeGroupId
	 * @param status
	 * @param productName
	 * @return Tim kiem danh sach san pham theo cac tham so tim kiem
	 */
	public Object findLstProductBy(Integer categoryId, String productName) {
		String channelString = categoryId == SaleChannel.CD.value() ? SaleChannelString.CD.value() : SaleChannelString.TW.value(); 
		List<ProductDTO> lstProduct = ModelMapperHelper.mapList(_uok.common.productTaskRepo().findLstProductBy(channelString, productName), ProductDTO.class);
		for(ProductDTO item: lstProduct){
			// Lay code table Product code 
			List<CodeTable> lstCodeProduct = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.PROD.value(), CTCat.PROD_CODE.value(), item.getProductCode(), null);
			if(lstCodeProduct == null || lstCodeProduct.size() == 0)
				continue;
			// Tim kiem Mapping de lay ra group product id
			List<CodeTableComboboxDTO> lstScheme = _uok.common.codeTableRepo().getByMapping(lstCodeProduct.get(0).getId(), MapType.Product_ProductGroup.value());
			String schemeGroupName = "";
			Long schemeGroupId = null; 
			for(CodeTableComboboxDTO itemCodeTable : lstScheme) {
				schemeGroupName+=itemCodeTable.getDescription1()+",";
				schemeGroupId =  itemCodeTable.getId();  
			}
			if(!StringUtils.isNullOrEmpty(schemeGroupName)) schemeGroupName = schemeGroupName.substring(0, schemeGroupName.length()-1);
			item.setSchemeGroupId(schemeGroupId);
			item.setSchemeGroupName(schemeGroupName);
		}
		return lstProduct;
	}

	/** 
	 * Tim kiem danh sach hang hoa, nhan hieu, mau ma
	 * @author manhnt1.ho
	 * @param commodityId
	 * @param brandId
	 * @param channelSaleId
	 * @param pageIndex
	 * @return Tim kiem danh sach hang hoa, nhan hieu, mau ma
	 */
	public PagingDTO findCommodityDetail(Integer commodityId, Integer brandId, Integer channelSaleId, Integer pageIndex) {
		Integer saleChannelId = channelSaleId == SaleChannel.CD.value() ? channelIdCD : channelIdTW ;
		return _uok.common.commondityRepository().findCommondityDetails(commodityId, brandId, saleChannelId.longValue(), pageIndex, PageConfig.PageSize.value());
	}
	
	public PagingDTO findCommodityDetailFull(Integer commodityId, Integer brandId, Integer channelSaleId, Integer pageIndex) {
		Integer saleChannelId = channelSaleId == SaleChannel.CD.value() ? channelIdCD : channelIdTW ;
		return _uok.common.commondityRepository().findCommondityDetailsFull(commodityId, brandId, saleChannelId.longValue(), pageIndex, PageConfig.PageSize.value());
	}
	
	public List<CodeTableComboboxDTO> findCodeTableByParentId(Integer productChannelId, String objectType, String status) {
		Integer channelId = productChannelId == SaleChannel.CD.value() ? channelIdCD : channelIdTW; 
		return _uok.common.codeTableRepo().findCommodityByChannel(channelId);
		//return ModelMapperHelper.mapList(lstCodeTable, CodeTableComboboxDTO.class);
	}

	/** 
	 * Tim kiem hang hoa, nhan hieu, mau ma khi thiet lap nhom san pham other
	 * @author manhnt1.ho
	 * @param productId
	 * @param objectType
	 * @param channelType
	 * @return Tim kiem hang hoa, nhan hieu, mau ma khi thiet lap nhom san pham other
	 */
	public List<CodeTableComboboxDTO> findCodeTableConfig(Integer productId, String objectType, Integer channelType) {
		String sMapType =   ProductCategory.Commodity.value().equals(objectType) ? MapType.SchemeCommTW.value() : MapType.SchemeBrandTW.value();
		// Tim kiem code Product 
		String productCode = this._uok.common.productTaskRepo().findProductById(productId.longValue()).getProductCode();
		List<CodeTable> lstCodeProduct = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.PROD.value(), CTCat.PROD_CODE.value(), productCode, null);
		List<CodeTableComboboxDTO> lstCodeTableDTO  = new ArrayList<>();
		// 1. Tim kiem hang hoa nhan hieu theo loai other
		if(lstCodeProduct.size() >0)		
			lstCodeTableDTO = _uok.common.codeTableRepo().findCodeTableConfig(lstCodeProduct.get(0).getId().intValue(), sMapType);
		// 2. Neu khong co nam trong day thi thuc hien get all hang hoa hoac
		// nhan hieu theo kenh ban
		if (lstCodeTableDTO.size() == 0) {
			List<CodeTable> lstCodeTable = new ArrayList<>();
			if (ProductCategory.Commodity.value().equals(objectType)) {
				if(SaleChannel.CD.value() == channelType)
					return _uok.common.codeTableRepo().findCommodityByChannel(channelIdCD); // chi dung cho CD
				else 
					return _uok.common.codeTableRepo().findCommodityByChannel(channelIdTW); // chi dung cho CD
			}
			else if (ProductCategory.Brand.value().equals(objectType))
				lstCodeTable = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.INST.value(), CTCat.BRAND.value(), objectType, null);
			return ModelMapperHelper.mapList(lstCodeTable, CodeTableComboboxDTO.class);
		} else
			return lstCodeTableDTO;
	}
	
	public List<CodeTable> findCodeTableDb(String codeGroup, String category, String status) throws Exception{
		return _uok.common.codeTableRepo().findCodeTableBy(codeGroup, category, status);
	}
	
	/** 
	 * Lay du lieu cho BPM. Du lieu truyen vao : productCode, GoodCode, BrandCode, Type
	 * @author manhnt1.ho
	 * @param obj
	 * @return Lay du lieu cho BPM. Du lieu truyen vao : productCode, GoodCode, BrandCode, Type
	 * @throws Exception
	 */
	public Object findDataForBPM(CommodityDTO obj) throws Exception {   
		// Lay du lieu theo type 
		_uok.clearCache();
		List<CodeTable> lstResult = new ArrayList<>();
		List<ComboboxDTO> lstCombobox = new ArrayList<>();
		// Lay thong tin tu bang product
		List<CodeTable> lstCodeProduct = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.PROD.value(), CTCat.PROD_CODE.value(), obj.getProductCode(), null);
		if (lstCodeProduct == null || lstCodeProduct.size() == 0)
			return lstResult;
		List<MappingHierarchy> lstMapProd_ProdGroup = _uok.common.mappingHierarchyRepository().findLstMappingHierarchy(lstCodeProduct.get(0).getId(), null, null, MapType.Product_ProductGroup.value());
		if (lstMapProd_ProdGroup == null || lstMapProd_ProdGroup.size() == 0)
			return lstResult;
		Long productId = lstCodeProduct.get(0).getId();  
		if(ProductCategory.Goods.value().equals(obj.getObjectType())) {
			// Chia kenh ban 
			if (obj.getProductCode().equals("E0000020") || obj.getProductCode().equals("E0000021")) {
				
				Long productGroupId = lstMapProd_ProdGroup.get(0).getMapId2();
				lstResult = _uok.common.commondityRepository().getComByPrdGroup(productGroupId, null);
				
				// Add Non High tech
				List<CodeTable> lstResult2 = _uok.common.commondityRepository().getComByPrdGroup(Long.valueOf(nonHighTech), null);
				
				lstResult.addAll(lstResult2);
			}
			else if (obj.getProductCode().indexOf("E") != -1) {
				
				Long productGroupId = lstMapProd_ProdGroup.get(0).getMapId2();
				if(productGroupId==productAllId.longValue())
					lstResult = ModelMapperHelper.mapList(_uok.common.codeTableRepo().findCommodityByChannel(channelIdCD), CodeTable.class) ;
				else if(productGroupId == productOtherId.longValue()) 
					lstResult = _uok.common.commondityRepository().getProComConfig(productId, null);
				else
					lstResult = _uok.common.commondityRepository().getComByPrdGroup(productGroupId, null);
			}
			else {
				for(MappingHierarchy item : lstMapProd_ProdGroup) {
					if(item.getMapId2()== productOtherId.longValue()) {
						lstResult = _uok.common.commondityRepository().getProComConfig(item.getMapId1(), null);
						break;
					} else {
						CodeTable codeTableTmp = _uok.common.codeTableRepo().getById(item.getMapId2());
						if(codeTableTmp== null) continue;
						lstResult.add(codeTableTmp);
					}
				}
				lstResult = ModelMapperHelper.mapList(lstResult, CodeTable.class);
				List<CodeTable> lstResultTmp = new ArrayList<>();
				for(CodeTable item2 : lstResult) {
					CodeTable codeTableTmp = new CodeTable(item2.getCodeGroup(),item2.getCategory(),item2.getCodeValue1(),item2.getDescription1());
					codeTableTmp.setStatus(item2.getStatus());
					System.out.println("===> VALUE CODE_TABLE :" + JSONFactory.toJson(item2));
					switch (item2.getCodeValue1()) {
					case "TW_XMD":{
							codeTableTmp.setCodeValue1("GOODS47_XMD");
						break;
					}
					case "TW_XDD":   
						codeTableTmp.setCodeValue1("GOODS46_XDD");
						break;
					case "TW_XM":{
						if(obj.getProductCode().equals("M0000001") || obj.getProductCode().equals("M0000003"))
							codeTableTmp.setCodeValue1("GOODS45_XM_EXTEND");
						else
							codeTableTmp.setCodeValue1("GOODS45_XM");
						break;
					}
					default:
						break;
					}
					lstResultTmp.add(codeTableTmp);
				}
				lstResult =lstResultTmp;
			}
			lstResult = ModelMapperHelper.mapList(lstResult, CodeTable.class);
		} else if(ProductCategory.Brand.value().equals(obj.getObjectType())) {
			// Xu ly doi voi dau vao TW
			if("GOODS45_XM_EXTEND".equals(obj.getCommodityCode()) || "GOODS47_XMD".equals(obj.getCommodityCode()) || "GOODS46_XDD".equals(obj.getCommodityCode()) || "GOODS45_XM".equals(obj.getCommodityCode())){
				switch (obj.getCommodityCode()) {
				case "GOODS45_XM_EXTEND":
						obj.setCommodityCode("TW_XM");
					break;
				case "GOODS47_XMD":
					obj.setCommodityCode("TW_XMD");
				break;
				case "GOODS46_XDD":
					obj.setCommodityCode("TW_XDD");
					break;
				case "GOODS45_XM":
					obj.setCommodityCode("TW_XM");
					break;
				default:
					break;
				}
			}	
			// Lay Id COMM
			obj.setObjectType(ProductCategory.Commodity.value());
			CodeTable codeTableComm = findSingleCodeTable(obj);
			// Lay du lieu
			System.out.println("payload => " + JSONFactory.toJson(obj));
			// Kiem tra xem san pham co thuoc nhom other khong. Neu co thi chi lay cac nhan hieu orther
			List<MappingHierarchy> listOther = lstMapProd_ProdGroup.stream().filter(item -> item.getMapId2()== productOtherId.longValue()).collect(Collectors.toList());
			System.out.println("listOther => " + JSONFactory.toJson(listOther));
			List<CodeTableComboboxDTO> lstCodeTableCombo = new ArrayList<>();
			if(listOther.size() > 0 )
				lstCodeTableCombo = _uok.common.commondityRepository().getProComBrandConfig(productId, codeTableComm.getId());
			else 
				lstCodeTableCombo = _uok.common.commondityRepository().getCodeTableMapId2ById1(codeTableComm.getId(), MapType.CommodityBrand.value(),null); 
			lstResult = ModelMapperHelper.mapList(lstCodeTableCombo, CodeTable.class);
		} else if(ProductCategory.Model.value().equals(obj.getObjectType())) {
				// Xu ly doi voi dau vao TW
				if ("GOODS45_XM_EXTEND".equals(obj.getCommodityCode()) || "GOODS47_XMD".equals(obj.getCommodityCode()) || "GOODS46_XDD".equals(obj.getCommodityCode())
						|| "GOODS45_XM".equals(obj.getCommodityCode())) {
					switch (obj.getCommodityCode()) {
					case "GOODS45_XM_EXTEND":
						obj.setCommodityCode("TW_XM");
						break;
					case "GOODS47_XMD":
						obj.setCommodityCode("TW_XMD");
						break;
					case "GOODS46_XDD":
						obj.setCommodityCode("TW_XDD");
						break;
					case "GOODS45_XM":
						obj.setCommodityCode("TW_XM");
						break;
					default:
						break;
					}
				}
				// Lay Id COMM
				obj.setObjectType(ProductCategory.Commodity.value());
				CodeTable codeTableComm = findSingleCodeTable(obj);
				// Lay Id brand
				obj.setObjectType(ProductCategory.Brand.value());
				CodeTable codeTableBrand = findSingleCodeTable(obj);
				if(codeTableComm == null || codeTableBrand == null ) lstResult = new ArrayList<>();
				else {
					MappingHierarchy _mapComBrand = _uok.common.mappingHierarchyRepository().findMappingHierarchy(codeTableComm.getId(), codeTableBrand.getId(), null,MapType.CommodityBrand.value());
					if(_mapComBrand == null)
						lstResult = new ArrayList<>();
					else {
						List<CodeTableComboboxDTO> lstCodeTableCombo = _uok.common.commondityRepository().getModelForBpm(_mapComBrand.getId(), null); 
						lstResult = ModelMapperHelper.mapList(lstCodeTableCombo, CodeTable.class);
					}
				}
			}
			for(CodeTable item : lstResult) {
				ComboboxDTO cb = new ComboboxDTO(item.getCodeValue1(), item.getDescription1(), item.getStatus());
				lstCombobox.add(cb);
			}
			lstCombobox = lstCombobox.stream().filter(item -> !item.getStatus().equals(ActionData.Delete.value())).collect(Collectors.toList());
			return lstCombobox;
	}

	/** 
	 * Tim kiem 1 ban ghi code table duy nhat theo loai san pham la : COM, BRAND< MOD
	 * @author manhnt1.ho
	 * @param obj
	 * @return Tim kiem 1 ban ghi code table duy nhat theo loai san pham la : COM, BRAND< MOD
	 * @throws Exception
	 */
	public CodeTable findSingleCodeTable(CommodityDTO obj) throws Exception {
		String codeInsert = "";
		if (obj.getObjectType().equals(ProductCategory.Commodity.value())) {
			codeInsert = obj.getCommodityCode();
		} else if (obj.getObjectType().equals(ProductCategory.Brand.value()))
			codeInsert = obj.getBrandCode();
		else
			codeInsert = obj.getModelCode();
		List<CodeTable> lstCodeTableCheck = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.INST.value(), obj.getObjectType(), codeInsert, null);
		if (lstCodeTableCheck.size() > 0)
			return lstCodeTableCheck.get(0);   
		return null;
	}

	/** 
	 * Tim kiem hang hoa, nhan hieu, mau ma khi chon nhom san pham la other
	 * @author manhnt1.ho
	 * @param productId
	 * @return Tim kiem hang hoa, nhan hieu, mau ma khi chon nhom san pham la other
	 */
	public List<MappingHierarchy> findProductConfig(Long productId) {
		List<MappingHierarchy> lstPrdComBrand = new ArrayList<>();
		String productCode = _uok.common.productTaskRepo().findProductById(productId).getProductCode();
		List<CodeTable> lstCodeProduct = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.PROD.value(), CTCat.PROD_CODE.value(), productCode, null);
		Long idProductCodeTable = new Long(0);
		if(lstCodeProduct == null || lstCodeProduct.isEmpty()) return lstPrdComBrand;
		else {
			idProductCodeTable = lstCodeProduct.get(0).getId();
			List<MappingHierarchy> lstPrdComm = _uok.common.mappingHierarchyRepository().findLstMappingHierarchy(idProductCodeTable, null, null,  MapType.ProductCommodity.value());
			
			for (MappingHierarchy item : lstPrdComm) {
				List<MappingHierarchy> lstPrdTmp = _uok.common.mappingHierarchyRepository().findLstMappingHierarchy(null, null, item.getId(), MapType.ProductCommodityBrand.value());
				lstPrdComBrand.addAll(lstPrdTmp);
			}
		}
		
		return lstPrdComBrand;
	}

	/** 
	 * Lay duong dan file download
	 * @author manhnt1.ho
	 * @param uplMasterId
	 * @param loginId
	 * @return Lay duong dan file download
	 * @throws Exception
	 */
	public String getFilePath(Long uplMasterId, String loginId) throws Exception{
		return _uok.common.uplDetailRepo().findUplDetailbyMaterID(uplMasterId).getServerFileName();
	}
	
	/** 
	 * Them moi hang hoa, nhan hieu theo lo
	 * @author manhnt1.ho
	 * @param lstCommodityObj
	 * @return Them moi hang hoa, nhan hieu theo lo
	 * @throws Exception
	 */
	public List<CommodityDTO> insertLstCommodity(File fileUpload, String payload, String fullName) throws Exception {
		// 1. Conver payload => Object
		UploadFileComodityDTO uploadDTO = (UploadFileComodityDTO) JSONFactory.fromJSON(payload, UploadFileComodityDTO.class);
		// 2. Thuc hien luu file
		ProductFileDTO writeFileDTO = ProductUtil.writeFile(fileUpload, uploadDTO.getFileName(), Constants.FILE_EXTENDED_EXCEL);
		List<CommodityDTO> lstCommodity = uploadDTO.getLstCommodity();
		if (writeFileDTO != null) {
			// 3. Thuc hien luu thong tin hang hoa, nhan hieu, mau ma
			String type = uploadDTO.getType();
			for (CommodityDTO item : lstCommodity) {
				if (StringUtils.isNullOrEmpty(type)) insertCommodity(item, fullName, false);
				else updateCommodity(item, type, uploadDTO.getCategory());
			}
			// 4. Ghi log : Dang bo lai do chua co bang de ghi
			Long idUplType = CodeTableCacheManager.getInstance().getIdBy(CTCodeValue1.PRD_UPL_TYPE_GOODS, CTCat.UPL_TYPE);
			// 4.1. Ghi file vao uplMaster
			UplMaster uplMaster = new UplMaster(RecordStatus.ACTIVE.value(), new Date(), new Date(), ".xlsx", new Long(0), ProductUtil.getDateTimeString(), idUplType.intValue(), new Long(1), new Long(0));
			uplMaster.setCreatedBy(fullName);
			_uok.common.uplMasterRepo().add(uplMaster);
			// 4.2. Ghi vao uplDetails sau do ghi vao log
			if (uplMaster.getId() != null && uplMaster.getId() > 0) {
				UplDetail uplDetail = new UplDetail(RecordStatus.ACTIVE.value(), new Date(), new Date(), 1, 1, 0, LeadGenEnums.UPL_STATUS.value(), uplMaster.getId());
				uplDetail.setServerFileName(writeFileDTO.getFileDir());
				uplDetail.setUplFileName(writeFileDTO.getFileName());
				_uok.common.uplDetailRepo().add(uplDetail);
			}
		} else throw new ValidationException(Messages.getString("validation.field.mainMessage", Messages.getString("ts.no.record.inserted")));
		return lstCommodity;
	}

	/** 
	 * Chuc nang cap nhat hang hoa, nhan hieu, mau ma
	 * @author manhnt1.ho
	 * @param commodityObj
	 * @return Chuc nang cap nhat hang hoa, nhan hieu, mau ma
	 * @throws Exception
	 */
	public void updateCommodity(CommodityDTO commodityObj, String type, String category) throws Exception {
		if (ProductCategory.Model.value().equals(category)) updateModel(commodityObj, type);
		else updateCommBrand(commodityObj, type);
	}

	/** 
	 * thuc hien update hang hoa, nhan hieu
	 * @author manhnt1.ho
	 * @param commodityObj
	 * @param type
	 * @throws Exception
	 * @return thuc hien update hang hoa, nhan hieu
	 */
	public void updateCommBrand(CommodityDTO commodityObj, String type) throws Exception {
		// 1. Tim kiem ban ghi can update trong db
		CodeTable codeTableObj = findSingleCodeTable(commodityObj);
		if (codeTableObj == null)
			throw new ValidationException(Messages.getString("lable.prd.message.err.not_exits.record"), Messages.getString("lable.prd.message.err.not_exits.record"));
		// 2. Neu type = U => Cap nhat tÃªn má»›i. Neu type = A, I, D => Cap nhat
		// trang thai cot status
		if (type.equals(ActionData.Update.value()))
			codeTableObj.setDescription1(commodityObj.getNameNew());
		else if (type.equals(ActionData.Active.value()) || type.equals(ActionData.InActive.value()) || type.equals(ActionData.Delete.value()))
			codeTableObj.setStatus(type);
		_uok.common.codeTableRepo().update(codeTableObj);
	}

	/** 
	 * Thuc hien update mau ma
	 * @author manhnt1.ho
	 * @param commodityObj
	 * @param type
	 * @throws Exception
	 * @return Thuc hien update mau ma
	 */
	public void updateModel(CommodityDTO commodityObj, String type) throws Exception {
		// Doi voi truong hop update la model. Can tach case ra nhu sau :
		// 1. Doi voi truong hop cap nhat. Bat buoc nhap CODE moi, Ten moi =>
		// Sinh ban ghi moi trong code table
		if (type.equals(ActionData.Update.value())) {
			// Kiem tra code moi co bi trung trong code table ko. Neu co thi bao
			// loi luon
			List<CodeTable> lstCodeTableCheck = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.INST.value(), CTCat.MODEL.value(), commodityObj.getCodeNew(), null);
			if (lstCodeTableCheck.size() > 0)
				throw new ValidationException(Messages.getString("lable.prd.message.err.duplicate.code.mod"), Messages.getString("lable.prd.message.err.duplicate_record.code"));
			else {
				commodityObj.setObjectType(ProductCategory.Commodity.value());
				CodeTable codeTableComm = findSingleCodeTable(commodityObj);
				commodityObj.setObjectType(ProductCategory.Brand.value());
				CodeTable codeTableBrand = findSingleCodeTable(commodityObj);
				commodityObj.setObjectType(ProductCategory.Model.value());
				CodeTable codeTableModel = findSingleCodeTable(commodityObj);
				if (codeTableComm == null || codeTableBrand == null || codeTableModel == null)
					throw new ValidationException(Messages.getString("lable.prd.message.err.not_exits.comm_brand_mod"), Messages.getString("lable.prd.message.err.duplicate_record.code"));
				// sinh code table moi
				CommodityDTO commodityCreate = commodityObj;
				commodityCreate.setObjectType(ProductCategory.Model.value());
				commodityCreate.setModelCode(commodityObj.getCodeNew());
				commodityCreate.setModelName(commodityObj.getNameNew());
				CodeTable codeTableCreate = saveCodeTable(commodityCreate);
				// Tim mapping comm - brand
				MappingHierarchy mappingCommBrand = _uok.common.mappingHierarchyRepository().findMappingHierarchy(codeTableComm.getId(), codeTableBrand.getId(), null, MapType.CommodityBrand.value());
				if (mappingCommBrand == null)
					throw new ValidationException(Messages.getString("lable.prd.message.err.not_exits.map_comm_brand"), Messages.getString("lable.prd.message.err.duplicate_record.code"));
				// Tim mapping comm-brand-mod
				MappingHierarchy mappingCommBrandModel = _uok.common.mappingHierarchyRepository().findMappingHierarchy(codeTableBrand.getId(), codeTableModel.getId(), mappingCommBrand.getId(),
						MapType.CommodityBrandModel.value());
				if (mappingCommBrandModel == null)
					throw new ValidationException(Messages.getString("lable.prd.message.err.not_exits.map_comm_brand_mod"), Messages.getString("lable.prd.message.err.duplicate_record.code"));
				// Set Id moi cho lien ket comm - brand - mod
				mappingCommBrandModel.setMapId2(codeTableCreate.getId());
				// update
				_uok.common.mappingHierarchyRepository().update(mappingCommBrandModel);
			}
		} else {
			// 2. Truong hop update trang thai : Active, Inactive, Delete =>
			// Update thang tren bang mapping type -> COMM _BRAND_MODEL
			commodityObj.setObjectType(ProductCategory.Commodity.value());
			CodeTable codeTableComm = findSingleCodeTable(commodityObj);
			commodityObj.setObjectType(ProductCategory.Brand.value());
			CodeTable codeTableBrand = findSingleCodeTable(commodityObj);
			commodityObj.setObjectType(ProductCategory.Model.value());
			CodeTable codeTableModel = findSingleCodeTable(commodityObj);
			if (codeTableComm == null || codeTableBrand == null || codeTableModel == null)
				throw new ValidationException(Messages.getString("lable.prd.message.err.not_exits.comm_brand_mod"), Messages.getString("lable.prd.message.err.duplicate_record.code"));
			// Tim mapping comm - brand
			MappingHierarchy mappingCommBrand = _uok.common.mappingHierarchyRepository().findMappingHierarchy(codeTableComm.getId(), codeTableBrand.getId(), null, MapType.CommodityBrand.value());
			if (mappingCommBrand == null)
				throw new ValidationException(Messages.getString("lable.prd.message.err.not_exits.map_comm_brand"), Messages.getString("lable.prd.message.err.duplicate_record.code"));
			// Tim mapping comm-brand-mod
			MappingHierarchy mappingCommBrandModel = _uok.common.mappingHierarchyRepository().findMappingHierarchy(codeTableBrand.getId(), codeTableModel.getId(), mappingCommBrand.getId(),
					MapType.CommodityBrandModel.value());
			if (mappingCommBrandModel == null)
				throw new ValidationException(Messages.getString("lable.prd.message.err.not_exits.map_comm_brand_mod"), Messages.getString("lable.prd.message.err.duplicate_record.code"));
			mappingCommBrandModel.setStatus(type);
			_uok.common.mappingHierarchyRepository().update(mappingCommBrandModel);
		}
		// return commodityObj;
	}

	/**
	 * Tra ve Id loai hanh dong
	 * @author manhnt1.ho
	 * @param sCode
	 * @return Tra ve Id loai hanh dong
	 */
	public Long mappingTypeToAction(String sCode) {
		Long result = new Long(0);
		if(sCode.equals(ActionData.Create.value())) 
			result = ProductUtil.getIdCodeTableFromCache(CTCodeValue1.PRD_AUDIT_CREATE, CTCat.PROD_AUDIT_ACTION);
		else if(sCode.equals(ActionData.Update.value())) 
			ProductUtil.getIdCodeTableFromCache(CTCodeValue1.PRD_AUDIT_UPDATE, CTCat.PROD_AUDIT_ACTION);
		else if(sCode.equals(ActionData.Active.value())) 
			result = ProductUtil.getIdCodeTableFromCache(CTCodeValue1.PRD_AUDIT_ACTIVE, CTCat.PROD_AUDIT_ACTION);
		else if(sCode.equals(ActionData.InActive.value())) 
			result = ProductUtil.getIdCodeTableFromCache(CTCodeValue1.PRD_AUDIT_INACTIVE, CTCat.PROD_AUDIT_ACTION);
		else if(sCode.equals(ActionData.Delete.value())) 
			result = ProductUtil.getIdCodeTableFromCache(CTCodeValue1.PRD_AUDIT_DELETE, CTCat.PROD_AUDIT_ACTION);
		return result;
	}

	/** 
	 * Chuc nang them moi hang hoa, nhan hieu, mau ma
	 * @author manhnt1.ho
	 * @param commodityObj
	 * @return Chuc nang them moi hang hoa, nhan hieu, mau ma
	 * @throws Exception
	 */
	public CodeTable insertCommodity(CommodityDTO commodityObj, String fullName, boolean insertLog) throws Exception {
		return commodityObj.getObjectType().equals(ProductCategory.Model.value()) ? insertModel(commodityObj) : saveCodeTable(commodityObj);
	}

	/** 
	 * Ham nay dung de them nhom san pham duoc cau hinh dac biet (
	 *         Product Group : Other )
	 * @author manhnt1.ho
	 * @param commodityObj
	 * @return Ham nay dung de them nhom san pham duoc cau hinh dac biet (
	 *         Product Group : Other )
	 * @throws Exception
	 */
	public List<CommodityDTO> insertProductConfig(List<CommodityDTO> lstCommodityObj, String fullName) throws Exception {
		HashMap<String, MappingHierarchy> hmPrCommInsertSuccess = new HashMap<String, MappingHierarchy>();
		String productCode = _uok.common.productTaskRepo().findProductById(lstCommodityObj.get(0).getProductId()).getProductCode();
		List<CodeTable> lstCodeProduct = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.PROD.value(), CTCat.PROD_CODE.value(), productCode, null);
		Long idProductCodeTable = new Long(0);
		if(lstCodeProduct == null || lstCodeProduct.isEmpty())  {
			CodeTable codeTable = new CodeTable(CTGroup.PROD.value(), CTCat.PROD_CODE.value(),productCode, productCode);
			_uok.common.codeTableRepo().insertTypeCommodity(codeTable, fullName);
			idProductCodeTable = codeTable.getId();
		} else idProductCodeTable = lstCodeProduct.get(0).getId();
		// 1. Delete toan bo cap pro-comm, pro-com-brand
		_uok.common.mappingHierarchyRepository().deletePrdComQuery(idProductCodeTable);
		_uok.common.mappingHierarchyRepository().deletePrdComBrandQuery(idProductCodeTable);
		// 2. Thuc hien insert
		for (CommodityDTO item : lstCommodityObj) {
			// Thay doi luong code. Ko can kiem tra cap product - comm nua. Kiem
			// tra xem cap product - comm da duoc insert truoc do hay chua
			String sMapPrComm = idProductCodeTable + "_" + item.getCommodityId();
			MappingHierarchy itemPrCommInsertSuccess = hmPrCommInsertSuccess.get(sMapPrComm);
			// Neu co thi khong insert them cap pro- comm nua
			if (itemPrCommInsertSuccess != null) {
				Long parentId = itemPrCommInsertSuccess.getId();
				// Insert cap product - comm - brand
				MappingHierarchy itemInsert = new MappingHierarchy(item.getCommodityId(), item.getBrandId(), parentId, MapType.ProductCommodityBrand.value());
				_uok.common.mappingHierarchyRepository().insert(itemInsert, _user.getLoginId());
			} else {
				// neu chua co thi Insert cap product - comm
				MappingHierarchy mapHierarchy = new MappingHierarchy(idProductCodeTable, item.getCommodityId(), null, MapType.ProductCommodity.value());
				_uok.common.mappingHierarchyRepository().insert(mapHierarchy,_user.getLoginId());
				hmPrCommInsertSuccess.put(sMapPrComm, mapHierarchy);
				Long parentId = mapHierarchy.getId();
				// Insert cap product - comm - brand
				MappingHierarchy itemInsert = new MappingHierarchy(item.getCommodityId(), item.getBrandId(), parentId, MapType.ProductCommodityBrand.value());
				_uok.common.mappingHierarchyRepository().insert(itemInsert,_user.getLoginId());
			}
		}
		// 3.Sau khi Ä‘Ã£ thá»±c hiá»‡n xong thÃ¬ thá»±c hiá»‡n update scheme nhÃ³m sáº£n pháº©m
		// vá»� other
		// Xoa bo moi ban ghi trong bang Mapping Hierarchy theo id san pham 
		_uok.common.mappingHierarchyRepository().deleteById1AndType(idProductCodeTable, MapType.Product_ProductGroup.value());
		MappingHierarchy itemSearh = new MappingHierarchy(idProductCodeTable, productOtherId.longValue(), null, MapType.Product_ProductGroup.value());
		_uok.common.mappingHierarchyRepository().insert(itemSearh,_user.getLoginId());
		return lstCommodityObj;
	}

	/** 
	 * Them moi doi voi hang hoa, nhan hieu
	 * @author manhnt1.ho
	 * @param obj
	 * @return Them moi doi voi hang hoa, nhan hieu
	 * @throws Exception
	 */
	public CodeTable saveCodeTable(CommodityDTO obj) throws Exception {
		String codeInsert = "";
		String description1Insert = "";
		Integer productGroupId = 0;
		Integer productChannel = obj.getProductChannelId() == SaleChannel.CD.value() ? channelIdCD : channelIdTW;
		if (obj.getObjectType().equals(ProductCategory.Commodity.value())) {
			codeInsert = obj.getCommodityCode();
			description1Insert = obj.getCommodityName();
			productGroupId = obj.getProductGroupId();
		} else if (obj.getObjectType().equals(ProductCategory.Brand.value())) {
			codeInsert = obj.getBrandCode();
			description1Insert = obj.getBrandName();
		} else {
			codeInsert = obj.getModelCode();  
			description1Insert = obj.getModelName();  
		}    
		CodeTable item = new CodeTable(ProductGroupCodeTable.COMMODITY.value(), obj.getObjectType(), codeInsert, description1Insert);
		// Kiem tra trung code, hoac name thi khong cho insert
		List<CodeTable> lstCodeTableCheck = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.INST.value(),obj.getObjectType(), codeInsert, null);
		if (lstCodeTableCheck.size() > 0)
			throw new ValidationException(Messages.getString("lable.prd.message.err.duplicate.record"), Messages.getString("lable.prd.message.err.duplicate_record.code") + '-' + codeInsert);
		else{
			_uok.common.codeTableRepo().insertTypeCommodity(item,_user.getLoginId());
			// Neu la insert COMM thi thuc hien insert 1 lien ket vao trong mapping ten type : PRDGROUP_COMM, 1 lien ket mapping voi type : MAP_COMM_CHANNEL
			if (obj.getObjectType().equals(ProductCategory.Commodity.value())) {
				MappingHierarchy mapping = new MappingHierarchy(item.getId(),productGroupId.longValue(), null, MapType.Comm_ProductGroup.value());
				_uok.common.mappingHierarchyRepository().insert(mapping,_user.getLoginId());
				MappingHierarchy mapping2 = new MappingHierarchy(item.getId(),productChannel.longValue(), null, MapType.Comm_Channel.value());
				_uok.common.mappingHierarchyRepository().insert(mapping2,_user.getLoginId());
			}
		}
		return item;
	}

	/**
	 * Them moi mau ma
	 *  @author manhnt1.ho
	 * @param obj
	 * @return Them moi mau ma
	 * @throws Exception
	 */
	public CodeTable insertModel(CommodityDTO obj) throws Exception {
		int codeCheck = 0;
		String sMessage = "";
		// Kiem tra hang hoa, nhan hieu co ton tai hay khong
		List<CodeTable> lstCheckComm = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.INST.value(), ProductCategory.Commodity.value(), obj.getCommodityCode(), "");
		if (lstCheckComm.size() == 0)
			codeCheck += 1;
		List<CodeTable> lstCheckBrand = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.INST.value(), ProductCategory.Brand.value(), obj.getBrandCode(), "");
		if (lstCheckBrand.size() == 0)
			codeCheck += 2;
		if (codeCheck > 0) {
			sMessage = codeCheck == 1 ? Messages.getString("lable.prd.message.err.not_exits.comm")
					: (codeCheck == 2 ? Messages.getString("lable.prd.message.err.not_exits.brand") : Messages.getString("lable.prd.message.err.not_exits.comm") + "." + Messages.getString("lable.prd.message.err.not_exits.brand"));
			throw new ValidationException(sMessage, Messages.getString("lable.prd.message.err.duplicate_record.code"));
		} 
		// Kiem tra model xem da ton tai chua. Neu da ton tai thi lay gia tri va
		// thuc hien tiep cac cong viec tiep theo. Neu chua co thi insert vao
		// codetable
		List<CodeTable> lstCheckModel = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.INST.value(), ProductCategory.Model.value(), obj.getModelCode(), "");
		CodeTable codeTableInsert = new CodeTable();
		if (lstCheckModel.size() > 0)
			codeTableInsert = lstCheckModel.get(0);   
		else
			codeTableInsert = this.saveCodeTable(obj);
		// 2. Kiem tra xem da co map giua comm - brand trong mapping-hir...
		MappingHierarchy mapHierarchy = _uok.common.mappingHierarchyRepository().findMappingHierarchy(lstCheckComm.get(0).getId(), lstCheckBrand.get(0).getId(), null, MapType.CommodityBrand.value());
		if (mapHierarchy != null) {
			// Kiem tra xem co mapping giua comm - brand-model hay chua. neu co roi thi dua ra thong bao rang da ton tai
			MappingHierarchy mapComBrandMod = _uok.common.mappingHierarchyRepository().findMappingHierarchy(mapHierarchy.getMapId2(), codeTableInsert.getId(), mapHierarchy.getId(), MapType.CommodityBrandModel.value());
			if(mapComBrandMod !=null) 
				throw new ValidationException(Messages.getString("lable.prd.message.err.duplicate.code.mod"));
			// thuc hien tao mapping comm - brand-model
			MappingHierarchy mapHierarchyModel = new MappingHierarchy(lstCheckBrand.get(0).getId(), codeTableInsert.getId(), mapHierarchy.getId(), MapType.CommodityBrandModel.value());
			_uok.common.mappingHierarchyRepository().insert(mapHierarchyModel,_user.getLoginId());
		} else {
			// 3. Neu chua co tao 1 mapping giua comm - brand
			MappingHierarchy mapHierarchyCommBrand = new MappingHierarchy(lstCheckComm.get(0).getId(), lstCheckBrand.get(0).getId(), null,MapType.CommodityBrand.value());
			_uok.common.mappingHierarchyRepository().insert(mapHierarchyCommBrand,_user.getLoginId());
			// 4. Tao 1 mapping giua comm - brand - model
			MappingHierarchy mapHierarchyModel = new MappingHierarchy(lstCheckBrand.get(0).getId(), codeTableInsert.getId(), mapHierarchyCommBrand.getId(), MapType.CommodityBrandModel.value());
			_uok.common.mappingHierarchyRepository().insert(mapHierarchyModel,_user.getLoginId());
		}
		return codeTableInsert;
	}

	/** 
	 * Xoa nhom san pham - Quan ly scheme san pham
	 * @author manhnt1.ho
	 * @param id
	 * @param fullName
	 * @return Xoa nhom san pham - Quan ly scheme san pham
	 * @throws Exception
	 */
	public CommonDTO deleteProductGroup(CommonDTO item, String fullName) throws Exception {
		// Xoa bo moi ban ghi trong bang Mapping Hierarchy theo id san pham 
		String productCode = _uok.common.productTaskRepo().findProductById(item.getiValue1().longValue()).getProductCode();
		List<CodeTable> lstCodeProduct = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.PROD.value(), CTCat.PROD_CODE.value(), productCode, null);
		if(lstCodeProduct.size() > 0)  _uok.common.mappingHierarchyRepository().deleteById1AndType(lstCodeProduct.get(0).getId(), MapType.Product_ProductGroup.value()); 
		return item;
	}

	/** 
	 * Them moi nhom san pham High tech, non - high tech, all, other
	 * @author manhnt1.ho
	 * @param CommonDTO item
	 * @return Them moi nhom san pham High tech, non - high tech, all, other
	 */
	public CommonDTO updateSchemeGroup(CommonDTO item, String fullName) throws Exception {
		String productCode = _uok.common.productTaskRepo().findProductById(item.getiValue1().longValue()).getProductCode();
		List<CodeTable> lstCodeProduct = _uok.common.codeTableRepo().findCodeTableBy(CTGroup.PROD.value(), CTCat.PROD_CODE.value(), productCode, null);
		Long idProductCodeTable = new Long(0);
		if(lstCodeProduct.size() > 0)  idProductCodeTable= lstCodeProduct.get(0).getId();
		else {
			CodeTable codeTable = new CodeTable(CTGroup.PROD.value(), CTCat.PROD_CODE.value(),productCode, productCode);
			_uok.common.codeTableRepo().insertTypeCommodity(codeTable, fullName);
			idProductCodeTable = codeTable.getId();
		}
		// 1. Xem san pham la CD, hay TW. Neu la CD thi chi duoc insert 1 ban ghi vao mapping. Neu TW thi dc insert nhieu
		if(item.getsValue1().equals(SaleChannel.CD.value().toString())) {			
			// Tim kiem mapping xem da co chua ( id1 : prd - prdgroup) 
			MappingHierarchy itemSearh = _uok.common.mappingHierarchyRepository().findMappingHierarchy(idProductCodeTable, null, null, MapType.Product_ProductGroup.value());
			if(itemSearh!=null) {
				itemSearh.setMapId2(item.getiValue2().longValue());
				_uok.common.mappingHierarchyRepository().update(itemSearh);
			} else { 
				// Tao moi 
				itemSearh = new MappingHierarchy(idProductCodeTable, item.getiValue2().longValue(), null, MapType.Product_ProductGroup.value());
				_uok.common.mappingHierarchyRepository().insert(itemSearh,_user.getLoginId());
			}
		} else {
			// Tim kiem mapping xem da co chua ( id1 : prd - prdgroup) 
			MappingHierarchy itemSearh = _uok.common.mappingHierarchyRepository().findMappingHierarchy(idProductCodeTable, item.getiValue2().longValue(), null, MapType.Product_ProductGroup.value());
			// Truong hop tim thay ban ghi trong DB thi khong thuc hien ghi them nua. Ghi them chi khi khong tim thay
			if(itemSearh==null) {
				itemSearh = new MappingHierarchy(idProductCodeTable, item.getiValue2().longValue(), null, MapType.Product_ProductGroup.value());
				_uok.common.mappingHierarchyRepository().insert(itemSearh,_user.getLoginId());
			}
		}
		return item;
	}
	
}