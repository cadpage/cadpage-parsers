package net.anei.cadpage.parsers.VA;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class VACarolineCountyAParser extends MsgParser {
  
  public VACarolineCountyAParser() {
    super("CAROLINE COUNTY", "VA");
    setFieldList("DATE TIME ID CALL ADDR APT CITY PLACE INFO UNIT");
  }
  
  private static final Pattern MASTER = Pattern.compile("(?:superuser|ACTIVE 911-([A-Z0-9]+))?:As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) (\\d{12}) ([A-Z0-9]+) ([^,]+), *(.+?) (Call Created Time.*:(?: \\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d\\b)?) *(.*)");
  private static final Pattern INFO_SPLIT_PTN = Pattern.compile("(?<=\\d\\d:\\d\\d:\\d\\d) +");
  private static final Pattern UNIT_SPLIT_PTN = Pattern.compile("[, ]+");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    Set<String> unitSet = new HashSet<String>();
    
    String unit = match.group(1);
    if (unit != null) addUnit(unit, unitSet, data);
    data.strDate = match.group(2);
    data.strTime = match.group(3);
    data.strCallId = match.group(4);
    data.strCall = match.group(5);
    parseAddress(match.group(6).trim(), data);
    String cityPlace = match.group(7).trim();
    String city = CITY_LIST.getCode(cityPlace);
    if (city != null) {
      data.strCity = city;
      data.strPlace = cityPlace.substring(city.length()).trim();
    } else {
//      return false;
      data.strCity = cityPlace;
    }
    
    data.strSupp = INFO_SPLIT_PTN.matcher(match.group(8).trim()).replaceAll("\n");
    unit = match.group(9);
    for (String unt : UNIT_SPLIT_PTN.split(unit)) {
      addUnit(unt, unitSet, data);
    }
    return true;
  }
  
  private void addUnit(String unit, Set<String> unitSet, Data data) {
    unit = unit.toUpperCase();
    if (unitSet.add(unit)) {
      data.strUnit = append(data.strUnit, ",", unit);
    }
  }
  
  private static CodeSet CITY_LIST = new CodeSet(
      
      // Incorporated Towns
      "BOWLING GREEN",
      "PORT ROYAL",

      // Census designated places
      "LAKE CAROLINE",
      "LAKE LANDOR",

      // Unincorporated communities
      "ACORS CORNER",
      "ANN WRIGHTS CORNER",
      "ANTIOCH FORK",
      "ATHENS",
      "BAGBY",
      "BAGDAD",
      "BALTY",
      "BAYLORTOWN",
      "BLANTONS",
      "BRANDYWINE",
      "BROADDUS",
      "BROADUS CORNER",
      "BULLOCKS CORNER",
      "BURRUSS CORNER",
      "BUTLERS FORK",
      "CAMPBELL CORNER",
      "CAMPBELLS CORNER",
      "CAROLINE PINES",
      "CARTERS CORNER",
      "CASH CORNER",
      "CEDAR FORK",
      "CEDON",
      "CENTRAL POINT",
      "CHANDLER CROSSING",
      "CHENAULTS SHOP",
      "CHILESBURG",
      "CHRISTOPHER FORK",
      "CLAIBORNE",
      "COFFEY",
      "CORNER",
      "COLEMANS MILL",
      "CROSSING",
      "COLLINS CROSSING",
      "CORBIN",
      "COVINGSTON CORNER",
      "DALTONS",
      "DANIEL CORNER",
      "DAVIS CORNER",
      "DAWN",
      "DEJARNETTE",
      "DELOS",
      "DOGGETTS FORK",
      "EDGAR",
      "ELEVON",
      "ETTA",
      "EUBANK CORNER",
      "FEATHERSTONE FORK",
      "FLIPPOS CORNER",
      "FROG LEVEL",
      "GETHER",
      "GOLANSVILLE",
      "GOLDMANS CORNER",
      "GUINEA",
      "HALEYS CORNER",
      "HARD CORNER",
      "HART CORNER",
      "HAYMOUNT",
      "HICKORY FORK",
      "HICKS MILL",
      "HOUSTONS CORNER",
      "HOWARDS CORNER",
      "JONES CORNER",
      "KEMP CORNER",
      "KIDDS FORK",
      "LADYSMITH",
      "LAURAVILLE",
      "LENT",
      "LIBERTY",
      "LIBERTY FORK",
      "LOCKS CORNER",
      "LONG BRANCH",
      "LORNE",
      "LOVING FORK",
      "MARTINS CORNER",
      "MARYTON",
      "MCBRYANT CORNER",
      "MCDUFF",
      "MICA",
      "MILFORD",
      "MONCURE CORNER",
      "MONROE CORNER",
      "MOSS NECK",
      "NANCY WRIGHTS CORNER",
      "NAULAKLA",
      "NEW LONDON",
      "OAK CORNER",
      "OLIVE",
      "OLNEY CORNER",
      "PAIGE",
      "PATERSONS CORNER",
      "PEATROSS",
      "PENNY CORNER",
      "PENOLA",
      "POORHOUSE CORNER",
      "POPLAR",
      "PORT ROYAL CROSS ROADS",
      "PORTOBAGO",
      "PULLERS CORNER",
      "RAINES CORNER",
      "RANGE CORNER",
      "RAPPAHANNOCK ACADEMY",
      "RAPPAHANNOCK CORNER",
      "RAYMONDS FORK",
      "REEDY MILL",
      "RIXEY",
      "RUTHER GLEN",
      "RYLAND CORNER",
      "SALES CORNER",
      "SAMUELS CORNER",
      "SHUMANSVILLE",
      "SIGNBOARD",
      "SKINKERS CORNER",
      "SMOOTS",
      "SORRELL",
      "SPARTA",
      "STUART CORNER",
      "SWANS CORNER",
      "TAYLORS CORNER",
      "TIGNOR",
      "TRAVIS MILL",
      "UPPER ZION",
      "VALLEYVIEW CORNER",
      "VILLBORO",
      "WALLERS CORNER",
      "WASHINGTON CORNER",
      "WAVERLY WELCHS",
      "WOODFORD",
      "WRIGHTS CORNER",
      "WRIGHTS FORK",
      "WRIGHTSVILLE",
      "YOUNG CORNER",
      
      // Hanover County
      "HANOVER"
  );
}