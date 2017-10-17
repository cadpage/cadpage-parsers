package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchEmergitechParser;

/**
 * Christian County, IL
 */
public class ILShelbyCountyParser extends DispatchEmergitechParser {
  
  public ILShelbyCountyParser() {
    super("ChristianCounty911:", true, CITY_LIST, "SHELBY COUNTY", "IL", TrailAddrType.PLACE);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR2")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{
    
    // Cities
    "SHELBYVILLE",
    "WINDSOR",

    // Town
    "SIGEL",

    // Villages
    "COWDEN",
    "FINDLAY",
    "HERRICK",
    "MOWEAQUA",
    "OCONEE",
    "STEWARDSON",
    "STRASBURG",
    "TOWER HILL",

    // Census-designated place
    "WESTERVELT",

    // Other unincorporated communities
    "CLARKSBURG",
    "DOLLVILLE",
    "DUVALL",
    "FANCHER",
    "HENTON",
    "HERBORN",
    "HINTON",
    "KINGMAN",
    "LAKEWOOD",
    "MIDDLESWORTH",
    "MODE",
    "RENNERVILLE",
    "YANTISVILLE",

    // Townships
    "ASH GROVE",
    "BIG SPRING",
    "CLARKSBURG",
    "COLD SPRING",
    "DRY POINT",
    "FLAT BRANCH",
    "HERRICK",
    "HOLLAND",
    "LAKEWOOD",
    "MOWEAQUA",
    "OCONEE",
    "OKAW",
    "PENN",
    "PICKAWAY",
    "PRAIRIE",
    "RICHLAND",
    "RIDGE",
    "ROSE",
    "RURAL",
    "SHELBYVILLE",
    "SIGEL",
    "TODDS POINT",
    "TOWER HILL",
    "WINDSOR",
    
    // Christian County
    "ASSUMPTION",
    "MOWEAQUA",
    "PANA",
    "PRAIRETON",
    
    // Coles County
    "MATTOON",
    "PARADISE",
    
    // Macon County
    "MACON",
    "SOUTH MACON",
    "MILAM",
    
    // Moultrie County
    "BETHANY",
    "GAYS",
    "LAKE SHELBYVILLE",
    "MARROWBONE",
    "SULLIVAN",
    "WHITLEY"
  };
}
