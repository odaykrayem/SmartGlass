package com.example.smartglass;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.smartglass.model.Sentence;
import com.example.smartglass.utils.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    SharedPrefManager prefManager;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = SharedPrefManager.getInstance(this);

        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        timer = new Timer();
        timer.schedule(new GetData(), 5000, 3000);
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
        getPatientLocation(prefManager.getUserId());
    }

    class GetData extends TimerTask {
        public void run() {
            getPatientLocation(prefManager.getUserId());
        }
    }

    private void getPatientLocation(int userId) {
        AndroidNetworking.post(Urls.LOGIN_URL)
                .addBodyParameter("patient_id", String.valueOf(userId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            //converting response to json object
                            if(response.getInt("status") == 1){
                                double lat = Double.parseDouble(response.getString("lat"));
                                double lon = Double.parseDouble(response.getString("lon"));
                                // Add a marker in Sydney and move the camera
                                LatLng blindLocation = new LatLng(lat, lon);
                                mMap.addMarker(new MarkerOptions().position(blindLocation).title("blind is here!"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(blindLocation, 15f));


                            } else if(response.getInt("status") == 0){
                                Toast.makeText(MapActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MapActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}