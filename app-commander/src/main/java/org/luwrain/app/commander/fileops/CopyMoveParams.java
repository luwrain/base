// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander.fileops;


import java.util.*;
import java.nio.file.*;
import lombok.*;

import org.luwrain.app.commander.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class CopyMoveParams
{
    String name;
    private List<Path> source;
    private Path dest;
    private OperationListener listener;
}
