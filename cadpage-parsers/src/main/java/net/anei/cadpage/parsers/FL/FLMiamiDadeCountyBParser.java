package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLMiamiDadeCountyBParser extends FieldProgramParser {
  
  public FLMiamiDadeCountyBParser() {
    super("MIAMI-DADE COUNTY", "FL", 
          "Call_ID:ID! Loc:ADDRCITY! X Access:APT NOC:CALL! END");
  }

  @Override
  public String getLocName() {
    return "Miami-Dade County, FL";
  }
  
  @Override
  public String getFilter() {
    return "7863223966";
  }
  
  private static final Pattern MARKER = Pattern.compile("(?:New|Updated) call from Hatzalah Miami\n\\s*");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?)(?:, *([A-Z]{2}))?(?:, *(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field, data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
}
