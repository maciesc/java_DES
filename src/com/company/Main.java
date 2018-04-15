package com.company;

import java.util.BitSet;

public class Main {

    public static void main(String[] args) {

        BitSet xd = new BitSet(64);
        byte[] xdb = {43,54,86,-10,-64,100,1,43};
        xd = BitSet.valueOf(xdb);

        DES.encrypt("C:\\Users\\Maciej\\Desktop\\BSK\\3 i 4\\JD.bin",xd);
        DES.decrypt("C:\\Users\\Maciej\\Desktop\\DESoutput.bin",xd);

    }
}
