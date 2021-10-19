package net.anei.cadpage.parsers.VT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class VTHartfordParser extends DispatchA19Parser {

  public VTHartfordParser() {
    super(CITY_CODES, "HARTFORD", "VT");
  }

  @Override
  public String getFilter() {
    return "fire@hartford-vt.org,spillman@test.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (NH_CITIES.contains(data.strCity.toUpperCase())) data.strState = "NH";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY",  "CITY ST");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
         "AND",  "ANDOVER",
         "BAL",  "BALTIMORE",
         "BAR",  "BARNARD",
         "BET",  "BETHEL",
         "BNT",  "BRAINTREE",
         "BRI",  "BRIDGEWATER",
         "BRK",  "BROOKFIELD",
         "CAV",  "CAVENDISH",
         "CHE",  "CHELSEA",
         "CHR",  "CHESTER",
         "CLEM", "CLAREMONT",
         "COR",  "CORNISH",
         "FAE",  "FAIRLEE",
         "GRA",  "GRAFTON",
         "HAN",  "HANOVER",
         "HRL",  "HARTLAND",
         "HRT",  "HARTFORD",
         "LEB",  "LEBANON",
         "LON",  "LONDONDERRY",
         "LUD",  "LUDLOW",
         "NRW",  "NORWICH",
         "PER",  "PERU",
         "PIT",  "PITTSFIELD",
         "PLF",  "PLAINFIELD",
         "PLY",  "PLYMOUTH",
         "POM",  "POMFRET",
         "RAN",  "RANDOLPH",
         "RCK",  "ROCKINGHAM",
         "REA",  "READING",
         "ROC",  "ROCHESTER",
         "ROY",  "SOUTH ROYALTON",
         "SHA",  "SHARON",
         "SLD",  "SOUTH LONDONERRY",
         "SPR",  "SPRINGFIELD",
         "SRO",  "SOUTH ROYALTON",
         "SXR",  "SAXTON RIVER",
         "WDM",  "WINDHAM",
         "WES",  "WESTON",
         "WSR",  "WINDSOR"

  });

  private static final Set<String> NH_CITIES = new HashSet<String>(Arrays.asList(new String[]{
      "CLAREMONT",
  }));

}
