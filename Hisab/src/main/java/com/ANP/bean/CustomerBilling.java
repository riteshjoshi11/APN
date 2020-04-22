package com.ANP.bean;

import java.util.Date;

public class CustomerBilling {

        private int id;
        private int customerId;
        private java.util.Date date;
        private double amount;
        private double CGST;
        private double SGST;
        private double IGST;
        private double extra;
        private double total;
        private int orgId;
        private int createdById;
        private String note;
        private boolean includeInReport;



        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public int getCustomerId() {
                return customerId;
        }

        public void setCustomerId(int customerId) {
                this.customerId = customerId;
        }

        public Date getDate() {
                return date;
        }

        public void setDate(Date date) {
                this.date = date;
        }

        public double getAmount() {
                return amount;
        }

        public void setAmount(double amount) {
                this.amount = amount;
        }

        public double getCGST() {
                return CGST;
        }

        public void setCGST(double CGST) {
                this.CGST = CGST;
        }

        public double getSGST() {
                return SGST;
        }

        public void setSGST(double SGST) {
                this.SGST = SGST;
        }

        public double getIGST() {
                return IGST;
        }

        public void setIGST(double IGST) {
                this.IGST = IGST;
        }

        public double getExtra() {
                return extra;
        }

        public void setExtra(double extra) {
                this.extra = extra;
        }

        public double getTotal() {
                return total;
        }

        public void setTotal(double total) {
                this.total = total;
        }

        public int getOrgId() {
                return orgId;
        }

        public void setOrgId(int orgId) {
                this.orgId = orgId;
        }

        public int getCreatedById() {
                return createdById;
        }

        public void setCreatedById(int createdById) {
                this.createdById = createdById;
        }

        public String getNote() {
                return note;
        }

        public void setNote(String note) {
                this.note = note;
        }

        public boolean isIncludeInReport() {
                return includeInReport;
        }

        public void setIncludeInReport(boolean includeInReport) {
                this.includeInReport = includeInReport;
        }



}