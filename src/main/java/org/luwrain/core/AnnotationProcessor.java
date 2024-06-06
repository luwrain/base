
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

@SupportedAnnotationTypes("org.luwrain.core.annotations.App")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor
{
    static private final String LS = System.lineSeparator();
    //  static private final RE_I18N = Pattern.compile("

    
                @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env)
    {
	for (var a: annotations)
	    switch(a.getSimpleName().toString())
	{
	case "App":
	for(var e: env.getElementsAnnotatedWith(a))
	    write(e.toString() + "Extension", generate(e));
	break;
	}
	return true;
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

	    String generate(Element el)
    {
	final var a = el.getAnnotation(org.luwrain.core.annotations.App.class);
	if (a == null)
	    throw new IllegalStateException("" + el.toString() + " is not annotated with org.luwrain.core.annotations.App");
	final var app = (org.luwrain.core.annotations.App)a;
	final String
	cl = el.toString(),
	simpleCl = cl.substring(cl.lastIndexOf(".") + 1),
		newCl = simpleCl + "Extension",
	pkg = cl.substring(0, cl.lastIndexOf("."));

	final var titles = new HashMap<String, String>();
	if (app.i18n() != null)
	    for(var i: app.i18n())
	{

	}
	return "package " + pkg + ";" + LS  +
	"import org.luwrain.core.*;" + LS +
			   "import com.google.auto .service.*;" + LS +
			   "@AutoService(org.luwrain.core.Extension.class)" + LS +
			   "public final class " + newCl + " extends org.luwrain.core.EmptyExtension {" + LS +
			   "@Override public ExtensionObject[] getExtObjects(Luwrain luwrain) { return new ExtensionObject[] { new SimpleShortcut(\"" + app.name() + "\", " + simpleCl + ".class) }; }" + LS +
				   "@Override public Command[] getCommands(Luwrain luwrain) { return new Command[] { new SimpleShortcutCommand(\"" + app.name() + "\") }; }" + LS +
	"}" + LS;
    }
            }
