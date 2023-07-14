package net.anei.cadpage.parsers.IA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IACerroGordoCountyParser extends FieldProgramParser {

  public IACerroGordoCountyParser() {
    super("CERRO GORDO COUNTY", "IA",
          "Message:INFO! Time:DATE_TIME_PLACE! Address:ADDRCITYST! Nearest_intersection:X!");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,zuercher@cgcounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern TRAIL_GPS_PTN = Pattern.compile(" +([-+]?\\d{2,3}\\.\\d{3,} [-+]?\\d{2,3}\\.\\d{3,})$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Call Type:")) return false;
    data.strCall = subject.substring(10).trim();
    body = stripFieldEnd(body, " Please respond immediately.");
    int pt = body.lastIndexOf("\n\n");
    if (pt >= 0) body = body.substring(pt+2).trim();

    Matcher match = TRAIL_GPS_PTN.matcher(body);
    if (match.find()) {
      setGPSLoc(match.group(1), data);
      body = body.substring(0,match.start());
    }
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram() + " GPS";
  }

  @Override
  public Field  getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATE_TIME_PLACE")) return new MyDateTimePlaceField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PLACE_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d)\\b *(.*)");
  private class MyDateTimePlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PLACE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strPlace = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "None");
      super.parse(field, data);
    }
  }

  private static final Pattern SLASH_PTN = Pattern.compile(" */ *");
  private static final Pattern TRAIL_DIR_PTN = Pattern.compile(" *[NSEW]B?$");

  @Override
  protected String adjustGpsLookupAddress(String address) {
    if (address.startsWith("MM ")) {
      address = SLASH_PTN.matcher(address).replaceAll(" ");
      address = address.replace("I 35", "I35");
      address = TRAIL_DIR_PTN.matcher(address).replaceFirst("");
    }
    return address;
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "MM 180 I35",                           "+42.943993,-93.350784",
      "MM 182 I35",                           "+42.980130,-93.344672",
      "MM 188 I35",                           "+43.067302,-93.342899",
      "MM 190 I35",                           "+43.099276,-93.345547",
      "MM 193 I35",                           "+43.133711,-93.356062",
      "MM 194 I35",                           "+43.146258,-93.355832",
      "MM 197 I35",                           "+43.198764,-93.349298",

      "MM 183 HWY 18",                        "+43.105356,-93.260737",
      "MM 186 HWY 18",                        "+43.104299,-93.201360",
      "MM 190 HWY 18",                        "+43.112076,-93.122528",
      "MM 195 HWY 18",                        "+43.116339,-93.023539"
  });
}
