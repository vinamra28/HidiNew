package com.example.hp.hidi2.app;

/**
 * Created by Ravi on 08/07/15.
 */
public class Config {
    // server URL configuration
    public static final String URL_REQUEST_SMS = "http://hidi.org.in/hidi1/Auth/getotp.php";
    public static final String URL_VERIFY_OTP = "http://hidi.org.in/hidi1/Auth/verifyotp2.php";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "Hidi";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";
}
