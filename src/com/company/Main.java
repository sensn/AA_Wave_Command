package com.company;
import com.beust.jcommander.JCommander;   // Jcommander.org
import com.beust.jcommander.Parameter;    // "Because life is too short to parse command line parameters" - Cédric Beust

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main {
    private WaveReader waveReader;
    private static WaveReader waveReader2;
    private static String filename;
    private static boolean cutme= false;

    private static int cutlength=0;
    private static boolean reverseme=false;
    private Map<Boolean,Integer> OrderofParameters = new HashMap<Boolean,Integer>();

    @Parameter(description = "Files")
    private List<String> files = new ArrayList<>();
    @Parameter(names={"--cut", "-c"}, validateWith = PositiveInteger.class)
    int length;
 //   @Parameter(names={"--reverse", "-r"}, validateWith = PositiveInteger.class)
 //   int pattern;
    @Parameter(names={"--reverse", "-r"}, description = "Reverse File")
    private boolean reverse = false;
    public static void main(String ... argv) throws IOException {
//        Main main = new Main();
//        JCommander.newBuilder()
//                .addObject(main)
//                .build()
//                .parse(argv);

        waveReader2 = new WaveReader();
//        for(String s : argv) {
//            if (s.contains(".wav")) {
//                filename = s;
//                System.out.println(s);
//                waveReader2.wavload(filename);
//            }
//            if (s.contains("--cut") || s.contains("-c)")) {
//                System.out.println(s);
//                //waveReader2.trim();
//            }
//            if (s.contains("--reverse") || s.contains("-r)")) {
//                System.out.println(s);
//                //waveReader2.trim();
//            }
//        }
        for (int i = 0; i < argv.length; i++) {
            if (argv[i].contains(".wav")) {
                filename = argv[i];
                System.out.println("file: " +filename);
                waveReader2.wavload(filename);
                waveReader2.read();
                System.out.println(waveReader2.header.toString());   //print out Header -- Overriden in tostringWaveheader
            }
            if (argv[i].contains("--cut") || argv[i].contains("-c)")) {
                System.out.println(argv[i]);
                cutme = true;
                cutlength = Integer.parseInt(argv[i + 1]);
                System.out.println("cutlength: "+cutlength);
                waveReader2.trim(0, cutlength);
                System.out.println("CUTME");
            }

            if (argv[i].contains("--reverse") || argv[i].contains("-r)")) {
                System.out.println(argv[i]);
                reverseme = true;
                waveReader2.reverse();
                System.out.println("REVERSEME");
                //waveReader2.trim();
            }

        }
waveReader2.createWaveFile();
    }
          //  main.run();    JCONMMANDER


        public void run() throws IOException {
           // System.out.printf(" %s %d %d",files, length);
            waveReader= new WaveReader();
           // waveReader.wavload("test1.wav");
           // waveReader.wavload(files.get(0));
            waveReader.wavload(filename);

            waveReader.read();
            System.out.println(waveReader.header.toString());
            if (reverse){
                System.out.printf(" Reverse me!");
            }
        }
    }

