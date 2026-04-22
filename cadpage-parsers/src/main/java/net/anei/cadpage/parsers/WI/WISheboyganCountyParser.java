package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA19Parser;



public class WISheboyganCountyParser extends DispatchA19Parser {

  public WISheboyganCountyParser() {
    super("SHEBOYGAN COUNTY", "WI");
  }

  private static final Pattern MM_PTN = Pattern.compile("\\bMM\\d+\\b");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    Matcher match = MM_PTN.matcher(data.strAddress);
    if (match.lookingAt()) {
      data.strAddress = data.strAddress.replace(" "+match.group(), "");
    }
    return true;
  }

}
