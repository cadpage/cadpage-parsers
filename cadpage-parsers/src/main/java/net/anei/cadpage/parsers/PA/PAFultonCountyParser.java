package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;



public class PAFultonCountyParser extends DispatchArchonixParser {

  public PAFultonCountyParser() {
    super(CITY_CODES, MA_CITY_CODES, "FULTON COUNTY", "PA", 0);
  }

  @Override
  public String getFilter() {
    return "EP911@ccpa.net";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseMsg(subject, body, data)) return false;
    if (data.strCity.equals("WASHINGTON COUNTY")) data.strState = "PA";
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{

      "AR", "AYR TWP",
      "BC", "BRUSH CREEK TWP",
      "BL", "BELFAST TWP",
      "BT", "BETHEL TWP",
      "DB", "DUBLIN TWP",
      "LC", "LICKING CREEK TWP",
      "MC", "MCCONNELLSBURG",
      "TD", "TODD TWP",
      "TH", "THOMPSON TWP",
      "TY", "TAYLOR TWP",
      "UN", "UNION TWP",
      "VH", "VALLEY-HI",
      "WL", "WELLS TWP",

      "CBG", "CHAMBERSBURG",
      "FC",  "FRANKLIN COUNTY",
      "HC",  "HUNTINGDON COUNTY",
      "WC",  "WASHINGTON COUNTY"
  });

  private static final Properties MA_CITY_CODES = buildCodeTable(new String[] {
      "CBG", "CHAMBERSBURG",
      "FC",  "FRANKLIN COUNTY",
      "HC",  "HUNTINGDON COUNTY",
      "WC",  "WASHINGTON COUNTY"
  });
}
