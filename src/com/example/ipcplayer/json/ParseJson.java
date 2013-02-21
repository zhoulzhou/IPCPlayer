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
			// �������¶�8��json�ı��е��ַ�����ʱ�տ�ʼ������{��
			jsonParser.next(8); // { "phone��tab��һ���ַ�

			// �������¶�1��json�ı��е��ַ�
			jsonParser.next(); // "

			// �������¶�ȡһ��json�ı��е��ַ������ַ����ǿհס�ͬʱҲ����ע���е��ַ�
			jsonParser.nextClean(); // :

			// ���ص�ǰ�Ķ�ȡλ�õ���һ������'a'֮����ַ�����������a����
			jsonParser.nextString('a'); // ["12345678", "87654321"], "n��ǰ���������ո�

			// ���ص�ǰ��ȡλ�õ���һ�������ַ�����(��"0089")�����ַ�֮����ַ�����ͬʱ���ַ���trimmed�ġ����˴����ǵ�һ��������89��
			jsonParser.nextTo("0089"); // me" : "yuanzhifei

			// ��ȡλ�ó���һ��
			jsonParser.back();
			jsonParser.next(); // i

			// ��ȡλ��ǰ����ָ���ַ������������ַ�����
			jsonParser.skipPast("address");
			jsonParser.next(8); // " : { "c

			// ��ȡλ��ǰ����ִ���ַ������������ַ���
			jsonParser.skipTo('m');
			jsonParser.next(8); // married"
		} catch (JSONException ex) {
			// �쳣�������
		}
	}
	
	public void parse(String json){
		System.out.println("json: " + json);
		StringBuilder string = new StringBuilder();
		try {
			JSONTokener jsonParser = new JSONTokener(json);
			// ��ʱ��δ��ȡ�κ�json�ı���ֱ�Ӷ�ȡ����һ��JSONObject����  
		    // �����ʱ�Ķ�ȡλ����"name" : �ˣ���ônextValue����"yuanzhifei89"��String��  
			JSONObject person = (JSONObject) jsonParser.nextValue();
			System.out.println("parse: get jsonobject ");
			// �������ľ���JSON����Ĳ�����  
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
			// �������¶�8��json�ı��е��ַ�����ʱ�տ�ʼ������{�� 
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