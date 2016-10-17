package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VAClarkeCountyParser extends FieldProgramParser {
  
  public VAClarkeCountyParser() {
    super(CITY_LIST, "CLARKE COUNTY", "VA",
           "ADDR/S X TIME CALL! INFO+");
  }
  
  @Override
  public String getFilter() {
    return "ClarkeECC@clarkecounty.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!body.startsWith("ClarkeECC:")) return false;
    body = body.substring(10).trim();
    return parseFields(body.split(","), 4, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "X ");
      field = stripFieldEnd(field, " X");
      field = field.replace(" X ", " / ");
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, ", ", field);
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    address = stripFieldStart(address, "*");
    return address;
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "BERRYVILLE",
    "BOYCE",

    // Unincorporated communities
    "BERRYS",
    "BETHEL",
    "BRIGGS",
    "CASTLEMANS FERRY",
    "CLAYTONVILLE",
    "DOUBLE TOLLGATE",
    "FROGTOWN",
    "GAYLORD",
    "GREENWAY COURT",
    "LEWISVILLE",
    "LOCKES LANDING",
    "LOST CORNER",
    "MILLWOOD",
    "PIGEON HILL",
    "PYLETOWN",
    "SARATOGA",
    "STONE BRIDGE",
    "STRINGTOWN",
    "SWIMLEY",
    "WADESVILLE",
    "WATERLOO",
    "WEBBTOWN",
    "WHITE POST",
    "WICKLIFFE",
    
    // Fauquier County
    "PARIS",
    
    // Loudoun County
    "BLUEMONT"
  };
}
