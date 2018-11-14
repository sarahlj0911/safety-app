import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordEncryptionUtil {

    public static byte[] getSalt() {
        SecureRandom random = new SecureRandom();
		byte[] salt = new byte[32];
		random.nextBytes(salt);
		return salt;
    }	
    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, 1000, 256);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }
	public static String generateSecurePassword(String password, byte[] salt) {
        String returnValue = null;
        byte[] securePassword = hash(password.toCharArray(), salt);
     
        returnValue = Base64.getEncoder().encodeToString(securePassword);
     
        return returnValue;
    }
	//test***************************************************************************************
	public static void main(String[] args)
    {
        String myPassword = "pawwsord";
            
        byte[] salt = PasswordUtils.getSalt();
            
        String encryptedPassword = PasswordUtils.generateSecurePassword(myPassword, salt);
            
        System.out.println("My secure password = " + encryptedPassword);
        System.out.println("Salt value = " + salt);
        System.out.println(verifyUserPassword(myPassword, encryptedPassword, salt));
    }
    
    
    //for serverside validation******************************************************************
    public static boolean verifyUserPassword(String password_in, String password_db, byte[] salt)
    {
        boolean returnValue = false;
        // Generate New secure password with the same salt
        String password = generateSecurePassword(password_in, salt);
            
        // Check if two passwords are equal
        returnValue = password.equalsIgnoreCase(password_db);
        return returnValue;
    }
}
		