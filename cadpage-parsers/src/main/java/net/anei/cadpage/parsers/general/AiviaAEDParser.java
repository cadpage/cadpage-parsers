package net.anei.cadpage.parsers.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class AiviaAEDParser extends FieldProgramParser {
  
  public AiviaAEDParser() {
    super("", "", 
          "CALL1! Date/Hour:DATETIME! CALL/SDS ADDR ZIP_CITY!");
  }
  
  @Override public String getFilter() {
    return "supervisionaivianet@hd1py.com";
  }

  @Override
  public String getLocName() {
    return "Avida AED";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Aivia ")) return false;
    return parseFields(body.split("\n+"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ZIP_CITY")) return new MyZipCityField();
    return super.getField(name);
  }
  
  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Alarm on Aivia ");
      super.parse(field, data);
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d{2}-\\d{2}) (\\d\\d?:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2).replace('-', '/') + '/' + match.group(1);
      data.strTime = match.group(3);
    }
  }
  
  private class MyAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt < 0) abort();
      data.strPlace = field.substring(pt+1).trim();
      parseAddress(field.substring(0,pt).trim(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE";
    }
  }
  
  private class MyZipCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt < 0) abort();
      data.strCity = field.substring(pt+1).trim();
      if (data.strCity.length() == 0) data.strCity = field.substring(0,pt).trim();
    }
  }

}
