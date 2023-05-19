package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALCoosaCountyBParser extends DispatchSouthernParser {

  public ALCoosaCountyBParser() {
    super(CITY_LIST, "COOSA COUNTY", "AL",
          DSFLG_ADDR | DSFLG_ID | DSFLG_TIME );
    setupCities(MISPELLED_CITIES);
    removeWords("SQUARE");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCall = stripFieldEnd(data.strCall, "-");
    data.strCity = convertCodes(data.strCity, MISPELLED_CITIES);
    return true;
  }

  private static final Pattern CCR_PTN = Pattern.compile("\\bCCR\\b");

  @Override
  public String adjustMapAddress(String addr) {
    return CCR_PTN.matcher(addr).replaceAll("COUNTY ROAD");
  }

  private static final String[] CITY_LIST = new String[] {

      // Towns
      "GOODWATER",
      "KELLYTON",
      "ROCKFORD",

      // Census-designated places
      "EQUALITY",
      "HANOVER",
      "HISSOP",
      "MOUNT OLIVE",
      "NIXBURG",
      "RAY",
      "STEWARTVILLE",
      "WEOGUFKA",

      // Unincorporated communities
      "DOLLAR",
      "FISHPOND",
      "HATCHET",
      "MARBLE VALLEY",
      "RICHVILLE",

      // Clay County
      "CLAY COUNTY",

      // Talladega County
      "TALLADEGA",

      // Tallaploosa County
      "ALEXANDER CITY",
      "DADEVILLE",
      "SYLACAUGA"
  };

  private static final Properties MISPELLED_CITIES = buildCodeTable(new String[] {
      "STEWARTIVVLLE",    "STEWARTVILLE"
  });

}
