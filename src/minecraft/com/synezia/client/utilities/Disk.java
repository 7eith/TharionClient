package com.synezia.client.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Snkh
 *	26 jul. 2018
 */

public class Disk {
    private static final String UTF8 = "UTF-8";
    private static HashMap<String, Lock> locks = new HashMap<String, Lock>();

    public static byte[] readBytes(final File file) throws IOException {
        final int length = (int)file.length();
        final byte[] output = new byte[length];
        final InputStream in = new FileInputStream(file);
        for (int offset = 0; offset < length; offset += in.read(output, offset, length - offset)) {}
        in.close();
        return output;
    }
    
    public static void writeBytes(final File file, final byte[] bytes) throws IOException {
        final FileOutputStream out = new FileOutputStream(file);
        out.write(bytes);
        out.close();
    }
    
    public static void write(final File file, final String content) throws IOException {
        writeBytes(file, utf8(content));
    }
    
    public static String read(final File file) throws IOException {
        return utf8(readBytes(file));
    }
    
    public static boolean writeCatch(final File file, final String content) {
        final byte[] bytes = utf8(content);
        String name = file.getName();
        final Lock lock;

        if (locks.containsKey(name)) {
            lock = locks.get(name);
        } else {
            ReadWriteLock rwl = new ReentrantReadWriteLock();
            lock = rwl.writeLock();
            locks.put(name, lock);
        }

            lock.lock();
            try {
                FileOutputStream out = new FileOutputStream(file);
                out.write(bytes);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }


        return true;
    }
    
    public static String readCatch(final File file) {
        try {
            return read(file);
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static byte[] utf8(final String string) {
        try {
            return string.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String utf8(final byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
