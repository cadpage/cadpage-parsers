package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

/**
 * Jasper County, SC
 */
public class SCJasperCountyParser extends DispatchH05Parser {
      
  public SCJasperCountyParser() {
    super("JASPER COUNTY", "SC",
          "Respond_for:CALL! Radio_Channel_Assigned:CH! Respond_to:ADDRCITY! Lot/Appt/Room_Number:APT! Common_Name:PLACE! Cross_Streets:X! Additional_Location_Information:INFO! MAP Caller_Name:NAME! Caller_Phone:PHONE! Call_Narrative:EMPTY! INFO_BLK/Z+? Incident_Number(s):ID! Units_Assigned:UNIT! Status_Times:EMPTY TIMES+? Google_Maps_Link:EMPTY");
  }
  
  @Override
  public String getFilter() {
    return "@jasperscountysc.gov";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("MAP")) return new MapField("Map Page *(.*)", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }
  
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("() -")) return;
      super.parse(field, data);
    }
  }
}
