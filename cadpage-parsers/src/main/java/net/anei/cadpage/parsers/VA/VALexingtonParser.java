package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class VALexingtonParser extends DispatchH05Parser {

  public VALexingtonParser() {
    super("LEXINGTON", "VA", 
          "Date/Time:DATE_TIME_ID! LOC:ADDRCITY! XSTS:X! Lat/Long:GPS! Quad:MAP! Dist:MAP/L! Radio_Channel:CH! Details:EMPTY! TIMES+");
  }
  
  public String getFilter() {
    return "noreply@lexingtonva.gov";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATE_TIME_ID")) return new MyDateTimeIdField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("CH")) return new MyChannelField();
    return super.getField(name);
  }
  
  private static final Pattern DATE_TIME_ID_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d) Incident#: *(.*)");
  private class MyDateTimeIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strCallId = match.group(3);
    }
    
    @Override
    public String getFieldNames() {
      return "DATE TIME ID";
    }
  }
  
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
  
  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      super.parse(field, data);
    }
  }
}
