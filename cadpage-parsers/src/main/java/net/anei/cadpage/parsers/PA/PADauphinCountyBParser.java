package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class PADauphinCountyBParser extends DispatchH05Parser {
  
  public PADauphinCountyBParser() {
    super("DAUPHIN COUNTY", "PA", 
          "Report_Date:SKIP! Call_Date:DATETIME! Call_Address:ADDRCITY! Common_Name:PLACE! Cross_Streets:X! Fire_Call_Type:SKIP! Fire_Box:BOX! EMS_Box:BOX! EMS_Call_Type:SKIP Unit_Incident_Number:ID! Unit_Times:EMPTY! TIMES+? Units_Assigned:UNIT!");
  }
  
  @Override
  public String getFilter() {
    return "ripandrun@lcdes.org";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("BOX")) return new MyBoxField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strBox)) return;
      data.strBox = append(data.strBox, "/", field);
        
    }
  }
}
