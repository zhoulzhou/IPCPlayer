package com.example.ipcplayer.http;

import javax.net.ssl.SSLSocketFactory;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.example.ipcplayer.application.IPCApplication;
import com.example.ipcplayer.utils.LogUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class HttpApi{
	private static String TAG = HttpApi.class.getSimpleName();
	private static final int TIMEOUT = 30;
	private static final String CHARSET = HTTP.UTF_8;
	private static final String DEFAULT_CLIENT_VERSION = "android_1.0.0";

	public static synchronized DefaultHttpClient getDefaultHttpClientSimple(){
		LogUtil.d(TAG + " getDefaultHttpClientSimple ");
		DefaultHttpClient httpClient = null ;
		if (null == httpClient) {
			HttpParams httpParams = createHttpParams(TIMEOUT);

			// �������ǵ�HttpClient֧��HTTP��HTTPS����ģʽ
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
//			schReg.register(new Scheme("https",
//					(SocketFactory) SSLSocketFactory.getDefault(), 443));

			// ʹ���̰߳�ȫ�����ӹ���������HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					httpParams, schReg);
			httpClient = new DefaultHttpClient(conMgr, httpParams);
		}
		return httpClient;
	}
	
	public static HttpParams createHttpParams(int timeout){
		LogUtil.d(TAG + " createHttpParams ");
		HttpParams httpParams = new BasicHttpParams();
		// Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		// ��ʱ����
        /* �����ӳ���ȡ���ӵĳ�ʱʱ�� */
        ConnManagerParams.setTimeout(httpParams, 1000);
        /* ���ӳ�ʱ */
        HttpConnectionParams.setConnectionTimeout(httpParams, 2000);
        /* ����ʱ */
        HttpConnectionParams.setSoTimeout(httpParams, 4000);
        
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams,CHARSET);
        HttpProtocolParams.setUseExpectContinue(httpParams, true);
        HttpProtocolParams.setUserAgent(httpParams,DEFAULT_CLIENT_VERSION);
      
        fillProxy(IPCApplication.getInstance().getApplicationContext(),httpParams);
		return httpParams ;
	}
	
	 /**
     * ���ݵ�ǰ����״̬������
     * @param context
     * @param httpParams
     */
	public static void fillProxy(final Context context,
			final HttpParams httpParams) {
		LogUtil.d(TAG + " fillProxy ");
		if (context == null) {
			return;
		}
			
		WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
		if (wifiManager.isWifiEnabled()) {
			return;
		}
		
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (networkInfo == null || networkInfo.getExtraInfo() == null) {
			return;
		}
		String info = networkInfo.getExtraInfo().toLowerCase(); // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
		// �ȸ�������apn��Ϣ�ж�,������ proxy �Զ�����
		if (info != null) {
			if (info.startsWith("cmwap") || info.startsWith("uniwap")
					|| info.startsWith("3gwap")) {
				HttpHost proxy = new HttpHost("10.0.0.172", 80);
				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
				return;
			} else if (info.startsWith("ctwap")) {
				HttpHost proxy = new HttpHost("10.0.0.200", 80);
				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
				return;
			} else if (info.startsWith("cmnet") || info.startsWith("uninet")
					|| info.startsWith("ctnet") || info.startsWith("3gnet")) {
				return;
			} // else fall through
		} // else fall through

		// ���û�� apn ��Ϣ������� proxy�����жϡ�
		// ����android 4.2 �� "content://telephony/carriers/preferapn"
		// ��ȡ���������ƣ�����ͨ��ϵͳ�ӿڻ�ȡ��

		// ���󲿷�����²����ߵ�����
		// ������������deprecated�ģ�����4.2���Կ���
		String defaultProxyHost = android.net.Proxy.getDefaultHost();    
		int defaultProxyPort = android.net.Proxy.getDefaultPort();

		if (defaultProxyHost != null && defaultProxyHost.length() > 0) {
			/*
			 * �޷����� proxy host ��ԭ apn ���� ���ﲻ���� mApn
			 */
			if ("10.0.0.172".equals(defaultProxyHost.trim())) {
				// ��ǰ������������Ϊcmwap || uniwap
				HttpHost proxy = new HttpHost("10.0.0.172", defaultProxyPort);
				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
			} else if ("10.0.0.200".equals(defaultProxyHost.trim())) {
				HttpHost proxy = new HttpHost("10.0.0.200", 80);
				httpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, proxy);
			} else {
			}
		} else {
			// �������綼������net
		}
	}
}