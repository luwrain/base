// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.viewer;

import java.util.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.app.base.*;
import org.luwrain.util.*;

import static java.util.Objects.*;
import static org.luwrain.util.UrlUtils.*;

public class App extends AppBase<Strings>
{
    static final String
	LOG_COMPONENT = "viewer";

    private final String arg;
    private URL url = null;
    private String[] text = new String[0];
    private MainLayout mainLayout = null;

    final List<Path> files = new ArrayList<>();

    public App() { this(null); }
    public App(String arg)
    {
	super(Strings.class);
	this.arg = arg;
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
	final File file;
	if (arg != null && !arg.isEmpty())
	    file = urlToFile(arg); else
	    file = null;
	if (file != null && file.getName().toUpperCase().endsWith(".PDF"))
	{
	    final ViewPdf viewPdf = createPdfView(file);
	    viewPdf.show();
	    setAppName(file.getName());
	} else
	    	if (file != null && file.getName().toUpperCase().endsWith(".JPG"))
	{
	    final ViewImage viewImage = createImageView(file);
	    viewImage.show();
	    setAppName(file.getName());
	} else
	{
	    setAppName(getStrings().appName());
	    if (file != null && file.isDirectory())
		populateFiles(file.toPath()); else
		populateFiles(Paths.get(getLuwrain().getPath("~")));
	}
	this.mainLayout = new MainLayout(this);
	return mainLayout.getAreaLayout();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    void openPdf(Path path) throws IOException
    {
	final ViewPdf viewPdf = createPdfView(path.toFile());
	    viewPdf.show();
	    setAppName(path.getFileName().toString());
    }

    void populateFiles(Path dir) throws IOException
    {
	requireNonNull(dir);
	try (final var stream = Files.list(dir))
	{
	    stream.filter(Files::isRegularFile)
	    .sorted()
	    .forEach(files::add);
	}
    }

    private ViewPdf createPdfView(File file) throws IOException
    {
	return new ViewPdf(getLuwrain(), file){
	    @Override void inaccessible() {  getLuwrain().playSound(Sounds.INACCESSIBLE); }
	    @Override void announcePage(int pageNum, int pageCount) { message(getStrings().pdfPage(String.valueOf(pageNum), String.valueOf(pageCount)), Luwrain.MessageType.OK); }
	    @Override void announceMoveLeft() {}
	    @Override void announceMoveRight() {}
	    @Override void announceMoveUp() {}
	    @Override void announceMoveDown() {}
	    @Override void announceZoomIn() {}
	    @Override void announceZoomOut() {}
	};
    }

    private ViewImage createImageView(File file) throws IOException
    {
	return new ViewImage(getLuwrain(), file){
	    @Override void inaccessible() {  getLuwrain().playSound(Sounds.INACCESSIBLE); }
	    @Override void announcePage(int pageNum, int pageCount) { message(getStrings().pdfPage(String.valueOf(pageNum), String.valueOf(pageCount)), Luwrain.MessageType.OK); }
	    @Override void announceMoveLeft() {}
	    @Override void announceMoveRight() {}
	    @Override void announceMoveUp() {}
	    @Override void announceMoveDown() {}
	    @Override void announceZoomIn() {}
	    @Override void announceZoomOut() {}
	};
    }
}
