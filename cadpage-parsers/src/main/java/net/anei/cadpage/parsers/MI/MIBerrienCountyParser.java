package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA87Parser;

public class MIBerrienCountyParser extends DispatchA87Parser {

  public MIBerrienCountyParser() {
    super("BERRIEN COUNTY", "MI");
    setupCities(CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "Dispatch@berriencounty.org";
  }

  private static final Pattern CITY_CODE_PTN = Pattern.compile("\\d\\d +(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CITY_CODE_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);

    if (data.strCity.isEmpty()) {
      if (!data.strApt.isEmpty() && isCity(data.strApt)) {
        data.strCity = data.strApt;
        data.strApt = "";
      }
    }
    if (data.strCity.equalsIgnoreCase("NEW CARLISLE")) data.strState = "IN";
    return true;
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    return apt.startsWith("EXIT") || apt.startsWith("&");
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "BENTON HARBOR",
      "BRIDGMAN",
      "BUCHANAN",
      "COLOMA",
      "NEW BUFFALO",
      "NILES",
      "ST JOSEPH",
      "WATERVLIET",

      // Villages
      "BARODA",
      "BERRIEN SPRINGS",
      "EAU CLAIRE",
      "GALIEN",
      "GRAND BEACH",
      "MICHIANA",
      "SHOREHAM",
      "STEVENSVILLE",
      "THREE OAKS",

      // TOWNSHIPs
      "BENTON TOWNSHIP",
      "COLOMA TOWNSHIP",
      "LAKE TOWNSHIP",
      "LINCOLN TOWNSHIP",
      "NILES TOWNSHIP",
      "ORONOKO TOWNSHIP",
      "ST. JOSEPH TOWNSHIP",
      "WATERVLIET TOWNSHIP",

      // Civil TOWNSHIPs
      "BAINBRIDGE TOWNSHIP",
      "BARODA TOWNSHIP",
      "BERRIEN TOWNSHIP",
      "BERTRAND TOWNSHIP",
      "BUCHANAN TOWNSHIP",
      "CHIKAMING TOWNSHIP",
      "GALIEN TOWNSHIP",
      "HAGAR TOWNSHIP",
      "NEW BUFFALO TOWNSHIP",
      "PIPESTONE TOWNSHIP",
      "ROYALTON TOWNSHIP",
      "SODUS TOWNSHIP",
      "THREE OAKS TOWNSHIP",
      "WEESAW TOWNSHIP",

      // Census-designated places
      "BENTON HEIGHTS",
      "FAIR PLAIN",
      "LAKE MICHIGAN BEACH",
      "MILLBURG",
      "NEW TROY",
      "PAW PAW LAKE",
      "SHOREWOOD-TOWER HILLS-HARBERT",
      "OTHER UNINCORPORATED COMMUNITIES",
      "BERRIEN CENTER",
      "BETHANY BEACH",
      "BIRCHWOOD",
      "DAYTON",
      "GLENDORA",
      "HARBERT",
      "HAZELHURST",
      "HINCHMAN",
      "LAKESIDE",
      "MILLBURG",
      "RIVERSIDE",
      "SAWYER",
      "SCOTTDALE",
      "SHOREWOOD HILLS",
      "TOWER HILL SHORELANDS",
      "UNION PIER",

      "CASS CO",
      "CASS",
      "COVERT",
      "NEW CARLISLE"
  };
}
