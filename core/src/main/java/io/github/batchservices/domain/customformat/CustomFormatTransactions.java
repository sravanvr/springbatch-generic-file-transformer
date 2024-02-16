package io.github.batchservices.domain.customformat;

import javax.persistence.MappedSuperclass;

import io.github.batchservices.domain.AbstractDetailRecord;
import io.github.batchservices.domain.EntityMarker;

@MappedSuperclass
public class CustomFormatTransactions extends AbstractDetailRecord implements EntityMarker {
	
	String branchOrBankNumber;
	String customerID;
	String filler;
	String module;
	String amount;
	String tranCode;
	String referenceTransactionDescription;
	String accountNumberExpanded;
	String effectiveDate;
	
	public String getBranchOrBankNumber() {
		return branchOrBankNumber;
	}
	
	public void setBranchOrBankNumber(String branchOrBankNumber) {
		this.branchOrBankNumber = branchOrBankNumber;
	}
	
	public String getCustomerID() {
		return customerID;
	}
	
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	public String getFiller() {
		return filler;
	}
	
	public void setFiller(String filler) {
		this.filler = filler;
	}
	
	public String getModule() {
		return module;
	}
	
	public void setModule(String module) {
		this.module = module;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getTranCode() {
		return tranCode;
	}
	
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	
	public String getReferenceTransactionDescription() {
		return referenceTransactionDescription;
	}
	
	public void setReferenceTransactionDescription(String referenceTransactionDescription) {
		this.referenceTransactionDescription = referenceTransactionDescription;
	}
	
	public String getAccountNumberExpanded() {
		return accountNumberExpanded;
	}
	
	public void setAccountNumberExpanded(String accountNumberExpanded) {
		this.accountNumberExpanded = accountNumberExpanded;
	}
	
	public String getEffectiveDate() {
		return effectiveDate;
	}
	
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	
	
	
	/** Below is the original domain object with field types declared as per the spec. 
	 *  The above one is converted to have all fields as Strings, because we are writing this data into a flat file.
	 * 
	 */
//	Integer branchOrBankNumber;
//	Integer customerID;
//	String filler;
//	Integer module;
//	Integer amount;
//	Integer tranCode;
//	String referenceTransactionDescription;
//	Integer accountNumberExpanded;
//	Date effectiveDate;
//	
//	public Integer getBranchOrBankNumber() {
//		return branchOrBankNumber;
//	}
//	
//	public void setBranchOrBankNumber(Integer branchOrBankNumber) {
//		this.branchOrBankNumber = branchOrBankNumber;
//	}
//	
//	public Integer getCustomerID() {
//		return customerID;
//	}
//	
//	public void setCustomerID(Integer customerID) {
//		this.customerID = customerID;
//	}
//	
//	public String getFiller() {
//		return filler;
//	}
//	
//	public void setFiller(String filler) {
//		this.filler = filler;
//	}
//	
//	public Integer getModule() {
//		return module;
//	}
//	
//	public void setModule(Integer module) {
//		this.module = module;
//	}
//	
//	public Integer getAmount() {
//		return amount;
//	}
//	
//	public void setAmount(Integer amount) {
//		this.amount = amount;
//	}
//	
//	public Integer getTranCode() {
//		return tranCode;
//	}
//	
//	public void setTranCode(Integer tranCode) {
//		this.tranCode = tranCode;
//	}
//	
//	public String getReferenceTransactionDescription() {
//		return referenceTransactionDescription;
//	}
//	
//	public void setReferenceTransactionDescription(String referenceTransactionDescription) {
//		this.referenceTransactionDescription = referenceTransactionDescription;
//	}
//	
//	public Integer getAccountNumberExpanded() {
//		return accountNumberExpanded;
//	}
//	
//	public void setAccountNumberExpanded(Integer accountNumberExpanded) {
//		this.accountNumberExpanded = accountNumberExpanded;
//	}
//	
//	public Date getEffectiveDate() {
//		return effectiveDate;
//	}
//	
//	public void setEffectiveDate(Date effectiveDate) {
//		this.effectiveDate = effectiveDate;
//	}
	
	
	
}