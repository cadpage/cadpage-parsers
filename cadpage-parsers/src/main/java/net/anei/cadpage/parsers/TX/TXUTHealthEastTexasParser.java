package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class TXUTHealthEastTexasParser extends DispatchProQAParser {

  public TXUTHealthEastTexasParser() {
    this("", "TX");
  }

  TXUTHealthEastTexasParser(String defCity, String defState) {
    super(CITY_LIST, defCity, defState,
          "PRI ID! TIME INFO NAME NAME/CS? ADDR/Z CITY APT APT! INFO/N+", true);
  }

  @Override
  public String getLocName() {
    if (getDefaultCity().isEmpty()) return "UTHealth East Texas, TX";
    return super.getLocName();
  }

  @Override
  public String getFilter() {
    return "cadpage@uthet.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("APT")) return new MyAptField();
    return super.getField(name);
  }

  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0, pt);
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:Apt|Rm|Room|Lot)\\.? *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field =  match.group(1);
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[] {

    // Cherokee County
      // Cities
      "GALLATIN",
      "JACKSONVILLE",
      "NEW SUMMERFIELD",
      "REKLAW",
      "RUSK",
      "TOWNS",
      "ALTO",
      "CUNEY",
      "WELLS",

      // Census-designated place
      "SHADYBROOK",

      // Unincorporated communities
      "AFTON GROVE",
      "ATOY",
      "BLACKJACK",
      "CHURCH HILL",
      "CONCORD",
      "CORINE",
      "COVE SPRINGS",
      "CRAFT",
      "DELMER",
      "DIALVILLE",
      "EARLES CHAPEL",
      "FOREST",
      "FRYS GAP",
      "GOULD",
      "IRONTON",
      "LAKEVIEW",
      "LARISSA",
      "LINWOOD",
      "MAYDELLE",
      "MIXON",
      "MOUNT SELMAN",
      "NEW HOPE",
      "OAKLAND",
      "PIERCES CHAPEL",
      "PINE HILL",
      "PONTA",
      "REESE",
      "SALEM",
      "SHADY GROVE",
      "TECULA",
      "TURNEY",
      "WEEPING MARY",
      "WOODVILLE",
      "GHOST TOWNS",
      "ETNA",
      "KNOXVILLE",
      "LARISSA",
      "LONE STAR",
      "NEBO",

    // Henderson County
      // Cities
      "ATHENS",
      "BROWNSBORO",
      "CHANDLER",
      "EUSTACE",
      "GUN BARREL CITY",
      "LOG CABIN",
      "MALAKOFF",
      "MOORE STATION",
      "MURCHISON",
      "SEVEN POINTS",
      "STAR HARBOR",
      "TOOL",
      "TRINIDAD",
      "TOWNS",
      "BERRYVILLE",
      "CANEY CITY",
      "COFFEE CITY",
      "ENCHANTED OAKS",
      "MABANK",
      "PAYNE SPRINGS",
      "POYNOR",

      // Unincorporated communities
      "ALEY",
      "ANTIOCH",
      "BAXTER",
      "BETHEL",
      "BIG ROCK",
      "BUFFALO",
      "CRESCENT HEIGHTS",
      "CROSS ROADS",
      "DAUPHIN",
      "EVELYN",
      "FINCASTLE",
      "HARMONY",
      "LARUE",
      "LEAGUEVILLE",
      "MANKIN",
      "NEW HOPE",
      "OPELIKA",
      "PAULINE",
      "PICKENS",
      "PINE GROVE",
      "RUTH SPRINGS",
      "SHADY OAKS",
      "STOCKARD",
      "SUMER HILL",
      "UNION HILL",
      "VIRGINIA HILL",
      "GHOST TOWNS",
      "CENTREVILLE",
      "CORINTH",

    // Rusk County
      // Cities
      "EASTON",
      "HENDERSON",
      "KILGORE",
      "MOUNT ENTERPRISE",
      "NEW LONDON",
      "OVERTON",
      "REKLAW",
      "TATUM",

      // Census-designated place
      "LAKE CHEROKEE",

      // Other unincorporated communities
      "CONCORD",
      "HARMONY HILL",
      "JOINERVILLE",
      "LAIRD HILL",
      "LANEVILLE",
      "LEVERETTS CHAPEL",
      "MINDEN",
      "NEW SALEM",
      "PRICE",
      "RED LEVEL",
      "SELMAN CITY",
      "STEWART",
      "TURNERTOWN",
      "BRACHFIELD",

    // Smith County
      // Cities
      "ARP",
      "HIDEAWAY",
      "LINDALE",
      "NEW CHAPEL HILL",
      "NOONDAY",
      "TROUP",
      "TYLER",
      "WHITEHOUSE",
      "TOWNS",
      "BULLARD",
      "WINONA",

      // Census-designated place
      "EMERALD BAY",

      // Unincorporated communities
      "ANTIOCH",
      "BASCOM",
      "BLACKJACK",
      "BOSTICK",
      "BROWNING",
      "CARROLL",
      "CHAPEL HILL",
      "COPELAND",
      "DOGWOOD CITY",
      "ELBERTA",
      "FLINT",
      "GARDEN VALLEY",
      "GRESHAM",
      "JAMESTOWN",
      "LEE SPRING",
      "MIDWAY",
      "MOUNT SYLVAN",
      "NEW HARMONY",
      "NEW HOPE",
      "OMEN",
      "OWENTOWN",
      "PINE SPRINGS",
      "PINE TRAIL ESTATES",
      "RED SPRINGS",
      "SALEM",
      "SAND FLAT",
      "SHADY GROVE",
      "SINCLAIR CITY",
      "STARRVILLE",
      "SWAN",
      "TEASELVILLE",
      "THEDFORD",
      "WALNUT GROVE",
      "WATERS BLUFF",
      "WOOD SPRINGS",
      "WRIGHT CITY",
      "GHOST TOWNS",
      "BURNING BUSH",
      "DOUGLAS",
      "UTICA",

    // Wood County
      // Municipalities and incorporated towns
      "ALBA",
      "HAWKINS",
      "MINEOLA",
      "QUITMAN",
      "WINNSBORO",
      "YANTIS",

      // Unincorporated settlements and towns
      "CARTWRIGHT",
      "CONCORD",
      "EAST POINT",
      "CROW",
      "FOUKE",
      "GOLDEN",
      "HAINESVILLE",
      "HOLLY LAKE RANCH",
      "LIBERTY",
      "LITTLE HOPE",
      "MACEDONIA",
      "MOUNT ENTERPRISE",
      "MOUNT PISGAH",
      "NEW HOPE",
      "OAK GROVE",
      "PEACH",
      "PERRYVILLE",
      "PINE MILLS",
      "STOUT",

      // Small communities, ghost towns, and former settlements
      "BLACK OAK",
      "CANEY",
      "CEDAR TREE",
      "CENTER POINT",
      "CHALYBEATE SPRINGS",
      "COLDWATER",
      "COTTONWOOD",
      "DYESS",
      "FLETCHER",
      "FLOYDS COMMON RIDGE",
      "FOREST HILL",
      "FOREST HOME",
      "FRIENDSHIP",
      "LONE PINT",
      "LONE STAR",
      "MERRIMAC",
      "MOUNT ZION",
      "MUDDY CREEK",
      "MYRTLE SPRINGS",
      "OGBURN",
      "PERSIMMON GROVE",
      "PLEASANT DIVIDE",
      "PLEASANT GROVE",
      "ROBINSONS CHAPEL",
      "ROCK HILL",
      "SALEM",
      "SAND SPRINGS",
      "SHADY GROVE",
      "SMYRNA",
      "SPRING HILL",
      "TRANQUIL",
      "WEBSTER"

  };
}
