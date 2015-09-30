
package org.luwrain.browser;

import javafx.concurrent.Worker.State;

public interface BrowserEvents
{
    void onChangeState(State state);
    void onProgress(Number progress);
    void onAlert(String message);
    String onPrompt(String message,String value);
    void onError(String message);
    boolean onDownloadStart(String url);
    Boolean onConfirm(String message);
}
