
package org.luwrain.browser;

//import org.luwrain.browser.ElementList.SplittedLine;

public interface ElementList
{
    public interface Selector
    {
	boolean check(ElementList wel);
	boolean first(ElementList wel);
	boolean next(ElementList wel);
	boolean prev(ElementList wel);
	boolean to(ElementList wel,int pos);
    }

    public interface SelectorALL extends Selector
    {
	boolean isVisible();
	void setVisible(boolean visible);
	boolean checkVisible(ElementList wel);
	//	boolean check(ElementList wel);
    }

    public interface SelectorTEXT extends Selector
    {
	String getFilter();
	void setFilter(String filter);
	//	boolean check(ElementList wel);
    }

    public interface SelectorCSS extends Selector
    {
	String getTagName();
	void setTagName(String tagName);
	String getStyleName();
	void setStyleName(String styleName);
	String getStyleValue();
	void setStyleValue(String styleValue);

	//	boolean check(ElementList wel);
    }

    public static interface SelectorTAG extends SelectorALL
    {
	String getTagName();
	void setTagName(String tagName);
	String getAttrName();
	void setAttrName(String attrName);
	String getAttrValue();
	void setAttrValue(String attrValue);

	boolean check(ElementList wel);
    }

    int getPos();
    String getType();
    String getText();
    String getComputedText();
    String getLink();
    boolean isEditable();
    void setText(String text);
    String getAttributeProperty(String name);
    String getComputedStyleProperty(final String name);
    String getComputedStyleAll();

    void clickEmulate();
}
