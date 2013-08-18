package com.vosto;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import com.vosto.accounts.services.RegisterDeviceResult;
import com.vosto.accounts.services.RegisterDeviceService;
import com.vosto.HomeActivity;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RestResult;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class GCMIntentService extends com.google.android.gcm.GCMBaseIntentService implements OnRestReturn {

	public GCMIntentService(){
		super("1091536520954"); // The Vosto project id as assigned by GCM at the beginning.
		//1091536520954 - shadley's account - used for production
		   //263607631818 - flippie test account
	}
	
	@Override
	protected void onError(Context context, String arg1) {
		// GCM has returned an unrecoverable error.
		Log.d("GCM", "GCM onError (unrecoverable)");
	}

//	@Override
//    protected void onMessage(Context context, Intent intent) {
//        String message = intent.getStringExtra("msg");
//        message = (message==null) ? "" : message;
//        Log.d("GCMIntentService", message);
//        handleMessage(context, message);
//    }
//
//    protected void handleMessage(Context context, String msg) {
//        //if msg is check for new orders, check if activity is active
//        //if so check for new orders, else send an offline status update
//        //and set a db flag to check for new orders on resume
//        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//        if (msg.equals("new")) {
//            boolean active = Main.isActive;
//            active = true;
//            if (active) {
//                Intent intent = new Intent(ACTION_MESSAGE);
//                intent.putExtra("action", "getNew");
//                context.sendBroadcast(intent);
//            }
//        }
//    }

    @Override
	protected void onMessage(Context context, Intent intent) {
		// A message has been received from GCM.
		Log.d("GCM", "GCM message received.");

		// MyOrdersActivity will open when the notification is clicked, passing through the order id:
		Intent notificationIntent = new Intent(context, HomeActivity.class);
		notificationIntent.putExtra("order_id", Integer.parseInt(intent.getStringExtra("order_id")));
		Log.d("GCM", "order_id received from gcm: " + Integer.parseInt(intent.getStringExtra("order_id")));
		generateNotification(context, "There is a new Vosto Order.", notificationIntent);
	}
	
	

	private static void generateNotification(Context context, String message, Intent notificationIntent) {
	    int icon = R.drawable.vosto_logo;
	    long when = System.currentTimeMillis();
	    NotificationManager notificationManager = (NotificationManager)
	            context.getSystemService(Context.NOTIFICATION_SERVICE);
	    Notification notification = new Notification(icon, message, when);
	    String title = context.getString(R.string.app_name);

        playNotify(context);

        notificationIntent = new Intent(context, HomeActivity.class);

        PendingIntent intent = PendingIntent.getActivity(context, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	    // PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

	    notification.setLatestEventInfo(context, title, message, intent);
	    notificationManager.notify(0, notification);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	}

    public static void playNotify(Context context) {

//        SoundPool soundPool;
//        int soundID;
//        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
//
//        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
//        soundID = soundPool.load(context, R.raw.vosto_alarm, 1);
//
//        float maxVolume = (float) audioManager
//                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        // Is the sound loaded already?
//        soundPool.play(soundID, maxVolume, maxVolume, 1, 3,  0.99f);
//        Log.d("Test", "Played sound");
//        soundPool.release();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

	@Override
	protected void onRegistered(Context context, String gcmId) {
		// Device has been registered with GCM. Save the id to the device preferences:
		 Log.d("GCM", "GCM onRegistered : id = " + gcmId);
		 SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.putString("gcmId", gcmId);
	     editor.commit();
	     
	     // Now we register the device with Vosto, passing the new gcm id:
	     Log.d("GCM", "Registering device with Vosto...");
	     RegisterDeviceService service = new RegisterDeviceService(this, (VostoCustomerApp)context, gcmId);
	     service.execute();
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// Device has been unregistered from gcm. Remove the id from the app preferences:
		Log.d("GCM", "GCM onUnregistered");
		 SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
		 SharedPreferences.Editor editor = settings.edit();
		 editor.remove("gcmId");
	     editor.commit();	
	}

	@Override
	public void onRestReturn(RestResult result) {
		if(result == null){
			return;
		}
		if(result instanceof RegisterDeviceResult){
			// Vosto has responded after we called devices/register
			RegisterDeviceResult registerResult = (RegisterDeviceResult)result;
			if(registerResult.wasSuccessful()){
				//Device has been registered with Vosto.
				Log.d("GCM", "Device registered with Vosto!");
			}else{
				Log.d("GCM", "Vosto could not register the device.");
			}
			
		}
		
	}
	
	
}