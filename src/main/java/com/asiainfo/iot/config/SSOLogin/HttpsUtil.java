//package com.asiainfo.iot.config.SSOLogin;
//
//import javax.net.ssl.*;
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.security.KeyManagementException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
///**
// * @date 2018-04-25
// * https工具类
// */
//public class HttpsUtil{
//
//    private static class TrustAnyTrustManager implements X509TrustManager {
//
//        public void checkClientTrusted(X509Certificate[] chain, String authType)
//                throws CertificateException {
//        }
//
//        public void checkServerTrusted(X509Certificate[] chain, String authType)
//                throws CertificateException {
//        }
//
//        public X509Certificate[] getAcceptedIssuers() {
//            return new X509Certificate[] {};
//        }
//    }
//
//    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    }
//
//
//    /**
//     * post方式请求服务器(https协议)
//     * @param url
//     *            请求地址
//     * @param content
//     *            参数
//     * @return
//     * @throws NoSuchAlgorithmException
//     * @throws KeyManagementException
//     * @throws IOException
//     */
//    public static String post(String url, String content)
//            throws NoSuchAlgorithmException, KeyManagementException,
//            IOException {
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
//                new java.security.SecureRandom());
//
//        URL console = new URL(url);
//        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
//        conn.setDoOutput(true);
//        conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
//        conn.setRequestProperty("accept", "application/json");
//        conn.setRequestProperty("connection", "Keep-Alive");
//        conn.setSSLSocketFactory(sc.getSocketFactory());
//        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
//        conn.connect();
//        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//        out.write(content.getBytes("utf-8"));
//        // 刷新、关闭
//        out.flush();
//        out.close();
//        InputStream is = conn.getInputStream();
//        if (is != null) {
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = is.read(buffer)) != -1) {
//                outStream.write(buffer, 0, len);
//            }
//            is.close();
//            if(outStream!=null){
//                return new String(outStream.toByteArray(),"utf-8");
//            }
//        }
//        return null;
//    }
//
//    public static String encryptForMD5(String souce) {
//        if (souce == null || souce.length() == 0) {
////            logger.error("警告 ： 空字符串不可以作MD5加密 !");
//            return null;
//        }
//        StringBuffer hexString = new StringBuffer();
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(souce.getBytes());
//            byte hash[] = md.digest();
//            for (int i = 0; i < hash.length; i++) {
//                if ((0xff & hash[i]) < 0x10) {
//                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
//                } else {
//                    hexString.append(Integer.toHexString(0xFF & hash[i]));
//                }
//            }
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
////            logger.error("当请求特定的加密算法<MD5>而它在该环境中不可用时抛出此异常:" + e.getMessage(), e);
//        }
//        return null;
//    }
//
//    public static void main(String[] args) {
//        try {
//            String str= HttpsUtil.post("https://api.mch.weixin.qq.com/pay/unifiedorder", "");
//            System.out.println(str);
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
