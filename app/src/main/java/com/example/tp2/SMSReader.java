package com.example.tp2;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Log;

import java.util.Date;

public class SMSReader extends Service {
    public SMSReader() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final Uri msj = Telephony.Sms.CONTENT_URI;
        final ContentResolver contentR = getContentResolver();

        Runnable Reader = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Cursor cursor = contentR.query(msj, null, null, null, null);

                    if (cursor.getCount() > 0) {
                        int x = 0;
                        while (cursor.moveToNext() && x < 5) {
                            String numero = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS));
                            String fecha = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.DATE));
                            long dateTLong = Long.parseLong(fecha);
                            Date date = new Date(dateTLong);
                            String msjContent = cursor.getString(cursor.getColumnIndex(Telephony.Sms.Inbox.BODY));
                            Log.d("salida", "Mensaje de: " +numero+  "Se recibió el: " + date.toString() + " " + msjContent);
                            x++;

                        }
                        try {
                            Thread.sleep(9000);
                            cursor.moveToFirst();
                        } catch (InterruptedException a) {
                            Log.e("Error ", a.getMessage());
                        }

                    }

                }
            }
        };

        Thread Worker = new Thread(Reader);
        Worker.start();
        return START_STICKY;
    }

        @Override
        public IBinder onBind (Intent intent){
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

