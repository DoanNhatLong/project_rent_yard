package com.example.project_rent_yard.service;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

@Service
public class VnPayService implements IVnPayService {

    private static final String VNP_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private static final String TMN_CODE = "9MO7DQLD";
    private static final String HASH_SECRET = "4FRP791QYFLRTKSQL89QQ26136RGGNHD";
    private static final String RETURN_URL = "http://rentyard.local/vnpay-return";

    @Override
    public String createVnPayUrl(Integer bookingId, double amount) {

        Map<String, String> params = new TreeMap<>();

        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", TMN_CODE);
        params.put("vnp_Amount", String.valueOf((long) (amount * 100)));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", bookingId.toString());
        params.put("vnp_OrderInfo", "Thanh toan booking " + bookingId);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", RETURN_URL);
        params.put("vnp_IpAddr", "127.0.0.1");

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        params.put("vnp_CreateDate",
                new SimpleDateFormat("yyyyMMddHHmmss").format(cal.getTime()));

        /* ========= 1️⃣ TẠO HASH DATA (URL ENCODE VALUE) ========= */
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            hashData.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }
        hashData.deleteCharAt(hashData.length() - 1);

        String secureHash = hmacSHA512(HASH_SECRET, hashData.toString());

        System.out.println("VNPay HASH DATA = " + hashData);
        System.out.println("VNPay SECURE HASH = " + secureHash);

        /* ========= 2️⃣ TẠO QUERY STRING ========= */
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            query.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }

        query.append("vnp_SecureHashType=HmacSHA512");
        query.append("&vnp_SecureHash=").append(secureHash);

        return VNP_URL + "?" + query;
    }


    /* ================== HÀM KÝ HASH ================== */
    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey =
                    new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);

            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception e) {
            throw new RuntimeException("VNPay HMAC error", e);
        }
    }

}
