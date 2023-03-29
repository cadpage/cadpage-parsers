package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA25Parser;

public class MOCallawayCountyParser extends DispatchA25Parser {

  public MOCallawayCountyParser() {
    super("CALLAWAY COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "EnterpolAlerts@cceoc.org";
  }

  @Override
  public String adjustMapAddress(String addr) {
    String tmp = GPS_LOOKUP_TABLE.getProperty(addr.toUpperCase());
    if (tmp != null) return tmp;

    addr = BUS_54_PTN.matcher(addr).replaceAll("US 54 BUS");
    addr = addr.replace("SR AE", "MO-AE");
    return addr;
  }
  private static final Pattern BUS_54_PTN = Pattern.compile("\\bBUS 54\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[] {
      "6557 MEADOW VIEW CT",                  "+38.822870,-92.004890",
      "6585 MEADOW VIEW CT",                  "+38.823050,-92.004830",
      "6588 MEADOW VIEW CT",                  "+38.822830,-92.004530",
      "6590 MEADOW VIEW CT",                  "+38.822670,-92.004370",
      "6593 MEADOW VIEW CT",                  "+38.822072,-92.004162",
      "6596 MEADOW VIEW CT",                  "+38.822220,-92.004870",
      "1510 N BUS 54",                        "1510 NORTH BLUFF ST",
      "2000 N BUS 54",                        "2000 N BLUFF ST"
  });
}

