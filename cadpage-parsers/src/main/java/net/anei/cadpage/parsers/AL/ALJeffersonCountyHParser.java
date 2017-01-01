package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALJeffersonCountyHParser extends DispatchSouthernParser {
    
  public ALJeffersonCountyHParser() {
    super(ALJeffersonCountyParser.CITY_LIST, "JEFFERSON COUNTY", "AL",
          DSFLG_ADDR|DSFLG_ADDR_NO_IMPLIED_APT|DSFLG_ID|DSFLG_TIME);
  }
  
  private static final Pattern OCA_PTN = Pattern.compile("\\bOCA: *[\\d-]+$");
  private static final Pattern MM_PTN = Pattern.compile(" MM, *");
  private static final Pattern UNIT_CALL_PTN = Pattern.compile("(\\d\\d) +(.*)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = OCA_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();
    body = MM_PTN.matcher(body).replaceAll(" MM ");
    if (!super.parseMsg(body, data)) return false;
    match = UNIT_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strUnit = match.group(1);
      data.strCall = match.group(2);
    }
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "UNIT CALL");
  }
}
