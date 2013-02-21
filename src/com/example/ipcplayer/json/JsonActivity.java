package com.example.ipcplayer.json;

import org.json.JSONObject;
import org.json.JSONStringer;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;

public class JsonActivity extends Activity{
	private CreateJson mJson ;
	private JSONObject mJSONObject;
	private JSONStringer mJsonText;
	private ParseJson mParseJson;
	private ParseSample mParseSample;
	
	private static final String JSON =   
			"{" +  
			    "   \"phone\" : [\"12345678\", \"87654321\"]," +  
			    "   \"name\" : \"yuanzhifei89\"," +  
			    "   \"age\" : 100," +  
			    "   \"address\" : { \"country\" : \"china\", \"province\" : \"jiangsu\" }," +  
			    "   \"married\" : false," +  
			"}"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mJson = new CreateJson();
		mJSONObject = mJson.createJson1();
		System.out.println("mJSONOhject: " +mJSONObject.toString());
		System.out.println("name: " + mJson.getPersonName());
		
		mJsonText = mJson.createJson2();
		System.out.println("mJsonText: " + mJsonText);
		
		mParseJson = new ParseJson();
		mParseJson.parse2(JSON);
		
		mParseSample = new ParseSample();
		mParseSample.parseJson();
		
	}
	
}