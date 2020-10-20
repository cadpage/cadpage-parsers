package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class CTTollandCountyDParser extends DispatchH05Parser {
  
  public CTTollandCountyDParser() {
    super("TOLLAND COUNTY", "CT", 
          "Call_Address:ADDRCITY! Qualifier:APT! Location_Info:INFO! Common_Name:PLACE! Call_Type:CALL! Nature_of_Call:CALL/SDS! Call_Date/Time:DATETIME! Incident_#:ID! " + 
               "Latitude:GPS1! Longitude:GPS2! Narrative:EMPTY! INFO_BLK+ Status_Times:EMPTY! TIMES+ Final_Report:SKIP");
  }
  
  @Override
  public String getFilter() {
    return "TN@tollandcounty911.com";
  }
}
