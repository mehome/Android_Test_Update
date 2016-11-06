package com.example.testupdate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class CommonDialog extends Dialog {
	private TextView tvTitle, tv_Msg;
	private Button btnOk, btnCancel;
	private ProgressBar progressBar;
	private LinearLayout dialog_container;
	private Activity activity;

	// private Context mContext;

	public CommonDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public CommonDialog(Context context, int theme) {
		super(context, theme);
		// this.mContext = context;
		setContentView(R.layout.update_dialog);
		this.activity = (Activity) context;
		initView();
	}

	public CommonDialog(Context context) {
		this(context, R.style.HHDialog);
	}

	private void initView() {
		dialog_container = (LinearLayout) findViewById(R.id.dialog_container);
		@SuppressWarnings("deprecation")
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				(int) (getDisplay().getWidth() * 0.9), (int) (getDisplay()
						.getHeight() * 0.3));
		dialog_container.setLayoutParams(params);
		tvTitle = (TextView) findViewById(R.id.dialog_title);
		tv_Msg = (TextView) findViewById(R.id.dialog_message);
		progressBar = (ProgressBar) findViewById(R.id.dialog_progressbar);
		btnOk = (Button) findViewById(R.id.dialog_btn_ok);
		btnCancel = (Button) findViewById(R.id.dialog_btn_cancel);
	}

	public void setTitle(String title) {
		tvTitle.setVisibility(View.VISIBLE);
		tvTitle.setTextColor(R.color.black);
		tvTitle.setText(title);
	}

	public void setMsg(String msg) {
		tv_Msg.setVisibility(View.VISIBLE);
		tv_Msg.setTextColor(R.color.black);
		tv_Msg.setText(msg);
	}

	public void setProgressBar(boolean flag) {
		if (flag == true) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
	}

	public void setProgress(int progress) {
		if (progressBar.getVisibility() == View.VISIBLE) {
			progressBar.setProgress(progress);
		}
	}

	public void setShowBtnOk(boolean isShow) {
		if (isShow == true) {
			btnOk.setVisibility(View.VISIBLE);
		} else {
			btnOk.setVisibility(View.GONE);
		}
	}

	public void setBtnOkListener(android.view.View.OnClickListener listener) {
		if (listener != null) {
			if (btnOk.getVisibility() == View.VISIBLE) {
				btnOk.setOnClickListener(listener);
			}
		}
	}

	public void setShowBtnCancel(boolean isShow) {
		if (isShow == true) {
			btnCancel.setVisibility(View.VISIBLE);
		} else {
			btnCancel.setVisibility(View.GONE);
		}
	}

	public void setBtnCancelListener(android.view.View.OnClickListener listener) {
		if (listener != null) {
			if (btnCancel.getVisibility() == View.VISIBLE) {
				btnCancel.setOnClickListener(listener);
			}
		}
	}

	private Display getDisplay() {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display;
	}
}