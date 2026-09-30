// SPDX-License-Identifier: BUSL-1.1
// Copyright 2012-2026 Michael Pozhidaev <msp@luwrain.org>

package org.luwrain.app.commander;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;

final class DirectoryInfoSecondArea extends NavigationArea implements SecondArea
{
    static private final long UPDATE_INTERVAL_MILLIS = 500L;
    
    final App app;
    final Path path;
    final Runnable closing;
    private final List<String> lines = new ArrayList<>();
        private final Thread worker;
    private volatile boolean cancelled = false;
    private long lastUpdated = System.currentTimeMillis();
    
    DirectoryInfoSecondArea(App app, ControlContext controlContext, Path path, Runnable closing)
    {
	super(controlContext);
	this.app = app;
	this.path = path;
	this.closing = closing;
	        this.worker = new Thread(this::collectInfo, "commander-dir-info");
        this.worker.setDaemon(true);
        this.worker.start();
    }

    @Override public boolean cancel()
    {
	cancelled = true;
	worker.interrupt();
	closing.run();
	return true;
    }
    
    @Override public int getLineCount()
    {
	return Math.max(lines.size(), 1);
    }

    @Override public String getLine(int index)
    {
	return index < lines.size() ? lines.get(index) : "";
    }

    @Override public String getAreaName()
    {
	return app.getStrings().directoryInfoAreaName();
    }

    
    private void collectInfo()
    {
        if (!Files.isDirectory(path))
	    return;
	        final ScanStats stats = new ScanStats();
        try
        {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>()
            {
                @Override public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                {
                    stats.directories++;
                    stats.entries++;
		    updateLines(stats);
                    return cancelled ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    stats.entries++;
                    if (Files.isSymbolicLink(file))
                    {
                        stats.symbolicLinks++;
			updateLines(stats);
                    return cancelled ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
                    }
                    if (Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS))
                    {
                        stats.files++;
                        long size = 0;
                        try {
                            size = Files.size(file);
                        }
                        catch (IOException e)
                        {
                            stats.incomplete = true;
                        }
                        stats.totalSize += size;
                        if (size == 0)
                            stats.emptyFiles++;
                    }
		    updateLines(stats);
                    return cancelled ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
                }

                @Override public FileVisitResult visitFileFailed(Path file, IOException exc)
                {
                    stats.incomplete = true;
		    updateLines(stats);
                    return cancelled ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException | SecurityException e)
        {
            stats.incomplete = true;
        }
	fillLines(stats);
    }

    private void updateLines(ScanStats stats)
    {
	final long currentTime = System.currentTimeMillis();
	if (currentTime < lastUpdated + UPDATE_INTERVAL_MILLIS)
	    return;
	lastUpdated = currentTime;
	fillLines(stats);
    }

    private void fillLines(ScanStats stats)
    {
	final var result = new ArrayList<String>();
	        double averageSize = stats.files == 0 ? 0.0 : (double) stats.totalSize / stats.files;
        result.add("Directory: " + path);
        result.add("Files: " + stats.files);
        result.add("Total size: " + stats.totalSize + " bytes");
        result.add("Average file size: " + String.format(Locale.ROOT, "%.1f bytes", averageSize));
        result.add("Symbolic links: " + stats.symbolicLinks);
        result.add("Empty files: " + stats.emptyFiles);
        result.add("Directories: " + stats.directories);
        result.add("Total entries: " + stats.entries);
        try
        {
            FileStore store = Files.getFileStore(path);
            long totalSpace = store.getTotalSpace();
            long freeSpace = store.getUsableSpace();
            long usedSpace = totalSpace - freeSpace;
            double share = usedSpace == 0 ? 0.0 : (stats.totalSize * 100.0) / usedSpace;
            result.add("Disk total space: " + totalSpace + " bytes");
            result.add("Disk free space: " + freeSpace + " bytes");
            result.add("Directory share of used space: " + String.format(Locale.ROOT, "%.2f%%", share));
        }
        catch (IOException | SecurityException e)
        {
            stats.incomplete = true;
            result.add("Disk information: unavailable");
        }
        if (stats.incomplete)
        {
            result.add("Warning: information is incomplete because some directories could not be read");
        }
	lines.clear();
	lines.addAll(result);
	context.onAreaNewContent(this);
    }

    private static final class ScanStats
    {
        private long files;
        private long totalSize;
        private long symbolicLinks;
        private long emptyFiles;
        private long directories;
        private long entries;
        private boolean incomplete;
    }

}
