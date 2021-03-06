package com.github.yjcpaj4.play_with_us.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.MessageDigest;

public class FileUtil {

    private FileUtil() {
    }

    public static String getNameWithoutExtension(File f) {
        try {
            String s = f.getName();
            return s.substring(0, s.lastIndexOf("."));
        } catch (Exception e) {
            return "";
        }
    }

    public static String getExtension(File f) {
        try {
            String s = f.getName();
            return s.substring(s.lastIndexOf(".") + 1).toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }
    
    public static String getContents(File f) {
        FileInputStream s;
        
        try {
            s = new FileInputStream(f);
        }
        catch(FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        try {
            StringBuilder sb = new StringBuilder();
        
            byte[] b = new byte[1024 * 4];
            int n = -1;
            while ((n = s.read(b)) != -1) {
                sb.append(new String(b, 0, n));
            }
            
            s.close();

            return sb.toString();
        }
        catch(IOException e) {
            return null;
        }
    }
    
    public static void setContents(File f, String v) {
        try {
            if ( ! f.exists()) {
                f.createNewFile();
            }

            FileWriter fw = new FileWriter(f);
            fw.write(v);
            fw.flush();
            fw.close();
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] getChecksumBytes(File f) throws Exception {
        FileInputStream s = new FileInputStream(f);

        byte[] b = new byte[1024];
        MessageDigest m = MessageDigest.getInstance("MD5");
        int n;

        do {
            n = s.read(b);

            if (n > 0) {
                m.update(b, 0, n);
            }
        } while (n != -1);

        s.close();

        return m.digest();
    }

    public static String getChecksum(File f) {
        byte[] b;
        
        try {
            b = getChecksumBytes(f);
        }
        catch(Exception e) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (int n = 0; n < b.length; n++) {
            sb.append(Integer.toString((b[n] & 0xFF) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
