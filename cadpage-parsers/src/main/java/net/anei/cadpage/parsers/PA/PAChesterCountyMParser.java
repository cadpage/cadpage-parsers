package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyMParser extends PAChesterCountyBaseParser {
  
  public PAChesterCountyMParser() {
    super("Alarm_Desc:CALL! Address:ADDR! Municipality:CITY? Location_Info:INFO? Incident_#:ID! Initial_Priority:PRI! Alarm_Date:DATE Alarm_Time:TIME! Map:MAP? Cross_Streets:X? ENTRY_Comments:INFO? Development:PLACE? Initial_Level:SKIP? Latitude:GPS1? Longitude:GPS2?  Caller_Name:NAME? Caller_Address:SKIP? Caller_Phone:PHONE? Initial_Type:CODE?");
  }
  
  @Override
  public String getFilter() {
    return "47dispatch@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("#([A-Z]{4}\\d+)", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
   return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "*");
      super.parse(field, data);
    }
  }
  
} 
