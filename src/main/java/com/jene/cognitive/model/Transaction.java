package com.jene.cognitive.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * @uthor Jorge Nieves
 */

public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long transactionId;

	private Date operationDate;

	private BigInteger timeMilis;

	private String status;

	private Double amount;

	private String debitCardId;

	private String peerId;

	private String peerAddress;

	private String peerCity;

	private String peerState;

	private String peerZip;

	private String peerCountry;

	private Date valueDate;

	private String type;

	private String peerName;

	private String accountId;

	private String accountCode;

	private String debitCardPAN;

	private Double accountBalance;

	private String peerActivity;

	private String description;

	private String mcc;

	private Fees fees;

	private GeoLoc peerLocation;

	public Transaction() {
	}

	public Transaction(Long transactionId, Date operationDate, BigInteger timeMilis, String status, Double amount, String debitCardId, String peerId, String peerAddress, String peerCity, String peerState, String peerZip, String peerCountry, Date valueDate, String type, String peerName, String accountId, String accountCode, String debitCardPAN, Double accountBalance, String peerActivity, String description, String mcc, Fees fees, GeoLoc peerLocation) {
		this.transactionId = transactionId;
		this.operationDate = operationDate;
		this.timeMilis = timeMilis;
		this.status = status;
		this.amount = amount;
		this.debitCardId = debitCardId;
		this.peerId = peerId;
		this.peerAddress = peerAddress;
		this.peerCity = peerCity;
		this.peerState = peerState;
		this.peerZip = peerZip;
		this.peerCountry = peerCountry;
		this.valueDate = valueDate;
		this.type = type;
		this.peerName = peerName;
		this.accountId = accountId;
		this.accountCode = accountCode;
		this.debitCardPAN = debitCardPAN;
		this.accountBalance = accountBalance;
		this.peerActivity = peerActivity;
		this.description = description;
		this.mcc = mcc;
		this.fees = fees;
		this.peerLocation = peerLocation;
	}


	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public BigInteger getTimeMilis() {
		return timeMilis;
	}

	public void setTimeMilis(BigInteger timeMilis) {
		this.timeMilis = timeMilis;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getDebitCardId() {
		return debitCardId;
	}

	public void setDebitCardId(String debitCardId) {
		this.debitCardId = debitCardId;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public String getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(String peerAddress) {
		this.peerAddress = peerAddress;
	}

	public String getPeerCity() {
		return peerCity;
	}

	public void setPeerCity(String peerCity) {
		this.peerCity = peerCity;
	}

	public String getPeerState() {
		return peerState;
	}

	public void setPeerState(String peerState) {
		this.peerState = peerState;
	}

	public String getPeerZip() {
		return peerZip;
	}

	public void setPeerZip(String peerZip) {
		this.peerZip = peerZip;
	}

	public String getPeerCountry() {
		return peerCountry;
	}

	public void setPeerCountry(String peerCountry) {
		this.peerCountry = peerCountry;
	}

	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getDebitCardPAN() {
		return debitCardPAN;
	}

	public void setDebitCardPAN(String debitCardPAN) {
		this.debitCardPAN = debitCardPAN;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public String getPeerActivity() {
		return peerActivity;
	}

	public void setPeerActivity(String peerActivity) {
		this.peerActivity = peerActivity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public Fees getFees() {
		return fees;
	}

	public void setFees(Fees fees) {
		this.fees = fees;
	}

	public GeoLoc getPeerLocation() {
		return peerLocation;
	}

	public void setPeerLocation(GeoLoc peerLocation) {
		this.peerLocation = peerLocation;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	@Override
	public String toString() {
		return "Transaction{" +
				"transactionId=" + transactionId +
				", operationDate=" + operationDate +
				", timeMilis=" + timeMilis +
				", status='" + status + '\'' +
				", amount=" + amount +
				", debitCardId='" + debitCardId + '\'' +
				", peerId='" + peerId + '\'' +
				", peerAddress='" + peerAddress + '\'' +
				", peerCity='" + peerCity + '\'' +
				", peerState='" + peerState + '\'' +
				", peerZip='" + peerZip + '\'' +
				", peerCountry='" + peerCountry + '\'' +
				", valueDate=" + valueDate +
				", type='" + type + '\'' +
				", peerName='" + peerName + '\'' +
				", accountId='" + accountId + '\'' +
				", accountCode='" + accountCode + '\'' +
				", debitCardPAN='" + debitCardPAN + '\'' +
				", accountBalance=" + accountBalance +
				", peerActivity='" + peerActivity + '\'' +
				", description='" + description + '\'' +
				", mcc='" + mcc + '\'' +
				", fees=" + fees +
				", peerLocation=" + peerLocation +
				'}';
	}
}