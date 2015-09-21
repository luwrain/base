package org.luwrain.browser;

import javafx.concurrent.Worker.State;

public interface BrowserEvents
{
	public void onChangeState(State state);
	public void onProgress(Number progress);
	public void onAlert(String message);
	public String onPrompt(String message,String value);
	public void onError(String message);
	public boolean onDownloadStart(String url);
}
