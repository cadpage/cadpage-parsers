package net.anei.cadpage.parsers.dispatch;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA16Parser extends FieldProgramParser {
  
  private static final String DEFAULT_SUBJECT = "Imc Solutions Page";
  
  private String chkSubject;
  
  public DispatchA16Parser(String[] cityList, String defCity, String defState) {
    this(DEFAULT_SUBJECT, cityList, defCity, defState);
  }
  
  public DispatchA16Parser(String chkSubject, String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "CALL STATUS? ( DATETIME2!  UNIT? ( ADDR/Z! CITY | PLACE ADDR STATUS/Z+? CITY | ADDR! STATUS/Z+? CITY | PLACE ADDR! STATUS/Z+? CITY | PLACE ADDR/Z! STATUS/Z CITY | PLACE? ADDR/Z! CITY ) " + 
                       "| ( PLACENAME ADDR/Z CITY | ADDR/Z CITY | PLACENAME? ADDR! ) INFO+? ( UNIT DATETIME1? | DATETIME1 ) ) INFO+");
    this.chkSubject = chkSubject;
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (chkSubject != null && !subject.equals(chkSubject)) return false;
    return parseFields(body.split("\n"), 4, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d{2}:\\d{2}", true);
    if (name.equals("DATETIME2")) return new MyDateTime2Field();
    if (name.equals("UNIT")) return new UnitField("(?:Fire )?District: *(.*)");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("STATUS")) return new MyStatusField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }
  
  private static final Pattern STATUS_PTN = Pattern.compile(".*\\bUnder Control\\b.*", Pattern.CASE_INSENSITIVE);
  private class MyStatusField extends Field {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (!STATUS_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      parseStatusField(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CALL INFO";
    }
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4})(?: (\\d\\d?:\\d\\d[AP]M))?", Pattern.CASE_INSENSITIVE);
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mmaa");  
  private class MyDateTime2Field extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time != null) {
        setTime(TIME_FMT, time, data);
      }
      return true;
    }
    
    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      field = stripFieldStart(field, "Addr:");
      if (field.contains(",")) return false;
      return super.checkParse(field, data);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Addr:");
      super.parse(field, data);
    }
  }
  
  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*), *([A-Z]{2})(?:/ +(.*))?");
  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      return doParse(field, data, false);
    }
    
    @Override
    public void parse(String field, Data data) {
      doParse(field, data, true);
    }
    
    public boolean doParse(String field, Data data, boolean force) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        super.parse(match.group(1).trim(), data);;
        data.strState = match.group(2);
        String status = match.group(3);
        if (status != null) parseStatusField(status, data);
        return true;
      }
      if (force) {
        super.parse(field, data);
        return true;
      } 
      
      return super.checkParse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ST INFO";
    }
  }

  private static void parseStatusField(String field, Data data) {
    if (data.strCall.contains(field)) return;
    if (! data.strCall.contains(" - ")) {
      data.strCall = append(data.strCall, " - ", field);
    } else {
      if (data.strSupp.contains(field)) return;
      data.strSupp = append(data.strSupp, " / ", field);
    }
  }
}
