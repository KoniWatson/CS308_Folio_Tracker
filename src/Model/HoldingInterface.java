package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface HoldingInterface {

    void changeShares(int change);
    int getNoOfShares();
    void setHoldingValue();
    double getValue();
    String getTickerSymbol();
    String getName();
    double getPrice();
    double getProfit();
    double getChangeOfPrice();
    void refreshPrices();
    ArrayList<Double> getPriceHistory();
    LocalDateTime getLocalDateTime();
    void setPrice(double price);
    double getHighest();
    double getLowest();
    String getUpOrDown();
    void refreshUpOrDown();
}
