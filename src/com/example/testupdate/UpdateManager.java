package com.example.testupdate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class UpdateManager {
	private final String URL = "http://10.1.1.121:8080/VersionUpdate/servlet/VersionUpdate";// 检查更新的服务器地址
	private final String SAVE_PATH = Environment.getExternalStorageDirectory()
			+ "/test/";// 下载APK的地址
	private final String SAVE_NAME = SAVE_PATH + "TestUpdate.apk";// 下载的名称
	private final int NO_EDITION = 0;// 不需要更新
	private final int FORCE_UPDATE = 1;// 强制更新
	private final int NOFORCE_UPDATE = 2;// 非强制更新
	private final int ERROR = 3;// 出现错误
	private final int DOWN_OVER = 4;// 下载完成
	private final int DOWN_PROGRESS = 5;// 下载过程中更新下载了多少

	private int progress = 0;// 下载了多少
	private CommonDialog dialog;
	private Context mContext;
	String localVersionName = "";
	private UpdateInfo info;
	private boolean isshow = false;// 如果是后台自动检查，检查失败不弹出窗口
	private boolean stopdownload = false;// 如果为点击取消，就停止下载

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg != null) {
				switch (msg.what) {
				case DOWN_OVER:// 下载完成
					installApk();
					break;
				case DOWN_PROGRESS:// 下载中更新进度条
					if (dialog != null) {
						dialog.setProgressBar(true);
						dialog.setProgress(progress);
						dialog.setShowBtnOk(false);
						dialog.setShowBtnCancel(true);
						dialog.setBtnCancelListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								dialog.dismiss();
							}
						});
					}
					break;
				case ERROR:// 出现错误
					if (dialog != null) {
						if (isshow == true) {
							dialog.setProgressBar(false);
							dialog.setMsg(mContext.getResources().getString(
									R.string.dialog_msg_error));
							dialog.setShowBtnOk(true);
							dialog.setShowBtnCancel(false);
							dialog.setBtnOkListener(new OnClickListener() {

								@Override
								public void onClick(View view) {
									dialog.dismiss();
								}
							});
						}
					}
					break;
				case NO_EDITION:// 没有新版本
					if (dialog != null) {
						dialog.setProgressBar(false);
						dialog.setMsg(mContext.getResources().getString(
								R.string.dialog_msg_isnew));
						dialog.setShowBtnOk(true);
						dialog.setShowBtnCancel(false);
						dialog.setBtnOkListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								dialog.dismiss();
							}
						});
					}
				case FORCE_UPDATE:// 强制更新
					if (dialog != null) {
						dialog.setProgressBar(false);
						dialog.setMsg(mContext.getResources().getString(
								R.string.dialog_msg_havenewversion));
						dialog.setShowBtnOk(true);
						dialog.setShowBtnCancel(false);
						dialog.setBtnOkListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								dialog.setProgressBar(true);
								dialog.setMsg(mContext
										.getResources()
										.getString(
												R.string.dialog_msg_havenewversion));
								dialog.setShowBtnOk(false);
								dialog.setShowBtnCancel(false);
								download(info.getUrl());
							}
						});
						dialog.setBtnCancelListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								dialog.dismiss();
							}
						});
					}
					break;
				case NOFORCE_UPDATE:// 非强制更新
					if (dialog != null) {
						dialog.setProgressBar(false);
						dialog.setMsg(mContext.getResources().getString(
								R.string.dialog_msg_havenewversion));
						dialog.setShowBtnOk(true);
						dialog.setShowBtnCancel(true);
						dialog.setBtnOkListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								dialog.setProgressBar(true);
								dialog.setMsg(mContext
										.getResources()
										.getString(
												R.string.dialog_msg_havenewversion));
								download(info.getUrl());
								stopdownload = true;
							}
						});
						dialog.setBtnCancelListener(new OnClickListener() {

							@Override
							public void onClick(View view) {
								dialog.dismiss();
							}
						});
					}
					break;
				}
			}
		};
	};

	private void download(final String urls) {
		new Thread() {
			public void run() {
				super.run();
				DownLoadEntity entity = HttpUtils.getApk(urls);
				if (entity == null) {
					handler.sendEmptyMessage(ERROR);
					return;
				}
				try {
					File file = new File(SAVE_PATH);
					if (!file.exists()) {
						file.mkdirs();
					}
					File fileApk = new File(SAVE_NAME);
					InputStream is = entity.getIs();
					FileOutputStream fos = new FileOutputStream(fileApk);
					int count = 0;
					byte[] by = new byte[1024];
					do {
						int numread = is.read(by);
						count += numread;
						progress = (int) ((float) count / entity.getLength()) * 1000;
						handler.sendEmptyMessage(DOWN_PROGRESS);
						if (numread < 0) {
							handler.sendEmptyMessage(DOWN_OVER);
							break;
						}
						fos.write(by, 0, numread);
					} while (stopdownload == true);
					fos.close();
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	/** 参数表示是否显示自动检查的progressbar */
	public void checkUpdates(boolean showProgress) {
		isshow = showProgress;
		dialog = new CommonDialog(mContext);
		dialog.setMsg(mContext.getResources().getString(
				R.string.dialog_msg_checkversion));
		dialog.setProgressBar(showProgress);
		dialog.show();
		localVersionName = getLocalVersionName();
		new Thread(new CheckUpdate()).start();
	}

	private class CheckUpdate implements Runnable {

		@Override
		public void run() {
			info = JsonParser.getInfo(HttpUtils.getVersion(URL));
			if (info == null) {
				handler.sendEmptyMessage(ERROR);
				return;
			}
			if (!info.getVersion().equals(localVersionName)) {
				if (info.isIsforce() == true) {// 强制更新
					handler.sendEmptyMessage(FORCE_UPDATE);
				} else {// 非强制更新
					handler.sendEmptyMessage(NOFORCE_UPDATE);
				}
			} else {
				handler.sendEmptyMessage(NO_EDITION);
			}
		}
	}

	private String getLocalVersionName() {
		try {
			PackageManager packageManager = mContext.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					mContext.getPackageName(), 0);
			if (packageInfo != null) {
				return packageInfo.versionName;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void installApk() {
		File file = new File(SAVE_NAME);
		if (!file.exists()) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + file.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}
}