package net.anei.cadpage.parsers.CT;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTTollandCountyCParser extends FieldProgramParser {
  
  public CTTollandCountyCParser() {
    super("TOLLAND COUNTY", "CT", 
          "Address:ADDRCITY! Location_Info:INFO! INFO/N+ Call_Type:CALL! Nature:CALL/SDS X-Sts:X! Date/Time:DATETIME Incident_#:ID! ID+");
  }
  
  @Override
  public String getFilter() {
    return "messaging@iamresponding.com,777";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      data.strCallId = append(data.strCallId, ",", field);
    }
  }

}
