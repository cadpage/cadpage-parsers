package net.anei.cadpage.parsers.ZCAAB;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;

/**
 * Canmore, Alberta, Canada
 */
public class ZCAABCanmoreAParser extends FieldProgramParser {

  private static final CodeTable STD_CODE_TABLE = new StandardCodeTable();

  public ZCAABCanmoreAParser() {
    super("CANMORE", "AB",
          "TIME! UNITS:UNIT? CALL:CODE! AT:ADDRCITY! EVENT_NO:ID! LAT:GPS1 LON:GPS2 END");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "@fresc.ca";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Rip & Run")) return false;
    return super.parseMsg(body, data);
  }

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyUnitField  extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', ',');
      super.parse(field, data);
    }
  }

  private class MyCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
      data.strCode = field;
      String call = STD_CODE_TABLE.getCodeDescription(field);
      if (call == null) call = field;
      data.strCall = call;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|ROOM|UNIT|LOT)(?![A-Z]) *(.*)|(\\d{1,4}[A-Z]?|[A-Z])", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String addr = p.get(',');
      if (addr.length() == 0) addr = p.get(',');
      addr = addr.replace('_', ' ');
      addr = stripFieldStart(addr, "*");
      parseAddress(addr.trim(), data);
      data.strCity = stripFieldStart(p.getLast(','), "md of ");
      String place = p.get();
      Matcher match = APT_PTN.matcher(place);
      if (match.matches()) {
        place = match.group(1);
        if (place == null) place = match.group(2);
        data.strApt = append(data.strApt, "-", place);
      } else if (!place.equals("u:0 c:90")){
        data.strPlace = place;
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private static final Pattern SPLIT_NUMBER_PTN = Pattern.compile("^\\d{1,3}-(\\d{3,})\\b");
  private static final Pattern SFX_PTN = Pattern.compile("\\b[a-z]{1,7}\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = SPLIT_NUMBER_PTN.matcher(addr).replaceFirst("$1");
    StringBuffer sb = null;
    Matcher match = SFX_PTN.matcher(addr);
    while (match.find()) {
      String word1 = match.group();
      String word2 = SUFFIX_TABLE.getProperty(word1.toLowerCase());
      if (word2 != null) {
        if (sb == null) sb = new StringBuffer();
        match.appendReplacement(sb, word2);
      }
    }
    if (sb != null) {
      match.appendTail(sb);
      addr = sb.toString();
    }

    return addr;
  }

  private static final Properties SUFFIX_TABLE = buildCodeTable(new String[]{
      "allee",    "alley",
      "ally",     "alley",
      "aly",      "alley",
      "anex",     "annex",
      "annx",     "annex",
      "anx",      "annex",
      "arc",      "arcade",
      "av",       "ave",
      "aven",     "ave",
      "avenu",    "ave",
      "avn",      "ave",
      "avnue",    "ave",
      "bayou",    "bayoo",
      "bch",      "beach",
      "bg",       "burg",
      "bgs",      "burgs",
      "blf",      "bluff",
      "blfs",     "bluffs",
      "bluf",     "bluff",
      "blvd",     "boulevard",
      "bnd",      "bend",
      "bot",      "bottom",
      "bottm",    "bottom",
      "boul",     "boulevard",
      "boulv",    "boulevard",
      "br",       "branch",
      "brdge",    "bridge",
      "brg",      "bridge",
      "brk",      "brook",
      "brks",     "brooks",
      "brnch",    "branch",
      "btm",      "bottom",
      "byp",      "bypass",
      "bypa",     "bypass",
      "bypas",    "bypass",
      "byps",     "bypass",
      "byu",      "bayoo",
      "canyn",    "canyon",
      "causway",  "causeway",
      "cen",      "center",
      "cent",     "center",
      "centr",    "center",
      "centre",   "center",
      "circ",     "cir",
      "circl",    "cir",
      "cirs",     "circles",
      "ck",       "creek",
      "clb",      "club",
      "clf",      "cliff",
      "clfs",     "cliffs",
      "cmp",      "camp",
      "cnter",    "center",
      "cntr",     "center",
      "cnyn",     "canyon",
      "cor",      "corner",
      "cors",     "corners",
      "cp",       "camp",
      "cpe",      "cape",
      "cr",       "crescent",
      "crcl",     "circle",
      "crcle",    "circle",
      "crecent",  "crescent",
      "cres",     "crescent",
      "cresent",  "crescent",
      "crk",      "creek",
      "crscnt",   "crescent",
      "crse",     "course",
      "crsent",   "crescent",
      "crsnt",    "crescent",
      "crssing",  "crossing",
      "crssng",   "crossing",
      "crst",     "crest",
      "crt",      "ct",
      "cswy",     "causeway",
      "ctr",      "center",
      "ctrs",     "centers",
      "cts",      "courts",
      "curv",     "curve",
      "cv",       "cove",
      "cvs",      "coves",
      "cyn",      "canyon",
      "div",      "divide",
      "dl",       "dale",
      "dm",       "dam",
      "dr",       "drive",
      "driv",     "drive",
      "drs",      "drives",
      "drv",      "drive",
      "dv",       "divide",
      "dvd",      "divide",
      "est",      "estate",
      "ests",     "estates",
      "exp",      "expressway",
      "expr",     "expressway",
      "express",  "expressway",
      "expw",     "expressway",
      "expy",     "expressway",
      "ext",      "extension",
      "extn",     "extension",
      "extnsn",   "extension",
      "exts",     "extensions",
      "fld",      "field",
      "flds",     "fields",
      "fls",      "falls",
      "flt",      "flat",
      "flts",     "flats",
      "forests",  "forest",
      "forg",     "forge",
      "frd",      "ford",
      "frds",     "fords",
      "freewy",   "freeway",
      "frg",      "forge",
      "frgs",     "forges",
      "frk",      "fork",
      "frks",     "forks",
      "frry",     "ferry",
      "frst",     "forest",
      "frt",      "fort",
      "frway",    "freeway",
      "frwy",     "freeway",
      "fry",      "ferry",
      "ft",       "fort",
      "fwy",      "freeway",
      "gardn",    "garden",
      "gatewy",   "gateway",
      "gatway",   "gateway",
      "gdn",      "garden",
      "gdns",     "gardens",
      "gln",      "glen",
      "glns",     "glens",
      "grden",    "garden",
      "grdn",     "garden",
      "grdns",    "gardens",
      "grn",      "green",
      "grns",     "greens",
      "grov",     "grove",
      "grv",      "grove",
      "grvs",     "groves",
      "gtway",    "gateway",
      "gtwy",     "gateway",
      "harb",     "harbor",
      "harbr",    "harbor",
      "havn",     "haven",
      "hbr",      "harbor",
      "hbrs",     "harbors",
      "height",   "heights",
      "hgts",     "heights",
      "highwy",   "hwy",
      "hiway",    "hwy",
      "hl",       "hill",
      "hllw",     "hollow",
      "hls",      "hills",
      "hollows",  "hollow",
      "holw",     "hollow",
      "holws",    "hollow",
      "hrbor",    "harbor",
      "ht",       "heights",
      "hts",      "heights",
      "hvn",      "haven",
      "hway",     "hwy",
      "ia",       "viaduct",
      "inlt",     "inlet",
      "is",       "island",
      "isles",    "isle",
      "islnd",    "island",
      "iss",      "islands",
      "jct",      "junction",
      "jction",   "junction",
      "jctn",     "junction",
      "jctns",    "junctions",
      "jcts",     "junctions",
      "junctn",   "junction",
      "juncton",  "junction",
      "knl",      "knoll",
      "knls",     "knolls",
      "knol",     "knoll",
      "ky",       "key",
      "kys",      "keys",
      "la",       "lane",
      "lanes",    "lane",
      "lck",      "lock",
      "lcks",     "locks",
      "ldg",      "lodge",
      "ldge",     "lodge",
      "lf",       "loaf",
      "lgt",      "light",
      "lgts",     "lights",
      "lk",       "lake",
      "lks",      "lakes",
      "ln",       "lane",
      "lndg",     "landing",
      "lndng",    "landing",
      "lodg",     "lodge",
      "loops",    "loop",
      "mdw",      "meadow",
      "mdws",     "meadows",
      "medows",   "meadows",
      "missn",    "mission",
      "ml",       "mill",
      "mls",      "mills",
      "mnr",      "manor",
      "mnrs",     "manors",
      "mnt",      "mount",
      "mntain",   "mountain",
      "mntn",     "mountain",
      "mntns",    "mountains",
      "mountin",  "mountain",
      "msn",      "mission",
      "mssn",     "mission",
      "mt",       "mount",
      "mtin",     "mountain",
      "mtn",      "mountain",
      "mtns",     "mountains",
      "mtwy",     "motorway",
      "nck",      "neck",
      "opas",     "overpass",
      "orch",     "orchard",
      "orchrd",   "orchard",
      "ovl",      "oval",
      "park",     "parks",
      "parkwy",   "parkway",
      "paths",    "path",
      "pikes",    "pike",
      "pk",       "park",
      "pkway",    "parkway",
      "pkwy",     "parkways",
      "pkwys",    "parkways",
      "pky",      "parkway",
      "pl",       "place",
      "plaines",  "plains",
      "pln",      "plain",
      "plns",     "plains",
      "plz",      "plaza",
      "plza",     "plaza",
      "pne",      "pine",
      "pnes",     "pines",
      "pr",       "prairie",
      "prarie",   "prairie",
      "prk",      "park",
      "prr",      "prairie",
      "prt",      "port",
      "prts",     "ports",
      "psge",     "passage",
      "pt",       "point",
      "pts",      "points",
      "rad",      "radial",
      "radiel",   "radial",
      "radl",     "radial",
      "ranches",  "ranch",
      "rdg",      "ridge",
      "rdge",     "ridge",
      "rdgs",     "ridges",
      "rds",      "roads",
      "riv",      "river",
      "rivr",     "river",
      "rnch",     "ranch",
      "rnchs",    "ranch",
      "rpd",      "rapid",
      "rpds",     "rapids",
      "rst",      "rest",
      "rte",      "route",
      "rvr",      "river",
      "shl",      "shoal",
      "shls",     "shoals",
      "shoars",   "shores",
      "shr",      "shore",
      "shrs",     "shores",
      "skwy",     "skyway",
      "smt",      "summit",
      "spg",      "spring",
      "spgs",     "springs",
      "spng",     "spring",
      "spngs",    "springs",
      "sprng",    "spring",
      "sprngs",   "springs",
      "spur",     "spurs",
      "sq",       "square",
      "sqr",      "square",
      "sqre",     "square",
      "sqrs",     "squares",
      "sqs",      "squares",
      "squ",      "square",
      "ss",       "islands",
      "st",       "street",
      "sta",      "station",
      "statn",    "station",
      "stn",      "station",
      "str",      "street",
      "stra",     "stravenue",
      "strav",    "stravenue",
      "strave",   "stravenue",
      "straven",  "stravenue",
      "stravn",   "stravenue",
      "streme",   "stream",
      "strm",     "stream",
      "strt",     "street",
      "strvn",    "stravenue",
      "strvnue",  "stravenue",
      "sts",      "streets",
      "sumit",    "summit",
      "ter",      "terrace",
      "terr",     "terrace",
      "tpk",      "turnpike",
      "tpke",     "turnpike",
      "tr",       "trail",
      "traces",   "trace",
      "tracks",   "track",
      "trails",   "trail",
      "trak",     "track",
      "trce",     "trace",
      "trfy",     "trafficway",
      "trk",      "track",
      "trks",     "track",
      "trl",      "trail",
      "trls",     "trail",
      "trnpk",    "turnpike",
      "trpk",     "turnpike",
      "trwy",     "throughway",
      "tunel",    "tunnel",
      "tunl",     "tunnel",
      "tunls",    "tunnel",
      "tunnels",  "tunnel",
      "tunnl",    "tunnel",
      "turnpk",   "turnpike",
      "un",       "union",
      "uns",      "unions",
      "upas",     "underpass",
      "vally",    "valley",
      "vdct",     "viaduct",
      "via",      "viaduct",
      "viadct",   "viaduct",
      "vill",     "village",
      "villag",   "village",
      "villg",    "village",
      "vis",      "vista",
      "vist",     "vista",
      "vl",       "ville",
      "vlg",      "village",
      "vlgs",     "villages",
      "vlly",     "valley",
      "vly",      "valley",
      "vlys",     "valleys",
      "vst",      "vista",
      "vsta",     "vista",
      "vw",       "view",
      "vws",      "views",
      "walk",     "walks",
      "wl",       "well",
      "wls",      "wells",
      "wy",       "way",
      "xing",     "crossing",
      "xrd",      "crossroad"

  });

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "2 HWY E & 418 AVE E",   "50.6739928, -113.8691339",
      "100-72132 594 AVE E",   "50.5217056, -113.8836797",
      "2140 466 AVE E",        "50.6351678, -113.9864622",
      "434243 2 HWY E",        "50.6594800, -113.8396003"
  });
}
