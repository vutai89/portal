package com.mcredit.data.customer.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;

import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.customer.entity.CustomerAccountLink;
import com.mcredit.data.repository.IUpsertRepository;
import com.mcredit.model.object.CardInformation;
import com.mcredit.model.object.CreditContractInfo;
import com.mcredit.model.object.PaymentOnWebInfo;

public class CustomerAccountLinkRepository extends BaseRepository implements IUpsertRepository<CustomerAccountLink> {

	public CustomerAccountLinkRepository(Session session) {
		super(session);
	}

	public void update(CustomerAccountLink item) throws DataException {
		this.session.update("CustomerAccountLink", item);
	}

	public void upsert(CustomerAccountLink item) throws DataException {
		if (item.getId() != null)
			updateEntity(item, "CustomerAccountLink");
		else
			this.session.save("CustomerAccountLink", item);
	}

	public void remove(CustomerAccountLink item) throws DataException {
		this.session.delete("CustomerAccountLink", item);
	}

	public CustomerAccountLink findCustomerAccountLinkById(Long id) {
		CustomerAccountLink accountLink = (CustomerAccountLink) this.session.find(CustomerAccountLink.class, id);
		return accountLink;
	}

	public Integer findNextLinkSeq(Long custId, String linkType) {
		Integer result = null;
		List<?> lst = this.session.getNamedQuery("CustomerAccountLink.nextLinkSeq").setParameter("custId", custId)
				.setParameter("linkType", linkType).list();
		if (lst != null && lst.size() > 0)
			result = (Integer) lst.get(0);
		return result;
	}

	@SuppressWarnings("rawtypes")
	public List findAllCustomerAccountLink(int page, int rowsPerPage) throws DataException {
		return this.session.getNamedQuery("findAllCustomerAccountLink").setFirstResult((page - 1) * rowsPerPage)
				.setMaxResults(rowsPerPage).list();
	}

	public CustomerAccountLink findCustomerAccountLinkById(long customerAccountLinkId) throws DataException {
		return (CustomerAccountLink) this.session.getNamedQuery("findCustomerAccountLinkById")
				.setParameter("custAccLinkId", customerAccountLinkId).uniqueResult();
	}

	public CardInformation findCardInformationByCardId(String cardId, BigDecimal feeCollectAmt, String[] condition)
			throws DataException {

		CardInformation result = null;

		List<?> lst = this.session.getNamedQuery("findCardInformationByCardId").setParameter("cardId", cardId).list();

		if (lst != null && lst.size() > 0) {

			Object obj = lst.get(0);

			if (obj != null) {

				Object[] o = (Object[]) obj;

				result = new CardInformation();

				result.setCardId(o[0] != null ? String.valueOf(o[0]) : "");
				result.setFullName(o[1] != null ? String.valueOf(o[1]) : "");
				result.setIdentityCard(o[2] != null ? String.valueOf(o[2]) : "");
				// result.setIdentityIssueDate(o[3]!=null?String.valueOf(o[4]):"");
				// result.setIdentityIssuePlace(o[4]!=null?String.valueOf(o[5]):"");
				BigDecimal dueBalance = (BigDecimal) o[3];
				BigDecimal paymentAmount = (BigDecimal) o[4];
				String cardStatus = o[5] != null ? String.valueOf(o[5]) : "";

				if (Arrays.asList(condition).contains(cardStatus.trim())) {
					if (dueBalance != null && paymentAmount != null) {

						BigDecimal balance = dueBalance.add(paymentAmount);

						if (balance.compareTo(new BigDecimal(0)) < 0)
							balance = balance.abs().add(feeCollectAmt);
						else
							balance = new BigDecimal(0);

						result.setPaymentAmount(balance);
					} // else: Du lieu join khong co, paymentAmount se = null, ngoai biz tra thong bao
						// thieu thong tin sao ke
				} else
					result.setPaymentAmount(new BigDecimal(-1));// Trang thai the la khong hop le, ngoai biz tra thong
																// bao the khong hop le
			}
		}
		return result;
	}

	public List<CreditContractInfo> findCreditContractInfo(final String contractNumbers, final String identityNumber,
			final String militaryId, final String mobilePhone) {

		if (!"".equals(identityNumber) && !"".equals(militaryId))
			return null;

		List<CreditContractInfo> lst = null;
		try {
			lst = this.session.doReturningWork(new ReturningWork<List<CreditContractInfo>>() {
				@Override
				public List<CreditContractInfo> execute(Connection conn) throws SQLException {
					ResultSet rs = null;
					List<CreditContractInfo> lst = null;
					try {

						String condition = "";

						// String identity = !"".equals(identityNumber) ? identityNumber : militaryId;

						if (!"".equals(contractNumbers) && !"".equals(identityNumber))
							condition = " ULM.CONTRACT_NUMBER = ? AND CI.IDENTITY_NUMBER = ? AND CI.IDENTITY_TYPE_ID IN (SELECT ID FROM CODE_TABLE WHERE CATEGORY='IDTYP' AND (CODE_VALUE1='1' OR CODE_VALUE1='3')) ";
						else if (!"".equals(contractNumbers) && !"".equals(militaryId))
							condition = " ULM.CONTRACT_NUMBER = ? AND CI.IDENTITY_NUMBER = ? AND CI.IDENTITY_TYPE_ID IN (SELECT ID FROM CODE_TABLE WHERE CATEGORY='IDTYP' AND CODE_VALUE1='2') ";
						else if (!"".equals(contractNumbers))
							condition = " ULM.CONTRACT_NUMBER = ? ";
						else if (!"".equals(identityNumber))
							condition = " CI.IDENTITY_NUMBER = ? AND CI.IDENTITY_TYPE_ID IN (SELECT ID FROM CODE_TABLE WHERE CATEGORY='IDTYP' AND (CODE_VALUE1='1' OR CODE_VALUE1='3')) ";
						else if (!"".equals(militaryId))
							condition = " CI.IDENTITY_NUMBER = ? AND CI.IDENTITY_TYPE_ID IN (SELECT ID FROM CODE_TABLE WHERE CATEGORY='IDTYP' AND CODE_VALUE1='2') ";

						String condition2 = "";
						if (!"".equals(mobilePhone))
							condition2 = " CCI.CONTACT_VALUE = '" + mobilePhone
									+ "' AND CT.CATEGORY = 'CONTAC_TYP' AND CT.CODE_VALUE1 = 'MOBILE'";

						if (!"".equals(condition) && !"".equals(condition2))
							condition += " AND " + condition2;

						String sql = "SELECT ULM.CONTRACT_NUMBER, CI.IDENTITY_NUMBER, ULM.OUTSTANDING_BALANCE, to_char(ULM.SIGN_DATE, 'dd/MM/yyyy') AS SIGN_DATE, ULM.STATUS, ULM.CUSTOMER_NAME, "
								+ " ULM.ORIGINAL_AMOUNT, ULM.TENOR, to_char(ULM.VALUE_DATE, 'dd/MM/yyyy') AS VALUE_DATE, to_char(ULM.MATURITY_DATE, 'dd/MM/yyyy') AS MATURITY_DATE, ULM.DISBURSEMENT_CHANNEL, "
								+ " ULM.NEXT_PAYMENT, to_char(ULM.NEXT_PAYMENT_DATE, 'dd/MM/yyyy') AS NEXT_PAYMENT_DATE, ULM.NEXT_PAYMENT_AMOUNT, ULM.TOTAL_AMOUNT_OVERDUE, ULM.TOTAL_AMOUNT, to_char(ULM.UPDATE_TIME, 'dd/MM/yyyy') AS UPDATE_TIME, "
								+ " ULM.INTEREST_RATE, CAR.ID, CAR.CUST_ID, CCI.CONTACT_TYPE, CCI.CONTACT_VALUE, CT.CATEGORY, CT.CODE_VALUE1, CT_PRO.CODE_VALUE1 AS TYPE_OF_LOAN "
								+ " FROM UPL_LOAN_MSTR ULM "
								+ " INNER JOIN CREDIT_APP_REQUEST CAR ON ULM.CONTRACT_NUMBER = CAR.MC_CONTRACT_NUMBER "
								+ " LEFT  JOIN CUST_IDENTITY CI ON CAR.CUST_ID = CI.CUST_ID "
								+ " INNER JOIN CUST_CONTACT_INFO CCI ON (CAR.CUST_ID = CCI.CUST_ID AND CCI.CONTACT_TYPE=(SELECT ID FROM CODE_TABLE WHERE CATEGORY='CONTAC_TYP' AND CODE_VALUE1='MOBILE') AND CCI.CONTACT_CATEGORY=(SELECT ID FROM CODE_TABLE WHERE CATEGORY='CONTAC_CAT' AND CODE_VALUE1='CUSTOMER') AND CONTACT_VALUE is not null) "
								+ " LEFT JOIN CODE_TABLE CT_PRO ON CT_PRO.ID = CAR.PRODUCT_GROUP "
								+ " LEFT JOIN CODE_TABLE CT ON CT.ID = CCI.CONTACT_TYPE " + " WHERE " + condition;

						PreparedStatement ps = conn.prepareStatement(sql);
						if (!"".equals(contractNumbers) && !"".equals(identityNumber)) {
							ps.setString(1, contractNumbers);
							ps.setString(2, identityNumber);
						} else if (!"".equals(contractNumbers) && !"".equals(militaryId)) {
							ps.setString(1, contractNumbers);
							ps.setString(2, militaryId);
						} else if (!"".equals(contractNumbers))
							ps.setString(1, contractNumbers);
						else if (!"".equals(identityNumber))
							ps.setString(1, identityNumber);
						else if (!"".equals(militaryId))
							ps.setString(1, militaryId);

						rs = ps.executeQuery();

						CreditContractInfo obj = null;

						String status = "";

						while (rs.next()) {

							if (lst == null)
								lst = new ArrayList<>();

							obj = new CreditContractInfo();

							obj.setContractNumber(rs.getString("CONTRACT_NUMBER"));
							obj.setTypeOfLoan(rs.getString("TYPE_OF_LOAN"));
							obj.setIdentityNumber(rs.getString("IDENTITY_NUMBER"));
							obj.setOutstandingBalance(rs.getBigDecimal("OUTSTANDING_BALANCE"));
							obj.setSignDate(rs.getString("SIGN_DATE"));

							status = rs.getString("STATUS");
							if (status != null)
								status = status.replace("OPEN", "ACTIVE").replace("BACKDATE", "ACTIVE");
							obj.setStatus(status);

							obj.setCustomerName(rs.getString("CUSTOMER_NAME"));
							obj.setMobilePhone(rs.getString("CONTACT_VALUE"));
							obj.setOriginalAmount(rs.getBigDecimal("ORIGINAL_AMOUNT"));
							obj.setTenor(rs.getInt("TENOR"));
							obj.setValueDate(rs.getString("VALUE_DATE"));
							obj.setMaturityDate(rs.getString("MATURITY_DATE"));
							obj.setDisbursementChannel(rs.getString("DISBURSEMENT_CHANNEL"));
							obj.setNextPayment(rs.getInt("NEXT_PAYMENT"));
							obj.setNextPaymentDate(rs.getString("NEXT_PAYMENT_DATE"));
							obj.setNextPaymentAmount(rs.getBigDecimal("NEXT_PAYMENT_AMOUNT"));
							obj.setTotalOverdueAmount(rs.getBigDecimal("TOTAL_AMOUNT_OVERDUE"));
							obj.setTotalAmount(rs.getBigDecimal("TOTAL_AMOUNT"));
							obj.setLastUpdated(rs.getString("UPDATE_TIME"));
							obj.setInterestRate(rs.getInt("INTEREST_RATE"));

							lst.add(obj);
						}
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						if (rs != null)
							rs.close();
					}
					return lst;
				}
			});
		} catch (Exception ex) {
			System.out.println("CustomerAccountLinkRepository.findCreditContractInfo.Ex: " + ex.toString());
		}
		return lst;
	}

	public List<PaymentOnWebInfo> findPaymentOnWeb(final String contractNumbers, final String identityNumber,
			final String militaryId) {

		if (!"".equals(identityNumber) && !"".equals(militaryId))
			return null;

		List<PaymentOnWebInfo> lst = null;

		try {
			lst = this.session.doReturningWork(new ReturningWork<List<PaymentOnWebInfo>>() {
				@Override
				public List<PaymentOnWebInfo> execute(Connection conn) throws SQLException {
					ResultSet rs = null;
					List<PaymentOnWebInfo> lst = null;
					try {

						String condition = "";

						String identity = !"".equals(identityNumber) ? identityNumber : militaryId;

						if (!"".equals(contractNumbers) && !"".equals(identity))
							condition = " CONTRACT_NUMBER = ? and IDENTITY_NUMBER = ? ";
						else if (!"".equals(contractNumbers))
							condition = " CONTRACT_NUMBER = ? ";
						else if (!"".equals(identity))
							condition = " IDENTITY_NUMBER = ? ";

						String sql = " select CONTRACT_NUMBER, IDENTITY_NUMBER, CUSTOMER_NAME, to_char(VALUE_DATE, 'dd/MM/yyyy') as VALUE_DATE, LOAN_AMOUNT, INTEREST_RATE, TENOR, PERIOD, to_char(PERIOD_DATE, 'dd/MM/yyyy') as PERIOD_DATE,"
								+ " PERIOD_AMOUNT, PRINCIPAL_AMOUNT, INTEREST_AMOUNT, FEE_AMOUNT, TOTAL_AMOUNT, to_char(UPDATE_TIME, 'dd/MM/yyyy') as UPDATE_TIME "
								+ " from UPL_LOAN_PYMNT_SCH " + " where " + condition + " order by PERIOD ";

						PreparedStatement ps = conn.prepareStatement(sql);
						if (!"".equals(contractNumbers) && !"".equals(identity)) {
							ps.setString(1, contractNumbers);
							ps.setString(2, identity);
						} else if (!"".equals(contractNumbers))
							ps.setString(1, contractNumbers);
						else if (!"".equals(identity))
							ps.setString(1, identity);

						rs = ps.executeQuery();

						PaymentOnWebInfo obj = null;

						while (rs.next()) {

							if (lst == null)
								lst = new ArrayList<>();

							obj = new PaymentOnWebInfo();

							obj.setContractNumber(rs.getString("CONTRACT_NUMBER"));
							obj.setIdentityNumber(rs.getString("IDENTITY_NUMBER"));
							obj.setCustomerName(rs.getString("CUSTOMER_NAME"));
							obj.setValueDate(rs.getString("VALUE_DATE"));
							obj.setLoanAmount(rs.getBigDecimal("LOAN_AMOUNT"));
							obj.setInterestRate(rs.getInt("INTEREST_RATE"));
							obj.setTenor(rs.getInt("TENOR"));
							obj.setPeriod(rs.getLong("PERIOD"));
							obj.setPeriodDate(rs.getString("PERIOD_DATE"));
							obj.setPeriodAmount(rs.getBigDecimal("PERIOD_AMOUNT"));
							obj.setPrincipalAmount(rs.getBigDecimal("PRINCIPAL_AMOUNT"));
							obj.setInterestAmount(rs.getBigDecimal("INTEREST_AMOUNT"));
							obj.setFeeAmount(rs.getBigDecimal("FEE_AMOUNT"));
							obj.setTotalAmount(rs.getBigDecimal("TOTAL_AMOUNT"));
							obj.setLastUpdated(rs.getString("UPDATE_TIME"));

							lst.add(obj);
						}
					} catch (Exception e) {

					} finally {
						if (rs != null)
							rs.close();
					}
					return lst;
				}
			});
		} catch (Exception ex) {
			System.out.println("CustomerAccountLinkRepository.findPaymentOnWeb.Ex: " + ex.toString());
		}
		return lst;
	}

	public String checkContractNumber(String contractNumber) throws DataException {

		List<?> lst = this.session.getNamedNativeQuery("checkContractNumber")
				.setParameter("contractNumber", contractNumber).list();

		if (lst != null && lst.size() > 0 && lst.get(0) != null) {

			String value = lst.get(0).toString();

			if (value != null)
				return value.trim();
		}

		return "";
	}

	public Object findCardInformationAllByCardId(String cardId) throws DataException {
		List<?> lst = this.session.getNamedQuery("findCardInformationAllByCardId").setParameter("cardId", cardId)
				.list();
		if (lst != null && lst.size() > 0)
			return lst.get(0);
		return null;
	}
}
