package com.mcredit.service.product;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mcredit.model.dto.product.CommodityDTO;
import com.mcredit.model.enums.ServiceName;
import com.mcredit.product.dto.CommonDTO;
import com.mcredit.product.manager.ProductManager;
import com.mcredit.service.BasedService;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/** 
 * @author manhnt1.ho
 * @since 06/2019
 */
@Path("/v1.0/product")
public class ProductService extends BasedService {
	public ProductService(@Context HttpHeaders headers) {
		super(headers);
	}

	/** 
	 * Lay sach sach lich su co phan trang
	 * @author manhnt1.ho
	 * @param pageIndex
	 * @return Lay sach sach lich su details co phan trang
	 * @throws Exception
	 */
	@GET
	@Path("/history/details")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHisDetails(@QueryParam("pageIndex") Integer pageIndex) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_HISTORY,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.getHisProdPaging(pageIndex));
			}
		});
	}

	/** 
	 * Lay sach sach lich su co phan trang
	 * @author manhnt1.ho
	 * @param pageIndex
	 * @return Lay sach sach lich su upload co phan trang
	 * @throws Exception
	 */
	@GET
	@Path("/history/uploads")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHisUpload(@QueryParam("pageIndex") Integer pageIndex) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_HISTORY,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.findHisUploadPading(pageIndex));
			}
		});
	}
	
	/** 
	 * Lay danh sach san pham khi chon nhom san pham other
	 * @author manhnt1.ho
	 * @param productId
	 * @return Lay danh sach san pham khi chon nhom san pham other
	 * @throws Exception
	 */
	@GET
	@Path("/find/lst_product_config")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLstProductConfig(@QueryParam("productId") Long productId) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_LST_PRD_CONFIG,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.findProductConfig(productId));
			}
		});
	}

	/** 
	 * Danh sach san pham theo tham so tim kiem
	 * @author manhnt1.ho
	 * @param categoryId
	 * @param schemeGroupId
	 * @param status
	 * @param productName
	 * @return Danh sach san pham theo tham so tim kiem
	 * @throws Exception
	 */
	@GET
	@Path("/find/lst_product_by")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLstProductBy(@QueryParam("categoryId") Integer categoryId,@QueryParam("productName") String productName) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_LST_PRD_BY,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.findLstProductBy(categoryId, productName));
			}
		});
	}
	
	@GET
	@Path("/find/code_table_by_parent")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCodeTableByParent(@QueryParam("productChannelId") Integer productChannelId, @QueryParam("objectType") String objectType,
			@QueryParam("status") String status) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_CODE_TABLE_BY,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.findCodeTableByParent(productChannelId, objectType, status));
			}
		});
	}

	/** 
	 * Danh sach hang hoa, nhan hieu theo nhom san pham other
	 * @author manhnt1.ho
	 * @param productId
	 * @param objectType
	 * @param channelType
	 * @return Danh sach hang hoa, nhan hieu theo nhom san pham other
	 * @throws Exception
	 */
	@GET
	@Path("/find/code_table_by_config")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCodeTableByConfig(@QueryParam("productId") Integer productId, @QueryParam("objectType") String objectType, @QueryParam("channelType") Integer channelType) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_CODE_TABLE_BY_CONFIG,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.findCodeTableConfig(productId, objectType, channelType));
			}
		});

	}

	/** 
	 * @return Danh sach hang hoa, nhan hieu, mau ma
	 * @author manhnt1.ho
	 * @param commodityId
	 * @param brandId
	 * @param productChannel
	 * @param pageIndex
	 * @return Danh sach hang hoa, nhan hieu, mau ma
	 * @throws Exception
	 */
	@GET
	@Path("/find/commodity_details")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommodityDetails(@QueryParam("commodityId") Integer commodityId, @QueryParam("brandId") Integer brandId, @QueryParam("productChannel") Integer productChannel,
			@QueryParam("pageIndex") Integer pageIndex) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_COMMODITY_DETAILS,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.findCommodityDetails(commodityId, brandId, productChannel, pageIndex));
			}
		});
	}
	
	/** 
	 * Danh sach hang hoa, nhan hieu, mau ma
	 * @author manhnt1.ho
	 * @param commodityId
	 * @param brandId
	 * @param productChannel
	 * @param pageIndex
	 * @return Danh sach hang hoa, nhan hieu, mau ma
	 * @throws Exception
	 */
	@GET
	@Path("/find/commodity_details_full")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCommodityDetailsFull(@QueryParam("commodityId") Integer commodityId, @QueryParam("brandId") Integer brandId, @QueryParam("productChannel") Integer productChannel,
			@QueryParam("pageIndex") Integer pageIndex) throws Exception {
		return this.authorize(ServiceName.GET_V1_0_PRD_COMMODITY_DETAILS,() -> {
			try (ProductManager manager = new ProductManager(this.currentUser)) {
				return ok(manager.findCommodityDetailFull(commodityId, brandId, productChannel, pageIndex));
			}
		});
	}
	
	@GET
	@Path("/get-code-table-db")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCodeTableDb(@QueryParam("codeGroup") String codeGroup, @QueryParam("category") String category, @QueryParam("status") String status) throws Exception {
		try( ProductManager manager = new ProductManager(this.currentUser) ) {
			return ok(manager.findCodeTableDb(codeGroup, category,status));
		}
	}  
	
	/** 
	  * Function download file
	  *  @author manhnt1.ho 
	  * @param filePath
	  * @return File
	  */
   @GET
   @Path("/download_file")
   @Produces(MediaType.APPLICATION_JSON)
   @Consumes(MediaType.APPLICATION_OCTET_STREAM)
   public Response downloadFile(@QueryParam("uplMasterId") Long uplMasterId) throws Exception {
   	 return this.authorize(() -> {  
   		 try (ProductManager manager = new ProductManager(this.currentUser)) {
        		String filePath = manager.getFilePath(uplMasterId, this.currentUser.getLoginId());
        		File f = new File(filePath);
               return responseFileDownload(f);
	        }
   	});
   }
   
	/** 
	 * Update scheme san pham
	 * @author manhnt1.ho
	 * @param productId
	 * @param schemeGroupId
	 * @return Update scheme san pham
	 * @throws Exception
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSchemeProduct(CommonDTO item) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_PRD_UPDATE,() -> {
			try (ProductManager mng = new ProductManager(this.currentUser)) {
				return ok(mng.updateSchemeGroup(item));
			}
		});
	}
	
	/** 
	 * Xoa scheme san pham
	 * @author manhnt1.ho
	 * @param id 
	 * @return Xoa scheme san pham
	 * @throws Exception
	 */
	@POST
	@Path("/delete_group")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteProductGroup(CommonDTO item) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_PRD_DELETE_SCHEME_GR,() -> {
			try (ProductManager mng = new ProductManager(this.currentUser)) {
				return ok(mng.deleteProductGroup(item));
			}
		});
	}

	/** 
	 * Them moi 1 config san pham thuoc nhom other
	 * @author manhnt1.ho
	 * @param lstCommodityObj
	 * @return Them moi 1 config san pham thuoc nhom other
	 * @throws Exception
	 */
	@POST
	@Path("/insert/product_config")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertProductConfig(List<CommodityDTO> lstCommodityObj) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_PRD_INS_PRD_CONFIG,() -> {
			try (ProductManager mng = new ProductManager(this.currentUser)) {
				return ok(mng.insertProductConfig(lstCommodityObj));
			}
		});

	}

	/** 
	 * Them moi hang hoa, nhan hieu,mau ma
	 * @author manhnt1.ho
	 * @param commodityObj
	 * @return Them moi hang hoa, nhan hieu,mau ma
	 * @throws Exception
	 */
	@POST
	@Path("/commodity/insert")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertCommodity(CommodityDTO commodityObj) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_PRD_INS_COMM,() -> {
			try (ProductManager mng = new ProductManager(this.currentUser)) {
				return ok(mng.insertCommodity(commodityObj));
			}
		});

	}

	/** 
	 * Them moi theo lo hang hoa, nhan hieu, mau ma
	 * @author manhnt1.ho
	 * @param uploadedInputStream
	 * @param fileDetail
	 * @param fileUpload
	 * @param payload
	 * @return Them moi theo lo hang hoa, nhan hieu, mau ma
	 * @throws Exception
	 */
	@POST
	@Path("/commodity/insert_excel")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertCommodityExcel(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("file") File fileUpload,
			@FormDataParam("object") String payload) throws Exception {
		return this.authorize(ServiceName.POST_V1_0_PRD_INS_EXCEL,() -> {
			try (ProductManager mng = new ProductManager(this.currentUser)) {
				return ok(mng.insertLstCommodity(fileUpload, payload));
			}
		});
	}
	
	/** 
	 * Lay du lieu cho BPM. Du lieu truyen vao : productCode, GoodCode, BrandCode, Type
	 * @author manhnt1.ho
	 * @param obj
	 * @return Lay du lieu cho BPM. Du lieu truyen vao : productCode, GoodCode, BrandCode, Type
	 * @throws Exception
	 */
	@POST  
	@Path("/get_data_for_bpm")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getDataForBPM(CommodityDTO obj) throws Exception {
		try (ProductManager mng = new ProductManager(this.currentUser)) {
			return ok(mng.findDataForBPM(obj));
		}
	}
	
}
