package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class CTTollandCountyDParser extends DispatchH05Parser {

  public CTTollandCountyDParser() {
    super("TOLLAND COUNTY", "CT",
          "Call_Address:ADDRCITY! Qualifier:APT? ( Fire_Quandrant:MAP! | Location_Info:INFO! ) Common_Name:PLACE! Call_Type:CALL! Nature_of_Call:CALL/SDS! " +
               "Call_Priority:PRI? Units_Assigned:UNIT! Cross_Streets:X! Call_Date/Time:DATETIME! Incident_#:ID! " +
               "Latitude:GPS1! Longitude:GPS2! ( Status_Times:EMPTY! TIMES+ Narrative:EMPTY! INFO_BLK+ Alerts:ALERT! | Narrative:EMPTY! INFO_BLK+ Status_Times:EMPTY! TIMES+ Final_Report:SKIP )");
  }

  @Override
  public String getFilter() {
    return "TN@tollandcounty911.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new PriorityField("(?:Priority *)?(\\d|[AB]LS)(?: PRIORITY)?", true);
    return super.getField(name);
  }
}
