package net.anei.cadpage.parsers.MN;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MNCrowWingCountyParser extends FieldProgramParser {
  
  public MNCrowWingCountyParser() {
    super(CITY_LIST, "CROW WING COUNTY", "MN",
          "CALL:CALL! PLACE:PLACE? ADDR:ADDR/S? CITY:CITY? ID:ID! PRI:PRI! INFO:INFO+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("[A-Z]{3}\\d{10}", true);
    if (name.equals("PRI")) return new PriorityField("\\d");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "; ", field);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BAXTER",
    "BRAINERD",
    "BREEZY POINT",
    "CROSBY",
    "CROSSLAKE",
    "CUYUNA",
    "DEERWOOD",
    "EMILY",
    "FIFTY LAKES",
    "FORT RIPLEY",
    "GARRISON",
    "IRONTON",
    "JENKINS",
    "MANHATTAN BEACH",
    "NISSWA",
    "PEQUOT LAKES",
    "RIVERTON",
    "TROMMALD",
 
    // Townships
    "BAY LAKE",
    "CENTER",
    "CROW WING",
    "DAGGETT BROOK",
    "DEERWOOD",
    "FAIRFIELD",
    "FORT RIPLEY",
    "GAIL LAKE",
    "GARRISON",
    "IDEAL",
    "IRONDALE",
    "JENKINS",
    "LAKE EDWARD",
    "LITTLE PINE",
    "LONG LAKE",
    "MAPLE GROVE",
    "MISSION",
    "NOKAY LAKE",
    "OAK LAWN",
    "PELICAN",
    "PERRY LAKE",
    "PLATTE LAKE",
    "RABBIT LAKE",
    "ROOSEVELT",
    "ROSS LAKE",
    "SIBLEY",
    "ST MATHIAS",
    "TIMOTHY",
    "WOLFORD",

    // Other
    "LAKE HUBERT",
    "MERRIFIELD",
    "MISSON",
    "DEAN LAKE",
    "WEST CROW WING",
    "OLD CROW WING"
    
  };
}
