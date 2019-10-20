package lk.mobile.meghanaada;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.mobile.meghanaada.utils.CommonUtils;
import lk.mobile.meghanaada.utils.VolleySingleton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final String TAG = "MapsActivity";

    private GoogleMap mMap;
    private final int REQUEST_LOCATION_PERMISSION = 1;

    Polyline line;
    // Static LatLng

    LatLng currentLatLng;
    String currentLat;
    String currentLng;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;

    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private String today;
    private String monthBeforeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Date date = Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(date);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date result = cal.getTime();
        monthBeforeDate = formatter.format(result);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();


        mapDataFetch();


    }


    private void mapDataFetch() {

        CommonUtils.getInstance().showProgressDialog(MapsActivity.this, R.string.loading_download);
        // Sending String Request to Server
        Map<String, Object> jsonParams = new HashMap<String, Object>();
//        jsonParams.put("lastModifiedDate", user.getLastSyncDateProducts());


        String url = "https://pmmpublisher.pps.eosdis.nasa.gov/opensearch?q=flood_nowcast&lat=" + currentLat + " &lon=" + currentLng + "&limit=20&startTime=" + monthBeforeDate + "&endTime=" + today;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray contentArray = response.getJSONArray("items");
                                    if (contentArray.length() == 0) {
                                        Toast.makeText(MapsActivity.this, "No Floods Reported!", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                CommonUtils.getInstance().hideProgressDialog();

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        showToastMessage(VolleySingleton.getInstance().getErrorMessage(error));
//                        CommonUtils.getInstance().hideProgressDialog();
                        // stopping swipe refresh
                        CommonUtils.getInstance().hideProgressDialog();
                    }
                }) {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                return headers;
            }

        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsObjRequest.setRetryPolicy(policy);
        VolleySingleton.getInstance().addToRequestQueue(jsObjRequest);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {

            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
//        mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
//        mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLatLng = latLng;
        currentLat = Double.toString(location.getLatitude());
        currentLng = Double.toString(location.getLongitude());
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera 7.051456, 79.916215
        LatLng currentLoc = new LatLng(6.959817, 79.912472);
        mMap.addMarker(new MarkerOptions().position(currentLoc).title("Your Location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLoc).zoom(15).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.getUiSettings().setZoomControlsEnabled(false); // true to enable
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);


//        List<LatLng> list = decodePoly();
//
//        PolylineOptions options = new PolylineOptions().width(25).color(Color.CYAN).geodesic(true);
//        for (int z = 0; z < list.size(); z++) {
//            LatLng point = list.get(z);
//            options.add(point);
//        }
//        line = mMap.addPolyline(options);
//
//        List<LatLng> list2 = decodePoly2();
//        PolylineOptions options2 = new PolylineOptions().width(25).color(Color.CYAN).geodesic(true);
//        for (int z = 0; z < list2.size(); z++) {
//            LatLng point = list2.get(z);
//            options2.add(point);
//        }
//        line = mMap.addPolyline(options2);
//
//        List<LatLng> list3 = decodePoly3();
//        PolylineOptions options3 = new PolylineOptions().width(25).color(Color.CYAN).geodesic(true);
//        for (int z = 0; z < list3.size(); z++) {
//            LatLng point = list3.get(z);
//            options3.add(point);
//        }
//        line = mMap.addPolyline(options3);
//
//        List<LatLng> list4 = decodePoly4();
//        PolylineOptions options4 = new PolylineOptions().width(25).color(Color.CYAN).geodesic(true);
//        for (int z = 0; z < list4.size(); z++) {
//            LatLng point = list4.get(z);
//            options4.add(point);
//        }
//        line = mMap.addPolyline(options4);
//
//        List<LatLng> list5 = decodePoly5();
//        PolylineOptions options5 = new PolylineOptions().width(25).color(Color.CYAN).geodesic(true);
//        for (int z = 0; z < list5.size(); z++) {
//            LatLng point = list5.get(z);
//            options5.add(point);
//        }
//        line = mMap.addPolyline(options5);
//
//        List<LatLng> list6 = decodePoly6();
//        PolylineOptions options6 = new PolylineOptions().width(25).color(Color.CYAN).geodesic(true);
//        for (int z = 0; z < list6.size(); z++) {
//            LatLng point = list6.get(z);
//            options6.add(point);
//        }
//        line = mMap.addPolyline(options6);
//
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            mMap.setMyLocationEnabled(true); // false to disable
        }

//        mMap.addMarker(new MarkerOptions().position(new LatLng(6.967788, 79.911623)).title("Nearest Safe Area").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//        Circle circle = mMap.addCircle(new CircleOptions()
//                .center(new LatLng(6.967788, 79.911623))
//                .radius(300)
//                .strokeColor(Color.TRANSPARENT)
//                .fillColor(Color.GREEN));
    }

    private List<LatLng> decodePoly() {

        List<LatLng> poly = new ArrayList<LatLng>();

        LatLng p = new LatLng(6.949815, 79.919730);
        poly.add(p);
        p = new LatLng(6.950080, 79.917289);
        poly.add(p);
        p = new LatLng(6.950669, 79.913839);
        poly.add(p);
        p = new LatLng(6.951299, 79.910584);
        poly.add(p);

        return poly;
    }

    private List<LatLng> decodePoly2() {

        List<LatLng> poly = new ArrayList<LatLng>();

        LatLng p = new LatLng(6.949815, 79.919730);
        poly.add(p);
        p = new LatLng(6.948684, 79.918838);
        poly.add(p);
        p = new LatLng(6.947275, 79.917418);
        poly.add(p);
        p = new LatLng(6.947116, 79.914530);
        poly.add(p);
        p = new LatLng(6.949094, 79.912660);
        poly.add(p);
        p = new LatLng(6.950552, 79.911296);
        poly.add(p);
        p = new LatLng(6.951028, 79.910084);
        poly.add(p);

        return poly;
    }

    private List<LatLng> decodePoly3() {

        List<LatLng> poly = new ArrayList<LatLng>();

        LatLng p = new LatLng(6.950053, 79.919377);
        poly.add(p);
        p = new LatLng(6.950894, 79.919940);
        poly.add(p);
        p = new LatLng(6.951725, 79.919822);
        poly.add(p);
        p = new LatLng(6.952854, 79.920820);
        poly.add(p);
        p = new LatLng(6.953631, 79.923245);
        poly.add(p);
        p = new LatLng(6.952758, 79.925702);
        poly.add(p);
        p = new LatLng(6.952886, 79.929854);
        poly.add(p);
        p = new LatLng(6.952407, 79.932193);
        poly.add(p);
        p = new LatLng(6.951554, 79.933501);
        poly.add(p);
        p = new LatLng(6.950500, 79.936366);
        poly.add(p);
        p = new LatLng(6.946091, 79.940185);
        poly.add(p);

        return poly;
    }

    private List<LatLng> decodePoly4() {

        List<LatLng> poly = new ArrayList<LatLng>();

        LatLng p = new LatLng(6.951333, 79.910520);
        poly.add(p);
        p = new LatLng(6.951328, 79.909004);
        poly.add(p);
        p = new LatLng(6.952096, 79.902695);
        poly.add(p);
        p = new LatLng(6.955738, 79.900853);
        poly.add(p);
        p = new LatLng(6.957758, 79.898258);
        poly.add(p);
        p = new LatLng(6.957338, 79.895324);
        poly.add(p);
        p = new LatLng(6.955873, 79.892238);
        poly.add(p);
        p = new LatLng(6.955873, 79.887744);
        poly.add(p);
        p = new LatLng(6.955706, 79.883173);
        poly.add(p);
        p = new LatLng(6.960615, 79.880716);
        poly.add(p);
        p = new LatLng(6.964705, 79.881832);
        poly.add(p);

        return poly;
    }

    private List<LatLng> decodePoly5() {

        List<LatLng> poly = new ArrayList<LatLng>();

        LatLng p = new LatLng(6.950019, 79.917249);
        poly.add(p);
        p = new LatLng(6.952915, 79.917470);
        poly.add(p);
        p = new LatLng(6.953193, 79.919400);
        poly.add(p);
        p = new LatLng(6.952871, 79.920859);
        poly.add(p);

        return poly;
    }

    private List<LatLng> decodePoly6() {

        List<LatLng> poly = new ArrayList<LatLng>();

        LatLng p = new LatLng(6.951306, 79.910530);
        poly.add(p);
        p = new LatLng(6.952008, 79.910706);
        poly.add(p);
        p = new LatLng(6.952535, 79.911207);
        poly.add(p);
        p = new LatLng(6.953383, 79.911163);
        poly.add(p);

        return poly;
    }


}
