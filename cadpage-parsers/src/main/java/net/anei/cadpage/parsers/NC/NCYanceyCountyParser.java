package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCYanceyCountyParser extends FieldProgramParser {
  
  public NCYanceyCountyParser() {
    super("YANCEY COUNTY", "NC", 
          "CALL ADDR/S! http:EMPTY! GPS INFO+");
  }
  
  @Override
  public String getFilter() {
    return "YANCEY_EOC@smtp.gmail.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("YANCEY_EOC:")) return false;
    body = body.substring(11).trim();
    return parseFields(body.split("//"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("maps.apple.com/\\?daddr=(?:([-+]?\\d+\\.\\d{3,})/([-+]?\\d+\\.\\d{6,})\\b)?.*");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      String gps1 = match.group(1);
      String gps2 = match.group(2);
      if (gps1 != null) setGPSLoc(gps1 + ',' + gps2, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) data.strCall = field;
      else data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
