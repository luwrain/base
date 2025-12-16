
package org.luwrain.app.notepad;

import java.util.*;
import java.io.*;

import static java.util.Objects.*;
import static org.luwrain.util.StreamUtils.*;
import static org.luwrain.util.TextUtils.*;

final class TextFiles
{
    static String readAsString(File file, String charset) throws IOException
    {
	requireNonNull(file, "file can't be null");
	requireNonNull(charset, "charset can't be null");
	try (final InputStream is = new FileInputStream(file)) {
	    return new String(readAllBytes(is), charset);
    }
    }

    static List<String> read(File file, String charset) throws IOException
    {
	requireNonNull(file, "file can't be null");
	requireNonNull(charset, "charset can't be null");
	if (charset.isEmpty())
	    throw new IllegalArgumentException("charset can't be empty");
	return splitLinesAsList(readAsString(file, charset));
    }

    static void write(File file, String text, String charset) throws IOException
    {
		requireNonNull(file, "file can't be null");
	requireNonNull(text, "text can't be null");
	requireNonNull(charset, "charset can't be null");
	if (charset.isEmpty())
	    throw new IllegalArgumentException("charset can't be empty");
	try (final OutputStream os = new FileOutputStream(file)) {
	    writeAllBytes(os, text.getBytes(charset));
	    os.flush();
	}
    }

    static void write(File file, List<String> text, String charset, String lineSep) throws IOException
    {
	requireNonNull(file, "file can't be null");
		requireNonNull(text, "text can't be null");
	requireNonNull(charset, "charset can't be null");
		requireNonNull(lineSep, "lineSep can't be null");
		if (text.isEmpty() || (text.size() == 1 && text.get(0).isEmpty()))
		{
		    write(file, "", charset);
		    return;
		}
		final var b = new StringBuilder();
		boolean first = true;
		for(var i: text)
		{
		    if (!first)
			b.append(lineSep);
		    b.append(i);
		    first = false;
		}
		write(file, new String(b), charset);
    }
}
