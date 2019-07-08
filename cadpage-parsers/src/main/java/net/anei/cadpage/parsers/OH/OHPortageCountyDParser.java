package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;


public class OHPortageCountyDParser extends DispatchH05Parser {

  public OHPortageCountyDParser() {
    super("PORTAGE COUNTY", "OH",
          "( U_DATETIME U_ADDRESS U_NAME U_PHONE U_CFS_NUMBER U_PLACE U_X U_CALL U_ID! " +
          "| Date/time:DATETIME! Address:ADDRCITY! Caller_Name:NAME! Caller_phone:PHONE! CFS_Number:SKIP! Common_Name:PLACE! Cross_Streets:X! Call_Type:CALL! Incident_Number:ID! " + 
          ") Narrative%EMPTY! INFO_BLK/Z+? Status_Times%EMPTY! TIMES/Z+? U_UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "@kent.edu";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("U_DATETIME")) return new DateTimeField("Date/time *(\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d)", true);
    if (name.equals("U_ADDRESS")) return new AddressField("Address *(.*)", true);
    if (name.equals("U_NAME")) return new NameField("Caller Name *(.*)", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("U_PHONE")) return new MyPhoneField("Caller phone *(.*)", true);
    if (name.equals("U_CFS_NUMBER")) return new SkipField("CFS Number.*", true);
    if (name.equals("U_PLACE")) return new PlaceField("Common Name *(.*)", true);
    if (name.equals("U_X")) return new CrossField("Cross Streets *(.*)", true);
    if (name.equals("U_CALL")) return new CallField("Call Type *(.*)", true);
    if (name.equals("U_ID")) return new IdField("Incident Number *(.*)", true);
    if (name.equals("U_UNIT")) return new UnitField("Units Assigned *(.+)", true);
    return super.getField(name);
  }
  
  private class MyPhoneField extends PhoneField {
    
    public MyPhoneField() {
      super();
    }
    
    public MyPhoneField(String pattern, boolean hard) {
      super(pattern, hard);
    }
    
    public void parse(String field, Data data) {
      if (field.equals("() -")) return;
      super.parse(field, data);
    }
  }
}
