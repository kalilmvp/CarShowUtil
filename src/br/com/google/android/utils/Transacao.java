package br.com.google.android.utils;

public interface Transacao {
	
	public void executar() throws Exception;
	
	public void atualizarView();
}
