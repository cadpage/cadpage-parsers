package net.anei.cadpage.parsers.FL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
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
    return "CAD@Sheriff.org,CAD_Notify@regionalpsi.net,bsocad@southfloridarest.org";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean mixedMsgOrder() { return true; }
    };
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern TRAIL_CH_PTN = Pattern.compile("(.*?)[- ]* +([A-Z]{2}/[ A-Z]+)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n\n--");
    if (pt >= 0) {
      int pt2 = body.indexOf(" TYPN:", pt);
      String trail = pt2 >= 0 ? body.substring(pt2) : null;
      body = body.substring(0,pt).trim();
      if (trail != null) body = body + trail;
    }
    Matcher match = TRAIL_CH_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1);
      data.strChannel = match.group(2);
    } else {
      data.expectMore = true;
    }
    body = body.replace("DROP TIME:", "DROP_TIME:");
    if (!super.parseMsg(body, data)) return false;
    data.strSupp = data.strSupp.replace("DROP_TIME:", "DROP TIME:");
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " CH";
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
