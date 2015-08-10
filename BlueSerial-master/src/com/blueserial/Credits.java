package com.blueserial;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;

public class Credits extends Activity{
	private Button contact;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cres);
		
		/*ActivityHelper.initialize(this);
		TextView link = (TextView) findViewById(R.id.contactus);
	    String linkText = "Visit the <a href='http://btdooropener.esy.es/Home'>Bluetoot Door Opener</a> web page.";
	    link.setText(Html.fromHtml(linkText));
	    link.setMovementMethod(LinkMovementMethod.getInstance());*/
	}
}
