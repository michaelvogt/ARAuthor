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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

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

  public static String getFullPuplicFolderLocalUrl(String path) {
    return "file://" + getFullPuplicFolderFile(path).getAbsolutePath();
  }

  public static String readTextFile(String path) throws IOException {
    File filePath = FileUtils.getFullPuplicFolderFile(path);

    StringBuilder builder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        builder.append(line);
      }
    }

    return builder.toString();
  }

  public static String readTextFile(String filePath, String language) throws IOException {
    return readTextFile(internationalizePath(filePath, language));
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

  public static File getFullMarkerFolderFile() {
    return getFullPuplicFolderFile(MARKERS_PATH);
  }

  public static String getFullMarkerFolderPath() {
    return getFullPuplicFolderPath(MARKERS_PATH);
  }

  public static boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
  }

  public static List<String> getFilepathsOfFolder(String folderPath) {
    List<String> fileNames = new ArrayList<>();

    File directory = new File(folderPath);
    for (File file : directory.listFiles()) {
      fileNames.add(folderPath + "/" + file.getName());
    }

    return fileNames;
  }

  public static String getRelativePath(String path) {
    // TODO: Needs improvement
    return path.replace("/document//storage/emulated/0", "");
  }

  public static void saveImageToExternalStorage(Bitmap bitmap, String imagePath) {
    try {
      OutputStream stream = new FileOutputStream(FileUtils.getFullPuplicFolderFile(imagePath));
      bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
      stream.flush();
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String internationalizePath(String filePath, String language) {
    int insertPoint = filePath.lastIndexOf(".");
    return filePath.substring(0, insertPoint) + language + filePath.substring(insertPoint);
  }
}
