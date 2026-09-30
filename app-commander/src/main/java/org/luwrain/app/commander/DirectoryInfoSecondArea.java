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
    
    DirectoryInfoSecondArea(App app, ControlContext controlContext, Path path, Runnable closing)
    {
	super(controlContext);
	this.app = app;
	this.path = path;
	this.closing = closing;
	        this.worker = new Thread(this::scanLoop, "dir-info-scanner");
        this.worker.setDaemon(true);
        this.worker.start();
    }

    @Override public boolean cancel()
    {
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

    
    private void scanLoop()
    {
        while (!cancelled)
        {
            List<String> snapshot = collectInfo();
            lines.clear();
            lines.addAll(snapshot);

            try
            {
                Thread.sleep(UPDATE_INTERVAL_MILLIS);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private List<String> collectInfo()
    {
        List<String> result = new ArrayList<>();

        if (!Files.isDirectory(path))
        {
            result.add("Directory: " + path);
            result.add("Error: path is not a directory");
            return result;
        }

        ScanStats stats = new ScanStats();

        try
        {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                {
                    stats.directories++;
                    stats.entries++;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                {
                    stats.entries++;

                    if (Files.isSymbolicLink(file))
                    {
                        stats.symbolicLinks++;
                        return FileVisitResult.CONTINUE;
                    }

                    if (Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS))
                    {
                        stats.files++;

                        long size = 0;
                        try
                        {
                            size = Files.size(file);
                        }
                        catch (IOException e)
                        {
                            stats.incomplete = true;
                        }

                        stats.totalSize += size;
                        if (size == 0)
                        {
                            stats.emptyFiles++;
                        }
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                {
                    stats.incomplete = true;
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        catch (IOException | SecurityException e)
        {
            stats.incomplete = true;
        }

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

        return result;
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
