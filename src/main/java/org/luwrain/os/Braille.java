
package org.luwrain.os;

import org.luwrain.core.*;

public interface Braille
{
    InitResult init(EventConsumer eventConsumer);
    String getDriverName();
    int getDisplayWidth();
    int getDisplayHeight();
    void writeText(String text);
}
