package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYCarterCountyBParser extends DispatchA74Parser {

  public KYCarterCountyBParser() {
    super("CARTER COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

  private static final Pattern D_JOHNSON_LN_PTN = Pattern.compile("\\bD JOHNSON (?:RD|LN)\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = D_JOHNSON_LN_PTN.matcher(addr).replaceAll("DEWEY JOHNSON RD");
    return addr;
  }

}
