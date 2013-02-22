package com.example.ipcplayer.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.example.ipcplayer.utils.LogUtil;

import android.util.Xml;

public class Pull{
	private final static String TAG = Pull.class.getSimpleName();
	
	public static List<Person>	pullParse(InputStream inputStream){
		List<Person> persons = null;
		XmlPullParser parser = Xml.newPullParser();
		try{
			parser.setInput(inputStream, "UTF-8");
			int eventType = parser.getEventType();
			Person currentPerson = null ;
			while(eventType != XmlPullParser.END_DOCUMENT){
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					persons = new ArrayList<Person>();
					break;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					if(name.equalsIgnoreCase("person")){
						currentPerson = new Person();
						currentPerson.setPersonId(new Integer(parser.getAttributeValue(null, "id")));
					}else if(currentPerson != null){
						if(name.equalsIgnoreCase("name")){
							currentPerson.setName(parser.nextText());
						}else if(name.equalsIgnoreCase("age")){
							currentPerson.setAge(new Short(parser.nextText()));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if(parser.getName().equalsIgnoreCase("person") && currentPerson != null){
						persons.add(currentPerson);
					}
					currentPerson = null;
					break;
				}
				eventType = parser.next();
			}
			inputStream.close();
			return persons;
		}catch(Exception e){
			LogUtil.d(TAG + " pullParse exception: ");
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<Song> pullParse2(InputStream inputStream){
		LogUtil.d(TAG + " pullParse2 ");
		List<Song> songs = null;
		Song song = null;
		XmlPullParser parser = Xml.newPullParser();
		try{
			parser.setInput(inputStream, "UTF-8");
			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				LogUtil.d(TAG + "eventType: " + eventType);
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					songs = new ArrayList<Song>();
					break ;
				case XmlPullParser.START_TAG:
					String name = parser.getName();
					LogUtil.d(TAG + " name: " + name);
					if(name.equalsIgnoreCase("song")){
						song = new Song();
					}else if(song != null){
						if(name.equalsIgnoreCase("song_id")){
							song.setSongId(new Integer(parser.nextText()));
						}else if(name.equalsIgnoreCase("title")){
							song.setSongName(parser.nextText());
							LogUtil.d(TAG + " get title ++++++++++++++++++++++++++++++++++++++++");
						}else if(name.equalsIgnoreCase("author")){
							song.setArtist(parser.nextText());
							LogUtil.d(TAG + " get author ++++++++++++++++++++++++++++++++++++++++");
						}else if(name.equalsIgnoreCase("lrclink")){
							song.setLrclink(parser.nextText());
							LogUtil.d(TAG + " get lrclink ++++++++++++++++++++++++++++++++++++++++");
						}
					}
					break ;
				case XmlPullParser.END_TAG:
					if(parser.getName().equalsIgnoreCase("song") && song != null){
						songs.add(song);
					    song = null;
					}
					break;
				}
				eventType = parser.next();
				
			}
			inputStream.close();
			return songs;
		}catch(Exception e){
			LogUtil.d(TAG + " pullParse2 exception: ");
			e.printStackTrace();
		}
		return null;
	}
}