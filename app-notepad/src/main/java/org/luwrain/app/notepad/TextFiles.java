
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
}
