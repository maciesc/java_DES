package com.company;

import java.io.*;
import java.util.BitSet;

public class FileManager {
    private static int FILE_LENGTH;
    public static BitSet readBinaryFile(String filePath) {
        File inputFile = new File(filePath);
        byte[] byteData = new byte[(int) inputFile.length()];
        try {
            FILE_LENGTH = (int)inputFile.length()*8;
            FileInputStream fileInputStreams = new FileInputStream(inputFile);
            fileInputStreams.read(byteData, 0, byteData.length);
            fileInputStreams.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitSet data = BitSet.valueOf(byteData);


            int bitSetModulo = FILE_LENGTH % 64;
            System.out.println(FILE_LENGTH);
            data.set(FILE_LENGTH);
            data.set(FILE_LENGTH,FILE_LENGTH + (63-bitSetModulo),false);

        return data;
    }

    public static long getFILE_LENGTH() {return FILE_LENGTH;}

    public static void writeBinaryFile(String filePath, byte[] data) {
        File outputFile = new File(filePath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(data, 0, data.length);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generateOutputFileName(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        StringBuilder outputStringBuilder = new StringBuilder();
        outputStringBuilder.append(filePath);
        outputStringBuilder.insert(dotIndex, "_output");
        return outputStringBuilder.toString();
    }
}