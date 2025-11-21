import org.mindrot.jbcrypt.BCrypt;

public class generarhash {
    public static void main(String[] args) {
        String hash = BCrypt.hashpw("1234", BCrypt.gensalt(10));
        System.out.println(hash);
    }

}
