package net.anei.cadpage.parsers.TX;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA53Parser;

public class TXHardinCountyBParser extends DispatchA53Parser {

  public TXHardinCountyBParser() {
    super("HARDIN COUNTY", "TX");
  }

  @Override
  public String getFilter() {
    return "dispatch@cityofsilsbee.com";
  }

  private static final Pattern NOT_APT_PTN = Pattern.compile("[A-Z] [NSEW]");

  @Override
  protected boolean isNotExtraApt(String apt) {
    if (NOT_APT_PTN.matcher(apt).matches()) return true;
    return super.isNotExtraApt(apt);
  }


}
