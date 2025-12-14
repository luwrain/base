
package org.luwrain.app.notepad;

import java.util.*;
import org.apache.logging.log4j.*;

import org.luwrain.app.base.*;
import org.luwrain.controls.*;

import static java.util.Objects.*;

public final class SettingsLayout extends LayoutBase
{
    static private final Logger log = LogManager.getLogger();

static private final String
    YANDEX_FOLDER_ID = "yandex-folder-id",
    YANDEX_API_KEY = "yandex-api-key";

    final App app;
    final FormArea form;

    public SettingsLayout(App app, ActionHandler close)
    {
	super(app);
	this.app = app;
	final var s = app.getStrings();
	form = new FormArea(getControlContext(), s.settingsAreaName());
	form.addEdit("narrating-channel-name", s.settingsFormNarratingChannelName(), requireNonNullElse(app.conf.getNarratingChannelName(), ""));
	form.addEdit("narrating-channel-params", s.settingsFormNarratingChannelParams(), requireNonNullElse(app.conf.getNarratingChannelParams(), ""));
	form.addEdit("narrated-file-len", s.settingsFormNarratedFileLen(), String.valueOf(app.conf.getNarratedFileLen()));
	form.addEdit("narrating-speech-pitch", s.settingsFormNarratingSpeechPitch(), String.valueOf(app.conf.getNarratingSpeechPitch()));
	form.addEdit("narrating-speech-rate", s.settingsFormNarratingSpeechRate(), String.valueOf(app.conf.getNarratingSpeechRate()));
	form.addEdit("narrating-pause-duration", s.settingsFormNarratingPauseDuration(), String.valueOf(app.conf.getNarratingPauseDuration()));
			setAreaLayout(form, null);
			setOkHandler(() -> {
				//				app.conf.setYandexFolderId(form.getEnteredText(YANDEX_FOLDER_ID));
				//				app.conf.setYandexApiKey(form.getEnteredText(YANDEX_API_KEY));
				app.getLuwrain().saveConf(app.conf);
				close.onAction();
				return true;
			    });
			log.debug("Setting close handler");
			setCloseHandler(close);
		    }

    /*
    @Override public boolean saveSectionData()
    {
	conf.setNarratingChannelName(getEnteredText("narrating-channel-name"));
	conf.setNarratingChannelParams(getEnteredText("narrating-channel-params"));
	try {
	    final int value = Integer.parseInt(getEnteredText("narrated-file-len"));
	    if (value < 0)
	    {
		luwrain.message(strings.settingsFormFileLenMayNotBeNegative(), Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratedFileLen(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message(strings.settingsFormFileLenIsNotInteger(), Luwrain.MessageType.ERROR);
	    return false;
	}
	try {
	    final int value = Integer.parseInt(getEnteredText("narrating-speech-pitch"));
	    if (value < -50 || value > 50)
	    {
		luwrain.message("fixme", Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratingSpeechPitch(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message("fixme" + e.getMessage(), Luwrain.MessageType.ERROR);
	    return false;
	}
	try {
	    final int value = Integer.parseInt(getEnteredText("narrating-speech-rate"));
	    if (value < -50 || value > 50)
	    {
		luwrain.message("fixme", Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratingSpeechRate(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message("fixme" + e.getMessage(), Luwrain.MessageType.ERROR);
	    return false;
	}
	try {
	    final int value = Integer.parseInt(getEnteredText("narrating-pause-duration"));
	    if (value < 0)
	    {
		luwrain.message("fixme", Luwrain.MessageType.ERROR);
		return false;
	    }
	    conf.setNarratingPauseDuration(value);
	}
	catch(NumberFormatException e)
	{
	    luwrain.message("fixme" + e.getMessage(), Luwrain.MessageType.ERROR);
	    return false;
	}
	return true;
    }
    */

}
