
package org.luwrain.core;

import java.util.*;

public class CmdLineUtils
{
    private String[] cmdLine;

    public CmdLineUtils(String[] cmdLine)
    {
	NullCheck.notNullItems(cmdLine, "cmdLine");
    }

    public boolean used(String option)
    {
	NullCheck.notNull(option, "option");
	for(String s: cmdLine)
	    if (s.equals(option))
		return true;
	return false;
    }

    public String getFirstArg(String prefix)
    {
	NullCheck.notNull(prefix, "prefix");
	for(String s: cmdLine)
	{
	    if (s.length() < prefix.length() || !s.startsWith(prefix))
		continue;
	    return s.substring(prefix.length());
	}
	return null;
    }

    public String[] getArgs(String prefix)
    {
	final LinkedList<String> res = new LinkedList<String>();
	NullCheck.notNull(prefix, "prefix");
	for(String s: cmdLine)
	{
	    if (s.length() < prefix.length() || !s.startsWith(prefix))
		continue;
	    res.add(s.substring(prefix.length()));
	}
	return res.toArray(new String[res.size()]);
    }
}
