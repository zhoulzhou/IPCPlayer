package com.example.ipcplayer.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.example.ipcplayer.utils.LogUtil;

public class CreateJson{
	private static final String TAG = CreateJson.class.getSimpleName();
	JSONObject mPerson = new JSONObject();
	
	// 假设现在要创建这样一个json文本  
//  {  
//      "phone" : ["12345678", "87654321"], // 数组  
//      "name" : "yuanzhifei89", // 字符串  
//      "age" : 100, // 数值  
//      "address" : { "country" : "china", "province" : "jiangsu" }, // 对象  
//      "married" : false // 布尔值  
//  } 
	public JSONObject createJson1(){
		
		try {
			JSONArray phone = new JSONArray();
			phone.put("123456").put("654321");

			mPerson.put("phone", phone);
			mPerson.put("name", "yuanzhifei89");
			mPerson.put("age", 100);
			
			JSONObject address = new JSONObject();
			address.put("country", "china");
			address.put("province", "jiangsu");
			
			mPerson.put("address", address);
			mPerson.put("married", false);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtil.d(TAG + " createJson1 exception :");
			e.printStackTrace();
			return null;
		}
		return mPerson; 
	}
	
	public String getPersonName(){
		String name = "";
		try {
			name = mPerson.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			LogUtil.d(TAG + " getPersonName exception :");
			e.printStackTrace();
			name = "";
		}
		return name;
	}

    public JSONStringer createJson2(){
    	JSONStringer jsonText = new JSONStringer();
    	try {
			jsonText.object();
			
			jsonText.key("phone");
			jsonText.array();
			jsonText.value("123456").value("123456");
			jsonText.endArray();
			
			jsonText.key("name");
			jsonText.value("yuanzhifei89");
			
			jsonText.key("age");
			jsonText.value(100);
			
			jsonText.key("address");
			jsonText.object();
			jsonText.key("country");
			jsonText.value("china");
			jsonText.key("province");
			jsonText.value("jiangsu");
			jsonText.endObject();
			
			jsonText.key("married");
			jsonText.value(false);
			
			jsonText.endObject();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return jsonText ;
    }
}