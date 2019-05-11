package net.anei.cadpage.parsers.TX;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA37Parser;

public class TXHidalgoCountyParser extends DispatchA37Parser {

  public TXHidalgoCountyParser() {
    super(null, "HIDALGO COUNTY", "TX");
   }
  
  @Override
  public String getFilter() {
    return "PHARRDispatch@yourdomain.com";
  }
  
  @Override
  protected boolean parseMessageField(String field, Data data) {
    data.strSupp = field;
    return true;
  }
  
}
