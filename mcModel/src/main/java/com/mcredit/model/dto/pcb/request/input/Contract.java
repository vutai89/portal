package com.mcredit.model.dto.pcb.request.input;

public class Contract {
	
	private String DateRequestContract;			// ngay yeu cau cap tin dung (ddMMyyyy)
	private Integer OperationType;				// loai hop dong (vay ca nhan)
	private String CodCurrency;					// ma tien nguyen te
	private Instalment Instalment;				// thong tin khoan vay
	
	public String getDateRequestContract() {
		return DateRequestContract;
	}
	
	public void setDateRequestContract(String dateRequestContract) {
		DateRequestContract = dateRequestContract;
	}

	public Integer getOperationType() {
		return OperationType;
	}

	public void setOperationType(Integer operationType) {
		OperationType = operationType;
	}

	public String getCodCurrency() {
		return CodCurrency;
	}

	public void setCodCurrency(String codCurrency) {
		CodCurrency = codCurrency;
	}

	public Instalment getInstalment() {
		return Instalment;
	}

	public void setInstalment(Instalment instalment) {
		Instalment = instalment;
	}

}
