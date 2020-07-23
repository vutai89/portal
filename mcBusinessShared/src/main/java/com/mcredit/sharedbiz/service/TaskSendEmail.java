package com.mcredit.sharedbiz.service;

import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.SendEmail;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.service.MessageService;

public class TaskSendEmail implements Runnable {

    protected UnitOfWork uok = null;
    private CodeTableCacheManager ctCache = CacheManager.CodeTable();
    private SendEmailDTO emailInfo = null;
    private UserDTO currentUser = null;
    private Long idEmailType = null;

    public TaskSendEmail(SendEmailDTO emailInfo, Long idEmailType, UserDTO currentUser) {
        this.emailInfo = emailInfo;
        this.currentUser = currentUser;
        this.idEmailType = idEmailType;
    }

    @Override
    public void run() {

        this.uok = new UnitOfWork();
        String emailOpCentralUser = "";
        String emailOpCentralEmail = "";
        String passwordEmailOpCentral = "";
        Long emailTypeSchedule = ctCache.getIdBy(CTCodeValue1.PROD_SCHEDULE, CTCat.EMAIL_TYPE, CTGroup.EMAIL);
        Long emailTypeWareHouse = ctCache.getIdBy(CTCodeValue1.WH_OP_2, CTCat.EMAIL_TYPE, CTGroup.EMAIL);
        if (this.idEmailType.equals(emailTypeSchedule)) {
            emailOpCentralUser = CacheManager.Parameters().findParamValueAsString(ParametersName.ACTIVE_INACTIVE_PRODUCT_USER);
            emailOpCentralEmail = CacheManager.Parameters().findParamValueAsString(ParametersName.ACTIVE_INACTIVE_PRODUCT_EMAIL);
            passwordEmailOpCentral = CacheManager.Parameters().findParamValueAsString(ParametersName.ACTIVE_INACTIVE_PRODUCT_PASSWORD_EMAIL);
        } else if (this.idEmailType.equals(emailTypeWareHouse)) {
            emailOpCentralUser = CacheManager.Parameters().findParamValueAsString(ParametersName.MANAGE_PROFILE_OP_CENTRAL_USER);
            emailOpCentralEmail = CacheManager.Parameters().findParamValueAsString(ParametersName.MANAGE_PROFILE_OP_CENTRAL_EMAIL);
            passwordEmailOpCentral = CacheManager.Parameters().findParamValueAsString(ParametersName.MANAGE_PROFILE_OP_CENTRAL_PASSWORD_EMAIL);
        }
        this.emailInfo.setSmtpUser(emailOpCentralUser);
        this.emailInfo.setSmtpPassword(passwordEmailOpCentral);
        this.emailInfo.setFrom(emailOpCentralEmail);
        MessageService messaseService = new MessageService(this.emailInfo);
        Long idCodeTableStatusSendEmail = null;
        String errorMsg = "";
        try {
            messaseService.send();
            idCodeTableStatusSendEmail = ctCache.getIdBy(CTCodeValue1.SEND_OK, CTCat.SEND_STATUS, CTGroup.EMAIL);

        } catch (Exception e) {
            idCodeTableStatusSendEmail = ctCache.getIdBy(CTCodeValue1.SEND_FAIL, CTCat.SEND_STATUS, CTGroup.EMAIL);
            if (e.getMessage() != null) {
                errorMsg = e.getMessage();
            }
            e.printStackTrace();
        } finally {
            this.logToTableSendEmail(idCodeTableStatusSendEmail, errorMsg);
        }
    }

    public void logToTableSendEmail(Long idCodeTableStatusSendEmail, String statusMsg) {
        String strToEmails = emailInfo.getTo() != null ? String.join(",", emailInfo.getTo()) : "";
        String strCCEmails = emailInfo.getCc() != null ? String.join(",", emailInfo.getCc()) : "";
        String strBccEmails = emailInfo.getBcc() != null ? String.join(",", emailInfo.getBcc()) : "";
        SendEmail sendEmail = new SendEmail(strToEmails, strCCEmails, strBccEmails, emailInfo.getSubject(), emailInfo.getBody(), this.currentUser.getLoginId(), statusMsg, this.idEmailType, idCodeTableStatusSendEmail);
        try {
            this.uok.common.start();
            this.uok.common.getSendEmailRepository().add(sendEmail);
            this.uok.common.commit();
        } catch (Exception e) {
            e.printStackTrace();
            this.uok.common.rollback();
        }

    }

}
