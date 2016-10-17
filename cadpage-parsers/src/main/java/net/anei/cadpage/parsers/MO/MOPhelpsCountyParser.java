package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA61Parser;

public class MOPhelpsCountyParser extends DispatchA61Parser {
 
  public MOPhelpsCountyParser() {
    super("PHELPS COUNTY", "MO");
  }
  
  public String getFilter() {
    return "dispatch@rollacity.org";
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return PVTNNNN_PTN.matcher(address).replaceAll("PVT DRIVE $1");
  }
  private static final Pattern PVTNNNN_PTN = Pattern.compile("\\b(?:PVT|PRIVATE DR) *(\\d+)\\b", Pattern.CASE_INSENSITIVE);
}