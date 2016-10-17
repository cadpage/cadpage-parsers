package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class OHWarrenCountyDParser extends FieldProgramParser {
  
  public OHWarrenCountyDParser() {
    super(CITY_LIST, "WARREN COUNTY", "OH", 
          "Call:CALL! Location:ADDRCITY/S Priority:PRI Unit:UNIT/S Units:UNIT/S Name:PLACE INFO/N", FLDPROG_IGNORE_CASE | FLDPROG_ANY_ORDER);
  }
  
  @Override
  public String getFilter() {
    return "garrettpopovich.oh@gmail.com,@thebeachwaterpark.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.length() == 0) data.msgType = MsgType.GEN_ALERT;
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Notes:");
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "FRANKLIN",
    "LEBANON",
    "LOVELAND",
    "MASON",
    "MIDDLETOWN",
    "MONROE",
    "SPRINGBORO",

    // Villages
    "BLANCHESTER (PART)",
    "BUTLERVILLE",
    "CARLISLE (PART)",
    "CORWIN",
    "HARVEYSBURG",
    "MAINEVILLE",
    "MORROW",
    "PLEASANT PLAIN",
    "SOUTH LEBANON",
    "WAYNESVILLE",

    // Townships
    "CLEARCREEK",
    "DEERFIELD",
    "FRANKLIN",
    "HAMILTON",
    "HARLAN",
    "MASSIE",
    "SALEM",
    "TURTLECREEK",
    "UNION",
    "WASHINGTON",
    "WAYNE",

    // Census-designated places
    "FIVE POINTS",
    "HUNTER",
    "KINGS MILLS",
    "LANDEN",
    "LOVELAND PARK",

    // Unincorporated communities
    "BEEDLES STATION",
    "BLACKHAWK",
    "BLUE BALL (A NEIGHBORHOOD OF MIDDLETOWN)",
    "BROWN'S STORE",
    "CAMARGO",
    "COZADDALE",
    "CROSSWICK",
    "DALLASBURG",
    "DUNLEVY",
    "DODDS",
    "EDWARDSVILLE",
    "FORT ANCIENT",
    "FLAT IRON",
    "FOSTERS",
    "FREDERICKSBURG",
    "GENN TOWN",
    "GREENTREE CORNERS",
    "HAGEMANS CROSSING",
    "HAMMEL",
    "HILLCREST",
    "HENPECK",
    "HICKORY CORNER",
    "HICKS",
    "HOPKINSVILLE",
    "KENDRICKSVILLE",
    "KIRKWOOD",
    "LIBERTY HALL",
    "LEVEL",
    "LYTLE",
    "MATHERS MILL",
    "MIDDLEBORO",
    "MIDDLETOWN JUNCTION",
    "MOUNT HOLLY",
    "MOUNTS STATION",
    "MURDOCH",
    "NEW COLUMBIA",
    "OCEOLA",
    "OREGONIA",
    "OTTERBEIN",
    "PEKIN",
    "RAYSVILLE",
    "RED LION",
    "ROACHESTER",
    "ROSSBURG",
    "RIDGEVILLE",
    "SAN MAR GALE",
    "SENIOR",
    "SCOTTSVILLE",
    "SNIDERCREST",
    "SOCIALVILLE",
    "STUBBS MILL",
    "TWENTY MILE STAND",
    "UNION VILLAGE",
    "UNITY",
    "UTICA",
    "WEST WOODVILLE",
    "WINDSOR",
    "ZOAR",
    
    // Clinton County
    "LIBERTY TOWNSHIP"
  };
}
