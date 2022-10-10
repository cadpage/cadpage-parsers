package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class VAGilesCountyParser extends DispatchSouthernParser {

  public VAGilesCountyParser() {
    super(CITY_LIST, "GILES COUNTY", "VA",
          DSFLG_PROC_EMPTY_FLDS | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_NAME | DSFLG_PHONE | DSFLG_CODE | DSFLG_UNIT1 | DSFLG_ID | DSFLG_TIME);
  }

  private static final Pattern LEAD_PRI_PTN = Pattern.compile("(\\d);");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = LEAD_PRI_PTN.matcher(body);
    if (!match.lookingAt()) return false;
    data.strPriority = match.group(1);
    body = body.substring(match.end()).trim();
    if (!super.parseMsg(body, data)) return false;

    data.strCode = stripFieldEnd(data.strCall, "-");
    data.strCall = data.strSupp;
    data.strSupp = "";
    int pt = data.strCall.indexOf('\n');
    if (pt >= 0) {
      data.strSupp = data.strCall.substring(pt+1).trim();
      data.strCall = data.strCall.substring(0,pt).trim();
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "PRI " + super.getProgram();
  }

  private static final String[] CITY_LIST = new String[]{

      // Towns
      "GLEN LYN",
      "NARROWS",
      "PEARISBURG",
      "PEMBROKE",
      "RICH CREEK",

      // Unincorporated communities
      "EGGLESTON",
      "GOLDBOND",
      "HOGES CHAPEL",
      "KIMBALLTON",
      "MAYBROOK",
      "NEWPORT",
      "PROSPECTDALE",
      "RIPPLEMEAD",
      "STAFFORDSVILLE",
      "TRIGG",
      "WHITE GATE"
  };

}
