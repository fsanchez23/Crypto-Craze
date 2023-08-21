package fsanchez.CryptoApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Main driver class for crypto app
 * @author  Frankie Sanchez
 * @version 1.0.0
 */

public class MainActivity extends AppCompatActivity {

    /**
     * Used for top 5 crypto by calling table row, text view, and Coins
    */
    TextView btc, eth, doge, cardano, binance;
    TableRow trBtc, trEth, trDog, trCardano, trBinance;
    Coin btcCoin, ethCoin, cardCoin, binCoin, dogCoin;

    /**
     * List view is used for the coin displaying
    */
    ProgressDialog dialog;
    ListView coinList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Used for searching of coins
     * If coin is matched to id=name start new activity with info
     * else fetch the rest of the coins
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.searchBtn)
        {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }else
        {
            fetchTopCoinPrice();
            fetchRestOfCoins();
        }

        return true;
    }

    /** 
     * omCreate Function used for the instance of the main activity
     * Sets on click listeners for user input and fetches approiprate data
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setOnClickListeners();
        fetchTopCoinPrice();
        fetchRestOfCoins();
    }
    /**
     * setOnClickListeners function
     * Used to launch new activity if the user clicks 
     * a table row of a top 5 coin
     * displays: name, vol24, change24, marketcap, and price
    */
    private void setOnClickListeners() {

        trBtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name", "Bitcoin");
                intent.putExtra("vol24", btcCoin.getVol24());
                intent.putExtra("change24", btcCoin.getChange24());
                intent.putExtra("marketCap", btcCoin.getMarketCap());
                intent.putExtra("price", btc.getText().toString());
                startActivity(intent);
            }
        });

        trEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name","Ethereum");
                intent.putExtra("price", eth.getText().toString());
                intent.putExtra("vol24", ethCoin.getVol24());
                intent.putExtra("change24", ethCoin.getChange24());
                intent.putExtra("marketCap", ethCoin.getMarketCap());
                startActivity(intent);
            }
        });

        trCardano.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name", "Cardano");
                intent.putExtra("price",cardano.getText().toString());
                intent.putExtra("vol24", cardCoin.getVol24());
                intent.putExtra("change24", cardCoin.getChange24());
                intent.putExtra("marketCap", cardCoin.getMarketCap());
                startActivity(intent);
            }
        });

        trBinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name", "Binancecoin");
                intent.putExtra("price", binance.getText().toString());
                intent.putExtra("vol24", binCoin.getVol24());
                intent.putExtra("change24", binCoin.getChange24());
                intent.putExtra("marketCap", binCoin.getMarketCap());
                startActivity(intent);
            }
        });

        trDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name", "Dogecoin");
                intent.putExtra("price", doge.getText().toString());
                intent.putExtra("vol24", dogCoin.getVol24());
                intent.putExtra("change24", dogCoin.getChange24());
                intent.putExtra("marketCap", dogCoin.getMarketCap());
                startActivity(intent);
            }
        });


    }

    /**
     * initViews function
     * Used for deploying the initial views of the top 5 crypto, 
     * table rows of coin, and coin list
    */
    private void initViews() {

        dialog = new ProgressDialog(this);
        btc = findViewById(R.id.txtBitcoin);
        eth = findViewById(R.id.txtEthereum);
        doge = findViewById(R.id.txtDoge);
        cardano = findViewById(R.id.txtCardano);
        binance = findViewById(R.id.txtBNB);
        coinList = findViewById(R.id.coinList);
        trBtc = findViewById(R.id.trBitcoin);
        trBinance = findViewById(R.id.trBinanceCoin);
        trEth = findViewById(R.id.trEthereum);
        trCardano = findViewById(R.id.trCardano);
        trDog = findViewById(R.id.trDoge);


    }

    /**
     * fetchTopCoinPrice function 
     * this is used to fetcht the top 5 coin prices, market cap, 24 hour volume
     * 24 hour change, last updated, and compared to usd
    */
    public void fetchTopCoinPrice() {

        /**
         * Used as a rest client for JSON objects
         * Grabs api from coin gecko to use data
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .build();

        /**
         * A new hash map is initialized for coin data of api to be put into it
         */
        Map<String, String> map = new HashMap<>();

        /**
         * This is used for putting data in our hash map essentially allowing multiple entries 
         * and can be show at the same time 
        */
        map.put("ids", "bitcoin,ethereum,dogecoin,binancecoin,cardano");
        map.put("include_market_cap", "true");
        map.put("include_24hr_vol", "true");
        map.put("include_24hr_change", "true");
        map.put("include_last_updated_at", "true");
        map.put("vs_currencies", "usd");

        /**
         * API call used to call the request type 
         * Call is data of top 5 coin
         */
        Call<ResponseBody> call = retrofit.create(CoinGecko.class).getCoinData(map);

        /**
         * Used to print to the user that data is being loaded
         * can't be canceled
        */
        dialog.setMessage("Loading data");
        dialog.setCancelable(false);
        dialog.show();

        /**
         * This is for the calling of the response of the api
          * and adding of coin data to our chart data
          * Also can be used for debugging
        */
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("HGS", "CAME");

                dialog.dismiss();

                if(response.isSuccessful())
                {
                    try {
                        String res = response.body().string();
                        Log.d("HGS", res);

                        /**
                         * Create a new object keys for our coin data
                         * Used to handle coin data of top 5 crypto
                         * Commonly used for data implementation for JSON objects
                        */
                        JSONObject obj = new JSONObject(res);

                        Iterator<String> iter = obj.keys();
                        while(iter.hasNext()){
                            String key = iter.next();

                            JSONObject objChild = obj.getJSONObject(key);

                            /** 
                             * If object is equal to top 5 crypto names 
                             * set the data for this amd is used 
                             * to fetcht the top 5 coin prices, 
                             * market cap, 24 hour volume
                             * 24 hour change,
                             * and compared to usd
                            */
                            if(key.equals("bitcoin"))
                            {
                                btc.setText("$" + objChild.getString("usd"));
                                btcCoin = new Coin("Bitcoin", objChild.getString("usd"));
                                btcCoin.setMarketCap(objChild.getString("usd_market_cap"));
                                btcCoin.setVol24(objChild.getString("usd_24h_vol"));
                                btcCoin.setChange24(objChild.getString("usd_24h_change"));
                            }
                            else if(key.equals("ethereum"))
                            {
                                eth.setText("$" + objChild.getString("usd"));
                                ethCoin = new Coin("Dodge",objChild.getString("usd"));
                                ethCoin.setMarketCap(objChild.getString("usd_market_cap"));
                                ethCoin.setVol24(objChild.getString("usd_24h_vol"));
                                ethCoin.setChange24(objChild.getString("usd_24h_change"));
                            }
                            else if(key.equals("dogecoin"))
                            {
                                doge.setText("$" + objChild.getString("usd"));
                                dogCoin = new Coin("Dodge",objChild.getString("usd"));
                                dogCoin.setMarketCap(objChild.getString("usd_market_cap"));
                                dogCoin.setVol24(objChild.getString("usd_24h_vol"));
                                dogCoin.setChange24(objChild.getString("usd_24h_change"));
                            }
                            else if(key.equals("cardano"))
                            {
                                cardano.setText("$" + objChild.getString("usd"));
                                cardCoin = new Coin("Cardano",objChild.getString("usd"));
                                cardCoin.setMarketCap(objChild.getString("usd_market_cap"));
                                cardCoin.setVol24(objChild.getString("usd_24h_vol"));
                                cardCoin.setChange24(objChild.getString("usd_24h_change"));
                            }
                            else
                            {
                                binance.setText("$" + objChild.getString("usd"));
                                binCoin = new Coin("Binance",objChild.getString("usd"));
                                binCoin.setMarketCap(objChild.getString("usd_market_cap"));
                                binCoin.setVol24(objChild.getString("usd_24h_vol"));
                                binCoin.setChange24(objChild.getString("usd_24h_change"));
                            }
                        }

                    /**
                     * Exception use
                    */
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else
                /**
                 * Debugging purposes
                */
                {
                    Log.d("HGS", response.raw().toString());

                }
            }

            /** 
             * If failure throw error also used for debugging
            */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("HGS", "FAILURE: " + t.toString());

            }
        });
    }

    /**
     * fetchRestOfCoins function
     * Used for fetching data of the rest of the coins 
    */
    public void fetchRestOfCoins() {

        /**
         * Used as a rest client for JSON objects
         * Grabs api from coin gecko to use data
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .build();

        /**
         * A new hash map is initialized for coin data of api to be put into it
         */
        Map<String, String> map = new HashMap<>();

        /** 
         * Used id's of list of coins by name
         * also put their data in the map for use
        */
        map.put("ids", "tether,safemoon,betherchip,bitcoin,ethereum,dogecoin,binancecoin,cardano,");
        map.put("include_market_cap", "true");
        map.put("include_24hr_vol", "true");
        map.put("include_24hr_change", "true");
        map.put("include_last_updated_at", "true");
        map.put("vs_currencies", "usd");

        /**
         * Create a new array list for our coin data
         * Used to handle graph data by historic data
         * Commonly used for data implementation for JSON objects
        */
        ArrayList<Coin> coins = new ArrayList<>();

        /**
         * API call used to call the request type 
         * Call is data of coin map
         */
        Call<ResponseBody> call = retrofit.create(CoinGecko.class).getCoinData(map);

        /**
         * Used to print to the user that data is being loaded
         * can't be canceled
        */
        dialog.setMessage("Loading data");
        dialog.setCancelable(false);
        dialog.show();

        /**
         * This is for the calling of the response of the api
        * and adding of coin data to our chart data
         * Also can be used for debugging
        */
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("HGS", "CAME");

                dialog.dismiss();

                /**
                 * If response is successful load the coin data into a JSON
                 * object for iteration of data and puts that 
                 * back in the array list of entries for our graph data
                */
                if(response.isSuccessful())
                {
                    try {
                        String res = response.body().string();
                        Log.d("HGS", res);

                        /**
                         * JSON object resources
                        */
                        JSONObject obj = new JSONObject(res);
                        /**
                         * Iteration of coin data dynamically
                        */
                        Iterator<String> iter = obj.keys();
                        while(iter.hasNext()){
                            String key = iter.next();

                            JSONObject objChild = obj.getJSONObject(key);
                            Coin coin = new Coin(key,objChild.getString("usd"));
                            coin.setVol24(objChild.getString("usd_24h_vol"));
                            coin.setChange24(objChild.getString("usd_24h_change"));
                            coin.setMarketCap(objChild.getString("usd_market_cap"));
                            coins.add(coin);
                        }
                        /**
                         * Sets adapter of coin list
                        */
                        coinList.setAdapter(new CoinAdapter(MainActivity.this, coins));

                        /** 
                         * After coin is clicked it starts up a new activity and displays data 
                         * of coin dynamically
                        */
                        coinList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                                intent.putExtra("name",coins.get(pos).getName());
                                intent.putExtra("price",coins.get(pos).getPrice());
                                intent.putExtra("vol24", coins.get(pos).getVol24());
                                intent.putExtra("change24", coins.get(pos).getChange24());
                                intent.putExtra("marketCap", coins.get(pos).getMarketCap());
                                startActivity(intent);
                            }
                        });
                    /**
                     * Debugging purposes if call fails
                    */
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                /**
                 * onFailure used for debugging and error handling
                */
                }else
                {
                    Log.d("HGS", response.raw().toString());

                }
            }
            /**
             * onFailure used for debugging and error handling
            */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("HGS", "FAILURE: " + t.toString());

            }
        });
    }






}