package js.ui.util;

import java.lang.reflect.Field;

/**
 * Created by jgruber on 02.05.2016.
 */
public class LibUtil {

    public static void setDefaultLibPath() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("java.library.path", "lib");
        final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
        sysPathsField.setAccessible(true);
        sysPathsField.set(null, null);
    }

}
