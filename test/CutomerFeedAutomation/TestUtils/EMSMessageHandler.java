/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CutomerFeedAutomation.TestUtils;

import CutomerFeedAutomation.TestCases.MDMBusinessRules_Test.*;
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
    
    public void CreateEMSMessage(String filename, HashMap<customerXMLkeys, String> xmlKeyVal) {
        String file = utilities.LoadTestFile(filename);
        String[] splitFile = file.split("\\$TextBody:");
        String[] propertyList = splitFile[0].split("\\$Properties:")[1].split("\n");
        _properties = CreatePropertiesHashMap(propertyList);
        _messageBody = splitFile[1].trim();

        _messageBody = _messageBody.replaceAll(":systemid:", xmlKeyVal.get(customerXMLkeys.systemId));
        _messageBody = _messageBody.replaceAll(":firstName:", xmlKeyVal.get(customerXMLkeys.firstName));
        _messageBody = _messageBody.replaceAll(":lastName:", xmlKeyVal.get(customerXMLkeys.lastName));
        _messageBody = _messageBody.replaceAll(":middleName:", xmlKeyVal.get(customerXMLkeys.middleName));
        _messageBody = _messageBody.replaceAll(":DOB:", xmlKeyVal.get(customerXMLkeys.DOB));
        _messageBody = _messageBody.replaceAll(":email:", xmlKeyVal.get(customerXMLkeys.email));
        _messageBody = _messageBody.replaceAll(":mobile:", xmlKeyVal.get(customerXMLkeys.mobile));
        _messageBody = _messageBody.replaceAll(":landline:", xmlKeyVal.get(customerXMLkeys.landline));
        _messageBody = _messageBody.replaceAll(":genderTitle:", xmlKeyVal.get(customerXMLkeys.genderTitle));
        _messageBody = _messageBody.replaceAll(":extraTitle:", xmlKeyVal.get(customerXMLkeys.extraTitle));
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
