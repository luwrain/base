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

package org.luwrain.util;

import java.net.*;
import java.io.*;

import org.luwrain.core.*;

public class Connections
{
    static private final int MAX_REDIRECT_COUNT = 16;
    static private final int TIMEOUT = 15000;

    static public final class InvalidHttpResponseCodeException extends IOException
    {
	private final int code;
	private final String url;
	public InvalidHttpResponseCodeException(int code, String url)
	{
	    super("" + code + " for " + url);
	    this.code = code;
	    this.url = url;
	}
	public int getHttpCode()
	{
	    return this.code;
	}
	public String getHttpUrl()
	{
	    return this.url;
	}
    }

    public static URLConnection connect(URL url, long startFrom) throws IOException
    {
	NullCheck.notNull(url, "url");
	URL urlToTry = url;
	for(int i = 0;i < MAX_REDIRECT_COUNT;++i)
	{
	    final URLConnection con = urlToTry.openConnection();
	    final HttpURLConnection httpCon = (HttpURLConnection)con;
	    //httpCon.setRequestProperty("User-Agent", "LUWRAIN");
	    httpCon.setConnectTimeout(TIMEOUT);
	    httpCon.setReadTimeout(TIMEOUT);
	    if (startFrom > 0)
		httpCon.setRequestProperty("Range", "bytes=" + startFrom + "-");
	    httpCon.setInstanceFollowRedirects(false);
	    httpCon.connect();
	    if (httpCon.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM ||
		httpCon.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP)
	    {
		final String location = httpCon.getHeaderField("location");
		if (location == null || location.isEmpty())
		    throw new IOException("The response has the redirect code but the location is empty (" + urlToTry.toString() + ")");
		urlToTry = new URL(location);
		continue; 
	    }
	    if ((startFrom == 0 && httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) ||
		(startFrom > 0 && httpCon.getResponseCode() != HttpURLConnection.HTTP_PARTIAL))
		throw new InvalidHttpResponseCodeException(httpCon.getResponseCode(), urlToTry.toString());
	    return httpCon;
	}
	throw new IOException("Too many redirects (" + MAX_REDIRECT_COUNT + ")");
    }
}
