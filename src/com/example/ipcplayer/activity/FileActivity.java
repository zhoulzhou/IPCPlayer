package com.example.ipcplayer.activity;

 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStreamReader;
 import java.io.RandomAccessFile;

import com.example.ipcplayer.R;

 import android.app.Activity;
 import android.os.Bundle;
 import android.os.Environment;
 import android.view.View;
 import android.view.View.OnClickListener;
 import android.widget.Button;
 import android.widget.EditText;
import android.os.Bundle;
 
 public class FileActivity  extends Activity
 {
     final String FILE_NAME = "/FileSDTest.ini";
 
     @Override
     public void onCreate(Bundle savedInstanceState)
     {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.file);
         // ��ȡ������ť
         Button read = (Button) findViewById(R.id.read);
         Button write = (Button) findViewById(R.id.write);
         // ��ȡ�����ı���
         final EditText edit1 = (EditText) findViewById(R.id.edit1);
         final EditText edit2 = (EditText) findViewById(R.id.edit2);
         // Ϊwrite��ť���¼�������
         write.setOnClickListener(new OnClickListener()
         {
             @Override
             public void onClick(View source)
             {
                 // ��edit1�е�����д���ļ���
                 write(edit1.getText().toString());
                 edit1.setText("");
             }
         });
 
         read.setOnClickListener(new OnClickListener()
         {
             @Override
             public void onClick(View v)
             {
                 // ��ȡָ���ļ��е����ݣ�����ʾ����
                 edit2.setText(read());
             }
         });
     }
 
     private String read()
     {
         try
         {
             //����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
             if (Environment.getExternalStorageState()
                 .equals(Environment.MEDIA_MOUNTED))
             {
                 //��ȡSD����Ӧ�Ĵ洢Ŀ¼
                 File sdCardDir = Environment.getExternalStorageDirectory();
                 //��ȡָ���ļ���Ӧ��������
                 FileInputStream fis = new FileInputStream(sdCardDir
                     .getCanonicalPath()    + FILE_NAME);
                 //��ָ����������װ��BufferedReader
                 BufferedReader br = new BufferedReader(new 
                     InputStreamReader(fis));
                 StringBuilder sb = new StringBuilder("");
                 String line = null;
                 while((line = br.readLine()) != null)
                 {
                     sb.append(line);
                 }
                 return sb.toString();
             }
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
         return null;
     }
 
     private void write(String content)
     {
         try
         {    
             //����ֻ�������SD��������Ӧ�ó�����з���SD��Ȩ��
             if (Environment.getExternalStorageState()
                 .equals(Environment.MEDIA_MOUNTED))
             {
                 //��ȡSD����Ŀ¼
                 File sdCardDir = Environment.getExternalStorageDirectory();
                 File targetFile = new File(sdCardDir.getCanonicalPath()
                     + FILE_NAME);
                 //��ָ���ļ�����    RandomAccessFile����,��һ���������ļ����ƣ��ڶ��������Ƕ�дģʽ
                 RandomAccessFile raf = new RandomAccessFile(
                     targetFile , "rw");
                 //���ļ���¼ָ���ƶ������
                 raf.seek(targetFile.length());
                 // ����ļ�����
                 raf.write(content.getBytes());
                 raf.close();
             }
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
     }
 }