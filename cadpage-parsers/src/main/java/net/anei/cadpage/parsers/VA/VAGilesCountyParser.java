package net.anei.cadpage.parsers.VA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class VAGilesCountyParser extends DispatchSouthernParser {

  public VAGilesCountyParser() {
    super(CITY_LIST, "GILES COUNTY", "VA",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_BAD_PLACE | DSFLG_PHONE | DSFLG_CODE | DSFLG_UNIT1 | DSFLG_ID | DSFLG_TIME);
    setupCities(MAP_CITY_TABLE);
    setupCities(WV_CITY_LIST);
  }

  private static final Pattern LEAD_PRI_PTN = Pattern.compile("(\\d);");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = LEAD_PRI_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strPriority = match.group(1);
      body = body.substring(match.end()).trim();
    }
    if (!super.parseMsg(body, data)) return false;

    data.strAddress = data.strAddress.replace("KATHERINE", "CATHERINE");
    if (WV_CITY_SET.contains(data.strCity)) data.strState = "WV";
    return true;
  }

  @Override
  public String getProgram() {
    return "PRI " + super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[] {
      "POPLAR HILL",    "PEARISBURG"
  });

  private static final String[] CITY_LIST = new String[]{

      // Towns
      "GLEN LYN",
      "NARROWS",
      "PEARISBURG",
      "PEMBROKE",
      "RICH CREEK",

      // Unincorporated communities
      "EGGLESTON",
      "GOLDBOND",
      "HOGES CHAPEL",
      "KIMBALLTON",
      "MAYBROOK",
      "NEWPORT",
      "PROSPECTDALE",
      "RIPPLEMEAD",
      "STAFFORDSVILLE",
      "TRIGG",
      "WHITE GATE",

      // Independent Cities
      "ROANOKE",

      // Bland County
      "BLAND",

      // Craig County
      "CRAIG",
      "CRAIG COUNTY",
      "NEW CASTLE",
      "NEWCASTLE",

      // Pulaski County
      "DUBLIN",

      // Smyth county
      "MARION"
  };

  private static final String[] WV_CITY_LIST = new String[] {

      // Monroe County, WV
      "BALLARD",
      "LINDSIDE",
      "PETERSTOWN",

      // Mercer County, WV
      "SPANISHBURG"
  };

  private static final Set<String> WV_CITY_SET = new HashSet<>(Arrays.asList(WV_CITY_LIST));
}
