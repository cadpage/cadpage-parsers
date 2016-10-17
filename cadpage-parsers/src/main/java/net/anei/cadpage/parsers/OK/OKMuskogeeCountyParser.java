package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OKMuskogeeCountyParser extends SmartAddressParser {

  public OKMuskogeeCountyParser() {
    super(CITY_LIST, "MUSKOGEE COUNTY", "OK");
    setFieldList("UNIT MAP ADDR APT CITY PLACE CALL ID DATE TIME INFO");
  }
  
  private static Pattern MASTER = Pattern.compile("(?:((?:[A-Z]\\d+ +)+(?:Alarm +)?)(?:Territory (\\d[A-Z]) +)?)?(.*)(\\d{4}-\\d{8})([^\n]+)?(?:\n *(.*))?", Pattern.DOTALL);
  private static Pattern DISCARD = Pattern.compile("  (?:Call Number \\d+ was created from Call Number \\d+\\(([^ ]+ \\d{1,2} \\d{4}) +(\\d{1,2}:\\d{1,2}[AP]M)\\)|Dispatch received by unit \\d+)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    if (!subject.equals("Incident")) return false;
    
    Matcher mat = MASTER.matcher(body);
    if (!mat.matches()) return false;
    
    data.strUnit = getOptGroup(mat.group(1));
    data.strMap = getOptGroup(mat.group(2));
    
    // ADDR APT CITY CALL
    parseAddress(StartType.START_ADDR, FLAG_ALLOW_DUAL_DIRECTIONS | FLAG_IMPLIED_INTERSECT | FLAG_PAD_FIELD, mat.group(3).trim(), data);
    data.strCall = getLeft();
    
    // abort if no CITY or CALL
    if (data.strCall.length() == 0) return false;
    
    // PLACE ID
    data.strPlace = getPadField();
    data.strCallId = mat.group(4);
    
    // parse 
    String firstInfoLine = mat.group(5);
    if (firstInfoLine != null) {
      data.strSupp = DISCARD.matcher(firstInfoLine).replaceAll("").trim();
    } 
    
    // append the rest of the page to INFO
    data.strSupp = append(data.strSupp, "\n", getOptGroup(mat.group(6)));
    
    return true;
  }

  private static String[] CITY_LIST = new String[]{
      "MUSKOGEE COUNTY",
      "MUSKOGEE",
      "BOYNTON",
      "BRAGGS",
      "COUNCIL HILL",
      "FORT GIBSON",
      "HASKELL",
      "OKTAHA",
      "PORUM",
      "TAFT",
      "WAINWRIGHT",
      "WARNER",
      "WEBBERS FALLS",
      "RIVER BOTTOM",
      "SAND HILLS",
      "SIMMS",
      "SOUR JOHN",
      "SUMMIT",
      "PUMPKIN CENTER",
  };
  
}
