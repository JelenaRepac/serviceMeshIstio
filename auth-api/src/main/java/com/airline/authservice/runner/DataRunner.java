package com.airline.authservice.runner;

import com.airline.authservice.model.Admin;
import com.airline.authservice.repository.AdminRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;
import org.antlr.v4.runtime.misc.Utils;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
@Profile({"default"})
@Component
public class DataRunner implements CommandLineRunner {

  @Autowired
  private AdminRepository adminRepository;

  public static String generateSecretKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[20];
    random.nextBytes(bytes);
    Base32 base32 = new Base32();
    return base32.encodeToString(bytes);
  }

  public static String getTOTPCode(String secretKey) {
    Base32 base32 = new Base32();
    byte[] bytes = base32.decode(secretKey);
    String hexKey = Hex.encodeHexString(bytes);
    return TOTP.getOTP(hexKey);
  }

  public static void createQRCode(String barCodeData, String filePath, int height, int width)
          throws WriterException, IOException {
    BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE,
            width, height);
    try (FileOutputStream out = new FileOutputStream(filePath)) {
      MatrixToImageWriter.writeToStream(matrix, "png", out);
    }
  }

  public static String getGoogleAuthenticatorBarCode(String secretKey, String account, String issuer) {
    try {
      String label = URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20");
      String encodedSecret = URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20");
      String encodedIssuer = URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");

      return String.format("otpauth://totp/%s?secret=%s&issuer=%s", label, encodedSecret, encodedIssuer);
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }

//
  @Override
  public void run(String... args) throws Exception {
//    String secretKey = generateSecretKey();
//    System.out.println("Secret: " + secretKey);
//
//    String email = "repac01jelena@gmail.com";
//    String companyName = "airline";
//
//    String barCodeUrl = getGoogleAuthenticatorBarCode(secretKey, email, companyName);
//    System.out.println("QR Code URL: " + barCodeUrl);
//
//    String qrPath = "./totp-qr.png";
//    createQRCode(barCodeUrl, qrPath, 200, 200);
//    System.out.println("QR code saved to: " + qrPath);
//
//    // Optional - Print TOTP every 30 seconds
//    String lastCode = null;
//    while (true) {
//      String code = getTOTPCode(secretKey);
//      if (!code.equals(lastCode)) {
//        System.out.println("Current TOTP: " + code);
//      }
//      lastCode = code;
//      Thread.sleep(1000);
//    }
  }
}
