package Model;//package folio_tracker;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Stock implements StockInterface, Serializable {

    private String name;
    private String tickerSymbol;
    public ArrayList<Price> priceList;

    public class Price implements Serializable {
        public double price;
        public LocalDateTime time;

        public Price(double price, LocalDateTime time) {
            this.price = price;
            this.time = time;
        }
    }

    public Stock(String n, String ts) {
        this.name = n;
        this.tickerSymbol = ts.toUpperCase();
        this.priceList = new ArrayList<>();
        this.setPrice(this.getPrice());
    }

    /**
     * get the price of a stock from the server, parse to a double
     *
     * @return the price of the stock as fetched from the server
     */
    @Override
    public double getPrice() {
        String line = null;

        try {
            line = StrathQuoteServer.getLastValue(tickerSymbol);
        } catch (WebsiteDataException | NoSuchTickerException e) {
            return -1;
        }
        return Double.parseDouble(line);
    }

    /**
     * Adds the price of a stock at the current time to the list of prices
     *
     * @param p The price to be added to the stock
     */
    @Override
    public void setPrice(double p) { this.priceList.add(new Price(p, LocalDateTime.now())); }

    /**
     * Returns the ticker ID of a stock
     *
     * @return the ticker symbol
     */
    @Override
    public String getTickerID() { return this.tickerSymbol.toUpperCase(); }

    /**
     * Gets name of stock
     *
     * @return the name of a stock
     */
    @Override
    public String getName() { return this.name; }

    /**
     * This method gets the
     *
     * @return the most recent price in the list of previous prices
     */
    @Override
    public double getPriceLocally() {
        if (Duration.between(this.timeOfLastPrice(), LocalDateTime.now()).getSeconds() > 30) {
            return getPrice();
        }
        return this.priceList.get(priceList.size() - 1).price;
    }

    /**
     * Used to find the time of the last stock update
     *
     * @return Time of the last update to the price of the stock
     */
    @Override
    public LocalDateTime timeOfLastPrice() {
        return this.priceList.get(this.priceList.size() - 1).time;
    }

    /**
     * This method gets the prices from all the last updates of a stock
     *
     * @return a list of all the stock prices saved in memory
     */
    @Override
    public ArrayList<Double> getPriceHistory() {
        ArrayList<Double> list = new ArrayList<>();

        for (Price p : priceList) {
            list.add(p.price);
        }

        return list;
    }

    @Override
    public double getLow() {
        double low = priceList.get(0).price;

        for (int i = 1; i < priceList.size(); i++) {

            if (priceList.get(i).price < low) {
                low = priceList.get(i).price;
            }
        }

        return low;
    }

    @Override
    public double getHigh() {
        double high = 0.0;

        for (Price p : priceList) {

            if (p.price > high) {
                high = p.price;
            }
        }

        return high;
    }

    /**
     * Prints statements telling the user how they will fair if the sell their stock at its current price
     *
     * @return the difference between the current price and the price the stock was purchased at
     */
    @Override
    public double currentProfitOnSingleUnit() { return getPriceLocally() - priceBoughtAt(); }

    /**
     * Checks if there has been a price update in the last 24 hours
     *
     * @return false if there has not
     */
    public boolean updateInLastDay() { return (Duration.between(LocalDateTime.now(), priceList.get(priceList.size() - 2).time).getSeconds() < 24 * 60 * 60); }

    /**
     * Finds the oldest possible price within the last 24 hours
     *
     * @return comparator The oldest price within the last 24 hours
     */
    public Price getPriceOneDayAgo() {
        Price comparator = null;

        //i = ...size()-2 as ...size()-1 is the current price so no need to compare
        for (int i = priceList.size() - 2; i >= 0; i--) {
            if (Duration.between(LocalDateTime.now(), priceList.get(i).time).getSeconds() <= 86400) {
                comparator = priceList.get(i);
            }
        }

        return comparator;
    }

    /**
     * Compare the value of two doubles
     *
     * @param old The oldest possible price (within 24 hours) of a stock
     * @param now The current price of a stock
     * @return "SAME" if the values are the same
     */
    public double comparePrice(double old, double now) {
        double d = now - old;

        d = Math.round(d * 100.0) / 100.0;

        return d;
    }

    /**
     * Checks if the price has went up or down in either the last 24hrs, and if no update,
     * since the last update available
     *
     * NOTE: if method return >0, make text green
     *       if method return <0, make text red???
     * <p>
     *
     * @return 0 as there is no price to compare to
     * @return result of comparePrice(old, now) function with the last updated price within 24 hours and current value
     * @return result of comparePrice(old, now) function with the last updated price and current value
     */
    @Override
    public double isPriceUpOrDown() {
        if (priceList.size() < 2) {
            return 0;
        } else if (updateInLastDay()) {
            return comparePrice(getPriceOneDayAgo().price, getPriceLocally());
        } else {
            return comparePrice(priceList.get(priceList.size() - 2).price, getPriceLocally());
        }
    }

    /**
     * Returns the price the stock was initially bought for
     *
     * @return the first price stored for a stock
     */
    public double priceBoughtAt() { return priceList.get(0).price; }

}
