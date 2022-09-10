
package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class NCWashingtonCountyParser extends DispatchSouthernParser {
  
  public NCWashingtonCountyParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "NC", 
          DSFLG_ADDR | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME | DSFLG_PROC_EMPTY_FLDS);
    addExtendedDirections();
  }

  @Override
  public String getFilter() {
    return "@washconc.org";
  }
  
  private static final Pattern SECTOR_PTN = Pattern.compile("- [NSEW] SECTOR");
  
  @Override
  protected boolean isNotExtraApt(String apt) {
    if (apt.startsWith("BOUND")) return true;
    if (SECTOR_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }
  
  @Override 
  public String adjustMapAddress(String addr) {
    Matcher match = SECTOR_PTN.matcher(addr);
    if (match.find()) addr = addr.substring(0,match.start()).trim();
    return super.adjustMapAddress(addr);
  }
  
  private static final String[] CITY_LIST = new String[]{
      "CRESWELL",
      "LEES MILL",
      "PEA RIDGE",
      "PLYMOUTH",
      "ROPER",
      "SCUPPERNONG",
      "SKINNERSVILLE",
      
      // Tyrell County
      "TYRELL CO",
      "COLUMBIA"

  };
}
