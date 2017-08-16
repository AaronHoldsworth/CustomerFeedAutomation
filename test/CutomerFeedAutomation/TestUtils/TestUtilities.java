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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import org.bson.Document;
import java.util.concurrent.ThreadLocalRandom;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author TTGAHX
 */
public class TestUtilities {
    public static enum eContactType {
        email,
        phoneNumber,
        address;
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
    
    public String GenerateName(int len)
    {
        char[] allowedChars = "abcedfghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder generatedName = new StringBuilder();
        
        for(int i=0; i<len+1; i++)
        {
            generatedName.append(allowedChars[ThreadLocalRandom.current().nextInt(0, allowedChars.length-1)]);
        }
          
        return generatedName.toString();
    }
    
    public String GenerateDOB(){
        Random  rnd;
        Date    dt;
        long    ms;

        // Get a new random instance, seeded from the clock
        rnd = new Random();

        // Get an Epoch value roughly between 1940 and 2010
        // -946771200000L = January 1, 1940
        // Add up to 70 years to it (using modulus on the next long)
        ms = -946771200000L + (Math.abs(rnd.nextLong()) % (70L * 365 * 24 * 60 * 60 * 1000));

        // Construct a date
        dt = new Date(ms);
        String dob = new SimpleDateFormat("yyyy-MM-dd").format(dt);
        return dob;
    }
    
    public String GeneratePhoneNumber(){        
        int start, end;
        start = 11111;
        end = 99999;
        
        Random rnd = new Random();
        String ret = "" + (rnd.nextInt(end +1 -start) + start) + (rnd.nextInt(end +1 -start) + start);
        return  ret;
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
    
    public List<String> GetContactPoints(JSONObject jsonRecord, eContactType ContactType) throws JSONException {
        JSONArray contactPoints;
        List<String> contactPointValue = new ArrayList<>();
       try
       {
           //JSONObject contactPointsobj = jsonRecord.getJSONObject("customer").getJSONObject("contactPoints");
           
        contactPoints = jsonRecord.getJSONObject("customer").getJSONObject("contactPoints").getJSONArray("contactPoints");
        
        
        switch(ContactType){
                case email: contactPointValue = getEmail(contactPoints, ContactType);
                    break;
                case phoneNumber: contactPointValue = getPhone(contactPoints, ContactType);
                    break;
                case address: contactPointValue = getAddress(contactPoints, ContactType);
                    break;
                default:
                    throw new AssertionError(ContactType.name());
        }
        
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
           contactPointValue = null;
       }
        return contactPointValue;
    }
    
    public List<String> getEmail(JSONArray contactPoints, eContactType ContactType) throws JSONException {  
        List<String> emailAddress = new ArrayList<>();
       try
       {
           //JSONObject contactPointsobj = jsonRecord.getJSONObject("customer").getJSONObject("contactPoints");
        for (Object o : contactPoints) {
            if (o instanceof JSONObject) {
                JSONObject contactPoint = (JSONObject) o;
                if (contactPoint.has("contactEmailAddress"))
                {
                    contactPoint = contactPoint.getJSONObject("contactEmailAddress").getJSONObject("emailAddress");
                    emailAddress.add(contactPoint.getString(ContactType.toString()));
                }
                //middleNames.add(contactPoint.getString("name"));
            }
        }
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
           emailAddress = null;
       }
        return emailAddress;
    }
    
    public List<String> getPhone(JSONArray contactPoints, eContactType ContactType) throws JSONException {  
        List<String> phoneNumbers = new ArrayList<>();
       try
       {
           //JSONObject contactPointsobj = jsonRecord.getJSONObject("customer").getJSONObject("contactPoints");
        for (Object o : contactPoints) {
            if (o instanceof JSONObject) {
                JSONObject contactPoint = (JSONObject) o;
                if (contactPoint.has("contactPhoneNumber"))
                {
                    contactPoint = contactPoint.getJSONObject("contactPhoneNumber").getJSONObject("unStructuredPhoneNumber");
                    phoneNumbers.add(contactPoint.getString(ContactType.toString()));
                }                
            }
        }
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
           phoneNumbers = null;
       }
        return phoneNumbers;
    }
    
    public List<String> getAddress(JSONArray contactPoints, eContactType ContactType) throws JSONException {  
        List<String> emailAddress = new ArrayList<>();
       try
       {
           //JSONObject contactPointsobj = jsonRecord.getJSONObject("customer").getJSONObject("contactPoints");
        for (Object o : contactPoints) {
            if (o instanceof JSONObject) {
                JSONObject contactPoint = (JSONObject) o;
                if (contactPoint.has("contactEmailAddress"))
                {
                    contactPoint = contactPoint.getJSONObject("contactEmailAddress").getJSONObject("emailAddress");
                    emailAddress.add(contactPoint.getString(ContactType.toString()));
                }                
            }
        }
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
           emailAddress = null;
       }
        return emailAddress;
    }
    
    public List<String> GetDroolsTraceMessage(JSONObject jsonRecord) throws JSONException {
        JSONArray droolsArray;
        List<String> drools = new ArrayList<>();
       try
       {
        droolsArray = jsonRecord.getJSONObject("droolsTrace").getJSONArray("droolsTrace");
        for (Object o : droolsArray) {
            if (o instanceof JSONObject) {
                JSONObject droolsTrace = (JSONObject) o;
                drools.add(droolsTrace.getString("ruleName"));
            }
        }
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
           drools = null;
       }
        return drools;
    }
    
    public List<String> GetRefValues(JSONObject jsonRecord) throws JSONException {
        JSONArray droolsArray;
        List<String> drools = new ArrayList<>();
       try
       {
        droolsArray = jsonRecord.getJSONArray("value");
        for (Object o : droolsArray) {
            if (o instanceof JSONObject) {
                JSONObject droolsTrace = (JSONObject) o;
                drools.add(droolsTrace.getString("ruleName"));
            }
        }
       }
       catch (Exception e)
       {
           System.out.println(e.getMessage());
           drools = null;
       }
        return drools;
    }
}
