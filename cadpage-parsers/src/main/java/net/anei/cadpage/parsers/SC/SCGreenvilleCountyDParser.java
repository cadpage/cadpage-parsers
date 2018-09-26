package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class SCGreenvilleCountyDParser extends MsgParser {
  
  public SCGreenvilleCountyDParser() {
    super("GREENVILLE COUNTY", "SC");
    setFieldList("CALL ADDR APT CITY PLACE INFO");
  }
  
  @Override
  public String getFilter() {
    return "InformCADPaging@Greenvillecounty.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.endsWith("Emergency")) return false;
    FParser p = new FParser(body);
    data.strCall = p.get(30);
    parseAddress(p.get(50), data);
    if (!p.checkBlanks(350)) return false;
    if (!p.check("(C) ")) return false;
    data.strCity = p.get(30);
    if (!p.checkBlanks(1)) return false;
    data.strPlace = p.get(30);
    if (!p.checkBlanks(370)) return false;
    if (!p.check("[1]")) return false;
    data.strSupp = p.get(1000);
    return true;
  }
}
