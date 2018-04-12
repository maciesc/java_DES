package com.company;

import java.util.BitSet;

public class Main {

    public static void main(String[] args) {
/*
        String filePath = "";
        BitSet bitSetData = FileManager.readBinaryFile(filePath);

        int bslength = bitSetData.size() / 64;
        int index = 0;
        for (int i = 0 ; i < bslength ; i++)
        {
            BitSet bitSetBlock = bitSetData.get(index, index + 64);
            //tu wywołać desa
            index += 64;
        }

        int bitSetModulo = bitSetData.size() % 64;
*/


        BitSet data = FileManager.readBinaryFile("C:/Users/Maciej/Desktop/Test1.bin");
        System.out.println(data);
        for(int i =0;i<data.size();i++)
            System.out.print(data.get(i)?1:0);
        System.out.println();

        BitSet xd = new BitSet(64);
        xd.set(0,8);
        xd.set(16,31);
        xd.set(32,58,false);
        xd.set(59);
        System.out.println(xd.length());
        FileManager.writeBinaryFile("C:/Users/Maciej/Desktop/Test1.bin",xd.toByteArray());






    }
}
