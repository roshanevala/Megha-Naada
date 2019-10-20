package lk.mobile.meghanaada.utils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import lk.mobile.meghanaada.R;

/**
 * Volley - Using for Transmitting data over network
 *
 * Volley is designed to queue all your requests.
 * It wouldn't make sense to have more than one queue,
 * and that's why it's a singleton.
 *
 * Created by roshane on 5/20/16.
 */
public class VolleySingleton
{
    private static final String TAG = VolleySingleton.class.getSimpleName();
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleySingleton()
    {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    /**
     * Singleton class for VolleySingleton
     * @return volley singleton object
     */
    public static synchronized VolleySingleton getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    /**
     * Get the Volley Request Queue
     *
     * @return Request Queue
     */
    private RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        }
        return mRequestQueue;
    }

    /**
     * Adding API request to Volley
     * @param req request
     * @param <T> getting the request in queue
     */
    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    /**
     * Getting the Error description from VolleyError
     *
     * @param error volley error
     * @return error message
     */
    public String getErrorMessage(VolleyError error)
    {
        if(error != null)
        {
            try
            {
                String responseBody = new String(error.networkResponse.data, "utf-8");
                JSONObject response = new JSONObject(responseBody);
                return response.optString("error_description");
            }
            catch (JSONException e)
            {
                return MyApplication.getAppContext().getString(R.string.server_error);
            }
            catch (UnsupportedEncodingException e)
            {
                return MyApplication.getAppContext().getString(R.string.server_error);
            }
        }
        else
        {
            return MyApplication.getAppContext().getString(R.string.server_error);
        }
    }

}
