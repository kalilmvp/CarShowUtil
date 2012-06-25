package br.com.google.android.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class AnalyticsUtils {
	private static final boolean ANALYTICS_ENABLED = true;
	private GoogleAnalyticsTracker mTracker;
	private Context mApplicationContext;
	
	/**
	 * Código para o Google Analytics do LivroAndroid
	 */
	private static final String UACODE = "UA-32846066-1";
	private static AnalyticsUtils sInstance;
	
	/**
	 * Returns the global {@link AnalyticsUtils} singleton object, creating one if necessary.
	 */
	public static AnalyticsUtils getInstance(Context context) {
		if (!ANALYTICS_ENABLED) {
			return sEmptyAnalyticsUtils;
		}
		if (sInstance == null) {
			if (context == null) {
				return sEmptyAnalyticsUtils;
			}
			sInstance = new AnalyticsUtils(context);
		}
		return sInstance;
	}
	
	private AnalyticsUtils(Context context) {
		if (context == null) {
			// This should only occur for the empty Analytics utils object.
			return;
		}
		mApplicationContext = context.getApplicationContext();
		mTracker = GoogleAnalyticsTracker.getInstance();
		// Unfortunately this needs to be synchronous.
		mTracker.startNewSession(UACODE, 300, mApplicationContext);
		Log.d(AnalyticsUtils.class.getSimpleName(), "Initializing Analytics");
	}
	
	public void trackEvent(final String category, final String action, final String label, final int value) {
		// We wrap the call in an AsyncTask since the Google Analytics library
		// writes to disk on its calling thread.
		new AsyncTask<Void, Void, Void>() {
			
			@Override
			protected Void doInBackground(Void... voids) {
				try {
					mTracker.trackEvent(category, action, label, value);
					Log.d(AnalyticsUtils.class.getSimpleName(), "Analytics trackEvent: " 
							+ category + " / " + action + " / " + label + " / " + value);
				} catch (Exception e) {
					// We don’t want to crash if there’s an Analytics library exception.
					Log.w(AnalyticsUtils.class.getSimpleName(), "Analytics trackEvent error: " 
							+ category + " / " + action + " / " + label + " / " + value, e);
				}
				return null;
			}
		}.execute();
	}
	public void trackPageView(final String path) {
		
		// We wrap the call in an AsyncTask since the Google Analytics library
		// writes to disk on its calling thread.
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... voids) {
				try {
					mTracker.trackPageView(path);
					Log.d(AnalyticsUtils.class.getSimpleName(), "Analytics trackPageView: "+ path);
				} catch (Exception e) {
					// We don’t want to crash if there’s an Analytics library
					// exception.
					Log.w(AnalyticsUtils.class.getSimpleName(),"Analytics trackPageView error: "+ path, e);
				}
				return null;
			}
		}.execute();
	}
	/**
	 * Empty instance for use when Analytics is disabled or there was no Context available.
	 */
	private static AnalyticsUtils sEmptyAnalyticsUtils = new AnalyticsUtils(null) {
		@Override
		public void trackEvent(String category, String action, String label,int value) {
		}
		@Override
		public void trackPageView(String path) {
		}
	};
}
