/*
   Copyright 2012-2018 Michael Pozhidaev <michael.pozhidaev@gmail.com>

   This file is part of LUWRAIN.

   LUWRAIN is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   LUWRAIN is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.core;

import java.util.*;
import java.io.*;
import java.net.*;
//import java.lang.reflect.Field;
//import java.nio.charset.Charset;

public final class Init
{
    static public final String LOG_COMPONENT = "init";
        static private final File DEBUG_FILE = new File(new File(System.getProperty("user.home")), "luwrain-debug.txt");

    static public void main(String[] args) throws IOException
    {
	//	org.luwrain.app.console.App.installListener();
	if (DEBUG_FILE.exists() && !DEBUG_FILE.isDirectory())
	{
	    final PrintStream log = new PrintStream(new BufferedOutputStream(new FileOutputStream(DEBUG_FILE)), true);
	    System.setOut(log);
	    System.setErr(log);
	} //else
	//	    Log.enableBriefMode();
	//	System.out.println(GREETING);
	//	System.out.println();
	//	setUtf8();

	    final List<URL> urls = new LinkedList();


	    addJarsToClassPath(new File("jar"), urls);
	    System.out.println("" + urls.size());

	    addJarsToClassPath(new File("lib"), urls);
	    	    final ClassLoader classLoader = new URLClassLoader(urls.toArray(new java.net.URL[urls.size()]), ClassLoader.getSystemClassLoader());

	    final File dataDir = new File("data");
	    final LaunchFactory factory;
	    try {
		final Object obj = Class.forName("org.luwrain.core.LaunchFactoryImpl", true, classLoader).newInstance();
	    factory = (LaunchFactory)obj;
	    }
	    catch(Throwable e)
	    {
		e.printStackTrace();
		System.exit(1);
		return;
	    }
	    factory.newLaunch(args, dataDir).run();
    }

    /*
    static private void addExtensionsJarsToClassPath(File extensionsDir)
    {
	NullCheck.notNull(extensionsDir, "extensionsDir");
	final File[] subdirs = extensionsDir.listFiles();
	if (subdirs == null)
	    return;
	for(File s: subdirs)
	{
	    if (!s.isDirectory())
		continue;
	    final File jarsDir = new File(s, "jar");
	    if (!jarsDir.isDirectory())
		continue;
	    Log.debug(LOG_COMPONENT, "registering extension jars from the directory " + jarsDir.getAbsolutePath());
	    addJarsToClassPath(jarsDir);
	}
    }
    */

    static private void addJarsToClassPath(File file, List<URL> urls)
    {
	try {
	    final File[] files = file.listFiles();
	    if (files == null)
		return;
	    for(File f: files)
		if (!f.isDirectory() && f.getName().toLowerCase().trim().endsWith(".jar"))
		{
		    final java.net.URL url = f.toURI().toURL();
		    urls.add(url);
		}
	}
	catch(IOException e)
	{
	    //	    Log.error(LOG_COMPONENT, "unable to add to the classpath the directory " + file.getAbsolutePath() + ":" + e.getClass().getName() + ":" + e.getMessage());
	}
    }

    /*
    static private void setUtf8()
    {
	Log.debug("init", "using UTF-8, while default system charset was " + System.getProperty("file.encoding"));
	System.setProperty("file.encoding","UTF-8");
	Field charset;
	try {
	    charset=Charset.class.getDeclaredField("defaultCharset");
	    charset.setAccessible(true);
	    charset.set(null,null);
	} catch(Exception e)
	{
	    e.printStackTrace();
	}
    }
    */
}
