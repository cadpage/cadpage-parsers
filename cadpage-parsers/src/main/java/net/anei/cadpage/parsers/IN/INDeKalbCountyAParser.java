package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA68Parser;

public class INDeKalbCountyAParser extends DispatchA68Parser {

  public INDeKalbCountyAParser() {
    super("DEKALB COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "bhunter@co.dekalb.in.us";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strCity = data.strCity.replace(".", "");
    return true;
  }


}
