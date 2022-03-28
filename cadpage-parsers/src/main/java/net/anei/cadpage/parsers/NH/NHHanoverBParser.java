package net.anei.cadpage.parsers.NH;

import net.anei.cadpage.parsers.dispatch.DispatchA27Parser;

public class NHHanoverBParser extends DispatchA27Parser {

  public NHHanoverBParser() {
    super("HANOVER", "NH");
  }

  @Override
  public String getFilter() {
    return "notification@nhpd.cloud";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
