package com.mcredit.business.transaction.aggregate;

import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.NotificationTemplate;
import com.mcredit.data.transaction.entity.Transactions;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.dto.transaction.AppActiveInactiveProductDTO;
import com.mcredit.model.dto.transaction.ProductCompactDTO;
import com.mcredit.model.dto.transaction.TransactionDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.TemplateEnum;
import com.mcredit.model.object.ListObjectResult;
import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.model.object.transaction.InforSendEmailObject;
import com.mcredit.model.object.transaction.ProductName;
import com.mcredit.model.object.transaction.ScheduleActiveInactiveObject;
import com.mcredit.model.object.transaction.TransactionProductResult;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.service.ThreadSendEmail;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TransactionAggregate {

    private UnitOfWork unitOfWork = null;
    private CodeTableCacheManager ctCache = CacheManager.CodeTable();
    final int SCHEDULE_CR = 1;
    final int SCHEDULE_AC = 0;
    private BasedHttpClient bs = new BasedHttpClient();
    private String _esbHost = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST);

    public TransactionAggregate(UnitOfWork uok) {
        this.unitOfWork = uok;
    }

    public List<ProductName> getLstProduct() throws ValidationException {
        return this.unitOfWork.transactions.transactionsRepo().getLstProductCode();
    }

    public Object addTransactions(TransactionDTO transactionDTO, UserDTO user) throws ValidationException, ParseException {
        List<Object> error = new ArrayList<>();
        List<Object> sucsess = new ArrayList<>();
        Long statusDesire = ctCache.getBy(transactionDTO.getStatusDesire(), CTCat.TRANS_OP_CODE, CTGroup.TRANS).getId().longValue();
        Long statusRequest = ctCache.getBy(CTCodeValue1.TRANS_APP_WAIT, CTCat.TRANS_STATUS, CTGroup.TRANS).getId().longValue();
        try {
            ScheduleActiveInactiveObject object = new ScheduleActiveInactiveObject(transactionDTO.getTicketCode(), transactionDTO.getEffectDate(), transactionDTO.getNote());
            Transactions transactions = new Transactions(new Date(), user.getId(), new Date(), user.getId(), transactionDTO.getProductId(), "PRODUCTS",
                    transactionDTO.getProductId(), statusDesire, statusRequest, JSONConverter.toJSON(object));
            this.unitOfWork.transactions.transactionsRepo().add(transactions);
            sucsess.add(transactions);
            sendEmailScheduleProduct(new ArrayList<>(Arrays.asList(transactions.getId())), SCHEDULE_AC, user);
        } catch (Exception e) {
            error.add(e);
        }
        return new ListObjectResult(error, sucsess);
    }

    public TransactionProductResult seachTransactions(TransactionDTO input, Integer pageSize, Integer pageNum) throws ValidationException {
        return this.unitOfWork.transactions.transactionsRepo().seachTransactions(input, pageSize, pageNum);
    }

    public Object approveActiveInactiveProduct(AppActiveInactiveProductDTO aspdto, UserDTO user) throws ValidationException, ParseException {
        List<Object> error = new ArrayList<>();
        List<Object> sucsess = new ArrayList<>();
        Long statusRequest = ctCache.getBy(aspdto.getStatusRequest(), CTCat.TRANS_STATUS, CTGroup.TRANS).getId().longValue();
        Long statusAppOk = ctCache.getBy(CTCodeValue1.TRANS_APP_OK, CTCat.TRANS_STATUS, CTGroup.TRANS).getId().longValue();
        Long statusAppReject = ctCache.getBy(CTCodeValue1.TRANS_APP_REJECT, CTCat.TRANS_STATUS, CTGroup.TRANS).getId().longValue();
        Long statusActive = ctCache.getBy(CTCodeValue1.TRANS_OP_A, CTCat.TRANS_OP_CODE, CTGroup.TRANS).getId().longValue();
        Long statusInactive = ctCache.getBy(CTCodeValue1.TRANS_OP_I, CTCat.TRANS_OP_CODE, CTGroup.TRANS).getId().longValue();
        List<Long> transactionId = new ArrayList<>();
        try {
            if (statusRequest.equals(statusAppOk)) {
                for (ProductCompactDTO dTO : aspdto.getCompactDTOs()) {
                    Long statusDesire = ctCache.getBy(dTO.getStatusDesire(), CTCat.TRANS_OP_CODE, CTGroup.TRANS).getId().longValue();
                    if (statusDesire.equals(statusActive)) {
                        this.unitOfWork.transactions.transactionsRepo().updateActive(dTO.getProductId());
                        bs.doPost(this._esbHost + BusinessConstant.ACTIVE_PRODUCT, "{\"id\": \"" + dTO.getBpmProductId() + "\"}", ContentType.Json, AcceptType.Json);
                    } else if (statusDesire.equals(statusInactive)) {
                        this.unitOfWork.transactions.transactionsRepo().updateInactive(dTO.getEffectDate(), dTO.getProductId());
                        bs.doPost(this._esbHost + BusinessConstant.INACTIVE_PRODUCT, "{\"id\": " + dTO.getBpmProductId() + ",\"effDate\": \"" + dTO.getEffectDate() + "\"}", ContentType.Json, AcceptType.Json);
                    }
                    this.unitOfWork.transactions.transactionsRepo().updateStatusTransactions(statusRequest, dTO.getTransactionId());
                    this.unitOfWork.transactions.transactionsRepo().updateInforTransaction(user.getId(), dTO.getTransactionId());
                    transactionId.add(dTO.getTransactionId());
                }
                sendRefreshNoti("PRODUCTS");
//                sendRefreshNoti("DOCUMENT");
            } else if (statusRequest.equals(statusAppReject)) {
                for (ProductCompactDTO dTO : aspdto.getCompactDTOs()) {
                    this.unitOfWork.transactions.transactionsRepo().updateStatusTransactions(statusRequest, dTO.getTransactionId());
                    this.unitOfWork.transactions.transactionsRepo().updateInforTransaction(user.getId(), dTO.getTransactionId());
                    transactionId.add(dTO.getTransactionId());
                }
            }

            sucsess.add(aspdto.getCompactDTOs());
            sendEmailScheduleProduct(transactionId, SCHEDULE_CR, user);
        } catch (Exception e) {
            error.add(e);
        }
        return new ListObjectResult(error, sucsess);
    }

    public void sendRefreshNoti(String cache) throws ValidationException {
        KafkaProducer<String, String> _producer = initKafkaProducer();
        try {
            _producer.send(new ProducerRecord<String, String>(
                    CacheManager.Parameters().findParamValue(ParametersName.KAFKA_TOPIC_REFRESH_CACHE).toString(),
                    BusinessConstant.BIZ_ADMIN_REFRESH_KEY, cache));
        } catch (Exception ex) {
            throw new ValidationException(Messages.getString("refresh.cache.fail"));
        }

    }

    private KafkaProducer<String, String> initKafkaProducer() {
        Properties properties = new Properties();

        // list of host/port to use for establishing the initial connection to the Kafka cluster. 
        properties.put("bootstrap.servers", CacheManager.Parameters().findParamValue(ParametersName.KAFKA_BOOTSTRAP_SERVER));

        // An id string to pass to the server when making requests. 
        properties.put("client.id", "Assign Permissions");
        // Serializer class for key that implements the org.apache.kafka.common.serialization.Serializer interface.
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Serializer class for value that implements the org.apache.kafka.common.serialization.Serializer interface. 
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // 	The number of acknowledgments the producer requires the leader to have received before considering a request complete
        properties.put("acks", "1");

        properties.put("retries", "10");
        properties.put("batch.size", "20971520");
        properties.put("linger.ms", "33");
        properties.put("max.request.size", "2097152");

        return new KafkaProducer<>(properties);
    }

    public void sendEmailScheduleProduct(List<Long> transactionId, int sendEmailType, UserDTO user) throws Exception {
        List<InforSendEmailObject> lstInfor = this.unitOfWork.transactions.transactionsRepo().getInforSendEmail(transactionId);
        NotificationTemplate notiTemplate = null;
        String body;
        SendEmailDTO emailInfo = new SendEmailDTO();
        for (InforSendEmailObject object : lstInfor) {
            if (sendEmailType == SCHEDULE_AC) {
                notiTemplate = this.unitOfWork.common.getNotificationTemplateRepository().findByCode(TemplateEnum.SCHEDULE_PRODUCT_AC.toString());
                emailInfo.setTo(new ArrayList<>(Arrays.asList(object.getEmailTeamLead())));
            } else {
                notiTemplate = this.unitOfWork.common.getNotificationTemplateRepository().findByCode(TemplateEnum.SCHEDULE_PRODUCT_CR.toString());
                emailInfo.setTo(new ArrayList<>(Arrays.asList(object.getEmail())));
            }
            body = this.getEmailBody(notiTemplate.getNotificationTemplate(), sendEmailType, object);
            emailInfo.setSubject(notiTemplate.getNotificationName());
            emailInfo.setBody(body);
            Long idSendEmailType = ctCache.getIdBy(CTCodeValue1.PROD_SCHEDULE, CTCat.EMAIL_TYPE, CTGroup.EMAIL);
            ThreadSendEmail sendEmail = new ThreadSendEmail(emailInfo, idSendEmailType, user);
            sendEmail.send();
        }

    }

    public String getEmailBody(String emailTemplate, int sendEmailType, InforSendEmailObject object) {
        String body = emailTemplate;
        if (sendEmailType == SCHEDULE_AC) {
            body = body.replaceAll(":createdBy", object.getCreatedBy());
            body = body.replaceAll(":productName", object.getProductName());
            body = body.replaceAll(":statusDesire", object.getStatusActive());
            body = body.replaceAll(":effectDate", object.getEffectDate());
        } else {
            body = body.replaceAll(":ticketCode", object.getTicketCode());
            body = body.replaceAll(":statusDesire", object.getStatusActive());
            body = body.replaceAll(":productName", object.getProductName());
            body = body.replaceAll(":statusApp", object.getStatusApp());
        }
        return body;
    }

}
