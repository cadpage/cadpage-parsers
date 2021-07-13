package net.anei.cadpage.parsers.NY;


import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchRedAlertParser;



public class NYNassauCountyRedAlertParser extends DispatchRedAlertParser {

  private static final Pattern JUNK_PTN = Pattern.compile(" *\\([A-Z]\\) *");

  public NYNassauCountyRedAlertParser() {
    super("NASSAU COUNTY","NY");
    setupMultiWordStreets(MWORD_STREET_LIST);

    setupSpecialStreets(
        "BROADWAY",
        "COTTAGE ROW",
        "NORTH BROADWAY",
        "PENINSULA BOULEVARD",
        "SOUTHERN STATE",
        "SOUTHERN STATE EX 19"
    );
  }

  @Override
  public String getFilter() {
    return super.getFilter() + ",alarms@rvcny.us,notifications@Plainviewfd.com,vtext.com@mxh3.email-od.com,alerts@nmxpaging.com,93001";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_REMOVE_EXT;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    body = body.replace('\n', ' ');
    if (!super.parseMsg(subject, body, data)) return false;

    // Supp info can have all kinds of fun things
    Parser p = new Parser(data.strSupp);
    data.strUnit = p.getLastOptional(", Response:");
    data.strMap = append(p.getLastOptional("Zone:"), " - ", data.strMap);
    String sInfo = p.get();
    if (sInfo.endsWith(":")) sInfo = sInfo.substring(0, sInfo.length()-1).trim();
    data.strSupp = sInfo;

    // Some of the city names need to be adjusted
    data.strCity = convertCodes(data.strCity, CITY_CODES);

    // Clean paren stuff out of cross fields
    data.strCross = JUNK_PTN.matcher(data.strCross).replaceAll(" ");
    return true;
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ACORN PONDS",
    "BAITING PLACE",
    "BAKER HILL",
    "BEACH 2ND",
    "BELLA VISTA",
    "BERRY HILL",
    "BETHPAGE SWEETHOLLOW",
    "BIRCHWOOD PARK",
    "BLUE SEA",
    "BRUSH HOLLOW",
    "BUENA VISTA",
    "CANTIAGUE ROCK",
    "CAPITOL HEIGHTS",
    "CEDAR SWAMP",
    "CENTRAL PARK",
    "CHERRY VALLEY",
    "CLINTON C BOONE",
    "COUNTRY CLUB",
    "COUNTRY VILLAGE",
    "COUNTY COURTHOUSE",
    "COUNTY COURT HOUSE",
    "COVE EDGE",
    "COVE NECK",
    "CRESCENT BEACH",
    "EAST MEADOW",
    "EAST SHORE",
    "ETHEL T KLOBERG",
    "FAIR OAKS",
    "FROST POND",
    "GLEN COVE",
    "GLEN HEAD",
    "GLEN KEITH",
    "GRAND TERRACE",
    "GREAT NECK",
    "GRIST MILL",
    "HARBOR POINT",
    "HEATHER HILL",
    "HILLSIDE PARK",
    "HYDE PARK",
    "I U WILLETS",
    "ISLAND PARK",
    "JAMES L L BURRELL",
    "KINGS POINT",
    "LARCH HILL",
    "LAUREL COVE",
    "LAUREL HOLLOW",
    "LAURELTON BEACH",
    "LITTLE NECK",
    "LITTLE WHALENECK",
    "LONG BEACH",
    "LONG ISLAND",
    "MARTIN LUTHER KING",
    "MATINECOCK FARMS",
    "MIDDLE NECK",
    "MILL HILL",
    "MILL RIVER",
    "MOORES HILL",
    "NORTH FRANKLIN",
    "NORTH JERUSALEM",
    "NORTH MARGINAL",
    "NORTH SERVICE",
    "NORTH STATION",
    "NORTHERN STATE",
    "NOTRE DAME",
    "O BAYVIEW",
    "O FAIRVIEW",
    "O KOENIG",
    "O UPPER",
    "O WEST SHORE",
    "O WOODBURY",
    "OAK HILL",
    "OPP KEATS",
    "OPP SLEEPY",
    "OYSTER BAY",
    "PEA POND",
    "PEACOCK POND",
    "PINE HOLLOW",
    "POND PARK",
    "QUAKER MEETING HOUSE",
    "RED SPRING",
    "ROLLING HILL",
    "ROOSEVELT PARK BAY",
    "ROUND SWAMP",
    "SAGAMORE HILL",
    "SAINT ROCCO",
    "SANDY HILL",
    "SCHOOL HOUSE",
    "SCHOOL STSCHOOL",
    "SEA CLIFF",
    "SHELTER ROCK",
    "SHIPS POINT",
    "SHORE PARK",
    "SHRUB HOLLOW",
    "SO MIDDLE NECK",
    "SOUND BEACH",
    "SOUTH FRANKLIN",
    "SOUTH LONG BEACH",
    "SOUTH MARGINAL",
    "SOUTH SERVICE",
    "SOUTHERN STATE",
    "ST ANDREWS",
    "ST GEORGE",
    "ST LUKES",
    "ST MARKS",
    "ST PAULS",
    "STEAMBOAT LANDING",
    "STONE HILL",
    "SUGAR MAPLE",
    "SUTTON HILL",
    "TENNIS COURT",
    "TERRA PARK",
    "THOMAS POWELL",
    "TOWN HOUSE",
    "TOWN PATH",
    "VAN COTT",
    "VISTA HILL",
    "WANTAGH STATE",
    "WATER MILL",
    "WEST END",
    "WEST GRAHAM",
    "WEST MARSHALL",
    "WEST MERRICK",
    "WEST SHORE",
    "WEST TERRACE",
    "WILLIAM PENN",
    "WINDSOR GATE",
    "WOOD PARK",
    "YELLOW COTE"

  };

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "LAKE SUCCESS QUARD", "LAKE SUCCESS",
      "STRATHMORE VILLAGE", "STRATHMORE"
  });
}
