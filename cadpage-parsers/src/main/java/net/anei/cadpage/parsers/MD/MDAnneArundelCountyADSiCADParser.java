package net.anei.cadpage.parsers.MD;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Anne Arundel County, MD (ADSiCAD)
*/

public class MDAnneArundelCountyADSiCADParser extends DispatchA49Parser {

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("Remarks F>>IC<", "Remarks:\nF>>IC<");
    return super.parseMsg(body, data);
  }

  public MDAnneArundelCountyADSiCADParser() {
    super("ANNE ARUNDEL COUNTY","MD");
  }
  
  @Override
  public String getFilter() {
    return "cad@e9.com,alerts@e9.com";
  }
}
