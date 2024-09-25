package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;


public class KYBarrenCountyParser extends DispatchA74Parser {

  public KYBarrenCountyParser() {
    super("BARREN COUNTY", "KY");
  }

  public String getFilter() {
    return "Dispatch@BarrenKY.info,Dispatch@911comm2.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = CPW_PTN.matcher(addr).replaceAll("CUMBERLAND PKWY");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern CPW_PTN = Pattern.compile("\\bCPW\\b", Pattern.CASE_INSENSITIVE);
}
