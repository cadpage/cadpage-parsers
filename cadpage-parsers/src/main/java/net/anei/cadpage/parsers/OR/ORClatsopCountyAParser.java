package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA85Parser;

public class ORClatsopCountyAParser extends DispatchA85Parser {

  public ORClatsopCountyAParser() {
    super(CITY_CODES, "CLATSOP COUNTY", "OR");
    setupGpsLookupTable(ORClatsopCountyParser.GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "@astoria.or.us";
  }

  private static final Pattern PROM_PTN = Pattern.compile("\\bPROM\\b", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Expand PROM -> PROMONADE
    subject = PROM_PTN.matcher(subject).replaceAll("PROMENADE");
    body = PROM_PTN.matcher(body).replaceAll("PROMENADE");
    if (!super.parseMsg(subject,  body, data)) return false;
    data.strSupp = stripFieldStart(data.strSupp, "Scrath Pad:");
    return true;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("CAPE FALCON")) city = "ARCH CAPE";
    return city;
  }

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return ORClatsopCountyParser.doAdjustGpsLookupAddress(address);
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ARC", "ARCH CAPE",
      "AST", "ASTORIA",
      "CAN", "CANNON BEACH",
      "FAL", "CAPE FALCON",
      "GEA", "GEARHEART",
      "HAM", "HAMLET",
      "KNA", "KNAPPA",
      "SEA", "SEASIDE",
      "TOL", "TOLVANA PARK",
      "WAR", "WARRENTON",
      "WES", "WESTPORT"
  });
}
