package com.example.smartglass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateDataService extends Service {
    public UpdateDataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
