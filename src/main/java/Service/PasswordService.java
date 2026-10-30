package Service;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordService {

    private static final int cost = 12;

    // Hash passwords before storing them.
    public static String genHash(String pass) {
        return BCrypt.withDefaults().hashToString(cost, pass.toCharArray());
    }

    public static boolean verify(String pass, String hash) {
        if (pass == null || hash == null) return false;
        if (needsMigration(hash)) return pass.equals(hash);
        return BCrypt.verifyer().verify(pass.toCharArray(), hash).verified;
    }

    public static boolean needsMigration(String hash) {
        return hash != null && !hash.startsWith("$2");
    }

}
