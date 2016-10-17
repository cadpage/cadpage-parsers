package net.anei.cadpage.parsers.NC;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA3Parser;


public class NCCatawbaCountyParser extends DispatchA3Parser {
  
  private static final Pattern C_AND_B = Pattern.compile("\\bC AND B\\b|\\bC *& *B\\b", Pattern.CASE_INSENSITIVE);
  
  public NCCatawbaCountyParser() {
    super(-2, "CATAWBA COUNTY", "NC");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    if (body.length() == 0) return false;
    body = body.replaceAll("//+", "/");
    body = C_AND_B.matcher(body).replaceAll("C%B");
    if (!super.parseMsg(body, data)) return false;
    data.strAddress = data.strAddress.replace("C%B", "C AND B");
    data.strCross = data.strCross.replace("C%B", "C AND B");
    
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    return C_AND_B.matcher(addr).replaceAll("C%B");
  }

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return sAddress.replace("C%B", "C AND B");
  }
  
  
}
