
package org.luwrain.browser;

import org.luwrain.browser.ElementList.SplitedLine;

public interface ElementList
{
    public static abstract interface Selector
    {
	boolean check(ElementList wel);
	boolean first(ElementList wel);
	boolean next(ElementList wel);
	boolean prev(ElementList wel);
	boolean to(ElementList wel,int pos);
    }

    public static interface SelectorALL extends Selector
    {
	boolean isVisible();
	void setVisible(boolean visible);
	boolean checkVisible(ElementList wel);
	boolean check(ElementList wel);
    }

    public static interface SelectorTEXT extends Selector
    {
	String getFilter();
	void setFilter(String filter);
	boolean check(ElementList wel);
    }

    public static interface SelectorCSS extends Selector
    {
	String getTagName();
	void setTagName(String tagName);
	String getStyleName();
	void setStyleName(String styleName);
	String getStyleValue();
	void setStyleValue(String styleValue);

	boolean check(ElementList wel);
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
	
	static public class SplitedLine
	{
		public String type;
		public String text;
		public int pos; // element position in domidx
		public int index; // line index in global line count
		public SplitedLine(String type,String text,int pos,int index)
		{
			this.type=type;
			this.text=text;
			this.pos=pos;
			this.index=index;
		}
	};

	SplitedLine[][] getSplitedLines();
	int getSplitedCount();
	void splitAllElementsTextToLines(int width,Selector selector);
	SplitedLine getSplitedLineByIndex(int index);
	SplitedLine[] getSplitedLineByPos(int pos);
}
