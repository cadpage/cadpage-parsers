package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAShastaCountyEParser extends FieldProgramParser {
  
  public CAShastaCountyEParser() {
    super(CITY_CODES, "SHASTA COUNTY", "CA", 
          "CadNum:ID! Nature:CODE_CALL! OrigLoc:ADDRCITY! OtherLocInfo:INFO! Comments:INFO! INFO/N+ GPS:GPS! Units:UNIT! Xstrts:X! END!");
  }

  @Override
  public String getFilter() {
    return "support@hangar14solutions.com,admin@streetwisecadfeed.com";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+1).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(")")) {
        int pt = field.indexOf('(');
        data.strPlace = field.substring(pt+1, field.length()-1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MTNGATE",        "MOUNTAIN GATE",
      "SHASTALKCTY",    "SHASTA LAKE",
      "SHFWEAVVILLE",   "WEAVERVILLE"
  });
}
