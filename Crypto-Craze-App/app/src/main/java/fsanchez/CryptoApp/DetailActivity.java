package fsanchez.CryptoApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

public class DetailActivity extends AppCompatActivity {

    /**
     * DetailActivity java class
     * Able to dynamically call data from the api coin gecko
     * for a smoother response
     * Text view coinName, coinPrice, vol24, change24, marketCap 
     * for dynamic calles
     * String name, price, mCap, v24, c24 for the crypto
     * Line chart is for displaying a graph
    */

    TextView coinName, coinPrice, vol24, change24, marketCap;
    String name, price, mCap, v24, c24;
    ProgressDialog dialog;
    LineChart chart;


    /**
     * onCreate function 
     * @param savedInstanceState used for application instance state
     * Sets the Content view of new activity
     * And calls the views of the coin clicked
     * And last it will call the chart data
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        getChartData();
    }

    /**
     * getChartData function used to call the coin gecko api to gather
     * coin chart data
    */
    private void getChartData() {

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
         * This is used for chart data graph 
         * Our chart data of a year
         */
        Calendar current = Calendar.getInstance();
        Calendar old = Calendar.getInstance();

        old.add(Calendar.YEAR, -1);

        /**
         * This is used for debugging purposes
        */
        Log.d("HGS", String.valueOf(old.getTimeInMillis()).substring(0,10) + " " + String.valueOf(current.getTimeInMillis()).substring(0,9));

        /**
         * This is used for putting data in our hash map essentially allowing multiple entries 
         * and can be show at the same time 
        */
        map.put("from", String.valueOf(old.getTimeInMillis()).substring(0,10));
        map.put("to", String.valueOf(current.getTimeInMillis()).substring(0,10));
        map.put("vs_currency", "usd");


        /**
         * API call used to call the request type 
         * Call is chart data of coin
         */
        Call<ResponseBody> call = retrofit.create(CoinGecko.class).getChartData(name.toLowerCase(), map);

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

            /**
             * onResponse verifies the api call
            */
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();

                /**
                 * Create a new array list for our coin data
                 * Used to handle graph data by historic data
                 * Commonly used for data implementation for JSON objects
                 */
                ArrayList<Entry> entries = new ArrayList<>();

                /**
                 * If response is successful load the prices into a JSON
                 * object and array for iteration of prices and puts that 
                 * back in the array list of entries for our graph data
                */
                if(response.isSuccessful())
                {
                    try {
                        String res = response.body().string();
                        Log.d("HGS", res);

                        JSONObject obj = new JSONObject(res);
                        JSONArray prices = obj.getJSONArray("prices");

                        for(int i = 0; i < prices.length(); i++)
                        {
                            JSONArray arr = prices.getJSONArray(i);
                            entries.add(new Entry(Float.parseFloat(arr.get(0).toString()), Float.parseFloat(arr.get(1).toString())));
                        }

                        setupChart(entries);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    /**
                     * Debugging purposes if call fails
                    */
                }else
                {
                    try {
                        Log.d("HGS", "ERROR: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            /**
             *  onFailure used for debugging and error handling
            */
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                Log.d("HGS", "FAILURE");
            }
        });

    }

    /**
     * setupChart used for setting the graphical chart
     * Uses entries of array list
    */
    private void setupChart(ArrayList<Entry> entries) {
        // background color
        chart.setBackgroundColor(Color.WHITE);

        // disable description text
        chart.getDescription().setEnabled(false);
        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);


        /**
         * Used for bounds of chart
        */
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        XAxis axis = chart.getXAxis();
        axis.setValueFormatter(new XAxisValueFormatter());
        axis.setLabelCount(3);

        /**
         * Used for graph texture and colors
        */
        LineDataSet set = new LineDataSet(entries, "");
        set.setColor(Color.BLACK);
        set.setLineWidth(1f);
        set.setDrawCircles(false);
        set.setDrawValues(false);
        set.setValueTextColor(Color.BLACK);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        /**
         * Sets the line chart data
        */
        LineData data = new LineData(set);
        chart.setData(data);
        chart.invalidate();

    }

    /**
     * initViews function used to initate the views of the
     * top 5 crypto and the rest of the dynamically called coins
    */
    private void initViews() {
        dialog = new ProgressDialog(this);
        coinName = findViewById(R.id.coinName);
        coinPrice = findViewById(R.id.coinPrice);
        vol24 = findViewById(R.id.vol24Val);
        change24 = findViewById(R.id.change24Val);
        marketCap = findViewById(R.id.marketCapVal);
        chart = findViewById(R.id.coinChart);

        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        mCap = getIntent().getStringExtra("marketCap");
        v24 = getIntent().getStringExtra("vol24");
        c24 = getIntent().getStringExtra("change24");

        coinName.setText(name);
        coinPrice.setText("$" + price);
        vol24.setText("Volume: " + v24);
        change24.setText("Change: " + c24);
        marketCap.setText("Market Cap: " + mCap);
    }


}