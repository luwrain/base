// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.notepad;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;
import org.apache.logging.log4j.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.controls.edit.*;
import org.luwrain.speech.*;
import org.luwrain.app.base.*;

import static org.luwrain.util.TextUtils.*;
import static org.luwrain.app.notepad.TextFiles.*;

public final class App extends AppBase<Strings>
{
    static private final Logger log = LogManager.getLogger();
    static private final String
	DEFAULT_CHARSET = "UTF-8",
	NATURAL_MODE_CORRECTOR_HOOK = "luwrain.notepad.mode.natural",
	PROGRAMMING_MODE_CORRECTOR_HOOK = "luwrain.notepad.mode.programming";

    enum Mode { NONE, NATURAL, PROGRAMMING };

    File file = null;
    boolean modified = false;
    String charset = DEFAULT_CHARSET;
    String lineSeparator = System.lineSeparator();
    Mode mode = Mode.NONE;
    boolean speakIndent = false;
    Config conf = null;
Conv conv = null;
Hooks hooks = null;
    private FutureTask narratingTask = null; 
    private Narrating narrating = null;
    private final String arg;
    private MainLayout mainLayout = null;
    private NarratingLayout narratingLayout = null;

    public App()
    {
	this(null);
    }

    public App(String arg)
    {
	super(Strings.class, "luwrain.notepad");
	this.arg = arg;
	setTabProcessing(false);
    }

    @Override protected AreaLayout onAppInit() throws IOException
    {
		conf = getLuwrain().loadConf(Config.class);
	if (conf == null)
	{
	    conf = new Config();
	    getLuwrain().saveConf(conf);
	}
	conv = new Conv(this);
	hooks = new Hooks(this);
	mainLayout = new MainLayout(this);
	this.narratingLayout = new NarratingLayout(this, ()->{});
	setAppName(getStrings().appName());
	if (arg != null && !arg.isEmpty())
	{
	    this.file = new File(arg);
	    if (this.file.exists() && !this.file.isDirectory())
		mainLayout.setText(read(file, charset));
	    this.modified = false;
	    setAppName(file.getName());
	}
	return mainLayout.getAreaLayout();
    }

    void openLayout(AreaLayout layout)
    {
	NullCheck.notNull(layout, "layout");
	getLayout().setBasicLayout(layout);
    }

    //Returns True if everything saved, false otherwise
    boolean onSave()
    {
	if (!modified)
	{
	    getLuwrain().message(getStrings().noModificationsToSave());
	    return true;
	}
	if (file == null)
	{
	    final File f = conv.save(null);
	    if (f == null)
		return false;
	    this.file = f;
	    mainLayout.onNewFile();
	    setAppName(file.getName());
	}
	try {
	    save(mainLayout.editArea.getText());
	}
	catch(IOException e)
	{
	    getLuwrain().crash(e);
	    return false;
	}
	this.modified = false;
	getLuwrain().message(getStrings().fileIsSaved(), Luwrain.MessageType.OK);
	return true;
    }

    //Returns true, if there are no modifications a user might want to save
    boolean everythingSaved()
    {
	if (!modified)
	    return true;
	switch(conv.unsavedChanges())
	{
	case CONTINUE_SAVE:
	    return onSave();
	case CONTINUE_UNSAVED:
	    return true;
	case CANCEL:
	    return false;
	}
	return false;
    }

    void activateMode(Mode mode)
    {
	switch(mode)
	{
	case NATURAL:
	    //	    corrector.setActivatedCorrector(new DirectScriptMultilineEditCorrector(new DefaultControlContext(getLuwrain()), corrector.getDefaultCorrector(), NATURAL_MODE_CORRECTOR_HOOK));
	    break;
	case PROGRAMMING:
	    //	    corrector.setActivatedCorrector(new DirectScriptMultilineEditCorrector(new DefaultControlContext(getLuwrain()), corrector.getDefaultCorrector(), PROGRAMMING_MODE_CORRECTOR_HOOK));
	    break;
	}
    }

    boolean narrating(String[] text)
    {
	if (isBusy())
	    return false;
	final NarratingText narratingText = new NarratingText();
	narratingText.split(text);
	if (narratingText.sents.isEmpty())
	{
	    getLuwrain().message(getStrings().noTextToSynth(), Luwrain.MessageType.ERROR);
	    return true;
	}
	final File destDir = conv.narratingDestDir();
	if (destDir == null)
	    return true;
	final Channel channel;
	try {
	    channel = getLuwrain().loadSpeechChannel(conf.getNarratingChannelName(), conf.getNarratingChannelParams());
	}
	catch(Exception e)
	{
	    getLuwrain().crash(e);
	    return true;
	}
	if (channel == null)
	{
	    getLuwrain().message(getStrings().noChannelToSynth(conf.getNarratingChannelName()), Luwrain.MessageType.ERROR);
	    return true;
	}
	log.trace("Narrating channel loaded: " + channel.getChannelName());
	final NarratingLayout layout = new NarratingLayout(this, ()->cancelNarrating());
	this.narrating = new Narrating(this, layout, narratingText.sents.toArray(new String[narratingText.sents.size()]),
				       destDir, new File(getLuwrain().getFileProperty("luwrain.dir.scripts"), "lwr-audio-compress").getAbsolutePath(), channel);
	this.narratingTask = new FutureTask<>(this.narrating, null);
	getLuwrain().executeBkg(this.narratingTask);
	getLayout().setBasicLayout(layout.getLayout());
	return true;
    }

    private void cancelNarrating()
    {
	if (isBusy() && narrating != null)
	{
	    narrating.interrupting = true;
	    return;
	}
	this.narrating = null;
	this.narratingTask = null;
	setAreaLayout(mainLayout);
	getLuwrain().announceActiveArea();
    }

    void finishedNarrating()
    {
	narrating = null;
	narratingTask = null;
    }

    void save(String[] lines) throws IOException
    {
	NullCheck.notNullItems(lines, "lines");
	//	org.luwrain.util.FileUtils.writeTextFileMultipleStrings(file, lines, charset, lineSeparator);
    }

    @Override public boolean isBusy()
    {
	return narratingTask != null && !narratingTask.isDone();
    }

    @Override public boolean onEscape()
    {
	closeApp();
	return true;
    }

    @Override public void closeApp()
    {
	if (isBusy())
	{
	    getLuwrain().message(getStrings().cancelNarratingBeforeClosing(), Luwrain.MessageType.ERROR);
	    return;
	}
	if (!everythingSaved())
	    return;
	super.closeApp();
    }
}
