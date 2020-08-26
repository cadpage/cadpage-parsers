package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.dispatch.DispatchA74Parser;

public class KYElliottCountyParser extends DispatchA74Parser {

  public KYElliottCountyParser() {
    super("ELLIOTT COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern S_KY_PTN = Pattern.compile("\\b(?:S )?KY\\b");

  @Override
  public String postAdjustMapAddress(String addr) {
    return S_KY_PTN.matcher(addr).replaceAll("STATE HWY");
  }
}
