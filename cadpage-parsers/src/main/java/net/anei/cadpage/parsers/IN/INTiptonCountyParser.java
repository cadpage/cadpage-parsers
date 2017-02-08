package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class INTiptonCountyParser extends DispatchOSSIParser {
  
  public INTiptonCountyParser() {
    super("TIPTON COUNTY", "IN",
           "UNIT CALL MAP? ADDR INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@tipco.com.CAD@tiptoncounty.in.gov";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("CAD:")) {
      if (subject.equals("CAD TEXT")) {
        body = "CAD:" + body;
      } else if (subject.contains(";") && !subject.contains(":")) {
        body = "CAD:" + subject + ':' + body;
      }
    }
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field  getField(String name) {
    if (name.equals("MAP")) return new MapField("\\d+|CITY");
    return super.getField(name);
  }
}
