package com.jene.cognitive.model.cep.fraud;

import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.model.cep.IWarning;

/**
 * @uthor Jorge Nieves
 */
public class FraudWarning implements IWarning {

    private final Transaction localTransaction0;
    private final Transaction localTransaction1;

    public FraudWarning(Transaction localTransaction0, Transaction localTransaction1) {
        this.localTransaction0 = localTransaction0;
        this.localTransaction1 = localTransaction1;
    }

    public Transaction getLocalTransaction0() {
        return localTransaction0;
    }
    public Transaction getLocalTransaction1() {
        return localTransaction1;
    }

    @Override
    public String toString() {
        return String.format("HighAmountWarn (ID = %s, First event = (%s), Second event= (%s))",
                localTransaction0.getAccountId(),
                getEventSummary(localTransaction0),
                getEventSummary(localTransaction1));
    }

    private String getEventSummary(Transaction localTransaction) {

        return String.format("Date = %s, Country = %s, Amount = %f",
                localTransaction.getOperationDate(), localTransaction.getPeerCountry(), localTransaction.getAmount());
    }
}
