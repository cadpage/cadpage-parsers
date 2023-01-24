package net.anei.cadpage.parsers.ZCANB;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA91Parser;

public class ZCANBFrederictonParser extends DispatchA91Parser {

  public ZCANBFrederictonParser() {
    super("Station Dispatch - Dispatch Alert", "FREDERICTON", "NB");
  }

  @Override
  public String getFilter() {
    return "alerts@wrnotify.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(subject, body, data);
  }
}
