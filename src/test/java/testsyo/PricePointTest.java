package testsyo;

import com.jarlerocks.EnergyChecker;
import com.jarlerocks.PricePoint;
import org.junit.Assert;
import org.junit.Test;

public class PricePointTest {
    @Test
    public void getLowestPrices() throws Exception {
        int count = 10;
        PricePoint[] prices = EnergyChecker.getLowestPrices(count);
        Assert.assertEquals(count, prices.length);
        for(PricePoint price : prices) {
            Assert.assertNotNull(price);
        }

        //System.out.println("Cheapest prices are: ");
        //for(PricePoint point : prices) {
        //    System.out.println("Starting " + point.getTime() + ": " + point.getPrice());
        //}
    }
}
