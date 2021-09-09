package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA71Parser;

public class FLWaltonCountyParser extends DispatchA71Parser {

  public FLWaltonCountyParser() {
    super("WALTON COUNTY", "FL");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\S+) - +(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CODE_CALL_PTN.matcher(data.strCall);
    if (match.matches()) {
      data.strCode = match.group(1);
      data.strCall = match.group(2);
    }
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CALL", "CODE CALL");
  }

}
