

package net.anei.cadpage.parsers.NC;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

/**
 * Chattam County, NC
 */
public class NCCaswellCountyParser extends DispatchSouthernParser {

  public NCCaswellCountyParser() {
    super(CITY_LIST, "CASWELL COUNTY", "NC",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_OPT_DISP_ID | DSFLG_ADDR |DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_X | DSFLG_OPT_CODE | DSFLG_ID | DSFLG_TIME );
  }

  @Override
  public String getFilter() {
    return "@caswellcountync.gov,caswell911@gmail.com";
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("\\d+-Call Report OCA [-0-9]+ \\| Call (\\d+) *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Run reports are attached word documents that we can not yet read
    // but we can collect some information from the subject
    Matcher match = RUN_REPORT_PTN.matcher(subject);
    if (match.matches()) {
      setFieldList("ID ADDR APT CITY CALL");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      String callAddr = match.group(2).trim();
      parseAddress(StartType.START_ADDR, callAddr, data);
      data.strCall = getLeft();
    } else {
      if (!super.parseMsg(body, data)) return false;
    }
    if (VA_CITY_SET.contains(data.strCity.toUpperCase())) data.strState = "VA";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = EXT_PTN.matcher(addr).replaceAll("EXD");
    addr = addr.replace("GATEWOOD RD EXD", "GATEWOOD EXD");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern EXT_PTN = Pattern.compile("\\bEXT\\b", Pattern.CASE_INSENSITIVE);

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "MILTON",
    "YANCEYVILLE",

    // Townships
    "ANDERSON TWP",
    "HIGHTOWERS TWP",
    "LEASBURG TWP",
    "LOCUST HILL TWP",
    "PROVIDENCE TWP",
    "MILTON TWP",
    "PELHAM TWP",
    "STONEY CREEK TWP",
    "YANCEYVILLE TWP",

    // Unincorporated
    "CASVILLE",
    "LEASBURG",
    "PELHAM",
    "PROSPECT HILL",
    "PROVIDENCE",
    "PURLEY",
    "SEMORA",
    "CHERRY GROVE",
    "BLANCH",

    // Almace County
    "ELON",

    // Rockingham County
    "REIDSVILLE",

    // Independent cities, Virginia
    "DANVILLE"
  };

  private static final Set<String> VA_CITY_SET = new HashSet<String>(Arrays.asList(new String[]{
      "DANVILLE"
  }));
}
