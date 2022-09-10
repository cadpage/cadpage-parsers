package net.anei.cadpage.parsers.CA;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchA20Parser;

public class CAMaderaCountyBParser extends DispatchA20Parser {
  
  public CAMaderaCountyBParser() {
    super(CALL_CODES, "MADERA COUNTY", "CA");
  }
  
  @Override
  public String getFilter() {
    return "@cityofchowchilla.org";
  }
  
  private static final Properties CALL_CODES = buildCodeTable(new String[]{
      "FACCI",  "Motor Vehicle Accident",
      "FEXPL",  "Explosion",
      "FFWORK", "Firework Fire",
      "FHAZ",   "Hazardous Condition",
      "FIRE",   "Fire",
      "FMEDI",  "Medical Aid",
      "FSERV",  "Service Call",
      "SFIRE",  "Structure Fire"

  });

}
