package Entities;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordEncryption {

    private static final String SECRET_KEY = "0123456789abcdef"; // Clé secrète utilisée pour le cryptage

    public static String encrypt(String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedPassword) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedBytes);
    }

    public static void main(String[] args) throws Exception {
        String originalPassword = "mm";

        // Cryptage du mot de passe
        String encryptedPassword = encrypt(originalPassword);
        System.out.println("Mot de passe crypté : " + encryptedPassword);

        // Décryptage du mot de passe
        String decryptedPassword = decrypt(encryptedPassword);
        System.out.println("Mot de passe décrypté : " + decryptedPassword);
    }
}
