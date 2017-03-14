
package org.luwrain.util;

import java.io.*;
import java.net.*;

import org.luwrain.core.*;

public class Urls
{
    static public File toFile(URL url)
    {
NullCheck.notNull(url, "url");
if (url.getProtocol() == null || !url.getProtocol().toLowerCase().equals("file"))
    return null;
try {
return new File(url.toURI());
}
catch(URISyntaxException e)
{
    return new File(url.getPath());
}



    }
}
