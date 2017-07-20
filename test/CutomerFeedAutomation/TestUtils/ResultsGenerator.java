/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.TestUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author TTGAHX
 */
public class ResultsGenerator {

    
    private String fileName;
    private String dirPath;

    public ResultsGenerator() {
        
        DateFormat df = new SimpleDateFormat("ddMMyyHHmmss");
        Date dateobj = new Date();
        dirPath = "C:\\AutomationResults\\";
        fileName = dirPath + "Results" + df.format(dateobj) + ".csv";
        CreateResultsFolderIfNotExist();
    }


    public void WriteResultsToFile(List<String> resultsList) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {

            for (String result : resultsList) {
                bw.write(result);
                bw.newLine();
            }

        } catch (IOException e) {
            
        }

    }

    private void CreateResultsFolderIfNotExist() {
        File theDir = new File(dirPath);

        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
            } catch (SecurityException se) {
                System.out.println(se.getMessage());
            }
        }
    }

}
