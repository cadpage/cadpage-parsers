package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyOParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyOParser() {
    super("Call_Time:DATETIME2/d! EMPTY! Event:ID! Event_Type_Code:CALL! Event_Subtype_Code:CALL/SDS! ESZ:MAP! Beat:BOX! Address:EMPTY! PLACE! ADDR! Cross_Street:X! Location_Information:INFO! Development:INFO/N! Municipality:CITY! Caller_Information:INFO/N! Caller_Name:NAME! Caller_Phone_Number:PHONE! Alt_Phone_Number:SKIP! Caller_Address:SKIP! Caller_Source:SKIP! Units:UNIT! UNIT/S+ Event_Comments:INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "gfac55calls@gmail.com,EWFC05@verizon.net,pfdfire@fdcms.info,vfvfco168@comcast.net,westwoodfire@comcast.net,cad@oxfordfire.com,afc23@fdcms.info";
  }
  
  private static final String MARKER = "Chester County Emergency Services Dispatch Report \n\n";
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf(MARKER);
    if (pt < 0) return false;
    body = body.substring(pt+MARKER.length()).trim();
    pt = body.indexOf("\n\f\n");
    body = stripFieldEnd(body, "**");
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME2")) return new DateTimeField("\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PHONE")) return new MyPhoneField();
    return super.getField(name);
  }
  
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("x Type:");
      if (pt >= 0) {
        field = append(field.substring(0,pt).trim(), " X:", field.substring(pt+7).trim());
      }
      super.parse(field, data);
    }
  }
}
