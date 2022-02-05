package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYRussellCountyParser extends DispatchA74Parser {

  public KYRussellCountyParser() {
    super("RUSSELL COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm1.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern EST_PTN = Pattern.compile("\\bEST\\b");

  @Override
  public String adjustMapAddress(String addr) {
    addr =  EST_PTN.matcher(addr).replaceAll("ESTATES");
    return addr;
  }
}
