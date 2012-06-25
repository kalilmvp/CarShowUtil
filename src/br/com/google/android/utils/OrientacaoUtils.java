package br.com.google.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

public class OrientacaoUtils {
	
	public static int getOrientacao(Context context) {
		return context.getResources().getConfiguration().orientation;
	}
	
	public static boolean isHorizontal(Context context) {
		return getOrientacao(context) == Configuration.ORIENTATION_LANDSCAPE;
	}
	
	public static boolean isVertical(Context context) {
		return getOrientacao(context) == Configuration.ORIENTATION_PORTRAIT;
	}
	
	public static void setOrientationHorizontal(Activity context) {
		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	public static void setOrientationVertical(Activity context) {
		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	public static void setOrientationUnspecified(Activity context) {
		context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}
}