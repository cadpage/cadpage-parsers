package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA39Parser;

public class OHMedinaCountyCParser extends DispatchA39Parser {

  public OHMedinaCountyCParser() {
    super(CITY_LIST, "MEDINA COUNTY", "OH");
  }
  
  @Override
  public String getFilter() { 
    return "dispatch@brunswick.oh.us"; 
  }
  
  @Override
  public boolean parseUntrimmedMsg(String subject, String body, Data data) {
    if (!super.parseUntrimmedMsg(subject, body, data)) return false;
    if (data.strCity.equalsIgnoreCase("BRUNSWICK HI")) data.strCity = "BRUNSWICK HILLS";
    return true;
  }

  private static final String[] CITY_LIST = new String[]{
      "BRUNSWICK",
      "MEDINA",
      "RITTMAN",
      "WADSWORTH",
      
      "CHIPPEWA LAKE",
      "CRESTON",
      "GLORIA GLENS PARK",
      "LODI",
      "SEVILLE",
      "SPENCER",
      "SULLIVAN",
      "WESTFIELD CENTER",
      
      "BRUNSWICK HI",
      "CHATHAM",
      "GRANGER",
      "GUILFORD",
      "HARRISVILLE",
      "HINCKLEY",
      "HOMER",
      "LAFAYETTE",
      "LITCHFIELD",
      "LIVERPOOL",
      "MEDINA",
      "MONTVILLE",
      "SHARON",
      "SPENCER",
      "WADSWORTH",
      "WESTFIELD",
      "YORK",
      
      "ABBEYVILLE",
      "BEEBETOWN",
      "BENNETTS CORNERS",
      "CHATHAM",
      "CODDINGVILLE",
      "CRAWFORD CORNERS",
      "ERHART",
      "FRIENDSVILLE",
      "GRANGER",
      "HINCKLEY",
      "HOMERVILLE",
      "LESTER",
      "LITCHFIELD",
      "MALLET CREEK",
      "PAWNEE",
      "REMSEN CORNERS",
      "RIVER STYX",
      "SHARON CENTER",
      "VALLEY CITY"
  };
}
