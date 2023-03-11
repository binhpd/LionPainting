package com.hiccup.kidpainting.utilities;

/**
 * Created by ${binhpd} on 10/18/2015.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.hiccup.kidpainting.common.PaintingConstants;

/**
 * Created by hung.nn on 11/26/13.
 */
public class StorageUtils {

    public static final String TAG = "StorageUtils";
    private static final int BUFFER = 1024;

    public static String readInternalFile(Context context, String filePath) {
        StringBuilder sb = new StringBuilder();
        try{
            File file;
            if (filePath.startsWith("/")) {
                file = new File(context.getFilesDir() + filePath);
            } else {
                file = new File(context.getFilesDir() + "/" + filePath);
            }
            if(!file.exists()) {
                return "";
            }
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            fis.close();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + filePath);
        } catch(OutOfMemoryError e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static boolean writeIntToSharedPref(Context context, String key, int value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putInt(key, value).commit();
        return true;
    }

    public static boolean writeLongToSharedPref(Context context, String key, long value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putLong(key, value).commit();
        return true;
    }

    public static boolean writeStringToSharedPref(Context context, String key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putString(key, value).commit();
        return true;
    }

    public static boolean writeBoolenToSharedPref(Context context, String key, Boolean value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPref.edit().putBoolean(key, value).commit();
        return  true;
    }
    public static int getIntFromSharedPref(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(key, 0);
    }

    public static int getIntFromSharedPref(Context context, String key, int defValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getInt(key, defValue);
    }

    public static boolean getBooleanFromSharedPref(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getBoolean(key, defaultValue);
    }

    public static String getStringFromSharedPref(Context context, String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, "");
    }

    public static String getStringFromSharedPref(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(key, defaultValue);
    }

    public static boolean isFileExistInternal(Context context, String fileName) {
        File file = new File(context.getFilesDir().toString() + PaintingConstants.SLASH + fileName);
        if(file.exists()) {
            return true;
        }
        return false;
    }

    public static boolean isFileExistExternalPrivate(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(null), fileName);
        if (file != null) {
            return file.exists();
        }
        return false;
    }

    public static boolean deleteFileInternal(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (file != null) {
            return file.delete();
        }
        return true;
    }

    public static boolean deleteFileInExternalPrivate(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(null), fileName);
        if (file != null) {
            return file.delete();
        }
        return true;
    }

    public static void deleteFolderRecursive(File file) {

        if (file.isDirectory()) {
            if(file.listFiles() != null)
                for (File child : file.listFiles()) {
                    deleteFolderRecursive(child);
                }
        }
        file.delete();
    }

    /**
     * Checks if external storage is available for read and write
     * @return true if writable, false if not
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read
     * @return true if readable, false if not
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static File copyFileToExternalFilesDir(Context context, File source) {
        File destination = new File(context.getExternalFilesDir(null), source.getName());
        return copyFileToExternalFilesDir(context, source, destination);
    }

    public static File copyFileToExternalFilesDir(Context context, File source, File destination) {
        if (destination == null) {
            destination = new File(context.getExternalFilesDir(null), source.getName());
        }
        try {
            InputStream in = new FileInputStream(source);
            OutputStream out = new FileOutputStream(destination);
            byte[] buf = new byte[BUFFER];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return destination;
    }

    public static File compressFolder(Context context, File sourceDir) {
        File[] sourceFiles = sourceDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String fileName = pathname.getName().toLowerCase();
                if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
                    return true;
                }
                return false;
            }
        });
        String folderName = sourceDir.getName();
        File destination = new File(context.getExternalFilesDir(null), folderName + ".zip");
        BufferedInputStream origin;
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
            byte data[] = new byte[BUFFER];
            for (File sourceFile : sourceFiles) {
                FileInputStream fi = new FileInputStream(sourceFile);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(folderName + "/" + sourceFile.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch(Exception e) {
            return null;
        }
        return destination;
    }

    public static boolean zipFileAtPath(String sourcePath, String toLocation) {
        // ArrayList<String> contentList = new ArrayList<String>();
        final int BUFFER = 2048;


        File sourceFile = new File(sourcePath);
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void zipSubFolder(ZipOutputStream out, File folder,
                                     int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                LogUtils.i("ZIP SUBFOLDER", "Relative Path : " + relativePath);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    public static String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }
}

