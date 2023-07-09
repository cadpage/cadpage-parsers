package net.anei.cadpage.parsers.FL;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchA52Parser;

public class FLLakeCountyBParser extends DispatchA52Parser {

  public FLLakeCountyBParser() {
    super("LAKE COUNTY", "FL");
  }

  @Override
  public String getFilter() {
    return "tap@yourdomain.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("LOC:")) body = "CMT:" + body;
    body = body.replace("LAT:", " LAT:");
    if (!super.parseMsg(body, data)) return false;
    String call = CALL_CODES.getCodeDescription(data.strCall);
    if (call != null) {
      data.strCode = data.strCall;
      data.strCall = call;
    }
    data.strCity = expandCity(data.strCity);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

  /**
   * Expand possibly truncated city
   * @param truncCity possibly truncated city
   * @return corrected city name
   */

  private String expandCity(String truncCity) {
    SortedSet<String> set = CITY_SET.tailSet(truncCity);
    String city = null;
    for (String tmp : set) {
      if (!tmp.startsWith(truncCity)) break;
      if (city == null) {
        city = tmp;
      } else {
        city = "";
        break;
      }
    }
    if (city != null) return city;
    return truncCity;
  }

  private static final CodeTable CALL_CODES = new StandardCodeTable();

  private static final TreeSet<String> CITY_SET = new TreeSet<>(Arrays.asList(new String[]{

      // Cities
      "CLERMONT",
      "EUSTIS",
      "FRUITLAND PARK",
      "GROVELAND",
      "LEESBURG",
      "MASCOTTE",
      "MINNEOLA",
      "MOUNT DORA",
      "TAVARES",
      "UMATILLA",

      // Towns
      "ASTATULA",
      "HOWEY-IN-THE-HILLS",
      "LADY LAKE",
      "MONTVERDE",

      // Census-designated places
//      "ALTOONA",
//      "ASTOR",
//      "FERNDALE",
//      "FOUR CORNERS",
//      "LAKE KATHRYN",
//      "LAKE MACK-FOREST HILLS",
//      "LISBON",
////      "MOUNT PLYMOUTH",  conflict with MOUNT DORA
//      "OKAHUMPKA",
//      "PAISLEY",
//      "PINE LAKES",
//      "PITTMAN",
//      "SILVER LAKE",
//      "SORRENTO",
//      "YALAHA",
//
//      // Other unincorporated communities
//      "BASSVILLE PARK",
//      "FORT MASON",
//      "GRAND ISLAND",
//      "LANIER",
//      "ORANGE BEND",
//      "THE VILLAGES"
  }));

}
