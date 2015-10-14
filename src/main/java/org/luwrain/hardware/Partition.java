
package org.luwrain.hardware;

import java.io.File;

public class Partition
{
    static public final int REGULAR = 0;
    static public final int REMOVABLE = 1;
    static public final int REMOTE = 2;
    static public final int USER_HOME = 3;
    static public final int ROOT = 4;

    private int type;
    private File file;
    private String name;
    private boolean mounted = false;

    public Partition(int type, File file,
	      String name, boolean mounted)
    {
	this.type = type;
	this.file = file;
	this.name = name;
	this.mounted = mounted;
	if (file == null)
	    throw new NullPointerException("file may not be null");
	if (name == null)
	    throw new NullPointerException("name may not be null");
    }

    public int type()
    {
	return type;
    }

    public File file()
    {
	return file;
    }

public String name()
{
return name;
}

    public boolean mounted()
    {
	return mounted;
    }
}
