package net.anei.cadpage.parsers.KY;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA80Parser;

/**
 * Gallatin County, KY
 */
public class KYGallatinCountyAParser extends DispatchA80Parser {

  public KYGallatinCountyAParser() {
    super("GALLATIN COUNTY", "KY");
  }

  private static Pattern MISSING_BRK_PTN = Pattern.compile(" (?=(?:ADDR|CITY|ID|DATE|TIME|MAP|UNIT|INFO):)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    // Eliminate KYGallatinCountyB -> KYStatePolice
    if (body.startsWith("CALL:")) return false;

    body = "DISPATCH:" + MISSING_BRK_PTN.matcher(body).replaceAll("\n");
    return super.parseMsg(body, data);
  }
}
