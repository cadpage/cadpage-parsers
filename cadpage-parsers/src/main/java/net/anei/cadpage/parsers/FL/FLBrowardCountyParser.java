package net.anei.cadpage.parsers.FL;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchPrintrakParser;

/**
 * Broward County, FL
 */

public class FLBrowardCountyParser extends DispatchPrintrakParser {
  
  public FLBrowardCountyParser() {
    super(CITY_TABLE, "BROWARD COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "CAD@Sheriff.org,CAD_Notify@regionalpsi.net";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body, data);
  }

  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "BC", "BROWARD COUNTY",
      "CC", "COOPER CITY",
      "CK", "COCONUT CREEK",
      "CS", "CORAL SPRINGS",
      "DN", "DANIA BEACH",
      "DR", "DEERFIELD BEACH",
      "DV", "DAVIE",
      "FL", "FORT LAUDERDALE",
      "HB", "HILLSBORO BEACH",
      "HD", "HALLANDALE BEACH",
      "HW", "HOLLYWOOD",
      "LH", "LAUDERHILL",
      "LL", "LAUDERDALE LAKES",
      "LP", "LIGHTHOUSE POINT",
      "LS", "LAUDERDALE-BY-THE-SEA",
      "LZ", "LAZY LAKE",
      "MG", "MARGATE",
      "MM", "MIRAMAR",
      "NL", "NORTH LAUDERDALE",
      "OP", "OAKLAND PARK",
      "PB", "POMPANO BEACH",
      "PD", "PARKLAND",
      "PK", "PEMBROKE PARK",
      "PL", "PLANTATION",
      "PP", "PEMBROKE PINES",
      "SM", "BIG CYPRESS RESERVATION",
      "SN", "SUNRISE",
      "SR", "SEA RANCH LAKES",
      "SW", "SOUTHWEST RANCHES",
      "TM", "TAMARAC",
      "WM", "WILTON MANORS",
      "WP", "WEST PARK",
      "WS", "WESTON"
  });
}
