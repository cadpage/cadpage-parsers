package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


/**
 * Monterey County, CA
 */
public class CAMontereyCountyAParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("(?:(.*?) - )?([A-Z0-9]{2,6}):(.*?) - (.*?)(?: - ([A-Z]{3}))? *(?:Units?:(.*?))?");
  
  public CAMontereyCountyAParser() {
    super("MONTEREY COUNTY", "CA");
    setFieldList("MAP CODE CALL ADDR PLACE CITY UNIT INFO");
  }
  
  @Override
  public String getFilter() {
    return "donotreply@co.monterey.ca.us,donotreply@co.monterey,MONTEREY911@CO.MONTEREY.CA.US>";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("CAD Page")) return false;
    
    int pt = body.indexOf('\n');
    String extra = null;
    if (pt >= 0) {
      extra = body.substring(pt+1).trim();
      body = body.substring(0,pt).trim();
    }
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strMap = getOptGroup(match.group(1));
    data.strCode = match.group(2);
    data.strCall = match.group(3).trim();
    parseAddress(match.group(4).trim(), data);
    String city = match.group(5);
    if (city != null) data.strCity = convertCodes(city, CITY_CODES);
    data.strUnit = getOptGroup(match.group(6));
    
    pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strPlace = data.strCity.substring(0,pt);
      data.strCity = data.strCity.substring(pt+1);
    }
    
    if (extra != null) {
      if (extra.startsWith("Message:")) extra = extra.substring(8).trim();
      data.strSupp = extra;
    }
    
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ARO",   "AROMAS",
      "ARS",   "ARROYO SECO/GREENFIELD",
      "BKN",   "BOLSA KNOLLS",
      "BOR",   "BORONDA",
      "BRD",   "BRADLEY",
      "CAS",   "CASTROVILLE",
      "CDT",   "CORRAL DE TIERRA",
      "CHI",   "CARMEL HIGHLANDS",
      "CHS",   "CARMEL HILLS",
      "CHU",   "CHUALAR",
      "CML",   "CARMEL",
      "CMM",   "CARMEL MEADOWS/CARMEL",
      "CMP",   "CARMEL POINT",
      "CMV",   "CARMEL VALLEY",
      "CMW",   "CARMEL WOODS",
      "CST",   "COAST/CARMEL",
      "CSU",   "CAL STATE UNIVERSITY/SEASIDE",
      "CVR",   "CARMEL VALLEY RANCH/CARMEL",
      "CVW",   "CARMEL VIEWS/CARMEL",
      "DLI",   "DEFENSE LANGUAGE INSTITUTE/MONTEREY",
      "DMF",   "DEL MONTE FOREST",
      "DRO",   "DEL REY OAKS",
      "FTO",   "FORT ORD/SEASIDE",
      "GON",   "GONZALES",
      "GRN",   "GREENFIELD",
      "HHS",   "HIDDEN HILLS/SALINAS",
      "HIM",   "HIGH MEADOWS",
      "HTF",   "HATTON FIELDS",
      "IND",   "INDIAN SPRINGS/SALINAS",
      "JOL",   "JOLON",
      "KCY",   "KING CITY",
      "LAM",   "LAMESA VILLAGE/MONTEREY", 
      "LLG",   "LOS LAURELES GRADE/SALINAS",
      "LLS",   "LAS LOMAS",
      "LOC",   "LOCKWOOD/MONTEREY",
      "LPR",   "LAS PALMAS RANCH/SALINAS",
      "LSE",   "LAGUNA SECA ESTATES/SALINAS",
      "MAR",   "MARINA",
      "MCO",   "",                            // MONTEREY COUNTY
      "MDL",   "MONTE DEL LAGO/CASTROVILLE",
      "MPA",   "MONTEREY AIRPORT/MONTEREY",
      "MSF",   "MISSION FIELDS",
      "MSL",   "MOSS LANDING",
      "MTR",   "MONTERA RANCH/MONTEREY",
      "MTY",   "MONTEREY",
      "NAF",   "NAVY ANNEX FACILITY/MONTEREY",
      "NPS",   "NAVY POSTGRADUATE SCHOOL/MONTEREY",
      "OHS",   "OAK HILLS",
      "ORD",   "FT ORD/SEASIDE",
      "PAC",   "PACIFIC GROVE",
      "PAJ",   "PAJARO",
      "PGS",   "POSTGRADUATE SCHOOL/MONTEREY",
      "PRU",   "PRUNEDALE",
      "RDR",   "ROBLES DEL RIO",
      "SAR",   "SAN ARDO",
      "SBC",   "SAN BENITO COUNTY",
      "SBN",   "SAN BENANCIO",
      "SCO",   "",                          // SOUTH COUNTY
      "SCY",   "SAND CITY",
      "SEA",   "SEASIDE",
      "SLP",   "SANTA LUCIA PRESERVE/CARMEL",
      "SLU",   "SAN LUCAS",
      "SNS",   "SALINAS",
      "SOL",   "SOLEDAD",
      "SPK",   "SPRECKELS",
      "SRV",   "SERRA VILLAGE/SALINAS",
      "TPE",   "TORO PARK ESTATES/SALINAS",
      "VGR",   "VALLEY GREENS/CARMEL",
      "WAT",   "WATSONVILLE",
      "YKP",   "YANKEE POINT",

  });
}
