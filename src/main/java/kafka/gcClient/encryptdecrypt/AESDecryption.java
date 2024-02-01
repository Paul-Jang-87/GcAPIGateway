package kafka.gcClient.encryptdecrypt;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@PropertySource("classpath:application.properties")
public class AESDecryption {

	private static String AES_SECRET_KEY = "";
	
	@Value("${aes.secret.key.encoded}")//application.property 파일에 base64로 인코딩 된 키를 가지고 온다. 
    private String encodedAesSecretKey;


    @PostConstruct
    private void init() {
    	AES_SECRET_KEY = decodeBase64(encodedAesSecretKey);//base64로 인코딩 된 키를 복호화.
    }
	
    public static String decodeBase64(String encodedInput) {//base64로 인코딩 된 키를 복호화.
        byte[] decodedBytes = Base64.getDecoder().decode(encodedInput);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static String decrypt(String encryptedIdOrPwd) throws Exception {//AES로 암호화 된 ID 혹은 PWD 복호화 후 리턴.
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(AES_SECRET_KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedIdOrPwd));
        return new String(decryptedBytes);
    }
    
}
