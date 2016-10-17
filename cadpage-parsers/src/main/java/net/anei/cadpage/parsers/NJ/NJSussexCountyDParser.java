package net.anei.cadpage.parsers.NJ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Sussex County, NJ
 */
public class NJSussexCountyDParser extends FieldProgramParser {
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  public NJSussexCountyDParser() {
    super("SUSSEX COUNTY", "NJ",
           "( EMS_CAD#:ID! | Fire_CAD#:ID! ) Time_of_Call:TIME! ADDR! ( Type_of_EMS_Call:CALL! | Type_of_Fire:CALL! ) Notes:INFO+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@hopatcongpd.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    return parseFields(body.split("\n"), 4, data);
  }
  
  private static final Pattern INFO_GPS_PTN = Pattern.compile("Latitude: *([-+]\\d{3}\\.\\d{6}|) +Longitude: *([-+]\\d{3}\\.\\d{6}|)(?: +\\d\\d?:\\d\\d:\\d\\d [AP]M)?");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(getOptGroup(match.group(1))+','+getOptGroup(match.group(2)), data);
        return;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{5}", true);
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
}
