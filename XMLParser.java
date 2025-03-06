/********************************************************************
*    NEWGEN SOFTWARE TECHNOLOGIES LIMITED
*    Group                                     	: CIG
*    Product / Project                          : Deloitte P2P Automation
*    Module                                  	: VendorPortal
*    File Name                               	: XMLParser.java
*    Author                                    	: ksivashankar
*    Date written                          	: 22/09/2017
*   (DD/MM/YYYY)                      
*    Description                            	: XMLParser
*  CHANGE HISTORY
***********************************************************************************************
* Date                                Change By                    Change Description (Bug No. (If Any))
* (DD/MM/YYYY)            
* 27/09/2017  ksivashankar   Bug 1014568 - Code Review - Java File Header and Function Header missing
* 28/09/2017  ksivashankar   Bug 1014736 - AP NGFuser code suggestion
************************************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ngdeloitteautoprocess;

public class XMLParser {

    private String parseString;
    private String copyString;
    private int IndexOfPrevSrch;

    public XMLParser() {
    }

    public XMLParser(String parseThisString) {
        copyString = new String(parseThisString);
        parseString = toUpperCase(copyString, 0, 0);
    }

    public void setInputXML(String ParseThisString) {
        if (ParseThisString != null) {
            copyString = new String(ParseThisString);
            parseString = toUpperCase(copyString, 0, 0);
            IndexOfPrevSrch = 0;
        } else {
            parseString = null;
            copyString = null;
            IndexOfPrevSrch = 0;
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getServiceName
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : nothing
     *  Output Parameters                : nothing
     *  Return Values                    : nothing
     *  Description                      : getServiceName

     ************************************************************************/
    public String getServiceName() {
        try {
            return new String(copyString.substring(parseString.indexOf(
                    toUpperCase(
                    "<Option>", 0, 0))
                    + (new String(toUpperCase("<Option>",
                    0, 0))).length(),
                    parseString.indexOf(toUpperCase(
                    "</Option>", 0, 0))));
        } catch (StringIndexOutOfBoundsException e) {
            throw e;
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getServiceName
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : chr
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : getServiceName

     ************************************************************************/
    public String getServiceName(char chr) {
        try {
            if (chr == 'A') {
                return new String(copyString.substring(parseString.indexOf(
                        "<AdminOption>".toUpperCase())
                        + (new String("<AdminOption>".
                        toUpperCase())).length(),
                        parseString.indexOf(
                        "</AdminOption>".toUpperCase())));
            } else {
                return "";
            }
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "NoServiceFound";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : validateXML
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : nothing
     *  Output Parameters                : nothing
     *  Return Values                    : boolean
     *  Description                      : validateXML

     ************************************************************************/
    public boolean validateXML() {
        try {
            return parseString.indexOf("<?xml version=\"1.0\"?>".toUpperCase())
                    != -1;
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return false;
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      :getValueOf

     ************************************************************************/
    public String getValueOf(String valueOf) {
        try {
            return new String(copyString.substring(parseString.indexOf("<"
                    + toUpperCase(valueOf, 0, 0) + ">") + valueOf.length() + 2,
                    parseString.
                    indexOf("</"
                    + toUpperCase(valueOf, 0, 0) + ">")));
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf,type
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : getValueOf

     ************************************************************************/
    public String getValueOf(String valueOf, String type) {
        try {
            if (type.equalsIgnoreCase("Binary")) {
                int startPos = copyString.indexOf("<" + valueOf + ">");
                if (startPos == -1) {
                    return "";
                } else {
                    int endPos = copyString.lastIndexOf("</" + valueOf + ">");
                    startPos += (new String("<" + valueOf + ">")).length();
                    return copyString.substring(startPos, endPos);
                }
            } else {
                return "";
            }
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf,fromlast
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : getValueOf

     ************************************************************************/
    public String getValueOf(String valueOf, boolean fromlast) {
        try {
            if (fromlast) {
                return new String(copyString.substring(parseString.indexOf("<"
                        + toUpperCase(valueOf, 0, 0) + ">") + valueOf.length()
                        + 2,
                        parseString.lastIndexOf("</"
                        + toUpperCase(valueOf, 0, 0)
                        + ">")));
            } else {
                return new String(copyString.substring(parseString.indexOf("<"
                        + toUpperCase(valueOf, 0, 0) + ">") + valueOf.length()
                        + 2,
                        parseString.indexOf("</"
                        + toUpperCase(valueOf, 0, 0) + ">")));
            }
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf,end
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : getValueOf

     ************************************************************************/
    public String getValueOf(String valueOf, int start, int end) {
        try {
            if (start >= 0) {
                int endIndex = parseString.indexOf("</"
                        + toUpperCase(valueOf, 0, 0)
                        + ">", start);
                if (endIndex > start && (end == 0 || end >= endIndex)) {
                    return new String(copyString.substring(parseString.indexOf(
                            "<"
                            + toUpperCase(valueOf, 0, 0) + ">", start)
                            + valueOf.length() + 2,
                            endIndex));
                }
            }
            return "";
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getStartIndex
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : tag,start,end
     *  Output Parameters                : nothing
     *  Return Values                    : int
     *  Description                      : getStartIndex

     ************************************************************************/
    public int getStartIndex(String tag, int start, int end) {
        try {
            if (start >= 0) {
                int startIndex = parseString.indexOf("<"
                        + toUpperCase(tag, 0, 0) + ">",
                        start);
                if (startIndex >= start && (end == 0 || end >= startIndex)) {
                    return startIndex + tag.length() + 2;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return -1;
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getEndIndex
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : tag,start,end
     *  Output Parameters                : nothing
     *  Return Values                    : int
     *  Description                      : getEndIndex

     ************************************************************************/
    public int getEndIndex(String tag, int start, int end) {
        try {
            if (start >= 0) {
                int endIndex = parseString.indexOf("</" + toUpperCase(tag, 0, 0)
                        + ">",
                        start);
                if (endIndex > start && (end == 0 || end >= endIndex)) {
                    return endIndex;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return -1;
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getTagStartIndex
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : tag,start,end
     *  Output Parameters                : nothing
     *  Return Values                    : int
     *  Description                      : getTagStartIndex

     ************************************************************************/
    public int getTagStartIndex(String tag, int start, int end) {
        try {
            if (start >= 0) {
                int startIndex = parseString.indexOf("<"
                        + toUpperCase(tag, 0, 0) + ">",
                        start);
                if (startIndex >= start && (end == 0 || end >= startIndex)) {
                    return startIndex;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return -1;
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getTagEndIndex
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : tag,start,end
     *  Output Parameters                : nothing
     *  Return Values                    : int
     *  Description                      : getTagEndIndex

     ************************************************************************/
    public int getTagEndIndex(String tag, int start, int end) {
        try {
            if (start >= 0) {
                int endIndex = parseString.indexOf("</" + toUpperCase(tag, 0, 0)
                        + ">",
                        start);
                if (endIndex > start && (end == 0 || end >= endIndex)) {
                    return endIndex + tag.length() + 3;
                }
            }
            return -1;
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return -1;
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getFirstValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : getFirstValueOf

     ************************************************************************/
    public String getFirstValueOf(String valueOf) {
        try {
            IndexOfPrevSrch = parseString.indexOf("<"
                    + toUpperCase(valueOf, 0, 0)
                    + ">");
            return new String(copyString.substring(IndexOfPrevSrch
                    + valueOf.length()
                    + 2,
                    parseString.indexOf("</"
                    + toUpperCase(valueOf, 0, 0) + ">")));
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getFirstValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf,start
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      :getFirstValueOf

     ************************************************************************/
    public String getFirstValueOf(String valueOf, int start) {
        try {
            IndexOfPrevSrch = parseString.indexOf("<"
                    + toUpperCase(valueOf, 0, 0)
                    + ">", start);
            return new String(copyString.substring(IndexOfPrevSrch
                    + valueOf.length()
                    + 2,
                    parseString.indexOf("</"
                    + toUpperCase(valueOf, 0, 0) + ">", start)));
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getNextValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : getNextValueOf

     ************************************************************************/
    public String getNextValueOf(String valueOf) {
        try {
            IndexOfPrevSrch = parseString.indexOf("<"
                    + toUpperCase(valueOf, 0, 0)
                    + ">",
                    IndexOfPrevSrch
                    + valueOf.length()
                    + 2);
            return new String(copyString.substring(IndexOfPrevSrch
                    + valueOf.length()
                    + 2,
                    parseString.indexOf("</"
                    + toUpperCase(valueOf, 0, 0) + ">",
                    IndexOfPrevSrch)));
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getNoOfFields
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : tag
     *  Output Parameters                : nothing
     *  Return Values                    : int
     *  Description                      : getNoOfFields

     ************************************************************************/
    public int getNoOfFields(String tag) {
        int noOfFields = 0;
        int beginPos = 0;
        try {
            for (tag = toUpperCase(tag, 0, 0) + ">";
                    parseString.indexOf("<" + tag, beginPos) != -1;
                    beginPos += tag.length() + 2) {
                noOfFields++;
                beginPos = parseString.indexOf("</" + tag, beginPos);
                if (beginPos == -1) {
                    break;
                }
            }

        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
        }
        return noOfFields;
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getNoOfFields
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : tag,startPos,endPos
     *  Output Parameters                : nothing
     *  Return Values                    : int
     *  Description                      : getNoOfFields

     ************************************************************************/
    public int getNoOfFields(String tag, int startPos, int endPos) {
        int noOfFields = 0;
        int beginPos = startPos;
        try {
            for (tag = toUpperCase(tag, 0, 0) + ">";
                    parseString.indexOf("<" + tag, beginPos) != -1
                    && (beginPos < endPos || endPos == 0);) {
                beginPos = parseString.indexOf("</" + tag, beginPos)
                        + tag.length() + 2;
                if (beginPos != -1 && (beginPos <= endPos || endPos == 0)) {
                    noOfFields++;
                }
            }

        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
        }
        return noOfFields;
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : convertToSQLString
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : strName
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : convertToSQLString

     ************************************************************************/
    public String convertToSQLString(String strName) {
        try {
            for (int count = strName.indexOf("["); count != -1;
                    count = strName.indexOf("[", count + 2)) {
                strName = strName.substring(0, count) + "[[]"
                        + strName.substring(count + 1, strName.length());

            }
        } catch (Exception exception) {
        }
        try {
            for (int count = strName.indexOf("_"); count != -1;
                    count = strName.indexOf("_", count + 2)) {
                strName = strName.substring(0, count) + "[_]"
                        + strName.substring(count + 1, strName.length());

            }
        } catch (Exception exception1) {
        }
        try {
            for (int count = strName.indexOf("%"); count != -1;
                    count = strName.indexOf("%", count + 2)) {
                strName = strName.substring(0, count) + "[%]"
                        + strName.substring(count + 1, strName.length());

            }
        } catch (Exception exception2) {
        }
        strName = strName.replace('?', '_');
        return strName;
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : getValueOf
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf,type,from,end
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : getValueOf

     ************************************************************************/
    public String getValueOf(String valueOf, String type, int from, int end) {
        try {
            if (type.equalsIgnoreCase("Binary")) {
                int startPos = copyString.indexOf("<" + valueOf + ">", from);
                if (startPos == -1) {
                    return "";
                }
                int endPos = copyString.indexOf("</" + valueOf + ">", from);
                if (endPos > end) {
                    return "";
                } else {
                    startPos += (new String("<" + valueOf + ">")).length();
                    return copyString.substring(startPos, endPos);
                }
            } else {
                return "";
            }
        } catch (StringIndexOutOfBoundsException stringindexoutofboundsexception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : toUpperCase
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : valueOf,begin,end
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : toUpperCase

     ************************************************************************/
    public String toUpperCase(String valueOf, int begin, int end) throws
            StringIndexOutOfBoundsException {
        String returnStr = "";
        try {
            int count = valueOf.length();
            char strChar[] = new char[count];
            valueOf.getChars(0, count, strChar, 0);
            while (count-- > 0) {
                strChar[count] = Character.toUpperCase(strChar[count]);
            }
            returnStr = new String(strChar);
        } catch (ArrayIndexOutOfBoundsException arrayindexoutofboundsexception) {
        }
        return returnStr;
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : changeValue
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : ParseString,TagName,NewValue
     *  Output Parameters                : nothing
     *  Return Values                    : String
     *  Description                      : changeValue

     ************************************************************************/
    public String changeValue(String ParseString, String TagName,
            String NewValue) {
        try {
            String ParseStringTmp = ParseString.toUpperCase();
            String StrTag = (new String("<" + TagName + ">")).toUpperCase();
            int StartIndex = ParseStringTmp.indexOf(StrTag) + StrTag.length();
            int EndIndex = ParseStringTmp.indexOf((new String("</" + TagName
                    + ">")).
                    toUpperCase());
            String RetStr = ParseString.substring(0, StartIndex);
            RetStr = RetStr + NewValue + ParseString.substring(EndIndex);
            return RetStr;
        } catch (Exception exception) {
            return "";
        }
    }

    //Bug 1014568 - Code Review - Java File Header and Function Header missing
    /********************************************************************
     *  Function Name                    : changeValue
     *  Date Written                     : 11-Sep-2017
     *  Author                           : ksivashankar
     *  Input Parameters                 : TagName,NewValue
     *  Output Parameters                : nothing
     *  Return Values                    : void
     *  Description                      : changeValue

     ************************************************************************/
    public void changeValue(String TagName, String NewValue) {
        try {
            String StrTag = ("<" + TagName + ">").toUpperCase();
            int StartIndex = parseString.indexOf(StrTag);
            if (StartIndex > -1) {
                StartIndex += StrTag.length();
                int EndIndex = parseString.indexOf(("</" + TagName + ">").
                        toUpperCase());
                String RetStr = copyString.substring(0, StartIndex);
                copyString = RetStr + NewValue + copyString.substring(EndIndex);
            } else {
                int EndIndex = StartIndex = parseString.lastIndexOf("</");
                String RetStr = copyString.substring(0, StartIndex);
                copyString = RetStr + "<" + TagName + ">" + NewValue + "</"
                        + TagName
                        + ">" + copyString.substring(EndIndex);
            }
            parseString = toUpperCase(copyString, 0, 0);
        } catch (Exception exception) {
        }
    }

    public String toString() {
        return copyString;
    }
}

