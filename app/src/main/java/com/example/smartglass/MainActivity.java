package com.example.smartglass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextToSpeech speaker;
    String ttsMessage = null;

    TextView reportingScreen;

    Timer timer;

    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        reportingScreen = findViewById(R.id.reports);

        speaker = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    if(speaker.isLanguageAvailable(new Locale("ar", "")) == TextToSpeech.LANG_AVAILABLE){
                        Toast.makeText(MainActivity.this, "arabic is available", Toast.LENGTH_SHORT).show();
                        speaker.setLanguage( Locale.US);
                    }else {
                        speaker.setLanguage(Locale.US);
                    }
                }
            }
        });

        CountDownTimer countDownTimer = new CountDownTimer(10000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                switch (i){
                    case 0:
                        i++;
                        break;
                    case 1:
                        speaker.speak("tree 5 meters ahead", TextToSpeech.QUEUE_FLUSH, null);
                        i++;
                        break;
                    case 2:
                        speaker.speak("rock 10 meters ahead", TextToSpeech.QUEUE_FLUSH, null);
                        i++;
                        break;
                    case 3:
                        speaker.speak("wall in front of you", TextToSpeech.QUEUE_FLUSH, null);
                        i++;
                        break;

                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();

        // And From your main() method or any other method
        timer = new Timer();
        timer.schedule(new InformBlind(), 1000, 2000);


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

        if(speaker !=null){
            speaker.stop();
            speaker.shutdown();
        }
    }

    //todo api call get last update from data base to inform the blind of his situation
    void getSceneDescription(){

    }

    class InformBlind extends TimerTask {
        public void run() {
            getSceneDescription();
        }
    }
}