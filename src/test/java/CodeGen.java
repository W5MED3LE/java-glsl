import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: jgg
 * Date: 20.11.11
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */
public class CodeGen {

    @Test
    public void test() {
        for (int i = 0; i < 256; ++i) {
            String bin = Integer.toBinaryString(i);
            while (bin.length() < 8) {
                bin = "0" + bin;
            }

            int cnt = get1Count(bin);

            if (cnt == 6) {
                System.out.println("LMAP.put(bin(\"" + bin + "\"), paths());");
            }
        }
    }

    private int get1Count(String str) {
        return str.replaceAll("[^1]", "").length();
    }

}
