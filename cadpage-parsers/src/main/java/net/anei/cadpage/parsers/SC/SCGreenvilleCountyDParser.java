package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class SCGreenvilleCountyDParser extends MsgParser {
  
  public SCGreenvilleCountyDParser() {
    super("GREENVILLE COUNTY", "SC");
    setFieldList("CALL ADDR APT CITY PLACE INFO PRI ID X UNIT");
  }
  
  @Override
  public String getFilter() {
    return "InformCADPaging@Greenvillecounty.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    FParser fp = new FParser(body);
    data.strCall = fp.get(30);
    if (fp.check("@")) {
      parseAddress(fp.get(30), data);
      if (!fp.checkBlanks(370)) return false;
      if (!fp.check("UNIT ASSIGNED-"))  return false;
      data.strUnit = fp.get();
      return true;
      
    }
    if (fp.check(" ")) return false;
    parseAddress(fp.get(100), data);
    if (!fp.checkAhead(0, " ")) {
      data.strCity = stripFieldStart(fp.get(34), "(C)");
      if (!fp.check(" ")) return false;
      data.strPlace = fp.get(100);
      if (!fp.check("[1] ")) return false; 
      data.strSupp = fp.get(4092);
      if (fp.check(" ")) return false;
      data.strPriority = fp.get(30);
      if (fp.check(" ")) return false;
      data.strCallId =  fp.get(20);
      data.strCross = fp.get();
      return true;
    }
    if (!fp.checkBlanks(150)) return false;  
    if (!fp.checkAhead(0, " ")) {
      data.strCity = stripFieldStart(fp.get(34), "(C)");
      if (!fp.check(" ")) return false;
      data.strPlace = fp.get(400);
      if (!fp.check("[1] ")) return false;
      data.strSupp = fp.get(4092);
      if (fp.checkAhead(0, " ") && !fp.checkBlanks(3904)) return false;
      if (fp.check(" ")) return false;
      data.strPriority = fp.get(30);
      if (fp.check(" ")) return false;
      data.strCallId =  fp.get(20);
      data.strCross = fp.get();
      return true;
    }
    if (!fp.checkBlanks(150)) return false;
    data.strCity = stripFieldStart(fp.get(34), "(C)");
    if (!fp.check(" ")) return false;
    data.strPlace = fp.get(400);
    if (!fp.check("[1] ")) return false;
    data.strSupp = fp.get(4092);
    if (fp.check(" ")) return false;
    data.strCallId = fp.get(20);
    if (fp.check(" ")) return false;
    data.strPriority = fp.get(30);
    return true;
  }

}
