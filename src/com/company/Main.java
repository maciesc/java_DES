package com.company;

import java.util.BitSet;

public class Main {

    public static void main(String[] args) {

        BitSet xd = new BitSet(64);


        DES.permutatedChoice2(xd);
        DES.showMessage();
    }
}
