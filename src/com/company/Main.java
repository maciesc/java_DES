package com.company;

import java.util.BitSet;

public class Main {

    public static void main(String[] args) {

        /*
        BitSet xd = new BitSet(64);
        byte[] xdb = {43,54,86,-10,-64,100,1,43};
        xd = BitSet.valueOf(xdb);
        BitSet key = new BitSet(64);

        key.set(3);
        key.set(6);
        key.set(7);

        key.set(10);
        key.set(11);
        key.set(13);

        key.set(17);
        key.set(19);
        key.set(21);
        key.set(22);
        key.set(23);

        key.set(25);
        key.set(26);
        key.set(27);
        key.set(28);
        key.set(31);

        key.set(32);
        key.set(35);
        key.set(36);
        key.set(38);
        key.set(39);
        key.set(40);
        key.set(42);
        key.set(43);
        key.set(44);
        key.set(45);
        key.set(48);
        key.set(49);
        key.set(51);
        key.set(52);
        key.set(53);
        key.set(54);
        key.set(55);
        key.set(56);
        key.set(57);
        key.set(58);
        key.set(59);
        key.set(63);


        //byte[] message = {1,23,45,67,89,0xA,0xB,0xC,0xD,0xE,0xF};
        //FileManager.writeBinaryFile("C:\\Users\\Maciej\\Desktop\\dataTotest.bin",message,message.length);


        DES.encrypt("C:\\Users\\Maciej\\Desktop\\BSK\\3 i 4\\JD.bin",key);
        DES.decrypt("C:\\Users\\Maciej\\Desktop\\encrypted.bin",key);
        */
        Menu.printMenu();

    }
    public static void debug(String message,BitSet key,int modulo){
        System.out.println(message);
        for(int i = 0;i<key.size();i++){
            System.out.print(key.get(i)?1:0);
            if (i % modulo == modulo-1) {
                System.out.print(" ");
            }

        }
        System.out.println();
    }
}
