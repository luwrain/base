// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.*;
import java.util.concurrent.*;

import org.luwrain.core.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.nlp.*;
import static org.luwrain.core.DefaultEventResponse.*;

abstract class Appearance extends DefaultEditAreaAppearance
{
    Appearance(ControlContext context)
    {
	super(context);
    }

    abstract App.Mode getMode();
    abstract EditArea getEditArea();

    @Override public void announceLine(int index, String line)
    {
	final App.Mode mode = getMode();
	final String text;
	if (mode != null)
	    switch(mode)
	    {
	    case NONE:
		text = context.getSpeakableText(line, Luwrain.SpeakableTextType.NONE);
		break;
	    case PROGRAMMING:
		text = context.getSpeakableText(line, Luwrain.SpeakableTextType.PROGRAMMING);
		break;
	    case NATURAL:
		text = context.getSpeakableText(line, Luwrain.SpeakableTextType.NATURAL);
		break;
	    default:
		text = line;
	    } else
	    text = line;
	boolean hasSpellProblems = false;
	if (getEditArea().getContent().getLineMarks(index) != null)
	{
	    final LineMarks.Mark[] marks = getEditArea().getContent().getLineMarks(index).getMarks();
	    if (marks != null)
		for(LineMarks.Mark m: marks)
		    if (m.getMarkObject() != null && m.getMarkObject() instanceof SpellProblem)
		    {
			hasSpellProblems = true;
			break;
		    }
	}
	if (!hasSpellProblems || line.trim().isEmpty())
	{
	    NavigationArea.defaultLineAnnouncement(context, index, text);
	    return;
	}
	context.setEventResponse(text(Sounds.SPELLING, text));
    }
}
