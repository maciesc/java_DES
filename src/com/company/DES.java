package com.company;

import java.util.BitSet;

public final class DES {
    private static String message;
    public DES(){
        message = "DES Initialization";
    }
    public static BitSet initialPermutation(BitSet tableOf64bits){

        BitSet IP_array = new BitSet(64);
        int index = 0;
        //Dla parzystych
        for(int row = 57; row <= 63; row+=2)
            for (int column = row; column > 0; column -= 8)
                IP_array.set(index++, tableOf64bits.get(column));


        //Dla nieparzystych
        for(int row = 56; row <= 62; row+=2)
            for(int column = row; column > 0; column-=8)
                IP_array.set(index++,tableOf64bits.get(column));



        return IP_array;
    }

    public static BitSet expansionPermutation(BitSet tableOf32bits){

        BitSet EP_array = new BitSet(48);
        //EP_array.size();
        int index = 0, bitNumber = 31;
        for(int row = 0; row < 8; row++) {
            for(int column = 0; column < 6; column++) {
                if(bitNumber > 31)bitNumber = 0;
                EP_array.set(index,tableOf32bits.get(bitNumber));
                index++;
                bitNumber++;
                System.out.print(bitNumber+ " ");
            }
            bitNumber-=2;
            System.out.println("");
        }

        return EP_array;
    }

    public static BitSet permutatedChoice1(BitSet tableOf64bits){
        BitSet PC1_array = new BitSet(56);
        //LEFT
        int bitNumberRemember = 56, bitNumber = bitNumberRemember, index = 0;
        for(int row = 0; row <4; row++) {
            for(int column = 0; column < 7; column++) {

                PC1_array.set(index,tableOf64bits.get(bitNumber));
                if(bitNumber-8 < 0)bitNumber = ++bitNumberRemember;
                else bitNumber-=8;

            }
        }

        //RIGHT
        bitNumberRemember = 62;
        bitNumber = bitNumberRemember;
        for(int row = 0; row <4; row++) {
            for(int column = 0; column < 7; column++) {
                PC1_array.set(index,tableOf64bits.get(bitNumber));
                if(bitNumber-8 < 0){
                    if(bitNumber==4)
                        bitNumber = 27;
                    else
                        bitNumber = --bitNumberRemember;
                }
                else bitNumber-=8;

            }
        }

        return PC1_array;
    }

    public static  BitSet permutatedChoice2(BitSet tableOf56bits){
        BitSet PC2_array = new BitSet(48);
        int []dependencies = {13,16,10,23,0,4,2,27,14,5,20,9,22,18,11,3,25,7,15,6,26,19,12,1,40,51,30,36,46,54,29,39,50,44,32,47,43,48,38,55,33,52,45,41,49,35,28,31};

        for(int i=0; i<48; i++){
            PC2_array.set(i,tableOf56bits.get(dependencies[i]));
        }

        return PC2_array;
    }
    public static void showMessage(){
        System.out.println(message);
    }

}
