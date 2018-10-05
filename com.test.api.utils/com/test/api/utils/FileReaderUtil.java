package com.test.api.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileReaderUtil {


    BufferedReader streamReader;

    public String readJsonFile(String fileName) throws IOException {
        File jsonFile = new File(fileName);
        InputStream jsonInputStream;

        try {
            jsonInputStream = new FileInputStream(jsonFile);
        	
        } catch (FileNotFoundException e) {

            throw e;
        }
        try {
            streamReader = new BufferedReader(new InputStreamReader(jsonInputStream, "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            jsonInputStream.close();
            streamReader.close();
            return responseStrBuilder.toString();
        } catch (IOException e) {
            if (streamReader != null)
                streamReader.close();
            throw e;
        }


    }

}
