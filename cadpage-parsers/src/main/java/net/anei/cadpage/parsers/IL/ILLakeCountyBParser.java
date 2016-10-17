package net.anei.cadpage.parsers.IL;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILLakeCountyBParser extends FieldProgramParser {
  
  public ILLakeCountyBParser() {
    super(CITY_CODES, "LAKE COUNTY", "IL", 
          "Call:CALL! Sta:SRC! Grid:MAP! Due:UNIT! Place:PLACE? Addr:ADDR! City:CITY? Zip:SKIP? Cross:X? DR#:ID! Pri:PRI! Date:DATE! Time:TIME! Map:GPS! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "wireless7@village.gurnee.il.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("** End of CAD Message **")) return;
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CNTY", ""
  });

}
