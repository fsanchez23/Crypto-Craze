package fsanchez.CryptoApp;

import java.util.Map;

import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * CoinGecko java class
 * Interface for coin gecko api
 * @author  Frankie Sanchez
 * @version 1.0.0
 */

public interface CoinGecko {

    /** 
     * Calls simple price, market_chart/range, 
     * and coins/list api for the implementation
     * of dynamic data calling
    */

    @GET("simple/price")
    Call<ResponseBody> getCoinData(
            @QueryMap() Map<String, String> coinMap
            );

    @GET("coins/{id}/market_chart/range")
    Call<ResponseBody> getChartData(
            @Path("id") String id,
            @QueryMap Map<String, String> map
    );

    @GET("coins/list")
    Call<ResponseBody> getAllTickers();

}
