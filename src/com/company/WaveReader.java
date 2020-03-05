package com.company;

import java.io.*;
import java.util.Arrays;

public class WaveReader {
    private static final int HEADER_SIZE = 44;
    public  WavHeader header = new WavHeader();
    private byte[] buf = new byte[HEADER_SIZE];

    public WavHeader read() throws IOException {
//        int res = inputStream.read(buf);
//        if (res != HEADER_SIZE) {
//            throw new IOException("Could not read header.");
//        }
        header.setChunkID(Arrays.copyOfRange(buf, 0, 4));
        if (new String(header.getChunkID()).compareTo("RIFF") != 0) {
            throw new IOException("Illegal format.");
        }
        int i= ((byte) buf[24]<<24)&0xff000000|
                (buf[25]<<16)&0x00ff0000|
                (buf[26]<< 8)&0x0000ff00;
        System.out.println(i);
        int ii= ((byte) buf[24]<<24)|
                (buf[25]<<16)|
                (buf[26]<< 8);
        System.out.println(ii);

        header.setChunkSize(toInt(4, false));
        header.setFormat(Arrays.copyOfRange(buf, 8, 12));
        header.setSubChunk1ID(Arrays.copyOfRange(buf, 12, 16));
        header.setSubChunk1Size(toInt(16, false));
        header.setAudioFormat(toShort(20, false));
        header.setNumChannels(toShort(22, false));
      // header.setSampleRate ((int) (Arrays.copyOfRange(buf, 24, 27));
       // header.setSampleRate(toInt(24, false));
        header.setSampleRate(i);
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
    public static byte[] getHeaderBuf(byte[] arr){
        byte[] header = new byte[44];
       header= Arrays.copyOfRange(arr, 0, 44);
//        for (int i = 0; i < 44; i++){
//            header[i] = arr[i];
//        }
        return header;
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


    public  void wavload() {
        try {
            File f = new File("C:\\Users\\ATN_70\\IdeaProjects\\AA_Wave_Command\\test.wav");
            byte[] origfile = getBytes(f);

            for(byte test :origfile){
             //   System.out.println(test);
            }
            buf=getHeaderBuf(origfile);
        }catch (Exception e){
            System.out.println(e.getMessage());

        }

    }
}
