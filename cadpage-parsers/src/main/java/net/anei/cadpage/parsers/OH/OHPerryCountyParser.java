package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OHPerryCountyParser extends SmartAddressParser {

  public OHPerryCountyParser() {
    super(CITY_LIST, "PERRY COUNTY", "OH");
    setFieldList("DATE TIME ID CODE CALL UNIT ADDR APT CITY X");
  }

  @Override
  public String getFilter() {
    return "alert@dispatch.perrycountyohio.gov";
  }

  private static final Pattern MASTER = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d:\\d\\d:\\d\\d)\n(\\d\\d-\\d{6}):([^:]*?):(.*?)\n(?:([A-Z0-9]+) *\n)?@ (.*?) BETW (.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("CAD Page ")) return false;
    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    data.strCallId = match.group(3);
    data.strCode = match.group(4).trim();
    data.strCall = match.group(5).trim();
    data.strUnit = getOptGroup(match.group(6));
    parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(7).trim(), data);
    data.strCross = stripFieldEnd(match.group(8).trim(), "&");
    return true;
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = TWP_RD_PTN.matcher(addr).replaceAll("TOWNSHIP HWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern TWP_RD_PTN = Pattern.compile("\\b(?:MONDAY CREEK|SALT LICK|[A-Z]+) TWP RD\\b");

  private static final String[] CITY_LIST = new String[] {

      // Villages
      "CORNING",
      "CROOKSVILLE",
      "GLENFORD",
      "HEMLOCK",
      "JUNCTION CITY",
      "NEW LEXINGTON",
      "NEW STRAITSVILLE",
      "RENDVILLE",
      "ROSEVILLE",
      "SHAWNEE",
      "SOMERSET",
      "THORNVILLE",

      // Townships
      "BEARFIELD",
      "CLAYTON",
      "COAL",
      "HARRISON",
      "HOPEWELL",
      "JACKSON",
      "MADISON",
      "MONDAY CREEK",
      "MONROE",
      "PIKE",
      "PLEASANT",
      "READING",
      "SALT LICK",
      "THORN",

      // Census-designated places
      "ROSE FARM",
      "THORNPORT",

      // Unincorporated communities
      "BRISTOL",
      "BUCKINGHAM",
      "CHALFANTS",
      "CHAPEL HILL",
      "CLARKSVILLE",
      "CROSSENVILLE",
      "MCCUNEVILLE",
      "MCLUNEY",
      "MILLERTOWN",
      "MILLIGAN",
      "MOUNT PERRY",
      "MOXAHALA",
      "NEW READING",
      "OAKFIELD",
      "PORTERSVILLE",
      "REHOBOTH",
      "SALTILLO",
      "SEGO",
      "SULPHUR SPRINGS",

      // Ghost town
      "DICKSONTON",
      "SAN TOY",

      // Muskingum County
      "ZANESVILLE"
  };
}