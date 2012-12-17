package fr.enseirb.odroidx.libdash;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.VideoView;

public class VideoActivity extends Activity implements DownloadManagerCallback {

	private static final String TAG = VideoActivity.class.getSimpleName();
	
	private DownloadManager downloadManager;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private VideoView videoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_layout);
		downloadManager = new DownloadManager(this, "http://www-itec.uni-klu.ac.at/ftp/datasets/mmsys12/BigBuckBunny/bunny_1s/");
		videoView = (VideoView) findViewById(R.id.videoView);	 
		downloadManager.downloadSegmentContent(new Segment(0, 0, 100862));
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				Log.d(TAG, "Media ready");
				mediaPlayer.start();
			}
		});
	}

	@Override
	public void segmentRetreived(Segment segment) {
		Log.d(TAG, "Segment " + segment.getId() + " downloaded!");
		// Write in a temp file
		File tmpFile;
		try {
			tmpFile = File.createTempFile("dashBuffer", ".mp4");
			File testFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "test.mp4");
			FileOutputStream testStream = new FileOutputStream(testFile);
			FileOutputStream outputStream = new FileOutputStream(tmpFile);
			int bufChar = 0;
			while ((bufChar = segment.getContent().read()) != -1) {
				outputStream.write(bufChar);
				testStream.write(bufChar);
			}
			testStream.flush();
			testStream.close();
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return; 
		}
		try {
			mediaPlayer.setDataSource(tmpFile.getAbsolutePath());
			mediaPlayer.setDisplay(videoView.getHolder());
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
