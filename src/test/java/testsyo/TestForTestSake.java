package testsyo;

import com.jarlerocks.EnergyChecker;

import org.junit.Assert;

import org.junit.Test;

public class TestForTestSake {
    @Test
    public void test_number_1() throws Exception {
        Assert.assertEquals("this",EnergyChecker.getHome().toString());
    }
}
