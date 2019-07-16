package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Jasper County, SC
 */
public class SCJasperCountyParser extends DispatchH05Parser {
      
  public SCJasperCountyParser() {
    super("JASPER COUNTY", "SC",
          "Respond_for:CALL! Radio_Channel_Assigned:CH! Respond_to:ADDRCITY! Lot/Appt/Room_Number:APT! Common_Name:PLACE! Cross_Streets:X! Additional_Location_Information:INFO! MAP! Caller_Name:NAME! Caller_Phone:PHONE! Call_Narrative:EMPTY! INFO_BLK/Z+? Incident_Number(s):ID! Units_Assigned:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "@jasperscountysc.gov";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("Map Page *(.*)", true);
    return super.getField(name);
  }
}
