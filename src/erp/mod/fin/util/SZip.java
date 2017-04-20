/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.fin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import sa.lib.SLibUtils;

/**
 *
 * @author Edwin Carmona
 */
public class SZip {
    
    public static byte[] compressFileToZip(File[] srcfile) {
	byte[] buf = new byte[1024];
        int len = 0;
        byte[] bFile = null;
        File fileTemporal = null;
        
        try {
            fileTemporal = File.createTempFile("file", ".zip");
            
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fileTemporal));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
            
            bFile = readBytesFromFile(fileTemporal);
        }
        catch (Exception e) {
            SLibUtils.printException(SZip.class, e);
        }
        
        return bFile;
    }
    
    private static byte[] readBytesFromFile(File file) {
        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {
            bytesArray = new byte[(int) file.length()];
            
            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        }
        catch (IOException e) {
            SLibUtils.printException(SZip.class, e);
        } 
        finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                }
                catch (IOException e) {
                    SLibUtils.printException(SZip.class, e);
                }
            }

        }

        return bytesArray;

    }

}
