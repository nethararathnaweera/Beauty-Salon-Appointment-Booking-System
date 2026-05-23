package com.salon.review.module;


public class BillDetail {

    private String billId;

    public BillDetail() {
        billId = "";
    }

    public BillDetail(String billId) {
        this.billId = billId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        if (billId == null) {
            this.billId = "";
        } else {
            this.billId = billId.trim();
        }
    }

    public void displayDetails() {
        System.out.println("Bill ID : " + billId);
    }
}
