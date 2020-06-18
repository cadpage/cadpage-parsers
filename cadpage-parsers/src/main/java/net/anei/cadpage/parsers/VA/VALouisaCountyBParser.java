package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VALouisaCountyBParser extends FieldProgramParser {
  
  public VALouisaCountyBParser() {
    super("LOUISA COUNTY", "VA", 
          "CALL ADDRCITY PLACE! Addtl_Location_Info:INFO! BOX! UNIT! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@louisa.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Information")) return false;
    body = body.replace("; LON:", " LON:");
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new PlaceField("At *(.*)", true);
    if (name.equals("BOX")) return new BoxField("Box +(.*)|()", true);
    return super.getField(name);
  }

}
