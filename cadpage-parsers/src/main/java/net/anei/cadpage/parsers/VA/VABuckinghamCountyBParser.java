package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class VABuckinghamCountyBParser extends DispatchA71Parser {

  public VABuckinghamCountyBParser() {
    super("BUCKINGHAM COUNTY", "VA");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return super.parseMsg(subject, body, data);
  }
}
