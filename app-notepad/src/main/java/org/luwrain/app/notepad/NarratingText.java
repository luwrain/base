// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.*;

import org.luwrain.core.*;

final class NarratingText
{
    final LinkedList<String> sents = new LinkedList<>();
    private StringBuilder b = new StringBuilder();

    void split(String[] text)
    {
	NullCheck.notNullItems(text, "text");
	for(int i = 0;i < text.length;i++)
	{
	    final String line = text[i].trim();
	    if (line.isEmpty())
	    {
		if (!sents.isEmpty() && !sents.getLast().isEmpty())
		    sents.add("");
		continue;
	    }
	    int posFrom = 0;
	    for(int j = 0;j < line.length();j++)
	    {
		final char c = line.charAt(j);
		final char cc = j + 1< line.length()?line.charAt(j + 1):'\0';
		if ((c == '.' || c == '!' || c == '?')&&
		    (cc == '\0' || Character.isWhitespace(cc)))
		{
		    onSentEnd(line, posFrom, j + 1);
		    j++;
		    posFrom = j + 1;
		}
	    }
	    if (posFrom < line.length())
		onSentPart(line, posFrom, line.length());
	}
	final String l = new String(b).trim();
	if (!l.isEmpty())
	    sents.add(l);
    }

    private void onSentPart(String line, int posFrom, int posTo)
    {
	if (b.length() > 0)
	    b.append(" ");
	b.append(line.substring(posFrom, posTo));
    }

    private void onSentEnd(String line, int posFrom, int posTo)
    {
	if (b.length() > 0)
	    b.append(" ");
	b.append(line.substring(posFrom, posTo));
	final String l = new String(b).trim();
	if (!l.isEmpty())
	    sents.add(l);
	b = new StringBuilder();
    }
}
