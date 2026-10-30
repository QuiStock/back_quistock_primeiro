package Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordService {

    private static final int cost = 12;

    // Hash passwords before storing them.
    public static String genHash(String pass) {
        return BCrypt.withDefaults().hashToString(cost, pass.toCharArray());
    }

    // Handles verify
    public static boolean verify(String pass, String hash) {
        // Applying the method logic, using the provided values
        if (pass == null || hash == null) return false;
        if (needsMigration(hash)) return pass.equals(hash);
        return BCrypt.verifyer().verify(pass.toCharArray(), hash).verified;
    }

    // Handles needs, Migration
    public static boolean needsMigration(String hash) {
        // Applying the method logic, using the provided values
        return hash != null && !hash.startsWith("$2");
    }

}
