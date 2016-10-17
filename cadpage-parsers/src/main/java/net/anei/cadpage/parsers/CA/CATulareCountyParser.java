package net.anei.cadpage.parsers.CA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA49Parser;

/*
Tulare County, CA
*/

public class CATulareCountyParser extends DispatchA49Parser {

  public CATulareCountyParser() {
    super("TULARE COUNTY","CA");
  }
  
  @Override
  public String getFilter() {
    return "ADSI_CAD@co.tulare.ca.us";
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = RNN_PTN.matcher(addr).replaceAll("RD $1");
    addr = ANN_PTN.matcher(addr).replaceAll("AVE $1");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern RNN_PTN = Pattern.compile("\\bR(\\d+) RD\\b");
  private static final Pattern ANN_PTN = Pattern.compile("\\bA(\\d+) AVE?\\b");
}
