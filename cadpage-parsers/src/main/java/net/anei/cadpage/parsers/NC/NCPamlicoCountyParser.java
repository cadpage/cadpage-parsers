package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCPamlicoCountyParser extends FieldProgramParser {
  
  public NCPamlicoCountyParser() {
    super(CITY_LIST, "PAMLICO COUNTY", "NC", 
          "ADDR/S X EMPTY EMPTY EMPTY? ID CALL! INFO+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split(";"), data)) return false;
    if (data.strCity.equalsIgnoreCase("VANDERMORE")) data.strCity = "VANDEMERE";
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{6,}", true);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("*", "");
      field = field.replace(" X ", " / ");
      field = stripFieldStart(field, "X ");
      field = stripFieldEnd(field, " X");
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Towns
    "ALLIANCE",
    "ARAPAHOE",
    "BAYBORO",
    "GRANTSBORO",
    "MESIC",
    "MINNESOTT BEACH",
    "ORIENTAL",
    "STONEWALL",
    "VANDEMERE",
    "VANDERMORE",   // Mispelled

    // Census-designated place
    "HOBUCKEN",

    // Unincorporated communities
    "FLORENCE",
    "JERICHO",
    "LOWLAND",
    "MARIBEL",
    "MERRITT",
    "OLYMPIA",
    "REELSBORO",
    "WHORTONSVILLE",
    
    // Craven County
    "NEW BERN",
  };
}
