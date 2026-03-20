package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.GroupBestParser;

public class WVSummitBechtelReserveParser extends GroupBestParser {

  public WVSummitBechtelReserveParser() {
    super(new WVSummitBechtelReserveAParser(),
          new WVSummitBechtelReserveBParser());
  }
}
