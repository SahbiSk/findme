package com.example.findme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

// TODO: This method is called when the BroadcastReceiver is receiving an
// Intent broadcast.
        String messageBody,phoneNumber;
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle =intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    messageBody = messages[0].getMessageBody();
                    phoneNumber = messages[0].getDisplayOriginatingAddress();

                    Toast.makeText(context, "message recu", Toast.LENGTH_SHORT).show();
                   if(messageBody.contains("FindMe")){
                       String t[]=messageBody.split("#");
                       String lang = t[1];
                       String lat = t[2];

                       Intent i = new Intent(context,MapsActivity.class);
                       i.putExtra("lat",lat);
                       i.putExtra("lang",lang);
                       i.putExtra("phoneNumber",phoneNumber);

                       PendingIntent pendingIntent = PendingIntent.getActivity(context,0,i,0);
                       NotificationCompat.Builder mBuilder = new
                               NotificationCompat.Builder(context, NotificationChannel.DEFAULT_CHANNEL_ID)
                               .setSmallIcon(android.R.drawable.ic_dialog_map)
                               .setContentTitle("My notification")
                               .setContentText("Hello World!")
                               .setPriority(NotificationCompat.PRIORITY_DEFAULT)
// Set the intent that will fire when the user taps the notification
                               .setContentIntent(pendingIntent)
                               .setAutoCancel(true);
//Vibration
                       mBuilder.setVibrate(new long[] { 1000, 2000,1000,1000 });

                       //son
                       Uri alarmSound =
                               RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                       mBuilder.setSound(alarmSound);
                       // instance du gestionnaire des notifications de l'appareil
                       NotificationManagerCompat manager=
                               NotificationManagerCompat.from(context);

                       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                       {
/* creation du canal si la version android de l'appareil est supérieur à
Oreo */
                           NotificationChannel canal=new
                                   NotificationChannel("myapplication_channel",
                                   // l'ID exacte du canal
                                   "canal pour lapplication find me",
                                   NotificationManager.IMPORTANCE_DEFAULT);
                           AudioAttributes attr=new AudioAttributes.Builder()

                                   .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                   .setUsage(AudioAttributes.USAGE_ALARM)
                                   .build();
                           // ajouter du son pour le canal
                           canal.setSound(alarmSound,attr);
                           // creation du canal dans l'appareil
                           manager.createNotificationChannel(canal);
                           // lancement de la notification

                       }
                       manager.notify(0,mBuilder.build());


                   }

                }
            }
        }
    }
}

