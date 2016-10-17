package net.anei.cadpage.parsers.KY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYBowlingGreenParser extends FieldProgramParser {
  
  public KYBowlingGreenParser() {
    super("BOWLING GREEN", "KY", 
          "TIME:TIME! ALARM:PRI! LOC:ADDR! APT:APT? GPS! NAT:CALL! UNIT:UNIT! NOTES:INFO/S+ CROSS:X");
  }
  
  @Override
  public String getFilter() {
    return "noreply@bgky.org";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Assigned to Incident (\\d{4}-\\d{5})");
  
  @Override
  public  boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private static final Pattern GPS_PTN = Pattern.compile("<http://maps.google.com/maps\\?q=([-+]?\\d+\\.\\d{6})(?:%20| +)([-+]?\\d+\\.\\d{6})>");
  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1)+','+match.group(2), data);
      }
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
