package finalproject.clock;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;



import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import android.widget.*;
public class ClockActivity extends Activity 
{
    /** Called when the activity is first created. */
	public Spinner spinnerRing;
	public Spinner spinnerLevel;
	public Button buttonSave;
	public Button buttonCancel;
	public EditText editHour;
	public EditText editMinute;
	public CheckBox checkMon;
	public CheckBox checkTue;
	public CheckBox checkWed;
	public CheckBox checkThu;
	public CheckBox checkFri;
	public CheckBox checkSat;
	public CheckBox checkSun;

	public String ring=null;
	public String level=null; 
	public String time=null;
	public String[] ringList;
	public boolean[] days=new boolean[7];
	static public int RING=1;
	static public int LEVEL=2;
	static public int MAXLEVEL=6;
	static public int MONDAY=0;
	static public int TUESDAY=1;
	static public int WEDNESDAY=2;
	static public int THURSDAY=3;
	static public int FRIDAY=4;
	static public int SATURDAY=5;
	static public int SUNDAY=6;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        spinnerRing = (Spinner) findViewById(R.id.spinnnerRing);
        spinnerLevel = (Spinner) findViewById(R.id.spinnnerLevel);
        buttonSave= (Button) findViewById(R.id.ButtonSave);
        buttonCancel= (Button) findViewById(R.id.ButtonCancel);
        editHour= (EditText) findViewById(R.id.EditHour);
        editMinute= (EditText) findViewById(R.id.EditMinute);
        buttonSave.setOnClickListener(SaveEvent);
        buttonCancel.setOnClickListener(CancelEvent);
        checkMon=(CheckBox) findViewById(R.id.Mon);
        checkTue=(CheckBox) findViewById(R.id.Tue);
    	checkWed=(CheckBox) findViewById(R.id.Wed);
    	checkThu=(CheckBox) findViewById(R.id.Thu);
    	checkFri=(CheckBox) findViewById(R.id.Fri);
    	checkSat=(CheckBox) findViewById(R.id.Sat);
    	checkSun=(CheckBox) findViewById(R.id.Sun);
    	checkMon.setOnCheckedChangeListener(listener);
		checkTue.setOnCheckedChangeListener(listener);
		checkWed.setOnCheckedChangeListener(listener);
		checkThu.setOnCheckedChangeListener(listener);
		checkFri.setOnCheckedChangeListener(listener);
		checkSat.setOnCheckedChangeListener(listener);
		checkSun.setOnCheckedChangeListener(listener);
        getSavedInfo();
        getLists(RING);
        setSpinner(spinnerRing,ringList,RING);
        String[] tLevel= new String[MAXLEVEL];
        for(int i=0;i<MAXLEVEL;i++)
        {
        	tLevel[i]=String.valueOf(i);
        }
        setSpinner(spinnerLevel,tLevel,LEVEL); 
    }
	
	
	private CheckBox.OnCheckedChangeListener listener=new CheckBox.OnCheckedChangeListener()
	{
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
		{
			if(checkMon.isChecked()==true)
			{
				days[MONDAY]=true;
				//Log.d("dfg",String.valueOf(days[MONDAY])); 
			}
			if(checkTue.isChecked()==true)
			{
				days[TUESDAY]=true;
				//Log.d("dfg",String.valueOf(days[TUESDAY])); 
			}
			if(checkWed.isChecked()==true)
			{
				days[WEDNESDAY]=true;
				//Log.d("dfg",String.valueOf(days[WEDNESDAY])); 
			}
			if(checkThu.isChecked()==true)
			{
				days[THURSDAY]=true;
				//Log.d("dfg",String.valueOf(days[THURSDAY])); 
			}
			if(checkFri.isChecked()==true)
			{
				days[FRIDAY]=true;
				//Log.d("dfg",String.valueOf(days[FRIDAY])); 
			}
			if(checkSat.isChecked()==true)
			{
				days[SATURDAY]=true;
			//	Log.d("dfg",String.valueOf(days[SATURDAY])); 
			}
			if(checkSun.isChecked()==true)
			{
				days[SUNDAY]=true;
				//Log.d("dfg",String.valueOf(days[SUNDAY])); 
			}
			
		}
	};
	public void getSavedInfo()
	{
//		ring="JingBells";
//		time="12:32";
//		level="5";
//      days;
		
	}
	public OnClickListener CancelEvent=new OnClickListener(){
		public void onClick(View v){
			 AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);  

			for(int i=MONDAY;i<=SUNDAY;i++)
			{
				if(!days[i])
				{
					continue;		
				}
		        Intent intent = new Intent(ClockActivity.this,AlarmReceiver.class);  
	            PendingIntent pendingIntent = PendingIntent.getBroadcast(ClockActivity.this, i, intent, 0);  
	            alarmManager.cancel(pendingIntent);
	             
			   
			}
            
            Toast.makeText(ClockActivity.this, "cancelled", Toast.LENGTH_SHORT).show();  
		}
	};
	public OnClickListener SaveEvent=new OnClickListener(){
		public void onClick(View v){
			File clockList = new File("/sdcard/clockList.txt");
			String str = ring + "/n" + time + "/n" + level;
			if( !clockList.exists() ){
				try {
					clockList.createNewFile();
					FileWriter fw = new FileWriter("/sdcard/clockList.txt",false);
					fw.write(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);  
			Calendar currentCal=Calendar.getInstance();
			/*Log.d("year", String.valueOf(currentCal.get(Calendar.YEAR)));
	        Log.d("month", String.valueOf(currentCal.get(Calendar.MONTH)));
	        Log.d("date", String.valueOf(currentCal.get(Calendar.DATE)));
	        Log.d("DAY_OF_WEEK", String.valueOf(currentCal.get(Calendar.DAY_OF_WEEK)));
	        Log.d("h", String.valueOf(currentCal.get(Calendar.HOUR)));
        	Log.d("m", String.valueOf(currentCal.get(Calendar.MINUTE)));*/
	        for(int i=MONDAY;i<=SUNDAY;i++)
			{
				
				if(!days[i])
				{
					continue;		
				}
				Calendar calendar=Calendar.getInstance();
				
				calendar.setTimeInMillis(System.currentTimeMillis()); 
				calendar.set(Calendar.DAY_OF_WEEK,casting(i));
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(editHour.getText().toString()));
				calendar.set(Calendar.MINUTE, Integer.parseInt(editMinute.getText().toString()));
				calendar.set(Calendar.SECOND, 0);  
		        calendar.set(Calendar.MILLISECOND, 0);
		       
		        if(calendar.before(currentCal))
		        {
		        /*	Log.d("year", String.valueOf(calendar.get(Calendar.YEAR)));
		        	Log.d("month", String.valueOf(calendar.get(Calendar.MONTH)));
		        	Log.d("date", String.valueOf(calendar.get(Calendar.DATE)));
		        	Log.d("DAY_OF_WEEK", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
		        	Log.d("h", String.valueOf(calendar.get(Calendar.HOUR)));
		        	Log.d("m", String.valueOf(calendar.get(Calendar.MINUTE)));*/
		        	calendar.add(Calendar.WEEK_OF_YEAR,1);
		        	
				   // Log.d("date", String.valueOf(calendar.get(Calendar.DATE)));
		        }

		        Intent intent = new Intent(ClockActivity.this,AlarmReceiver.class);  
	            PendingIntent pendingIntent = PendingIntent.getBroadcast(ClockActivity.this, i, intent, 0);  
				alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent); 
				
			}
			Toast.makeText(ClockActivity.this, "set", Toast.LENGTH_LONG).show();
			
			
			//save in file time ring and days
				finish();
			}
		
	};
	
	public int  casting(int i)
	{
		
		if(i==MONDAY)
		{
			return Calendar.MONDAY;		
		}
		else if (i==TUESDAY)
		{
			return Calendar.TUESDAY;
		}
		else if (i==WEDNESDAY)
		{
			return Calendar.WEDNESDAY;
		}
		else if (i==THURSDAY)
		{
			return Calendar.THURSDAY;
		}
		else if (i==FRIDAY)
		{
			return Calendar.FRIDAY;
		}
		else if (i==SATURDAY)
		{
			return Calendar.SATURDAY;
		}	
		else if (i==SUNDAY)
		{
			return Calendar.SUNDAY;
		}
		else
		{
			return -1;
		}
	}
	
	
    public void getLists(int type)
    {
    	String postfix=getFormat(type);
    	
    	saveLists(null,type);
    }
    public String getFormat(int type)
    {
    	if(type==RING)
    	{
    		return ".mp3";
    	}
    	else
    	{
    		return null;
    	}
    }
    public void saveLists(String[] list,int type)
    {
    	if(type==RING)
    	{
    		//ringList=list;
    		ringList= new String[]{"asdf","asdfff","ery"};
    	}
    }
    public void setSpinner(Spinner spinner, String[] list,final int type)
    {
    	 ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
         int pos=getDefaultSelectionPositon(type);
         spinner.setSelection(pos);
         spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
         {
         	
         	public void onItemSelected(AdapterView adapterView, View view, int position, long id)
         	{
         		saveSelectedItem(adapterView.getSelectedItem().toString(),type);
             }
             public void onNothingSelected(AdapterView arg0) 
             {
              //   Toast.makeText(ClockActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
             }
          });
    	
    	
    }
    public int getDefaultSelectionPositon(int type)
    {
    	return 0;
    	
    }
    
    public void saveSelectedItem(String Item,int type)
    {
    	if(type==RING)
    	{
    		ring=Item;
    	}
    	else if(type==LEVEL)
    	{
    		level=Item;	
    	}
    }
}

