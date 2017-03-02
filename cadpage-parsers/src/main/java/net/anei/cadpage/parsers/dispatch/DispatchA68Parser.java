package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA68Parser extends FieldProgramParser {
  
  public DispatchA68Parser(String defCity, String defState) {
    super(defCity, defState,
          "DATETIME UNIT ADDR! EMPTY? CALL INFO+");
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Emergency Message")) return false;
    return parseFields(body.split("\n"), 3, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("ADDR")) return new BaseAddressField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d) (\\d\\d)(\\d\\d)");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2)+':'+match.group(3);
    }
  }
  
  private static final Pattern ADDRESS_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLast(',');
      if (city.length() > 0) {
        if (ADDRESS_ST_PTN.matcher(city).matches()) {
          data.strState = city;
          city = p.getLast(',');
        }
        data.strCity = city;
        field = p.get();
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }
}
