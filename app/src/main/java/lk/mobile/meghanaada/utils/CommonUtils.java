package lk.mobile.meghanaada.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Common Utils class for Event360 application
 * <p/>
 * Created by roshane on 9/12/16.
 */
public class CommonUtils {
    private static CommonUtils commonUtils;
    private ProgressDialog mProgressDialog;

    /**
     * Singleton method for CommonUtils
     *
     * @return CommonUtils
     */
    public static CommonUtils getInstance() {
        if (commonUtils == null) {
            commonUtils = new CommonUtils();
        }
        return commonUtils;
    }


    public DateFormat getDateFormat() {
        return new SimpleDateFormat("hh:mm a", Locale.US);
    }

    public DateFormat getDayFormat() {
        return new SimpleDateFormat("dd MMM hh:mm a", Locale.US);
    }


    /**
     * Display Toast Message in Application
     *
     * @param message Display Toast message
     */
    public void showToastMessage(int message) {
        Toast.makeText(MyApplication.getAppContext(),
                MyApplication.getAppContext().getResources().getString(message),
                Toast.LENGTH_SHORT).show();
    }

    public String getDateByMillis(long date) {
        String txtDay;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        String formatted = format1.format(calendar.getTime());
        txtDay = formatted;

        return txtDay;
    }



    /**
     * Display Progress Dialog, while doing any AsyncTask work
     *
     * @param activity
     * @param message
     */
    public void showProgressDialog(Activity activity, int message) {
        // If already progress dialog is loading
        // dismiss the dialog
        hideProgressDialog();

        // Create a new progress dialog
        mProgressDialog = new ProgressDialog(activity);
        mProgressDialog.setMessage(MyApplication.getAppContext().getString(message));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * Hide Progress Dialog, After finish the AsyncTAsk Work
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Check Internet Connection is Available or not
     *
     * @return isInternetAvailable
     */
    public boolean isInternetAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) MyApplication.getAppContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    /**
     * A 64-bit number (as a hex string) that is randomly generated
     * when the user first sets up the device and
     * should remain constant for the lifetime of the user's device.
     *
     * @return deviceId - Android device ID
     */
    public String getDeviceID() {
        return Settings.Secure.getString(MyApplication.getAppContext().getContentResolver()
                , Settings.Secure.ANDROID_ID);
    }

    public String getDeviceName() {
        return Build.MODEL;
    }

    public String getDeviceType() {
        return "Phone";
    }

    /**
     * This provides a convenient way to create an intent that is
     * intended to execute and navigate a new class
     *
     * @param packageContext A Context of the application package implementing
     *                       this class.
     * @param cls            The component class that is to be used for the intent.
     */
    public void navigateTo(Context packageContext, Class<?> cls) {
        navigateTo(packageContext, cls, null);
    }

    /**
     * This provides a convenient way to create an intent that is
     * intended to execute and navigate a new class
     *
     * @param packageContext A Context of the application package implementing
     *                       this class.
     * @param cls            The component class that is to be used for the intent.
     * @param bundle         Bundle Value
     */
    public void navigateTo(Context packageContext, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(packageContext, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        packageContext.startActivity(intent);
    }

    /**
     * Show Soft Keyboard in UI
     *
     * @param view
     */
    public void showKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Hide Soft Keyboard in UI
     *
     * @param view
     */
    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) MyApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
