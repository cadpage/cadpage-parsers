package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INJohnsonCountyParser extends FieldProgramParser {
  
  public INJohnsonCountyParser() {
    super("JOHNSON COUNTY", "IN", 
          "Locution_Dispatch%EMPTY! Units:UNIT! Incident_#:ID! Incident:CALL! Address:ADDR! Common_Place:PLACE! Apartment:APT! Cross:X! City:CITY! Map:MAP! Radio:CH! Comments:INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "CADVoice";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Locution Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d{4} - .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_HDR_PTN.matcher(field).matches()) return;
      super.parse(field, data);
      
    }
  }
}
