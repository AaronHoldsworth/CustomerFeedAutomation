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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author TTGAHX
 */
public class TestUtilities {

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

    public String GenerateGuid() {
        UUID guid = UUID.randomUUID();
        String id = guid.toString();

        return id;
    }
    
    public String GenerateName()
    {
        char[] allowedChars = "abcedfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder generatedName = new StringBuilder();
        
        for(int i=0; i<10; i++)
        {
            generatedName.append(allowedChars[ThreadLocalRandom.current().nextInt(0, allowedChars.length-1)]);
        }
          
        return generatedName.toString();
    }

    public void WaitForMessage() {
        try {
            Thread.sleep(5000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public List<String> GetExtraNames(JSONObject jsonRecord) throws JSONException {
        JSONArray extraNames;
        List<String> middleNames = new ArrayList<>();
       try
       {
        extraNames = jsonRecord.getJSONObject("customer").getJSONObject("extraNames").getJSONArray("extraNames");
        for (Object o : extraNames) {
            if (o instanceof JSONObject) {
                JSONObject extraName = (JSONObject) o;
                middleNames.add(extraName.getString("name"));
            }
        }
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
           middleNames = null;
       }
        return middleNames;
    }
}
