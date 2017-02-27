import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sidney on 08/12/2016.
 */
public class MyApp {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<>();
        map.put("age", "20");
        map.put("name", "sid");

        StringBuilder sb = new StringBuilder();

        map.forEach((k, v) -> sb.append(String.format(",\"%s\"=\"%s\"", k, v)));
        sb.deleteCharAt(0);
        System.out.println(sb.toString());
    }
}
