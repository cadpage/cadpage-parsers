package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCLenoirCountyAParser extends SmartAddressParser {
  
  static final Pattern MASTER = Pattern.compile("[A-Za-z ]+(\\d+) +- +(.*)");
  
  public NCLenoirCountyAParser() {
    super(CITY_LIST, "LENOIR COUNTY", "NC");
    setFieldList("CODE CALL ADDR APT CITY X");
    setupMultiWordStreets("GRIFTON HUGO");
  }
  
  @Override
  public String getFilter() {
    return "3364058803";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    
    data.strCode = match.group(1);
    Parser p = new Parser(match.group(2));
    String addr = p.get(" X:");
    String cross = p.get();
    
    parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, addr, data);
    if (cross.length() > 0) {
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS | FLAG_IMPLIED_INTERSECT | FLAG_ANCHOR_END, cross, data);
    }
    return true;
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities and towns
    "GRIFTON",
    "NORTH GRIFTON",
    "KINSTON",
    "NORTH KINSTON",
    "LA GRANGE",
    "PINK HILL",
    "DEEP RUN",
    
    // Townships
    "CONTENTNEA NECK TWP",
    "FALLING CREEK TWP",
    "INSTITUTE TWP",
    "KINSTON TWP",
    "MOSELEY HALL TWP",
    "NEUSE TWP",
    "PINK HILL TWP",
    "SAND HILL TWP",
    "SOUTHWEST TWP",
    "TRENT TWP",
    "VANCE TWP",
    "WOODINGTON TWP"
  };
}
