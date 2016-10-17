package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;



public class MDCalvertCountyParser extends SmartAddressParser {
  
  private static final Pattern TIME_DATE_PTN = Pattern.compile("\\b(\\d\\d:\\d\\d) (\\d\\d/\\d\\d/\\d\\d)\\b");
  private static final String UNIT_STR = "(?:\\b(?:[A-Z]{1,5}\\d{1,3}|TN\\d|NDC|NMED|COM|NOMED|SDC|Dive|[A-Z]+TEAM|HMRT[A-Z]*)\\b[ ,]*)+";
  private static final Pattern UNIT_PTN = Pattern.compile(UNIT_STR);
  private static final Pattern UNIT_END_PTN = Pattern.compile(UNIT_STR+'$');
  private static final Pattern PRIORITY_PTN = Pattern.compile("M Priority (\\d) +(.*)");
  private static final Pattern ID_PTN = Pattern.compile("\\b\\d{4}-\\d{8}\\b");
  private static final Pattern INFO_BREAK_PTN1 = Pattern.compile("^[-A-Z ]+?(\\b)(?:$|[A-Z]*[a-z0-9])");
  private static final Pattern INFO_BREAK_PTN2 = Pattern.compile("(?<!\\b[NSEW])  +");
  
  public MDCalvertCountyParser() {
    super(CITY_LIST, "CALVERT COUNTY", "MD");
    setFieldList("TIME DATE PRI CALL UNIT BOX ADDR APT CITY PLACE ID CODE INFO");
    setupSpecialStreets("TJ BRIDGE");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@co.cal.md.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher timeDateMatch = TIME_DATE_PTN.matcher(body);
    if (! timeDateMatch.find()) return false;
    data.strTime = timeDateMatch.group(1);
    data.strDate = timeDateMatch.group(2);
    
    String strCall = body.substring(0,timeDateMatch.start()).trim();
    Matcher unitMatch = UNIT_END_PTN.matcher(strCall);
    if (unitMatch.find()) {
      data.strUnit = unitMatch.group().trim();
      strCall = strCall.substring(0,unitMatch.start()).trim();
    }
    Matcher priMatch = PRIORITY_PTN.matcher(strCall);
    if (priMatch.matches()) {
      data.strPriority = priMatch.group(1);
      strCall = priMatch.group(2);
    }
    data.strCall = strCall;
    
    String addr = body.substring(timeDateMatch.end()).trim();
    Parser p = new Parser(addr);
    if (p.get(' ').equalsIgnoreCase("BOX")) {
      data.strBox = p.get(' ');
      addr = p.get();
    }
    
    // See if there is an ID field, this is an older and probably obsolete format, but we still support it
    String info = null;
    Matcher idMatch = ID_PTN.matcher(addr);
    if (idMatch.find()) {
      data.strCallId = idMatch.group();
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, addr.substring(0,idMatch.start()).trim(), data);
      info = addr.substring(idMatch.end()).trim();
      
      // What is left is either a place name or supplemental info.  
      // And there is no consistent way to separate them
      int brk = 0;
      Matcher match = INFO_BREAK_PTN1.matcher(info);
      if (match.find()) {
        brk = match.start(1);
        if (brk > 35) brk = 0;
      }
      data.strPlace = info.substring(0,brk).trim();
      info = info.substring(brk);
      
    } else {
      
      // Current format, looks like we can count on a double separating the
      // address from general info
      Matcher match = INFO_BREAK_PTN2.matcher(addr);
      if (match.find()) {
        info = addr.substring(match.end()).trim();
        addr = addr.substring(0,match.start()).trim();
      }
      
      match = UNIT_END_PTN.matcher(addr);
      if (match.find()) {
        data.strUnit = append(data.strUnit, " ", match.group().trim());
        addr = addr.substring(0,match.start()).trim();
      }
      
      parseAddress(StartType.START_ADDR, addr, data);
      String left = getLeft();
      
      // We pretty much always expect to find a place name.  If we didn't, see
      // if we can parse it from the front of the leftover stuff
      if (data.strCity.length() == 0) {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, left, data);
        left = getLeft();
      }
      data.strPlace = left;
    }
    
    if (info != null) {
      Matcher match = UNIT_PTN.matcher(info);
      if (match.lookingAt()) {
        data.strUnit = append(data.strUnit, " ", match.group().replace(',', ' ').trim().replaceAll("  +", " "));
        info = info.substring(match.end()).trim();
      }
      DispatchProQAParser.parseProQAData(true, info, data);
    }
//    
//    // Special case
//    if (data.strPlace.equals("MARYS") && data.strAddress.endsWith(" ST")) {
//      data.strPlace = "ST MARYS";
//      data.strAddress = data.strAddress.substring(0, data.strAddress.length()-3).trim();
//    }
    return true;
  }
  
  private static final String[] CITY_LIST = new String[] {
    "CHESAPEAKE BEACH", 
    "NORTH BEACH", 
    "DUNKIRK", 
    "HUNTINGTOWN", 
    "LUSBY",
    "OWINGS", 
    "PRINCE FREDERICK", 
    "SAINT LEONARD",
    "ST LEONARD", 
    "SOLOMONS",
    "BARSTOW", 
    "BROOMES ISLAND", 
    "DARES BEACH", 
    "DOWELL", 
    "LOWER MARLBORO", 
    "PORT REPUBLIC", 
    "SUNDERLAND",
    
    "ANNE ARUNDEL",
    "CHARLES",
    "DORCHESTER",
    "PRINCE GEORGES",
    "ST MARYS",
    "TALBOT"
  };
}
