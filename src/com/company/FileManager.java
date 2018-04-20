package com.company;

import java.io.*;
import java.util.Arrays;
import java.util.BitSet;

public class FileManager {

    public static byte[] readBinaryFile(String filePath, boolean ifEncrypt) {
        File inputFile = new File(filePath);

        byte[] byteData;
        if(ifEncrypt)
            byteData = new byte[(int) inputFile.length() + 8 - ((int)inputFile.length() % 8)];
        else
            byteData =new byte[(int) inputFile.length()];
        try {

            FileInputStream fileInputStreams = new FileInputStream(inputFile);
            fileInputStreams.read(byteData, 0, byteData.length);
            fileInputStreams.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(ifEncrypt){
        byte[]paddingArray = padding((int)inputFile.length());
        for(int i = (int) inputFile.length(),j = 0; i < byteData.length; i++,j++)
        {
            byteData[i]= paddingArray[j];
        }}
        return byteData;
    }

    public static byte[] padding(int byteDataLength){

        byteDataLength = 8 - (byteDataLength % 8);
        byte [] padding = new byte [byteDataLength];

        padding[0] = 1;
        for(int i = 1; i < byteDataLength; i++)
        {
            padding[i] = 0;
        }

        return padding;
    }

    public static void writeBinaryFile(String filePath, byte[] data,int index) {
        File outputFile = new File(filePath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.write(data, 0, index);
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

    public static BitSet getKey(String filePath){
        File inputFile = new File(filePath);

        byte[] byteData = new byte[(int)inputFile.length()];
        try {

            FileInputStream fileInputStreams = new FileInputStream(inputFile);
            fileInputStreams.read(byteData, 0, byteData.length);
            fileInputStreams.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte []blockOf64bits =  Arrays.copyOfRange(byteData,0,8);

        BitSet key = BitSet.valueOf(blockOf64bits);


        return key;
    }
}