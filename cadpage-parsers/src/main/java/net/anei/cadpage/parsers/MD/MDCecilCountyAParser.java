package net.anei.cadpage.parsers.MD;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class MDCecilCountyAParser extends MsgParser {

  private static final Pattern TIME_PTN = Pattern
      .compile("\\d\\d:\\d\\d:\\d\\d");

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CCLT", "CECILTON", "CHES", "CHESAPEAKE CITY", "ELK", "ELKTON", "NE",
      "NORTH EAST", "CH", "CHARLESTOWN", "PV", "PERRYVILLE", "PD",
      "PORT DEPOSIT", "RS", "RISING SUN", "EARL", "EARLVILLE", "PP",
      "PERRY POINT", "COLO", "COLORA", "CONO", "CONOWINGO" });

  public MDCecilCountyAParser() {
    super("CECIL COUNTY", "MD");
    setFieldList("BOX CALL ADDR APT X CITY ID");
  }

  @Override
  public String getFilter() {
    return "cfcrs8@gmail.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {

    String[] flds = body.split(" *\\* *");
    if (flds.length < 5)
      return false;

    boolean ooc = flds[0].equals("OOC");
    if (!ooc)
      data.strBox = flds[0];

    data.strCall = flds[1];
    String sAddr = flds[2];
    int pt = sAddr.indexOf('@');
    if (pt >= 0) {
      data.strPlace = sAddr.substring(0, pt).trim();
      sAddr = sAddr.substring(pt+1).trim();
    }
    parseAddress(sAddr, data);

    int ndx = 2;
    while (++ndx < flds.length) {
      String fld = flds[ndx];
      if (fld.length() == 0)
        continue;
      if (TIME_PTN.matcher(fld).matches())
        break;

      if (fld.length() <= 4) {
        if (!ooc)
          data.strCity = convertCodes(fld, CITY_CODES);
      } else {
        if (data.strCross.length() > 0)
          data.strCross += " & ";
        data.strCross += fld;
      }
    }

    if (++ndx >= flds.length)
      return false;
    data.strCallId = flds[ndx];

    // Out of county responses are a problem, we really don't know
    // which county or state they might be in.
    if (ooc) data.defCity = data.defState = "";

    return true;
  }
}
