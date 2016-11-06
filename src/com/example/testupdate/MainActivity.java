package com.example.testupdate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initBtn();
	}

	private void initBtn() {
		btn = (Button) findViewById(R.id.btn_check_version);
		btn.setTextSize(20);
		btn.setText("CheckVersion");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				UpdateManager updateManager = new UpdateManager(
						MainActivity.this);
				updateManager.checkUpdates(true);
			}
		});
	}
}