package net.anei.cadpage.parsers.OK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA33Parser;


public class OKGarfieldCountyAParser extends DispatchA33Parser {
  
  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("^\\*\\*\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d \\d\\*\\* *");
  
  public OKGarfieldCountyAParser() {
    super("GARFIELD COUNTY", "OK");
  }
  
  @Override
  public String getFilter() {
    return "911firedispatch@enid.org";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Call")) return false;
    if (!super.parseMsg(body, data)) return false;
    if (data.strCross.equals("OK")) data.strCross = "";
    if (data.strPlace.equalsIgnoreCase("PIONEER")) data.strPlace = "";
    Matcher match = INFO_DATE_TIME_PTN.matcher(data.strSupp);
    if (match.find()) data.strSupp = data.strSupp.substring(match.end());
    return true;
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return HWY412_PTN.matcher(address).replaceAll("US 412");
  }
  private static final Pattern HWY412_PTN = Pattern.compile("\\bHWY *412\\b", Pattern.CASE_INSENSITIVE);
}
