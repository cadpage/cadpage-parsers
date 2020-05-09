package net.anei.cadpage.parsers.FL;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;


public class FLCitrusCountyParser extends SmartAddressParser {
  
  private static final Pattern MARKER = Pattern.compile("CITRUS COUNTY FIRE DEPARTMENT:? +");
  private static final Pattern TRUNC_CITY_PTN = Pattern.compile("(?: [A-Z][a-z]+)+(?: [A-Z])?$");
  private static final Pattern MASTER1 = Pattern.compile("Unit:([A-Z0-9]+) Status:Dispatched ([A-Z0-9]+) - (.*?) (\\d{2}[A-Z]) (.*)");
  private static final Pattern MASTER2 = Pattern.compile("((?:[A-Z]+\\d+[A-Z]? )+) ([A-Z]?\\d{1,2}[A-Z]) (.*?) ([A-Z]?\\d{1,3}[A-Z]?\\d{0,2}|MEDICAL|>New<)(?:(?: - (.*?))?(?: +(\\d{4}-\\d+)(?: +(.*))?)?)?");
  private static final Pattern CITY_ABBRV_PTN = Pattern.compile("( [A-Z0-9]+) - [A-Z]{2,4} ?([A-Z][a-z])");
  private static final Pattern CITY_BRK_PTN = Pattern.compile("(.*? [A-Z]+)([A-Z][a-z].*)");

  
  public FLCitrusCountyParser() {
    super(CITY_LIST, "CITRUS COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "777,888,ddevoe@sheriffcitrus.org,CitrusCountyFireRescue@ccso.com,alerts@citruscountyfire.com,2183500403";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    do {
      Matcher match = MARKER.matcher(body);
      if (match.lookingAt()) {
        body = body.substring(match.end());
        break;
      }
      
      if (subject.equals("Message from HipLink")) break;
      if (subject.equals("Email Copy Message From Hiplink")) break;
      
      if (body.startsWith("Message from HipLink / ")) {
        body = body.substring(23).trim();
        break;
      }
      
    } while (false);
    
    int pt = body.indexOf('\n');
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    // There is a problem with truncated cities.  See if we can identify and restore a truncated
    // camel case city at the end of the text body
    Matcher match = TRUNC_CITY_PTN.matcher(body);
    if (match.find()) {
      String truncCity = match.group().trim();
      SortedSet<String> set = CITY_SET.tailSet(truncCity);
      if (!set.isEmpty()) {
        String city = set.first();
        if (city.startsWith(truncCity)) {
          body = body.substring(0,match.start()) + ' ' + city;
        }
      }
    }
    
    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT CODE CALL MAP ADDR APT X CITY INFO");
      data.strUnit = match.group(1);
      data.strCode = match.group(2);
      data.strCall = match.group(3);
      data.strMap = match.group(4);
      String sAddr = match.group(5).trim();
      parseAddress(StartType.START_ADDR, FLAG_PAD_FIELD | FLAG_CROSS_FOLLOWS, sAddr, data);
      data.strCross = getPadField();
      data.strSupp = getLeft();
      return true;
    }

    body =  CITY_ABBRV_PTN.matcher(body).replaceFirst("$1 $2");
    match = MASTER2.matcher(body);
    if (match.matches()) {
      setFieldList("UNIT MAP ADDR APT PLACE CITY CODE CALL ID INFO");
      data.strUnit = match.group(1).trim();
      data.strMap = getOptGroup(match.group(2));
      String sAddr = match.group(3).trim();
      data.strCode = match.group(4);
      data.strCall = getOptGroup(match.group(5));
      data.strCallId = getOptGroup(match.group(6));
      data.strSupp = getOptGroup(match.group(7));
      
      match = CITY_BRK_PTN.matcher(sAddr);
      if (match.matches()) {
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_NO_CITY, match.group(1).trim(), data);
        data.strPlace = getLeft();
        data.strCity = match.group(2);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_PAD_FIELD | FLAG_ANCHOR_END, sAddr, data);
        data.strPlace = getPadField();
      }
      return true;
    }
    return false;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("GULF TO LAKE", "GULF-TO-LAKE");
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Incorporated
    "CRYSTAL RIVER",
    "INVERNESS",
    
    // Unincorporated
    "BEVERLY HILLS",
    "BLACK DIAMOND",
    "CHASSAHOWITZKA",
    "CITRUS HILLS",
    "CITRUS SPRINGS",
    "FLORAL CITY",
    "HERNANDO",
    "HOLDER",
    "HOMOSASSA SPRINGS",
    "HOMOSASSA",
    "INVERNESS HIGHLANDS NORTH",
    "INVERNESS HIGHLANDS SOUTH",
    "LECANTO",
    "MEADOWCREST",
    "PINE RIDGE",
    "PINEOLA",
    "RED LEVEL",
    "SUGARMILL WOODS",
    
    // Levy County
    "INGLIS",
    
    // Marion County
    "DUNNELLON"
  }; 
  
  private static final TreeSet<String> CITY_SET = new TreeSet<String>();
  static {
    for (String city : CITY_LIST) CITY_SET.add(toCamelCase(city));
  }
  private static String toCamelCase(String city) {
    StringBuilder sb = new StringBuilder();
    boolean downshift = false;
    for (char chr : city.toCharArray()) {
      if (downshift) chr = Character.toLowerCase(chr);
      sb.append(chr);
      downshift = (chr != ' ');
    }
    return sb.toString();
  }
}
