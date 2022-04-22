package app.com.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RsaSecureEncoder {

	public static String RSA_WEB_KEY = "_RSA_WEB_Key_"; // 개인키 session key
	public static String RSA_INSTANCE = "RSA"; // rsa transformation
	
	 /**
     * 복호화
     * 
     * @param privateKey
     * @param securedValue
     * @return
     * @throws Exception
     */
    public String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance(RsaSecureEncoder.RSA_INSTANCE);
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }
 
    /**
     * 16진 문자열을 byte 배열로 변환한다.
     * 
     * @param hex
     * @return
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) { return new byte[] {}; }
 
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }
 
    /**
     * rsa 공개키, 개인키 생성
     * 
     * @param request
     */
    public Map<String, Object> initRsa(HttpServletRequest request) {
        HttpSession session = request.getSession();
 
        KeyPairGenerator generator;
        Map<String, Object> rsaMap = new HashMap<String, Object>();
        try {
            generator = KeyPairGenerator.getInstance(RsaSecureEncoder.RSA_INSTANCE);
            generator.initialize(1024);
 
            KeyPair keyPair = generator.genKeyPair();
            KeyFactory keyFactory = KeyFactory.getInstance(RsaSecureEncoder.RSA_INSTANCE);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
 
            session.setAttribute(RsaSecureEncoder.RSA_WEB_KEY, privateKey); // session에 RSA 개인키를 세션에 저장
 
            RSAPublicKeySpec publicSpec = (RSAPublicKeySpec) keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            String publicKeyModulus = publicSpec.getModulus().toString(16);
            String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

            // rsaMap.put("privateKey", privateKey);
            rsaMap.put("RSAModulus", publicKeyModulus);
            rsaMap.put("RSAExponent", publicKeyExponent);
            //request.setAttribute("RSAModulus", publicKeyModulus); // rsa modulus 를 request 에 추가
           // request.setAttribute("RSAExponent", publicKeyExponent); // rsa exponent 를 request 에 추가
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsaMap;
    }
}