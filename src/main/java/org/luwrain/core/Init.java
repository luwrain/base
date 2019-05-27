/*
   Copyright 2012-2019 Michael Pozhidaev <msp@luwrain.org>

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
import java.lang.reflect.Field;
import java.nio.charset.Charset;

public final class Init
{
    static public final String LOG_COMPONENT = "init";
    static private final File DEBUG_FILE = new File(new File(System.getProperty("user.home")), "luwrain-debug.txt");
    static private final File STANDALONE = new File("standalone");

    static private final String ENV_APP_DATA = "APPDATA";
    static private final String ENV_USER_PROFILE = "USERPROFILE";
    static private final String DEFAULT_USER_DATA_DIR_WINDOWS = "Luwrain";
    static private final String DEFAULT_USER_DATA_DIR_LINUX = ".luwrain";

    static public void main(String[] args) throws IOException
    {
	if (DEBUG_FILE.exists() && !DEBUG_FILE.isDirectory())
	{
	    final PrintStream log = new PrintStream(new BufferedOutputStream(new FileOutputStream(DEBUG_FILE)), true);
	    System.setOut(log);
	    System.setErr(log);
	} else
	    Log.enableBriefMode();
	final File userHomeDir = new File(System.getProperty("user.home"));
	final List<URL> urls = new LinkedList();
	addJarsToClassPath(new File("jar"), urls);
	addJarsToClassPath(new File("lib"), urls);
	final File userDataDir;
	final boolean standalone = STANDALONE.exists() && STANDALONE.isFile();
	if (!standalone)
	{
	    userDataDir = detectUserDataDir();
	    final File extDir = new File(userDataDir, "extensions");
	    addExtensionsJarsToClassPath(extDir, urls);
	} else
	{
	    userDataDir = createTempDataDir();
	    Log.info(LOG_COMPONENT, "standalone mode, temporary user data dir is " + userDataDir.getAbsolutePath());
	}
	final ClassLoader classLoader = new URLClassLoader(urls.toArray(new java.net.URL[urls.size()]), ClassLoader.getSystemClassLoader());
	Thread.currentThread().setContextClassLoader(classLoader);
	setUtf8();
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
	factory.newLaunch(standalone, args, dataDir, userDataDir, userHomeDir).run();
    }

    static private void addExtensionsJarsToClassPath(File extensionsDir, List<URL> urls)
    {
	NullCheck.notNull(extensionsDir, "extensionsDir");
	NullCheck.notNull(urls, "urls");
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
	    addJarsToClassPath(jarsDir, urls);
	}
    }

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
	}
    }

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

    static public File detectUserDataDir()
    {
	//Windows: in Application Data
	if(System.getenv().containsKey(ENV_APP_DATA) && !System.getenv().get(ENV_APP_DATA).trim().isEmpty())
	{
	    final File appData = new File(System.getenv().get(ENV_APP_DATA));
	    return new File(appData, DEFAULT_USER_DATA_DIR_WINDOWS);
	}
	if(System.getenv().containsKey(ENV_USER_PROFILE) && !System.getenv().get(ENV_USER_PROFILE).trim().isEmpty())
	{
	    final File userProfile = new File(System.getenv().get(ENV_USER_PROFILE));
	    return new File(new File(new File(userProfile, "Local Settings"), "Application Data"), DEFAULT_USER_DATA_DIR_WINDOWS);
	}
	//We are likely on Linux
	final File f = new File(System.getProperty("user.home"));
	return new File(f, DEFAULT_USER_DATA_DIR_LINUX);
    }

    static private File createTempDataDir()
    {
	try {
	    final File tmpDir = File.createTempFile("lwrtmpdatadir", "");
	    tmpDir.delete();
	    if (!tmpDir.mkdir())
	    {
		Log.fatal(LOG_COMPONENT, "unable to create temporary directory " + tmpDir.getAbsolutePath());
		System.exit(1);
	    }
	    return tmpDir;
	}
	catch(IOException e)
	{
	    Log.fatal(LOG_COMPONENT, "unable to create the temporary user data directory:" + e.getClass().getName() + ":" + e.getMessage());
	    System.exit(1);
	    return null;
	}
    }
}
