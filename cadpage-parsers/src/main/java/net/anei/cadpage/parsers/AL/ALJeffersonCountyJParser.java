package net.anei.cadpage.parsers.AL;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class ALJeffersonCountyJParser extends DispatchSouthernParser {
    
  public ALJeffersonCountyJParser() {
    super(ALJeffersonCountyParser.CITY_LIST, "JEFFERSON COUNTY", "AL",
          DSFLG_ADDR | DSFLG_OPT_BAD_PLACE | DSFLG_ID | DSFLG_TIME); 
  }
  
  private static final Pattern ID_PTN = Pattern.compile("\\d{4}-\\d{2}-\\d{5}");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (! super.parseMsg(body, data)) return false;
    return ID_PTN.matcher(data.strCallId).matches();
  }
}
