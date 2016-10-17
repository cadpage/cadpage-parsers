package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class MSCalhounCountyParser extends FieldProgramParser {

  public MSCalhounCountyParser() {
    super(CITY_LIST, "CALHOUN COUNTY", "MS",
           "CALL ( PLACE ADDR/Z APT CITY! | ADDR/Z APT CITY | PLACE? ADDR/Z CITY! ) Note:INFO");
  }
  
  @Override
  public String getFilter() {
    return "cadalerts@eforcesoftware.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_REMOVE_EXT;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Alert")) return false;
    if (!parseFields(body.split("\n"), 3, data)) return false;
    data.strAddress = data.strAddress.replace("BURKE-CC", "BURKE CALHOUN CITY");
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("Apt *(.*)", true);
    return super.getField(name);
  }
  
  
  private static final String[] CITY_LIST = new String[]{

    // Calhoun County
    // Towns
    "BRUCE",
    "CALHOUN CITY",
    "DERMA",
    "VARDAMAN",
    // Villages
    "BIG CREEK",
    "PITTSBORO",
    "SLATE SPRINGS",
    // Unincorporated places
    "SAREPTA",
    "BANNER",
    "NEW LIBERTY",
    "ELZEY",
    "ELLARD",
    "WARDWELL",
    "REID",
    "BENTLY",
    "CHICKENBONE",
    "MACEDONIA",
    "BAILEYVILLE",
    "OLD TOWN",
    "LLOYD",
    "NEW GAULLEY",
    "HOLLIS SWITCH",
    "COTTAGE LANE",
    "BLUEBERRY/TATER BED HILL",
    "BULL MOUNTAIN",
    "DENTONTOWN",
    "SABOUGLA",
    "SPRING HILL",
    
    // Chickasaw County
    // Cities
    "HOUSTON",
    "OKOLONA",
    // Towns
    "NEW HOULKA",
    // Villages
    "HOULKA",
    "WOODLAND",
    // Unincorporated Places
    "BUENA VISTA",
    "EGYPT",
    "MCCONDY",
    "PYLAND",
    "SPARTA",
    "THORN",
    "TREBLOC",
    "VAN VLEET",

    // Yalobusha County
    // Cities
    "WATER VALLEY",
    // Towns
    "COFFEEVILLE",
    "OAKLAND",
    "TILLATOBA",
    // Unincorporated places
    "SCOBEY",

  };
}
