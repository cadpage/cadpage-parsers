package net.anei.cadpage.parsers.MD;

import java.util.Properties;

import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class MDSomersetCountyParser extends DispatchOSSIParser {
  
  public MDSomersetCountyParser() {
    super(CITY_CODES, "SOMERSET COUNTY", "MD",
           "SRC? CALL ADDR! CITY INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@somerset911.local,CAD@somersetmd.us";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]{3}");
    return super.getField(name);
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALL",  "ALLEN",
      "BERL", "BERLIN",
      "CRIS", "CRISFIELD",
      "DEAL", "DEAL ISLAND",
      "EDEN", "EDEN",
      "EWEL", "EWELL",
      "FAIR", "UPPER FAIRMOUNT",
      "FRUI", "FRUITLAND",
      "MANO", "MANOKIN",
      "MARI", "MARION STATION",
      "POCO", "POCOMOKE CITY",
      "PRIN", "PRINCESS ANNE",
      "RHOD", "RHODES POINT",
      "SALI", "SALISBURY",
      "TYLE", "TYLERTON",
      "VENT", "VENTON",
      "WEST", "WESTOVER"
  });
}
