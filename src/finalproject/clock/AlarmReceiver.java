package finalproject.clock;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {  

	
	@Override  
    public void onReceive(Context arg0, Intent data) {  
    	Log.d("dfg", "the time is up,start the alarm...");  
        Toast.makeText(arg0, "time up", Toast.LENGTH_SHORT).show();  
        Intent intent = new Intent();   
        intent.setClass(arg0, WordSpelling.class);
   	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
   	    	try{
			ringController.startRing();
		//	Toast.makeText(arg0, "ring started", Toast.LENGTH_LONG).show();
			}
			catch(Exception e)
			{
			//	Toast.makeText(arg0, e.getMessage(), Toast.LENGTH_LONG).show();
			}
   	    arg0.startActivity(intent);
        
    }
	static public ringControlling ringController =new ringControlling("test.mp3");
  
	  
}  
