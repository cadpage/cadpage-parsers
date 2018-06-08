package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class TNSumnerCountyBParser extends SmartAddressParser {
  
  public TNSumnerCountyBParser() {
    super(CITY_LIST, "SUMNER COUNTY", "TN");
    setFieldList("ADDR CITY CALL");
  }
  
  @Override
  public String getFilter() {
    return "2083399423";
  }
  
  private static final Pattern MASTER = Pattern.compile("Sumner County ECC: CALL ALERT: (.*?) CALL TYPE:? (.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(1).trim(), data);
    data.strCall = match.group(2).trim();
    return true;
  }
  
  private static final Pattern PVT_PTN = Pattern.compile(" *\\bPVT\\b *", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = PVT_PTN.matcher(addr).replaceAll(" ").trim();
    return super.adjustMapAddress(addr);
  }
  
  private static final String[] CITY_LIST = new String[]{

      // Cities
      "GALLATIN",
      "GOODLETTSVILLE",
      "HENDERSONVILLE",
      "MILLERSVILLE",
      "MITCHELLVILLE",
      "PORTLAND",
      "WHITE HOUSE",

      // Town
      "WESTMORELAND",

      // Census-designated places
      "BETHPAGE",
      "BRANSFORD",
      "CASTALIAN SPRINGS",
      "COTTONTOWN",
      "FAIRFIELD",
      "GRABALL",
      "NEW DEAL",
      "OAK GROVE",
      "SHACKLE ISLAND",
      "WALNUT GROVE",

      // Unincorporated communities
      "BON AIR",
      "BRACKENTOWN",
      "CAIRO",
      "CORINTH"
      
  };
}
