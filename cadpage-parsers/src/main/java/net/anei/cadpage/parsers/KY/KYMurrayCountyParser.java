package net.anei.cadpage.parsers.KY;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class KYMurrayCountyParser extends DispatchSPKParser {
  public KYMurrayCountyParser() {
    super("MURRAY COUNTY", "KY");
  }

  @Override
  public String getFilter() {
    return "MurrayE911CAD@murrayky.gov";
  }
}
