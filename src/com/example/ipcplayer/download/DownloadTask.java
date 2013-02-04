package com.example.ipcplayer.download;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;

import com.example.ipcplayer.utils.LogUtil;
import com.example.ipcplayer.utils.NetworkUtil;
import com.example.ipcplayer.utils.StorageUtil;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadTask extends AsyncTask<Void, Integer, Long> {

    public final static int TIME_OUT = 30000;
    private final static int BUFFER_SIZE = 1024 * 8;

    private static final String TAG = "DownloadTask";
    private static final boolean DEBUG = true;
    private static final String TEMP_SUFFIX = ".tmp";

    private URL mURL;
    private File mFile;
    private File mTempFile;
    private String mUrl;
    private RandomAccessFile mOutputStream;
    private DownloadListener mListener;
    private Context mContext;

    private long mDownloadSize;
    private long mPreviousFileSize;
    private long mTotalSize;
    private long mDownloadPercent;
    private long mNetworkSpeed;
    private long mPreviousTime;
    private long mTotalTime;
    private Throwable error = null;
    private boolean mInterrupt = false;
    private DownloadInfo mDownloadInfo;
    
    private final class ProgressReportingRandomAccessFile extends RandomAccessFile {
        private int progress = 0; 
        public ProgressReportingRandomAccessFile(File file, String mode)
                throws FileNotFoundException {

            super(file, mode);
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {

            super.write(buffer, offset, count);
            progress += count;
            publishProgress(progress);
        }
    }

    public DownloadTask(Context context, String url, String path) {

        this(context, url, path, null);
    }

    public DownloadTask(Context context, String mUrl, String path, DownloadListener listener){

        super();
        this.mUrl = mUrl;
        try {
			this.mURL = new URL(mUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.mListener = listener;
        String fileName = new File(mURL.getFile()).getName();
        this.mFile = new File(path, fileName);
        this.mTempFile = new File(path, fileName + TEMP_SUFFIX);
        this.mContext = context;
    }

    public String getURL() {

        return mUrl;
    }

    public boolean isInterrupt() {

        return mInterrupt;
    }

    public long getDownloadPercent() {

        return mDownloadPercent;
    }

    public long getDownloadSize() {

        return mDownloadSize + mPreviousFileSize;
    }

    public long getTotalSize() {

        return mTotalSize;
    }

    public long getDownloadSpeed() {

        return this.mNetworkSpeed;
    }

    public long getTotalTime() {

        return this.mTotalTime;
    }

    public DownloadListener getListener() {

        return this.mListener;
    }

    @Override
    protected void onPreExecute() {

        mPreviousTime = System.currentTimeMillis();
        if (mListener != null)
            mListener.preDownload(mDownloadInfo);
    }

    @Override
    protected Long doInBackground(Void... params) {

        long result = -1;
        try {
            result = download();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

        if (progress.length > 1) {
            mTotalSize = progress[1];
            if (mTotalSize == -1) {
                if (mListener != null)
                    mListener.errorDownload(mDownloadInfo);
            } else {

            }
        } else {
            mTotalTime = System.currentTimeMillis() - mPreviousTime;
            mDownloadSize = progress[0];
            mDownloadPercent = (mDownloadSize + mPreviousFileSize) * 100 / mTotalSize;
            mNetworkSpeed = mDownloadSize / mTotalTime;
            if (mListener != null)
                mListener.updateProgress(mDownloadInfo);
        }
    }

    @Override
    protected void onPostExecute(Long result) {

        if (result == -1 || mInterrupt || error != null) {
            if (DEBUG && error != null) {
                Log.v(TAG, "Download failed." + error.getMessage());
            }
            if (mListener != null) {
                mListener.errorDownload(mDownloadInfo);
            }
            return;
        }
        // finish download
        mTempFile.renameTo(mFile);
        if (mListener != null)
            mListener.finishDownload(mDownloadInfo);
    }

    @Override
    public void onCancelled() {

        super.onCancelled();
        mInterrupt = true;
    }

    private AndroidHttpClient client;
    private HttpGet httpGet;
    private HttpResponse response;

    private long download() throws IOException, NetworkErrorException{

        if (DEBUG) {
            Log.v(TAG, "mTotalSize: " + mTotalSize);
        }

        /*
         * check net work
         */
        if (!NetworkUtil.isNetworkAvailable()) {
            throw new NetworkErrorException("Network blocked.");
        }

        /*
         * check file length
         */
        client = AndroidHttpClient.newInstance("DownloadTask");
        httpGet = new HttpGet(mUrl);
        response = client.execute(httpGet);
        mTotalSize = response.getEntity().getContentLength();

        if (mFile.exists() && mTotalSize == mFile.length()) {
             LogUtil.d(TAG + "Output file already exists. Skipping download.");

//            throw new FileAlreadyExistException("Output file already exists. Skipping download.");
        } else if (mTempFile.exists()) {
            httpGet.addHeader("Range", "bytes=" + mTempFile.length() + "-");
            mPreviousFileSize = mTempFile.length();

            client.close();
            client = AndroidHttpClient.newInstance("DownloadTask");
            response = client.execute(httpGet);

            if (DEBUG) {
                Log.v(TAG, "File is not complete, download now.");
                Log.v(TAG, "File length:" + mTempFile.length() + " mTotalSize:" + mTotalSize);
            }
        }

        /*
         * check memory
         */
        long storage = StorageUtil.getFreeSpace();
        if (DEBUG) {
            Log.i(null, "storage:" + storage + " mTotalSize:" + mTotalSize);
        }

        if (mTotalSize - mTempFile.length() > storage) {
//            throw new NoMemoryException("SD card no memory.");
        }

        /*
         * start download
         */
        mOutputStream = new ProgressReportingRandomAccessFile(mTempFile, "rw");

        publishProgress(0, (int) mTotalSize);

        InputStream input = response.getEntity().getContent();
        int bytesCopied = copy(input, mOutputStream);

        if ((mPreviousFileSize + bytesCopied) != mTotalSize && mTotalSize != -1 && !mInterrupt) {
            throw new IOException("Download incomplete: " + bytesCopied + " != " + mTotalSize);
        }

        if (DEBUG) {
            Log.v(TAG, "Download completed successfully.");
        }

        return bytesCopied;

    }

    public int copy(InputStream input, RandomAccessFile out) throws IOException,
            NetworkErrorException {

        if (input == null || out == null) {
            return -1;
        }

        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        if (DEBUG) {
            Log.v(TAG, "length" + out.length());
        }

        int count = 0, n = 0;
        long errorBlockTimePreviousTime = -1, expireTime = 0;

        try {

            out.seek(out.length());

            while (!mInterrupt) {
                n = in.read(buffer, 0, BUFFER_SIZE);
                if (n == -1) {
                    break;
                }
                out.write(buffer, 0, n);
                count += n;

                /*
                 * check network
                 */
                if (!NetworkUtil.isNetworkAvailable()) {
                    throw new NetworkErrorException("Network blocked.");
                }

                if (mNetworkSpeed == 0) {
                    if (errorBlockTimePreviousTime > 0) {
                        expireTime = System.currentTimeMillis() - errorBlockTimePreviousTime;
                        if (expireTime > TIME_OUT) {
                            throw new ConnectTimeoutException("connection time out.");
                        }
                    } else {
                        errorBlockTimePreviousTime = System.currentTimeMillis();
                    }
                } else {
                    expireTime = 0;
                    errorBlockTimePreviousTime = -1;
                }
            }
        } finally {
            client.close(); // must close client first
            client = null;
            out.close();
            in.close();
            input.close();
        }
        return count;

    }

}
