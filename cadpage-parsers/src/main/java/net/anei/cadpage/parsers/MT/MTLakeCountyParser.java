package net.anei.cadpage.parsers.MT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchC16Parser;

public class MTLakeCountyParser extends DispatchC16Parser {

  public MTLakeCountyParser() {
    this("LAKE COUNTY", "MT");
  }

  MTLakeCountyParser(String defCity, String defState) {
    super(defCity, defState);
  }

  @Override
  public String getFilter() {
    return "no-reply@csprosuite.centralsquarecloudgov.com";
  }
}
