package net.anei.cadpage.parsers.NY;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYDelawareCountyParser extends FieldProgramParser {
  
  public NYDelawareCountyParser() {
    super("DELAWARE COUNTY", "NY", 
          "( DATETIME | EMPTY | ) CODE? CALL ADDRCITY ( PLACENAME/Z! DATETIME! XST1:X_CITY! | ( XST1:X! XST2:X! | ID! ) CALLER:PLACENAME! ) END");
  }
  
  @Override
  public String getFilter() {
    return "delco911@co.delaware.ny.us";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("FLEISCHMANS FD: ")) return false;
    body = body.substring(16).trim();
    return parseFields(body.split("\\|"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("CODE"))  return new CodeField("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("CAD\\d\\d-\\d{6}", true);
    if (name.equals("X_CITY")) return new MyCrossCity();
    return super.getField(name);
  }
  
  @Override
  protected boolean checkPlace(String field) {
    if (field.endsWith(" HOME")) return true;
    if (field.equals("ADT ALARMS")) return true;
    return super.checkPlace(field);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        String zip = match.group(2);
        city = p.getLastOptional(',');
        if (zip != null && city.length() == 0) city = zip;
      }
      data.strCity = city;
      parseAddress(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private class MyCrossCity extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
}
