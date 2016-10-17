package net.anei.cadpage.parsers.PA;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA4Parser;



public class PAMifflinCountyParser extends DispatchA4Parser {
  
  public PAMifflinCountyParser() {
    super("MIFFLIN COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "cmessages@co.mifflin.pa.us,cadmessages@co.mifflin.pa.us";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (! super.parseMsg(subject, body, data)) return false;
    if (TOWNSHIPS.contains(data.strCity)) data.strCity += " TWP"; 
    return true;
  }

  private static final MatchList TOWNSHIPS = new MatchList(new String[]{
      "ARMAGH", "BRATTON", "BROWN", "DECATUR", "DERRY", "GRANVILLE", "MENNO", "OLIVER", "UNION", "WAYNE"
  });
}
