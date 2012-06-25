package br.com.google.android.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class TransacaoTask extends AsyncTask<Void, Void, Boolean>{
	
	private final Context context;
	private final Transacao transacao;
	private ProgressDialog progressDialog;
	private Throwable exception;
	private int aguardeMsg;
	
	public TransacaoTask(Context context, Transacao transacao, int aguardeMsg) {
		this.context = context;
		this.transacao = transacao;
		this.aguardeMsg = aguardeMsg;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		this.abrirProgress();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			this.transacao.executar();
		} catch (Throwable e) {
			Log.e(TransacaoTask.class.getSimpleName(), e.getMessage(), e);
			this.exception = e;
			return false;
		} finally {
			this.fecharProgress();
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
		this.progressDialog = ProgressDialog.show(this.context, "", this.context.getString(this.aguardeMsg));
		
		OrientacaoUtils.setOrientationVertical((Activity)this.context);
	}
	
	public void fecharProgress() {
		if (this.progressDialog != null) {
			this.progressDialog.dismiss();
			OrientacaoUtils.setOrientationUnspecified((Activity)this.context);
		}
	}
}