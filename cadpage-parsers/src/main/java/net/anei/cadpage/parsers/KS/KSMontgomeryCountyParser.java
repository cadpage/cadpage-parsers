package net.anei.cadpage.parsers.KS;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;


public class KSMontgomeryCountyParser extends DispatchA25Parser {

  public KSMontgomeryCountyParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "KS");
  }

  @Override
  public String getFilter() {
    return "EnterpolAlerts@coffeyvillepd.org,cad@indypd.com,cad@independenceks.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (OK_CITIES.contains(data.strCity.toUpperCase())) data.strState = "OK";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "CANEY",
      "CHERRYVALE",
      "COFFEYVILLE",
      "DEARING",
      "ELK CITY",
      "HAVANA",
      "INDEPENDENCE",
      "LIBERTY",
      "TYRO",

      // Unincorporated communities
      "AVIAN",
      "BLAKE",
      "BOLTON",
      "CORBIN",
      "JEFFERSON",
      "SYCAMORE†",
      "VIDETTA SPUR",
      "WAYSIDE",

      // Ghost towns
      "LE HUNT",

      // Chautauqua County
      "CHAUTAUQUA",
      "NIOTAZE",
      "PERU",
      "SEDAN",

      // Elk County
      "ELK FALLS",
      "LONGTON",
      "OAK VALLEY",

      // Labette County
      "ALTAMONT",
      "ANGOLA",
      "DENIS",
      "EDNA",
      "MOUND VALLEY",

      // Neosha County
      "EARLTON",
      "GALESBURG",
      "THAYER",

      // Noweta County, OK
      "LENAPAH",
      "NOXIE",
      "SOUTH COFFEYVILLE",
      "WANN",

      // Washington County, OK
      "COPAN",

      // Wilson County
      "ALTOONA",
      "FREDONIA",
      "LAFONTAINE",
      "NEODESHA",

      // Miami County
      "OSWATOMIE"
  };

  private static final Set<String> OK_CITIES = new HashSet<>(Arrays.asList(
      "COPAN",
      "LENAPAH",
      "NOXIE",
      "SOUTH COFFEYVILLE",
      "WANN"
  ));
}
