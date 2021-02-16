package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class OHGuernseyCountyParser extends DispatchA71Parser {

  public OHGuernseyCountyParser() {
    super("GUERNSEY COUNTY", "OH");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    return super.parseMsg(body,  data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

}
