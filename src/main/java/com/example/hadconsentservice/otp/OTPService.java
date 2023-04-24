package com.example.hadconsentservice.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.hadconsentservice.bean.OtpVerification;
import com.example.hadconsentservice.encryption.AESUtils;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

@Service
public class OTPService {
	@Value("${TWILIO_ACCOUNT_SID}")
	String accountSid;

	@Value("${TWILIO_AUTH_KEY}")
	String authKey;

    @Value("${TWILIO_VERIFY_SID}")
    String verifySid;
    
    @Autowired
    AESUtils aesUtils;

    @Value("${CMS_SECRET_KEY}")
    String cmsSecretString;

    public String sendKey(OtpVerification otpVerification) throws Exception {
        Twilio.init(accountSid, authKey);
        Verification verification = Verification.creator(verifySid, aesUtils.decrypt(otpVerification.getMobileNumber(), cmsSecretString), "sms").create();
        return verification.getStatus();
    }

    public String verifyKey(OtpVerification otpVerification) throws Exception {
        Twilio.init(accountSid, authKey);
        VerificationCheck verificationCheck = VerificationCheck.creator(verifySid)
            .setTo(aesUtils.decrypt(otpVerification.getMobileNumber(), cmsSecretString))
            .setCode(aesUtils.decrypt(otpVerification.getOtp(), cmsSecretString))            
            .create();

        return verificationCheck.getStatus();
    }
}
