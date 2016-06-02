package edu.scu.smurali.parkonthego;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import edu.scu.smurali.parkonthego.retrofit.services.LocationServices;
import edu.scu.smurali.parkonthego.retrofit.services.ReservationServices;
import edu.scu.smurali.parkonthego.retrofit.services.UserServices;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chshi on 5/21/2016.
 */
public class ParkOnTheGo extends MultiDexApplication {

    private static ParkOnTheGo mInstance;
    private static Context mContext;
    private static Context mApplicationContext;
    private Retrofit mRetrofit;



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Returns the Application class instance
     */

    public static synchronized ParkOnTheGo getInstance() {
        if (mInstance == null) {
            mInstance = new ParkOnTheGo();
        }
        return mInstance;
    }

    public static float getDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        int meterConversion = 1609;
        return new Float(dist * meterConversion).floatValue();
    }

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        return result == ConnectionResult.SUCCESS;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeVariables();
        buildRetrofitClient();
        //FileManager.createAllDirectories(getApplicationContext());
    }

    public UserServices getUserServices() {
        return mRetrofit.create(UserServices.class);
    }

    public LocationServices getLocationServices() {
        return mRetrofit.create(LocationServices.class);
    }

    public ReservationServices getReservationServices() {
        return mRetrofit.create(ReservationServices.class);
    }

    private void buildRetrofitClient() {
        OkHttpClient httpClient = new OkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(Config.BASE_URL).build();
    }

    private void initializeVariables() {
        mInstance = this;
        mApplicationContext = this.getApplicationContext();

    }

    /**
     * Returns the current activity context
     */

    public Context getCurrentActivityContext() {
        if (mContext == null) {
            return mApplicationContext;
        } else {
            return mContext;
        }
    }


    /**
     * Sets the current activity contest.
     * <p/>
     * Make sure that you call this in onCreate and onResume of an activity
     */
    public void setCurrentActivityContext(Context context) {
        mContext = context;
    }

    public String getDeviceId() {
        return Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }

    public void showProgressDialog() {

    }

    public void showProgressDialog(String title, String description) {

    }

    public void hideProgressDialog() {

    }

    public void handleError(Throwable throwable) {

    }

    public void showAlert(Context mContext,String alertMessage ,String title ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(alertMessage)
                .setTitle(title);
// Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
//                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
// Set other dialog properties


// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void showAlert(String alertMessage) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alertMessage)
                .setTitle("Error");
// Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
//                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
// Set other dialog properties


// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Checks Internet connection's availability
     *
     * @return availability
     */
    public boolean isConnectedToInterNet() {
        ConnectivityManager connectivity = (ConnectivityManager) getCurrentActivityContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }


}
