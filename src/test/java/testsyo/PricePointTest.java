package testsyo;

import com.jarlerocks.EnergyChecker;
import com.jarlerocks.PricePoint;
import org.junit.Assert;
import org.junit.Test;

public class PricePointTest {
    @Test
    public void getLowestPrices() throws Exception {
        PricePoint[] prices = EnergyChecker.getLowestPrices(3);
        Assert.assertEquals(3, prices.length);
        Assert.assertNotNull(prices[0]);
    }
}
