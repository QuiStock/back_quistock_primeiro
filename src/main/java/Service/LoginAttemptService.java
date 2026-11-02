package Service;

import java.util.concurrent.ConcurrentHashMap;

//counts failed logins in memory and blocks repeated attempts for a while
public final class LoginAttemptService {

    //failures from same email and ip
    private static final int MAX_PER_EMAIL_IP = 5;
    //failures from same ip, any email
    private static final int MAX_PER_IP = 20;
    //cooldown time, over 15 minutes
    private static final long WINDOW_MS = 15 * 60 * 1000L;

    private record Entry(int count, long windowStart) {}

    //concurrentHashMap because it can receive multiple requests simultaneously
    private static final ConcurrentHashMap<String, Entry> attempts = new ConcurrentHashMap<>();

    //checks if attempts are bigger than the limiar
    public static boolean isBlocked(String ip, String email) {
        return count(ip + "|" + norm(email)) >= MAX_PER_EMAIL_IP
                || count("ip|" + ip) >= MAX_PER_IP;
    }

   //counts a failed attempt for ip and email, and ip independently
    public static void recordFailure(String ip, String email) {
        hit(ip + "|" + norm(email));
        hit("ip|" + ip);
    }

    //restart all attempts if login is succesfull
    public static void recordSuccess(String ip, String email) {
        attempts.remove(ip + "|" + norm(email));
    }

    //counts attemps associated with the key (ip|user)
    private static int count(String key) {
        Entry e = attempts.get(key);
        if (e == null) return 0;
        if (System.currentTimeMillis() - e.windowStart() > WINDOW_MS) {
            attempts.remove(key, e);
            return 0;
        }
        return e.count();
    }

    //receives an entry and gets it back withen.getValue, gets inicial time and checks if 15 minutes have passed
    private static void hit(String key) {
        long now = System.currentTimeMillis();
        if (attempts.size() > 10_000) {
            attempts.entrySet().removeIf(en -> now - en.getValue().windowStart() > WINDOW_MS);
        }
        //case 1: creates a new window, starting on 1
        //case 2: gets an old/existent window, in (old, ignored)
        attempts.merge(key, new Entry(1, now), (old, ignored) ->
                now - old.windowStart() > WINDOW_MS
                        ? new Entry(1, now)
                        : new Entry(old.count() + 1, old.windowStart()));
    }

    private static String norm(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}