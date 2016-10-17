package net.anei.cadpage.parsers.NY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYJeffersonCountyBParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("[A-Z]+ \\d+");
  
  public NYJeffersonCountyBParser() {
    super("JEFFERSON COUNTY", "NY",
          "CALL ADDR INFO! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "777";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!SUBJECT_PTN.matcher(subject).matches()) return false;
    data.strSource = subject;
    return parseFields(body.split("\n"), 3, data);
  }
  
  @Override
  public String getProgram() {
    return "SRC  " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class  MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(';');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
}
