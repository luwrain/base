package org.luwrain.os;

import org.luwrain.core.EventConsumer;

import javafx.scene.input.KeyEvent;

public interface Keyboard
{
	void setEventConsumer(EventConsumer eventConsumer);

	void onKeyPressed(Object event);
	void onKeyReleased(KeyEvent event);
	void onKeyTyped(KeyEvent event);
}
