package com.tfhr.www.mapapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class IOUtils {

    public static void main(String args[]) throws IOException {
        BufferedReader bufferedReader;
        InputStreamReader reader;
        BufferedWriter bufferedWriter;
        FileWriter writer;
        String path = "C:/Users/ZZ04/Desktop/dart学习笔记.txt";
        String copy_path = "C:/Users/ZZ04/Desktop/dartlearning.txt";
        File copyfile = new File(copy_path);
        if (!copyfile.exists()) {
            copyfile.createNewFile();
        }
        try {
            reader=new InputStreamReader(new FileInputStream(path),"gbk");
            bufferedReader = new BufferedReader(reader);
            writer = new FileWriter(copyfile);
            bufferedWriter=new BufferedWriter(writer);
            try {
                while (bufferedReader.readLine() != null) {
                    bufferedWriter.write(bufferedReader.readLine()+"\r\n");
                }
                bufferedWriter.flush();
                reader.close();
                bufferedReader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
