package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class FLOkaloosaCountyParser extends DispatchA3Parser {
  
  public FLOkaloosaCountyParser() {
    super(2, "Okaloosa:", "OKALOOSA COUNTY", "FL");
  }
  
  @Override
  public String getFilter() {
    return "Okaloosa@co.okaloosa.fl.us";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strMap = data.strSupp;
    data.strSupp = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("INFO", "MAP");
  }
}
