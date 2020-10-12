package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class CAMendocinoCountyCParser extends DispatchH05Parser {
  
  public CAMendocinoCountyCParser() {
    super("MENDOCINO COUNTY", "CA", 
          "Location:ADDRCITY! Qualifier:APT! Common_Name:PLACE! Cross_Streets:X! Google_Maps_Hyperlink:EMPTY! Call_Type:CALL! Caller:NAME! " + 
            "Call_Date/Time:DATETIME! Status_Time:EMPTY! TIMES+ Incident_Number(s):ID! Narrative:EMPTY! INFO_BLK+");
  }
  
  @Override
  public String getFilter() {
    return "@mendocinocounty.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      super.parse(field, data);
    }
  }
  
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "[");
      field = stripFieldEnd(field, "]");
      if (field.startsWith("Incident not yet created")) return;
      super.parse(field, data);
    }
  }
}
