
package org.luwrain.util;

import java.net.*;
import java.io.*;

import org.luwrain.core.*;

public class Connections
{
    public static URLConnection connect(URL url, long startFrom) throws IOException
    {
	NullCheck.notNull(url, "url");
	    final URLConnection con = url.openConnection();
	    final HttpURLConnection httpCon = (HttpURLConnection)con;
	    //httpCon.setRequestProperty("User-Agent", "LUWRAIN");
	    if (startFrom > 0)
	    	    		con.setRequestProperty("Range", "bytes=" + startFrom + "-");
		httpCon.connect();
		if ((startFrom == 0 && httpCon.getResponseCode() != HttpURLConnection.HTTP_OK) ||
		    (startFrom > 0 && httpCon.getResponseCode() != HttpURLConnection.HTTP_PARTIAL))
		    throw new IOException("Bad HTTP response code: " + httpCon.getResponseCode());
		//		httpCode = httpCon.getResponseCode();
		//		final String location = httpCon.getHeaderField("location");
		//		final URL locationUrl = new URL(location);
		//		con = locationUrl.openConnection();
	    return httpCon;
    }
}
