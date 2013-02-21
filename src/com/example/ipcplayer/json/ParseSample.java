package com.example.ipcplayer.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ParseSample{
	 String json = "{"
	         + "  \"query\": \"Pizza\", "
	         + "  \"locations\": [ 94043, 90210 ] "
	         + "}";

	 public void parseJson(){
		try {
			JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
			String query = object.getString("query");
			System.out.println("query: " + query);
			JSONArray locations = object.getJSONArray("locations");
			System.out.println("locations: " + locations);
			for(int i = 0 ; i < locations.length(); i++){
				int location = locations.getInt(i);
				System.out.println("location: " + location);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	 
}