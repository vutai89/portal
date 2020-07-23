package com.mcredit.business.telesales.manager;

import java.util.List;

import com.mcredit.business.telesales.aggregate.XSellAggregate;
import com.mcredit.business.telesales.factory.TelesalesFactory;
import com.mcredit.business.telesales.validation.XSellValidation;
import com.mcredit.common.Messages;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.mobile.object.xsell.XSellSearchInfo;
import com.mcredit.model.dto.telesales.UplDetailDTO;
import com.mcredit.model.dto.telesales.UploadFileXsellDTO;
import com.mcredit.model.dto.xsell.XSellFileDTO;
import com.mcredit.model.enums.UplAction;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class XsellManager extends BaseManager{
   
    private XSellAggregate _agg = TelesalesFactory.getXSellAggregate(this.uok.telesale);
    private XSellValidation _validation = TelesalesFactory.getXSellValidation();
   
    private UserDTO _user;

    public XsellManager(UserDTO user) {
        _user = user;
    }

    public List<XSellFileDTO> searchXSellFiles(String statusCodeValue1, String fromDateUpload, String toDateUpload)
			throws Exception {
		return this.tryCatch(() -> {
			return _agg.searchXSellFiles(statusCodeValue1, fromDateUpload, toDateUpload);
		});
	}

	public Object changeStatusUpl(UplDetailDTO payloadUpl, int action) throws Exception {
		return this.tryCatch(() -> {
			
			_validation.validateFileID(payloadUpl.getId());
			UplDetail upl = _agg.getUplDetail(payloadUpl.getId());
			UplAction eAction = UplAction.values()[action];
			Object object = null;
			
			switch (eAction) {
			case UPL_DETAIL_APPROVE:
				_validation.exitsUpl(upl);
				_validation.validateUplStatus(_agg.checkStatusUPLIsWait(upl.getStatusApp()));
				object = _agg.approvalQTTR(this._user, upl.getId());
				break;
			case UPL_DETAIL_REFUSE:
				_validation.exitsUpl(upl);
				_validation.validateUplStatus(_agg.checkStatusUPLIsRefuse(upl.getStatusApp()));
				String rejectReason = payloadUpl.getRejectionReason();
				object = _agg.refuseQTTR(this._user, payloadUpl.getId(), rejectReason);
				break;
			case UPL_DETAIL_DELETE:
				_validation.exitsUpl(upl);
				_validation.validateUpl(upl);
				object = _agg.deleteFileMIS(upl,false);
				break;
			case UPL_DETAIL_DELETE_EXITS:
				_validation.exitsUpl(upl);
				_validation.validateUpl(upl);
				object = _agg.deleteFileMIS(upl,true);
				break;
			}
			return object;
		});
	}

	public List<XSellSearchInfo> searchXSellInfoBorrow(String identityNumber) throws Exception {
		return this.tryCatch(() -> {
			return _agg.searchXSellInfoBorrow(identityNumber);
		});
	}

	public UplDetail getUplDetailById(Long uplDetailId) throws Exception {
		return this.tryCatch(() -> {
			if (uplDetailId == null) {
				throw new ValidationException(Messages.getString("validation.field.madatory", "uplDetailId"));
			}
			return _agg.getUplDetailById(uplDetailId);
		});
	}

	public Object changeCampaignStatus() throws Exception {
		return this.tryCatch(() -> {
			return _agg.changeCampaignStatus();
		});
	}

    public Object importXsellUplCusomter(UploadFileXsellDTO xsellDTO) throws Exception {
        return this.tryCatch(() -> {
            _validation.validateImport(xsellDTO);
            return TelesalesFactory.getUploadAggregateInstance(uok.telesale).importXsell(xsellDTO, _user);
        });

    }
    
    public Result changeStatusImport(Long uplDetailId) throws Exception {
        return this.tryCatch(() -> {
            _validation.exitsUplDetailId(uplDetailId);
            return TelesalesFactory.getUploadAggregateInstance(uok.telesale).setStatusImportXsell(uplDetailId);
        });

    }

    /*public static void main(String[] args) throws FileNotFoundException, Exception {
        UploadFileXsellDTO UFDTO = new UploadFileXsellDTO();
        UFDTO.setUserFileName("Book3.xlsx");
        InputStream inputstream = new FileInputStream("D:\\MCredit\\Book3.xlsx");
        UFDTO.setFileContent(inputstream);
        UFDTO.setUplType("GC");
        UFDTO.setDateFrom("08/11/2018");
        UFDTO.setDateTo("08/12/2018");        
//
        UserDTO user = new UserDTO();
        user.setId(55143L);
        user.setLoginId("huongnv.ho");
        XsellManager um = new XsellManager(user);
        um.importXsellUplCusomter(UFDTO);
    }*/
}
