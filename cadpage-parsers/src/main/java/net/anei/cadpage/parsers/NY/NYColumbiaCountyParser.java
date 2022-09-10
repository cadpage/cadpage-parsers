package net.anei.cadpage.parsers.NY;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA13Parser;

public class NYColumbiaCountyParser extends DispatchA13Parser {
  
  public NYColumbiaCountyParser() {
    super(CITY_LIST, "COLUMBIA COUNTY", "NY", A13_FLG_LEAD_PLACE);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }
  
  @Override
  public String getFilter() {
    return "cc911@columbiacountyny.com";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " VILLAGE");
    data.strCity = stripFieldEnd(data.strCity, " CITY");
    if (data.strCity.equals("HANCOCK")) data.strState = "MA";
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "CITY ST");
  }

  // Override checkAddress to relax the standards a bit
  @Override
  protected boolean isValidAddress(String address) {
    return super.checkAddress(address, 1) > STATUS_NOTHING;
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "APPLE MEADOW",
    "ARCH BRIDGE",
    "BEEBE POND",
    "BRADLEYS CROSSING",
    "CHURCH HILL",
    "FISH LAKE",
    "FLINTS CROSSING",
    "FOX HOLLOW",
    "GREEN ACRES",
    "HADDOCK HILL",
    "INDUSTRIAL TRACT",
    "JOSLEN HEIGHTS",
    "KNICKERBOCKER LAKE",
    "LESS TRAVELED",
    "MT MERINO",
    "MT VIEW",
    "PEACEFUL VALLEY",
    "PERCY HILL",
    "PLUM TREE",
    "QUEECHY LAKE",
    "ROCK CITY",
    "ROUND LAKE",
    "RUNNING CREEK",
    "SCHILLINGS CROSSING",
    "SCHODACK LANDING",
    "SKY VIEW",
    "SPOOK ROCK",
    "ST LUKES",
    "STATE FARM",
    "STONY KILL",
    "SUMMIT HEIGHTS",
    "TEN BROECK",
    "TOWN HALL",
    "TREMPER ASSOCIATION",
    "TUNNEL HILL",
    "VAN HOESEN",
    "WARNER CROSSING",
    "WEST SHORE"
  };
  
  private static final String[] CITY_LIST = new String[]{
    // City
    "HUDSON",
    "HUDSON CITY",
    
    // Towns
    "ANCRAM",
    "AUSTERLITZ",
    "CANAAN",
    "CHATHAM",
    "CLAVERACK",
    "CLERMONT",
    "COPAKE",
    "GALLATIN",
    "GERMANTOWN",
    "GHENT",
    "GREENPORT",
    "HILLSDALE",
    "KINDERHOOK",
    "LIVINGSTON",
    "NEW LEBANON",
    "STOCKPORT",
    "STUYVESANT",
    "TAGHKANIC",
    
    // Villages
    "CHATHAM",
    "KINDERHOOK",
    "PHILMONT",
    "VALATIE",
    "CHATHAM VILLAGE",
    "KINDERHOOK VILLAGE",
    "PHILMONT VILLAGE",
    "VALATIE VILLAGE",
    
    // Census-designated places
    "CLAVERACK-RED MILLS",
    "COPAKE",
    "COPAKE FALLS",
    "COPAKE LAKE",
    "GERMANTOWN",
    "GHENT",
    "LORENZ PARK",
    "NIVERVILLE",
    "STOTTVILLE",
    "TACONIC SHORES",
    "HAMLETS",
    "BOSTON CORNER",
    "COLUMBIAVILLE",
    "CRARYVILLE",
    "EAST CHATHAM",
    "ELIZAVILLE",
    "HUMPHREYSVILLE",
    "LEBANON SPRINGS",
    "MELLENVILLE",
    "NEW BRITAIN",
    "NEW LEBANON",
    "NEW LEBANON CENTER",
    "NIVERVILLE",
    "OLD CHATHAM",
    "SPENCERTOWN",
    "STUYVESANT FALLS",
    "RED ROCK",
    "WEST COPAKE",
    "WEST LEBANON",
    
    // Greene County
    "CATSKILL",
    
    // Renselaer County
    "NASSAU",
    "SCHODACK",
    
    // Berkshire County, MA
    "HANCOCK",
  };
}
	