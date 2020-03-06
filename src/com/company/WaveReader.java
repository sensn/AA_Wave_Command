package com.company;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WaveReader {
    private byte[] origfile;
    private byte[] buf = new byte[HEADER_SIZE];
    private byte[] rawPCMdata;
    private byte[] reversedRawPCMdata;
    private byte[] trimedRawPCMdata;
    private byte[] editedRawPCMdata;
    private byte[] editedWaveFile;
    private byte[] sr;
    private static final int HEADER_SIZE = 44;
    public  WavHeader header = new WavHeader();


    public WavHeader read() throws IOException {
//        int res = inputStream.read(buf);
//        if (res != HEADER_SIZE) {
//            throw new IOException("Could not read header.");
//        }
        header.setChunkID(Arrays.copyOfRange(buf, 0, 4));
        if (new String(header.getChunkID()).compareTo("RIFF") != 0) {
            throw new IOException("Illegal format.");
        }
        int samplrate = ((buf[27] & 0xFF) << 24) | ((buf[26] & 0xFF) << 16)    //Get samplerate: Convert Byte array to Int - reversed order.
                | ((buf[25] & 0xFF) << 8) | (buf[24] & 0xFF);
       // System.out.println("iii: "+iii);
     //   System.out.println(new BigInteger(1,Arrays.copyOfRange(buf, 23, 27)).intValue());
      //  System.out.println(new BigInteger(sr).intValue());

        header.setChunkSize(toInt(4, false));
        header.setFormat(Arrays.copyOfRange(buf, 8, 12));
        header.setSubChunk1ID(Arrays.copyOfRange(buf, 12, 16));
        header.setSubChunk1Size(toInt(16, false));
        header.setAudioFormat(toShort(20, false));
        header.setNumChannels(toShort(22, false));
        //header.setSampleRate ((int) (Arrays.copyOfRange(buf, 24, 27));    44100 == 0x00ac44, 4500480 == 0x44ac00
        //header.setSampleRate(toInt(24, false));    //ORIG
        //header.setSampleRate(new BigInteger(sr).intValue());   //WORKS
        header.setSampleRate(samplrate);                   //WORKS
        header.setByteRate(toInt(28, false));
        header.setBlockAlign(toShort(32, false));
        header.setBitsPerSample(toShort(34, false));
        header.setSubChunk2ID(Arrays.copyOfRange(buf, 36, 40));
        header.setSubChunk2Size(toInt(40, false));
        return header;
    }

    private int toInt(int start, boolean endian) {
        int k = (endian) ? 1 : -1;
        if (!endian) {
            start += 3;
        }
        return (buf[start] << 24) + (buf[start + k * 1] << 16) +
                (buf[start + k * 2] << 8) + buf[start + k * 3];
    }

    private short toShort(int start, boolean endian) {
        short k = (endian) ? (short) 1 : -1;
        if (!endian) {
            start++;
        }
        return (short) ((buf[start] << 8) + (buf[start + k * 1]));
    }
    //
    public void trim(int startsamp,int endsamp){
    trimRawPCMData(editedRawPCMdata,startsamp,endsamp);
        editedRawPCMdata= new byte [trimedRawPCMdata.length];
        editedRawPCMdata=Arrays.copyOfRange(trimedRawPCMdata,0, rawPCMdata.length);
    }

    public void reverse() {
        reverseRawPCMData(editedRawPCMdata);
        editedRawPCMdata= new byte [reversedRawPCMdata.length];
        editedRawPCMdata=Arrays.copyOfRange(reversedRawPCMdata,0, rawPCMdata.length);
    }




    public  byte[] getHeaderBuf(byte[] arr){
        byte[] header = new byte[44];
         sr = new byte[4];                             // buffer for Sample rate Chunk
       header= Arrays.copyOfRange(arr, 0, 44);

        for (int i = 27,j=0; i > 23; i--,j++){
            sr[j] = header[i];                 // get Samplerate Chunk Reversed order
        }
        return header;
    }

    public byte[] getRawPCMData(byte[] arr){
        byte[] rawPCMData = new byte[arr.length];
        rawPCMData = Arrays.copyOfRange(arr, 44, arr.length);
        return rawPCMData;
    }

    public void trimRawPCMData(byte[] theRawPCMData ,int startsamp, int endsamp){
        trimedRawPCMdata=Arrays.copyOfRange(theRawPCMData, startsamp,endsamp);
    }

    public  byte[] reverseRawPCMData(byte[] arr){
        int len = arr.length;
        byte[] flipArray = new byte[arr.length];
        reversedRawPCMdata=new byte[arr.length];

         List<byte[]> byteList = Arrays.asList( arr);
         Collections.reverse(byteList);  // Reversing list will also reverse the array
//        reversedRawPCMdata=arr;
//       for(int i = len-1, j = 0; i >= 0; i--, j++){      //Write it in reversed order
//            flipArray[j] = arr[i];
//           reversedRawPCMdata[j] = arr[i];
//        }

        int bytesPerSample=4;              //https://medium.com/swlh/reversing-a-wav-file-in-c-482fc3dfe3c4
        int sampleIdentifier = 0;
        for (int i = 0; i < len; i++)
        {
            if (i != 0 && i % bytesPerSample == 0)
            {
                sampleIdentifier += 2 * bytesPerSample;
            }
            int index = len - bytesPerSample - sampleIdentifier + i;
            reversedRawPCMdata[i] =  arr[index];
        }


//4 bytes
     //   You actually need to reverse the frames, where the frame size is dependent on the bytes-per-sample and whether the file is stereo or mono (for example, if the file is stereo and 2 bytes per sample, then each frame is 4 bytes).

        return flipArray;
    }

    public void  createWaveFile(){
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(buf); //header Data
            outputStream.write(editedRawPCMdata);
            editedWaveFile = outputStream.toByteArray();
//            File dstFile = new File("dst.wav");
//            FileOutputStream out = new FileOutputStream(dstFile);
//
//            int len;
//            while ((len = in.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//
//            in.close();
//            out.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileOutputStream stream = new FileOutputStream("C:\\Users\\ATN_70\\IdeaProjects\\AA_Wave_Command\\dest4.wav")) {
            stream.write(editedWaveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
      //  FileUtils.writeByteArrayToFile(new File("pathname"), myByteArray)
    }

    public static byte[] getBytes(File f) throws FileNotFoundException, IOException{
        byte[] buffer = new byte[(int) f.length()];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileInputStream fs = new FileInputStream(f);
        int read;
        while((read = fs.read(buffer)) != -1){
            os.write(buffer, 0, read);
        }
        fs.close();
        os.close();
        return os.toByteArray();
    }


    public  void wavload(String filename) {
        try {
            File f = new File(filename);
            //File f = new File("C:\\Users\\ATN_70\\IdeaProjects\\AA_Wave_Command\\test.wav");
             origfile = getBytes(f);

            for(byte test :origfile){
             //   System.out.println(test);
            }
            buf=getHeaderBuf(origfile);    //get Header Bytes (44)
            rawPCMdata=getRawPCMData(origfile);
            editedRawPCMdata= new byte [rawPCMdata.length];
            editedRawPCMdata=getRawPCMData(origfile);

        }catch (Exception e){
            System.out.println(e.getMessage());

        }

    }


}
