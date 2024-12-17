package net.anei.cadpage.parsers.IN;

import net.anei.cadpage.parsers.dispatch.DispatchSPKParser;

public class INFloydCountyBParser extends DispatchSPKParser {

  public INFloydCountyBParser() {
    super("FLOYD COUNTY", "IN");
  }

  @Override
  public String getFilter() {
    return "NewAlbanyCAD@NAPD.COM";
  }
}
