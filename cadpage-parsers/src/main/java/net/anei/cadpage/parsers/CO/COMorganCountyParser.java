package net.anei.cadpage.parsers.CO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

/**
 * Morgan County, CO
 */

public class COMorganCountyParser extends DispatchA27Parser {
  
  public COMorganCountyParser() {
    super("MORGAN COUNTY", "CO", "\\w+");
  }
  
  @Override
  public String getFilter() {
    return "noreply@cisusa.org";
  }
  
  private static final Pattern MCR_PTN = Pattern.compile("\\bMCR\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String adjustMapAddress(String addr) {
    return MCR_PTN.matcher(addr).replaceAll("COUNTY ROAD");
  }
}
