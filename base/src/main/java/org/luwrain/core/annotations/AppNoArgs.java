// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.core.annotations;

import java.lang.annotation.*;
import org.luwrain.core.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AppNoArgs
{
    String name();
    String shortcut() default "";
    StarterCategory category() default StarterCategory.NONE;
String[] title();
}
