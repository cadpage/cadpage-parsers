package net.anei.cadpage.parsers.PA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchArchonixParser;



public class PAFultonCountyParser extends DispatchArchonixParser {
  
  public PAFultonCountyParser() {
    super(CITY_CODES, "FULTON COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "EP911@ccpa.net";
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
      
      "FC", "FRANKLIN COUNTY",
      "HC", "HUNTINGDON COUNTY"
  });
}
