package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA86Parser;

public class MSNeshobaCountyBParser extends DispatchA86Parser {

  public MSNeshobaCountyBParser() {
    super("NESHOBA COUNTY", "MS");
  }

  @Override
  public String getFilter() {
    return "dispatch@NeshobaCounty911.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CAD INCIDENT")) subject = "CAD DISPATCH";
    return super.parseMsg(subject, body, data);
  }

}
