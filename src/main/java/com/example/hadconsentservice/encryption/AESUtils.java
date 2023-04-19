package com.example.hadconsentservice.encryption;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AESUtils {
  @Value("${SECRET_KEY}")
  public String SECRET_KEY;

  public String encrypt(String plainTextData) throws Exception {
    try {
      String iv = SECRET_KEY.substring(0, 16);

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

      byte[] dataBytes = plainTextData.getBytes();
      int plaintextLength = dataBytes.length;
      byte[] plaintext = new byte[plaintextLength];
      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

      SecretKeySpec keyspec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

      cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
      byte[] encrypted = cipher.doFinal(plaintext);

      return new String(Base64.getEncoder().encode(encrypted));

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String decrypt(String cipherTextData) throws Exception {
    try {
      String iv = SECRET_KEY.substring(0, 16);

      byte[] encrypted = Base64.getDecoder().decode(cipherTextData);

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      SecretKeySpec keyspec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

      cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

      byte[] original = cipher.doFinal(encrypted);
      String originalString = new String(original);
      return originalString;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
