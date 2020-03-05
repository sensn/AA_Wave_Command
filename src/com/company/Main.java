package com.company;
import com.beust.jcommander.JCommander;   // Jcommander.org
import com.beust.jcommander.Parameter;    // "Because life is too short to parse command line parameters" - Cédric Beust

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class Main {
    private WaveReader waveReader;

    @Parameter(description = "Files")
    private List<String> files = new ArrayList<>();
    @Parameter(names={"--cut", "-c"}, validateWith = PositiveInteger.class)
    int length;
 //   @Parameter(names={"--reverse", "-r"}, validateWith = PositiveInteger.class)
 //   int pattern;
    @Parameter(names={"--reverse", "-r"}, description = "Reverse File")
    private boolean reverse = false;
    public static void main(String ... argv) throws IOException {
            Main main = new Main();
            JCommander.newBuilder()
                    .addObject(main)
                    .build()
                    .parse(argv);
            main.run();
        }

        public void run() throws IOException {
           // System.out.printf(" %s %d %d",files, length);
            waveReader= new WaveReader();
            waveReader.wavload();

            waveReader.read();
            System.out.println(waveReader.header.toString());
            if (reverse){
                System.out.printf(" Reverse me!");
            }
        }
    }

