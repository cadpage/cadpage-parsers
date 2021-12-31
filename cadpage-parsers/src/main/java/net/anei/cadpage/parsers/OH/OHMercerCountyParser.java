package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.GroupBestParser;

/*
 * Mercer County, OH
 */

public class OHMercerCountyParser extends GroupBestParser {

  public OHMercerCountyParser() {
    super(new OHMercerCountyAParser(), new OHMercerCountyBParser());
  }

  public static String adjustMapAddr(String addr) {
    addr = addr.replace("MRCR V WRT CO LN", "MERCER VANWERT COUNTY LINE");
    addr = addr.replace("MRCR VW CO LN", "MERCER VANWERT COUNTY LINE");
    addr = addr.replace("VAN WERT MRC CO LN", "MERCER VANWERT COUNTY LINE");
    addr = addr.replace("MRCR DRKE CO LN", "DARKE-MERCER COUNTY LINE");
    addr = addr.replace("OHIO IND STATE LN", "INDIANA OHIO LINE");
    addr = addr.replace("IND OHIO STATE LN", "INDIANA OHIO LINE");
    return addr;
  }
}
