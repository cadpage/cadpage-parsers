package net.anei.cadpage.parsers.WV;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

public class WVLewisCountyParser extends DispatchEmergitechParser {

  public WVLewisCountyParser() {
    super(CITY_LIST, "LEWIS COUNTY", "WV");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Text Message|")) return false;
    subject = subject.substring(13).trim();
    return super.parseMsg(subject, body, data);
  }

  private static String[] CITY_LIST = new String[] {

      // City
      "WESTON",

      // Town
      "JANE LEW",

      // Magisterial districts
      "COURTHOUSE-COLLINS SETTLEMENT",
      "FREEMANS CREEK",
      "HACKERS CREEK-SKIN CREEK",

      // Unincorporated communities
      "ABERDEEN",
      "ALKIRES MILLS",
      "ALUM BRIDGE",
      "ARNOLD",
      "ASPINALL",
      "BABLIN",
      "BEALLS MILLS",
      "BEN DALE",
      "BENNETT",
      "BERLIN",
      "BROWNSVILLE",
      "BUTCHERSVILLE",
      "CAMDEN",
      "CHURCHVILLE",
      "COPLEY",
      "COX TOWN",
      "CRAWFORD",
      "EMMART",
      "FREEMANSBURG",
      "GASTON",
      "GEORGETOWN",
      "HOMEWOOD",
      "HORNER",
      "IRELAND",
      "JACKSON MILL",
      "JACKSONVILLE",
      "KITSONVILLE",
      "LIGHTBURN",
      "MCGUIRE PARK",
      "ORLANDO",
      "PICKLE STREET",
      "ROANOKE",
      "TURNERTOWN",
      "WALKERSVILLE",
      "VALLEY CHAPEL",
      "VADIS"
  };
}
