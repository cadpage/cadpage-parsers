package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class NYRensselaerCountyParser extends SmartAddressParser {
  
  private static final Pattern DISPATCHER_PTN = Pattern.compile("(.*?) +- ?\\d{1,2}");
  private static final Pattern MISSED_BLANK_PTN = Pattern.compile("(?<! )(?=TOWN OF)", Pattern.CASE_INSENSITIVE);

  public NYRensselaerCountyParser() {
    super(CITY_LIST, "RENSSELAER COUNTY", "NY");
    setFieldList("CALL ADDR APT PLACE CITY INFO");
  }
  
  @Override
  public String getFilter() {
    return "renscobps3@gmail.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = DISPATCHER_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1);
    } else if (!isPositiveId()) return false;
    
    body = MISSED_BLANK_PTN.matcher(body).replaceAll(" ");
    parseAddress(StartType.START_CALL, FLAG_AT_PLACE, body, data);
    if (!isValidAddress()) return false;
    
    if (data.strCall.length() == 0) {
      data.strCall = getLeft();
    } else {
      data.strSupp = getLeft();
    }
    
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    "AVERILL PARK",
    "TOWN OF BERLIN",
    "TOWN OF BRUNSWICK",
    "CASTLETON-ON-HUDSON",
    "TOWN OF EAST GREENBUSH",
    "EAST NASSAU",
    "TOWN OF GRAFTON",
    "HAMPTON MANOR",
    "HOOSICK FALLS",
    "TOWN OF HOOSICK",
    "TOWN OF NASSAU",
    "NASSAU",
    "TOWN OF NORTH GREENBUSH",
    "TOWN OF PETERSBURGH",
    "TOWN OF PITTSTOWN",
    "TOWN OF POESTENKILL",
    "RENSSELAER",
    "TOWN OF SAND LAKE",
    "TOWN OF SCHAGHTICOKE",
    "SCHAGHTICOKE",
    "TOWN OF SCHODACK",
    "TOWN OF STEPHENTOWN",
    "TROY",
    "VALLEY FALLS",
    "WEST SAND LAKE",
    "WYNANTSKILL"
  };
}
	