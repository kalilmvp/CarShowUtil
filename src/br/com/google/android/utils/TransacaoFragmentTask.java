package br.com.google.android.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class TransacaoFragmentTask extends AsyncTask<Void, Void, Boolean>{
	
	private final Context context;
	private final Fragment fragment;
	private final Transacao transacao;
	private View view;
	private Throwable exception;
	private int progressId;
	
	public TransacaoFragmentTask(Fragment fragment, Transacao transacao, int progressId) {
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.transacao = transacao;
		this.progressId = progressId;
		this.view = this.fragment.getView();
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		try {
			this.abrirProgress();
		} catch (Exception e) {
			Log.e(TransacaoFragmentTask.class.getSimpleName(), e.getMessage(), e);
		}
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			this.transacao.executar();
		} catch (Throwable e) {
			Log.e(TransacaoFragmentTask.class.getSimpleName(), e.getMessage(), e);
			this.exception = e;
			return false;
		} finally {
			try {
				this.fragment.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						fecharProgress();
					}
				});
			} catch (Exception e) {
				Log.e(TransacaoFragmentTask.class.getSimpleName(), e.getMessage(), e);
			}
		}
		return true;
	}
	
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			this.transacao.atualizarView(); 
		} else {
			Utils.createAlertDialog(this.context, "Erro: " + this.exception.getMessage());
		}
	}
	
	private void abrirProgress() {
		
		if (this.view != null) {
			final ProgressBar progressBar = (ProgressBar) view.findViewById(this.progressId);
			if (progressBar != null) {
				progressBar.setVisibility(View.VISIBLE);
			}
//			OrientacaoUtils.setOrientationVertical((Activity)this.context);
		}
	}
	
	public void fecharProgress() {
		if (this.view != null) {
			final ProgressBar progressBar = (ProgressBar) view.findViewById(this.progressId);
			if (progressBar != null) {
				progressBar.setVisibility(View.GONE);
			}
//			OrientacaoUtils.setOrientationUnspecified((Activity)this.context);
		}
	}
}