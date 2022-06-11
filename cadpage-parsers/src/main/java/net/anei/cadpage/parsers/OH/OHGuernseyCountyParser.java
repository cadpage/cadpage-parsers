package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class OHGuernseyCountyParser extends DispatchA71Parser {

  public OHGuernseyCountyParser() {
    super("GUERNSEY COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io,uadispatch@seormc.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Run Detail")) data.strSource = subject;
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseMsg(body,  data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

}
