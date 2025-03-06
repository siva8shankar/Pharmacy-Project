/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package downloaddocumentWithworkitem;

import java.io.IOException;
import ISPack.CPISDocumentTxn;
import ISPack.ISUtil.JPISException;
import Jdts.DataObject.JPDBString;
import com.itextpdf.text.pdf.PdfReader;
import com.newgen.dmsapi.DMSCallBroker;
import com.newgen.dmsapi.DMSXmlList;
import com.newgen.dmsapi.DMSXmlResponse;
import com.newgen.omni.wf.util.app.NGEjbClient;
import com.newgen.omni.wf.util.xml.WFXmlList;
import com.newgen.omni.wf.util.xml.XMLParser;
import com.newgen.omni.wf.util.xml.api.dms.WFXmlResponse;

import com.newgen.wfdesktop.xmlapi.WFCallBroker;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author ksivashankar
 */
public class downloadDocumentWithWorkItem {

    private static String userName;
    private static String password;
    static String strIP;
    static String strPort;
    static String strAppServerType;
    static String strCabName;
    static String strSessionId;
    static String strUsername;
    static String strPwd;
    String Xmlout;
    StringBuffer strBuffer;
    static String mstrlog4jConfigFilePath = "";
    public static Logger mXMLLogger = Logger.getLogger("mLogger");
    public static Logger mErrLogger = Logger.getLogger("Errorlog");
    public static Logger mRepLogger = Logger.getLogger("Replog");
    public static String transactionId;
    public static String strInvoiceno;
    public static String sPrcsDefId;
    int totalInvAmountLimit;
    int tolerancelimit;

    public static void main(String[] args) throws IOException {
        String strDownloadPath = "D:\\Documents\\Required Documents";
        String downloadPath = "";
        String downloadedfilepath = null;
//        String transactionId = "AP-0000166014-Payments";

        downloadDocumentWithWorkItem ddww = new downloadDocumentWithWorkItem();
        try {

            System.out.println("Connecting Cabinet");
            ddww.ReadProperty();
            ddww.conCabinet();
            System.out.println("Cabinet Connected");

            XMLParser parsergetlist = null;
            WFXmlResponse wfXmlResponseFI = null;
            WFXmlList wfxmllistFI = null;
            int FolderIndx = 0;
            downloadDocumentWithWorkItem obj = new downloadDocumentWithWorkItem();

            System.out.println(" Get WorkItemID starts ");
            String strQryGetWI = "SELECT e.WorkID FROM EXT_AP e with(nolock), WFINSTRUMENTTABLE w WITH(nolock),EXT_TravelFare1 t WITH(NOLOCK) WHERE w.ProcessInstanceID=e.WorkID AND t.PID=e.WorkID AND e.InitSts='ER' AND w.Status NOT IN ('Rejected','Rejected as per mail')  AND w.ActivityName IN ('Exit','Audit','Payment_Confirmation') AND e.TypeOfTravel IN ('International','International - Annual Leave travel','International - Business Travel') AND convert(varchar(10),t.DateOfTra,126) > '2017-09-01 00:00:00:00' AND e.CompanyCode IN('BIL1','BDPL','BCFL') GROUP BY e.WorkID,w.Createdbyname,e.TypeOfTravel,e.CompanyCode ORDER BY e.WorkID DESC";

            try {
                String xmlstrQryGetWI = obj.getAPSelect1(strQryGetWI, 1);
                parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlstrQryGetWI);
                if (parsergetlist.getValueOf("MainCode").equals("0")) {

                    wfXmlResponseFI = new WFXmlResponse(xmlstrQryGetWI);
                    wfxmllistFI = wfXmlResponseFI.createList("DataList", "Data");
                    wfxmllistFI.reInitialize(true);

                    for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                        System.out.println("Inside for loop");
                        if (!isempty(wfxmllistFI.getVal("Value1").trim())) {
                            transactionId = wfxmllistFI.getVal("Value1").trim();
                            downloadPath = strDownloadPath + "\\" + transactionId;
                            File directory = new File(downloadPath);
                            if (!directory.exists()) {
                                directory.mkdir();
                            }
                            System.out.println(" transactionId ***** " + transactionId);
                            FolderIndx = ddww.getFolderIndexFromWIName(transactionId);
                            System.out.println("FolderIndx ----->" + FolderIndx);
                            downloadedfilepath = ddww.getDocList(Integer.toString(FolderIndx), downloadPath);
                            System.out.println("downloadedfilepath =====> " + downloadedfilepath);
                        }
                    }
                } else {
                    System.out.println("maincode not 0 in getdata");

                }
            } catch (Exception e) {
                System.out.println("Problem in execting APSelect in FolderIndx");

            }
            ddww.DisconCabinet();
        } catch (Exception ex) {
            ddww.DisconCabinet();
            Logger.getLogger(downloadDocumentWithWorkItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ReadProperty() {
        try {
            String strPropertyPath = System.getProperty("user.dir") + File.separator + "Settings.ini";
            System.out.println("Read property file from:" + strPropertyPath);
            FileInputStream File_Ini = new FileInputStream(strPropertyPath);
            Properties prop = new Properties();
            prop.load(File_Ini);

            strIP = prop.getProperty("AppServerIP");
            strPort = prop.getProperty("AppServerPort");
            strAppServerType = prop.getProperty("AppServerType");
            strCabName = prop.getProperty("CabinetName");
            strUsername = prop.getProperty("UserName");
            //String encodedPwd = ST Get Document;
            //this.strPwd = decrypt(encodedPwd);
            strPwd = prop.getProperty("Password");
            sPrcsDefId = prop.getProperty("ProcessDefID");
            totalInvAmountLimit = Integer.parseInt(prop.getProperty("TotalInvAmountLimit"));
//      this.tolerancelimit=Integer.parseInt(prop.getProperty("ToleranceLimit"));
            System.out.println("IP=" + strIP + " Port=" + strPort + " Appservertype=" + strAppServerType + " CabinetName=" + strCabName + " Username=" + strUsername + " Pswd=" + strPwd);
        } catch (Exception e) {
            System.out.println("[Error]** Exception in ReadProperty " + e.toString());
            e.printStackTrace();
        }
    }

    public boolean conCabinet() {
        DMSXmlResponse xmlResponse = null;
        this.Xmlout = "";
        StringBuffer strBuffer = new StringBuffer();
        try {
            strBuffer = new StringBuffer();
            strBuffer.append("<?xml version=1.0?>");
            strBuffer.append("<NGOConnectCabinet_Input>");
            strBuffer.append("<Option>NGOConnectCabinet</Option>");
            strBuffer.append("<CabinetName>" + strCabName + "</CabinetName>");
            strBuffer.append("<UserName>" + strUsername + "</UserName>");
            strBuffer.append("<UserPassword>" + strPwd + "</UserPassword>");
            strBuffer.append("<UserExist>N</UserExist>");
            strBuffer.append("</NGOConnectCabinet_Input>");
            System.out.println("ConnectCabinet InputXML:\n" + strBuffer.toString());
            this.Xmlout = WFCallBroker.execute(strBuffer.toString(), strIP, Integer.parseInt(strPort), 0);

            System.out.println("ConnectCabinet OutputXML:\n" + this.Xmlout);
            xmlResponse = new DMSXmlResponse(this.Xmlout);
            if (xmlResponse.getVal("status").equals("0")) {
                strSessionId = xmlResponse.getVal("UserDBId");
                System.out.println("Cabinet Connected successfully ....." + strSessionId);
                return true;
            }
            mXMLLogger.info("Problem in Connecting Cabinet : " + xmlResponse.getVal("Error"));
            mErrLogger.info("Problem in Connecting Cabinet : " + xmlResponse.getVal("Error"));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            mErrLogger.info("\n\n### **[Error]** Exception in Connecting Cabinet : " + e.toString());
        }
        return false;
    }

    public void DisconCabinet() {
        DMSXmlResponse xmlResponse = null;
        this.Xmlout = "";
        try {
            this.strBuffer = new StringBuffer();
            this.strBuffer.append("<?xml version='1.0'?>");
            this.strBuffer.append("<NGODisconnectCabinet_Input>");
            this.strBuffer.append("<Option>NGODisconnectCabinet</Option>");
            this.strBuffer.append("<CabinetName>" + strCabName + "</CabinetName>");
            this.strBuffer.append("<UserDBId>" + strSessionId + "</UserDBId>");
            this.strBuffer.append("</NGODisconnectCabinet_Input>");
            this.Xmlout = WFCallBroker.execute(this.strBuffer.toString(), strIP, Integer.parseInt(strPort), 0);
            System.out.println("Disconnect Cabinet InputXML:\n" + this.strBuffer.toString());
            System.out.println("Disconnect Cabinet OutputXMLt:\n" + this.Xmlout);
            xmlResponse = new DMSXmlResponse(this.Xmlout);
            if (xmlResponse.getVal("Status").equals("0")) {
                mXMLLogger.info("Cabinet Disconnected Successfully ...");
            } else {
                mErrLogger.info("Problem in Disconnecting cabinet:" + xmlResponse.getVal("Error"));
                mXMLLogger.info("Problem in Disconnecting cabinet:" + xmlResponse.getVal("Error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mErrLogger.info("\n\n### **[Error]** Exception in Disconnecting cabinet : " + e.toString());
        }
    }

    public int getFolderIndexFromWIName(String winame) {
        XMLParser parsergetlist = null;
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        int FolderIndx = 0;
        downloadDocumentWithWorkItem obj = new downloadDocumentWithWorkItem();

        System.out.println(" getFolderIndexFromWIName ");
        String qrytoFolderIndx = "Select FOLDERINDEX from pdbfolder where NAME='" + winame + "'";

        try {
            String xmlqrytoqrytoFolderIndx = obj.getAPSelect1(qrytoFolderIndx, 1);
            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(xmlqrytoqrytoFolderIndx);
            if (parsergetlist.getValueOf("MainCode").equals("0")) {

                wfXmlResponseFI = new WFXmlResponse(xmlqrytoqrytoFolderIndx);
                wfxmllistFI = wfXmlResponseFI.createList("DataList", "Data");
                wfxmllistFI.reInitialize(true);

                for (; wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)) {
                    System.out.println("Inside for loop");

                    if (!isempty(wfxmllistFI.getVal("Value1").trim())) {
                        FolderIndx = Integer.parseInt(wfxmllistFI.getVal("Value1").trim());
                    }
                }
                System.out.println("FolderIndx=" + FolderIndx);

            } else {
                System.out.println("maincode not 0 in getdata");

            }
        } catch (Exception e) {
            System.out.println("Problem in execting APSelect in FolderIndx");

        }
        return FolderIndx;
    }

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

    public String getAPSelect(String strQuery, int noofCols) {
        com.newgen.omni.wf.util.xml.XMLParser parsergetlist = null;
        String strOutputXmlFI = null;
        StringBuilder strBuffFI = new StringBuilder();
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        String FIDocNo = "";
        // TreeSet<String> setFIDocNo= new TreeSet<String>();

        strBuffFI = strBuffFI.append("<?xmlversion=\"1.0\"?>");
        strBuffFI = strBuffFI.append("<APSelectWithColumnNames>");
        strBuffFI = strBuffFI.append("<Option>APSelectWithColumnNames</Option>");
        strBuffFI = strBuffFI.append("<EngineName>").append(strCabName).append("</EngineName>");
        strBuffFI = strBuffFI.append("<SessionId>").append(strSessionId).append("</SessionId>");
        strBuffFI = strBuffFI.append("<QueryString>").append(strQuery).append("</QueryString>");
        strBuffFI = strBuffFI.append("<NoOfCols>").append(noofCols).append("</NoOfCols>");
        strBuffFI = strBuffFI.append("</APSelectWithColumnNames>");
        System.out.println("strBuffFI==" + strBuffFI);
        try {
            strOutputXmlFI = WFCallBroker.execute(strBuffFI.toString(), strIP, Integer.parseInt(strPort), 0);
            System.out.println("strCompOutputXml==" + strOutputXmlFI);
//            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(strOutputXmlFI);
//            if (parsergetlist.getValueOf("MainCode").equals("0")) {
//            wfXmlResponseFI= new WFXmlResponse(strOutputXmlFI);
//            wfxmllistFI= wfXmlResponseFI.createList("Records", "Record");
//            wfxmllistFI.reInitialize(true);
//            for(;wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)){
//                
//                FIDocNo = wfxmllistFI.getVal("FIDocNo").trim();
//                if(!FIDocNo.equalsIgnoreCase("")){
//                    setFIDocNo.add(FIDocNo);
//                }
//                System.out.println("setFIDocNo=="+setFIDocNo);
//            }
//            }  
        } catch (Exception e) {
            System.out.println("[Error]Error in executing APSelect " + e);

        }
        return strOutputXmlFI;
    }

    public String getAPSelect1(String strQuery, int noofCols) {
        com.newgen.omni.wf.util.xml.XMLParser parsergetlist = null;
        String strOutputXmlFI = null;
        StringBuilder strBuffFI = new StringBuilder();
        WFXmlResponse wfXmlResponseFI = null;
        WFXmlList wfxmllistFI = null;
        String FIDocNo = "";
        // TreeSet<String> setFIDocNo= new TreeSet<String>();

        strBuffFI = strBuffFI.append("<?xml version=\"1.0\"?>");
        strBuffFI = strBuffFI.append("<WFCustomBean_Input><Option>NGGetData</Option>");
        strBuffFI = strBuffFI.append("<QryOption>NGGetData</QryOption>");
        strBuffFI = strBuffFI.append("<EngineName>").append(strCabName).append("</EngineName>");
        strBuffFI = strBuffFI.append("<SessionId>").append(strSessionId).append("</SessionId>");
        strBuffFI = strBuffFI.append("<QueryString>").append(strQuery).append("</QueryString>");
        strBuffFI = strBuffFI.append("<ColumnNo>").append(noofCols).append("</ColumnNo>");
        strBuffFI = strBuffFI.append("</WFCustomBean_Input>");

        System.out.println("strBuffFI==" + strBuffFI);
        System.out.println(" this.strIP == " + strIP);
        System.out.println("this.iPort == " + Integer.parseInt(strPort));
        try {
            strOutputXmlFI = WFCallBroker.execute(strBuffFI.toString(), strIP, Integer.parseInt(strPort), 0);
            System.out.println("strCompOutputXml==" + strOutputXmlFI);
//            parsergetlist = new com.newgen.omni.wf.util.xml.XMLParser(strOutputXmlFI);
//            if (parsergetlist.getValueOf("MainCode").equals("0")) {
//            wfXmlResponseFI= new WFXmlResponse(strOutputXmlFI);
//            wfxmllistFI= wfXmlResponseFI.createList("Records", "Record");
//            wfxmllistFI.reInitialize(true);
//            for(;wfxmllistFI.hasMoreElements(true); wfxmllistFI.skip(true)){
//                
//                FIDocNo = wfxmllistFI.getVal("FIDocNo").trim();
//                if(!FIDocNo.equalsIgnoreCase("")){
//                    setFIDocNo.add(FIDocNo);
//                }
//                System.out.println("setFIDocNo=="+setFIDocNo);
//            }
//            }  
        } catch (Exception e) {
            System.out.println("[Error]Error in executing APSelect " + e);

        }
        return strOutputXmlFI;
    }

    public String downloadDoc(String doxI, int imgI, String DocN, String path) throws IOException, Exception {
        StringBuffer strInputXml = null;
        String strOutputXml;
        strInputXml = new StringBuffer();

        strInputXml.append("<?xml version=1.0?><NGOGetDocumentProperty_Input>"
                + "<Option>NGOGetDocumentProperty</Option>"
                + "<CabinetName>" + strCabName + "</CabinetName>"
                + "<UserDBId>" + strSessionId + "</UserDBId>"
                + "<CurrentDateTime></CurrentDateTime>"
                + "<DocumentIndex>" + doxI + "</DocumentIndex>"
                + "<DataAlsoFlag>Y</DataAlsoFlag>"
                + "</NGOGetDocumentProperty_Input>");

        strOutputXml = DMSCallBroker.execute(strInputXml.toString(), strIP, Integer.parseInt(strPort), 0);

        DMSXmlResponse xmlResponse = new DMSXmlResponse(strOutputXml);

        if (xmlResponse.getVal("Status").equals("0")) {

            Jdts.DataObject.JPDBString jpdbStrObj = new JPDBString();

            try {
                CPISDocumentTxn.GetDocInFile_MT(null, this.strIP, Short.parseShort(this.strPort), this.strCabName, Short.parseShort("1"), Short.parseShort("1"), imgI, "", path + "\\" + DocN, jpdbStrObj);

                System.out.println(DocN + " downloaded to location :: " + path);
            } catch (JPISException ex) {

                ex.printStackTrace();
            }

        }
        System.out.println("path " + path);
        System.out.println("DocN " + DocN);
        return path + "\\" + DocN;
    }

    public String getDocList(String FolderIndx, String downloadPath) throws IOException, Exception {
        String downloadedfilepath = null;
        StringBuffer strInputXml = null;
        String strOutputXml;
        int k = 0;
        String DocName;
        strInputXml = new StringBuffer();

        strInputXml.append("<?xml version=1.0?>"
                + "<NGOGetDocumentListExt_Input>"
                + "<Option>NGOGetDocumentListExt</Option>"
                + "<CabinetName>" + strCabName + "</CabinetName>"
                + "<UserDBId>" + strSessionId + "</UserDBId>"
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

        System.out.println("strInputXml ------> " + strInputXml.toString());
        strOutputXml = DMSCallBroker.execute(strInputXml.toString(), strIP, Integer.parseInt(strPort), 0);
        System.out.println("strOutputXml ------> " + strOutputXml.toString());

        DMSXmlResponse xmlResponse = new DMSXmlResponse(strOutputXml);

        if (xmlResponse.getVal("Status").equals("0")) {

//            System.out.println(xmlResponse.toString());
//
//            System.out.println(xmlResponse.getVal("NoOfRecordsFetched"));
            DMSXmlList xmlList = xmlResponse.createList("Documents", "Document");

//            System.out.println(xmlList);
            for (int i = 0; xmlList.hasMoreElements(true); xmlList.skip(true), i++) {

                String DocIndex = xmlList.getVal("DocumentIndex");
                int ImgIndex = Integer.parseInt(xmlList.getVal("ISIndex").substring(0, (xmlList.getVal("ISIndex")).indexOf('#')));
                if (xmlList.getVal("DocumentName").equalsIgnoreCase("Invoice")) {
                    DocName = xmlList.getVal("DocumentName") + k + "." + xmlList.getVal("CreatedByAppName");
                    k++;
                } else {
                    DocName = xmlList.getVal("DocumentName") + "." + xmlList.getVal("CreatedByAppName");
                }

//                System.out.println("DocumentIndex : " + xmlList.getVal("DocumentIndex"));
//
//                System.out.println("ImageIndex : " + xmlList.getVal("ISIndex").substring(0, (xmlList.getVal("ISIndex")).indexOf('#')));
//                System.out.println("____________");
                downloadedfilepath = downloadDoc(DocIndex, ImgIndex, DocName, downloadPath);

            }

        }

        return downloadedfilepath;
    }

}
