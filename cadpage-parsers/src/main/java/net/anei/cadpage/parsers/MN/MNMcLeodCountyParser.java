package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNMcLeodCountyParser extends FieldProgramParser {
  
  public MNMcLeodCountyParser() {
    super(CITY_LIST, "MCLEOD COUNTY", "MN",
        "CALL:CALL! PLACE:PLACE? ADDR:ADDR/S! CITY:CITY? ID:ID! PRI:PRI! INFO:INFO");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities  
    "BISCAY",
    "BROWNTON",
    "GLENCOE",
    "HUTCHINSON",
    "LESTER PRAIRIE",
    "PLATO",
    "SILVER LAKE",
    "STEWART",
    "WINSTED",

    //Townships
    "ACOMA",
    "BERGEN",
    "COLLINS",
    "GLENCOE",
    "HALE",
    "HASSAN VALLEY",
    "HELEN",
    "HUTCHINSON",
    "LYNN",
    "PENN",
    "RICH VALLEY",
    "ROUND GROVE",
    "SUMTER",
    "WINSTED",

    // Unincorporated communities
    "FERNANDO",
    "HEATWOLE",
    "KOMENSKY",
    "SHERMAN",
    "SOUTH SILVER LAKE",
    "SUMTER",
    
    // Carver County
    "HOLLYWOOD",
    
    // Wright County
    "VICTOR"
  };
}
