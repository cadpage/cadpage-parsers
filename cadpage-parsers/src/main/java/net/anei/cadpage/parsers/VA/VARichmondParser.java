package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VARichmondParser extends FieldProgramParser {

  public VARichmondParser() {
    super(CITY_CODES, "RICHMOND", "VA", 
          "ADDR/S Type:CALL! CALL+? Xstreet1:X! Xstreet2:X Disp:UNIT");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // check for and kill leading comma
    if (!body.startsWith(",")) {
      if (!body.startsWith("WORKING FIRE")) return false;
      // special case for WORKING FIRE pages
      data.strCall = "WORKING FIRE";
      body = body.substring(12);
      parseAddress(StartType.START_ADDR, body, data);
      data.strSupp = getLeft();
      setFieldList("CALL ADDR INFO");
      return true;
    }
    body = body.substring(1);

    // if successful parse and empty addr use x as addr
    boolean success = super.parseFields(body.split(",(?! )"), data);
    if (data.strAddress.length() == 0 && success) {
      data.strAddress = data.strCross;
      data.strCross = "";
      setFieldList("CALL ADDR UNIT");
    }
    return success;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  // support ADDR: @PLACE notation
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      
      int i = field.indexOf(':');
      if (i > -1) {
        data.strPlace = field.substring(i+1).trim();
        if (data.strPlace.startsWith("@")) data.strPlace = data.strPlace.substring(1).trim();
        field = field.substring(0, i).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] { "RICH", "RICHMOND" });
}
