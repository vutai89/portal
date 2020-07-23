package com.mcredit.common;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

public class JustOneLock {
    private String appName;
    private File file;
    private FileChannel channel;
    private FileLock lock;

    public JustOneLock(String appName) {
        this.appName = appName;
    }

    public boolean isAppActive() {
        try {
        	URL applicationRootPathURL = getClass().getProtectionDomain().getCodeSource().getLocation();
        	File applicationRootPath = new File(applicationRootPathURL.getPath());
        	if(applicationRootPath.isDirectory()){
        		file = new File(applicationRootPath, appName + ".tmp");
        	}
        	else{
        		file = new File(applicationRootPath.getParentFile(), appName + ".tmp");
        	}        	
            channel = new RandomAccessFile(file, "rw").getChannel();

            try {
                lock = channel.tryLock();
            }
            catch (OverlappingFileLockException e) {
                // already locked
                closeLock();
                return true;
            }

            if (lock == null) {
                closeLock();
                return true;
            }

            Runtime.getRuntime().addShutdownHook(new Thread() {
                    // destroy the lock when the JVM is closing
                    public void run() {
                        closeLock();
                        deleteFile();
                    }
                });
            return false;
        }
        catch (Exception e) {
            closeLock();
            return true;
        }
    }

    private void closeLock() {
        try { lock.release();  }
        catch (Exception e) {  }
        try { channel.close(); }
        catch (Exception e) {  }
    }

    private void deleteFile() {
        try { file.delete(); }
        catch (Exception e) { }
    }
}
