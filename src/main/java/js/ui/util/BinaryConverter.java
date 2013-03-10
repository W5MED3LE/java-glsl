package js.ui.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 20.11.11
 * Time: 12:01
 * To change this template use File | Settings | File Templates.
 */
public class BinaryConverter {

    private static Map<String, Integer> MAP = new HashMap<String, Integer>();

    public static int bin(String value) {
        Integer result = MAP.get(value);
        if (result == null) {
            result = Integer.parseInt(""+value, 2);
            MAP.put(value, result);
        }
        return result;
    }

}
