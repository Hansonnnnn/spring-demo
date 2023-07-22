package com.example.springdemo.seata.core.auth;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class RamSignAdapter {

  private static final String SHA256_ENCRYPT = "HmacSHA256";

  private static final String PREFIX = "aliyun_v4";

  private static final String CONSTANT = "aliyun_v4_request";

  private static final String DEFAULT_REGION = "cn-beijing";

  private static final String DEFAULT_PRODUCT_CODE = "seata";

  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMdd");

  public static String getRamSign(String encryptText, String encryptKey) {
    try {
      String[] encryptData = encryptKey.split(",");
      byte[] data = getProductSigningKey(
        encryptKey,
        LocalDateTime.ofEpochSecond(Long.parseLong(encryptData[2]) / 1000, 0, ZoneOffset.UTC).format(DTF),
        DEFAULT_REGION, DEFAULT_PRODUCT_CODE, SHA256_ENCRYPT
      );
      SecretKey secretKey = new SecretKeySpec(data, SHA256_ENCRYPT);
      Mac mac = Mac.getInstance(SHA256_ENCRYPT);
      mac.init(secretKey);
      byte[] text = encryptText.getBytes(StandardCharsets.UTF_8);
      byte[] textFile = mac.doFinal(text);
//      return ConfigTools.byte2Base64(textFile);
      return null;
    } catch (Exception e) {
      throw new RuntimeException("get ram sign with hmacSHA1Encrypt fail", e);
    }
  }

  /**
   * get date level signing key
   *
   * @param secret     secret
   * @param date       data, yyyyMMdd
   * @param signMethod HmacSHA256
   * @return date level signing key
   */
  private static byte[] getDateSigningKey(String secret, String date, String signMethod) {
    try {
      Mac mac = Mac.getInstance(signMethod);
      mac.init(new SecretKeySpec((PREFIX + secret).getBytes(StandardCharsets.UTF_8), signMethod));
      return mac.doFinal(date.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("unsupport Algorithm:" + signMethod);
    } catch (InvalidKeyException e) {
      throw new RuntimeException("InvalidKey");
    }
  }

  /**
   * get date&region level signing key
   *
   * @param secret     secret
   * @param date       data
   * @param region     region
   * @param signMethod HmacSHA256
   * @return date&region level signing key
   */
  private static byte[] getRegionSigningKey(String secret, String date, String region, String signMethod) {
    byte[] dateSignkey = getDateSigningKey(secret, date, signMethod);
    try {
      Mac mac = Mac.getInstance(signMethod);
      mac.init(new SecretKeySpec(dateSignkey, signMethod));
      return mac.doFinal(region.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("unsupport Algorithm:" + signMethod);
    } catch (InvalidKeyException e) {
      throw new RuntimeException("InvalidKey");
    }
  }

  /**
   * get date&region&product level signing key
   *
   * @param secret      secret
   * @param date        date
   * @param region      region
   * @param productCode productCode
   * @param signMethod  signMethod
   * @return date&region&product level signing key
   */
  private static byte[] getProductSigningKey(String secret, String date, String region, String productCode, String signMethod) {
    byte[] regionSignkey = getRegionSigningKey(secret, date, region, signMethod);
    try {
      Mac mac = Mac.getInstance(signMethod);
      mac.init(new SecretKeySpec(regionSignkey, signMethod));
      byte[] thirdSigningKey = mac.doFinal(productCode.getBytes(StandardCharsets.UTF_8));
      mac = Mac.getInstance(signMethod);
      mac.init(new SecretKeySpec(thirdSigningKey, signMethod));
      return mac.doFinal(CONSTANT.getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("unsupport Algorithm:" + signMethod);
    } catch (InvalidKeyException e) {
      throw new RuntimeException("InvalidKey");
    }
  }
}
