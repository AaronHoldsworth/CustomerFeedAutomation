/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customerfeedautomation;

import java.util.HashMap;

/**
 *
 * @author TTGAHX
 */
public class EMSMessageHandler {

    private HashMap<String, String> _properties;
    private String _messageBody;
    TestUtilities utilities = new TestUtilities();

    public EMSMessageHandler(String filename, String systemId) {
        String file = utilities.LoadTestFile(filename);
        String[] splitFile = file.split("\\$TextBody:");
        String[] propertyList = splitFile[0].split("\\$Properties:")[1].split("\n");
        _properties = utilities.CreatePropertiesHashMap(propertyList);
        _messageBody = splitFile[1].trim();
        
        _messageBody = _messageBody.replaceAll(":systemid:", systemId);
    }

    public HashMap<String, String> Properties() {
        return _properties;
    }
    
    public String MessageBody()
    {
        return _messageBody;
    }

}
