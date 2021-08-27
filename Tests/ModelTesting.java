import Model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelTesting {

    FolioHolder fh = new FolioHolder();
    /**
     * Uncomment if need to make a new portfolio for testing purposes

    @Test
    public void createFolioWithStocks(){
        //ID = ####
        fh.createfolio("TestFolio");
        fh.getFolio("TestFolio").addHolding("Stock1", "Msft", 10);
        fh.getFolio("TestFolio").addHolding("Stock2", "ACA", 10);
        fh.getFolio("TestFolio").addHolding("Stock3", "ARAV", 10);
        fh.saveFolio(fh.getFolio("TestFolio"));

    }
*/


    /**
     * Folio to ensure folios are created properly
     */
    @Test
    public void createFolioTest() {

        fh.createfolio("Folio1");
        fh.createfolio("Folio2");
        assertEquals(2, fh.folios.size());

        //test for first folio
        assertEquals(fh.getFolio("Folio1").getFolioID(), fh.folios.get(0).getFolioID());
        assertEquals(fh.getFolio("Folio1").getFolioName(), fh.folios.get(0).getFolioName());

        //tests for second folio
        assertEquals(fh.getFolio("Folio2").getFolioID(), fh.folios.get(1).getFolioID());
        assertEquals(fh.getFolio("Folio2").getFolioName(), fh.folios.get(1).getFolioName());
    }

    /**
     * Test to ensure a folio is deleted correctly
     */
    @Test
    public void deleteFolioTest() {

        fh.createfolio("Folio1");
        String id = fh.getFolio("Folio1").getFolioID();
        //System.out.println(id +"   "+fh.folios.get(0).getFolioID());
        assertEquals(true,fh.deleteFolio(id));
        assertEquals(null, fh.getFolio("Folio1"));
        assertEquals(null, fh.getFolioByID(id));

    }

    /**
     * Check to ensure stocks are added to the portfolio correctly
     * Tests include checks against the name, tickerID and number of shares held
     * Price isn't tested as if a share isn't found, the price is set to 0
     */
    @Test
    public void addHoldingsToFolioTest() {

        Folio f = new Folio("Folio1");
        assertEquals(true, f.addHolding("Stock1", "msft", 5));
        assertEquals(true, f.addHolding("Stock2", "aa", 10));
        assertEquals(2, f.getStocks().size());
        assertEquals(true, f.getStocks().contains(f.getStock("msft")));
        assertEquals(true, f.getStocks().contains(f.getStock("aa")));

    }

    /**
     * Test to ensure the holding value of a stock is correctly calculated
     */
    @Test
    public void stockHoldingValueTest() {

        Folio f = new Folio("Folio1");
        int shares;
        for (int i = 0; i < 10; i++) {
            shares = (int) (Math.random() * 100);
            f.addHolding("TEST", "msft", shares);
            assertEquals(shares * f.getStock("msft").getPrice(), f.getStock("msft").getValue());
            f.deleteStock("msft");

        }
    }

    /**
     * Creates a portfolio and randomly updates the three stocks prices and shares
     * Test to check the total calculated using random values is correct
     */
    @Test
    public void getTotalTest() {

        Folio f = new Folio("Folio1");
        int shares1, shares2, shares3;
        double total;

        for (int i = 0; i < 10; i++) {
            shares1 = (int) (Math.random() * 100);
            shares2 = (int) (Math.random() * 100);
            shares3 = (int) (Math.random() * 100);
            f.addHolding("Stock1", "msft", shares1);
            f.addHolding("Stock2", "aa", shares2);
            f.addHolding("Stock3", "a", shares3);
            total = (shares1 * f.getStock("msft").getPrice()) + (shares2 * f.getStock("aa").getPrice()) + (shares3 * f.getStock("a").getPrice());
            total = Math.round(total * 100.0) / 100.0;
            assertEquals(total, f.getTotalHolding());
            f.deleteStock("msft");
            f.deleteStock("aa");
            f.deleteStock("a");
        }
    }

    /**
     * Creates a folio, saves it and then loads the folio
     * Test checks to see if the folio is loaded into the program with the same name and the stocks are the same
     */
    @Test
    public void saveAndLoadTest() {

        fh.createfolio("TestSaveAndLoadFile");
        fh.getFolio("TestSaveAndLoadFile").addHolding("Stock1", "msft", 50);
        fh.saveFolio(fh.getFolio("TestSaveAndLoadFile"));
        assertEquals(true, fh.loadFolio(fh.getFolio("TestSaveAndLoadFile").getFolioID()));
        assertEquals("TestSaveAndLoadFile", fh.folios.get(fh.folios.size() - 1).getFolioName());
        assertEquals("MSFT", fh.folios.get(fh.folios.size() - 1).getStocks().get(0).getTickerSymbol());
        assertEquals(fh.getFolio("TestSaveAndLoadFile").getStocks().get(0).getPrice(), fh.folios.get(fh.folios.size() - 1).getStocks().get(0).getPrice());
        assertEquals(fh.getFolio("TestSaveAndLoadFile").getFolioID(), fh.folios.get(fh.folios.size() - 1).getFolioID());
        assertEquals(fh.getFolio("TestSaveAndLoadFile").getTotalHolding(), fh.folios.get(fh.folios.size() - 1).getTotalHolding());
    }

    /**
     * Test method to ensure setFolioID produces random 4 character ID
     */
    @Test
    public void testSetFolioID() {

        Folio f = new Folio("Karl");
        assertEquals(4, f.getFolioID().length());
        //System.out.println(f.getFolioID());
    }

    /**
     * Test to see if a new price is added to the price list following a refresh
     */
    @Test
    public void refreshStockPricesTest() {

        fh.loadFolio("7HPU");

        int sizeBeforeRefresh = fh.getFolio("TestFolio").getStock("msft").getPriceHistory().size();
        fh.getFolio("TestFolio").refreshPrices();
        int sizeAfterRefresh = fh.getFolio("TestFolio").getStock("msft").getPriceHistory().size();
        assertEquals(sizeBeforeRefresh + 1, sizeAfterRefresh);

    }


    /**
     * Test that the highest/lowest prices of a stock since the purchase are correctly calculated
     */
    @Test
    public void testHighsAndLows() {

        Stock s = new Stock("TestStock", "TESTSTOCK");
        s.setPrice(100);
        s.setPrice(50);
        s.setPrice(-10);
        s.setPrice(-105);
        s.setPrice(200);
        s.setPrice(30);

        assertEquals(200, s.getHigh());
        assertEquals(-105, s.getLow());
    }

    /**
     * Check to see if the price of a stock (from server) goes up or down in the last day
     */
    @Test
    public void isPriceUpOrDownTest() {

        Stock s = new Stock("Stock1", "Stock1");
        s.setPrice(3);
        s.setPrice(8);
        //stock not found starts at -£1 so 8 - (-1) = 9
        assertEquals(9, s.isPriceUpOrDown());
        assertEquals(true, isPriceUp(s.isPriceUpOrDown()));
        s.setPrice(-5);
        //stock not found starts at -£1 so over last 24hrs the stock will be down -£4
        assertEquals(-4, s.isPriceUpOrDown());
        assertEquals(false, isPriceUp(s.isPriceUpOrDown()));
    }

    private boolean isPriceUp(double p){

        if(p>0){
            return true;
        }else{
            return false;
        }

    }

    /**
     * Test to see if the profit/loss from the initial purchase of a stock until the current price is correct
     */
    @Test
    public void profitMadeTest() {

        //Currently every stock without a ticker symbol will have a value of -£1
        //As the test stocks don't exist, the starting value will be -£1

        Stock s = new Stock("Stock1", "Stock1");
        s.setPrice(101);
        s.setPrice(100);
        assertEquals(101, s.currentProfitOnSingleUnit());
        s.setPrice(-45);
        s.setPrice(-41);
        assertEquals(-40, s.currentProfitOnSingleUnit());
    }

    /**
     *
     * Test to see if the number of shares increases for the stock once shares are purchased
     */
    @Test
    public void increaseSharesTest() {

        Folio f = new Folio("Folio1");
        f.addHolding("Stock1", "pihpp", 30);

        f.getStock("pihpp").changeShares(15);
        f.getStock("pihpp").changeShares(10);
        int expected = 30 + 10 + 15;
        assertEquals(expected, f.getStock("pihpp").getNoOfShares());
        assertEquals(f.getStock("pihpp").getPrice()*55, f.getStock("pihpp").getValue());
    }

    /**
     * First test checks if the number of shares decreases when shares are sold
     * <p>
     * Second test checks if the stock is removed from the portfolio if the number of shares
     * is equal to or less than 0
     */
    @Test
    public void decreaseSharesTest() {

        Folio f = new Folio("Folio1");
        f.addHolding("Stock1", "yi", 10);
        f.getStock("yi").changeShares(-5);
        int expected = 5;
        assertEquals(expected, f.getStock("yi").getNoOfShares());
        assertEquals(f.getStock("yi").getPrice()*5, f.getStock("yi").getValue(), 0.005);

    }


    @Test
    public void tickerNotFoundTest(){

        Folio f = new Folio("Folio1");
        f.addHolding("FakeStock", "FakeStock", 1);
        assertEquals(null, f.getStock("FakeStock"));
        assertEquals(false, f.addHolding("FakeStock", "FakeStock", 1));
    }

    @Test
    public void folioNotFoundTest(){
        assertEquals(false, fh.loadFolio("NotAnExistingFolioID"));
    }


    @Test
    public void openFolioWithSameNameTest(){

        fh.createfolio("Folio");
        fh.createfolio("Folio(1)");
        fh.createfolio("Folio");
        assertEquals(3, fh.openFoliosWithSameName("Folio"));


    }


    @Test
    public void addHoldingThatExistsTest(){

        fh.createfolio("Folio1");
        fh.getFolio("Folio1").addHolding("Microsoft", "msft", 1);
        fh.getFolio("Folio1").addHolding("Microsoft", "msft", 1);

        HoldingInterface h = fh.getFolio("Folio1").getStock("msft");

        assertEquals(h.getPrice()*2, h.getValue(), 0.5);


    }


}