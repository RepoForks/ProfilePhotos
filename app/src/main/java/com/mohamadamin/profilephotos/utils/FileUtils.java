package com.mohamadamin.profilephotos.utils;

import java.io.File;

public class FileUtils {

    final static String VIBER_DIRECTORY = "viber/media/User photos/",
                        TELEGRAM_DIRECTORY="Android/data/org.telegram.messenger/cache/",
                        WHATSAPP_DIRECTORY="WhatsApp/Profile Pictures/",
                        LINE_DIRECTORY="Android/data/jp.naver.line.android/storage/p/";

    public static File[] getViberFiles() {
        File[] internalFiles = getFilesArray(getExternalStoragePath(), VIBER_DIRECTORY);
        File[] externalFiles = getFilesArray(getSdCardStoragePath(), VIBER_DIRECTORY);
        return concatArrays(internalFiles, externalFiles);
    }

    public static File[] getLineFiles() {
        File[] internalFiles = getFilesArray(getExternalStoragePath(), LINE_DIRECTORY);
        File[] externalFiles = getFilesArray(getSdCardStoragePath(), LINE_DIRECTORY);
        return concatArrays(internalFiles, externalFiles);
    }

    public static File[] getWhatsAppFiles() {
        File[] internalFiles = getFilesArray(getExternalStoragePath(), WHATSAPP_DIRECTORY);
        File[] externalFiles = getFilesArray(getSdCardStoragePath(), WHATSAPP_DIRECTORY);
        return concatArrays(internalFiles, externalFiles);
    }

    public static File[] getTelegramFiles() {
        File[] internalFiles = getFilesArray(getExternalStoragePath(), TELEGRAM_DIRECTORY);
        File[] externalFiles = getFilesArray(getSdCardStoragePath(), TELEGRAM_DIRECTORY);
        return concatArrays(internalFiles, externalFiles);
    }

    public static File[] getFilesArray(String storagePath, String directory) {
        if (storagePath == null || directory == null) return null;
        String filePath = storagePath;
        if (!filePath.endsWith("/")) filePath += "/";
        filePath += directory;
        File file = new File(filePath);
        return file.listFiles();
    }

    public static String getExternalStoragePath() {
        return System.getenv("EXTERNAL_STORAGE");
    }

    public static String getSdCardStoragePath() {
        return System.getenv("SECONDARY_STORAGE");
    }

    public static File[] concatArrays(File[] a, File[] b) {

        if (a == null) return b;
        else if (b == null) return a;

        int aLen = a.length;
        int bLen = b.length;
        File[] c = new File[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;

    }

}
