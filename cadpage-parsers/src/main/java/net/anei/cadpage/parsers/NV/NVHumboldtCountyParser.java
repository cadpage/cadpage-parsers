package net.anei.cadpage.parsers.NV;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

/**
 * Humbolt County, NV
 */
public class NVHumboldtCountyParser extends DispatchA20Parser {

  public NVHumboldtCountyParser() {
    super("HUMBOLDT COUNTY", "NV");
  }

  @Override
  public String getFilter() {
    return "hcsodispatch795@gmail.com,hcsodispatch@hcsonv.com,no-reply-rico@humboldtcountynv.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }
}
