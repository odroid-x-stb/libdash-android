package fr.enseirb.odroidx.libdash;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MockActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helloword_layout);
		final SAXParser parser = new SAXParser("http://www-itec.uni-klu.ac.at/ftp/datasets/mmsys12/BigBuckBunny/MPDs/BigBuckBunnyNonSeg_2s_isoffmain_DIS_23009_1_v_2_1c2_2011_08_30.mpd");
		((Button) findViewById(R.id.button_helloworld)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parser.parse();
			}
		});
	}
}
