package fsanchez.CryptoApp;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Data formatting class for crypto app
 * @author  Frankie Sanchez
 * @version 1.0.0
 */

public class XAxisValueFormatter extends ValueFormatter {

    /** 
     * Class is used for formatting of the data 
     * used for chart in relation to calendar
    */

    private SimpleDateFormat formatter;

    public XAxisValueFormatter()
    {
        formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }


    @Override
    public String getFormattedValue(float value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long) value);
        return formatter.format(calendar.getTime());
    }

}
