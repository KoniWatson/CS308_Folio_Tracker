package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Folio implements FolioInterface, Serializable {


    //HashMap with Stocks as key and the number of shares as the value
    public ArrayList<HoldingInterface> stocks;
    private String folioName;
    private String id = "";


    public Folio(String name) {
        stocks = new ArrayList<>();
        this.folioName = name;
        id = setFolioID();
        System.out.println("--FOLIO CREATED--");
    }



    /**
     * refreshes all the stock prices within the portfolio
     */
    @Override
    public void refreshPrices() {
        for (HoldingInterface h : stocks) {
            h.refreshPrices();
        }

    }

    /**
     * getTotalHolding
     * <p>
     * This method returns the total value of the whole portfolio by finding the sum
     * of the values of all shares held
     *
     * @return is the total value of the portfolio
     */
    @Override
    public double getTotalHolding() {
        double total = 0.0;

        if (stocks.size() > 0) {
            for (HoldingInterface h : stocks) {
                total += h.getValue();
            }
        }
        total = Math.round(total * 100.0) / 100.0;

        return total;

    }


    /**
     * addStock
     * <p>
     * A method for adding a stock to the HashMap of stocks that make up the portfolio
     *  @param name         The name of the stock
     * @param tickerSymbol The tickerID or the identifier of the stock
     * @param shares       The number of shares the user wishes to purchase of this individual stock
     * @return true if the stock was successfully added
     * @return false if no stock exists with the ticker symbol
     */
    @Override
    public boolean addHolding(String name, String tickerSymbol, int shares) {
        if (stocks.contains(getStock(tickerSymbol))) {
            getStock(tickerSymbol).changeShares(shares);
            System.out.println("Model.Stock already owned, purchasing additional stock!");
            return true;

        } else {

            stocks.add(new Holding(new Stock(name, tickerSymbol), shares));
            //if the stock price is negative 1, no stock exists with the symbol so delete
            if(getStock(tickerSymbol).getPrice() == -1){
                deleteStock(tickerSymbol);
                System.out.println("Stock doesnt exist!");
                return false;

            }else{

                return true;
            }
        }
    }

    /**
     * deleteStock
     * <p>
     * A method for deleting a particular stock from the portfolio
     *
     * @param tickerSymbol The tickerID or the identifier of the stock
     */
    @Override
    public void deleteStock(String tickerSymbol) {
        stocks.remove(getStock(tickerSymbol));
        System.out.println("Stock "+ tickerSymbol+" deleted!");

    }

    /**
     * getStock
     * <p>
     * A method for getting a particular stock class found by its ticker symbol
     *
     * @param tickerSymbol The tickerID or the identifier of the stock
     * @return stock The stock found to have the tickerSymbol needed
     */
    @Override
    public HoldingInterface getStock(String tickerSymbol) {
        tickerSymbol = tickerSymbol.toUpperCase();
        HoldingInterface stock;

        for (HoldingInterface s : stocks) {
            if (s.getTickerSymbol().equals(tickerSymbol)) {
                stock = s;
                return stock;
            }

        }
        return null;
    }

    @Override
    public String getFolioName() {
        return this.folioName;
    }

    /**
     * setFolioID
     * <p>
     * This method generates a String of four random characters from the given alphabet
     *
     * @return a 4 character string
     */
    @Override
    public String setFolioID() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 4; i++) {
            int index = (int) (alphabet.length() * Math.random());
            id += alphabet.charAt(index);
        }

        if (checkID(id)) {
            setFolioID();
        }

        try {
            FileWriter fw = new FileWriter("FolioIDs", true);
            fw.write(id + "\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    /**
     * getFolioID
     * <p>
     * This method returns this folios unique ID
     *
     * @return id
     */
    @Override
    public String getFolioID() { return this.id; }

    /**
     * Loads in IDs from saved file and checks if new ID is already taken
     *
     * @param id the ID to be checked against an ArrayList of all IDs in use
     * @return true if the ID is already in use and false if not
     */
    private Boolean checkID(String id) {
        ArrayList<String> IDList = new ArrayList<>();

        try {
            File idFile = new File("FolioIDs");
            Scanner sc = new Scanner(idFile);

            while (sc.hasNextLine()) {
                IDList.add(sc.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return IDList.contains(id);
    }


    /**
     * Returns all the stocks in the portfolio
     *
     * @return stockList is the list of stocks in the portfolio
     */
    @Override
    public ArrayList<HoldingInterface> getStocks() {
        ArrayList<HoldingInterface> stockList = new ArrayList<>();

        for (HoldingInterface s : stocks) {
            stockList.add(s);
        }
        return stockList;
    }

}
