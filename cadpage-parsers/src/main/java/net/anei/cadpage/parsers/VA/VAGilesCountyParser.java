package net.anei.cadpage.parsers.VA;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class VAGilesCountyParser extends DispatchA71Parser {

  public VAGilesCountyParser() {
    super("GILES COUNTY", "VA");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strAddress = data.strAddress.replace("KATHERINE", "CATHERINE");
    if (WV_CITY_SET.contains(data.strCity)) data.strState = "WV";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[] {
      "POPLAR HILL",    "PEARISBURG"
  });

  private static final Set<String> WV_CITY_SET = new HashSet<>(Arrays.asList(

      // Monroe County, WV
      "BALLARD",
      "LINDSIDE",
      "PETERSTOWN",

      // Mercer County, WV
      "SPANISHBURG"
      ));
}
