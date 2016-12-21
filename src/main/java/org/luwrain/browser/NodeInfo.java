
package org.luwrain.browser;

import org.w3c.dom.Node;
import java.awt.Rectangle;

public class NodeInfo
{
	final Node node;
    Integer parent = null;
    Rectangle rect;
    boolean forText;
    int hash;
    long hashTime=0;

    public NodeInfo(Node node,int x,int y,int width,int height,boolean forText)
    {
	this.node = node;
	this.forText=forText;
	rect=new Rectangle(x,y,width,height);
    }
    
    public Node getNode()
    {
    	return node;
    }
    
    public Integer getParent()
    {
    	return parent;
    }
    
    public void setParent(int val)
    {
    	parent=val;
    }

    public boolean isVisible()
    {
	return rect.width > 0 && rect.height > 0;
    }
    
    public void calcHash(String text)
    {
	hash=text.hashCode();
	hashTime=new java.util.Date().getTime();
    }

    public String descr()
    {
    	String str=node.getNodeValue();
    	if(str==null) str="null";
		return node.getNodeName()+ "\tp:"+parent+" "+node.getClass().getSimpleName() + "\t" + str.substring(0,Math.min(160,str.length()))+"'";
	//return node.getNodeName() + " " + node.getNodeValue();
    }

	public boolean getForText()
	{
		return forText;
	}

	public Rectangle getRect()
	{
		return rect;
	}

	public long getHashTime()
	{
		return hashTime;
	}

	public int getHash()
	{
		return hash;
	}
}
