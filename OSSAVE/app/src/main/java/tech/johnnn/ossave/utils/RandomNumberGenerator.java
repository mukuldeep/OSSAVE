package tech.johnnn.ossave.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import tech.johnnn.ossave.crypto.CryptoUtils;


public class RandomNumberGenerator {
    public static int generateSecureRandomInt(){
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            int randomInt = secureRandom.nextInt();
            //Log.d("RNG","Random Number: " + randomInt);
            return randomInt;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static byte[] generateSecureRandomBytes(int nBytes){
        byte[] randomBytes = new byte[nBytes];
        int pointer = 0;
        for(int i=0;i<nBytes;i++){
            int rn = generateSecureRandomInt();
            if(rn == 0){
                return null;
            }
            byte[] rnbytes = CryptoUtils.intToBytes(rn);
            //Log.d("RNG:",CryptoUtils.byteArrayToHexString(rnbytes));
            for(int j=0;j<rnbytes.length;j++){
                randomBytes[pointer] = rnbytes[j];
                pointer++;
                if(pointer == nBytes){
                    //Log.d("RNG",String.valueOf(nBytes)+"Byte "+CryptoUtils.byteArrayToHexString(randomBytes));
                    return randomBytes;
                }
            }
        }
        return null;
    }

}

