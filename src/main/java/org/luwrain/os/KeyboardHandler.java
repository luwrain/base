
package org.luwrain.os;

import org.luwrain.core.EventConsumer;

public interface KeyboardHandler
{
    void setEventConsumer(EventConsumer consumer);
    void onKeyPressed(Object event);
    void onKeyReleased(Object event);
    void onKeyTyped(Object event);
}
