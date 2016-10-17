package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Solano County, CA (B)
 */
public class CASolanoCountyBParser extends FieldProgramParser {
  
  public CASolanoCountyBParser() {
    super("SOLANO COUNTY", "CA",
          "UNIT CALL ADDR INFO INFO+? TIME!");
  }
  
  @Override
  public String getFilter() {
    return "7075900300@vtext.com";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private static final Pattern UNIT_PTN = Pattern.compile("\\d\\d");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Vaca Dist.");
      field = stripFieldStart(field, "Sta");
      if (!UNIT_PTN.matcher(field).matches()) abort();
      super.parse(field, data);
    }
  }
  
  private static final Pattern ADDR_PLACE_PTN = Pattern.compile("(.*) at (.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PLACE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strPlace = match.group(2).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
}
