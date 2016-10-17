package net.anei.cadpage.parsers.TN;

import java.util.regex.Pattern;

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
  public String adjustMapAddress(String addr) {
    addr = BY_PTN.matcher(addr).replaceAll("BYPASS");
    return super.adjustMapAddress(addr);
  }
  
  private static final Pattern BY_PTN = Pattern.compile("\\bBY\\b", Pattern.CASE_INSENSITIVE);
}
