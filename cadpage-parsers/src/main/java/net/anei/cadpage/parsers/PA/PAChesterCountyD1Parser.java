package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyD1Parser extends PAChesterCountyBaseParser {

  public PAChesterCountyD1Parser() {
    super("INCIDENT:CALL! STATUS:SKIP! EVENT_ID:ID! DISPATCH_TIME:DATETIME! BOX:BOX! DISPATCHED_UNITS:UNIT! LOCATION:EMPTY! ADDR! CITY! SKIP! " +
            "CAD_ADDRESS:EMPTY? Common_Place:PLACE? X1:X? X2:X? Map:GPS? CALLER_NAME:NAME! CALLER_ADDRESS:SKIP! CALLER_PHONE:PHONE! " +
            "NOTES:EMPTY! INFO/N+ PORTAL_LINK:URL! END");
  }

  @Override
  public String getFilter() {
    return "@alerts.stationcad.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Status: Dispatch - ")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("https://.*\\+USA/@(.*),17z/", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('(');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d ~ \\S+ - *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
}
