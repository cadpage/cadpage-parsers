package net.anei.cadpage.parsers.GA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class GAPauldingCountyParser extends SmartAddressParser {

  public GAPauldingCountyParser() {
    super("PAULDING COUNTY", "GA");
    setFieldList("CODE CALL ADDR PHONE X NAME");
    setupMultiWordStreets(MW_STREETS);
    setupSpecialStreets("COBB COUNTY LINE");
  }
  
  private static Pattern HEAD_PHONE_TAIL = Pattern.compile("(.+?) (?:(\\d{10})|770) (.+)");
  private static Pattern NUMERIC_HWY = Pattern.compile("/(\\d+[NS]?[EW]?)(?: +(.+))?");
  
  @Override
  public boolean parseMsg(String body, Data data) {
    //check + remove leading text
    if (!body.startsWith("PAULDINGCOUNTY911:")) return false;
    body = body.substring(18).trim();
    
    //try to split by phone #
    Matcher mat = HEAD_PHONE_TAIL.matcher(body);
    String tail;
    if (mat.matches()) {
      if (parseCALL_ADDR(mat.group(1), data, FLAG_ANCHOR_END) == null) return false;
      data.strPhone = getOptGroup(mat.group(2));
      if (data.strPhone.equals("0000000000")) data.strPhone = "";
      tail = mat.group(3);
    } else {
      //if no PHONE to split by we have to parse from whole body
      tail = parseCALL_ADDR(body, data, FLAG_CROSS_FOLLOWS);
      if (tail == null) return false;
    }
    
    if (tail.length() == 0) return true;
    
    //parse X and NAME from tail
    tail = stripFieldStart(tail, "XS:");
    Result res = parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_ONLY_CROSS, tail);
    if (res.getStatus() >= STATUS_STREET_NAME) { 
      res.getData(data); 
      data.strName = res.getLeft();
      //sometimes hwys unrecognizable by SAP make it into NAME, try parsing them
      data.strName = res.getLeft();
      mat = NUMERIC_HWY.matcher(data.strName);
      if (mat.matches()) {
        data.strCross = append(data.strCross, " / ", "HWY " + mat.group(1));
        data.strName = getOptGroup(mat.group(2));
      }
    }
    else data.strName = tail;
    
    return true;
  }

  private static Pattern HWY_NOTATION = Pattern.compile("H(\\d+)-(\\d+(?= |$))?");
  private static Pattern CALL_ADDR = Pattern.compile("(.*?) - (.*)");
  
  /** Try a few approaches to parse CALL and ADDR from text
   * @param text
   * @param data
   * @param flags
   * @return what's left (null if parseMsg() should return false)
   */
  private String parseCALL_ADDR(String text, Data data, int flags) {
    //convert things like H16-45 to HWY 16 & HWY 45
    Matcher mat = HWY_NOTATION.matcher(text);
    if (mat.find()) {
      StringBuffer sb = new StringBuffer();
      do {
        //re-insert groups with proper HWY formatting
        String rep = "HWY "+mat.group(1) + " & ";
        String g2 = mat.group(2);
        if (g2 != null) rep += "HWY "+mat.group(2);
        mat.appendReplacement(sb, rep);
      } while (mat.find());
      mat.appendTail(sb);
      text = sb.toString();
    }
    
    //check if text starts with a known CODE and it's respective CALL
    int si = text.indexOf(" ");
    String possCode = text.substring(0, si);
    if (CALL_CODES.containsKey(possCode)) {
      String possCall = CALL_CODES.getProperty(possCode);
      //make sure the call is actually in the string
      if (text.substring(si+1).startsWith(possCall)) {
        data.strCode = possCode;
        data.strCall = possCall;
        parseAddress(StartType.START_ADDR, flags, text.substring(si+1+data.strCall.length()).trim(), data);
        return getLeft();
      }
    }
    
    //if no known CODE then it might be a bare CALL, check that with a CodeSet
    String knownCall = CALL_LIST.getCode(text);
    if (knownCall != null) {
      data.strCall = knownCall;
      parseAddress(StartType.START_ADDR, flags, text.substring(knownCall.length()).trim(), data);
      return getLeft();
    } 

    //try pattern match
    mat = CALL_ADDR.matcher(text);
    if (mat.matches()) {
      data.strCall = mat.group(1);
      parseAddress(StartType.START_ADDR, flags, mat.group(2), data);
      return getLeft();
    }

    //try smart address parser
    parseAddress(StartType.START_CALL, flags, text, data);
    if (getStatus() < STATUS_FULL_ADDRESS) return null;
    return getLeft();
  }
  
  @Override
  public CodeSet getCallList() {
    return CALL_LIST;
  }
  
  private static Properties CALL_CODES = buildCodeTable(new String[]{
      "911HAN", "911-HANG UP - 10-93",
      "ACCI",   "ACCIDENT....NO INJURIES",
      "ACCPED", "ACCIDENT...PEDESTRIAN",
      "ACENVE", "ACCIDENT...ENTRAPMENT, VEHICLE",
      "ACINVE", "ACCIDENT....INJURY",
      "ACINVE", "ACCIDENT....INJURY, VEHICLE",
      "ALABUR", "ALARM,BURGLARY-INTRUSION -1090",
      "ALAPAN", "ALARM, PANIC/DURESS - 1090",
      "ANICAS", "ANIMAL CASE",
      "BURGAO", "BURGLARY...AO....(10-15)",
      "BURGIP", "BURGLARY...IP/JO......(10-15)",
      "DOMDIS", "DOMESTIC DISPUTE [VER/PHY]",
      "FIGHIP", "FIGHT.....I/P....(10-10)",
      "FIRBRU", "FIRE, BRUSH",
      "MED3AC", "ANIMAL BITE....NO RES/EMS",
      "MISPER", "MISSING PERSON (18YO & UP)",
  });
  
  //init CALL_LIST from CALL_CODES.values()
  private static CodeSet CALL_LIST;
  static {
    Object[] objs = CALL_CODES.values().toArray();
    String[] calls = new String[objs.length];
    for (int i = 0; i < calls.length; i++) calls[i] = (String)objs[i];
    CALL_LIST = new CodeSet(calls);
  }
  
  private static String[] MW_STREETS = new String[]{
    "AUSTIN BRIDGE",
    "BAKERS BRIDGE",
    "BENTLEIGH STATION",
    "BETHEL CHURCH",
    "BILL CARRUTH",
    "BOB GROGAN",
    "CAMPGROUND SCHOOL",
    "COHRAN STORE",
    "COLE LAKE",
    "CREST HOLLOW",
    "DABBS BRIDGE",
    "DALLAS ACWORTH",
    "DALLAS NEBO",
    "DARBYS RUN",
    "DUE WEST",
    "EAGLE ROCK",
    "EAST MEMORIAL",
    "ESTATES VIEW",
    "GOLF LINKS",
    "HIRAM ACWORTH",
    "HIRAM DOUGLASVILLE",
    "HIRAM SUDIE",
    "JEWELL COLE",
    "JIMMY CAMPBELL",
    "JIMMY LEE SMITH",
    "LAKE SWAN",
    "LEGACY POINTE",
    "MANORS MILL",
    "MORGAN LAKE",
    "MULBERRY ROCK",
    "NEW VINSON MOUNTAIN",
    "OLD CARTERSVILLE",
    "PAUL RITCH",
    "PINE SHADOWS",
    "RC THOMPSON",
    "SEVEN HILLS",
    "VILLA RICA",
    "VINSON MOUNTAIN",
    "WATER WAY",
    "WENDY BAGWELL",
    "WHEELER LAKE",
    "WHITWORTH CHURCH",
  };

}
