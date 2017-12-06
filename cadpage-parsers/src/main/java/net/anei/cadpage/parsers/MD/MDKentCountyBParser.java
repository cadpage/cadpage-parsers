package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

public class MDKentCountyBParser extends FieldProgramParser {
  
  public MDKentCountyBParser() {
    super(CITY_CODES, "KENT COUNTY", "MD", 
          "Inc:ID! Call_Type:CODE! Call_Desc:CALL! Date:DATE! Time_Recv:TIME! Time_Clear:EMPTY! Box_Area:BOX! Station:SRC! Priority:PRI! Lat:GPS1! Long:GPS2! City:CITY! Location:ADDR! Units:UNIT! Rmk:INFO/N! INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "911@kentgov.org";
  }
  
  private static final Pattern DELIM = Pattern.compile(" *[\t\n]+ *");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From Kent County CAD")) return false;
    if (!parseFields(DELIM.split(body), data)) return false;

    String call = CALL_CODES.getCodeDescription(data.strCode);
    if (call != null) data.strCall = call;
    
    if (data.strBox.equals("QA")) {
      data.strCity = "QUEEN ANNES COUNTY";
    }
    else if (data.strBox.equals("OOC")) {
      data.defCity = "";
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("-  ");
      if (pt >= 0) {
        data.strSupp = field.substring(pt+3).trim();
        field = field.substring(0, pt).trim();
      }
      pt = field.indexOf('@');
      if (pt >= 0) {
        String part1 = field.substring(0,pt).trim();
        String part2 = field.substring(pt+1).trim();
        if (checkAddress(part2) > checkAddress(part1)) {
          data.strPlace = part1;
          field = part2;
        } else {
          data.strPlace = part2;
          field = part1;
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE INFO?";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BETT", "BETTERTON",
      "CHES", "CHESTERTOWN",
      "GALE", "GALENA",
      "GOLT", "GOLTS",
      "KENN", "KENNEDYVILLE",
      "MASS", "MASSEY",
      "MILL", "MILLINGTON",
      "ROCK", "ROCK HALL",
      "STIL", "STILL POND",
      "WORT", "WORTON"
  });

  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
