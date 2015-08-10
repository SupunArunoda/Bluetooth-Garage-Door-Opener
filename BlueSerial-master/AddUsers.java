package com.blueserial;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddUsers extends Activity {
	private EditText mAutherName;//initilase for my purpose
	private EditText mAutherPassWord;//initilase for my purpose
	private EditText mNewName;//initilase for my purpose
	private EditText mNewPassWord;//initilase for my purpose
	private TextView mRecieiveText;
	private Button mBtnOK;
	private BluetoothSocket mBTSocket;
	private BluetoothDevice mDevice;
	private UUID mDeviceUUID;
	private int mMaxChars = 50000;//Default
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adding_users);
		ActivityHelper.initialize(this);
		mBtnOK = (Button) findViewById(R.id.ok);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		mDevice = b.getParcelable(Homescreen.DEVICE_EXTRA);
		mDeviceUUID = UUID.fromString(b.getString(Homescreen.DEVICE_UUID));
		mMaxChars = b.getInt(Homescreen.BUFFER_SIZE);
		
		mAutherName=(EditText)findViewById(R.id.auterizedName);
		mAutherPassWord=(EditText)findViewById(R.id.autherizedPassword);
		mNewName=(EditText)findViewById(R.id.newuser_name);
		mNewPassWord=(EditText)findViewById(R.id.newuser_password);
		
		mRecieiveText=(TextView)findViewById(R.id.txtaddReceive);
		mBtnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				try {
					String sr=mAutherName.getText().toString()+" "+mAutherPassWord.getText().toString()+" "+mNewName.getText().toString()+" "+mNewPassWord.getText().toString();
					mBTSocket.getOutputStream().write(sr.getBytes());
					//mBTSocket.getOutputStream().write(mEditSend.getText().toString().getBytes());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
	}	
	private class ReadInput implements Runnable {

		private boolean bStop = false;
		private Thread t;

		public ReadInput() {
			t = new Thread(this, "Input Thread");
			t.start();
		}

		public boolean isRunning() {
			return t.isAlive();
		}

		@Override
		public void run() {
			InputStream inputStream;

			try {
				inputStream = mBTSocket.getInputStream();
				while (!bStop) {
					String success="sucessfully";//get the ardiuno value
					String error="authorerror";
					String exists="alreadyexists";
					final String setTheStatus;
					byte[] buffer = new byte[256];
					if (inputStream.available() > 0) {
						inputStream.read(buffer);
						int i = 0;
						/*
						 * This is needed because new String(buffer) is taking the entire buffer i.e. 256 chars on Android 2.3.4 http://stackoverflow.com/a/8843462/1287554
						 */
						for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
						}
						
						//for my purpose
						final String strInput = new String(buffer, 0, i);
						if(strInput.equals(success)){
							setTheStatus="You have SUCESSFULLY added the user....";
							
						}else if(strInput.equals(exists)){
							setTheStatus="Sorry!!! Username and Password are already exists....";
							
						}else if(strInput.equals(error)){
							setTheStatus="Sorry...Authentication ERROR.. Check authentic username and password";
							
						}else{
							setTheStatus=".....SYSTEM ERROR.....";
						}
						/*
						 * If checked then receive text, better design would probably be to stop thread if unchecked and free resources, but this is a quick fix
						 */

				
							mRecieiveText.post(new Runnable() {
								@Override
								
								public void run() {
									mRecieiveText.setText("");
									mRecieiveText.append(setTheStatus);
									//Uncomment below for testing
									//mTxtReceive.append("\n");
									//mTxtReceive.append("Chars: " + strInput.length() + " Lines: " + mTxtReceive.getLineCount() + "\n");
									
									int txtLength = mRecieiveText.getEditableText().length();  
									if(txtLength > mMaxChars){
										mRecieiveText.getEditableText().delete(0, txtLength - mMaxChars);
									}

									/*if (chkScroll.isChecked()) { // Scroll only if this is checked
										scrollView.post(new Runnable() { // Snippet from http://stackoverflow.com/a/4612082/1287554
													@Override
													public void run() {
														scrollView.fullScroll(View.FOCUS_DOWN);
													}
												});
									}*/
								}
								
							});
						

					}
					Thread.sleep(500);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void stop() {
			bStop = true;
		}

	}
}

