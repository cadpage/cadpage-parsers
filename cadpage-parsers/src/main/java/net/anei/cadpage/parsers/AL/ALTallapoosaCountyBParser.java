package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALTallapoosaCountyBParser extends FieldProgramParser {
  
  public ALTallapoosaCountyBParser() {
    super(CITY_LIST, "TALLAPOOSA COUNTY", "AL", 
          "CALL CALL_EXT/CS? ADDR/S6! CITY? INFO/N+");
  }
  
  private static final Pattern DELIM = Pattern.compile("[;,]");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(DELIM.split(body), data)) return false;
    return !data.strCity.isEmpty() || checkAddress(data.strAddress) >= STATUS_INTERSECTION;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL_EXT"))  return new CallField("LIFT ASSIST", true);
    return super.getField(name);
  }
  
  private static final String[] CITY_LIST = new String[] {
      
      // Cities
      "ALEXANDER CITY",
      "DADEVILLE",
      "TALLASSEE",

      // Towns
      "CAMP HILL",
      "DAVISTON",
      "GOLDVILLE",
      "JACKSONS GAP",
      "NEW SITE",

      // Census-designated places
      "HACKNEYVILLE",
      "OUR TOWN",
      "REELTOWN",
      
      // Unincorporated communities
      "ANDREW JACKSON",
      "BULGERS",
      "CHEROKEE BLUFFS",
      "CHURCH HILL",
      "DUDLEYVILLE",
      "FOSHEETON",
      "FROG EYE"
  };
}
