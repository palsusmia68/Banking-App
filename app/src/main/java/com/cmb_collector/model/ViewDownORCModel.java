package com.cmb_collector.model;

public class ViewDownORCModel {
    private String voucherMonth;
    private String collecterCode;
    private String collectorName;
    private String dateFrom;
    private String dateTo;
    private String totalBusiness;
    private String totalCommission;
    private String netPayment;
    private final static long serialVersionUID = 7583571389019336229L;

    /**
     * No args constructor for use in serialization
     */
    public ViewDownORCModel() {
    }

    /**
     * @param netPayment
     * @param collecterCode
     * @param dateTo
     * @param dateFrom
     * @param totalBusiness
     * @param totalCommission
     * @param collectorName
     * @param voucherMonth
     */
    public ViewDownORCModel(String voucherMonth, String collecterCode, String collectorName, String dateFrom, String dateTo, String totalBusiness, String totalCommission, String netPayment) {
        super();
        this.voucherMonth = voucherMonth;
        this.collecterCode = collecterCode;
        this.collectorName = collectorName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.totalBusiness = totalBusiness;
        this.totalCommission = totalCommission;
        this.netPayment = netPayment;
    }

    public String getVoucherMonth() {
        return voucherMonth;
    }

    public void setVoucherMonth(String voucherMonth) {
        this.voucherMonth = voucherMonth;
    }

    public String getCollecterCode() {
        return collecterCode;
    }

    public void setCollecterCode(String collecterCode) {
        this.collecterCode = collecterCode;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTotalBusiness() {
        return totalBusiness;
    }

    public void setTotalBusiness(String totalBusiness) {
        this.totalBusiness = totalBusiness;
    }

    public String getTotalCommission() {
        return totalCommission;
    }

    public void setTotalCommission(String totalCommission) {
        this.totalCommission = totalCommission;
    }

    public String getNetPayment() {
        return netPayment;
    }

    public void setNetPayment(String netPayment) {
        this.netPayment = netPayment;
    }
}
