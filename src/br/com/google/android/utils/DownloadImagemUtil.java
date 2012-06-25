package br.com.google.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Faz o download das imagens de um Adapter em uma Thread.
 * 
 * Utilizado para exibir o ListView antes de terminar de fazer download das
 * imagens
 * 
 * @author rlecheta
 * 
 */
public class DownloadImagemUtil {
	public static final boolean LOG_ON = false;
	private static final String TAG = "DownloadImagemUtil";
	// Cache de imagens em memória
	private HashMap<String, Bitmap> cache = new HashMap<String, Bitmap>();
	private File cacheDir;
	private ThreadDownload threadDownload = new ThreadDownload();
	private static int TAMANHO_IMAGEM = 100;
	private final int tamanhoImagem;
	private FilaDownload fila = new FilaDownload();
	
	public DownloadImagemUtil(Context context) {
		this(context, TAMANHO_IMAGEM);
	}
	
	public DownloadImagemUtil(Context context, int tamanhoImagem) {
		this.tamanhoImagem = tamanhoImagem;
		
		// Deixa a thread que controla o download com prioridade baixa
		threadDownload.setPriority(Thread.NORM_PRIORITY - 1);
		
		// Cria a pasta "CacheImagens" dentro do sdcard para salvar imagens em disco
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "CacheImagens");
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			// Se a pasta não existe no sdcard, cria
			cacheDir.mkdirs();
		}
	}
	// Faz o download da imagem para a URL Fornecida
	// O ProgressBar é utilizado para a animação, e o ImageView será atualizado
	// automaticamente depois do download
	public void download(Activity activity, String url, ImageView imageView, ProgressBar progress) {
		if (cache.containsKey(url)) {
			Bitmap bitmap = getBitmap(url);
			imageView.setVisibility(View.VISIBLE);
			progress.setVisibility(View.INVISIBLE);
			imageView.setImageBitmap(bitmap);
		} else {
			// Tag para identificar o ImageView
			imageView.setTag(url);
			preparaDownloadImagem(url, activity, imageView, progress);
			imageView.setVisibility(View.INVISIBLE);
			if (progress != null) {
				progress.setVisibility(View.VISIBLE);
			}
		}
	}
	// Insere a URL de download na fila para ser processada pela thread
	private void preparaDownloadImagem(String url, Activity activity,ImageView imageView, ProgressBar progress) {
		fila.zerar(imageView);
		ImagemDownload p = new ImagemDownload(url, imageView, progress);
		synchronized (fila.imgs) {
			// Insere uma nova imagem para ser processada
			fila.imgs.push(p);
			fila.imgs.notifyAll();
		}
		if (threadDownload.getState() == Thread.State.NEW) {
			threadDownload.start();
		}
	}
	// Busca a imagem no cache ou em disco
	public Bitmap getBitmap(String url) {
		if (url == null) {
			return null;
		}
		Bitmap bitmap = cache.get(url);
		if (bitmap == null) {
			String filename = String.valueOf(url.hashCode());
			File f = new File(cacheDir, filename);
			// Busca imagem no SDCard
			Bitmap b = getBitmap(f);
			if (b != null) {
				return b;
			}
		}
		return bitmap;
	}
	
	// Transforma um File em um Bitmap
	private Bitmap getBitmap(File f) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			int larguraTmp = o.outWidth, height_tmp = o.outHeight;
			while (true) {
				if (larguraTmp / 2 < tamanhoImagem || height_tmp / 2 < tamanhoImagem)
					break;
				larguraTmp /= 2;
				height_tmp /= 2;
			}
			// Cria o bitmap
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f),null, o2);
			return bitmap;
		} catch (FileNotFoundException e) {
		}
		return null;
	}
	// Faz o download da imagem e atualiza o cache
	private Bitmap downloadBitmap(String url) {
		if (url == null) {
			return null;
		}
		String arquivo = String.valueOf(url.hashCode());
		File f = new File(cacheDir, arquivo);
		// Busca imagem no SDCard
		Bitmap b = getBitmap(f);
		if (b != null) {
			return b;
		}
		// Busca imagem na Web
		try {
			InputStream is = new URL(url).openStream();
			OutputStream os = new FileOutputStream(f);
			this.copiaStream(is, os);
			os.close();
			Bitmap bitmap = getBitmap(f);
			return bitmap;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}
	}
	// Método utilitário para copiar streams
	private void copiaStream(InputStream is, OutputStream os) {
		final int tamanho = 1024;
		try {
			byte[] bytes = new byte[tamanho];
			for (;;) {
				int count = is.read(bytes, 0, tamanho);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	private class ImagemDownload {
		public String url;
		public ImageView imageView;
		public ProgressBar progress;
		
		public ImagemDownload(String u, ImageView i, ProgressBar progress) {
			this.url = u;
			this.imageView = i;
			this.progress = progress;
		}
	}
	// Fila para download
	class FilaDownload {
		private Stack<ImagemDownload> imgs = new Stack<ImagemDownload>();
		// Remove as duplicatas deste ImageView
		public void zerar(ImageView image) {
			for (int j = 0; j < imgs.size();) {
				if (imgs.get(j).imageView == image) {
					imgs.remove(j);
				} else {
					++j;
				}
			}
		}
	}
	// Thread que faz o download das imagens
	class ThreadDownload extends Thread {
		public void run() {
			try {
				while (true) {
					// Aguarda e Sincroniza...
					if (fila.imgs.size() == 0)
						synchronized (fila.imgs) {
							fila.imgs.wait();
						}
					if (fila.imgs.size() != 0) {
						final ImagemDownload img;
						synchronized (fila.imgs) {
							// Recebe imagem para fazer download
							img = fila.imgs.pop();
						}
						// Faz Download
						final Bitmap bitmap = downloadBitmap(img.url);
						// Faz Cache
						cache.put(img.url, bitmap);
						// TAG == URL da imagem
						Object tag = img.imageView.getTag();
						if (tag != null && tag.toString().equals(img.url)) {
							Activity a = (Activity) img.imageView.getContext();
							// Atualiza a imagem na Thread principal
							a.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (bitmap != null) {
										img.imageView.setImageBitmap(bitmap);
										img.imageView.setVisibility(View.VISIBLE);
										if (img.progress != null) {
											img.progress.setVisibility(View.INVISIBLE);
										}
									}
								}
							});
						}
					}
					if (Thread.interrupted())
					{
						break;
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}
	public void limparCache() {
		// limpa memória
		cache.clear();
		// limpa do sd-card
		File[] files = cacheDir.listFiles();
		for (File f : files)
		{
			f.delete();
		}
	}
	/**
	 * Retorna se é Android 3.0 ou superior (API Level 11)
	 * 
	 * @return
	 */
	public static boolean isAndroid3() {
		int apiLevel = Integer.parseInt(Build.VERSION.SDK);
		if (apiLevel >= 11) {
			return true;
		}
		return false;
	}
	
	// Pára a thread
	public void stopThread() {
		threadDownload.interrupt();
	}
}
