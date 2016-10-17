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
      "CK", "COCONUT CREEK",
      "CC", "COOPER CITY",
      "CS", "CORAL SPRINGS",
      "DN", "DANIA BEACH",
      "DV", "DAVIE",
      "DR", "DEERFIELD BEACH",
      "FL", "FORT LAUDERDALE",
      "HD", "HALLANDALE BEACH",
      "HW", "HOLLYWOOD",
      "LL", "LAUDERDALE LAKES",
      "LS", "LAUDERDALE-BY-THE-SEA",
      "LH", "LAUDERHILL",
      "LZ", "LAZY LAKE",
      "LP", "LIGHTHOUSE POINT",
      "MG", "MARGATE",
      "MM", "MIRAMAR",
      "NL", "NORTH LAUDERDALE",
      "OP", "OAKLAND PARK",
      "PK", "PEMBROKE PARK",
      "PP", "PEMBROKE PINES",
      "PL", "PLANTATION",
      "PB", "POMPANO BEACH",
      "SR", "SEA RANCH LAKES",
      "SW", "SOUTHWEST RANCHES",
      "SN", "SUNRISE",
      "TM", "TAMARAC",
      "WS", "WESTON",
      "WP", "WEST PARK",
      "WM", "WILTON MANORS"
     
  });
}
