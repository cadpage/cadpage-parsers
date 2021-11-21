package net.anei.cadpage.parsers.MI;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MICalhounCountyParser extends GroupBestParser {

  public MICalhounCountyParser() {
    super(new MICalhounCountyAParser(), new MICalhounCountyBParser(), new MICalhounCountyCParser());
  }

  static void cleanup(Data data) {
    // Correct Mnn to MI nn
    data.strAddress = M_ROUTE_PTN.matcher(data.strAddress).replaceAll("MI $1");
  }

  private static final Pattern M_ROUTE_PTN = Pattern.compile("\\bM *(\\d+)\\b");


  static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AL", "ALBION",
      "AS", "ATHENS TWP",
      "AT", "ALBION TWP",
      "BC", "BATTLE CREEK",
      "BE", "BEDFORD TWP",
      "BT", "BURLINGTON TWP",
      "BU", "BURLINGTON",
      "CL", "CLARENCE TWP",
      "CO", "CONVIS TWP",
      "CT", "CLARENDON TWP",
      "EC", "ECKFORD TWP",
      "ET", "EMMETT TWP",
      "FT", "FREDONIA TWP",
      "HC", "HILLSDALE COUNTY",
      "HO", "HOMER",
      "HP", "HOMER TWP",
      "HT", "HOMER TWP",
      "JC", "JACKSON COUNTY",
      "KC", "KALAMAZOO COUNTY",
      "LE", "LEE TWP",
      "LT", "LEROY TWP",
      "MA", "MARSHALL",
      "MO", "MARENGO TWP",
      "MT", "MARSHALL TWP",
      "NT", "NEWTON TWP",
      "PT", "PENNFIELD TWP",
      "RC", "BRANCH COUNTY",
      "SC", "ST JOSEPH COUNTY",
      "SF", "SPRINGFIELD",
      "ST", "SHERIDAN TWP",
      "TK", "TEKONSHA",
      "TT", "TEKONSHA TWP",
      "UC", "UNION CITY",
      "VA", "ATHENS"
  });
}
