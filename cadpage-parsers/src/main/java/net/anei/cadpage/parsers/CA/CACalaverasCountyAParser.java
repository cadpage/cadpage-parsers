package net.anei.cadpage.parsers.CA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


/**
 * Calaveras County, CA
 */
public class CACalaverasCountyAParser extends MsgParser {
  
  private static final Pattern MASTER = 
      Pattern.compile("Inc# (\\d+):([^:]+):(?:([^@]+)@)?(.+?) *, *([A-Z_]+) *(?:\\(([^]]*?)\\) *)?:Map +([^:]*):(?: :)? LAT/LONG (X: [-+]?\\d+ \\d+\\.\\d+ +Y: [-+]?\\d+ \\d+\\.\\d+): ([^:]*)(?::([^:]*))?(?::.*)?");
  
  public CACalaverasCountyAParser() {
    this("CALAVERAS COUNTY");
  }
  
  protected CACalaverasCountyAParser(String defCity) {
    super(defCity, "CA");
    setFieldList("ID CALL PLACE ADDR APT CITY MAP GPS INFO UNIT");
  }
  
  @Override
  public String getFilter() {
    return "tcucad@FIRE.CA.GOV";
  }
  
  @Override
  public String getAliasCode() {
    return "CACalaverasCountyA";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strCall = match.group(2);
    data.strPlace = getOptGroup(match.group(3));
    parseAddress(match.group(4).trim(), data);
    data.strCity = match.group(5).replace('_', ' ').trim();
    data.strPlace = append(data.strPlace, " - ", getOptGroup(match.group(6)));
    data.strMap = match.group(7).trim();
    setGPSLoc(match.group(8), data);
    data.strSupp = match.group(9).trim();
    data.strUnit = getOptGroup(match.group(10));
    
    return true;
  }
}
