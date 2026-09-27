// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;

import java.util.*;
import java.nio.file.*;

import lombok.*;

import org.luwrain.app.commander.*;

import static java.util.Objects.*;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public final class CopyMoveParams
{
    String name;
    private List<Path> source;
    private Path dest;
    private OperationListener listener;

    public CopyMoveParams(String name, List<Path> source, Path dest, OperationListener listener)
    {
	this.name = requireNonNull(name, "name");
	this.source = requireNonNull(source, "source");
	this.dest = requireNonNull(dest, "dest");
	this.listener = requireNonNull(listener, "listener");
    }
}
