package finalproject.clock;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public  class ringControlling {
	public Thread ring;
	 public MediaPlayer mPlayer=new MediaPlayer();;	
	 public static int WAKED=1;
	 public boolean waked=false;
	 public Handler handler = new Handler() {
         @Override
         public void handleMessage(Message msg) {
             if (msg.what == WAKED)
             {
            	 waked=true;
            	 mPlayer.stop();
            	 ring.interrupt();
            	 Log.d("mp3","waked set");
             }  
             super.handleMessage(msg);
         }
     };
    
	public void playMusic()
	{
		try 
		{
			mPlayer.setOnPreparedListener(new OnPreparedListener(){

				public void onPrepared(MediaPlayer arg0) {
					arg0.setLooping(true);
					arg0.start();
				}
			} );		
			mPlayer.setDataSource("/sdcard/test.mp3");
			mPlayer.prepareAsync();
			
		} 
		catch (Exception e) 
		{
			Log.d("mp3",e.getMessage());
		}
		
		
	}
	public ringControlling(String threadName) 
	{
		ring = new Thread(threadName){
			public void run() {
				playMusic();
			}
	};
	}
	public void startRing()
	{
		ring.start();
	}
	
	public void setVolume()
	{
		AudioManager mAudioManager = (AudioManager)(new Activity()).getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); 
	}
	
}
