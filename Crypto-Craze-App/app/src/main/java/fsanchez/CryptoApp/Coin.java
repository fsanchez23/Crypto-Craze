package fsanchez.CryptoApp;

/**
 * Coin java class
 * @author  Frankie Sanchez
 * @version 1.0.0
 */


public class Coin {

/**
 * Coin java class
 * String name is for the coin name
 * String price is for the coin price
 * String marketCap is for the coin market cap 
 * String change24 is for the price voliatility in 24 hours
 * String vol24 is for the volume of the coin
 */

    private String name;
    private String price;
    private String marketCap;
    private String change24;
    private String vol24;


    
    /**
     * Coin constructor function
     * @param name sets name of crypto coin
     * @param price sets the price of crypto coin
    */
    public Coin(String name, String price) {
        this.name = name;
        this.price = price;
    }

    /**
     * getMarketCap function a getter function to get the market cap a crypto coin
     * @return marketcap of coin market cap 
     */
    public String getMarketCap() {
        return marketCap;
    }

    /**
     * setMarketCap a setter function that sets the market cap of a crypto coin
     * @param marketCap sets market cap
     */
    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    /**
     * getChange24 function a getter function to get the price change of a crypto coin of 24 hours
     * @return change24 coin crypto coin price change of 24 hours
     */
    public String getChange24() {
        return change24;
    }

    /**
     * setChange24 a setter function that sets the price change of a crypto coin
     * @param change24 sets price change 
     */
    public void setChange24(String change24) {
        this.change24 = change24;
    }

    /**
     * getVol24 function a getter function to get the  a crypto coin volume of 24 hours
     * @return getVol24 coin crypto coin price change of 24 hours
     */
    public String getVol24() {
        return vol24;
    }
    
    /**
     * setVol24 a setter function that sets the market cap of a crypto coin
     * @param vol24 sets market cap
     */
    public void setVol24(String vol24) {
        this.vol24 = vol24;
    }

    /**
     * getName function a getter function to get the a crypto coin name
     * @return name of the crypto coin
     */
    public String getName() {
        return name;
    }

    /**
     * setName a setter function that sets the market cap of a crypto coin
     * @param name sets name of the crypto coin
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getPrice function a getter function to get the  a crypto coin volume of 24 hours
     * @return price coin crypto coin price change of 24 hours
     */
    public String getPrice() {
        return price;
    }

    /**
     * setPrice a setter function that sets the market cap of a crypto coin
     * @param price sets price of crypto coin
     */
    public void setPrice(String price) {
        this.price = price;
    }

}
