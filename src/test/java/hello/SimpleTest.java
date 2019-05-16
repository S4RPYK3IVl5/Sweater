package hello;

import org.junit.Assert;
import org.junit.Test;

public class SimpleTest {
    @Test
    public void test()
    {
        int x = 26;
        int y = 2;
        Assert.assertEquals(52, x*y);
    }
}
