package org.luwrain.browser;

public interface ElementList
{
	public static abstract interface Selector
	{
		abstract boolean check(ElementList wel);
		public boolean first(ElementList wel);
		public boolean next(ElementList wel);
		public boolean prev(ElementList wel);
	}
	public static interface SelectorALL extends Selector
	{
		public boolean isVisible();
		public void setVisible(boolean visible);
		public boolean checkVisible(ElementList wel);
		boolean check(ElementList wel);
	}
	public static interface SelectorTEXT extends Selector
	{
		public String getFilter();
		public void setFilter(String filter);
		boolean check(ElementList wel);
	}
	public static interface SelectorCSS extends Selector
	{
		public String getTagName();
		public void setTagName(String tagName);
		public String getStyleName();
		public void setStyleName(String styleName);
		public String getStyleValue();
		public void setStyleValue(String styleValue);
		
		boolean check(ElementList wel);
	}
	public static interface SelectorTAG extends SelectorALL
	{
		public String getTagName();
		public void setTagName(String tagName);
		public String getAttrName();
		public void setAttrName(String attrName);
		public String getAttrValue();
		public void setAttrValue(String attrValue);

		boolean check(ElementList wel);
	}
	
	public String getType();
	public String getText();
	public String getComputedText();
	public String getLink();
	public boolean isEditable();
	public void setText(String text);
	public String getAttributeProperty(String name);
	public String getComputedStyleProperty(final String name);
	public String getComputedStyleAll();
	
	public void clickEmulate();
	
}
