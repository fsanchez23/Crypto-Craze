package fsanchez.CryptoApp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
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
 * DetailActivity java class
 * Extends the Main application
 * @author  Frankie Sanchez
 * @version 1.0.0
 */

public class SearchActivity extends AppCompatActivity {

    EditText searchEdit;
    ListView coinSearchList;
    ProgressDialog dialog;
    ArrayList<String> coins;
    ArrayList<String> searchedCoins;
    Map<String, String> map;

    /**
     * SeachActivity java class
     * Able to dynamically call data from the api coin gecko
     * EditText searchEdit; for search 
     * ListView coinSearchList; of searched coin
     * ProgressDialog dialog; dialog for user
     * ArrayList<String> coins; array list of coins from api
     * ArrayList<String> searchedCoins; array list of searched crypto
     * Map<String, String> map; Mapping for data
    */

    /**
     * OnCreate function 
     * used for creating content of searched coin
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        loadTickers();
        setOnClickListeners();
    }
    /**
     * OnClick Listeners for coin searched
    */
    private void setOnClickListeners() {
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                searchTicker(query.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Adds the searched coins to an array list 
     * where api is able to call the coin names
     * and display data of those coins
    */
    private void searchTicker(String query) {

        searchedCoins = new ArrayList<>();

        for(int i = 0; i < coins.size(); i++)
        {
            if(coins.get(i).toLowerCase().contains(query.toLowerCase()))
            {
                searchedCoins.add(coins.get(i));
            }

        }
        /**
         * Fectes the coin adapter of searched coin and 
         * on the click listener start fetching data from the coin map data
         * is able to display multiple data this way and simpler for 
         * handling dynamic data
        */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchedCoins);
        coinSearchList.setAdapter(adapter);

        coinSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fetchCoinData(map.get(coins.get(i)));
            }
        });

    }
    /**
     * Finds intially views 
     * by id
    */
    private void initViews() {
        dialog = new ProgressDialog(this);
        searchEdit = findViewById(R.id.searchEdit);
        coinSearchList = findViewById(R.id.coinSearchList);
    }
    /**
     * loadTickers function
     * used for coin searching
    */
    public void loadTickers() {
        /**
         * Used as a rest client for JSON objects
         * Grabs api from coin gecko to use data
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .build();

        /**
         * API call used to call the request type 
         * Call is coin search data
         */
        Call<ResponseBody> call = retrofit.create(CoinGecko.class).getAllTickers();

        /** 
         * new array list for coins and new hash map for data
         * of searched coins
        */
        coins =  new ArrayList<>();
        map = new HashMap<>();

        /**
         * Used to print to the user that data is being loaded
         * can't be canceled
        */
        dialog.setMessage("Loading data");
        dialog.setCancelable(false);
        dialog.show();

        /**
         * This is for the calling of the response of the api
         * and adding of coin data of our searched coins
         * Also can be used for debugging
        */
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("HGS", "CAME");

                dialog.dismiss();

                /**
                 * If response is successful load the searched coin data into a JSON
                 * object and array for iteration of names
                 * back in the object hash map and coin list
                */
                if(response.isSuccessful())
                {
                    try {
                        String res = response.body().string();
                        Log.d("HGS", res);

                        JSONArray arr = new JSONArray(res);

                        for(int i = 0; i < arr.length(); i++)
                        {
                            JSONObject obj = arr.getJSONObject(i);
                            coins.add(obj.getString("name"));
                            map.put(obj.getString("name"), obj.getString("id"));
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else
                /**
                 * Debugging purposes if call fails
                */
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
    /**
     * fetchCoinData used for finding id=name and findind data
    */
    public void fetchCoinData(String id)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .build();

        /** 
         * New hash map for coin data
        */
        Map<String, String> map = new HashMap<>();

        map.put("ids", id);
        map.put("include_market_cap", "true");
        map.put("include_24hr_vol", "true");
        map.put("include_24hr_change", "true");
        map.put("include_last_updated_at", "true");
        map.put("vs_currencies", "usd");

        /**
         * API call used to call the request type 
         * Call is searched coin data
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
                 * If response is successful load the data into a JSON
                 * object and an array for iteration of data and
                 * puts it back sets data into an intent of the the second activity
                */
                if(response.isSuccessful())
                {
                    try {
                        String res = response.body().string();
                        Log.d("HGS", res);

                        JSONObject obj = new JSONObject(res);

                        Iterator<String> iter = obj.keys();
                        Coin coin = null;

                        while(iter.hasNext()){
                            String key = iter.next();

                            JSONObject objChild = obj.getJSONObject(key);
                            coin = new Coin(key,objChild.getString("usd"));
                            coin.setVol24(objChild.getString("usd_24h_vol"));
                            coin.setChange24(objChild.getString("usd_24h_change"));
                            coin.setMarketCap(objChild.getString("usd_market_cap"));
                           }

                        Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                        intent.putExtra("name",coin.getName());
                        intent.putExtra("price",coin.getPrice());
                        intent.putExtra("vol24", coin.getVol24());
                        intent.putExtra("change24", coin.getChange24());
                        intent.putExtra("marketCap", coin.getMarketCap());
                        startActivity(intent);



                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }else
                /**
                 * Debugging purposes if call fails
                */
                {
                    Log.d("HGS", response.raw().toString());

                }
            }
            /**
             *  onFailure used for debugging and error handling
            */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("HGS", "FAILURE: " + t.toString());

            }
        });
    }


}