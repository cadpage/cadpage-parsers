package net.anei.cadpage.parsers.MS;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA35Parser;


public class MSNeshobaCountyParser extends DispatchA35Parser {
  
  public MSNeshobaCountyParser() {
    super("NESHOBA COUNTY", "MS");
  }
  
  @Override
  public String getFilter() {
    return "NC911@NC911.COM ";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0) return false;
    return super.parseMsg(body, data);
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = COUNTY_RD_PTN.matcher(addr).replaceAll("$1COUNTY ROAD$2");
    addr = STATE_RD_PTN.matcher(addr).replaceAll("MS $1");
    return addr;
  }
  private static final Pattern COUNTY_RD_PTN = Pattern.compile("(^|& +|\\d+ +)RD +(\\d+(?:$| +&| +[NSEW]))", Pattern.CASE_INSENSITIVE);
  private static final Pattern STATE_RD_PTN = Pattern.compile("\\bHWY +(\\d+)\\b", Pattern.CASE_INSENSITIVE);
}
