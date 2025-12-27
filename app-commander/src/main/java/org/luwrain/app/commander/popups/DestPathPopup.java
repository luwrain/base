// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.popups;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.app.commander.*;

public final class DestPathPopup extends FilePopup
{
    public enum Type {COPY, MOVE};

    private final Type type;
    private final Strings strings;

    public DestPathPopup(Luwrain luwrain, Strings strings, Type type,
		  Path srcDir, Path[] files, Path destDir)
{
    super(luwrain,
	  name(strings, type), prefix(strings, type, files), acceptance(),
	  destDir.toFile(), srcDir.toFile(),
	  EnumSet.noneOf(FilePopup.Flags.class), Popups.DEFAULT_POPUP_FLAGS);
    NullCheck.notNull(strings, "strings");
    NullCheck.notNull(type, "type");
    this.strings = strings;
    this.type = type;
}

    static private FileAcceptance acceptance()
    {
	return (file, announce)->{
	    return true;
	};
    }

    static private String name(Strings strings, Type type)
{
    NullCheck.notNull(strings, "strings");
    NullCheck.notNull(type, "type");
    switch(type)
    {
    case COPY:
    return strings.copyPopupName();
    case MOVE:
	    return strings.movePopupName();
    }
    return null;
}

    static private String prefix(Strings strings, Type type, Path[] files)
{
    NullCheck.notNull(strings, "strings");
    NullCheck.notNull(type, "type");
    NullCheck.notNullItems(files, "files");
    switch(type)
    {
    case COPY:
	return strings.copyPopupPrefix(files.length > 1?"":files[0].getFileName().toString()).replaceAll("  ", " ");
    case MOVE:
	return strings.movePopupPrefix(files.length > 1?"":files[0].getFileName().toString()).replaceAll("  ", " ");
    }
    return "";
}
}
