package finalproject.clock;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;

import jxl.*;

import java.util.Random;

public class WordSpelling extends Activity {
	public Button buttonClose;
	public Button buttonNext;
	public String originalWord = "hello";
	public String explanation;
	public TextView textExplanation;
	static public int maxWordLength = 40;
	static public int editEMS = 2;
	static public int textEMS = 1;
	static public int CENTER = 0x00000011;
	public int[] blanket = new int[maxWordLength];
	public int[] test = new int[maxWordLength];// 設一旗標陣列紀錄有無挖空(有=1,無=0)
	public LinearLayout wordLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isSettingValidate()) {
			volumeSetting();
			
			getInfo(0);// LV:0~5

			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			// center
			layout.setGravity(CENTER);

			buttonClose = new Button(this);
			buttonNext = new Button(this);
			buttonClose.setText("SHUT UP!");
			buttonNext.setText("NEXT");
			textExplanation = new TextView(this);
			
			textExplanation.setText(explanation);
			textExplanation.setGravity(CENTER);
			showSpelledWord();
			
			  layout.addView(wordLayout);
			 layout.addView(textExplanation);
			  layout.addView(buttonClose);
			  layout.addView(buttonNext);
			  
			  setContentView(layout);
			  
			  buttonClose.setOnClickListener(CloseEvent);
			  buttonNext.setOnClickListener(NextEvent);
			 
		} else {
			Toast.makeText(WordSpelling.this, "Setting Changed. Please check",
					Toast.LENGTH_LONG).show();

		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
			Toast.makeText(WordSpelling.this, "Volume locked!",
				Toast.LENGTH_LONG).show();
			return true; 
		}
		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
			Toast.makeText(WordSpelling.this, "Volume locked!",
				Toast.LENGTH_LONG).show();
			return true; 
		}
		
		return false;
	}

	public void showSpelledWord() {
		try{
		wordLayout = new LinearLayout(this);
		// wordLayout.setOrientation(LinearLayout.VERTICAL);
		wordLayout.setGravity(CENTER);
		int length = originalWord.length();
		char[] charOriginalWord = originalWord.toCharArray();
		//判斷字母有無被挖空來設置正確的Text
		for (int i = 0; i < length; i++) {
			if (test[i] == 0) {
				TextView TextLetter = new TextView(this);
				TextLetter.setEms(textEMS);
				TextLetter.setSingleLine(true);
				TextLetter.setText(String.valueOf(charOriginalWord[i]));
				wordLayout.addView(TextLetter, i);
			}
			else {
				EditText EditLetter = new EditText(this);
				EditLetter.setSingleLine(true);

				InputFilter[] FilterArray = new InputFilter[1];
				FilterArray[0] = new InputFilter.LengthFilter(1);
				EditLetter.setFilters(FilterArray);

				EditLetter.setEms(editEMS);

				wordLayout.addView(EditLetter, i);
			}
			
		}
		 
		}catch (Exception e) {
			Toast.makeText(WordSpelling.this, e.getMessage(), Toast.LENGTH_LONG).show();
		};
		
	}

	public boolean isSettingValidate() {
		return true;
	}
	public void closeActivity()
	{
		
		this.finish();	
	}
	
	public OnClickListener CloseEvent = new OnClickListener() {
		public void onClick(View v) {
			int length = originalWord.length();
			StringBuilder spellingWord = new StringBuilder();
			for (int i = 0; i < length; i++) {
				View tmp = wordLayout.getChildAt(i);
				String viewClass = tmp.getClass().toString();
				if (tmp instanceof EditText) {
					EditText editTmp = (EditText) tmp;
					spellingWord.append(editTmp.getText());
				} else if (tmp instanceof TextView) {
					TextView textTmp = (TextView) tmp;
					spellingWord.append(textTmp.getText());
				}
			}
			if (spellingWord.toString().equals(originalWord)) {
				Toast.makeText(WordSpelling.this, "Cong!", Toast.LENGTH_LONG).show();
				try{
					Toast.makeText(WordSpelling.this, "ring Closed", Toast.LENGTH_LONG).show();
					 Message msg = Message.obtain();
		            msg.what = ringControlling.WAKED;
		            sendMsg(msg);
				}
				catch(Exception e)
				{
					Toast.makeText(WordSpelling.this, e.getMessage(), Toast.LENGTH_LONG).show();
				}
				closeActivity();

			} else {

				
			}
			
		}
	};
	public void sendMsg(Message msg)
	{
		AlarmReceiver.ringController.handler.sendMessage(msg);
	};
	public OnClickListener NextEvent = new OnClickListener() {
		public void onClick(View v) {

		}

	};

	public void volumeSetting() {
		
	}
	
	public void getInfo(int LV) {  //傳入Level:0~5
		int range = 0;
		int blanketNum = 0;
		try {
			Workbook book = Workbook.getWorkbook(new File("/sdcard/vocab.xls"));
			Sheet sheet = book.getSheet(0);

			switch (LV) {
			case 0:
				range = 210;
				break;
			case 1:
				range = 484;
				break;
			case 2:
				range = 1089;
				break;
			case 3:
				range = 1083;
				break;
			case 4:
				range = 1095;
				break;
			case 5:
				range = 1092;
				break;
			}

			Random rnd = new Random();
			int random = rnd.nextInt(range);
			Cell vocab = sheet.getCell(LV, random);// 從vocab.xsl檔中找出第X列第X行的字母
			String strVocab = vocab.getContents();
			int i = 0;
			for (i = 0; i < strVocab.length(); i++) {
				if (strVocab.charAt(i) == ' ') {
					originalWord = strVocab.substring(0, i);
					break;
				}
			}
			for (int j = i; j < strVocab.length(); j++) {
				if (strVocab.charAt(j) != ' ') {
					explanation = strVocab.substring(j, strVocab.length());
					break;
				}
			}

			blanketNum = (originalWord.length()) / 2;//挖空數目為單字長度的%50

			for (int j = 0; j < originalWord.length(); j++) {
				test[j] = 0;// 讓旗標歸0(代表未被挖空)
			}

			int k = 0;
			do {
				Random rnd2 = new Random();
				int random2 = rnd2.nextInt(originalWord.length());// 產生挖空位置的隨機數
				if (test[random2] == 0) {
					blanket[k] = random2;// 判斷如未被挖空，便將其位置挖空
					test[random2] = 1;// 並將其位置標記為已挖空
					k++;
				}
			} while (k != blanketNum);

			book.close(); // 關閉
		} catch (IOException e) {
			originalWord = "IO fail";
		} catch (Exception e) {
			originalWord = "fail";
		}
	}

}
