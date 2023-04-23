sQuery = "SELECT PROJECT_NAME,PROJECT_MANAGER,PROJECT_MANAGER_EMAILID,PROJECT_AMOUNT,TO_CHAR(PROJECT_START_DATE, 'DD/MM/YYYY'),TO_CHAR(PROJECT_END_DATE, 'DD/MM/YYYY'),PROJECT_DESCRIPTON FROM MEMO_PROJECT_DETAILS WHERE PROJECT_ID='" + strProjectID + "'";
                commFuns.writeToLog(2, sQuery, strProcessInstanceID);
                commFuns.writeToLog(2, "sQuery = " + sQuery, strProcessInstanceID);
                sOutput = getDataFromDBWithColumns(ifr, sQuery, "PROJECT_NAME,PROJECT_MANAGER,PROJECT_MANAGER_EMAILID,PROJECT_AMOUNT,PROJECT_START_DATE,PROJECT_END_DATE,PROJECT_DESCRIPTON");
                commFuns.writeToLog(2, sOutput, strProcessInstanceID);
                if (Integer.parseInt(getTagValues(sOutput, "TotalRetrieved")) > 0) {
                    ifr.setValue("table40_PROJECT_NAME", getTagValues(sOutput, "PROJECT_NAME"));
                    ifr.setValue("table40_PROJECT_MANAGER", getTagValues(sOutput, "PROJECT_MANAGER"));
                    ifr.setValue("table40_PROJECT_MANAGER_EMAILID", getTagValues(sOutput, "PROJECT_MANAGER_EMAILID"));
                    ifr.setValue("table40_PROJECT_AMOUNT", getTagValues(sOutput, "PROJECT_AMOUNT"));
                    ifr.setValue("table40_PROJECT_START_DATE", getTagValues(sOutput, "PROJECT_START_DATE"));
                    ifr.setValue("table40_PROJECT_END_DATE", getTagValues(sOutput, "PROJECT_END_DATE"));
                    ifr.setValue("table40_PROJECT_DESCRIPTON", getTagValues(sOutput, "PROJECT_DESCRIPTON"));
                }
				
				
public String getDataFromDBWithColumns(IFormReference formobj, String query, String columns) {
	String data = "";
	System.out.println("Inside getDBfromDB================================================");
	System.out.println(query);
	List<List<String>> retval = formobj.getDataFromDB(query);
	System.out.println(retval);
	String[] Cols = columns.split(",");
	if (retval.isEmpty()) {
	return "<Data><TotalRetrieved>0</TotalRetrieved></Data>";
	}
	data = "<Data><TotalRetrieved>" + retval.size() + "</TotalRetrieved>";
	for (int i = 0; i < retval.size(); i++) {
	data += "<Row>";
	for (int j = 0; j < retval.get(i).size(); j++) {
	data += "<" + Cols[j] + ">" + retval.get(i).get(j) + "</" + Cols[j] + ">";
	}
	data += "</Row>";
	}
	data += "</Data>";
	return data;																											
}
public String getTagValues(String sXML, String sTagName) {
        String sTagValues = "";
        String sStartTag = "<" + sTagName + ">";
        String sEndTag = "</" + sTagName + ">";
        String tempXML = sXML;
        tempXML = tempXML.replaceAll("&", "#amp#");
        try {

            for (int i = 0; i < sXML.split(sEndTag).length - 1; i++) {
                if (tempXML.indexOf(sStartTag) != -1) {
                    sTagValues += tempXML.substring(tempXML.indexOf(sStartTag) + sStartTag.length(), tempXML.indexOf(sEndTag));
                    //System.out.println("sTagValues"+sTagValues);
                    tempXML = tempXML.substring(tempXML.indexOf(sEndTag) + sEndTag.length(), tempXML.length());
                }
                if (tempXML.indexOf(sStartTag) != -1) {
                    sTagValues += ",";
                    //System.out.println("sTagValues"+sTagValues);
                }
            }
            if (sTagValues.indexOf("#amp#") != -1) {
                System.out.println("Index found");
                sTagValues = sTagValues.replaceAll("#amp#", "&").trim();
            }
            //System.out.println(" Final sTagValues"+sTagValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sTagValues;
    }