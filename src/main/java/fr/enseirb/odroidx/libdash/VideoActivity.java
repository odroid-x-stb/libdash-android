package fr.enseirb.odroidx.libdash;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.widget.VideoView;

public class VideoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_layout);
		 VideoView videoView = (VideoView)findViewById(R.id.videoView);
		 SurfaceHolder surfaceHolder = videoView.getHolder();
		 MediaPlayer mediaPlayer = new MediaPlayer();
		 mediaPlayer.setDisplay(surfaceHolder);
		 try {
			mediaPlayer.setDataSource(bufferToFD(null));
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 mediaPlayer.start();
	}
	
	private FileDescriptor bufferToFD(byte[] buffer) {
		FileDescriptor fd = new FileDescriptor();
		FileOutputStream stream = new FileOutputStream(fd);
		try {
			stream.write(buffer);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fd;
	}
}
