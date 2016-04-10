import java.util.Arrays;

/**
 * Created by obama on 07/04/2016.
 */
public class gli {
    public static void main(String[] args) {
        String s = "vrE(vfever)eveafV(";

        String[] dots = s.split("\\(|\\)|E");

        System.out.println(s);
        System.out.println(Arrays.toString(dots));
    }
}
