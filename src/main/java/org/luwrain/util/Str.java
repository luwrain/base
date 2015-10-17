
package org.luwrain.util;

public class Str
{
    static public String removeIsoControlChars(String value)
    {
	final StringBuilder b = new StringBuilder();
	for(int i = 0;i < value.length();++i)
	    if (!Character.isISOControl(value.charAt(i)))
		b.append(value.charAt(i));
	return b.toString();
    }

    static public String replaceIsoControlChars(String value, char replaceWith)
    {
	final StringBuilder b = new StringBuilder();
	for(int i = 0;i < value.length();++i)
	    if (!Character.isISOControl(value.charAt(i)))
		b.append(value.charAt(i)); else
		b.append(replaceWith);
	return b.toString();
    }

    static public String replaceIsoControlChars(String value)
    {
	return replaceIsoControlChars(value, ' ');
    }
}
