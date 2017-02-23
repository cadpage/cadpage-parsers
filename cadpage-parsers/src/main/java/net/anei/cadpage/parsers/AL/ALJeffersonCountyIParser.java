package net.anei.cadpage.parsers.AL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALJeffersonCountyIParser extends FieldProgramParser {
  
  public ALJeffersonCountyIParser() {
    super("JEFFERSON COUNTY", "AL", 
          "CALL:CALL! ADDR:GPS! ADDR1:ADDR! ID:ID! ( Date/Time:DATETIME MAP:SKIP! UNITS:UNIT! INFO/N+ " + 
                                                 "| MAP:SKIP! UNITS:UNIT! ( Date/Time:DATETIME! INFO/N+ " + 
                                                                         "| INFO/N+? Date/Time:DATETIME! END ) )");
  }
  
  @Override
  public String getFilter() {
    return "@JeffCoal911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ACTIVE 9-1-1")) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO"))  return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "NARR:");
      super.parse(field, data);
    }
  }
}
