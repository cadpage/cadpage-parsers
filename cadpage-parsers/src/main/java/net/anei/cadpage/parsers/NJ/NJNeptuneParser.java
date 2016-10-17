package net.anei.cadpage.parsers.NJ;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NJNeptuneParser extends SmartAddressParser {
  
  public NJNeptuneParser() {
    super("NEPTUNE", "NJ");
    setFieldList("CALL ADDR INFO");
  }
  
  @Override
  public String getFilter() {
    return "iar@neptunetownship.org";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("NEPTUNE PD COMMS CENTER")) return false;
    Result res = parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_IMPLIED_APT, body);
    if (res.isValid()) {
      res.getData(data);
      data.strSupp = res.getLeft();
    } else {
      data.strCall = "GENERAL ALERT";
      data.strPlace = body;
    }
    return true;
  }
}
