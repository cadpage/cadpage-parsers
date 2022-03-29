package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;



public class VAWashingtonCountyAParser extends DispatchSouthernParser {
  
  public VAWashingtonCountyAParser() {
    super(CITY_LIST, "WASHINGTON COUNTY", "VA", 
          DSFLG_OPT_DISP_ID | DSFLG_ADDR_LEAD_PLACE | DSFLG_ADDR | DSFLG_ADDR_TRAIL_PLACE | DSFLG_OPT_NAME | DSFLG_OPT_PHONE | DSFLG_ID | DSFLG_TIME);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "@washcova.com";
  }
  
  private static final Pattern COUNTY_PTN = Pattern.compile("([A-Z ]+ CO(?:UNTY)?)(?: (TN))?", Pattern.CASE_INSENSITIVE);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!super.parseMsg(body, data)) return false;
    
    data.strCity = convertCodes(data.strCity, MISSPELLED_CITY_TABLE);
    
    Matcher match = COUNTY_PTN.matcher(data.strName); 
    if (match.matches()) {
      data.strName = "";
      if (data.strCity.length() == 0) data.strCity = match.group(1).trim().toUpperCase();
      String state = match.group(2);
      if (state != null) data.strState = state.toUpperCase();
    }
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_SECTOR_PTN = Pattern.compile(" *(?:[NSEW]|[NS][EW]) SECTOR\\b *");
  private class MyAddressField extends BaseAddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("(TN)");
      if (pt >= 0) {
        data.strState = "TN";
        field = append(field.substring(0, pt).trim(), " ", field.substring(pt+4).trim());
      }
      field = ADDR_SECTOR_PTN.matcher(field).replaceAll(" ").trim();
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
      "BATTLE HILL",
      "BEAVER DAM",
      "BERRY CREEK",
      "BIG BASS CAMP",
      "BIG JACK",
      "BLACK HOLLOW",
      "BLACK SHALE",
      "BLUE HILLS",
      "BLUE SPRINGS",
      "BLUE SPRUCE",
      "BLUFF HOLLOW",
      "BONE HOLLOW",
      "BREEZY POINT",
      "BRUMLEY GAP",
      "BRYSON COVE",
      "BUFFALO POND",
      "BUZZARD DEN",
      "CABIN CREEK",
      "CANEY VALLEY",
      "CEMETERY RIDGE",
      "CHERRY TREE",
      "CHESTNUT MOUNTAIN",
      "CHIP RIDGE",
      "CLEAR CREEK",
      "CLEVELAND CHURCH",
      "COUNTRY LAKE",
      "COUNTY PARK",
      "COVE CREEK",
      "DEBUSK MILL",
      "DEER RUN",
      "DENTON VALLEY",
      "DISHNER VALLEY",
      "EAST SECOND",
      "FALCON PLACE",
      "FALL HILL",
      "FIFTEEN MILE",
      "FIG TREE",
      "FISHER HOLLOW",
      "FLAME LEAF",
      "GATE CITY",
      "GLADE CENTER",
      "GOLDEN VIEW",
      "GREEN COVE",
      "GREEN SPRING",
      "GREEN SPRINGS CHURCH",
      "GRINDSTONE BRANCH",
      "HALLS BOTTOM",
      "HARMONY HILL",
      "HARMONY HILLS",
      "HAYTERS GAP",
      "HEATH BROTHERS",
      "HICKORY RIDGE",
      "HIDDEN VALLEY",
      "HIGHLANDS CENTER",
      "HILL CREST",
      "HONEY LOCUST",
      "INDUSTRIAL PARK",
      "JASPER CREEK",
      "JEB STUART",
      "JOE DERTING",
      "JOHNSTON MEMORIAL",
      "JOSH ALLEN",
      "KELLY CHAPEL",
      "KING MILL",
      "LARGE HOLLOW",
      "LIME HILL",
      "LIVINGSTON CREEK",
      "LOCUST BRANCH",
      "LONE STAR",
      "LOVES MILL",
      "MAIDEN CREEK",
      "MARYS CHAPEL",
      "MEDICAL PARK",
      "MOCK KNOB",
      "MONTE VISTA",
      "MONTEGO BAY",
      "MOORE CREEK",
      "MORNING SIDE",
      "MOUNT CALM",
      "MOUNT VERNON",
      "MOUNTAIN CITY",
      "MT VIEW",
      "NORTH FORK RIVER",
      "OAK GROVE",
      "OAK HILL",
      "ORCHARD HILL",
      "PARKS MILL",
      "PEAR TREE",
      "PLUM CREEK",
      "POOR VALLEY",
      "PRICES BRIDGE",
      "RED HILL",
      "REEDY CREEK",
      "RICH VALLEY",
      "ROCK SPRING",
      "ROCKY HILL",
      "RUST HOLLOW",
      "SCOTT RIDGE",
      "SILVER LAKE",
      "SLEIGH HILL",
      "SMITH CREEK",
      "SNAFFLE BIT",
      "SOUTH SHADY",
      "SPRING CREEK",
      "STONE MILL",
      "STONE MOUNTAIN",
      "SUGAR COVE",
      "SWEET BRIER",
      "TAYLOR VALLEY",
      "TOOLE CREEK",
      "TWIN OAKS",
      "TWIN RIVERS",
      "VAILS MILL",
      "VANCES MILL",
      "WALKER MOUNTAIN",
      "WALTON RIDGE",
      "WES GENTRY",
      "WHISPERING PINE",
      "WHITAKER HOLLOW",
      "WHITE RIDGE",
      "WHITES MILL",
      "WIDENER VALLEY",
      "WINDY HILL",
      "WOLF CREEK",
      "WYATT HOLLOW",
      "ZION CHURCH"
  };
  
  private static final Properties MISSPELLED_CITY_TABLE = buildCodeTable(new String[]{
      "HILTON",     "HILTONS"
  });

  private static final String[] CITY_LIST = new String[]{
      
      // Towns
     "ABINGDON",
     "DAMASCUS",
     "GLADE SPRING",
     "SALTVILLE",
     "WEST ABINGDON",
     
     // Census designated places
     "EMORY",
     "EMORY-MEADOWVIEW",
     "MEADOWVIEW",
     
     // Unincorporated communities
     "GOOSE PIMPLE JUNCTION",
     "GREEN SPRING",
     "KONNAROCK",
     "MENDOTA",
  
     // Sullivan County, TN
     "BRISTOL",
     "JOHNSON",
     "JOHNSON CITY",
    
     // Scott County
     "HILTONS",
     "HILTON", // misspelled
    
     // Smyth County
     "CHILHOWIE"
  };
}
