
package org.luwrain.core.annotations;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface App
{
    String name();
String[] i18n();
}
