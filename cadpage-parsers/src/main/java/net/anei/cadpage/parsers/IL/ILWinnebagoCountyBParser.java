package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;

public class ILWinnebagoCountyBParser extends SmartAddressParser {

  public ILWinnebagoCountyBParser() {
    super(CITY_LIST, "WINNEBAGO COUNTY", "IL");
    setFieldList("ADDR APT CITY PLACE CALL");
  }

  @Override
  public String getFilter() {
    return "DoNotReply@rockfordil.gov";
  }

  private static final Pattern DELIM = Pattern.compile(" +\\*(?=\\S)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert")) return false;
    String[] parts = DELIM.split(body);
    if (parts.length != 2) return false;
    data.strCall = parts[1];
    parseAddress(StartType.START_ADDR, parts[0], data);
    if (data.strCity.isEmpty()) return false;
    data.strPlace = getLeft();
    return true;
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "LOVES PARK",
      "ROCKFORD",
      "SOUTH BELOIT",

      // Villages
      "CHERRY VALLEY",
      "DURAND",
      "MACHESNEY PARK",
      "NEW MILFORD",
      "PECATONICA",
      "ROCKTON",
      "ROSCOE",
      "WINNEBAGO",

      // Census-designated place
      "LAKE SUMMERSET",

      // Unincorporated communities
      "ALWORTH",
      "ARGYLE",
      "HARLEM",
      "HARRISON",
      "KISHWAUKEE",
      "LATHAM PARK",
      "SEWARD",
      "SHIRLAND",
      "WEMPLETOWN",
      "WESTFIELD CORNERS",

      // Townships
      "BURRITT",
      "CHERRY VALLEY",
      "DURAND",
      "HARLEM",
      "HARRISON",
      "LAONA",
      "OWEN",
      "PECATONICA",
      "ROCKFORD",
      "ROCKTON",
      "ROSCOE",
      "SEWARD",
      "SHIRLAND",
      "WINNEBAGO",

      // Former Settlement
      "CAMP GRANT"
  };

}
