package net.anei.cadpage.parsers.PA;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PALehighCountyCParser extends FieldProgramParser {
  
  public PALehighCountyCParser() {
    super("LEHIGH COUNTY", "PA", 
          "CALL! Address:ADDRCITY! XSt:X! INFO/N+ Assigned_Units:UNIT! GPS_DATE_TIME! END");
  }
  
  private static final Pattern DELIM = Pattern.compile(" \\| |\n");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS_DATE_TIME")) return new MyGPSDateTimeField();
    return super.getField(name);
  }
  
  private static final Pattern CITY_PTN = Pattern.compile("(LOWER MACUNGIE)\\b *(.*)");
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt < 0) abort();
      String addr = field.substring(0, pt).trim();
      String city = field.substring(pt+1).trim();
      addr = addr.replace('@',  '&');
      parseAddress(addr, data);

      Matcher match = CITY_PTN.matcher(city);
      if (match.matches()) {
        data.strCity = match.group(1);
        data.strPlace = match.group(2);
      } else {
        data.strPlace = city;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR CITY PLACE";
    }
  }
  
  private static final Pattern GPS_DATE_TIME_PTN = Pattern.compile("([-+]?\\d{2}\\.\\d{6,} [-+]?\\d{2}\\.\\d{6,}) +(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d [AP]M)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  private class MyGPSDateTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      setGPSLoc(match.group(1), data);
      data.strDate = match.group(2);
      setTime(TIME_FMT, match.group(3), data);
    }

    @Override
    public String getFieldNames() {
      return "GPS DATE TIME";
    }
    
  }
}
