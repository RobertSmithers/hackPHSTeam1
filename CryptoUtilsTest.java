package net.codejava.crypto;
 
import java.io.File;
 
/**
 * A tester for the CryptoUtils class.
 * @author www.codejava.net
 *
 */
public class CryptoUtilsTest {
    public static void main(String[] args) {
        String key = "b2Hs0AkwpVme@duW";
        File inputFile = new File("encryption.txt");
        File encryptedFile = new File("accountDetails.encrypted");
        File decryptedFile = new File("accountDetails.decrypted");
         
        try {
            CryptoUtils.encrypt(key, inputFile, encryptedFile);
            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
}