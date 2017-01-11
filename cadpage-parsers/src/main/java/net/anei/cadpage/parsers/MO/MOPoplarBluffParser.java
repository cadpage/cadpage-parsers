package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOPoplarBluffParser extends DispatchA33Parser {
    
  public MOPoplarBluffParser() {
    super("POPLAR BLUFF", "MO", A33_FIX_LINE_BREAKS);
  }
  
  @Override
  public String getFilter() {
    return "INFOR@PBPOLICE.ORG,INFO@PBPOLICE.ORG";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    if (data.strCross.equals("MO")) data.strCross = "";
    else if (data.strCross.endsWith(", MO")) {
      data.strCity = append(data.strCity, " ", data.strCross.substring(0, data.strCross.length()-4).trim());
      data.strCross = "";
    }
    return true;
  }
  

}
