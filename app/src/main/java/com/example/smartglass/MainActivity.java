package com.example.smartglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.smartglass.model.Sentence;
import com.example.smartglass.model.User;
import com.example.smartglass.utils.SharedPrefManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import com.example.smartglass.utils.SharedPrefManager;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextToSpeech speaker;
    String ttsMessage = null;

    TextView reportingScreen, idTV;

    Timer getData, informBlind;

    int i = 0;

    SharedPrefManager prefManager;
    ArrayList<Sentence> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<Sentence>();

        prefManager = SharedPrefManager.getInstance(this);

        reportingScreen = findViewById(R.id.reports);
        idTV = findViewById(R.id.user_id);
        idTV.setText(String.valueOf(SharedPrefManager.getInstance(this).getUserId()));

        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speaker.setLanguage(Locale.US);
                }
            }
        });

        // And From your main() method or any other method
        getData = new Timer();
        getData.schedule(new GetData(), 1000, 2000);

        informBlind = new Timer();
        informBlind.schedule(new InformBlind(), 1000, 1500);



    }

    @Override
    protected void onPause() {
        super.onPause();
        if(speaker !=null){
            speaker.stop();
            speaker.shutdown();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getData.cancel();
        informBlind.cancel();
        if(speaker !=null){
            speaker.stop();
            speaker.shutdown();
        }
    }

    //todo api call get last update from data base to inform the blind of his situation
    void getSceneDescription(int glassId){
        AndroidNetworking.post(Urls.LOGIN_URL)
                .addBodyParameter("patient_id", String.valueOf(glassId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            //converting response to json object
                            if(response.getInt("status") == 1){
                                JSONArray items = response.getJSONArray("list");
                                for(int i = 0; i < items.length(); i++){
                                    JSONObject item = items.getJSONObject(i);
                                    Sentence sentence = new Sentence(
                                            Integer.parseInt(item.getString("id")),
                                            item.getString("sentence")
                                    );
                                    list.add(sentence);
                                    reportingScreen.append(sentence.getMessage());
                                }



                            } else if(response.getInt("status") == 0){
                                Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void editStatus(int sentenceId) {


        AndroidNetworking.post(Urls.EDIT_STATUS_URL)
                .addBodyParameter("sentence_id", String.valueOf(sentenceId))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            //converting response to json object
                            if(response.getInt("status") == 1){
                                String message = response.getString("message");
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                            } else if(response.getInt("status") == 0){
                                Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("edit status ", e.getMessage());

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("edit status", anError.getMessage());
                    }
                });
//
    }

    class InformBlind extends TimerTask {
        public void run() {
            informBlind();
        }
    }

    private void informBlind() {
        if(!list.isEmpty()) {
            ttsMessage = list.get(0).getMessage();
            if(ttsMessage.split(" ").length > 3){
                try {
                    informBlind.wait((ttsMessage.split(" ").length - 3) * 300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                speaker.speak(ttsMessage, TextToSpeech.QUEUE_FLUSH, null);
                editStatus(list.get(0).getId());

            }
            list.remove(0);
        }
    }

    class GetData extends TimerTask {
        public void run() {
            getSceneDescription(prefManager.getUserId());
        }
    }


}