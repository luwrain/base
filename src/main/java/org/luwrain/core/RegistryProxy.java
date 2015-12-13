/*
   Copyright 2012-2015 Michael Pozhidaev <msp@altlinux.org>

   This file is part of the Luwrain.

   Luwrain is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public
   License as published by the Free Software Foundation; either
   version 3 of the License, or (at your option) any later version.

   Luwrain is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.
*/

package org.luwrain.core;

import java.lang.reflect.*;

import org.luwrain.util.RegistryPath;

public class RegistryProxy
{
    static public <T> T create(Registry registry, String regDir, Class cl)
    {
	NullCheck.notNull(registry, "registry");
	NullCheck.notNull(regDir, "regDir");

	return (T) Proxy.newProxyInstance(
					  cl.getClassLoader(),
					  new Class[]{cl},
					  (object, method, args)->{
					      final String name = method.getName();
					      if (name.length() <= 3)
						  throw new IllegalArgumentException("\'" + name + "\' is too short to be a valid method name");
					      if (!name.startsWith("get") && !name.startsWith("set"))
						  throw new IllegalArgumentException("Method name should begin with \'get\' or \'set\', \'" + name + "\' is an inappropriate name");
					      final Class<?> returnType = method.getReturnType();

					      final StringBuilder b = new StringBuilder();
					      for(int i = 3;i < name.length();++i)
					      {
						  final char c = name.charAt(i);
						  if (i > 3 && Character.isUpperCase(c) && Character.isLowerCase(name.charAt(i - 1)))
						  {
						      b.append("-");
						      b.append(Character.toLowerCase(c));
						  } else
						      b.append(Character.toLowerCase(c));
					      }
					      final String paramName = RegistryPath.join(regDir, new String(b));

					      //Reading a string value
					      if (returnType.equals(String.class) && name.startsWith("get"))
					      {

						  final int valueType = registry.getTypeOf(paramName);
						  if (valueType == Registry.INVALID)
						      throw new IllegalArgumentException("There is no registry value " + paramName);
						  if (valueType != Registry.STRING)
						      throw new IllegalArgumentException("Registry value " + paramName + " is not a string");
						  return registry.getString(paramName);
					      }

					      return null;
					  });
    }
}
