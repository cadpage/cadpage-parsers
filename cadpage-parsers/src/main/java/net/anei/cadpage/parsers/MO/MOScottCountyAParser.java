package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class MOScottCountyAParser extends DispatchA33Parser {


  public MOScottCountyAParser() {
    super("SCOTT COUNTY", "MO");
  }

  @Override
  public String getFilter() {
    return "SCOTTCITY@PUBLICSAFETYSOFTWARE.NET,SCOTTCITY@itiusa.com,@OMNIGO.COM";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // They do weird things with the cross street information :(
    data.strCross = stripFieldEnd(data.strCross, "CITY FIRE");
    data.strCross = stripFieldEnd(data.strCross, "FIRE");
    if (!data.strCross.isEmpty() && !data.strCross.contains("/")) {
      int pt = data.strCross.indexOf(',');
      if (pt >= 0) {
        data.strState = data.strCross.substring(pt+1).trim();
        data.strCity = append(data.strCity, " ", data.strCross.substring(0,pt).trim());
        data.strCross = "";
      } else if (data.strState.length() == 0) {
        if (data.strCross.equals("MO")) {
          data.strState = data.strCross;
          data.strCross = "";
        } else if (!isValidAddress(data.strCross) && !Character.isDigit(data.strCross.charAt(0))) {
          data.strCity = append(data.strCity, " ", data.strCross);
          data.strCross = "";
        }
      }
    }
    return true;
  }
}