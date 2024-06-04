
package org.luwrain.core;

import java.lang.annotation.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.io.*;
import java.util.*;
import javax.annotation.processing.*;
import com.google.auto.service.*;

@SupportedAnnotationTypes("org.luwrain.core.annotations.App")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor
{
                @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
    {
	try {
	final var file = processingEnv.getFiler().createSourceFile("ProbaExtension");
	            try (PrintWriter out = new PrintWriter(file.openWriter())) {
			out.println("import com.google.auto.service.*;");
						out.println("import org.luwrain.core.*;");
						out.println("@AutoService(Extension.class)");
			out.println("public class ProbaExtension extends EmptyExtension{}");
			out.flush();
		    }
	}
	catch(IOException ex)
	{
	    //	    throw new RuntimeException(ex);
	}
		System.err.println("Created auto extension");
		    return false;
                }
            }
