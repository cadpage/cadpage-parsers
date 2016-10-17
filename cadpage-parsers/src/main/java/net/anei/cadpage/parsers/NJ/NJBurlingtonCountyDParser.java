package net.anei.cadpage.parsers.NJ;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;




public class NJBurlingtonCountyDParser extends FieldProgramParser {
  
  public NJBurlingtonCountyDParser() {
    super("BURLINGTON COUNTY", "NJ",
           "Call_Time:TIME! Incident_#:ID! Incident_Type:CALL! ( Located_Btwn:X! Nature:INFO! Quadrant:MAP | Quadrant:MAP Location:ADDR! Located_Btwn:X! Nature:INFO! )");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.startsWith("Messenger 911")) return false;
    return parseFields(body.split("\n"), 6, data);
  }
  
  private class MyIDField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" S")) field = field.substring(0,field.length()-1).trim();
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIDField();
    return super.getField(name);
  }

  @Override
  public String adjustMapAddress(String sAddress) {
    if (sAddress.endsWith(" CL")) sAddress = sAddress.substring(0,sAddress.length()-3).trim();
    sAddress = PAREN_PTN.matcher(sAddress).replaceAll(" ");
    return sAddress;
  }
  private static final Pattern PAREN_PTN = Pattern.compile("\\(.*\\)");
}
