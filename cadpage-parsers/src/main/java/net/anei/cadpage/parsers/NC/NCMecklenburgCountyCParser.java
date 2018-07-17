package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class NCMecklenburgCountyCParser extends MsgParser {
  
  public NCMecklenburgCountyCParser() {
    super("MECKLENBURG COUNTY", "NC");
    setFieldList("CALL ADDR APT CH PRI UNIT");
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Page")) return false;
    FParser fp = new FParser(body);
    data.strCall = fp.get(30);
    if (fp.check(" ")) return false;
    parseAddress(fp.get(35), data);
    if (!fp.check("Ch")) return false;
    data.strChannel = fp.get(3);
    if (!fp.check("Level:")) return false;
    data.strPriority = fp.get(6);
    if (!fp.check(",Unit(s):")) return false;
    data.strUnit = fp.get();
    return true;
  }
}
