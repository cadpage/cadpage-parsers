package net.anei.cadpage.parsers.ME;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class MEOxfordCountyParser extends MsgParser {
  
  private static final Pattern MASTER = Pattern.compile("(\\d+):(?:Spillman:)?([A-Z]+): ?- (.*?) - ([^,]+), ([A-Z]+)(?:: *(.*))?");
  
  public MEOxfordCountyParser() {
    super("OXFORD COUNTY", "ME");
    setFieldList("ID SRC CALL ADDR APT CITY INFO");
  }
  
  @Override
  public String getFilter() {
    return "2075151833,2075151834";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strSource = match.group(2);
    data.strCall = match.group(3).trim();
    parseAddress(match.group(4).trim(), data);
    data.strCity = convertCodes(match.group(5), CITY_CODES);
    data.strSupp = getOptGroup(match.group(6));
    return true;
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "MEX", "MEXICO",
      "RUM", "RUMFORD"
  });
 
//  private static final String[] CITY_LIST = new String[]{
//    "ANDOVER",
//    "BETHEL",
//    "BROWNFIELD",
//    "BUCKFIELD",
//    "BYRON",
//    "CANTON",
//    "DENMARK",
//    "DIXFIELD",
//    "FRYEBURG",
//    "GILEAD",
//    "GREENWOOD",
//    "HANOVER",
//    "HARTFORD",
//    "HEBRON",
//    "HIRAM",
//    "LINCOLN PLANTATION",
//    "LOVELL",
//    "MAGALLOWAY PLANTATION",
//    "MEXICO",
//    "NEWRY",
//    "NORWAY",
//    "OTISFIELD",
//    "OXFORD",
//    "PARIS",
//    "PERU",
//    "PORTER",
//    "ROXBURY",
//    "RUMFORD",
//    "STONEHAM",
//    "STOW",
//    "SUMNER",
//    "SWEDEN",
//    "UPTON",
//    "WATERFORD",
//    "WEST PARIS",
//    "WOODSTOCK",
//
//    "SOUTH OXFORD",
//    "NORTH OXFORD",
//    "MILTON"
//  };
}
