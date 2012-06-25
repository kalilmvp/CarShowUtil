package br.com.google.android.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import android.util.Log;

public class IOUtils {
	private static final String TAG = "IOUtils";
	
	public static String getData(InputStream in, String charset) throws UnsupportedEncodingException {
		
		byte[] bytes = getBytes(in);
		
		return new String(bytes, charset);
	}

	private static byte[] getBytes(InputStream in) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			byte[] buffer = new byte[1024];
			int length = 0;
			
			while ((length = in.read(buffer)) > 0) {
				bos.write(buffer, 0, length);
			}
			
			return bos.toByteArray();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		} finally {
			try {
				bos.close();
				in.close();
			} catch (IOException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
		return null;
	}
}