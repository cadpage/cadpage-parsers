package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

public class TNDyerCountyParser extends DispatchA49Parser {


  public TNDyerCountyParser() {
    super("DYER COUNTY","TN");
  }

  @Override
  public String getFilter() {
    return "cadalerts@dyersburgtn.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    Matcher match = CALL_GUIDE_PTN.matcher(data.strCall);
    if (match.matches()) data.strCall = match.group(1);
    return true;
  }

  private static final Pattern CALL_GUIDE_PTN =  Pattern.compile(".*?\\*+CALL GUIDE\\*+ *(.*)");

  @Override
  protected String fixCall(String call) {
    Matcher match = CALL_GUIDE_PTN.matcher(call);
    if (match.matches()) call = match.group(1);
    return super.fixCall(call);
  }

  private static final Pattern BY_PTN = Pattern.compile("\\bBY\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = BY_PTN.matcher(addr).replaceAll("BYPASS");
    return super.adjustMapAddress(addr);
  }
}
