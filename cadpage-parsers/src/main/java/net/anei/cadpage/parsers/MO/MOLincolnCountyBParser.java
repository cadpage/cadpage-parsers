package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class MOLincolnCountyBParser extends DispatchA74Parser {
  
  public MOLincolnCountyBParser() {
    super("LINCOLN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "Dispatch@lincolnE911.info";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strApt.length() > 0 && NUMERIC.matcher(data.strAddress).matches()) {
      Parser p = new Parser(data.strApt);
      data.strCity = p.getLastOptional(',');
      data.strApt = p.get(' ');
      data.strAddress = append(data.strAddress, " ", p.get());
    }
    return true;
  }

}
