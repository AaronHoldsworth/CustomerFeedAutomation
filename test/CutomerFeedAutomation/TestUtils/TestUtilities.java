/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.TestUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author TTGAHX
 */
public class TestUtilities {
        
    
    public HashMap<String, String> CreatePropertiesHashMap(String[] propertyList) {
        HashMap<String, String> list = new HashMap<String, String>();
        for (int i = 0; i < propertyList.length; i++) {
            if (!propertyList[i].equals("")) {
                String[] keyValuePair = propertyList[i].split("\\=");
                
                String value = keyValuePair[1].split(":")[1];
                
                list.put(keyValuePair[0], value);
            }
        }

        return list;
    }
    
    public String LoadTestFile(String fileName) {

        String fileAsString = "";
        try {
            InputStream is = new FileInputStream(fileName);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append("\n");
                line = buf.readLine();
                fileAsString = sb.toString();

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return fileAsString;
    }
    
    public String GenerateGuid()
    {
        UUID guid = UUID.randomUUID();
        String id = guid.toString();
        
        return id;
    }

    public void WaitForMessage() {
                try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
