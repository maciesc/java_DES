package com.company;

import java.util.Arrays;
import java.util.BitSet;

public final class DES {

    public static BitSet[] keygenerator(BitSet key){
        BitSet [] keyTable = new BitSet[16];

        BitSet copyKey = DES.permutatedChoice1(key);
        //Main.debugXD("56bit",copyKey,7);

        BitSet leftCopyKey = copyKey.get(0,28);
        //Main.debugXD("C0",leftCopyKey,7);
        BitSet rigthCopyKey = copyKey.get(28,56);
        //Main.debugXD("D0",rigthCopyKey,7);




        for(int j = 0; j < 16 ; j++) {

            if (j == 0 || j == 1 || j == 8 || j == 15) {
                leftCopyKey = DES.leftShift(leftCopyKey);
                rigthCopyKey = DES.leftShift(rigthCopyKey);
            } else {

                leftCopyKey = DES.leftShift(leftCopyKey);
                rigthCopyKey = DES.leftShift(rigthCopyKey);
                leftCopyKey = DES.leftShift(leftCopyKey);
                rigthCopyKey = DES.leftShift(rigthCopyKey);
            }

            //Main.debugXD("C"+(j+1),leftCopyKey,28);
            //Main.debugXD("D"+(j+1),rigthCopyKey,28);
            keyTable[j] = append2BitSets(leftCopyKey, rigthCopyKey,28);
        }
        return keyTable;
    }
    public static void encrypt(String fileName,BitSet key){
        byte [] input = FileManager.readBinaryFile(fileName,true); //nadpisywanie inputa -> mniej pamięci
        BitSet[] keyTable = keygenerator(key);

        for(int i = 0; i < input.length ; i+=8){

            byte []blockOf64bits = Arrays.copyOfRange(input,i,i+8);

            BitSet inputBitSet = BitSet.valueOf(blockOf64bits);

            /*BitSet convertedInput = new BitSet(64);
            for(int k = 0;k <8;k++){
                int d=k*8;
                boolean position0 =inputBitSet.get(d);
                boolean position1 =inputBitSet.get((d+1));
                boolean position2 =inputBitSet.get(d+2);
                boolean position3 =inputBitSet.get((d+3));
                convertedInput.set(d,inputBitSet.get((d)+7));
                convertedInput.set(d+1,inputBitSet.get((d)+6));
                convertedInput.set(d+2,inputBitSet.get((d)+5));
                convertedInput.set(d+3,inputBitSet.get((d)+4));
                convertedInput.set(d+4,position3);
                convertedInput.set(d+5,position2);
                convertedInput.set(d+6,position1);
                convertedInput.set(d+7,position0);
            }
            */
            BitSet temporary = DES.initialPermutation(inputBitSet);
            

            BitSet left = temporary.get(0,32);
            BitSet right = temporary.get(32,64);

            if(left.size()==0)left = new BitSet(32);
            if(right.size()==0)right = new BitSet(32);

            for(int j = 0; j < 16 ; j++){

                BitSet outputKey = keyTable[j];
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
        FileManager.writeBinaryFile("C:\\Users\\Maciej\\Desktop\\encrypted.bin",input,input.length);
    }

    public static void decrypt(String fileName,BitSet key){
        byte [] input = FileManager.readBinaryFile(fileName,false); //nadpisywanie inputa -> mniej pamięci
        //BitSet copyKey = (BitSet) key.clone();
        BitSet[] keyTable = keygenerator(key);
        for(int i = 0; i < input.length ; i+=8){

            BitSet temporary = DES.initialPermutation(BitSet.valueOf(Arrays.copyOfRange(input,i,i+8)));
            BitSet left = temporary.get(0,32);
            BitSet right = temporary.get(32,64);

            for(int j = 0; j < 16 ; j++){

                BitSet outputKey = keyTable[15-j];
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
        int index = input.length;
        for(int i = input.length-1; i >=0; i--){
            if(input[i]==1)
            {
                index = i;
                break;
            }
        }
        FileManager.writeBinaryFile("C:\\Users\\Maciej\\Desktop\\decrypted.bin",input,index);
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

        BitSet result = new BitSet(64);

        for (int i = 0 ; i < 64 ; i++)
            result.set(i, tableOf64bits.get(Tables.IP[i]-1));

        return result;
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
                //Napewno dziala testowalem

                if (resultBinStr.codePointAt(j) == '1') {
                    resultBitSet.set(i * 4 + j + 4 - resultBinStr.length());
                }

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

    //18
    public static BitSet reversedInitialPermutation(BitSet tableOf64bits)
    {
        BitSet result = new BitSet(64);

        for (int i = 0 ; i < 64 ; i++)
            result.set(i, tableOf64bits.get(Tables.REVERSED_IP[i]-1));

        return result;
    }

}
