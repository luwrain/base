
package org.luwrain.util;

import java.io.*;
import java.net.*;

import org.junit.*;

import org.luwrain.core.*;

public class ConnectionsTest extends Assert
{
static private final String url = "http://download.luwrain.org/pdf/presentation-HongKongOSConference-en-2015-06-27.pdf";

    @Test public void fullSize() throws Exception
    {
	final URLConnection con = Connections.connect(new URL(url), 0);
	final InputStream is = con.getInputStream();
	final byte[] buf = new byte[512];
	int numRead = 0;
	int totalCount = 0;
	while ( (numRead = is.read(buf)) >= 0)
	    totalCount += numRead;
	assertTrue(totalCount == 77249);
	is.close();
    }

        @Test public void fullSha1() throws Exception
    {
	final URLConnection con = Connections.connect(new URL(url), 0);
	final InputStream is = con.getInputStream();
	final String sha1 = Sha1.getSha1(is);
	assertTrue(sha1.toLowerCase().equals("58602629e630b1509a3e22110a1fdcedaf1de354"));
	is.close();
    }

        @Test public void partialSize() throws Exception
    {
	final URLConnection con = Connections.connect(new URL(url), 65535);
	final InputStream is = con.getInputStream();
	final byte[] buf = new byte[512];
	int numRead = 0;
	int totalCount = 0;
	while ( (numRead = is.read(buf)) >= 0)
	    totalCount += numRead;
	assertTrue(totalCount == (77249 - 65535));
	is.close();
    }

            @Test public void partialSha1() throws Exception
    {
	final URLConnection con = Connections.connect(new URL(url), 65535);
	final InputStream is = con.getInputStream();
	final String sha1 = Sha1.getSha1(is);
	assertTrue(sha1.toLowerCase().equals("758d421f15b1307ea1826c50eb183a9bb6882e4c"));
	is.close();
    }

}
