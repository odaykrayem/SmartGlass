package com.example.smartglass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.smartglass.model.Sentence;
import com.example.smartglass.utils.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextToSpeech speaker;
    String ttsMessage = null;

    TextView reportingScreen, idTV, openMap;

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
        openMap = findViewById(R.id.open_map);

        openMap.setOnClickListener(v -> {
            startActivity(new Intent(this, MapActivity.class));
            finish();
        });

        reportingScreen = findViewById(R.id.reports);
        idTV = findViewById(R.id.user_id);
        idTV.setText(String.valueOf(SharedPrefManager.getInstance(this).getUserId()));

        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speaker.setLanguage(Locale.US);
                    speaker.setPitch(0.5f);
                    speaker.setSpeechRate(0.6f);
                }
            }

        });

        // And From your main() method or any other method
        getData = new Timer();
        getData.schedule(new GetData(), 1000, 5000);

        informBlind = new Timer();
        informBlind.schedule(new InformBlind(), 2000, 5000);



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
        AndroidNetworking.post(Urls.GET_SENTENCES_URL)
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
                                    reportingScreen.append(sentence.getMessage() + "\n" + "\n");
                                }



                            } else if(response.getInt("status") == 0){
                                Toast.makeText(MainActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("amn catch", e.getMessage() );
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(MainActivity.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("main", anError.getMessage());
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
    }

    class InformBlind extends TimerTask {
        public void run() {
            informBlind();
        }
    }

    private void informBlind() {
        if(!list.isEmpty()) {
            ttsMessage = list.get(0).getMessage();
            speaker.speak(ttsMessage, TextToSpeech.QUEUE_FLUSH, null);
            editStatus(list.get(0).getId());
            list.remove(0);
        }
    }

    class GetData extends TimerTask {
        public void run() {
            prefManager = SharedPrefManager.getInstance(MainActivity.this);
            getSceneDescription(prefManager.getUserId());
        }
    }
    private class TTSInit extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            speaker=new TextToSpeech(MainActivity.this, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    speaker.setLanguage(Locale.ENGLISH);
                    speaker.setPitch(0.5f);
                    speaker.setSpeechRate(0.6f);
                    /* now you can invoke speak() */
                    informBlind();
                }
            });
            return null;
        }
    }

}