//Bug 1014568 - Code Review - Java File Header and Function Header missing
/********************************************************************
*    NEWGEN SOFTWARE TECHNOLOGIES LIMITED
*    Group                                     	: CIG
*    Product / Project                          : Deloitte P2P Automation
*    Module                                  	: VendorPortal
*    File Name                               	: NGDeloitteAutoProcess.java
*    Author                                    	: ksivashankar
*    Date written                          	: 22/09/2017
*   (DD/MM/YYYY)                      
*    Description                            	: AutoProcessing workitem will be checked 7 type of exceptions
*  CHANGE HISTORY
***********************************************************************************************
* Date                                Change By                    Change Description (Bug No. (If Any))
* (DD/MM/YYYY)            
* 27/09/2017  ksivashankar   Bug 1014568 - Code Review - Java File Header and Function Header missing
* 28/09/2017  ksivashankar   Bug 1014736 - AP NGFuser code suggestion
************************************************************************************************/

package ngdeloitteautoprocess;

import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPISException;
import Jdts.DataObject.JPDBString;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.pdf.PdfReader;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlList;
import com.newgen.dmsapi.DMSXmlResponse;
import com.newgen.omni.wf.util.xml.WFXmlList;
import com.newgen.omni.wf.util.xml.XMLParser;
import com.newgen.omni.wf.util.xml.api.dms.WFXmlResponse;
import com.newgen.wfdesktop.xmlapi.WFCallBroker;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author ksivashankar
 */
public class NGDeloitteAutoProcess {

    /**
     * @param args the command line arguments
     */
    private static String userName="";
    private static String password="";
    static String strIP="";
    String strPort="";
    int iPort;
    String strAppServerType="";
    static String strCabName="";
    static String strSessionId="";
    String strUsername="";
    String strPwd="";
    String Xmlout="";
    StringBuffer strBuffer=null;
    public static Logger mRepLogger = Logger.getLogger("Reportlog");
    static String mstrlog4jConfigFilePath = "";
//    public static Logger mRepLogger = Logger.getLogger("mLogger");
//    public static Logger mRepLogger = Logger.getLogger("Errorlog");
//    public static Logger mRepLogger = Logger.getLogger("Replog");
    
    public static String transactionId="";
    public static String strInvoiceno="";
    public static String sPrcsDefId="";
    int totalInvAmountLimit=0;
    double tolerancelimit=0.0;
    public static String downloadPath="";

    public NGDeloitteAutoProcess() {
        this.Xmlout = "";
        this.strBuffer = null;
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : InitLog
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : nothing
     *  Output Parameters                : nothing
     *  Return Values                    : nothing
     *  Description                      : Initializing loggers

     ************************************************************************/
 
    private static void InitLog() {
        try {           
            mstrlog4jConfigFilePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "Config"
                    + System.getProperty("file.separator")
                    + "log4j.properties";
            File lobjFile = new File(mstrlog4jConfigFilePath);
            if (!(lobjFile.exists())) {
                mRepLogger.info("Logger Config file doesnot exists !" + mstrlog4jConfigFilePath);
            } else {
                ConfigureLogger(mstrlog4jConfigFilePath);
            }
            lobjFile = null;
        } catch (Exception lobjExcp) {
            mRepLogger.info("Exception Occurs during Logger Initialization: " + lobjExcp.toString());
            mRepLogger.info("Exception Occurs during Logger Initialization: " + lobjExcp
                    .toString());
        }
    }

    /********************************************************************
     *  Function Name                    : ConfigureLogger
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : nothing
     *  Output Parameters                : nothing
     *  Return Values                    : nothing
     *  Description                      : Configuring loggers

     ************************************************************************/
 
    public static void ConfigureLogger(String plog4jConfigFilePath)
            throws Exception {
        String lExceptionId = new String("com.newgen.lns.myqueue.process.configureLogger.");
        try {
            FileInputStream lobjFileInputStream = null;
            lobjFileInputStream = new FileInputStream(plog4jConfigFilePath);
            Properties lobjPropertiesINI = new Properties();
            lobjPropertiesINI.load(lobjFileInputStream);
            lobjPropertiesINI = LoadProperties(lobjPropertiesINI);
            PropertyConfigurator propertyConfigurator = new PropertyConfigurator();
            propertyConfigurator.doConfigure(lobjPropertiesINI, LogManager.getLoggerRepository());

        } catch (Exception lobjExcp) {
            mRepLogger.info(lExceptionId + ": Exception occurs during Logger Initialization: " + lobjExcp.toString());
            mRepLogger.info(lExceptionId + ": Exception occurs during Logger Initialization: " + lobjExcp
                    .toString());
            throw lobjExcp;
        }
    }
    
    /********************************************************************
     *  Function Name                    : LoadProperties
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : pobjProperties
     *  Output Parameters                : Properties
     *  Return Values                    : nothing
     *  Description                      : Loading log4j file

     ************************************************************************/
   
    private static Properties LoadProperties(Properties pobjProperties)
            throws Exception {
        Properties lobjProperties = pobjProperties;
        try {
            Enumeration lobjEnumeration = null;
            lobjEnumeration = lobjProperties.keys();

            while (lobjEnumeration.hasMoreElements()) {
                String str1="";
                String s1 = ((String) lobjEnumeration.nextElement()).trim();
                if (!(s1.startsWith("log4j.logger."))) {
                    continue;
                }
                String s4 = lobjProperties.getProperty(s1);
                StringTokenizer stringtokenizer = new StringTokenizer(s4, ",");

                while (stringtokenizer.hasMoreTokens()) {
                    String s7 = stringtokenizer.nextToken();
                    String s2 = "log4j.appender." + s7.trim() + ".File";
                    str1 = lobjProperties.getProperty(s2);
                }

                stringtokenizer = null;
            }
        } catch (Exception lobjExcp) {
            mRepLogger.info("LoadProperties : Exception occurs during Logger Initialization: " + lobjExcp.toString());
            mRepLogger.info("LoadProperties : Exception occurs during Logger Initialization: " + lobjExcp.toString());
            throw lobjExcp;
        }

        return lobjProperties;
    }

    
    
    
    
    
    
    
     /********************************************************************
     *  Function Name                    : ReadProperty
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : nothing
     *  Output Parameters                : nothing
     *  Return Values                    : nothing
     *  Description                      : Reding settings.ini file

     ************************************************************************/
    public void ReadProperty() {
        try {
            mRepLogger.info("************************ ST Get Document UTILITY **************************");
            mRepLogger.info("************************ ST Get Document UTILITY **************************");
            mRepLogger.info("Start Reading INI File ..........");
            String strPropertyPath = System.getProperty("user.dir") + File.separator + "Settings.ini";
            mRepLogger.info("Read Property File From :" + strPropertyPath);
            FileInputStream File_Ini = new FileInputStream(strPropertyPath);
            Properties prop = new Properties();
            prop.load(File_Ini);

            this.downloadPath = System.getProperty("user.dir") + File.separator + "Documents";
            this.strIP = prop.getProperty("AppServerIP");
            this.strPort = prop.getProperty("AppServerPort");
            this.iPort = Integer.parseInt(this.strPort);
            this.strAppServerType = prop.getProperty("AppServerType");
            this.strCabName = prop.getProperty("CabinetName");
            this.strUsername = prop.getProperty("UserName");
            this.strPwd = prop.getProperty("Password");
            this.sPrcsDefId = prop.getProperty("ProcessDefID");
            this.totalInvAmountLimit = Integer.parseInt(prop.getProperty("TotalInvAmountLimit"));
            mRepLogger.info("downloadPath=" + downloadPath + "\nIP=" + strIP + " Port=" + strPort + " Appservertype=" + strAppServerType + " CabinetName=" + strCabName + " Username=" + strUsername + " Pswd=" + strPwd);
            mRepLogger.info("User Directory=" + System.getProperty("user.dir") + File.separator + "Documents");
            } catch (Exception e) {
            mRepLogger.info("\n\n### **[Error]** Exception in ReadProperty " + e.toString());
            e.printStackTrace();
        }
    }

     /********************************************************************
     *  Function Name                    : conCabinet
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : nothing
     *  Output Parameters                : nothing
     *  Return Values                    : boolean flag
     *  Description                      : Connecting to ibps cabinet

     ************************************************************************/
    public boolean conCabinet() {
        DMSXmlResponse xmlResponse = null;
        this.Xmlout = "";
        StringBuffer strBuffer = new StringBuffer();
        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version=1.0?>");
            strBuffer.append("<NGOConnectCabinet_Input>");
            strBuffer.append("<Option>NGOConnectCabinet</Option>");
            strBuffer.append("<CabinetName>" + this.strCabName + "</CabinetName>");
            strBuffer.append("<UserName>" + this.strUsername + "</UserName>");
            strBuffer.append("<UserPassword>" + this.strPwd + "</UserPassword>");
            strBuffer.append("<UserExist>N</UserExist>");
            strBuffer.append("</NGOConnectCabinet_Input>");
            mRepLogger.info("ConnectCabinet InputXML:\n" + strBuffer.toString());
            this.Xmlout = WFCallBroker.execute(strBuffer.toString(), this.strIP, this.iPort, 0);

            mRepLogger.info("ConnectCabinet OutputXML:\n" + this.Xmlout);
            xmlResponse = new DMSXmlResponse(this.Xmlout);
            if (xmlResponse.getVal("status").equals("0")) {
                strSessionId = xmlResponse.getVal("UserDBId");
                mRepLogger.info("Cabinet Connected successfully ....." + strSessionId);
                return true;
            }
            mRepLogger.info("Problem in Connecting Cabinet : " + xmlResponse.getVal("Error"));
            mRepLogger.info("Problem in Connecting Cabinet : " + xmlResponse.getVal("Error"));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            mRepLogger.info("\n\n### **[Error]** Exception in Connecting Cabinet : " + e.toString());
        }
        return false;
    }

    /********************************************************************
     *  Function Name                    : DisconCabinet
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : nothing
     *  Output Parameters                : nothing
     *  Return Values                    : boolean flag
     *  Description                      : Disconnecting to ibps cabinet

     ************************************************************************/
    public void DisconCabinet() {
        DMSXmlResponse xmlResponse = null;
        this.Xmlout = "";
        try {
            this.strBuffer = new StringBuffer();
            this.strBuffer.append("<?xml version='1.0'?>");
            this.strBuffer.append("<NGODisconnectCabinet_Input>");
            this.strBuffer.append("<Option>NGODisconnectCabinet</Option>");
            this.strBuffer.append("<CabinetName>" + this.strCabName + "</CabinetName>");
            this.strBuffer.append("<UserDBId>" + this.strSessionId + "</UserDBId>");
            this.strBuffer.append("</NGODisconnectCabinet_Input>");
            this.Xmlout = WFCallBroker.execute(this.strBuffer.toString(), this.strIP, this.iPort, 0);
            mRepLogger.info("Disconnect Cabinet InputXML:\n" + this.strBuffer.toString());
            mRepLogger.info("Disconnect Cabinet OutputXMLt:\n" + this.Xmlout);
            xmlResponse = new DMSXmlResponse(this.Xmlout);
            if (xmlResponse.getVal("Status").equals("0")) {
                mRepLogger.info("Cabinet Disconnected Successfully ...");
            } else {
                mRepLogger.info("Problem in Disconnecting cabinet:" + xmlResponse.getVal("Error"));
                mRepLogger.info("Problem in Disconnecting cabinet:" + xmlResponse.getVal("Error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mRepLogger.info("\n\n### **[Error]** Exception in Disconnecting cabinet : " + e.toString());
        }
    }
    
    /********************************************************************
     *  Function Name                    : getAPSelect
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : strQuery,noofCols
     *  Output Parameters                : strOutputXmlFI
     *  Return Values                    : strOutputXmlFI
     *  Description                      : Select API Call

     ************************************************************************/
    public String getAPSelect(String strQuery, int noofCols) {
        com.newgen.omni.wf.util.xml.XMLParser parsergetlist = null;
        String strOutputXmlFI = null;
        StringBuilder strBuffFI = new StringBuilder();
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        String FIDocNo = "";

        strBuffFI = strBuffFI.append("<?xmlversion=\"1.0\"?>");
        strBuffFI = strBuffFI.append("<APSelectWithColumnNames>");
        strBuffFI = strBuffFI.append("<Option>APSelectWithColumnNames</Option>");
        strBuffFI = strBuffFI.append("<EngineName>").append(strCabName).append("</EngineName>");
        strBuffFI = strBuffFI.append("<SessionId>").append(strSessionId).append("</SessionId>");
        strBuffFI = strBuffFI.append("<QueryString>").append(strQuery).append("</QueryString>");
        strBuffFI = strBuffFI.append("<NoOfCols>").append(noofCols).append("</NoOfCols>");
        strBuffFI = strBuffFI.append("</APSelectWithColumnNames>");
        mRepLogger.info("strBuffFI==" + strBuffFI);
        try {
            strOutputXmlFI = WFCallBroker.execute(strBuffFI.toString(), strIP, Integer.parseInt("3333"), 0);
            mRepLogger.info("strCompOutputXml==" + strOutputXmlFI); 
        } catch (Exception e) {
            mRepLogger.info("[Error]Error in executing APSelect " + e);

        }
        return strOutputXmlFI;
    }

    /********************************************************************
     *  Function Name                    : GetData
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : strQuery,noofCols
     *  Output Parameters                : Xmlout
     *  Return Values                    : Xmlout
     *  Description                      : GetData API Call

     ************************************************************************/
    public String GetData(String queryString, int noofCols) {
        mRepLogger.info("Get Data Call");
        Xmlout = "";
        try {
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            strBuffer.append("<IGGetData>");
            strBuffer.append("<Option>IGGetData</Option>");
            strBuffer.append("<EngineName>").append(strCabName)
                    .append("</EngineName>");
            strBuffer.append("<SessionId>");
            strBuffer.append(strSessionId);
            strBuffer.append("</SessionId>");
            strBuffer.append("<QueryString");
            strBuffer.append(">").append(queryString);
            strBuffer.append("</QueryString>");
            strBuffer.append("<ColumnNo>");
            strBuffer.append(noofCols);
            strBuffer.append("</ColumnNo>");

            strBuffer.append("</IGGetData>");
            Xmlout = callServer(strBuffer.toString());
            Xmlout = Xmlout.replaceAll(" Columns=\"\\d\"", "");

        } catch (Exception e) {
            mRepLogger.info("\n\n### **[Error]** Exception in GetData  : "
                    + e);
            e.printStackTrace();
        }
        return Xmlout;
    }
    
     /********************************************************************
     *  Function Name                    : callServer
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : xml
     *  Output Parameters                : Xmlout
     *  Return Values                    : Xmlout
     *  Description                      : call server

     ************************************************************************/
    private String callServer(String xml) throws Exception {

        mRepLogger.info("Call Server Input : " + xml);
        String output = com.newgen.wfdesktop.xmlapi.WFCallBroker.execute(
                xml.toString(), strIP, 3333, 0);
        String mainCode = new DMSXmlResponse(output).getVal("MainCode");
        mRepLogger.info("Call Server output : " + output);
        if (mainCode == null || mainCode.length() == 0) {
            mainCode = new DMSXmlResponse(output).getVal("Status");
        }
        if (mainCode == "16") {
            mRepLogger.info("Workitem is locked: " + output);

        }

        return output;
    }
    
     /********************************************************************
     *  Function Name                    : isempty
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : String
     *  Output Parameters                : boolean
     *  Return Values                    : boolean
     *  Description                      : Check if empty or null

     ************************************************************************/
    public static Boolean isempty(String value) throws Exception {

        if (value != null) {
            if (value.equalsIgnoreCase("") || value.equalsIgnoreCase("null")) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

    }
    
     /********************************************************************
     *  Function Name                    : chkEmpty
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : String
     *  Output Parameters                : boolean
     *  Return Values                    : boolean
     *  Description                      : Checks string is empty or not

     ************************************************************************/
    public static boolean chkEmpty(String s) {
        //System.out.println("Inside chkEmpty method for String " + s);
        if (s == null || s.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

     /********************************************************************
     *  Function Name                    : GetWorkitem
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : ProcessInstanceId
     *  Output Parameters                : 
     *  Return Values                    : 
     *  Description                      : method will do getting workitem

     ************************************************************************/
    public void GetWorkitem(String ProcessInstanceId) {
        DMSXmlResponse xmlResponse = null;
        Xmlout = "";

        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version='1.0'?>");
            strBuffer.append("<WMUnlockWorkItem_Input>");
            strBuffer.append("<Option>WMGetWorkItem</Option>");
            strBuffer.append("<EngineName>" + strCabName + "</EngineName>");
            strBuffer.append("<SessionID>" + strSessionId + "</SessionID>");
            strBuffer.append("<ProcessInstanceId>" + ProcessInstanceId + "</ProcessInstanceId>");
            strBuffer.append("<WorkItemId>1</WorkItemId>");
            strBuffer.append("</WMGetWorkItem_Input>");
            Xmlout = WFCallBroker.execute(strBuffer.toString(), strIP, 3333, 0);
            mRepLogger.info("*************GetWorkItem_Input InputXML:\n" + strBuffer.toString());
            mRepLogger.info("*************GetWorkItem_Input Cabinet OutputXML:\n" + Xmlout);
            xmlResponse = new DMSXmlResponse(Xmlout);
            if (xmlResponse.getVal("MainCode").equals("0")) {
                mRepLogger.info("*************GetWorkItem was Success*************");
                CompleteWorkitem(ProcessInstanceId);
            } else {
                mRepLogger.info("*************GetWorkItem was Failed*************");

            }
        } catch (Exception e) {
            mRepLogger.info("\n\n***************[Error]*************** : " + e);

        }
    }

     /********************************************************************
     *  Function Name                    : CompleteWorkitem
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : ProcessInstanceID
     *  Output Parameters                : 
     *  Return Values                    : 
     *  Description                      : Workitem Complete Call

     ************************************************************************/
    public void CompleteWorkitem(String ProcessInstanceId) {
        DMSXmlResponse xmlResponse = null;
        Xmlout = "";
        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version='1.0'?>");
            strBuffer.append("<WMCompleteWorkItem_Input>");
            strBuffer.append("<Option>WMCompleteWorkItem</Option>");
            strBuffer.append("<EngineName>" + strCabName + "</EngineName>");
            strBuffer.append("<SessionId>" + strSessionId + "</SessionId>");
            strBuffer.append("<ProcessInstanceId>" + ProcessInstanceId + "</ProcessInstanceId>");
            strBuffer.append("<WorkItemId>1</WorkItemId>");
            strBuffer.append("<AuditStatus></AuditStatus>");
            strBuffer.append("<Comments></Comments>");
            strBuffer.append("</WMCompleteWorkItem_Input>");
            Xmlout = WFCallBroker.execute(strBuffer.toString(), strIP, 3333, 0);
            mRepLogger.info("*************CompleteWorkitem InputXML:\n" + strBuffer.toString());
            mRepLogger.info("*************CompleteWorkitem OutputXML:\n" + Xmlout);
            xmlResponse = new DMSXmlResponse(Xmlout);
            if (xmlResponse.getVal("Maincode").equals("0")) {
                mRepLogger.info("WMCompleteWorkItem was Successful");
            } else {
            }
        } catch (Exception e) {
            mRepLogger.info("\n\n***************(Error)***************" + e);
            mRepLogger.info("WMCompleteWorkItem was Not Successful");
        }

    }
    
     /********************************************************************
     *  Function Name                    : dataExtractionChk
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : transactionid
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Check DataExtraction and return result

     ************************************************************************/
    public static String dataExtractionChk(String transactionid) throws Exception {
        mRepLogger.info("*****Inside dataExtractionCHk*****");
        //Bug 1014736 - AP NGFuser code suggestion
        String qryExtractedfields = "SELECT InvoiceType,iPS_INVOICENO AS InvoiceNo,InvoiceAmount,iPS_INVOICE_DATE AS InvoiceDate,iPS_VENDOR_CODE AS VendorCode,iPS_VENDOR_NAME AS VendorName,iPS_ORDER_NO AS PONumber,iPS_CURRENCY AS Currency, iPS_INVOICEAMT_WOTAX AS InvoiceAmountWOTax FROM NG_AP_ExtAPProcess with(nolock) WHERE TransactionID='" + transactionid + "'";
        String flag = "false";   // flag is returned true if action flag  is set to Exception else false
        XMLParser parsergetlist = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;

        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        String InvoiceType = "";
        String InvoiceNo = "";
        String VendorCode = "";
        String VendorName = "";
        String PONumber = "";
        String Currency = "";
        double invoiceamount = 0;
        String invoicedate = "";
        double InvoiceAmountWOTax = 0;
        try {
            String xmlqryExtractedfields = obj.getAPSelect(qryExtractedfields, 8);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqryExtractedfields);

            if (parsergetlist.getValueOf("MainCode").equals("0")) {
                wfXmlResponseFI = new WFXmlResponse(xmlqryExtractedfields);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);

                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    if (!isempty(wfxmllistFI.getVal("InvoiceType").trim())) {

                        InvoiceType = wfxmllistFI.getVal("InvoiceType").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("InvoiceNo").trim())) {

                        InvoiceNo = wfxmllistFI.getVal("InvoiceNo").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("InvoiceAmount").trim())) {

                        invoiceamount = Double.parseDouble(wfxmllistFI.getVal("InvoiceAmount").trim().replaceAll(",", ""));
                    }
                    if (!isempty(wfxmllistFI.getVal("InvoiceDate").trim())) {

                        invoicedate = wfxmllistFI.getVal("InvoiceDate").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("VendorCode").trim())) {

                        VendorCode = wfxmllistFI.getVal("VendorCode").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("VendorName").trim())) {

                        VendorName = wfxmllistFI.getVal("VendorName").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("PONumber").trim())) {

                        PONumber = wfxmllistFI.getVal("PONumber").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("Currency").trim())) {

                        Currency = wfxmllistFI.getVal("Currency").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("InvoiceAmountWOTax").trim())) {

                        InvoiceAmountWOTax = Double.parseDouble(wfxmllistFI.getVal("InvoiceAmountWOTax").trim().replaceAll(",", ""));
                    }
                }
                if (!obj.chkEmpty(InvoiceType) && InvoiceType.equalsIgnoreCase("PO Based")) {
                    if (obj.chkEmpty(invoicedate) || invoiceamount == 0 || obj.chkEmpty(InvoiceNo)
                            || obj.chkEmpty(VendorCode) || obj.chkEmpty(VendorName) || obj.chkEmpty(PONumber)
                            || obj.chkEmpty(Currency) || InvoiceAmountWOTax == 0) {
                        obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        obj.updateData("ExceptionType", "Data Extraction Failed", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        flag = "false";  ///set false as data not extracted
                        obj.GetWorkitem(transactionid);   //---working one..later uncomment
                    } else {
                        flag = "true";   //Data is Extracted 
                    }
                } else if (!obj.chkEmpty(InvoiceType) && !InvoiceType.equalsIgnoreCase("PO Based")) {
                    if (obj.chkEmpty(invoicedate) || invoiceamount == 0 || obj.chkEmpty(InvoiceNo)
                            || obj.chkEmpty(VendorCode) || obj.chkEmpty(VendorName) || obj.chkEmpty(Currency)) {
                        obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        obj.updateData("ExceptionType", "Data Extraction Failed", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        flag = "false";  ///set false as data not extracted
                        obj.GetWorkitem(transactionid);   //---working one..later uncomment
                    } else {
                        flag = "true";   //Data is Extracted 
                    }
                } else {
                    if (obj.chkEmpty(invoicedate) || invoiceamount == 0 || obj.chkEmpty(InvoiceNo)
                            || obj.chkEmpty(VendorCode) || obj.chkEmpty(VendorName) || obj.chkEmpty(Currency)) {
                        obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        obj.updateData("ExceptionType", "Data Extraction Failed", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                        flag = "false";  ///set false as data not extracted
                        obj.GetWorkitem(transactionid);   //---working one..later uncomment
                    } else {
                        flag = "true";   //Data is Extracted 
                    }
                }

            } else {
                mRepLogger.info("Problem in Executing APSelect in dataExtractionChk()");
            }
        } catch (Exception e) {
            mRepLogger.info("Exception Occurred in  dataextractionChk()");
        }
       mRepLogger.info(" Returning flag=" + flag + " from dataextractionChk()  --->(true=dataextractionChk is passed , false=dataextractionChk is failed )");
       return flag;
    }
    
     /********************************************************************
     *  Function Name                    : updateData
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : colName,value,tableName,whereClause
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Update the data

     ************************************************************************/
    public static String updateData(String colName, String value, String tableName, String whereClause) {
        mRepLogger.info("Inside updateData method");
        mRepLogger.info("colName to be updated======" + colName);
        mRepLogger.info("value to be updated==" + value);
        mRepLogger.info("Where clause=" + whereClause);
        StringBuilder inputxml = new StringBuilder();
        inputxml = inputxml.append("<?xmlversion=\"1.0\"?>");
        inputxml = inputxml.append("<APUpdate>");
        inputxml = inputxml.append("<Option>APUpdate</Option>");
        inputxml = inputxml.append("<ProcessDefId>").append(sPrcsDefId).append("</ProcessDefId>");
        inputxml = inputxml.append("<Status>true</Status>");
        inputxml = inputxml.append("<SessionId>").append(strSessionId).append("</SessionId>");
        inputxml = inputxml.append("<EngineName>").append(strCabName).append("</EngineName>");
        inputxml = inputxml.append("<TableName>").append(tableName).append("</TableName>");
        inputxml = inputxml.append("<ColName>").append(colName).append("</ColName>");
        inputxml = inputxml.append("<Values>'").append(value).append("'</Values>");
        inputxml = inputxml.append("<WhereClause>").append(whereClause).append("</WhereClause>");
       inputxml = inputxml.append("</APUpdate>");
        mRepLogger.info("inputxm==" + inputxml);
        XMLParser parsergetlist = null;
        String strCompOutputXml = null;
       try {
            strCompOutputXml = WFCallBroker.execute(inputxml.toString(), strIP, Integer.parseInt("3333"), 0);
             parsergetlist = new XMLParser(strCompOutputXml);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {
                return (parsergetlist.getValueOf("MainCode"));
            } else {
                return "";
            }
        } catch (Exception e) {
            mRepLogger.info("[Error]Error in update the payment date" + e);
            return "";
        } finally {
            if (strCompOutputXml != null) {
                strCompOutputXml = null;
            }
            if (parsergetlist != null) {
                parsergetlist = null;
            }
            if (inputxml != null) {
                inputxml = null;
            }
        }
    }
    
    /********************************************************************
     *  Function Name                    : insertCmplxPODetailsQuery
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : String sTableName, String winame, String PONum, String POAmount, String POOpenAmount
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Insert data into table

     ************************************************************************/
    public static String insertCmplxPODetailsQuery(String sTableName, String winame, String PONum, String POAmount, String POOpenAmount) {
        StringBuilder inputxml = new StringBuilder();
        inputxml = inputxml.append("<?xmlversion=\"1.0\"?>");
        inputxml = inputxml.append("<APInsert>");
        inputxml = inputxml.append("<Option>APInsert</Option>");
        inputxml = inputxml.append("<ProcessDefId>").append(sPrcsDefId).append("</ProcessDefId>");
        inputxml = inputxml.append("<Status>true</Status>");
        inputxml = inputxml.append("<SessionId>").append(strSessionId).append("</SessionId>");
        inputxml = inputxml.append("<EngineName>").append(strCabName).append("</EngineName>");
        inputxml = inputxml.append("<TableName>").append(sTableName).append("</TableName>");
        inputxml = inputxml.append("<ColName>WIName,PONumber,POAmount,POOpenAmount</ColName>");
        inputxml = inputxml.append("<Values>'" + winame + "','" + PONum + "'," + POAmount + "," + POOpenAmount + "</Values>");
        inputxml = inputxml.append("</APInsert>");
        mRepLogger.info("inputxml==" + inputxml);
        XMLParser parsergetlist = null;
        String strCompOutputXml = null;
        try {
            strCompOutputXml = WFCallBroker.execute(inputxml.toString(), strIP, Integer.parseInt("3333"), 0);
            mRepLogger.info("strCompOutputXml==" + strCompOutputXml);
            parsergetlist = new XMLParser(strCompOutputXml);
           if (parsergetlist.getValueOf("MainCode").equals("0")) {
                return "True";
            } else {
                return "False";
            }
        } catch (Exception e) {
             mRepLogger.info("[Error]Error in Insert  insert CmplxP ODetails Query " + e);
            return "";
        } finally {
            if (strCompOutputXml != null) {
                strCompOutputXml = null;
            }
            if (parsergetlist != null) {
                parsergetlist = null;
            }
            if (inputxml != null) {
                inputxml = null;
            }
        }
    }

    /********************************************************************
     *  Function Name                    : insertCmplxGRDetailsQuery
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : String winame, String sTableName, String GRNSCNNo, String PONumber, String POItemNumber,
                                            String ProductCode, String CostCentre, String Description, String UnitPrice, String OrderUnit, String Quantity,
                                            String POAmount, String TaxCode
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Insert data into table

     ************************************************************************/
    public static String insertCmplxGRDetailsQuery(String winame, String sTableName, String GRNSCNNo, String PONumber, String POItemNumber,
            String ProductCode, String CostCentre, String Description, String UnitPrice, String OrderUnit, String Quantity,
            String POAmount, String TaxCode) {
        StringBuilder inputxml = new StringBuilder();
        inputxml = inputxml.append("<?xmlversion=\"1.0\"?>");
        inputxml = inputxml.append("<APInsert>");
        inputxml = inputxml.append("<Option>APInsert</Option>");
        inputxml = inputxml.append("<ProcessDefId>").append(sPrcsDefId).append("</ProcessDefId>");
        inputxml = inputxml.append("<Status>true</Status>");
        inputxml = inputxml.append("<SessionId>").append(strSessionId).append("</SessionId>");
        inputxml = inputxml.append("<EngineName>").append(strCabName).append("</EngineName>");
        inputxml = inputxml.append("<TableName>").append(sTableName).append("</TableName>");
        inputxml = inputxml.append("<ColName>WIName,GRNSCNNo,PONumber,POItemNumber,ProductCode,CostCentre,Description,UnitPrice,OrderUnit,Quantity,Amount,TaxCode</ColName>");
        inputxml = inputxml.append("<Values>'" + winame + "','" + GRNSCNNo + "','" + PONumber + "','" + POItemNumber + "','"
                + ProductCode + "','" + CostCentre + "','" + Description + "'," + UnitPrice + ",'"
                + OrderUnit + "'," + Quantity + "," + POAmount + ",'" + TaxCode + "'</Values>");
        inputxml = inputxml.append("</APInsert>");
         mRepLogger.info("inputxml==" + inputxml);
        XMLParser parsergetlist = null;
        String strCompOutputXml = null;
        try {
            strCompOutputXml = WFCallBroker.execute(inputxml.toString(), strIP, Integer.parseInt("3333"), 0);
            mRepLogger.info("strCompOutputXml==" + strCompOutputXml);
            parsergetlist = new XMLParser(strCompOutputXml);
             if (parsergetlist.getValueOf("MainCode").equals("0")) {
                return "True";
            } else {
                return "False";
            }
        } catch (Exception e) {
            mRepLogger.info("[Error]Error in Insert  insert Cmplx GR Details Query " + e);
            return "";
        } finally {
            if (strCompOutputXml != null) {
                strCompOutputXml = null;
            }
            if (parsergetlist != null) {
                parsergetlist = null;
            }
            if (inputxml != null) {
                inputxml = null;
            }
        }
    }
    
    /********************************************************************
     *  Function Name                    : missingPO
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : winame
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Missing PO check

     ************************************************************************/
    public static String missingPO(String winame) {
        mRepLogger.info("*****Inside MissingPO method*****");
       String flag = "false";  // if action flag is set to Exception then return this flag as true;
       //Bug 1014736 - AP NGFuser code suggestion
        String qryPONum = "SELECT InvoiceType,PONumber,VendorCode FROM NG_AP_ExtAPProcess p with(nolock) where TransactionID='" + winame + "'";
        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        XMLParser parsergetlist = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        String PONum = "";
        String VendorCode = "";
        String InvoiceType = null;
        String MInvoiceType = null;
        try {
            String xmlqryPONum = obj.getAPSelect(qryPONum, 3);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqryPONum);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {
                wfXmlResponseFI = new WFXmlResponse(xmlqryPONum);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);
                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    if (!isempty(wfxmllistFI.getVal("InvoiceType").trim())) {

                        InvoiceType = wfxmllistFI.getVal("InvoiceType").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("PONumber").trim())) {

                        PONum = wfxmllistFI.getVal("PONumber").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("VendorCode").trim())) {

                        VendorCode = wfxmllistFI.getVal("VendorCode").trim();
                    }
                }

                if (obj.chkEmpty(InvoiceType)) { // InvoiceType is empty AND PONumber is not empty
                    if (!obj.chkEmpty(PONum)) {
                        if (!obj.chkEmpty(VendorCode)) {
                            mRepLogger.info("Inside VendorInvType From VendorMasterDetails ");
                            //Bug 1014736 - AP NGFuser code suggestion
                            String qrytoVendorInvType = "SELECT VendorInvType FROM NG_AP_Master_VendorDetails with(nolock) WHERE VendorCode='" + VendorCode + "'";

                            try {
                                String xmlqrytoqrytoVendorInvType = obj.getAPSelect(qrytoVendorInvType, 1);
                                parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoVendorInvType);
                                if (parsergetlist.getValueOf("MainCode").equals("0")) {

                                    wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoVendorInvType);
                                    wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                                    wfxmllistFI.reInitialize(true);

                                    for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                                        if (!isempty(wfxmllistFI.getVal("VendorInvType").trim())) {
                                            MInvoiceType = wfxmllistFI.getVal("VendorInvType").trim();
                                        }
                                    }
                                    if (obj.chkEmpty(MInvoiceType)) {
                                        obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                        obj.updateData("ExceptionType", "Missing PO", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                        obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                        obj.GetWorkitem(winame);
                                        flag = "true";
                                        mRepLogger.info("MInvoiceType is empty");
                                    }

                                } else {
                                    mRepLogger.info("maincode not 0 in getdata");

                                }
                            } catch (Exception e) {
                                mRepLogger.info("Problem in execting APSelect in MInvoiceType");

                            }
                        } else {
                            mRepLogger.info("***VendorCode is empty***");
                        }
                    }
                }

                if (obj.chkEmpty(InvoiceType) && obj.chkEmpty(PONum)) {
                    mRepLogger.info("Both InvoiceType and PONumber are empty : Case 1");
                    obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                    obj.updateData("ExceptionType", "Missing PO", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                    obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                    flag = "true";
                    obj.GetWorkitem(winame);

                }

                if (!obj.chkEmpty(InvoiceType)) {
                    if (InvoiceType.equalsIgnoreCase("PO Based") && obj.chkEmpty(PONum)) { //InvoiceType is yes AND PONum is missing                       
                         mRepLogger.info("InvoiceType is not empty and PONumber is empty : Case 3");
                        obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                        obj.updateData("ExceptionType", "Missing PO", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                        obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                        flag = "true";
                        obj.GetWorkitem(winame);
                    }
                }


            } else {
                mRepLogger.info("Problem in GetData in MissingPO");
            }
        } catch (Exception e) {
            mRepLogger.info("Exception Occurred in Missing PO " + e);
        }

         mRepLogger.info(" Returning flag =" + flag + " From MissingPO method --->(true=MissingPO Found, false=MissingPO Not Found) ");
        return flag;
    }

    public static boolean pswdprotected(String transactionid, String filepath) throws Exception {

       mRepLogger.info("*****Inside Pswdprotected method*****");
        boolean flag = false;
        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        mRepLogger.info("filepath = " + filepath);

        String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
        mRepLogger.info(" fileType ===> " + fileType);

        if (fileType.equalsIgnoreCase("PDF")) {
            try {
                mRepLogger.info("filepath ---> " + filepath);
                PdfReader reader = new PdfReader(filepath);
                } catch (BadPasswordException e) {
                flag = true;
                 mRepLogger.info("PDF is password protected");
            } catch (Exception e) {
               mRepLogger.info("Exception in Reading File");
            }
            if (flag) {
                try {
                   obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                    obj.updateData("ExceptionType", "Password Protected Invoice", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                    obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + transactionid + "'");
                    obj.GetWorkitem(transactionid);

                } catch (Exception e) {
                     mRepLogger.info("Exception occurred in pswdprotectd() " + e);
                }
            }
        } else {
        }

        mRepLogger.info("Returning flag =" + flag + " from pswdprotected method --->(true=passwordprotected, false=not protected) ");
        return flag;

    }
    
    /********************************************************************
     *  Function Name                    : discountsChk
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : winame
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Discount check

     ************************************************************************/
    public String discountsChk(String winame) {

       mRepLogger.info("***Inside discountsChk method***");
        String flag = "false";
        XMLParser parsergetlist = null;
        //Bug 1014736 - AP NGFuser code suggestion
        String qryinvAmt = "Select InvoiceAmount from NG_AP_ExtAPProcess with(nolock) where TransactionID='" + winame + "'";
        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;

        double invAmt = 0;
        try {
            String xmlqryinvAmt = obj.getAPSelect(qryinvAmt, 1);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqryinvAmt);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {
                wfXmlResponseFI = new WFXmlResponse(xmlqryinvAmt);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);
                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    if (!isempty(wfxmllistFI.getVal("InvoiceAmount").trim())) {

                        invAmt = Double.parseDouble(wfxmllistFI.getVal("InvoiceAmount").trim());
                    }
                }
               mRepLogger.info("Invoice Amt=" + invAmt);
                if (invAmt > totalInvAmountLimit) {
                    obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                    obj.updateData("ExceptionType", "Discounts", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                    obj.updateData("DiscountFlag", "True", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                    obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                    flag = "true";
                   obj.GetWorkitem(winame);

                } else {
                    flag = "false";
                }
            } else {
                mRepLogger.info("Problem in GetData in discountcchk()");
            }
        } catch (Exception e) {
            mRepLogger.info("Exception Occurred in discountcchk  " + e);
        }



        mRepLogger.info("Discounts chk returning flag== " + flag + " ---> (true=Exception ,false= No Exception) ");
        
        return flag;
    }
    
    /********************************************************************
     *  Function Name                    : checkDuplicate
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : winame
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Duplicate check

* //Bug 1014736 - AP NGFuser code suggestion
     ************************************************************************/
    public String checkDuplicate(String winame) throws Exception {
        mRepLogger.info("*****Duplicity Check start*****");   
        String flag = "false";  //flag=false if not duplicate, if duplicate found return true

        boolean exactMatch = false;
        boolean similarMatch = false;
        double InvAmount = 0;
        String sInvDate = "";
        String sInvoiceNo = "";
        String sVendorCode = "";
        String sCurrency = "";
        String params = "";
        StringBuffer sbQuery;
        //Bug 1014736 - AP NGFuser code suggestion
        String qryfields = "Select InvoiceAmount,CONVERT(DATE, InvoiceDate) AS 'InvoiceDate',Currency,VendorCode,InvoiceNo from NG_AP_ExtAPProcess with(nolock) where TransactionID='" + winame + "'";
        String dateForDB = "";
        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        XMLParser parsergetlist = null;
        XMLParser parsergetlist1 = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        WFXmlResponse wfXmlResponseFI1 = null;
        WFXmlList wfxmllistFI1 = null;
        String xmlqryfields = obj.getAPSelect(qryfields, 5);
        parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqryfields);
        try {
            if (parsergetlist.getValueOf("MainCode").equals("0")) {
                wfXmlResponseFI = new WFXmlResponse(xmlqryfields);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);

                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    if (!isempty(wfxmllistFI.getVal("InvoiceAmount").trim())) {
                        InvAmount = Double.parseDouble(wfxmllistFI.getVal("InvoiceAmount").trim());
                    }

                    if (!isempty(wfxmllistFI.getVal("InvoiceDate").trim())) {
                        sInvDate = wfxmllistFI.getVal("InvoiceDate").trim();
                    }

                    if (!isempty(wfxmllistFI.getVal("Currency").trim())) {
                        sCurrency = wfxmllistFI.getVal("Currency").trim();
                    }

                    if (!isempty(wfxmllistFI.getVal("VendorCode").trim())) {
                        sVendorCode = wfxmllistFI.getVal("VendorCode").trim();
                    }

                    if (!isempty(wfxmllistFI.getVal("InvoiceNo").trim())) {
                        sInvoiceNo = wfxmllistFI.getVal("InvoiceNo").trim();
                    }
                }
                 mRepLogger.info("Fileds --> invoiceamount=" + InvAmount + " InvoiceDate=" + sInvDate + " Currency=" + sCurrency + " VendorCode=" + sVendorCode + " InvoiceNo=" + sInvoiceNo);
                if ((sInvoiceNo != null && !sInvoiceNo.equals(""))
                        && (sVendorCode != null && !sVendorCode.equals(""))
                        && (sInvDate != null && !sInvDate.equals(""))
                        && (InvAmount != 0)
                        && (sCurrency != null && !sCurrency.equals(""))) {
                    sbQuery = new StringBuffer();
                    //Bug 1014736 - AP NGFuser code suggestion
                    sbQuery.append("Select TransactionID,InvoiceType,VendorCode,InvoiceNo,InvoiceAmount,CONVERT(DATE, InvoiceDate) AS 'InvoiceDate' from NG_AP_ExtAPProcess with(nolock) ");
                    sbQuery.append(" Where InvoiceNo ='");
                    sbQuery.append(sInvoiceNo);
                    sbQuery.append("' and VendorCode ='");
                    sbQuery.append(sVendorCode);
                    sbQuery.append("' and CONVERT(DATE, InvoiceDate) ='");
                    sbQuery.append(sInvDate);
                    sbQuery.append("' and InvoiceAmount =");
                    sbQuery.append(InvAmount);
                    sbQuery.append(" and Currency ='");
                    sbQuery.append(sCurrency);
                    sbQuery.append("' and not TransactionID ='" + winame + "'");
                    mRepLogger.info("Exact match Duplicate check Query : " + sbQuery.toString());

                    parsergetlist = null;//to clear existing items form xmlList1
                    String xmlsbQuery = obj.getAPSelect(sbQuery.toString(), 6);
                    parsergetlist1 = new com.newgen.omni.wf.util.xml.XMLParser(xmlsbQuery);
                    try {
                        if (parsergetlist1.getValueOf("MainCode").equals("0") && Integer.parseInt(parsergetlist1.getValueOf("TotalRetrieved")) > 0) {
                            wfXmlResponseFI1 = new WFXmlResponse(xmlsbQuery);
                            wfxmllistFI1 = wfXmlResponseFI1.createList("Records", "Record");
                            wfxmllistFI1.reInitialize(true);
                            exactMatch = true;
                            mRepLogger.info("ExactMatch = True : " + winame);
                            params = "Invoice No,Vendor Code,InvoiceDate,InvoiceAmount,Currency";
                            obj.updateData("ActionFlag", "Duplicate", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                            obj.updateData("ExceptionType", "Duplicate Invoice", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                            obj.updateData("TargetWs", "Discard", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                            obj.GetWorkitem(winame);
                        }


                        if (parsergetlist1.getValueOf("MainCode").equals("18")) {
                            mRepLogger.info("MainCode 18- no records");
                            flag = "false";
                            exactMatch = false;
                        }

                        if (!exactMatch) {
                            mRepLogger.info("Inside Similar Match Check : Case 1 (InvoiceAmount Like)");
                            sbQuery = new StringBuffer();
                            sbQuery.append("Select TransactionID,InvoiceType,VendorCode,InvoiceNo,InvoiceAmount,CONVERT(DATE, InvoiceDate) AS 'InvoiceDate' from NG_AP_ExtAPProcess with(nolock) ");
                            sbQuery.append(" WHERE VendorCode ='");
                            sbQuery.append(sVendorCode);
                            sbQuery.append("' and InvoiceDate ='");
                            sbQuery.append(sInvDate);
                            sbQuery.append("' and InvoiceNo='");
                            sbQuery.append(sInvoiceNo);
                            sbQuery.append("' and Currency ='");
                            sbQuery.append(sCurrency);
                            sbQuery.append("' ");
                            sbQuery.append(" and not TransactionID ='" + winame + "'");
                            mRepLogger.info("Similar Match(Invoice Amount Like) Duplicate check Query : " + sbQuery.toString());
                            parsergetlist = null;   //to clear the items of this list as it is reused
                            xmlsbQuery = obj.getAPSelect(sbQuery.toString(), 6);
                            parsergetlist1 = new com.newgen.omni.wf.util.xml.XMLParser(xmlsbQuery);
                            try {
                                if (parsergetlist1.getValueOf("MainCode").equals("0") && Integer.parseInt(parsergetlist1.getValueOf("TotalRetrieved")) > 0) {
                                    wfXmlResponseFI1 = new WFXmlResponse(xmlsbQuery);
                                    wfxmllistFI1 = wfXmlResponseFI1.createList("Records", "Record");
                                    wfxmllistFI1.reInitialize(true);
                                   mRepLogger.info("wfxmllistFI in  Similar match case " + wfxmllistFI);
                                     similarMatch = true;
                                    flag = "true";
                                     mRepLogger.info("Similar Match = True : " + winame);
                                    mRepLogger.info("Matching against InvoiceAmount");
                                     } else if (parsergetlist1.getValueOf("MainCode").equals("18")) {
                                     mRepLogger.info("MainCode 18- no records");
                                    similarMatch = false;
                                    flag = "false";
                                } else {
                                   mRepLogger.info("Maincode not 0 in similar check case1");
                                }

                            } catch (Exception e) {
                                mRepLogger.info("Exception in similar chaeck case 1");
                            }


                            if (!similarMatch) {
                                 mRepLogger.info("Inside Similar Match Check : Case 2 (InvoiceNo Like)");
                                sbQuery = new StringBuffer();
                                sbQuery.append("Select TransactionID,InvoiceType,VendorCode,InvoiceNo,InvoiceAmount,CONVERT(DATE, InvoiceDate) AS 'InvoiceDate' from NG_AP_ExtAPProcess with(nolock)");
                                sbQuery.append(" WHERE VendorCode ='");
                                sbQuery.append(sVendorCode);
                                sbQuery.append("' and InvoiceDate ='");
                                sbQuery.append(sInvDate);
                                sbQuery.append("' and invoiceamount=");
                                sbQuery.append(InvAmount);
                                sbQuery.append(" and currency ='");
                                sbQuery.append(sCurrency);
                                sbQuery.append("' ");
                                sbQuery.append(" and not TransactionID ='" + winame + "'");
                                //System.out.println("Similar Match(Invoice No Like) Duplicate check Query : " + sbQuery.toString());
                                mRepLogger.info("Similar Match(Invoice No Like) Duplicate check Query : " + sbQuery.toString());
                                parsergetlist1 = null;  //to clear the items of this list as it is reused
                                xmlsbQuery = obj.getAPSelect(sbQuery.toString(), 6);
                                parsergetlist1 = new com.newgen.omni.wf.util.xml.XMLParser(xmlsbQuery);
                                try {
                                    if (parsergetlist1.getValueOf("MainCode").equals("0") && Integer.parseInt(parsergetlist1.getValueOf("TotalRetrieved")) > 0) {
                                        wfXmlResponseFI1 = new WFXmlResponse(xmlsbQuery);
                                        wfxmllistFI1 = wfXmlResponseFI1.createList("Records", "Record");
                                        wfxmllistFI1.reInitialize(true);
                                       similarMatch = true;
                                        flag = "true";
                                         mRepLogger.info("Similar Match = True : " + winame);
                                        mRepLogger.info("Matching against InvoiceNo");
                                        } else if (parsergetlist1.getValueOf("MainCode").equals("18")) {
                                        similarMatch = false;
                                        flag = "false";
                                    } else {
                                        mRepLogger.info("Maincode not 0 in similar check case2");
                                    }

                                } catch (Exception e) {
                                     mRepLogger.info("Exception in similar chaeck case 2");
                                }

                            }
                           if (!similarMatch) {
                                mRepLogger.info("Inside Similar Match Check : Case 3 (InvoiceDate Like)");
                                sbQuery = new StringBuffer();
                                sbQuery.append("Select TransactionID,InvoiceType,VendorCode,InvoiceNo,InvoiceAmount,CONVERT(DATE, InvoiceDate) AS 'InvoiceDate' from NG_AP_ExtAPProcess with(nolock)");
                                sbQuery.append(" WHERE VendorCode ='");
                                sbQuery.append(sVendorCode);
                                sbQuery.append("' and InvoiceNo ='");
                                sbQuery.append(sInvoiceNo);
                                sbQuery.append("' and InvoiceAmount=");
                                sbQuery.append(InvAmount);
                                sbQuery.append(" and Currency ='");
                                sbQuery.append(sCurrency);
                                sbQuery.append("' ");
                                sbQuery.append(" and not TransactionID ='" + winame + "'");
                                mRepLogger.info("Similar Match(Invoice No Like) Duplicate check Query : " + sbQuery.toString());
                                parsergetlist = null;   //to clear the items of this list as it is reused

                                xmlsbQuery = obj.getAPSelect(sbQuery.toString(), 6);
                                parsergetlist1 = new com.newgen.omni.wf.util.xml.XMLParser(xmlsbQuery);
                                try {
                                    if (parsergetlist1.getValueOf("MainCode").equals("0") && Integer.parseInt(parsergetlist1.getValueOf("TotalRetrieved")) > 0) {
                                        wfXmlResponseFI1 = new WFXmlResponse(xmlsbQuery);
                                        wfxmllistFI1 = wfXmlResponseFI1.createList("Records", "Record");
                                        wfxmllistFI1.reInitialize(true);
                                        similarMatch = true;
                                        mRepLogger.info("Similar Match = True : " + winame);
                                        mRepLogger.info("Matching against InvoiceDate");
                                    } else if (parsergetlist1.getValueOf("MainCode").equals("18")) {
                                        mRepLogger.info("MainCode 18- no records");
                                        similarMatch = false;
                                        flag = "false";
                                    } else {
                                        mRepLogger.info("Maincode not 0 in similar check case3");
                                    }

                                } catch (Exception e) {
                                    mRepLogger.info("Exception in similar chaeck case 3");
                                }
                            }
                        }
                        if (!exactMatch && !similarMatch) {
                            mRepLogger.info("No Match found");
                            flag = "false";
                        }


                    } catch (Exception e) {
                        mRepLogger.info("Exception in getdata for sbQuery");
                    }
                }

            } else {
                mRepLogger.info("Problem in APSelectData in checkDuplicate()");
            }
        } catch (Exception e) {
            mRepLogger.info("Exception Occurred in checkDuplicate  " + e);
        }
        if (exactMatch) {
            flag = "true";
        } else if (similarMatch) { //set flag as true
            flag = "true";
            obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            obj.updateData("ExceptionType", "Duplicate Invoice", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");//added by ksivashankar
            obj.GetWorkitem(winame);
        } else {
            flag = "false";// no duplicates
        }

         mRepLogger.info("ExactMatch=" + exactMatch + " similar Match=" + similarMatch + " and returning flag=" + flag + " ---> (true=Exception ,false= No Exception)");
        return flag;
    }

    /********************************************************************
     *  Function Name                    : PO3WayMatch
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : winame
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : PO3WayMatch check

     ************************************************************************/
    public String PO3WayMatch(String winame) throws Exception {
        mRepLogger.info("****Inside PO3WayMatch method***");
         String flag = "false";   //flag false then set actionfalg exception and route to exception
        double invAmt = 0;
        double grAmt = 0;
        double taxAmt = 0;
        double toleranceAmt = 0;
        String result = "notequal";//to store result of caomparison as flag
        String qrytoInvAmt = "Select InvoiceAmount,TaxAmount from NG_AP_ExtAPProcess with(nolock) where TransactionID='" + winame + "'";
        XMLParser parsergetlist = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        try {
            String xmlqrytoInvAmt = obj.getAPSelect(qrytoInvAmt, 2);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoInvAmt);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {

                wfXmlResponseFI = new WFXmlResponse(xmlqrytoInvAmt);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);

                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    if (Double.parseDouble(wfxmllistFI.getVal("InvoiceAmount")) != 0) {

                        invAmt = Double.parseDouble(wfxmllistFI.getVal("InvoiceAmount"));
                    }
                    if (Double.parseDouble(wfxmllistFI.getVal("TaxAmount")) != 0) {

                        taxAmt = Double.parseDouble(wfxmllistFI.getVal("TaxAmount"));
                    }

                }
                mRepLogger.info("Invoice Amount in NG_AP_ExtAPProcess=" + invAmt);
                mRepLogger.info("Tax Amount in NG_AP_ExtAPProcess=" + taxAmt);

            } else {
                mRepLogger.info("maincode not 0 in getdata");

            }
        } catch (Exception e) {
            mRepLogger.info("Problem in executing APSelect in NG_AP_ExtAPProcess");

        }


        wfXmlResponseFI = null; //set to null to clear data to reuse
        wfxmllistFI = null;  //set to null to clear data to reuse
        String qrytoGRNAmt = "SELECT amount FROM NG_AP_Cmplx_GRDetails with(nolock) WHERE WIName='" + winame + "'";

        try {
            String xmlqrytoGRNAmt = obj.getAPSelect(qrytoGRNAmt, 1);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoGRNAmt);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {

                wfXmlResponseFI = new WFXmlResponse(xmlqrytoGRNAmt);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);

                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    grAmt = grAmt + Double.parseDouble(wfxmllistFI.getVal("amount").replaceAll(",", ""));
                }
                mRepLogger.info("GR Amount in NG_AP_Cmplx_GRDetails=" + grAmt);

            } else {
                mRepLogger.info("maincode not 0 in ApSelect");

            }
        } catch (Exception e) {
            mRepLogger.info("Problem in execting Apselect in NG_AP_Cmplx_GRDetails");

        }

        wfXmlResponseFI = null; //set to null to clear data to reuse
        wfxmllistFI = null;  //set to null to clear data to reuse
        String qrytoToleranceAmt = "SELECT TOP(1) ToleranceLimit  FROM NG_AP_Master_Tolerance with(nolock)";

        try {
            String xmlqrytoToleranceAmt = obj.getAPSelect(qrytoToleranceAmt, 1);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoToleranceAmt);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {

                wfXmlResponseFI = new WFXmlResponse(xmlqrytoToleranceAmt);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);

                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {

                    tolerancelimit = Double.parseDouble(wfxmllistFI.getVal("ToleranceLimit").replaceAll(",", ""));
                }

                mRepLogger.info("ToleranceLimit Amount is =" + tolerancelimit);

            } else {
                mRepLogger.info("maincode not 0 in ApSelect");

            }
        } catch (Exception e) {
            mRepLogger.info("Problem in execting Apselect in ToleranceLimit");

        }

        try {
            if (grAmt != 0 || invAmt != 0) {
                mRepLogger.info("Tolerance Limit=" + tolerancelimit);

                toleranceAmt = ((tolerancelimit * invAmt) / 100);
                mRepLogger.info("Tolerance Amount=" + toleranceAmt);
                invAmt = invAmt - taxAmt;
                mRepLogger.info("invAmt  == " + invAmt + "   ********   grAmt  == " + grAmt);
                if (invAmt == grAmt) {
                    result = "equal";
                    flag = "true";
                    mRepLogger.info("Setting result as " + result + " as InvAmt is equal to grAmt");
                }
                if (invAmt > grAmt) {
                    if (((invAmt - grAmt) == toleranceAmt) || ((invAmt - grAmt) < toleranceAmt)) {
                        result = "equal";
                        flag = "true";
                        mRepLogger.info("Setting result as " + result + " as difference of invoiceAmt and grAmt is within tolerance limit");
                    } else {
                        result = "notequal";  //send WI to exception and set filer to procurement user at WS level in process
                        flag = "false";
                        mRepLogger.info("result=notequal as invAmt>grAmt");
                    }

                }


                if (grAmt > invAmt) {
                    if (((grAmt - invAmt) == toleranceAmt) || ((grAmt - invAmt) < toleranceAmt)) {
                        result = "equal";
                        flag = "true";
                        mRepLogger.info("Setting result as " + result + " as difference of invoiceAmt and grAmt is within tolerance limit");
                    } else {
                        result = "notequal";
                        flag = "false";
                       mRepLogger.info("result=notequal as grAmt>invAmt");
                    }

                }


            } else {
                mRepLogger.info("Some problem in Calculations!! check invoice Amount or GRN Amount");
            }
        } catch (ArithmeticException e) {
            mRepLogger.info("Exception in calculations -- " + e);
        }
        if (result.equals("equal") && flag.equals("true")) {
           mRepLogger.info("updating targetWS flag to invoicePosting and calling completeWI call");
            obj.updateData("ActionFlag", "InvoicePosting", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            obj.updateData("ExceptionType", "NULL", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            obj.updateData("TargetWS", "InvoicePosting", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            }
        if (result.equals("notequal") && flag.equals("false")) {
            mRepLogger.info("updating ActionFlag  to Exception and calling completeWI call");
            obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            obj.updateData("ExceptionType", "3-Way Match Failure", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            obj.updateData("TargetWS", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
            obj.GetWorkitem(winame);
        }

        mRepLogger.info("**** PO3WayMatch *** flag= " + flag + " ---> (true=No Exception ,false= Exception)");
        return flag;
    }

    /********************************************************************
     *  Function Name                    : POGRchk
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : winame
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : POGRchk check

     ************************************************************************/
    public String POGRchk(String winame) {
        mRepLogger.info("***Inside POGRchk***");

        String flag = "true";   // if this check passed then flag is true if any Exception then flag=flase
mRepLogger.info("Inside PONumber is available or not Check : Case 1 ");
        String qryPONum = "SELECT InvoiceType,PONumber,PreviousPO,ExceptionType FROM NG_AP_ExtAPProcess p with(nolock) where TransactionID='" + winame + "'";
        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        XMLParser parsergetlist = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        String Invoicetype = "";
        String PONum = "";
        String PreviousPO = "";
        String ExceptionType = "";
        String GRNNo = "";
        String Taxcode = "";
        double POAmount = 0;
        double POOpenAmount = 0;
        String VendorName = "";
        String VendorCode = "";
        String BusinessArea = "";
        String CompanyCode = "";
        String TaxCode = "";
        String OrderType = "";
        String SAPPONumber = "";
        String SAPOrderType = "";
        int GRNNoCount = 0;

        try {
            String xmlqryPONum = obj.getAPSelect(qryPONum, 4);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqryPONum);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {
                wfXmlResponseFI = new WFXmlResponse(xmlqryPONum);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);
                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {

                    if (!isempty(wfxmllistFI.getVal("InvoiceType").trim())) {
                        Invoicetype = wfxmllistFI.getVal("InvoiceType").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("PONumber").trim())) {
                        PONum = wfxmllistFI.getVal("PONumber").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("PreviousPO").trim())) {
                        PreviousPO = wfxmllistFI.getVal("PreviousPO").trim();
                    }
                    if (!isempty(wfxmllistFI.getVal("ExceptionType").trim())) {
                        ExceptionType = wfxmllistFI.getVal("ExceptionType").trim();
                    }
                }
                mRepLogger.info("Inside PONumber and OrderType From SAP DATA ");
                String qrytoSAPPONum = "SELECT PONumber,OrderType FROM NG_AP_SAPMaster_PODetails with(nolock) WHERE PONumber ='" + PONum + "'";

                try {
                    String xmlqrytoqrytoSAPPONum = obj.getAPSelect(qrytoSAPPONum, 2);
                    parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoSAPPONum);
                    if (parsergetlist.getValueOf("MainCode").equals("0")) {

                        wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoSAPPONum);
                        wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                        wfxmllistFI.reInitialize(true);

                        for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                            if (!isempty(wfxmllistFI.getVal("PONumber").trim())) {
                                SAPPONumber = wfxmllistFI.getVal("PONumber").trim();
                            }
                            if (!isempty(wfxmllistFI.getVal("OrderType").trim())) {
                                SAPOrderType = wfxmllistFI.getVal("OrderType").trim();
                            }
                        }
                        mRepLogger.info("SAPPONumber=" + SAPPONumber);
                        mRepLogger.info("SAPOrderType=" + SAPOrderType);
                        if (Invoicetype.equalsIgnoreCase("PO Based")) {
                            if (obj.chkEmpty(SAPPONumber)) { // SAPPONumber is empty
                                mRepLogger.info("Inside SAPPONumber is not available...");
                                mRepLogger.info("going to update action flag");
                                obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                obj.updateData("ExceptionType", "Invalid PO No", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                obj.GetWorkitem(winame);
                                flag = "false";

                            } else {
                                mRepLogger.info("Inside GRNSCNNo is available or not Check : Case 2 ");
                                mRepLogger.info("Inside GRNSCNNo Multiples are available or not Check : Case 3 ");
                                String qrytoGRNNoCount = "Select count(GRNSCNNo) AS GRNNoCount from NG_AP_SAPMaster_GRDetails with(nolock) WHERE PONumber='" + PONum + "'";

                                try {
                                    String xmlqrytoGRNNoCount = obj.getAPSelect(qrytoGRNNoCount, 1);
                                    parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoGRNNoCount);
                                    if (parsergetlist.getValueOf("MainCode").equals("0")) {

                                        wfXmlResponseFI = new WFXmlResponse(xmlqrytoGRNNoCount);
                                        wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                                        wfxmllistFI.reInitialize(true);

                                        for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {

                                            if (!isempty(wfxmllistFI.getVal("GRNNoCount").trim())) {
                                                GRNNoCount = Integer.parseInt(wfxmllistFI.getVal("GRNNoCount").trim());
                                            }
                                        }
                                        mRepLogger.info("GRNNoCount=" + GRNNo);
                                        if (SAPOrderType.equalsIgnoreCase("Not blanket")) {
                                            if (GRNNoCount == 0) { //GRNNo is missing
                                                mRepLogger.info("Inside GRNno is not available.. ");
                                                obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                obj.updateData("ExceptionType", "Missing GR", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                obj.GetWorkitem(winame);
                                                flag = "false";

                                            }
                                        }

                                    } else {
                                        mRepLogger.info("maincode not 0 in getdata");

                                    }
                                } catch (Exception e) {
                                    mRepLogger.info("Problem in execting APSelect in GRNNoCount");

                                }

                                if (!flag.equalsIgnoreCase("false")) {
                                    mRepLogger.info("Inside Taxcode is available or not Check : Case 4 ");
                                    String qrytoTaxcode = "Select DISTINCT(TaxCode) from NG_AP_SAPMaster_GRDetails with(nolock) WHERE PONumber='" + PONum + "'";

                                    try {
                                        String xmlqrytoqrytoTaxcode = obj.getAPSelect(qrytoTaxcode, 1);
                                        parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoTaxcode);
                                        if (parsergetlist.getValueOf("MainCode").equals("0")) {

                                            wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoTaxcode);
                                            wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                                            wfxmllistFI.reInitialize(true);

                                            for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {

                                                if (!isempty(wfxmllistFI.getVal("TaxCode").trim())) {
                                                    Taxcode = wfxmllistFI.getVal("TaxCode").trim();
                                                }
                                            }
                                            mRepLogger.info("TaxCode=" + Taxcode);
                                            if (obj.chkEmpty(Taxcode)) { // Taxcode is empty
                                                mRepLogger.info("Inside Taxcode is not available...");
                                                mRepLogger.info("going to update action flag");
                                                obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                obj.updateData("ExceptionType", "Tax Code Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                obj.GetWorkitem(winame);
                                                flag = "false";

                                            }

                                        } else {
                                            mRepLogger.info("maincode not 0 in getdata");

                                        }
                                    } catch (Exception e) {
                                        mRepLogger.info("Problem in execting APSelect in Taxcode");

                                    }

                                }
                                if (!flag.equalsIgnoreCase("false")) {

                                    if ((ExceptionType.equalsIgnoreCase("Multiple GR Found") || !ExceptionType.equalsIgnoreCase("Multiple GR Found"))
                                            && !PreviousPO.equalsIgnoreCase(PONum)) {
                                        mRepLogger.info("\n***Going Update NG_AP_ExtAPProcess table & insert to NG_AP_Cmplx_PODetails table starts***\n");
                                        mRepLogger.info("Inside getting the values from NG_AP_SAPMaster_PODetails ");
                                        String qrytoMasterPODetails = "SELECT PONumber,POAmount,POOpenAmount,VendorName,VendorCode,BusinessArea,CompanyCode,TaxCode,OrderType "
                                                + "FROM NG_AP_SAPMaster_PODetails with(nolock) WHERE PONumber ='" + PONum + "'";

                                        try {
                                            String xmlqrytoqrytoMasterPODetails = obj.getAPSelect(qrytoMasterPODetails, 9);
                                            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoMasterPODetails);
                                            if (parsergetlist.getValueOf("MainCode").equals("0")) {

                                                wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoMasterPODetails);
                                                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                                                wfxmllistFI.reInitialize(true);

                                                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                                                   if (!isempty(wfxmllistFI.getVal("PONumber").trim())) {
                                                        PONum = wfxmllistFI.getVal("PONumber").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("POAmount").trim())) {
                                                        POAmount = Double.parseDouble(wfxmllistFI.getVal("POAmount").trim().replaceAll(",", ""));
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("POOpenAmount").trim())) {
                                                        POOpenAmount = Double.parseDouble(wfxmllistFI.getVal("POOpenAmount").trim().replaceAll(",", ""));
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("VendorName").trim())) {
                                                        VendorName = wfxmllistFI.getVal("VendorName").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("VendorCode").trim())) {
                                                        VendorCode = wfxmllistFI.getVal("VendorCode").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("BusinessArea").trim())) {
                                                        BusinessArea = wfxmllistFI.getVal("BusinessArea").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("CompanyCode").trim())) {
                                                        CompanyCode = wfxmllistFI.getVal("CompanyCode").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("TaxCode").trim())) {
                                                        Taxcode = wfxmllistFI.getVal("TaxCode").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("OrderType").trim())) {
                                                        OrderType = wfxmllistFI.getVal("OrderType").trim();
                                                    }
                                                    obj.updateData("VendorName", VendorName, "NG_AP_ExtAPProcess", "PONumber='" + PONum + "'");
                                                    obj.updateData("VendorCode", VendorCode, "NG_AP_ExtAPProcess", "PONumber='" + PONum + "'");
                                                    obj.updateData("BusinessArea", BusinessArea, "NG_AP_ExtAPProcess", "PONumber='" + PONum + "'");
                                                    obj.updateData("CompanyCode", CompanyCode, "NG_AP_ExtAPProcess", "PONumber='" + PONum + "'");
                                                    obj.updateData("TaxCode", Taxcode, "NG_AP_ExtAPProcess", "PONumber='" + PONum + "'");
                                                    obj.updateData("OrderType", OrderType, "NG_AP_ExtAPProcess", "PONumber='" + PONum + "'");
                                                    obj.deleteRow("NG_AP_Cmplx_PODetails", "PONumber", PONum);
                                                    String sTablename = "NG_AP_Cmplx_PODetails";
                                                    obj.insertCmplxPODetailsQuery(sTablename, winame, PONum, Double.toString(POAmount), Double.toString(POOpenAmount));

                                                }

                                            } else {
                                               mRepLogger.info("maincode not 0 in getdata");

                                            }
                                        } catch (Exception e) {
                                             mRepLogger.info("Problem in execting APSelect in getting the values from NG_AP_SAPMaster_PODetails");

                                        }
                                        mRepLogger.info("\n***Update NG_AP_ExtAPProcess table & insert to NG_AP_Cmplx_PODetails table ended***\n");

                                        mRepLogger.info("\n***Going Insert to NG_AP_Cmplx_GRDetails table starts***\n");

                                        String GRNSCNNo = null;
                                        String PONumber = null;
                                        String POItemNumber = null;
                                        String ProductCode = null;
                                        String CostCentre = null;
                                        String Description = null;
                                        String UnitPrice = null;
                                        String OrderUnit = null;
                                        String Quantity = null;

                                        mRepLogger.info("Inside getting the values from NG_AP_SAPMaster_GRDetails ");
                                        String qrytoMasterGRDetails = "SELECT GRNSCNNo,PONumber,POItemNumber,ProductCode,CostCentre,Description,"
                                                + "UnitPrice,OrderUnit,Quantity,Amount,TaxCode "
                                                + "FROM NG_AP_SAPMaster_GRDetails with(nolock) WHERE PONumber ='" + PONum + "'";

                                        try {
                                            String xmlqrytoqrytoMasterGRDetails = obj.getAPSelect(qrytoMasterGRDetails, 11);
                                            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoMasterGRDetails);
                                            if (parsergetlist.getValueOf("MainCode").equals("0")) {

                                                wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoMasterGRDetails);
                                                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                                                wfxmllistFI.reInitialize(true);

                                                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                                                    if (!isempty(wfxmllistFI.getVal("GRNSCNNo").trim())) {
                                                        GRNSCNNo = wfxmllistFI.getVal("GRNSCNNo").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("PONumber").trim())) {
                                                        PONumber = wfxmllistFI.getVal("PONumber").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("POItemNumber").trim())) {
                                                        POItemNumber = wfxmllistFI.getVal("POItemNumber").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("ProductCode").trim())) {
                                                        ProductCode = wfxmllistFI.getVal("ProductCode").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("CostCentre").trim())) {
                                                        CostCentre = wfxmllistFI.getVal("CostCentre").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("Description").trim())) {
                                                        Description = wfxmllistFI.getVal("Description").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("UnitPrice").trim())) {
                                                        UnitPrice = wfxmllistFI.getVal("UnitPrice").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("OrderUnit").trim())) {
                                                        OrderUnit = wfxmllistFI.getVal("OrderUnit").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("Quantity").trim())) {
                                                        Quantity = wfxmllistFI.getVal("Quantity").trim();
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("Amount").trim())) {
                                                        POAmount = Double.parseDouble(wfxmllistFI.getVal("Amount").trim().replaceAll(",", ""));
                                                    }
                                                    if (!isempty(wfxmllistFI.getVal("TaxCode").trim())) {
                                                        TaxCode = wfxmllistFI.getVal("TaxCode").trim();
                                                    }
                                                    obj.deleteRow("NG_AP_Cmplx_GRDetails", "PONumber", PONum);
                                                    String sTablename = "NG_AP_Cmplx_GRDetails";
                                                    obj.insertCmplxGRDetailsQuery(winame, sTablename, GRNSCNNo, PONumber, POItemNumber, ProductCode,
                                                            CostCentre, Description, UnitPrice, OrderUnit, Quantity, Double.toString(POAmount), TaxCode);

                                                    if (ExceptionType.equalsIgnoreCase("Multiple GR Found") && PreviousPO.equalsIgnoreCase(PONum)) {
                                                    } else {
                                                        if (GRNNoCount > 1) { // Multiple GRNNoCount
                                                            mRepLogger.info("Inside GRNNo Multiples are available...");
                                                            mRepLogger.info("going to update action flag");
                                                            obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                            obj.updateData("PreviousPO", PONum, "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                            obj.updateData("ExceptionType", "Multiple GR Found", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                            obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                                                            obj.GetWorkitem(winame);
                                                            flag = "false";

                                                        }
                                                    }
                                                }
                                            } else {
                                                mRepLogger.info("maincode not 0 in getdata");

                                            }
                                        } catch (Exception e) {
                                            mRepLogger.info("Problem in execting APSelect in getting the values from NG_AP_SAPMaster_PODetails");

                                        }
                                            mRepLogger.info("\n***insert to NG_AP_Cmplx_GRDetails table ends***\n");
                                    }
                                }
                            }
                        } else {
                            mRepLogger.info("*** POGRCHK is only for PO Based Invoices ****");
                        }

                    } else {
                        mRepLogger.info("maincode not 0 in getdata");

                    }
                } catch (Exception e) {
                    mRepLogger.info("Problem in execting APSelect in Taxcode");

                }
                mRepLogger.info("Invoicetype=" + Invoicetype);
                mRepLogger.info("PONUm=" + PONum);
                if (Invoicetype.equalsIgnoreCase("PO Based")) {
                    if (obj.chkEmpty(PONum)) { //PONum is missing
                        mRepLogger.info("Inside PONumber is not available..");
                        mRepLogger.info("going to update action flag");
                        obj.updateData("ActionFlag", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                        obj.updateData("ExceptionType", "Invalid PO No", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                        obj.updateData("TargetWs", "Exception", "NG_AP_ExtAPProcess", "TransactionID='" + winame + "'");
                        obj.GetWorkitem(winame);
                        flag = "false";
                    }
                } else {
                    mRepLogger.info("*** POGRCHK is only for PO Based Invoices ****");
                }
            } else {
                mRepLogger.info("Problem in GetData in MissingPO");
            }
        } catch (Exception e) {
            mRepLogger.info("Exception Occurred in Missing PO " + e);
        }

        mRepLogger.info("*** POGRchk finished*** with flag =" + flag + " ---> (true=check passed ,false= check failed)");
         return flag;

    }

    /********************************************************************
     *  Function Name                    : deleteRow
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : String TableName, String WhereClause, String WhereClauseValue
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : Deleting row in a table

     ************************************************************************/
    public static boolean deleteRow(String TableName, String WhereClause, String WhereClauseValue) {
        mRepLogger.info("Delete Row Method was called");
        StringBuilder inputxml = new StringBuilder();
        inputxml = inputxml.append("<?xmlversion=\"1.0\"?>");
        inputxml = inputxml.append("<APDelete>");
        inputxml = inputxml.append("<Option>APDelete</Option>");
        inputxml = inputxml.append("<ProcessDefId>").append(sPrcsDefId).append("</ProcessDefId>");
        inputxml = inputxml.append("<Status>true</Status>");
        inputxml = inputxml.append("<SessionId>").append(strSessionId).append("</SessionId>");
        inputxml = inputxml.append("<EngineName>").append(strCabName).append("</EngineName>");
        inputxml = inputxml.append("<TableName>").append(TableName).append("</TableName>");
        inputxml = inputxml.append("<ColName></ColName>");
        inputxml = inputxml.append("<WhereClause>").append(WhereClause).append("='").append(WhereClauseValue).append("'</WhereClause>");
        inputxml = inputxml.append("</APDelete>");
        mRepLogger.info("inputxml==" + inputxml);
        XMLParser parsergetlist = null;
        String strCompOutputXml = null;
        try {
            strCompOutputXml = WFCallBroker.execute(inputxml.toString(), strIP, Integer.parseInt("3333"), 0);
            mRepLogger.info("strCompOutputXml==" + strCompOutputXml);
            parsergetlist = new XMLParser(strCompOutputXml);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (strCompOutputXml != null) {
                strCompOutputXml = null;
            }
            if (parsergetlist != null) {
                parsergetlist = null;
            }
            if (inputxml != null) {
                inputxml = null;
            }
        }
    }

    /********************************************************************
     *  Function Name                    : getFolderIndexFromWIName
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : winame
     *  Output Parameters                : int
     *  Return Values                    : int
     *  Description                      : get FolderIndex From WIName 

     ************************************************************************/
    public int getFolderIndexFromWIName(String winame) {
        XMLParser parsergetlist = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        int FolderIndx = 0;
        NGDeloitteAutoProcess obj = new NGDeloitteAutoProcess();
        mRepLogger.info("Inside getFolderIndexFromWIName Method ");
        String qrytoFolderIndx = "Select FOLDERINDEX from pdbfolder with(nolock) where NAME='" + winame + "'";

        try {
            String xmlqrytoqrytoFolderIndx = obj.getAPSelect(qrytoFolderIndx, 1);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoFolderIndx);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {

                wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoFolderIndx);
                wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                wfxmllistFI.reInitialize(true);

                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    if (!isempty(wfxmllistFI.getVal("FOLDERINDEX").trim())) {
                        FolderIndx = Integer.parseInt(wfxmllistFI.getVal("FOLDERINDEX").trim());
                    }
                }
                mRepLogger.info("FolderIndx=" + FolderIndx);

            } else {
                mRepLogger.info("maincode not 0 in getdata");

            }
        } catch (Exception e) {
            mRepLogger.info("Problem in execting APSelect in FolderIndx");

        }
        return FolderIndx;
    }

     /********************************************************************
     *  Function Name                    : main
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : args
     *  Output Parameters                : 
     *  Return Values                    : 
     *  Description                      : AutoProcessing Utility 

     ************************************************************************/
    public static void main(String[] args) {
        
        NGDeloitteAutoProcess autoprocessObj = new NGDeloitteAutoProcess();
        InitLog();
        String downloadedfilepath = null;
        mRepLogger.info("****Utility Starts*****");
        DMSXmlResponse xmlResponse = null;
        DMSXmlResponse xmlRespInvType = null;
        DMSXmlResponse xmlRespAutoProcessingWI = null;
        DMSXmlList xmlList = null;
        DMSXmlList xmlList1 = null;

        XMLParser parsergetlist = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;

        String flag;
        autoprocessObj.ReadProperty();

        String invoiceType = "";
        String orderType = "";
        try {
            userName = autoprocessObj.strUsername;
            password = autoprocessObj.strPwd;
            DMSXmlResponse xmlReslock = null;
            mRepLogger.info("Connecting Cabinet");
            autoprocessObj.conCabinet();
            mRepLogger.info("Cabinet Connected");
            String qryAutoProcessingWI = "SELECT b.ProcessInstanceID FROM NG_AP_ExtAPProcess a with(nolock),WFINSTRUMENTTABLE b with(nolock) WHERE a.ItemIndex=b.var_rec_1 AND b.ActivityName='AutoProcessing'";
            mRepLogger.info("Qry to get list of WI in autoprocessing step, qryAutoProcessingWI--> " + qryAutoProcessingWI);
            String xmlqryAutoProcessingWI = autoprocessObj.getAPSelect(qryAutoProcessingWI, 1);
            mRepLogger.info("xmlqryAutoProcessingWI=" + xmlqryAutoProcessingWI);
xmlResponse = new DMSXmlResponse(xmlqryAutoProcessingWI);

            if (xmlResponse.getVal("MainCode").equals("0")) {   // If 1				
                xmlList = xmlResponse.createList("Records", "Record");
                mRepLogger.info("Xmllist=" + xmlList);

                for (; xmlList.hasMoreElements(true); xmlList.skip(true)) {  //for loop 1


                    if (!isempty(xmlList.getVal("ProcessInstanceID"))) {
                        transactionId = xmlList.getVal("ProcessInstanceID");
                    }
                    mRepLogger.info("TransactionId for processing =" + transactionId);
                    mRepLogger.info("downloadPath before calling getFolderIndexFromWIName Method ==== " + downloadPath);
                    if (downloadPath != "" && downloadPath != null) {                 
                        int FolderIndx = autoprocessObj.getFolderIndexFromWIName(transactionId);
                        downloadedfilepath = autoprocessObj.getDocList(Integer.toString(FolderIndx), downloadPath);
                        mRepLogger.info("downloadedfilepath after getting FolderIndex ------> " + downloadedfilepath);
                        if (!isempty(downloadedfilepath)) {
                            if (!autoprocessObj.pswdprotected(transactionId, downloadedfilepath)) { //if file is not pswd protected go for next check
                                flag = autoprocessObj.dataExtractionChk(transactionId);
                                if (flag.equals("true")) {
                                   flag = autoprocessObj.missingPO(transactionId);
                                    if (flag.equals("false")) {  //continue if pO is not misssing--- false is po not misssing
                                        if (autoprocessObj.checkDuplicate(transactionId).equalsIgnoreCase("false")) {  
                                            flag = autoprocessObj.POGRchk(transactionId);
                                            if (flag.equals("true")) {
                                                mRepLogger.info("***Inside Getting and checking InvoiceType and OrderType starts***");
                                                String InvoiceType = "";
                                                String OrderType = "";
                                                String DiscountFlag = "";
                                                mRepLogger.info("Inside Getting Invoice Type and Order Type Query ");
                                                String qrytoInvoiceOrderType = "Select InvoiceType,OrderType,DiscountFlag from NG_AP_ExtAPProcess with(nolock) where TransactionID='" + transactionId + "'";

                                                try {
                                                    String xmlqrytoqrytoInvoiceOrderType = autoprocessObj.getAPSelect(qrytoInvoiceOrderType, 3);
                                                    parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoInvoiceOrderType);
                                                    if (parsergetlist.getValueOf("MainCode").equals("0")) {

                                                        wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoInvoiceOrderType);
                                                        wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                                                        wfxmllistFI.reInitialize(true);

                                                        for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                                                             if (!isempty(wfxmllistFI.getVal("InvoiceType").trim())) {
                                                                InvoiceType = wfxmllistFI.getVal("InvoiceType").trim();
                                                            }
                                                            if (!isempty(wfxmllistFI.getVal("OrderType").trim())) {
                                                                OrderType = wfxmllistFI.getVal("OrderType").trim();
                                                            }
                                                            if (!isempty(wfxmllistFI.getVal("DiscountFlag").trim())) {
                                                                DiscountFlag = wfxmllistFI.getVal("DiscountFlag").trim();
                                                            }
                                                        }
                                                       mRepLogger.info("InvoiceType=" + InvoiceType);
                                                        mRepLogger.info("OrderType=" + OrderType);
                                                         mRepLogger.info("DiscountFlag=" + DiscountFlag);

                                                    } else {
                                                        mRepLogger.info("maincode not 0 in getdata");

                                                    }
                                                } catch (Exception e) {
                                                     mRepLogger.info("Problem in execting APSelect in InvoiceType,OrderType,DiscountFlag from NG_AP_ExtAPProcess with(nolock)");

                                                }
                                               mRepLogger.info("***Inside Getting and checking InvoiceType and OrderType ends***");
                                                if (!autoprocessObj.chkEmpty(InvoiceType) && !autoprocessObj.chkEmpty(OrderType)) {
                                                    if (InvoiceType.equalsIgnoreCase("PO Based") && !OrderType.equalsIgnoreCase("blanket")) {//PO3WAY Check
                                                         flag = autoprocessObj.PO3WayMatch(transactionId);
                                                        if (flag.equals("true")) {
                                                           String IndexingRqdFlag = "";
                                                            mRepLogger.info("Inside Getting IndexingRequired FROM NG_AP_Master_IndexingRqd ");
                                                            String qrytoIndexingRqdFlag = "SELECT IndexingRequired FROM NG_AP_Master_IndexingRqd with(nolock)";

                                                            try {
                                                                String xmlqrytoqrytoIndexingRqdFlag = autoprocessObj.getAPSelect(qrytoIndexingRqdFlag, 3);
                                                                parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoIndexingRqdFlag);
                                                                if (parsergetlist.getValueOf("MainCode").equals("0")) {

                                                                    wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoIndexingRqdFlag);
                                                                    wfxmllistFI = wfXmlResponseFI.createList("Records", "Record");
                                                                    wfxmllistFI.reInitialize(true);

                                                                    for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                                                                        if (!isempty(wfxmllistFI.getVal("IndexingRequired").trim())) {
                                                                            IndexingRqdFlag = wfxmllistFI.getVal("IndexingRequired").trim();
                                                                        }
                                                                    }
                                                                    mRepLogger.info("IndexingRqdFlag=" + IndexingRqdFlag);

                                                                } else {
                                                                    mRepLogger.info("maincode not 0 in getdata");

                                                                }
                                                            } catch (Exception e) {
                                                                mRepLogger.info("Problem in execting APSelect in IndexingRequired FROM NG_AP_Master_IndexingRqd");

                                                            }

                                                            if (!DiscountFlag.equalsIgnoreCase("True")) {
                                                                if (autoprocessObj.discountsChk(transactionId).equals("false")) {
                                                                    mRepLogger.info("\n+++++++++++++DiscountFlag Not True++++++++++++++++++++++");
                                                                    mRepLogger.info("All cehcks Passed--update the TargetWS flag and routing Wi to InvociePosting stage");
                                                                     mRepLogger.info("+++++++++++++++++++++++++++++++++++");
                                                                   if (IndexingRqdFlag.equalsIgnoreCase("Yes")) {
                                                                        autoprocessObj.updateData("IndexingRqdFlag", IndexingRqdFlag, "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                        autoprocessObj.updateData("ActionFlag", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                        autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                        autoprocessObj.updateData("TargetWs", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                    } else {
                                                                        autoprocessObj.updateData("ActionFlag", "InvoicePosting", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                        autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                        autoprocessObj.updateData("TargetWs", "InvoicePosting", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                    }
                                                                   autoprocessObj.GetWorkitem(transactionId);
                                                                }
                                                            } else {
                                                                mRepLogger.info("\n+++++++++++++DiscountFlag True++++++++++++++++++");
                                                               mRepLogger.info("All cehcks Passed--update the TargetWS flag and routing Wi to InvociePosting stage");
                                                                mRepLogger.info("+++++++++++++++++++++++++++++++++++");
                                                                if (IndexingRqdFlag.equalsIgnoreCase("Yes")) {
                                                                    autoprocessObj.updateData("IndexingRqdFlag", IndexingRqdFlag, "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                    autoprocessObj.updateData("ActionFlag", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                    autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                    autoprocessObj.updateData("TargetWs", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                } else {
                                                                    autoprocessObj.updateData("ActionFlag", "InvoicePosting", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                    autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                    autoprocessObj.updateData("TargetWs", "InvoicePosting", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                }
                                                                autoprocessObj.GetWorkitem(transactionId);
                                                            }
                                                        }
                                                    } else if (InvoiceType.equalsIgnoreCase("PO Based") && OrderType.equalsIgnoreCase("blanket")) {//PO2WAY Check
                                                       mRepLogger.info("***Inside PO2WAY ***");
                                                        if (!DiscountFlag.equalsIgnoreCase("True")) {
                                                            if (autoprocessObj.discountsChk(transactionId).equals("false")) {
                                                                mRepLogger.info("\n++++++++++++++++DiscountFlag Not True+++++++++++++++++++");
                                                                mRepLogger.info("All cehcks Passed--update the TargetWS flag and routing Wi to Indexing stage");
                                                                mRepLogger.info("+++++++++++++++++++++++++++++++++++");
                                                               autoprocessObj.updateData("ActionFlag", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                autoprocessObj.updateData("TargetWs", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                                autoprocessObj.GetWorkitem(transactionId);
                                                            }
                                                        } else {
                                                            mRepLogger.info("\n++++++++++++++++DiscountFlag True+++++++++++++++");
                                                           mRepLogger.info("All cehcks Passed--update the TargetWS flag and routing Wi to InvociePosting stage");
                                                            mRepLogger.info("+++++++++++++++++++++++++++++++++++");
                                                            autoprocessObj.updateData("ActionFlag", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                            autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                            autoprocessObj.updateData("TargetWs", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                            autoprocessObj.GetWorkitem(transactionId);
                                                        }

                                                    }
                                                }
                                                if (!autoprocessObj.chkEmpty(InvoiceType) && InvoiceType.equalsIgnoreCase("Non PO Based")) {
                                                   mRepLogger.info("***Inside NOT PO3WAY and PO2WAY ***");
                                                     if (!DiscountFlag.equalsIgnoreCase("True")) {
                                                        if (autoprocessObj.discountsChk(transactionId).equals("false")) {
                                                             mRepLogger.info("\n+++++++++++++++DiscountFlag Not True++++++++++++++++++++");
                                                             mRepLogger.info("All cehcks Passed--update the TargetWS flag and routing Wi to Indexing stage");
                                                            mRepLogger.info("+++++++++++++++++++++++++++++++++++");
                                                             autoprocessObj.updateData("ActionFlag", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                            autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                            autoprocessObj.updateData("TargetWs", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                             autoprocessObj.GetWorkitem(transactionId);
                                                        }
                                                    } else {
                                                        mRepLogger.info("\n+++++++++++DiscountFlag True++++++++++++++++++++++++");
                                                         mRepLogger.info("All cehcks Passed--update the TargetWS flag and routing Wi to InvociePosting stage");
                                                        mRepLogger.info("+++++++++++++++++++++++++++++++++++");
                                                        autoprocessObj.updateData("ActionFlag", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                        autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                        autoprocessObj.updateData("TargetWs", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                        autoprocessObj.GetWorkitem(transactionId);
                                                    }
                                                } else if (InvoiceType.equalsIgnoreCase("Advance Payments") || InvoiceType.equalsIgnoreCase("Credit Note")) {
                                                    mRepLogger.info("All cehcks Passed--update the TargetWS flag and routing Wi to InvociePosting stage");
                                                    mRepLogger.info("+++++++++++++++++++++++++++++++++++");
                                                   autoprocessObj.updateData("ActionFlag", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                    autoprocessObj.updateData("ExceptionType", "", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                    autoprocessObj.updateData("TargetWs", "Indexing", "NG_AP_ExtAPProcess", "TransactionID='" + transactionId + "'");
                                                    autoprocessObj.GetWorkitem(transactionId);
                                                  }

                                            }

                                        }

                                    }
                                }
                            }
                        } else {
                            System.out.println("******** downloadPath is null or empty  **********");
                        }
                    } else {
                        mRepLogger.info("Problem Getting downloadPath check ini file");
                        }
                }  // for loop 1


            } // if 1
            else {
                mRepLogger.info("Problem in executing GetData:" + xmlResponse.getVal("Error"));
                 }


             mRepLogger.info("Disconnecting cabinet");
            autoprocessObj.DisconCabinet();

        } catch (Exception e) {
            mRepLogger.info("Error Is: " + e.toString());
        }
        mRepLogger.info("****Utility Ends*****");

    }

    /********************************************************************
     *  Function Name                    : downloadDoc
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : String doxI, int imgI, String DocN, String path
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : DocumentDownload path

     ************************************************************************/
    public String downloadDoc(String doxI, int imgI, String DocN, String path) throws IOException, Exception {
        mRepLogger.info("Inside Download Document method");
        StringBuffer strInputXml = null;
        String strOutputXml;
        strInputXml = new StringBuffer();

        strInputXml.append("<?xml version=1.0?><NGOGetDocumentProperty_Input>"
                + "<Option>NGOGetDocumentProperty</Option>"
                + "<CabinetName>" + this.strCabName + "</CabinetName>"
                + "<UserDBId>" + this.strSessionId + "</UserDBId>"
                + "<CurrentDateTime></CurrentDateTime>"
                + "<DocumentIndex>" + doxI + "</DocumentIndex>"
                + "<DataAlsoFlag>Y</DataAlsoFlag>"
                + "</NGOGetDocumentProperty_Input>");
        strOutputXml = DMSCallBroker.execute(strInputXml.toString(), this.strIP, this.iPort, 0);
        DMSXmlResponse xmlResponse = new DMSXmlResponse(strOutputXml);
        if (xmlResponse.getVal("Status").equals("0")) {
            Jdts.DataObject.JPDBString jpdbStrObj = new JPDBString();
            try {
                CPISDocumentTxn.GetDocInFile_MT(null, this.strIP, Short.parseShort(this.strPort), this.strCabName, Short.parseShort("1"), Short.parseShort("1"), imgI, "", path + "\\" + DocN, jpdbStrObj);
                mRepLogger.info(DocN + " file downloaded to location :: " + path);
            } catch (JPISException ex) {
                ex.printStackTrace();
            }
        }
        return path + "\\" + DocN;
    }

    /********************************************************************
     *  Function Name                    : getDocList
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : String FolderIndx, String downloadPath
     *  Output Parameters                : String
     *  Return Values                    : String
     *  Description                      : get Documents list

     ************************************************************************/
    public String getDocList(String FolderIndx, String downloadPath) throws IOException, Exception {
        //System.out.println("Inside get Document List method ");
        mRepLogger.info("Inside get Document List method ");
        String downloadedfilepath = null;
        StringBuffer strInputXml = null;
        String strOutputXml;
        strInputXml = new StringBuffer();

        strInputXml.append("<?xml version=1.0?>"
                + "<NGOGetDocumentListExt_Input>"
                + "<Option>NGOGetDocumentListExt</Option>"
                + "<CabinetName>" + this.strCabName + "</CabinetName>"
                + "<UserDBId>" + this.strSessionId + "</UserDBId>"
                + "<CurrentDateTime></CurrentDateTime>"
                + "<FolderIndex>" + FolderIndx + "</FolderIndex>"
                + "<DocumentIndex></DocumentIndex>"
                + "<PreviousIndex></PreviousIndex>"
                + "<LastSortField></LastSortField>"
                + "<StartPos></StartPos>"
                + "<NoOfRecordsToFetch></NoOfRecordsToFetch>"
                + "<OrderBy></OrderBy>"
                + "<SortOrder></SortOrder>"
                + "<DataAlsoFlag>N</DataAlsoFlag>"
                + "<ReferenceFlag></ReferenceFlag>"
                + "<PreviousRefIndex></PreviousRefIndex>"
                + "<LastRefField></LastRefField>"
                + "<RefOrderBy></RefOrderBy>"
                + "<RefSortOrder></RefSortOrder>"
                + "<NoOfReferenceToFetch></NoOfReferenceToFetch>"
                + "<RecursiveFlag></RecursiveFlag>"
                + "<DocumentType></DocumentType>"
                + "<LinkDocFlag></LinkDocFlag>"
                + "<AnnotationFlag></AnnotationFlag>"
                + "<ThumbNailAlsoFlag>N</ThumbNailAlsoFlag>"
                + "</NGOGetDocumentListExt_Input>");

        mRepLogger.info("strInputXml ------> " + strInputXml.toString());
        strOutputXml = DMSCallBroker.execute(strInputXml.toString(), this.strIP, this.iPort, 0);
        mRepLogger.info("strOutputXml ------> " + strOutputXml.toString());

        DMSXmlResponse xmlResponse = new DMSXmlResponse(strOutputXml);
        try {
            if (xmlResponse.getVal("Status").equals("0")) {
                if (!xmlResponse.getVal("TotalNoOfRecords").equals("0")) {
                    DMSXmlList xmlList = xmlResponse.createList("Documents", "Document");
                    for (int i = 0; xmlList.hasMoreElements(true); xmlList.skip(true), i++) {
                        String DocIndex = xmlList.getVal("DocumentIndex");
                        int ImgIndex = Integer.parseInt(xmlList.getVal("ISIndex").substring(0, (xmlList.getVal("ISIndex")).indexOf('#')));
                        String DocName = xmlList.getVal("DocumentName") + "." + xmlList.getVal("CreatedByAppName");
                        if (xmlList.getVal("DocumentName").toString().equalsIgnoreCase("Invoice")) {
                            mRepLogger.info("Invoice DocumentType Found...");
                            downloadedfilepath = downloadDoc(DocIndex, ImgIndex, DocName, downloadPath);
                        }
                    }
                } else {
                   mRepLogger.info("Documents are not available for the FolderIndx ==> " + FolderIndx);
                }
            } else {
                 mRepLogger.info("Status not 0 in GetDocumentList");
            }
        } catch (Exception e) {
            mRepLogger.info("Problem Getting Documents from OD" + e);

        }
        return downloadedfilepath;
    }
}
