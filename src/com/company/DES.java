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

    public static BitSet permutatedChoice2(BitSet tableOf56bits){
        BitSet PC2_array = new BitSet(48);
        int []dependencies = {13,16,10,23,0,4,2,27,14,5,20,9,22,18,11,3,25,7,15,6,26,19,12,1,40,51,30,36,46,54,29,39,50,44,32,47,43,48,38,55,33,52,45,41,49,35,28,31};

        for(int i=0; i<48; i++){
            PC2_array.set(i,tableOf56bits.get(dependencies[i]));
        }

        return PC2_array;
    }

    public static BitSet leftShift(BitSet tableOf28bits ){
        BitSet LF_array = tableOf28bits.get(1,28);
        LF_array.set(27,tableOf28bits.get(0));
        for(int i =0; i < 28;i++){
            if(LF_array.get(i))
            System.out.print(1);
            else
             System.out.print(0);

        }
        return LF_array;
    }

    //9
    public static BitSet xor48bKeys(BitSet R, BitSet K)
    {
        BitSet key = new BitSet(48);
        for (int i = 0 ; i < 48 ; i++)
            key.set(i, R.get(i) ^ K.get(i));
        return key;
    }

    //10, 11, 12, 13
    public static BitSet decreaseKeyTo32b(BitSet key48b)
    {
        int column, row, index;
        BitSet resultBitSet = new BitSet(32);

        for (int i = 0 ; i < 8 ; i++)
        {
            index = i*6;

            column = (key48b.get(index) ? 10 : 0) + (key48b.get(index+5) ? 1 : 0);
            switch(column){
                case 11: column = 3; break;
                case 10: column = 2; break;
                case 01: column = 1; break;
                case 00: column = 0; break;
                default: column = 0; break;
            }

            row = (key48b.get(index+1) ? 1000 : 0) + (key48b.get(index+2) ? 100 : 0) + (key48b.get(index+3) ? 10 : 0) + (key48b.get(index+4) ? 1 : 0);
            switch (row){
                case 0000: row = 0; break;
                case 1111: row = 15; break;
                case 1110: row = 14; break;
                case 1101: row = 13; break;
                case 1100: row = 12; break;
                case 1011: row = 11; break;
                case 1010: row = 10; break;
                case 1001: row = 9; break;
                case 1000: row = 8; break;
                case 0111: row = 7; break;
                case 0110: row = 6; break;
                case 0101: row = 5; break;
                case 0100: row = 4; break;
                case 0011: row = 3; break;
                case 0010: row = 2; break;
                case 0001: row = 1; break;
                default: row = 0; break;
            }

            String resultBinStr = Integer.toBinaryString(Tables.S[i][row * 16 + column]);
            for (int j = 0 ; j < resultBinStr.length() ; j++)
            {
                if (resultBinStr.codePointAt(3-j) == '1')
                    resultBitSet.set(i*4+j);
            }
        }

        return resultBitSet;
    }

    //14
    public static BitSet permutate32bitKey(BitSet key32b)
    {
        BitSet result = new BitSet(32);

        for (int i = 0 ; i < 32 ; i++)
            result.set(i, key32b.get(Tables.P[i]));

        return result;
    }

    //15
    public static BitSet xorLR(BitSet R, BitSet L)
    {
        BitSet Rplus1 = new BitSet(32);
        for (int i = 0 ; i < 32 ; i++)
            Rplus1.set(i, R.get(i) ^ L.get(i));
        return Rplus1;
    }

    //18
    public static BitSet reversedInitialPermutation(BitSet tableOf64bits)
    {
        BitSet result = new BitSet(64);

        for (int i = 0 ; i < 32 ; i++)
            result.set(i, tableOf64bits.get(Tables.REVERSED_IP[i]));

        return result;
    }

    //konwerter longow na bitsety
    public static BitSet convert(long value) {
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    // nie działa
    public static BitSet paddingAdding(BitSet data){
        int bitSetModulo = data.size() % 64;

        data.set(data.size(),true);
        data.set(data.size(),data.size()+(63-bitSetModulo),false);
        return data;
    }

    //konwerter bitsetow na longi
    public static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }
}
