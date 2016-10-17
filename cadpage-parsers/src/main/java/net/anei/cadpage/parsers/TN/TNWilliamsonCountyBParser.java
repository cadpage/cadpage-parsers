package net.anei.cadpage.parsers.TN;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class TNWilliamsonCountyBParser extends DispatchB2Parser {
  
  private static final Pattern BAD_MARKER = Pattern.compile("911-CENTER:[A-Z0-9]+ ?>");
  private static final Pattern TO_X_PTN = Pattern.compile("(?:(.*) )?(?:NORTH|SOUTH|EAST|WEST) BOUND (.*) TO +(.*)");
  
  public TNWilliamsonCountyBParser() {
    super("911-CENTER:", TNWilliamsonCountyParser.CITY_LIST, "WILLIAMSON COUNTY", "TN", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_NAMES);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() > 0) return false;
    if (BAD_MARKER.matcher(body).lookingAt()) return false;
    if (!super.parseMsg(body, data)) return false;
    
    // Odd cross street conventions frequently end up in the name field
    String name = append(data.strCross, " ", data.strName);
    Matcher match = TO_X_PTN.matcher(name);
    if (match.matches()) {
      data.strAddress = append(data.strAddress, " ", getOptGroup(match.group(1)));
      String cross = match.group(2).trim();
      name = match.group(3);
      data.strCross = "";
      if (name.equals("OHB") || name.startsWith("OHB ")) {
        data.strCross = "OHB";
        name = name.substring(3).trim();
      } else {
        parseAddress(StartType.START_ADDR, FLAG_ONLY_CROSS, name, data);
        name = getLeft();
      }
      data.strCross = append(cross, " / ", data.strCross);
      data.strName = name;
    }
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    addr = CR_XX_PTN.matcher(addr).replaceAll(" ").trim();
    return addr;
  }
  private static final Pattern CR_XX_PTN = Pattern.compile(" *\\b[A-Z]{2,3}-[A-Z]{2,3}\\b *");
  
  private static final String[] MWORD_STREET_NAMES = new String[]{
    "BAKERS BRIDGE",
    "CROWNE BROOKE",
    "GLEN RIDGE",
    "GOOD SPRINGS",
    "GRAND HAVEN",
    "GRAND OAKS",
    "GREEN HILL",
    "KNOX VALLEY",
    "MALLORY STATION",
    "NORTH BOUND MOORES",
    "SHADOW RIDGE",
    "SUMMIT VIEW",
    "TWIN SPRINGS",
    "WILSON PIKE"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "HOME INVASION",
      "KIDNAPPING OR ABDUCTION",
      "MISSING ENDANGERED",
      "PERSONAL INJURY ACCIDENT",
      "PERSON SHOT",
      "PERSON STABBED",
      "ROBBERY IN PROGRESS",
      "STRUCTURE FIRE"
  );
}