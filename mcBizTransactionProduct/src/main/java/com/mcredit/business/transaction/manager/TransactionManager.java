package com.mcredit.business.transaction.manager;

import com.mcredit.business.transaction.aggregate.TransactionAggregate;
import com.mcredit.business.transaction.factory.TransactionFactory;
import com.mcredit.business.transaction.validation.TransactionValidation;
import com.mcredit.model.dto.transaction.AppActiveInactiveProductDTO;
import com.mcredit.model.dto.transaction.TransactionDTO;
import com.mcredit.model.object.transaction.ProductName;
import com.mcredit.model.object.transaction.TransactionProductObject;
import com.mcredit.model.object.transaction.TransactionProductResult;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.manager.BaseManager;
import java.util.List;

public class TransactionManager extends BaseManager {

    private final UserDTO _user;
    private TransactionAggregate _spAgg = null;

    private final TransactionValidation _scheduleValidate = new TransactionValidation();

    public TransactionManager(UserDTO user) {
        _user = user;
        this._spAgg = TransactionFactory.getTransactionAggregate(uok);
    }

    /**
     * get list product
     * @return
     * @throws Exception 
     */
    public List<ProductName> getLstProduct() throws Exception {
        return this.tryCatch(() -> {
            return _spAgg.getLstProduct();            
        });
    }
    /**
     * create request setup calendar active/inactive products
     * @param transactionDTO
     * @return
     * @throws Exception 
     */
     public Object addTransactions(TransactionDTO transactionDTO) throws Exception {
        return this.tryCatch(() -> {
            _scheduleValidate.validateaAddScheduleProduct(transactionDTO);  
            return _spAgg.addTransactions(transactionDTO, _user);
        });
    }
     /**
      * search information schedule product
      * @param transactionDTO
      * @param pageSize
      * @param pageNum
      * @return
      * @throws Exception 
      */
     public TransactionProductResult seachTransactions(TransactionDTO transactionDTO, Integer pageSize, Integer pageNum) throws Exception {
        return this.tryCatch(() -> {             
            _scheduleValidate.validateaSeachTransactions(transactionDTO); 
            return _spAgg.seachTransactions(transactionDTO, pageSize, pageNum);
        });
    }
     /**
      * Approval for ticket refusal
      * @param aspdto
      * @return
      * @throws Exception 
      */
     public Object approveActiveInactiveProduct(AppActiveInactiveProductDTO aspdto) throws Exception {
        return this.tryCatch(() -> {             
            return _spAgg.approveActiveInactiveProduct(aspdto,_user);
        });
    }

}
