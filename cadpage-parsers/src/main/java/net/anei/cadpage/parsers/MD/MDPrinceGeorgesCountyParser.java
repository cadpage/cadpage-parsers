package net.anei.cadpage.parsers.MD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;

public class MDPrinceGeorgesCountyParser extends GroupBestParser {

  public MDPrinceGeorgesCountyParser() {
    super(new MDPrinceGeorgesCountyFireBizParser(),
          new MDPrinceGeorgesCountyDParser(),
          new MDPrinceGeorgesCountyEParser(),
          new MDPrinceGeorgesCountyFParser(),
          new MDPrinceGeorgesCountyGParser(),
          new MDPrinceGeorgesCountyHParser(),
          new MDPrinceGeorgesCountyIParser());
  }

  private static final Pattern BALT_WASH_PKY = Pattern.compile("\\b(?:(?:BALT WASH|BW|BMW)/s*(?:PK[WY])?)\\b");

  static String adjustMapAddressCommon(String addr) {
    addr = addr.replace("CAP BELT OL", "CAPITAL BELTWAY OUTER LOOP")
               .replace("CAP BELT IL", "CAPITAL BELTWAY INNER LOOP");
    addr = BALT_WASH_PKY.matcher(addr).replaceAll("BALTIMORE WASHINGTON PARKWAY");
    return addr;
  }
}
