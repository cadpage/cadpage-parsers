package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAHuntingdonCountyParser extends FieldProgramParser {
  
  public PAHuntingdonCountyParser() {
    super("HUNTINGDON COUNTY","PA", 
          "INC_TYPE:CALL! ADDRESS:ADDRCITY! X-STREET:X! BOX_#:BOX! UNITS:UNIT! DATETIME! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@hunt911.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
