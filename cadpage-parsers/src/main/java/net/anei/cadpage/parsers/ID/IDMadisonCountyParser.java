package net.anei.cadpage.parsers.ID;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA55Parser;

public class IDMadisonCountyParser extends DispatchA55Parser {

  public IDMadisonCountyParser() {
    super("MADISON COUNTY", "ID");
  }

  @Override
  public String getFilter() {
    return "ereports@eforcesoftware.com";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *// *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    data.strSupp = INFO_BRK_PTN.matcher(data.strSupp).replaceAll("\n");
    return true;
  }

}
