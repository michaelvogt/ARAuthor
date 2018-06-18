package eu.michaelvogt.ar.author.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import static android.os.Environment.getExternalStorageDirectory;

public class FileUtils {
    public static final String MARKERS_PATH = "/Touristar/Markers/";
    public static final String HIDAKA_PATH = "/Touristar/Hidaka/";

    public static File getFullPuplicFolderFile(String path) {
        return new File(getExternalStorageDirectory(), path);
    }

    public static String getFullPuplicFolderPath(String path) {
        return getFullPuplicFolderFile(path).getAbsolutePath();
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        try (FileChannel source = new FileInputStream(sourceFile).getChannel();
             FileChannel destination = new FileOutputStream(destFile).getChannel()) {
            destination.transferFrom(source, 0, source.size());
        }
    }

    public static File getFullMarkerFolderFile() { return getFullPuplicFolderFile(MARKERS_PATH); }

    public static String getFullMarkerFolderPath() { return getFullPuplicFolderPath(MARKERS_PATH); }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static void saveImageToExternalStorage(Bitmap bitmap, String imagePath) {
        try {
            OutputStream stream = new FileOutputStream(FileUtils.getFullPuplicFolderFile(imagePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
