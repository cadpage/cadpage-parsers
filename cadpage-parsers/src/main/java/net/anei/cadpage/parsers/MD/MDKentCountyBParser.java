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
      super.parse(field, data);
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHES", "CHESTERTOWN",
      "ROCK", "ROCK HALL"
  });

  private static final CodeTable CALL_CODES = new StandardCodeTable();
}
