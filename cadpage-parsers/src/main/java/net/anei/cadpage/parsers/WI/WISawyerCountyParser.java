package net.anei.cadpage.parsers.WI;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class WISawyerCountyParser extends DispatchA19Parser {

  public WISawyerCountyParser() {
    super(CITY_CODES, "SAWYER COUNTY", "WI");
  }

  @Override
  public String getFilter() {
    return "FlexRapidNotification@dccnotify.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("OUT OF COUNTY")) data.defCity = "";
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("OUT OF COUNTY")) return "";
    return city;
  }


  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "BAS", "BASS LAKE",
      "C",   "COUDERAY",
      "CHA", "HAYWARD",
      "CUD", "COUDERAY",
      "DRA", "DRAPER",
      "E",   "EDGEWATER",
      "EDG", "EDGEWATER",
      "EXE", "EXELAND",
      "HAY", "HAYWARD",
      "HUN", "HUNTER",
      "LBA", "BASS LAKE",
      "LEN", "LENROOT",
      "LCU", "COUDERAY",
      "LHA", "HAYWARD",
      "LHU", "HUNTER",
      "LRA", "RADISSON",
      "LSA", "SAND LAKE",
      "MEA", "MEADOWBROOK",
      "MET", "METEOR",
      "O",   "OJIBWA",
      "OJI", "OJIBWA",
      "OUT", "OUT OF COUNTY",
      "R",   "RADISSON",
      "RAD", "RADISSON",
      "ROU", "ROUND LAKE",
      "S",   "STONE LAKE",
      "SAN", "SAND LAKE",
      "SPI", "SPIDER LAKE",
      "THA", "HAYWARD",
      "VCN", "CONVERSION",
      "VEX", "EXELAND",
      "VCO", "COUDERAY",
      "VEX", "EXELAND",
      "VRA", "RADISSON",
      "VWI", "WINTER",
      "W",   "WINTER",
      "WEI", "WEIRGOR",
      "WIN", "WINTER"
  });

}
