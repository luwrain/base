
package org.luwrain.app.notepad;

import java.util.*;
import java.io.*;

import static java.util.Objects.*;
import static org.luwrain.util.StreamUtils.*;

final class textFiles
{
static String readAsString(File file, String charset) throws IOException
    {
	requireNonNull(file, "file can't be null");
	requireNonNull(charset, "charset can't be null");
	try (final InputStream is = new FileInputStream(file)) {
	final byte[] bytes = readAllBytes(is);
	return new String(bytes, charset);
    }
    }

    static List<String> read(File file, String charset) throws IOException
    {
	final String s= readAsString(file, charset).replaceAll("\r\n",
							       "\r").replaceAll("\r", "\n");
	return Arrays.asList(s.split("\n", -1));
    }
}
