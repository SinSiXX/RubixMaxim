package com.xetanai.rubix;

import java.util.*;
import java.io.*;

public class IO {
   
   public static Hashtable<String, String> read_a_at_b(String filename) throws FileNotFoundException {
      Hashtable<String, String> retVal = new Hashtable<String, String>();
      try {
         File file = new File(filename);
         Scanner input = new Scanner(file);
         String line;
         String[] split;
         while(input.hasNextLine()) {
            line = input.nextLine();
            split = line.split("@");
            retVal.put(split[0], split[1]);
         }
         input.close();
      }
      catch(FileNotFoundException e) {
         System.out.println("File not found!");
         e.printStackTrace();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
      return retVal;
   }
   
}