package com.freeing.message.sending.test.inapp.bean;

public class TransInfo {
    private String id;
    private String transNo;
    private String amount;

    public TransInfo(String id, String transNo, String amount) {
        this.id = id;
        this.transNo = transNo;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransInfo{" +
                "id='" + id + '\'' +
                ", transNo='" + transNo + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
