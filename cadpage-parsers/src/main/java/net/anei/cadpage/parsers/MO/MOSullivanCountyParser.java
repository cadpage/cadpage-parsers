package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA62Parser;

public class MOSullivanCountyParser extends DispatchA62Parser {
  
  public MOSullivanCountyParser() {
    super("SULLIVAN COUNTY", "MO");
  }
  
  @Override
  public String getFilter() {
    return "lawmansullco911@gmail.com";
  }
  
  private static final Pattern UNIT_INFO_PTN = Pattern.compile("(\\d{3}[A-Z])\\b *(.*)", Pattern.DOTALL);
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    // They always have something that looks like a unit designation
    // in the beginning of the CAD notes
    Matcher match = UNIT_INFO_PTN.matcher(data.strSupp);
    if (match.matches()) {
      data.strUnit = match.group(1);
      data.strSupp = match.group(2);
    }
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("INFO", "UNIT INFO");
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = SHELBYNNN_PTN.matcher(addr).replaceAll("COUNTY ROAD $1");
    addr = RT_PTN.matcher(addr).replaceAll("HWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern SHELBYNNN_PTN = Pattern.compile("\\bSHELBY +(\\d+)\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern RT_PTN = Pattern.compile("\\bRT\\b", Pattern.CASE_INSENSITIVE);
}
