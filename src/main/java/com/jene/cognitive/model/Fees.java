package com.jene.cognitive.model;


import java.io.Serializable;

/**
 * @uthor Jorge Nieves
 */

public class Fees implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private Double amount;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Fees{" +
                "type='" + type + '\'' +
                ", amount=" + amount +
                '}';
    }
}
