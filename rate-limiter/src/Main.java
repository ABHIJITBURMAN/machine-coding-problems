import model.User;
import model.UserTier;
import service.RateLimiter;
import service.RateLimiterImpl;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        RateLimiter rateLimiter = new RateLimiterImpl();
        User user1 = new User("1234", UserTier.ENTERPRISE,5);
        User user2 = new User("1234", UserTier.ENTERPRISE,1);
        User user3 = new User("1234", UserTier.ENTERPRISE,2);
        User user4 = new User("1234", UserTier.ENTERPRISE,3);
        User user5 = new User("1234", UserTier.ENTERPRISE,4);
        System.out.println(rateLimiter.allowRequest(user2));
        System.out.println(rateLimiter.allowRequest(user3));
        System.out.println(rateLimiter.allowRequest(user4));
        System.out.println(rateLimiter.allowRequest(user5));
        System.out.println(rateLimiter.allowRequest(user1));



    }
}