package com.cmb_collector.services;

public class ServiceConnector {

    public static int MY_SOCKET_TIME = 60000;

    public static String APP_VERSION = "1.0.0.1";

    public static String base_URL = "http://cmbindiaapi.webinfotechedu.com/BISkin.asmx/";
    public static String sms_url="https://bulksms.webinfotechin.co.in/new/api/api_http.php";
    public static String recharge_url = "https://myrc.in/recharge/api";

    public static String ORC_VOUCHER_MONTH = base_URL + "GET_ORC_VoucherMonth";

    public static String ORC_VIEW_OWNORC = base_URL + "GET_ORC_ViewOwnORC";
 }