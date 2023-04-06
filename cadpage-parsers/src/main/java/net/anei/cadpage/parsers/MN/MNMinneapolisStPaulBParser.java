package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA92Parser;

/**
 Minneapolis/St Paul, MN (B)
 **/
public class MNMinneapolisStPaulBParser extends DispatchA92Parser {
  public MNMinneapolisStPaulBParser() {
    super("MINNEAPOLIS", "MN");
  }

  @Override
  public String getLocName() {
    return "MPLS/St. Paul, MN";
  }

  @Override
  public String getFilter() {
    return "logisids@vlitech.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    String place = data.strPlace;
    int pt = place.indexOf("Cross Street:");
    if (pt >= 0) {
      data.strCross = place.substring(pt+13).trim();
      place = place.substring(0,pt).trim();
    }
    if (place.startsWith("Original Address : ")) place = "";
    data.strPlace = place;
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("PLACE", "PLACE X");
  }

}
