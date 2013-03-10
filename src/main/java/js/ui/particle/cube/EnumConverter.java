package js.ui.particle.cube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jgg
 * Date: 02.08.12
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
public class EnumConverter {

    public static int encode(Enum e) {
        if (e == null) {
            return 0;
        }
        return 1 << e.ordinal();
    }

    public static int encode(Enum... enums) {
        return encode(Arrays.asList(enums));
    }

    public static int encode(Collection<? extends Enum> enums) {
        int result = 0;
        for (Enum e : enums) {
            result += encode(e);
        }
        return result;
    }

    public static <E extends Enum> Collection<E> decode(int signature, E[] values) {
        List<E> result = new ArrayList<E>();
        for (E e : values) {
            if ((encode(e) & signature) != 0) {
                result.add(e);
            }
        }

        return result;
    }

}
