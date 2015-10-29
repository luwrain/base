
package org.luwrain.browser;

import org.luwrain.browser.ElementList.*;
import org.luwrain.core.Interaction;

public interface Browser
{
    Browser setInteraction(Interaction interaction);
    void Remove();

    boolean isBusy();

    void setVisibility(boolean enable);
    boolean getVisibility();

    void RescanDOM();
    void load(String link);
    void loadContent(String text);
    void stop();

    String getBrowserTitle();
    String getTitle();
    String getUrl();

    Object executeScript(String script);

    SelectorALL selectorALL(boolean visible);
    SelectorTEXT selectorTEXT(boolean visible,String filter);
    SelectorTAG selectorTAG(boolean visible,String tagName,String attrName,String attrValue);
    SelectorCSS selectorCSS(boolean visible,String tagName,String styleName,String styleValue);
    ElementList elementList();
}
