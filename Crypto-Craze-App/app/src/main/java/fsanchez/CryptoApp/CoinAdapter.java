package fsanchez.CryptoApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * CoinAdapter java class
 * Extends Array adapter of coins 
 * @author  Frankie Sanchez
 * @version 1.0.0
 */

public class CoinAdapter extends ArrayAdapter<Coin> {

    /**
     * CoinAdapter java class
     * Essential to deploy the dyncamic data
     * Array List of coins
     * Context is our content on screen
    */
    private Context context;
    private ArrayList<Coin> coins;


    /**
     * CoinAdapter constructor function
     * @param context sets the content of the coins
     * @param coins sets the list of coins of an array list Coin(Gathered from api)
    */
    public CoinAdapter(@NonNull Context context, ArrayList<Coin> coins) {
        super(context, R.layout.coin_list_item, coins);
        this.coins = coins;
        this.context = context;
    }

    /**
     * getView function is to set the view of the crypto coin dynacially
     * @param position used to get position of coin data
     * @param convertView used to convert the view for data implementation
     * @param price parent of View Group
    */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
 
    /**
     * View is set to convert view
     * If the view is null set it to LayoutInflater and set it to false
     * Otherwise continue and create a TextView name and price
     * Now get the id's and position of the crypto and set them by our 
     * getter functionsand return the view to the user
     * Handles dynamic data
     */
        View view = convertView;

        if(view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.coin_list_item, parent, false);
        }

        TextView name, price;

        name = view.findViewById(R.id.coinName);
        price = view.findViewById(R.id.coinPrice);

        name.setText(coins.get(position).getName());
        price.setText("$" + coins.get(position).getPrice());

        return view;
    }
}
