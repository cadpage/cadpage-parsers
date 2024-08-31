package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class MIRoscommonCountyParser extends DispatchOSSIParser {

  public MIRoscommonCountyParser() {
    super(CITY_CODES, "ROSCOMMON COUNTY", "MI",
          "( CANCEL ADDR CITY! " +
          "| UNIT ENROUTE ADDR CITY! CALL " +
          "| FYI? CITY? CALL ADDR! CITY? UNIT2/C+? " +
          ") INFO/N+? GPS1 GPS2 PLACE END");
  }

  @Override
  public String getFilter() {
    return "CAD@roscommoncounty.net,@rc911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.contains(",Enroute,")) {
      return parseFields(stripFieldStart(body, "CAD:").split(","), data);
    } else {
      if (!body.startsWith("CAD:")) body = "CAD:" + body;
      return super.parseMsg(body, data);
    }
  }

  public Field getField(String name) {
    if (name.equals("UNIT2")) return new UnitField("[A-Z]{3,4}", true);
    if (name.equals("ENROUTE")) return new CallField("Enroute");
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, false);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AUS",  "AUSABLE TWP",
      "AUSA", "AUSABLE TWP",
      "BAC",  "BACKUS TWP",
      "BACK", "BACKUS TWP",
      "DEN",  "DENTON TWP",
      "DENT", "DENTON TWP",
      "GAP1", "GERRISH TWP",
      "GAP2", "GERRISH TWP",
      "GAP3", "GERRISH TWP",
      "GER",  "GERRISH TWP",
      "GERR", "GERRISH TWP",
      "HIG",  "HIGGINS TWP",
      "HIGG", "HIGGINS TWP",
      "LAK",  "LAKE TWP",
      "LAKE", "LAKE TWP",
      "LYO",  "LYON TWP",
      "LYON", "LYON TWP",
      "MAR",  "MARKEY TWP",
      "MARK", "MARKEY TWP",
      "NES",  "NESTER TWP",
      "NEST", "NESTER TWP",
      "RIC",  "RICHFIELD TWP",
      "RICH", "RICHFIELD TWP",
      "ROS",  "ROSCOMMON TWP",
      "ROSC", "ROSCOMMON TWP",
      "VIL",  "ROSCOMMON",
      "VILL", "ROSCOMMON"
  });
}
