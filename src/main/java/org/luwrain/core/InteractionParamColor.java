package org.luwrain.core;

/* Dumb color class for using inside InteractionParam, defined as one of predefined colors or RGB values, passed directly awt/javafx color
 * predefined and rgb colors are stored different private member, color can be only one of them 
 */
public class InteractionParamColor
{
	public enum Predefined {WHITE,LIGHT_GRAY,GRAY,DARK_GRAY,BLACK,RED,PINK,ORANGE,YELLOW,GREEN,MAGENTA,CYAN,BLUE};
	Predefined predefined=null;
	float red=0,green=0,blue=0;
	
	public Predefined getPredefined(){return predefined;}
	public void setPredefined(Predefined predefined){this.predefined=predefined;}
	
	public float getRed(){return red;}
	public void setRed(float red){this.red=red;}
	public float getGreen(){return green;}
	public void setGreen(float green){this.green=green;}
	public float getBlue(){return blue;}
	public void setBlue(float blue){this.blue=blue;}
	
	public InteractionParamColor(float red,float green,float blue)
	{
		this.red=red;
		this.green=green;
		this.blue=blue;
		predefined=null;
	}
	public InteractionParamColor(Predefined predefined)
	{
		this.predefined=predefined;
	}
}
