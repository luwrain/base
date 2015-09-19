package org.luwrain.browser;

import java.util.Vector;

import javafx.concurrent.Worker.State;

public interface BrowserEvents
{
	public enum EventType {ALERT,PROMPT,ERROR};
	static public class Event
	{
		public EventType type;
		public String message;
		public String value;
		public Object result;
	
	}
	public Vector<Event> events=new Vector<Event>();
	
	public void onChangeState(State state);
	public void onProgress(Number progress);
	public void onAlert(String message);
	public String onPrompt(String message,String value);
	public void onError(String message);
}
