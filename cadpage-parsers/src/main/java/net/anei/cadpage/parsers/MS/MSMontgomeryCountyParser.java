package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class MSMontgomeryCountyParser extends DispatchA74Parser {
  
  public MSMontgomeryCountyParser() {
    super("MONTGOMERY COUNTY", "MS");
  }
  
  @Override
  public String getFilter() {
    return "dispatch@montgomerye911.info";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith(":\nEVENT:")) {
      body = "CFS" + body;
    }
    return super.parseMsg(subject, body, data);
  }

}
