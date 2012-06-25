package br.com.google.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import br.com.google.android.infra.R;

public class Utils {
	
	public static void createAlertDialog(final Context context, final int message) {
		try {
			montaDialog(context, context.getString(message));
		} catch (Exception e) {
			Log.e(Utils.class.getSimpleName(), e.getMessage(), e);
		}	
	}
	
	public static void createAlertDialog(final Context context, final String message) {
		try {
			montaDialog(context, message);
		} catch (Exception e) {
			Log.e(Utils.class.getSimpleName(), e.getMessage(), e);
		}	
	}

	private static void montaDialog(final Context context, final String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.app_name))
				.setMessage(message)
				.create();
		
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				return;
			}
		});
		
		alertDialog.show();
	}
	
	//verifica API >= 11
	public static boolean isAndroid3() {
		int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 11) {
			return true;
		}
		
		return false;
	}
	
	//tela large/xlarge
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	public static boolean isTabletWith3(Context context) {
		return isAndroid3() && isTablet(context);
	}
	
	public static boolean closeVirtualKeyboard(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			return b;
		}
		return false;
	}
}