package net.anei.cadpage.parsers.SC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCSaludaCountyParser extends FieldProgramParser {

  public SCSaludaCountyParser() {
    super(CITY_LIST, "SALUDA COUNTY", "SC",
          "ADDR/SXP UNIT CALL INFO! END");
    removeWords("CIRCLE");
  }

  @Override
  public String getFilter() {
    return "Saluda911@saludacounty.sc.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseFields(body.split(";"), data)) return false;
    return !data.strCity.isEmpty();
  }

  private static final String[] CITY_LIST = new String[] {
      // Towns
      "BATESBURG",
      "LEESVILLE",
      "MONETTA",
      "RIDGE SPRING",
      "SALUDA",
      "WARD",

      // Unincorporated communities
      "CHAPPELLS",
      "MOUNT WILLING",
      
      // Edgefield County
      "JOHNSTON",
      
      // Greenwood County
      "NINETY SIX",

      // Newberry County
      "PROSPERITY"

  };

}
