package com.company;

import java.util.Arrays;
import java.util.BitSet;

public final class DES {
    private static String message;
    public DES(){
        message = "DES Initialization";
    }

    public static void encrypt(String fileName,BitSet key){
        byte [] input = FileManager.readBinaryFile(fileName,true); //nadpisywanie inputa -> mniej pamięci

        BitSet copyKey = (BitSet) key.clone();
        for(int i = 0; i < input.length ; i+=8){

            BitSet temporary = DES.initialPermutation(BitSet.valueOf(Arrays.copyOfRange(input,i,i+8)));
            BitSet left = temporary.get(0,32);
            BitSet right = temporary.get(32,64);

            copyKey = DES.permutatedChoice1(copyKey);
            BitSet leftCopyKey = copyKey.get(0,28);
            BitSet rigthCopyKey = copyKey.get(28,56);

            for(int j = 0; j < 16 ; j++){

                if(j == 0 || j == 1 || j == 8 || j == 15) {
                    leftCopyKey = DES.leftShift(leftCopyKey);
                    rigthCopyKey = DES.leftShift(rigthCopyKey);
                }
                else{

                    leftCopyKey = DES.leftShift(leftCopyKey);
                    rigthCopyKey = DES.leftShift(rigthCopyKey);
                    leftCopyKey = DES.leftShift(leftCopyKey);
                    rigthCopyKey = DES.leftShift(rigthCopyKey);
                }

                BitSet outputKey = append2BitSets(leftCopyKey, rigthCopyKey,28);

                outputKey = permutatedChoice2(outputKey);

                BitSet expandedRight = expansionPermutation(right);
                expandedRight.xor(outputKey);


                BitSet right32b = decreaseKeyTo32b(expandedRight);
                right32b = permutate32bitKey(right32b);
                left.xor(right32b);

                BitSet pom = left;
                left = right;
                right = pom;
            }
            //zmiana
            BitSet almostFinished = append2BitSets(right,left,32);
            almostFinished = reversedInitialPermutation(almostFinished);

            byte[] array = almostFinished.toByteArray();

               for(int k = 0; k < array.length; k++){
                   input[i+k] = array[k];  //nadpisywanie inputa -> mniej pamięci
           }
                for(int k = array.length;k<8;k++){
                   input[i+k] = 0;
                }
        }
        FileManager.writeBinaryFile("C:\\Users\\Maciej\\Desktop\\DESoutput.bin",input,input.length);
    }

    public static void decrypt(String fileName,BitSet key){
        byte [] input = FileManager.readBinaryFile(fileName,false); //nadpisywanie inputa -> mniej pamięci
        BitSet copyKey = (BitSet) key.clone();
        for(int i = 0; i < input.length ; i+=8){

            BitSet temporary = DES.initialPermutation(BitSet.valueOf(Arrays.copyOfRange(input,i,i+8)));
            BitSet left = temporary.get(0,32);
            BitSet right = temporary.get(32,64);

            copyKey = DES.permutatedChoice1(copyKey);
            BitSet leftCopyKey = copyKey.get(0,28);
            BitSet rigthCopyKey = copyKey.get(28,56);
            //leftCopyKey = leftShift32X(leftCopyKey);
            //rigthCopyKey = leftShift32X(rigthCopyKey);

            for(int j = 15; j >= 0 ; j--){

                if(j == 0 || j == 1 || j == 8 || j == 15) {
                    leftCopyKey = DES.rightShift(leftCopyKey);
                    rigthCopyKey = DES.rightShift(rigthCopyKey);
                }
                else{

                    leftCopyKey = DES.rightShift(leftCopyKey);
                    rigthCopyKey = DES.rightShift(rigthCopyKey);
                    leftCopyKey = DES.rightShift(leftCopyKey);
                    rigthCopyKey = DES.rightShift(rigthCopyKey);
                }

                BitSet outputKey = append2BitSets(leftCopyKey, rigthCopyKey,28);
                outputKey = permutatedChoice2(outputKey);

                BitSet expandedRight = expansionPermutation(right);
                expandedRight.xor(outputKey);


                BitSet right32b = decreaseKeyTo32b(expandedRight);
                right32b = permutate32bitKey(right32b);
                left.xor(right32b);

                BitSet pom = right;
                right = left;
                left = pom;
            }
            BitSet almostFinished = append2BitSets(right,left,32);
            almostFinished = reversedInitialPermutation(almostFinished);

            byte[] array = almostFinished.toByteArray();

            for(int k = 0; k < array.length; k++){
                input[i+k] = array[k];  //nadpisywanie inputa -> mniej pamięci
            }
            for(int k = array.length;k<8;k++){
                input[i+k] = 0;
            }
        }
        int index = 0;
        for(int i = input.length-1; i >=0; i--){
            if(input[i]==1)
            {
                index = i;
                break;
            }
        }
        FileManager.writeBinaryFile("C:\\Users\\Maciej\\Desktop\\DESdecryptOutput.bin",input,input.length);
    }

    private static BitSet append2BitSets(BitSet firstHalf, BitSet secondHalf,int numberOfBits) {
        BitSet outputKey = new BitSet(numberOfBits*2);
        for(int index = 0; index < numberOfBits; index++)
            outputKey.set(index,firstHalf.get(index));
        for(int index = 0; index < numberOfBits; index++)
            outputKey.set(index+numberOfBits,secondHalf.get(index));
        return outputKey;
    }

    //2
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

    //4
    public static BitSet permutatedChoice1(BitSet tableOf64bits){
        BitSet PC1_array = new BitSet(56);
        //LEFT
        int bitNumberRemember = 56, bitNumber = bitNumberRemember, index = 0;
        for(int row = 0; row <4; row++) {
            for(int column = 0; column < 7; column++) {

                PC1_array.set(index++,tableOf64bits.get(bitNumber));
                if(bitNumber-8 < 0)bitNumber = ++bitNumberRemember;
                else bitNumber-=8;

            }
        }

        //RIGHT
        bitNumberRemember = 62;
        bitNumber = bitNumberRemember;
        for(int row = 0; row <4; row++) {
            for(int column = 0; column < 7; column++) {
                PC1_array.set(index++,tableOf64bits.get(bitNumber));
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

    //6
    public static BitSet leftShift(BitSet tableOf28bits ){
        BitSet LF_array = tableOf28bits.get(1,28);
        LF_array.set(27,tableOf28bits.get(0));
        return LF_array;
    }

    public static BitSet rightShift(BitSet tableOf28bits){
        BitSet R_array = new BitSet(28);
        for(int i = 1; i < 28; i++)
            R_array.set(i,tableOf28bits.get(i-1));
        R_array.set(0,tableOf28bits.get(27));
        return R_array;
    }

    public static BitSet leftShift32X(BitSet tableOf28bits){
        BitSet LF_array = tableOf28bits;
        for(int i = 0; i < 4; i++)
        LF_array= leftShift(LF_array);
        return LF_array;
    }
    //7
    public static BitSet permutatedChoice2(BitSet tableOf56bits){
        BitSet PC2_array = new BitSet(48);
        int []dependencies = {13,16,10,23,0,4,2,27,14,5,20,9,22,18,11,3,25,7,15,6,26,19,12,1,40,51,30,36,46,54,29,39,50,44,32,47,43,48,38,55,33,52,45,41,49,35,28,31};

        for(int i=0; i<48; i++){
            PC2_array.set(i,tableOf56bits.get(dependencies[i]));
        }

        return PC2_array;
    }

    //8
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

            }
            bitNumber-=2;

        }

        return EP_array;
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
                case 1: column = 1; break;
                case 0: column = 0; break;
                default: column = 0; break;
            }

            row = (key48b.get(index+1) ? 1000 : 0) + (key48b.get(index+2) ? 100 : 0) + (key48b.get(index+3) ? 10 : 0) + (key48b.get(index+4) ? 1 : 0);
            switch (row){
                case 0: row = 0; break;
                case 1111: row = 15; break;
                case 1110: row = 14; break;
                case 1101: row = 13; break;
                case 1100: row = 12; break;
                case 1011: row = 11; break;
                case 1010: row = 10; break;
                case 1001: row = 9; break;
                case 1000: row = 8; break;
                case 111: row = 7; break;
                case 110: row = 6; break;
                case 101: row = 5; break;
                case 100: row = 4; break;
                case 11: row = 3; break;
                case 10: row = 2; break;
                case 1: row = 1; break;
                default: row = 0; break;
            }
            int[][] localS = Tables.getS();
            String resultBinStr = Integer.toBinaryString(localS[i][row  + column * 16]);
            for (int j = 0 ; j < resultBinStr.length() ; j++)
            {
                //TU to moze nie dzialac
                if (resultBinStr.codePointAt(j) == '1')
                    resultBitSet.set(i*4+j + 4-resultBinStr.length());
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

        for (int i = 0 ; i < 64 ; i++)
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
