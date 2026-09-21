// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.core.annotations;

import java.lang.annotation.*;
import org.luwrain.core.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AppSingleArg
{
    String name();
    StarterCategory category() default StarterCategory.NONE;
    String[] title();
}
