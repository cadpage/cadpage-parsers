package net.anei.cadpage.parsers.ZCAON;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.GroupBestParser;

/*
Simcoe County, ON
 */


public class ZCAONSimcoeCountyParser extends GroupBestParser {

  public ZCAONSimcoeCountyParser() {
    super(new ZCAONSimcoeCountyAParser(),
          new ZCAONSimcoeCountyBParser(),
          new ZCAONSimcoeCountyCParser());
  }


  static String doAdjustMapAddress(String sAddress) {
    sAddress = SR_PTN.matcher(sAddress).replaceAll("SIDEROAD");
    sAddress = LI_PTN.matcher(sAddress).replaceAll("LINE");
    return sAddress;
  }

  private static final Pattern SR_PTN = Pattern.compile("\\bSR\\b");
  private static final Pattern LI_PTN = Pattern.compile("\\bLI\\b");

}
