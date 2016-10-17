package net.anei.cadpage.parsers.MI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

/**
 * Jackson County, MI
 */
public class MIJacksonCountyParser extends SmartAddressParser {
  
  public MIJacksonCountyParser() {
    super(CITY_LIST, "JACKSON COUNTY", "MI");
    setFieldList("UNIT ID CALL DATE TIME ADDR APT CITY PLACE INFO");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@Co.Jackson.MI.US";
  }
  
  private static final Pattern DIR_MBLANK_PTN = Pattern.compile(" ([NSEW])  ");
  private static final Pattern MASTER = Pattern.compile("((?:[A-Z]+\\d+ )+) (\\d{4}-\\d{8}) ([-/<> A-Za-z0-9]+) (\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) (.*?)(?:  (.*))?");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("(.*?) *\\bE911 Info - .*?(?:Confidence:[ 0-9]*|$)(.*)");
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    body = DIR_MBLANK_PTN.matcher(body).replaceAll(" $1 ");
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strUnit = match.group(1).trim();
    data.strCallId = match.group(2);
    data.strCall = match.group(3).trim();
    data.strDate = match.group(4);
    data.strTime = match.group(5);
    parseAddress(StartType.START_ADDR, FLAG_IMPLIED_INTERSECT | FLAG_CROSS_FOLLOWS, match.group(6).trim(), data);
    data.strPlace = getLeft();
    String info = getOptGroup(match.group(7));
    match = INFO_JUNK_PTN.matcher(info);
    if (match.matches()) {
      info = append(match.group(1), " / ", match.group(2));
    }
    data.strSupp = info;
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "JACKSON",

    // Villages
    "BROOKLYN",
    "CEMENT CITY",
    "CONCORD",
    "GRASS LAKE",
    "HANOVER",
    "PARMA",
    "SPRINGPORT",

    // Census-designated places
    "MICHIGAN CENTER",
    "NAPOLEON",
    "SPRING ARBOR",
    "VANDERCOOK LAKE",

    // Unincorporated communities
    "CLARK LAKE",
    "HORTON",
    "LEONI",
    "LIBERTY",
    "MUNITH",
    "NORVELL",
    "PLEASANT LAKE",
    "PULASKI",
    "RIVES JUNCTION",
    "SANDSTONE",
    "TOMPKINS",
    "WATERLOO",

    // Townships
    "BLACKMAN TWP",
    "COLUMBIA TWP",
    "CONCORD TWP",
    "GRASS LAKE TWP",
    "HANOVER TWP",
    "HENRIETTA TWP",
    "LEONI TWP",
    "LIBERTY TWP",
    "NAPOLEON TWP",
    "NORVELL TWP",
    "PARMA TWP",
    "PULASKI TWP",
    "RIVES TWP",
    "SANDSTONE TWP",
    "SPRING ARBOR TWP",
    "SPRINGPORT TWP",
    "SUMMIT TWP",
    "TOMPKINS TWP",
    "WATERLOO TWP",
    
    // Calhoun County
    "SHERIDAN TWP"
  };
}
