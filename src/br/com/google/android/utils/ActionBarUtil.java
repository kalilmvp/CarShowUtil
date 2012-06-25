package br.com.google.android.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import br.com.google.android.infra.R;

public class ActionBarUtil {
	
	public static void setBackgroudImage(Activity activity) {
		if (Utils.isAndroid3()) {
			ActionBar actionBar = activity.getActionBar();
			if (actionBar != null) {
				Resources resources = activity.getResources();
				actionBar.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_header));
			}
		}
	}
	
	public static void setActionBarTitle(Activity activity, String title) {
		if (Utils.isAndroid3()) {
			ActionBar actionBar = activity.getActionBar();
			if (actionBar != null) {
				actionBar.setTitle(title);
			}
		}
	}
}