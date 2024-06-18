package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchH05Parser;

public class KYKentonCountyBParser extends DispatchH05Parser {

  public KYKentonCountyBParser() {
    super("KENTON COUNTY", "KY",
          "CALL:CALL! PLACE:PLACE! ADDRCITYST X GPS! INFO:EMPTY! INFO_BLK/N+ Alerts:ALERT! DATE:DATETIME! INC_#'s:ID! UNITS:UNIT! TIMES:EMPTY! TIMES+");
  }

  @Override
  public String getFilter() {
    return "KCECCService@kentoncounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new AddressCityStateField("ADDR\\b *(.*?)[ ,]*", true);
    if (name.equals("X")) return new CrossField("(?:CROSS\\b *)?(.*)", true);
    if (name.equals("GPS")) return new GPSField("LAT:.* LON:.*", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
