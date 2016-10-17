package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDCecilCountyDParser extends FieldProgramParser {

  public MDCecilCountyDParser() {
    super(CITY_CODES, "CECIL COUNTY", "MD",
           "CALL CODE? ADDR X/Z+? CITY ID? INFO? TIME! ID? INFO+");
  }

  @Override
  public String getFilter() {
    return "waterwitch73@zoominternet.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n+"), 4, data);
  }

  private static final Pattern CODE_PTN = Pattern.compile("\\d{1,2}[A-Z]\\d{1,2}|[A-Z]{2,6}");
  private class MyCodeField extends CodeField {
    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0,pt).trim();
      if (! CODE_PTN.matcher(field).matches()) return false;
      data.strCode = field;
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private static final Pattern ID_PTN = Pattern.compile("\\d{8}");
  private static final Pattern OPS_PTN = Pattern.compile("\\bOPS\\b");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (ID_PTN.matcher(field).matches()) {
        if (data.strCallId.length() == 0) {
          data.strCallId = field;
          return;
        } else if (data.strCallId.equals(field)) return;
      }
      field = field.replaceAll("  +", " ");
      if (field.startsWith("@")) data.strUnit = field.replace("@", "");
      else if (OPS_PTN.matcher(field).find()) data.strChannel = field;
      else {
        if (field.startsWith("[")) field = field.substring(1).trim();
        super.parse(field, data);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "INFO ID UNIT CH";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CONO", "CONOWINGO",
      "NE",   "NORTH EAST",
      "PD",   "PORT DEPOSIT",
      "PV",   "PERRYVILLE"
  });
}
