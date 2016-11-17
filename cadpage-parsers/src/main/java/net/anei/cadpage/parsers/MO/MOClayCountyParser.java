package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOClayCountyParser extends FieldProgramParser {
  
  public MOClayCountyParser() {
    super("CLAY COUNTY", "MO", 
          "CALL ADDR! APT? X+? INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "LogiSYS CAD,CAD@libertymo.gov";
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:\\[SPAM\\]:)?CAD Page for CFS ([-\\d]+)");
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("(?!X:)\\S+", true);
    if (name.equals("X")) return new CrossField("X: *(.*)", true);
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "U:");
      super.parse(field, data);
    }
  }
}
