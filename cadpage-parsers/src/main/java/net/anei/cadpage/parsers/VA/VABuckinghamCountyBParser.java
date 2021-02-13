package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VABuckinghamCountyBParser extends DispatchA71Parser {

  public VABuckinghamCountyBParser() {
    super("BUCKINGHAM COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith(" CLEARED")) data.msgType = MsgType.RUN_REPORT;
    body = MSPACE_PTN.matcher(body).replaceAll(" ");
    if (!super.parseMsg(body, data)) return false;
    if (data.strPlace.equals(data.strAddress)) data.strPlace = "";
    return true;
  }

}
