import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestSpec {
    @Test
    public void testJavaCode() {
        List<String> upcList = new ArrayList<>();
        upcList.add("Something");
        upcList.add("Something");
        upcList.add("Something");
        upcList.add("Something");
        upcList.add("Something");
        upcList.add("Something");
        String print = upcList.toString();
        Assert.assertTrue(print.length() > 0);
    }
}
