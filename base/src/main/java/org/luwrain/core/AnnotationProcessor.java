// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.core;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.lang.annotation.*;

import javax.lang.model.*;
import javax.lang.model.element.*;
import javax.lang.model.element.TypeElement;
import javax.annotation.processing.*;
import com.google.auto.service.*;

@SupportedAnnotationTypes("org.luwrain.core.annotations.*")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor
{
    static private final String LS = System.lineSeparator();
    static private final Pattern RE_I18N = Pattern.compile("([a-z]{2})=(.*)$");

                @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
	for (var a: annotations)
	    switch(a.getSimpleName().toString())
	{
	case "AppNoArgs":
	for(var e: env.getElementsAnnotatedWith(a))
	    write(e.toString() + "Extension", generateAppNoArgs(e));
	break;
		case "ResourceStrings":
	for(var e: env.getElementsAnnotatedWith(a))
	    write(e.toString() + "Extension", generateResourceStrings(e));
	break;
	}
	return true;
    }


	    String generateAppNoArgs(Element el)
    {
	final var a = el.getAnnotation(org.luwrain.core.annotations.AppNoArgs.class);
	if (a == null)
	    throw new IllegalStateException(el.toString() + " is not annotated with org.luwrain.core.annotations.AppNoArgs");
	final var app = (org.luwrain.core.annotations.AppNoArgs)a;
	final String
	cl = el.toString(),
	simpleCl = cl.substring(cl.lastIndexOf(".") + 1),
		newCl = simpleCl + "Extension",
	pkg = cl.substring(0, cl.lastIndexOf("."));

	final var titles = new StringBuilder();
	if (app.title() != null)
	    for(var i: app.title())
	{
	    final var m = RE_I18N.matcher(i);
	    if (!m.find())
		throw new IllegalArgumentException("Illegal command title value: " + i);
	    titles.append("        i18n.addCommandTitle(\"")
	    .append(m.group(1))
	    .append("\", \"")
	    .append(app.name())
	    .append("\", \"")
	    .append(m.group(2))
	    .append("\");")
	    .append(LS);
	}

	final String starter;
	if (app.category() != null && app.category() != StarterCategory.NONE)
	{
	    starter = ", new DefaultStarter(\"command:" + app.name() + "\", StarterCategory." + app.category().toString() + ")";
	} else
	    starter = "";

	return "package " + pkg + ";" + LS  +
	"import org.luwrain.core.*;" + LS +
			   "import com.google.auto .service.*;" + LS +
			   "@AutoService(org.luwrain.core.Extension.class)" + LS +
			   "public final class " + newCl + " extends org.luwrain.core.EmptyExtension {" + LS +
			   "@Override public ExtensionObject[] getExtObjects(Luwrain luwrain) { return new ExtensionObject[] { new DefaultShortcut(\"" + app.name() + "\", " + simpleCl + ".class)" + starter + " }; }" + LS +
				   "@Override public Command[] getCommands(Luwrain luwrain) { return new Command[] { new SimpleShortcutCommand(\"" + app.name() + "\") }; }" + LS +
	        "@Override public void i18nExtension(Luwrain luwrain, org.luwrain.i18n.I18nExtension i18n)" + LS +
    "{" + LS +
	new String(titles) + LS +
	"}" + LS +
		"}" + LS;
    }

    	    String generateResourceStrings(Element el)
    {
	final var s = el.getAnnotation(org.luwrain.core.annotations.ResourceStrings.class);
	if (s == null)
	    throw new IllegalStateException("" + el.toString() + " is not annotated with org.luwrain.core.annotations.ResourceStrings");
	final var str = (org.luwrain.core.annotations.ResourceStrings)s;
	final String
	cl = el.toString(),
	simpleCl = cl.substring(cl.lastIndexOf(".") + 1),
		newCl = simpleCl + "Extension",
	pkg = cl.substring(0, cl.lastIndexOf("."));
	final var b = new StringBuilder();
	for(var l: str.langs())
	{
	    final String resource = simpleCl + "-" + l + ".properties";
		    b.append("i18n.addStrings(\"").append(l).append("\", ")
		    .append("\"").append(cl).append("\", ")
	    .append("new ResourceStringsObj(getClass().getClassLoader(), getClass(), \"")
	    .append(resource)
	    .append("\").create(\"")
	    .append(l)
	    .append("\", ")
	    .append(cl)
	    .append(".class));")
	    .append(LS);
	}
	return "// Generated automatically by " + this.getClass().getName() + LS +
	"package " + pkg + ";" + LS  +
	"import org.luwrain.core.*;" + LS +
	"import org.luwrain.i18n.*;" + LS +
	"import org.apache.logging.log4j.*;" + LS +
			   "import com.google.auto .service.*;" + LS +
			   "@AutoService(org.luwrain.core.Extension.class)" + LS +
			   "public final class " + newCl + " extends org.luwrain.core.EmptyExtension {" + LS +
	"static private final Logger log = LogManager.getLogger();" + LS +
	        "@Override public void i18nExtension(Luwrain luwrain, org.luwrain.i18n.I18nExtension i18n)" + LS +
    "{" + LS +
	"try {" + LS +
	new String(b) +
	"}" + LS +
	"catch(Exception ex)" + LS +
	"{" + LS +
	"log.error(\"Unable to load a string resource file for the strings object " + cl + "\", ex);" + LS +
	"}" + LS +
	"}" + LS +
		"}" + LS;
    }

    	void write(String className, String text)
	{
	try {
	final var file = processingEnv.getFiler().createSourceFile(className);
	            try (PrintWriter out = new PrintWriter(file.openWriter())) {
			out.println(text);
			out.flush();
		    }
	}
	catch(IOException ex)
	{
	    	    throw new RuntimeException(ex);
	}
	}


            }
