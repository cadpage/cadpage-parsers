package net.anei.cadpage.parsers.TN;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class TNMadisonCountyBParser extends DispatchOSSIParser {

  public TNMadisonCountyBParser() {
    super(CITY_CODES, "MADISON COUNTY", "TN",
          "( UNIT ENROUTE ADDR CITY! INFO/N+ " +
          "| FYI? ( CANCEL ADDR CITY! INFO/N+ " +
               "| ADDR CALL! X+? INFO/N+? GPS1 GPS2 ( ID | UNIT? PLACE? ID ) " +
               ") " +
          ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains(",Enroute,")) {
      return parseFields(body.split(","), data);
    }
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ENROUTE")) return new SkipField("Enroute");
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("UNIT")) return new UnitField("(?:\\b(?:[A-Z]+\\d+|\\d{3})\\b,?)+", true);
    if (name.equals("ID")) return new IdField("\\d{10}");
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "JACK", "JACKSON"
  });

}
