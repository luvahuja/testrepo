package com.mobileapp.journalist.sfsu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;

import com.exercise.AndroidVideoCapture.R;

import android.app.Activity;
import android.content.ContextWrapper;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidVideoCapture extends Activity implements
		SurfaceHolder.Callback, OnClickListener {

	Button buttonStop, buttonStart, source, pause;
	MediaRecorder mediaRecorder;
	SurfaceHolder surfaceHolder;
	boolean sourceVisible, recording;
	RadioGroup sourceInfo;
	RelativeLayout cameraTypeTray;
	TextView tv1, tv2, tv3, tv4, tv5;
	EditText tv6;

	static int pauseCount = 1;
	int lastFocusedTextView = 0;
	int changeTextViewColorNumber = 0;

	File myStorageDir;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sourceVisible = false;

		mediaRecorder = new MediaRecorder();
		initMediaRecorder();

		pauseCount = 1;
		setContentView(R.layout.main);

		SurfaceView myVideoView = (SurfaceView) findViewById(R.id.videoview);
		surfaceHolder = myVideoView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		tv1 = (TextView) findViewById(R.id.text1);
		tv2 = (TextView) findViewById(R.id.text2);
		tv3 = (TextView) findViewById(R.id.text3);
		tv4 = (TextView) findViewById(R.id.text4);
		tv5 = (TextView) findViewById(R.id.text5);
		tv6 = (EditText) findViewById(R.id.text6);
		Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int setWidth = width / 3;
		int height = display.getHeight();
		int setHeight = height / 2;
		System.out.println("height " + height + " set height " + setHeight);
		LayoutParams params = new LayoutParams(setWidth, setHeight);
		params.topMargin = 100;
		tv1.setLayoutParams(params);
		tv2.setLayoutParams(params);
		tv3.setLayoutParams(params);
		tv4.setLayoutParams(params);
		tv5.setLayoutParams(params);

		tv1.setOnClickListener(this);
		tv2.setOnClickListener(this);
		tv3.setOnClickListener(this);
		tv4.setOnClickListener(this);
		tv5.setOnClickListener(this);

		cameraTypeTray = (RelativeLayout) findViewById(R.id.camera_type_tray);
		sourceInfo = (RadioGroup) findViewById(R.id.source_info);
		source = (Button) findViewById(R.id.source);
		pause = (Button) findViewById(R.id.pause);
		pause.setOnClickListener(this);
		// buttonStart = (Button) findViewById(R.id.rec_start);
		buttonStop = (Button) findViewById(R.id.stop);
		// buttonStart.setOnClickListener(this);
		buttonStop.setOnClickListener(this);
		source.setOnClickListener(this);
	}

	private Button.OnClickListener myButtonOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.stop) {
				mediaRecorder.stop();
				mediaRecorder.release();
				// finish();
			} else if (arg0.getId() == R.id.start) {
				mediaRecorder.start();
				buttonStop.setText("STOP");

			}
			// if(argo)
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		prepareMediaRecorder();
		recording = false;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	private void initMediaRecorder() {
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		CamcorderProfile camcorderProfile_HQ = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);
		mediaRecorder.setProfile(camcorderProfile_HQ);

		File myStorageDir = new File(Environment.getExternalStorageDirectory()
				+ "/journalist");
		if (!myStorageDir.exists()) {

			myStorageDir.mkdir();

		}
		mediaRecorder.setOutputFile(myStorageDir + "/myRecording.mp4");
		// Toast.makeText(this, Environment.getExternalStorageDirectory()
		// + "/myRecording.mp4", Toast.LENGTH_LONG);
		// mediaRecorder.setOutputFile(path);
		System.out.println(myStorageDir + "/myRecording.mp4");
		// mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
		// mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M
	}

	private void initMediaRecorderAfterPauseState(int count) {
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		CamcorderProfile camcorderProfile_HQ = CamcorderProfile
				.get(CamcorderProfile.QUALITY_HIGH);
		mediaRecorder.setProfile(camcorderProfile_HQ);

		myStorageDir = new File(Environment.getExternalStorageDirectory()
				+ "/journalist");
		if (!myStorageDir.exists()) {

			myStorageDir.mkdir();

		}
		mediaRecorder.setOutputFile(myStorageDir + "/myRecording" + count
				+ ".mp4");
		// Toast.makeText(this, Environment.getExternalStorageDirectory()
		// + "/myRecording.mp4", Toast.LENGTH_LONG);
		// mediaRecorder.setOutputFile(path);
		System.out.println(myStorageDir + "/myRecordingPart.mp4");
		// mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
		// mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M
	}

	private void prepareMediaRecorder() {
		mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.stop && recording) {
			System.out.println("Hello stopiing video recording");

			recording = false;
			try {
				FileInputStream f1 = new FileInputStream(myStorageDir
						+ "/myRecording.mp4");
				FileInputStream f2 = new FileInputStream(myStorageDir
						+ "/myRecordingPart.mp4");

				SequenceInputStream sistream = new SequenceInputStream(f2, f1);
				FileOutputStream fostream = new FileOutputStream(myStorageDir
						+ "/myRecordingComplete.mp4");// destinationfile

				int[] temp = new int[1024];

				int count = 1024;
				byte[] buffer = new byte[count];
				while ((count = sistream.read(buffer, 0, 1024)) != -1) {
					// System.out.print( (char) temp ); // to print at DOS
					// prompt
					System.out.println("Mergining files" + buffer);
					fostream.write(buffer, 0, 1024); // to write to file
					// buffer = 0;

					// fos
				}
				fostream.close();
				sistream.close();
				f1.close();
				f2.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaRecorder.stop();

			mediaRecorder.release();

			// finish();
		} else if (v.getId() == R.id.stop && !recording) {

			pause.setClickable(true);
			mediaRecorder.start();
			System.out.println("Hello starting video recording");
			buttonStop.setText("STOP");
			recording = true;
		}
		if (v.getId() == R.id.pause && recording) {
			mediaRecorder.reset();
			//
			buttonStop.setText("Resume");
			initMediaRecorderAfterPauseState(pauseCount);
			pauseCount++;
			recording = false;
			prepareMediaRecorder();

		} else if (v.getId() == R.id.pause && !recording) {

		}
		if (v.getId() == R.id.source) {
			if (!sourceVisible) {
				sourceInfo.setVisibility(0);

				cameraTypeTray.setVisibility(0);
				sourceVisible = true;
			} else {
				sourceVisible = false;
				sourceInfo.setVisibility(4);

				cameraTypeTray.setVisibility(4);
			}
		}
		if (v.getId() == R.id.text1) {

			switch (changeTextViewColorNumber) {
			case 1:
				if (!tv6.getText().toString().matches(""))
					tv1.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 2:
				if (!tv6.getText().toString().matches(""))
					tv6.setText("");
				tv2.setBackgroundColor(0x3364FE2E);

				break;

			case 3:
				if (!tv6.getText().toString().matches(""))
					tv3.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 4:
				if (!tv6.getText().toString().matches(""))
					tv4.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;
			case 5:
				if (!tv6.getText().toString().matches(""))
					tv5.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			}

			changeTextViewColorNumber = 1;

			// lastFocusedTextView = 1;
			// tv6.setText("Answer 1");

		}
		if (v.getId() == R.id.text2) {
			switch (changeTextViewColorNumber) {
			case 1:
				if (!tv6.getText().toString().matches(""))
					tv1.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			case 2:
				if (!tv6.getText().toString().matches(""))
					tv2.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 3:
				if (!tv6.getText().toString().matches(""))
					tv3.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			case 4:
				if (!tv6.getText().toString().matches(""))
					tv4.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;
			case 5:
				if (!tv6.getText().toString().matches(""))
					tv5.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			}

			changeTextViewColorNumber = 2;
			// tv2.setBackgroundColor(0x3364FE2E);
			lastFocusedTextView = 2;

			// tv6.setText("Answer 2");

		}
		if (v.getId() == R.id.text3) {
			switch (changeTextViewColorNumber) {
			case 1:
				if (!tv6.getText().toString().matches(""))
					tv1.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			case 2:
				if (!tv6.getText().toString().matches(""))
					tv2.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			case 3:
				if (!tv6.getText().toString().matches(""))
					tv3.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			case 4:
				if (!tv6.getText().toString().matches(""))
					tv4.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;
			case 5:
				if (!tv6.getText().toString().matches(""))
					tv5.setBackgroundColor(0x3364FE2E);
				tv6.setText("");

				break;

			}

			changeTextViewColorNumber = 3;
			// tv3.setBackgroundColor(0x3364FE2E);
			lastFocusedTextView = 3;
			// tv6.setText("Answer 3");

		}
		if (v.getId() == R.id.text4) {

			switch (changeTextViewColorNumber) {
			case 1:
				if (!tv6.getText().toString().matches(""))
					tv1.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 2:
				if (!tv6.getText().toString().matches(""))
					tv2.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 3:
				if (!tv6.getText().toString().matches(""))
					tv3.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 4:
				if (!tv6.getText().toString().matches(""))
					tv4.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;
			case 5:
				if (!tv6.getText().toString().matches(""))
					tv5.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			}
			changeTextViewColorNumber = 4;
			// tv4.setBackgroundColor(0x3364FE2E);
			lastFocusedTextView = 4;

			// tv6.setText("Answer 4");

		}
		if (v.getId() == R.id.text5) {
			switch (changeTextViewColorNumber) {
			case 1:

				if (!tv6.getText().toString().matches(""))
					tv1.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 2:
				if (!tv6.getText().toString().matches(""))
					tv2.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 3:
				if (!tv6.getText().toString().matches(""))
					tv3.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			case 4:
				if (!tv6.getText().toString().matches(""))
					tv4.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;
			case 5:
				if (!tv6.getText().toString().matches(""))
					tv5.setBackgroundColor(0x3364FE2E);

				tv6.setText("");
				break;

			}

			changeTextViewColorNumber = 5;
			// tv5.setBackgroundColor(0x3364FE2E);
			lastFocusedTextView = 5;
			// tv6.setText("Answer 5");

		}
	}
}