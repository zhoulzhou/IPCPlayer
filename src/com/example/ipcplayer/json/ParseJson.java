package com.example.ipcplayer.json;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ParseJson{
	//this is good 
	//http://blog.csdn.net/aomandeshangxiao/article/details/7000077
	private static final String JSON =   
			"{" +  
			    "   \"phone\" : [\"12345678\", \"87654321\"]," +  
			    "   \"name\" : \"yuanzhifei89\"," +  
			    "   \"age\" : 100," +  
			    "   \"address\" : { \"country\" : \"china\", \"province\" : \"jiangsu\" }," +  
			    "   \"married\" : false" +  
			"}"; 

	public void parse3(String json) {
		try {
			JSONTokener jsonParser = new JSONTokener(JSON);
			// 继续向下读8个json文本中的字符。此时刚开始，即在{处
			jsonParser.next(8); // { "phone。tab算一个字符

			// 继续向下读1个json文本中的字符
			jsonParser.next(); // "

			// 继续向下读取一个json文本中的字符。该字符不是空白、同时也不是注视中的字符
			jsonParser.nextClean(); // :

			// 返回当前的读取位置到第一次遇到'a'之间的字符串（不包括a）。
			jsonParser.nextString('a'); // ["12345678", "87654321"], "n（前面有两个空格）

			// 返回当前读取位置到第一次遇到字符串中(如"0089")任意字符之间的字符串，同时该字符是trimmed的。（此处就是第一次遇到了89）
			jsonParser.nextTo("0089"); // me" : "yuanzhifei

			// 读取位置撤销一个
			jsonParser.back();
			jsonParser.next(); // i

			// 读取位置前进到指定字符串处（包括字符串）
			jsonParser.skipPast("address");
			jsonParser.next(8); // " : { "c

			// 读取位置前进到执行字符处（不包括字符）
			jsonParser.skipTo('m');
			jsonParser.next(8); // married"
		} catch (JSONException ex) {
			// 异常处理代码
		}
	}
	
	public void parse(String json){
		System.out.println("json: " + json);
		StringBuilder string = new StringBuilder();
		try {
			JSONTokener jsonParser = new JSONTokener(json);
			// 此时还未读取任何json文本，直接读取就是一个JSONObject对象。  
		    // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）  
			JSONObject person = (JSONObject) jsonParser.nextValue();
			System.out.println("parse: get jsonobject ");
			// 接下来的就是JSON对象的操作了  
			person.getString("name");
			System.out.println("name: " + person.getString("name"));
			person.getJSONArray("phone");
			System.out.println("phone: " + person.getJSONArray("phone"));
			person.getBoolean("married");
			System.out.println("married: " + person.getBoolean("married"));
			person.getInt("age");
			System.out.println("age: " + person.getInt("age"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void parse2(String json){
		System.out.println("json: " + json);
		try{
			JSONTokener jsonParser = new JSONTokener(json);
			// 继续向下读8个json文本中的字符。此时刚开始，即在{处 
			jsonParser.next();
			System.out.println("0: " + jsonParser.next());
			jsonParser.next(1);
			System.out.println("1: " + jsonParser.next(1));
			jsonParser.next(2);
			System.out.println("2: " + jsonParser.next(2));
			jsonParser.next(3);
			System.out.println("3: " + jsonParser.next(3));
			jsonParser.next(4);
			System.out.println("4: " + jsonParser.next(4));
			jsonParser.next(5);
			System.out.println("5: " + jsonParser.next(5));
			jsonParser.next(6);
			System.out.println("6: " + jsonParser.next(6));
			jsonParser.next(7);
			System.out.println("7: " + jsonParser.next(7));
			jsonParser.next(8);
			System.out.println("8: " + jsonParser.next(8));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}