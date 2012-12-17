package fr.enseirb.odroidx.libdash;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class DownloadManager {

	private static final String TAG = DownloadManager.class.getSimpleName();
	
	private String baseURL;
	private DownloadManagerCallback listener;
	
	public DownloadManager(DownloadManagerCallback listener, String baseURL) {
		this.listener = listener;
		this.baseURL = baseURL;
	}
	
	public void downloadSegmentContent(Segment segment) {
		new DownloadThread(segment).start();
	}
	
	private InputStream retreiveSegmentContent(String specificURL, int startByte, int endByte) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getSegment = new HttpGet(baseURL + specificURL);
		getSegment.addHeader("Range", "bytes=" + startByte + "-" + endByte);
		InputStream content = null; 
		try {
			HttpResponse response = httpClient.execute(getSegment);
			HttpEntity entity = response.getEntity();
			Log.d(TAG, "Status code: " + response.getStatusLine().getStatusCode());
			content = entity.getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}
	
	private class DownloadThread extends Thread {
		
		private Segment segment;
		
		public DownloadThread(Segment segment) {
			this.segment = segment;
		}
		
		@Override
		public void run() {
			segment.setContent(retreiveSegmentContent("bunny_1s_300kbit/bunny_300kbit_dashNonSeg.mp4", segment.getStartByte(), segment.getEndByte()));
			listener.segmentRetreived(segment);
		}
	}
}
