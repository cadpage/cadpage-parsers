package net.anei.cadpage.parsers.TN;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchB2Parser;


public class TNWilliamsonCountyBParser extends DispatchB2Parser {
  
  private static final Pattern BAD_MARKER = Pattern.compile("911-CENTER:[A-Z0-9]+ +>");
  private static final Pattern TO_X_PTN = Pattern.compile("(?:(.*) )?(?:NORTH|SOUTH|EAST|WEST) BOUND (.*) TO +(.*)");
  private static final Pattern I_X_PTN = Pattern.compile("(\\d+ I\\d+(?: [NSEW]B)?)\\b *(.*)");
  
  public TNWilliamsonCountyBParser() {
    super("911-CENTER:", TNWilliamsonCountyParser.CITY_LIST, "WILLIAMSON COUNTY", "TN", B2_FORCE_CALL_CODE);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_NAMES);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (BAD_MARKER.matcher(body).lookingAt()) return false;
    
    // 615 area code frequently appears without the rest of the phone number :(
    body = body.replace("   615 ", "   6150000000 ");
    if (!super.parseMsg(body, data)) return false;
    if (data.strPhone.equals("6150000000")) data.strPhone = "";
    
    data.strAddress = stripFieldEnd(data.strAddress, " & CITY LIMITS");
    
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
    else if (data.strCross.length() == 0 && (match = I_X_PTN.matcher(data.strName)).matches()) {
      data.strCross = match.group(1);
      data.strName = match.group(2);
    }
    
    if (data.strCity.length() == 0) {
      data.strName = stripFieldEnd(data.strName, " CITY OF");
      if (data.strName.endsWith("FRANKLIN")) {
        data.strCity = "FRANKLIN";
        data.strName = data.strName.substring(0, data.strName.length()-8).trim();
      }
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
      "BEECH CREEK",
      "BERRY CHAPEL",
      "CARRIAGE HILLS",
      "COOL SPRINGS",
      "CROWNE BROOKE",
      "EXECUTIVE CENTER",
      "GENERAL MACARTHUR",
      "GLEN RIDGE",
      "GOOD SPRINGS",
      "GRAND HAVEN",
      "GRAND OAKS",
      "GRANNY WHITE",
      "GREEN HILL",
      "HIGH LEA",
      "HIGH OAKS",
      "HOLLY TEE GAP",
      "HOLLY TREE GAP",
      "IN A VALE",
      "JOHNSON CHAPEL",
      "KNOX VALLEY",
      "LONG VALLEY",
      "MALLORY STATION",
      "MARKET EXCHANGE",
      "MEADOW LAKE",
      "MORGAN FARMS",
      "MOUNTAIN ASH",
      "NORTH BOUND MOORES",
      "OWL LANDING",
      "PRINCETON HILLS",
      "QUAIL VALLEY",
      "RAVENSWOOD FARM",
      "RED OAK",
      "RIVER OAKS",
      "ROCKY SPRINGS",
      "SHADOW CREEK",
      "SHADOW RIDGE",
      "SUMMIT VIEW",
      "TORREY PINES",
      "TWIN SPRINGS",
      "WALNUT BEND",
      "WALNUT HILLS",
      "WALNUT PARK",
      "WARDLEY PARK",
      "WEST MCEWEN",
      "WILSON PIKE"
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ATTEMPTED ROBBERY",
      "DEATH INVESTIGATION",
      "HOME INVASION",
      "KIDNAPPING OR ABDUCTION",
      "MISSING ENDANGERED",
      "OTHER FIRE",
      "PERSONAL INJURY ACCIDENT",
      "PERSON SHOT",
      "PERSON STABBED",
      "ROBBERY FROM BUSINESS",
      "ROBBERY FROM PERSON",
      "ROBBERY IN PROGRESS",
      "STRUCTURE FIRE",
      "TEST STRUCTURE FIRE",
      "WEEKLY PAGER TEST"
  );
}