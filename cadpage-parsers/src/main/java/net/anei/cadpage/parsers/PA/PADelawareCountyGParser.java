package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class PADelawareCountyGParser extends FieldProgramParser {
  
  public PADelawareCountyGParser() {
    super("DELAWARE COUNTY", "PA",
          "( CALL:CALL! ADDR:ADDR! CITY:CITY? ID:ID? DATE:DATE! TIME:TIME! UNIT:UNIT INFO:INFO+ " + 
          "| ADDR:ADDR! CITY:CITY? CALL:CALL? DATE:DATE? TIME:TIME? ID:ID? INFO:INFO INFO/N+? CRITERIA:SKIP STATION:SRC UNIT:UNIT " + 
          "| DATE:DATE! CRITERIA:SKIP! STATION:SRC? UNIT:UNIT? )");
  }
  
  @Override
  public String getFilter() {
    return "admin@springfieldems.com,alerts@delcodispatch.com";
  }
  
  private static final Pattern DELIM = Pattern.compile("\n|<?\\|");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('~', ' ').trim().replaceAll("  +", " ");
      super.parse(field, data);
    }
  }
  
  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('-');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
}
