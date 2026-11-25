package net.anei.cadpage.parsers.ME;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;

public class MEOxfordCountyBParser extends DispatchA19Parser {

  public MEOxfordCountyBParser() {
    super("OXFORD COUNTY", "ME");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern MISSING_BLNK_PTN = Pattern.compile("(?=ACTIVE CALL#|REPORTED:|Desc:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = body.indexOf("\nTIME - ");
    if (pt >= 0) {
      String header = body.substring(pt+1);
      body = body.substring(0, pt).trim();
      header = MISSING_BLNK_PTN.matcher(header).replaceAll(" ");
      body = header + '\n' + body.replace("Phone:", "\nPhone:");
    }
    // TODO Auto-generated method stub
    return super.parseMsg(subject, body, data);
  }
}
