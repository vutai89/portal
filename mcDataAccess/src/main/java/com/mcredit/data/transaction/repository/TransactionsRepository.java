package com.mcredit.data.transaction.repository;

import org.hibernate.Session;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.repository.IAddRepository;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.data.transaction.entity.Transactions;
import com.mcredit.model.dto.transaction.TransactionDTO;
import com.mcredit.model.object.transaction.InforSendEmailObject;
import com.mcredit.model.object.transaction.ProductName;
import com.mcredit.model.object.transaction.TransactionProductObject;
import com.mcredit.model.object.transaction.TransactionProductResult;
import com.mcredit.util.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.FlushMode;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

public class TransactionsRepository extends BaseRepository implements IUpsertRepository<Transactions>, IAddRepository<Transactions> {

    public TransactionsRepository(Session session) {
        super(session);
    }

    public void add(Transactions item) {
        this.session.save(item);
        this.session.flush();
    }

    public void upsert(Transactions item) {
        this.session.saveOrUpdate("Transactions", item);
    }

    public List<ProductName> getLstProductCode() {
        List<ProductName> result = new ArrayList<>();
        List lstProductName = this.session.getNamedNativeQuery("getLstProduct").list();
        if (lstProductName != null && !lstProductName.isEmpty()) {
            for (Object obj : lstProductName) {
                result.add(transformObject(obj, ProductName.class));
            }
        }
        return result;
    }

    public TransactionProductResult seachTransactions(TransactionDTO input, Integer pageSize, Integer pageNum) {
        StringBuilder strQuery = new StringBuilder();
        StringBuilder strQueryCount = new StringBuilder();
        HashMap<String, Object> parameterList = new HashMap<>();
        NativeQuery<?> queryStr = session.getNamedNativeQuery("searchTransactions");
        NativeQuery<?> queryStrCount = session.getNamedNativeQuery("searchTransactionsCountRecord");
        strQuery.append(queryStr.getQueryString());
        strQueryCount.append(queryStrCount.getQueryString());
        TransactionProductResult result = new TransactionProductResult();
       
        if (input.getProductId() != null && input.getProductId() != 0L) {
            strQuery.append(" and pd.id = :productId ");
            strQueryCount.append(" and pd.id = :productId ");
            parameterList.put("productId", input.getProductId());
        }
        if (!StringUtils.isNullOrEmpty(input.getTicketCode())) {
            strQuery.append(" and UPPER(JSON_VALUE(ts.TRANSACTION_COMMENT, '$.ticketCode')) = :ticketCode ");
            strQueryCount.append(" and UPPER(JSON_VALUE(ts.TRANSACTION_COMMENT, '$.ticketCode')) = :ticketCode ");
            parameterList.put("ticketCode", input.getTicketCode().trim().toUpperCase());
        }
        if (!StringUtils.isNullOrEmpty(input.getEffectDateFrom())) {
            strQuery.append(" and TRUNC(ts.init_transaction_date) >= TO_DATE(:effectDateFrom,'dd/mm/yyyy') ");
            strQueryCount.append(" and TRUNC(ts.init_transaction_date) >= TO_DATE(:effectDateFrom,'dd/mm/yyyy') ");
            parameterList.put("effectDateFrom", input.getEffectDateFrom().trim());
        }
        if (!StringUtils.isNullOrEmpty(input.getEffectDateTo())) {
            strQuery.append(" and TRUNC(ts.init_transaction_date) <= TO_DATE(:effectDateTo,'dd/mm/yyyy') ");
            strQueryCount.append(" and TRUNC(ts.init_transaction_date) <= TO_DATE(:effectDateTo,'dd/mm/yyyy') ");
            parameterList.put("effectDateTo", input.getEffectDateTo().trim());
        }
        if (!StringUtils.isNullOrEmpty(input.getStatusRequest())) {
            strQuery.append(" and ctt.CODE_VALUE1 = :statusRequest ");
            strQueryCount.append(" and ctt.CODE_VALUE1 = :statusRequest ");
            parameterList.put("statusRequest", input.getStatusRequest().trim());
        }
        strQuery.append(" ORDER BY ts.init_transaction_date desc ");
        Query query = this.session.createNativeQuery(strQuery.toString()).setHibernateFlushMode(FlushMode.ALWAYS);
        Query queryCount = this.session.createNativeQuery(strQueryCount.toString()).setHibernateFlushMode(FlushMode.ALWAYS);
        for (String key : parameterList.keySet()) {
            query.setParameter(key, parameterList.get(key));
            queryCount.setParameter(key, parameterList.get(key));
        }
        if (pageSize != null && !pageSize.equals(0) && pageNum != null && !pageNum.equals(0)) {
            query.setFirstResult((pageNum - 1) * pageSize).setMaxResults(pageSize);
        }
        List lst = query.getResultList();
        List<TransactionProductObject> retList = new ArrayList<>();
        if (lst != null && !lst.isEmpty()) {
            for (Object o : lst) {
                retList.add(transformObject(o, TransactionProductObject.class));
            }
        }
        result.setCountRecord(((BigDecimal) queryCount.getSingleResult()).intValue());
        result.setLstProductObjects(retList);
             
        return result;
    }

    public int updateActive( Long productId) {
        return this.session.getNamedQuery("updateActiveProducts").setParameter("productId", productId)
                .setHibernateFlushMode(FlushMode.ALWAYS).executeUpdate();
    }
    
    public int updateInactive(String effDate, Long productId) {
        return this.session.getNamedQuery("updateInactiveProducts").setParameter("effDate", effDate).setParameter("productId", productId)
                .setHibernateFlushMode(FlushMode.ALWAYS).executeUpdate();
    }
    public int updateStatusTransactions(Long transactionStatus, Long transactionId) {
        return this.session.getNamedQuery("updateStatusTransactions").setParameter("transactionStatus", transactionStatus).setParameter("transactionId", transactionId)
                .setHibernateFlushMode(FlushMode.ALWAYS).executeUpdate();
    }

    public List<InforSendEmailObject> getInforSendEmail(List<Long> transactionId) {
        List<InforSendEmailObject> result = new ArrayList<>();
        List lstSendEmail = this.session.getNamedNativeQuery("getInforSendEmail").setParameterList("transactionId", transactionId).list();
        if (lstSendEmail != null && !lstSendEmail.isEmpty()) {
            for (Object obj : lstSendEmail) {
                result.add(transformObject(obj, InforSendEmailObject.class));
            }
        }
        return result;
    }
    
    public int updateInforTransaction(Long userId, Long transactionId) {
        return this.session.getNamedQuery("updateInforTransaction").setParameter("userId", userId).setParameter("transactionId", transactionId)
                .setHibernateFlushMode(FlushMode.ALWAYS).executeUpdate();
    }
    
}
