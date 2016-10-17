package net.anei.cadpage.parsers.OK;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKClevelandCountyBParser extends FieldProgramParser {
  
  public OKClevelandCountyBParser() {
    super("CLEVELAND COUNTY", "OK", 
          "DATETIME CALL ADDRCITY PLACE NAME PHONE INFO http:SKIP");
  }
  
  @Override
  public String getFilter() {
    return "@NormanOK.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("")) return false;
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa"); 
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      setTime(TIME_FMT, match.group(2), data);
    }
  }
  
  private static final Pattern ADDR_DASH_PTN = Pattern.compile(" -{2,} ");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      field = ADDR_DASH_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
      String city = CITY_CODES.getProperty(data.strCity);
      if (city != null) data.strCity = city;
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
 
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "POTT CO",      "Pottawatomie County"
  });
}
