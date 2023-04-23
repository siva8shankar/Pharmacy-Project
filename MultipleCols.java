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