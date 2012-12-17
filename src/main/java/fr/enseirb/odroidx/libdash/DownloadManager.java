package fr.enseirb.odroidx.libdash;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DownloadManager {

	private String baseURL;
	
	public DownloadManager(String baseURL) {
		this.baseURL = baseURL;
	}
	
	public InputStream retreiveSegment(String specificURL, int startByte, int endByte) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet getSegment = new HttpGet(baseURL + specificURL);
		getSegment.addHeader("Range", "bytes=" + startByte + "-" + endByte);
		InputStream content = null;
		try {
			HttpResponse response = httpClient.execute(getSegment);
			HttpEntity entity = response.getEntity();
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
}
