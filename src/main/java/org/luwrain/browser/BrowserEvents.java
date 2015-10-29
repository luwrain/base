
package org.luwrain.browser;

public interface BrowserEvents
{
	public enum WebState {CANCELLED,FAILED,READY,RUNNING,SCHEDULED,SUCCEEDED};
    void onChangeState(WebState state);
    void onProgress(Number progress);
    void onAlert(String message);
    String onPrompt(String message,String value);
    void onError(String message);
    boolean onDownloadStart(String url);
    Boolean onConfirm(String message);
}
