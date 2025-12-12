// SPDX-License-Identifier: Apache-2.0
// Copyright 2012-2025 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;
import java.io.*;
import java.nio.file.*;

import org.luwrain.core.*;
import org.luwrain.popups.*;
import org.luwrain.app.commander.popups.*;

final class Conv
{
    private final Luwrain luwrain;
    private final Strings strings;
    private final Set<String> runHistory = new TreeSet<>();

    Conv(App app)
    {
	this.luwrain = app.getLuwrain();
	this.strings = app.getStrings();
    }

    Path copy(Path copyFromDir, Path[] filesToCopy, Path copyTo)
    {
	final DestPathPopup popup = new DestPathPopup(luwrain, strings, DestPathPopup.Type.COPY, copyFromDir, filesToCopy, copyTo);
	luwrain.popup(popup);
	if (popup.wasCancelled())
	    return null;
	return popup.result().toPath();
    }

    Path move(Path  moveFromDir, Path[] filesToMove, Path moveTo)
    {
	final DestPathPopup popup = new DestPathPopup(luwrain, strings, DestPathPopup.Type.MOVE, moveFromDir, filesToMove, moveTo);
	luwrain.popup(popup);
	if (popup.wasCancelled())
	    return null;
	return popup.result().toPath();
    }

    File mkdirPopup(File createIn)
    {
	final File res = Popups.path(luwrain,
				     strings.mkdirPopupName(), strings.mkdirPopupPrefix(), createIn, 
				     (fileToCheck, announce)->{
					 NullCheck.notNull(fileToCheck, "fileToCheck");
					 if (fileToCheck.exists())
					 {
					     if (announce)
						 luwrain.message(strings.enteredPathExists(fileToCheck.getAbsolutePath()), Luwrain.MessageType.ERROR);
					     return false;
					 }
					 return true;
				     });
	return res != null?res:null;
    }

    boolean deleteConfirmation(Path[] files)
    {
	final String text = strings.delPopupText(luwrain.i18n().getNumberStr(files.length, "items"));
return Popups.confirmDefaultNo(luwrain, strings.delPopupName(), text);
    }

    String ftpAddress()
    {
	return Popups.text(luwrain, strings.ftpConnectPopupName(), strings.ftpConnectPopupText(), "ftp://");
    }

    /*
    FilesOperation.ConfirmationChoices overrideConfirmation(File file)
    {
	NullCheck.notNull(file, "file");
	final String cancel = "Прервать";
	final String overwrite = "Перезаписать";
	final String overwriteAll = "Перезаписать все";
	final String skip = "Пропустить";
	final String skipAll = "Пропустить все";
	final Object res = Popups.fixedList(luwrain, "Подтверждение перезаписи " + file.getAbsolutePath(), new String[]{overwrite, overwriteAll, skip, skipAll, cancel});
	if (res == overwrite || res == overwriteAll)
	    return FilesOperation.ConfirmationChoices.OVERWRITE;
	if (res == skip || res == skipAll)
	    return FilesOperation.ConfirmationChoices.SKIP;
	return FilesOperation.ConfirmationChoices.CANCEL;
    }
    */

    File leftPanelVolume()
    {
	return Popups.disks(luwrain, strings.leftPanelVolumePopupName());
    }

        File rightPanelVolume()
    {
	return Popups.disks(luwrain, strings.rightPanelVolumePopupName());
    }

    String run()
    {
	return Popups.editWithHistory(luwrain, "Выполнить", "Команда:", "", runHistory);
    }
}
