package com.example.ipcplayer.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.ipcplayer.R;

import android.app.Activity;
import android.os.Bundle;

public class XmlActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Thread thread = new Thread(new NetConn());
		thread.start();
//		try {
//		URL url = new URL("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=2.4.0&method=baidu.ting.billboard.billList&format=xml&type=1");
//		HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
//		InputStream in = urlconn.getInputStream();
//		Pull.pullParse2(in);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private class NetConn implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL url = new URL("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=2.4.0&method=baidu.ting.billboard.billList&format=xml&type=1");
				System.out.println("url: " + url);
				HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
				InputStream in = urlconn.getInputStream();
				System.out.println("in: " + in);
				List<Song> songs = new ArrayList<Song>();
				Song song = new Song();
				songs = Pull.pullParse2(in);
				for(int i=0 ; i< songs.size(); i++){
					song = songs.get(i);
					System.out.println("songname: " + song.getSongName());
					System.out.println("lrclink: " + song.getLrclink());
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
}