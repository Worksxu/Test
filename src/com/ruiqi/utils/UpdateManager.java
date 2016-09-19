package com.ruiqi.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;






import com.ruiqi.works.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName: UpdateManager
 * @Description: 版本更新管理
 * @version: 1.0
 * @Create: 2015-5-5 14:23
 */
@SuppressLint("HandlerLeak")
public class UpdateManager {
	private static Context mContext;

	// 提示语

	// 返回的安装包url

	private static Dialog noticeDialog;

	// private Dialog downloadDialog;
	/* 下载包安装路径 */
	private static String savePath = "";

	private static String saveFileName = savePath + "songqigong.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;

	private static final int DOWN_UPDATE = 1;
	private static final int TEST = 3;
	private static final int DOWN_OVER = 2;
	private static final int ERROR = -1;

	// private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;
	private String serverVesion = null;
	private String mdetial = null;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				// mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				// downLoadThread.stop();
//				showUpgradeNotifyCustom(serverVesion);
				// Intent intent = new
				// Intent(mContext,MineUpgradeActivity.class);
				// mContext.startActivity(intent);
				break;
			case TEST: {
				// Intent intent = new Intent(this,MainActivity.class);
				// startActivity(intent);
				showUpgradeNotifyCustom(serverVesion);
			}
				break;
			case ERROR:
//				EventHandler.showToast(mContext, "网络异常，出错了哦！");
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 
	 * @param force
	 *            是否需要强制更新
	 * @param serverVersionName
	 */
	public void checkUpdateInfo(boolean force, String serverVersionName,
			String detial) {
		 showNoticeDialog(serverVersionName);
		// showUpgradeNotifyCustom(serverVersionName);
		// 强制更新
		serverVesion = serverVersionName;
//		showDownloadDialog(force);
		mdetial = detial;
		// mHandler.sendEmptyMessage(TEST);
	}

	/**
	 * 
	 * @param serverVersionName
	 * @Title: showNoticeDialog
	 * @Description: 检测有新版本，提示更新窗口
	 * @date 2015-5-5 14:23
	 */
	private void showNoticeDialog(String serverVersionName) {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setMessage("发现新版本" + serverVersionName);
		builder.setPositiveButton("更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				 showDownloadDialog(true);
//				installApk();
			}
		});
		builder.setNegativeButton("忽略", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		noticeDialog = builder.create();
		noticeDialog.show();
	}

	/**
	 * 
	 * @param serverVersion
	 * @Title showUpgradeNotifyCustom
	 * @Description 自定义升级通知界面
	 * @date 2015-7-10
	 */
	private void showUpgradeNotifyCustom(String serverVersion) {
		try {
			final Dialog dlg = new Dialog(mContext, R.style.DialogCustomCenter);
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout layout = (LinearLayout) inflater.inflate(
					R.layout.activity_upgrade, null);

			final RelativeLayout comfirm = (RelativeLayout) layout
					.findViewById(R.id.activity_mime_upgrade_commit_rl);
			// final ImageView image1 = (ImageView)
			// layout.findViewById(R.id.activity_qcall_cancle_text);
			TextView mCancleTextView = (TextView) layout
					.findViewById(R.id.activity_mime_upgrade_next_tv);
			TextView titleTextView = (TextView) layout
					.findViewById(R.id.tv_title);
			titleTextView.setText("系统升级");

			TextView oldVersionTextView = (TextView) layout
					.findViewById(R.id.activity_mime_upgrade_info_name_tv);
			TextView newVersionTextView = (TextView) layout
					.findViewById(R.id.activity_mime_upgrade_info_name_new_tv);
			TextView detialTextView = (TextView) layout
					.findViewById(R.id.activity_mime_upgrade_tv);

			String pkName = mContext.getPackageName();
			String versionName;
			versionName = mContext.getPackageManager()
					.getPackageInfo(pkName, 0).versionName;
			oldVersionTextView.setText("V" + versionName);

			newVersionTextView.setText("V" + serverVesion);
			detialTextView.setText(mdetial);

//			ImageView left = (ImageView) layout
//					.findViewById(R.id.iv_title_left);
//			left.setVisibility(View.INVISIBLE);// 改

			mCancleTextView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dlg.dismiss();
//					Consts.isUpdrade = true;// 改动
				}
			});

			// set a large value put it in bottom
			comfirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dlg.dismiss();
//					Consts.isUpdrade = true; 改动
					// showDownloadDialog(true);
					installApk();
				}
			});

			Window dialogWindow = dlg.getWindow();
			// 设置位置
			dialogWindow.setGravity(Gravity.CENTER);
			// 设置dialog的宽高属性
			// dialogWindow.getDecorView().setPadding(20, 0, 20, 0);
			dlg.setContentView(layout);

			dialogWindow.setLayout(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
			dlg.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: showDownloadDialog
	 * @Description: 显示下载窗口（含进度条）
	 * @date 2015-5-5 14:23
	 */
	private void showDownloadDialog(final boolean force) {
		 AlertDialog.Builder builder = new Builder(mContext);
		 final LayoutInflater inflater = LayoutInflater.from(mContext);
//		 View v = inflater.inflate(R.layout.update_progress, null);
//		 mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
//		 TextView tv = (TextView) v.findViewById(R.id.update_tv_cancle);

//		 builder.setView(v);
//		 builder.setTitle("版本更新中，请稍后……");
		/*
		 * builder.setNegativeButton(R.string.update_cancel, new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss(); interceptFlag = true; } });
		 */
		// downloadDialog = builder.create();
		// downloadDialog.setCancelable(false);
		/*
		 * downloadDialog.setCancelable(true);
		 * downloadDialog.setOnCancelListener(new OnCancelListener() { public
		 * void onCancel(DialogInterface arg0) { if (force)
		 * {//强制更新情况下，点取消直接退出整个应用。 ViewManagerUtil.getInstance().exit();
		 * downloadDialog.dismiss(); } } });
		 */

		// downloadDialog.show();
		downloadApk();
	}

	/***
	 * 下载APK进程
	 */
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			Log.e("dsfdsf", "进下载了");
			try {
				//http://cztest.ruiqi100.com/statics/upload/worksxu.apk
				URL url = new URL("http://cztest.ruiqi100.com/statics/upload/Worksxu.apk");
//			    Intent intent = new Intent();  
//			    intent.setAction("android.intent.action.VIEW");  
//			    Uri content_url = Uri.parse("http://cztest.ruiqi100.com/statics/upload/Worksxu.apk");  
//			    intent.setData(content_url);  
//			    mContext.startActivity(intent);  
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestProperty("Accept-Encoding", "identity");
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				if ((Environment.getExternalStorageState() != Environment.MEDIA_REMOVED)
						|| (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED)) {
					savePath = Environment.getExternalStorageDirectory()
							.getPath() + savePath;
//					LogUtil.log("updateManager", "savapath:" + savePath);
				} 
//					LogUtil.log("updateManager",
//							"there isn't externalStorage ,savapath:" + savePath);

			File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}

				saveFileName = savePath + File.separator + "songqigong.apk";
				Log.e("llllll_lujing", saveFileName);
				File ApkFile = new File(saveFileName);
				if (!ApkFile.exists()) {
					// ApkFile.createTempFile(prefix, suffix, directory)
					ApkFile.createNewFile();
					// ApkFile.createTempFile("xiaoqfang", "apk", file);
//					LogUtil.log("updateManager", "apkFile is not exists!");
				}
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					Log.e("sadsadsad", numread+"");
					count += numread;
					// progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread < 0) {
						// 下载完成通知安装
						Log.e("是否完成了", "是否完成");
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
//				LogUtil.logError(e);
				// downloadDialog.dismiss();
				interceptFlag = true;
				mHandler.sendEmptyMessage(ERROR);
			} catch (IOException e) {
//				LogUtil.logError(e);/
				// downloadDialog.dismiss();
				interceptFlag = true;
				mHandler.sendEmptyMessage(ERROR);
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		Log.e("fasfasfa", "safsafsa");
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private static void installApk() {
		// downloadDialog.dismiss();
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}
}
