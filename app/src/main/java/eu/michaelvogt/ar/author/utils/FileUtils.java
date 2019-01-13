/*
    ARTester - AR for tourists by tourists
    Copyright (C) 2018, 2019  Michael Vogt

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
import java.io.FileDescriptor;
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
  public static final String THUMBS_PATH = "/Touristar/Thumbs/";
  public static final String INTROS_PATH = "/Touristar/Intros/";

  public static File getFullPuplicFolderFile(String path) {
    return new File(getExternalStorageDirectory(), path);
  }

  public static String getFullPuplicFolderPath(String path) {
    return getFullPuplicFolderFile(path).getAbsolutePath();
  }

  public static String getFullPuplicFolderLocalUrl(String path) {
    return "file://" + getFullPuplicFolderFile(path).getAbsolutePath();
  }

  public static boolean publicPathExists(String path) {
    String fullPath = getFullPuplicFolderPath(path);
    return new File(fullPath).exists();
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

  public static void copyFiles(File sourceFile, File destFile) {
    if (!destFile.getParentFile().exists())
      destFile.getParentFile().mkdirs();

    try (FileChannel source = new FileInputStream(sourceFile).getChannel();
         FileChannel destination = new FileOutputStream(destFile).getChannel()) {
      if (!destFile.exists()) {
        destFile.createNewFile();
      }

      destination.transferFrom(source, 0, source.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copyFiles(FileDescriptor readDescriptor, File destFile) {
    if (!destFile.getParentFile().exists())
      destFile.getParentFile().mkdirs();

    try (FileChannel source = new FileInputStream(readDescriptor).getChannel();
         FileChannel destination = new FileOutputStream(destFile).getChannel()) {
      if (!destFile.exists()) {
        destFile.createNewFile();
      }

      destination.transferFrom(source, 0, source.size());
    } catch (IOException e) {
      e.printStackTrace();
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

  public static void saveImageToExternalStorage(Bitmap bitmap, String imagePath) {
    try {
      if (new File(imagePath).isDirectory()) {
        throw new IllegalArgumentException("Please provide imagePath to a file, not a directory: " + imagePath);
      }

      File fullPuplicFolderFile = FileUtils.getFullPuplicFolderFile(imagePath);
      fullPuplicFolderFile.getParentFile().mkdirs();

      OutputStream stream = new FileOutputStream(fullPuplicFolderFile);
      bitmap.compress(Bitmap.CompressFormat.WEBP, 100, stream);
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
