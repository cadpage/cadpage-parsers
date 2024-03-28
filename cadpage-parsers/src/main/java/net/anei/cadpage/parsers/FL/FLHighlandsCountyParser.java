package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;

public class FLHighlandsCountyParser extends MsgParser {

  public FLHighlandsCountyParser() {
    super("HIGHLANDS COUNTY", "FL");
    setFieldList("ID CALL ADDR APT GPS PLACE X");
  }

  @Override
  public String getFilter() {
    return "smsmith@highlandssheriff.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  private static final Pattern MASTER = Pattern.compile("Incident: *(\\S+) ([-/& A-Z]+) +Address *(.*?)\\nhttps://maps.google.com/\\?q=(.*?)(?:\n(.*))?");
  private static final Pattern PLACE_X_PTN = Pattern.compile("(.*)\\bX2\\[(.*)\\]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("SQL Server Message")) return false;

    int pt = body.indexOf("\nUnder Florida Law");
    if (pt >= 0) body = body.substring(0, pt).trim();

    Matcher match = MASTER.matcher(body);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strCall =  match.group(2).trim();
    parseAddress(match.group(3).trim(), data);
    setGPSLoc(match.group(4), data);
    String extra = match.group(5);
    if (extra != null) {
      extra = extra.trim();
      match = PLACE_X_PTN.matcher(extra);
      if (match.matches()) {
        data.strCross = match.group(2).trim();
        extra =  match.group(1).trim();
      }
      data.strPlace = extra;
    }

    return true;
  }

}
