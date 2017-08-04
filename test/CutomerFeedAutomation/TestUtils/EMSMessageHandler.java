/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.TestUtils;

import java.util.HashMap;

/**
 *
 * @author TTGAHX
 */
public class EMSMessageHandler {

    private HashMap<String, String> _properties;

    private String _messageBody;
    TestUtilities utilities = new TestUtilities();

    public void CreateEMSMessage(String filename, String systemId, String generatedName) {
        String file = utilities.LoadTestFile(filename);
        String[] splitFile = file.split("\\$TextBody:");
        String[] propertyList = splitFile[0].split("\\$Properties:")[1].split("\n");
        _properties = CreatePropertiesHashMap(propertyList);
        _messageBody = splitFile[1].trim();

        _messageBody = _messageBody.replaceAll(":systemid:", systemId);
        _messageBody = _messageBody.replaceAll(":genName:", generatedName);
    }

    public HashMap<String, String> CreatePropertiesHashMap(String[] propertyList) {
        HashMap<String, String> list = new HashMap<>();
        for (String property : propertyList) {
            if (!property.equals("")) {
                String[] keyValuePair = property.split("\\=");
                String value = keyValuePair[1].split(":")[1];
                list.put(keyValuePair[0], value);
            }
        }

        return list;
    }

    public HashMap<String, String> Properties() {
        return _properties;
    }

    public String MessageBody() {
        return _messageBody;
    }

}
