package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYSuffolkCountyKParser extends FieldProgramParser {
  
  public NYSuffolkCountyKParser() {
    super("SUFFOLK COUNTY", "NY",
          "CALL! ( EMPTY PLACE | ) ADDR! X? TOA:TIMEDATE/d! ID INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "cipaging1@gmail.com";
  }
  
  private static final Pattern DELIM = Pattern.compile("\n\\**");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), 3, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d \\d\\d-\\d\\d-\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{6}", true);
    return super.getField(name);
  }
  
  private static final Pattern ADDRESS_PTN = Pattern.compile("(.*)\\*\\*(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDRESS_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).trim(), data);
      data.strCity = match.group(2).trim();
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("**CS: ")) return false;
      super.parse(field.substring(6).trim(), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
