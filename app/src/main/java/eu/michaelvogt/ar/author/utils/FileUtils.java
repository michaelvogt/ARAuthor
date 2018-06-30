/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018  Michael Vogt

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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
