package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.SmartAddressParser;
import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;



public class NYSuffolkCountyAParser extends SmartAddressParser {

  public NYSuffolkCountyAParser() {
    super(CITY_TABLE, "SUFFOLK COUNTY", "NY");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupSaintNames("ANNES", "ANDREWS", "JAMES", "JOHN", "JOSEPHS", "LUKES");
    setupDoctorNames("KAHN", "HSU", "KAMDAR", "KLEINER", "PATEL'S", "SINGH");
    removeWords("LA", "-");
  }

  @Override
  public String getFilter() {
    return "paging@scfres.com,printing@scfres.com,@communityamb.org,FRES CAD,6316640853@pm.sprint.com,6318487034@vzwpix.com,6316640853@vzwpix.com,8449810245,44444,81777";
  }

  private static final Pattern SIG_3_PTN = Pattern.compile("SIG 3: *(.*) (\\d{1,2}-[A-Z]-\\d{1,2}[A-Z]?)");
  private static final Pattern SIG_3_PLACE_ADDR_PTN = Pattern.compile("(.*?) (?:@(.*) )?: (.*)");
  private static final Pattern SUFFOLK_E_MARKER = Pattern.compile("(?:/[A-Z ]*RELAY */|(?:FROM )?RELAY )");

  private static final Pattern DOUBLE_LOC_PTN = Pattern.compile("(.* LOC: .*) LOC:(?!.*CROSS:)(.*)");
  private static final Pattern VIP_PTN = Pattern.compile(" +\\*+_VIP_\\*+[: ]+");
  private static final String[] KEYWORDS = new String[]{"TYPE", "LOC", "CROSS", "CODE", "TIME", "EVENT#", "AGENCY"};
  private static final Pattern CALL_ADDR_SPLIT_PTN = Pattern.compile(" +: +| {2,}");
  private static final Pattern APT_PTN = Pattern.compile("(.*?)[,: ]+(?:APT|ROOM|UNIT|STE\\b|SUITE|#)(?!S) *#?([-A-Z0-9]+?)[- ]*");
  private static final Pattern PLACE_MARK_PTN = Pattern.compile(": ?@|@|:|;");
  private static final Pattern ADDR_CROSS_PTN = Pattern.compile("(.*?)[,: ]+(?:[SC]/S(?: ?=)?|X-|CX )(.*?)(?:\\.{2,} *(.*))?");
  private static final Pattern SPECIAL_PTN = Pattern.compile("(.*)(\\*\\*\\*_[_A-Z]+_\\*\\*\\*):?(.*)");
  private static final Pattern TRAIL_MARK_PTN = Pattern.compile(" : *@?");
  private static final Pattern TRAIL_APT_PTN = Pattern.compile("#?(\\d+[A-Z]?|[A-Z])");
  private static final Pattern TRAIL_CALL_PTN = Pattern.compile("#[A-Z0-9]*\\d{3}_(.*)");

  private String originalCall = null;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // Anything starting with 3 asterisks is the similar but different (B) variant
    if (body.startsWith("***")) return false;

    // Drop anything that might be a E format
    if (subject.contains("FROM RELAY") || SUFFOLK_E_MARKER.matcher(body).lookingAt()) return false;

    body = stripFieldStart(body, "paging@scfres.com/");
    body = stripFieldStart(body, "FWD:");

    // Brentwood FD wraps their alert in some HTML text that needs to be stripped out
    if (body.startsWith("<HEAD>")) {
      int pt = body.indexOf("TYPE:");
      if (pt < 0) return false;
      body = body.substring(pt);
      pt = body.indexOf('\n');
      if (pt >= 0) body = body.substring(0,pt).trim();
    }

    int pt = body.indexOf("\nText stop");
    if (pt >= 0) body = body.substring(0,pt).trim();

    // Check for new SIG 3: format
    Matcher match = SIG_3_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("CALL PLACE ADDR APT CITY CODE");
      body = match.group(1).trim();
      data.strCode = match.group(2);
      match = SIG_3_PLACE_ADDR_PTN.matcher(body);
      if (match.matches()) {
        data.strCall = match.group(1).trim();
        data.strPlace = getOptGroup(match.group(2));
        parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, match.group(3).trim(), data);
      } else {
        parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, body, data);
      }
    }

    else {

      // Regular format
      setFieldList("CALL ADDR CITY PLACE APT X CODE INFO TIME ID SRC");
      boolean good = body.startsWith("TYPE:");
      if (!good) body = "TYPE:" + body;

      // Double LOC: keyword should change to a CROSS: keyword
      match = DOUBLE_LOC_PTN.matcher(body);
      if (match.matches()) body = match.group(1) + " CROSS:" + match.group(2);

      body = VIP_PTN.matcher(body).replaceAll(" ");
      body = body.replace(" XST ", " CROSS: ");

      pt = body.indexOf("CROSS:");
      if (pt >= 0) {
        pt += 6;
        body = body.substring(0,pt)+body.substring(pt).replace(" CROSS: ", " / ");
      }

      Properties props = parseMessage(body, KEYWORDS);

      data.strCall = props.getProperty("TYPE");
      if (data.strCall == null) return false;

      data.strCross = stripFieldEnd(props.getProperty("CROSS", ""), "/");;

      String sAddress = props.getProperty("LOC");
      if (sAddress != null) sAddress = stripFieldEnd(sAddress, ",");
      if (sAddress == null || sAddress.startsWith("/")) {
        if (sAddress != null) {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, sAddress.substring(1).trim(), data);
          data.strPlace = getLeft();
          sAddress = null;
        }
        if (data.strCross.length() > 0) {
          parseAddress(data.strCross, data);
          data.strCross = "";
        } else {
          if (!good) return false;
          match = CALL_ADDR_SPLIT_PTN.matcher(data.strCall);
          if (match.find()) {
            sAddress = data.strCall.substring(match.end());
            data.strCall = data.strCall.substring(0,match.start());
          } else {
            String sTmp = data.strCall;
            data.strCall = "";
            parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, sTmp, data);
          }
        }
      }
      if (sAddress != null) {

        pt = sAddress.indexOf("LL(");
        if (pt >= 0) {
          int pt2 = sAddress.indexOf(')', pt+3);
          if (pt2 >= 0) {
            String city = sAddress.substring(0,pt).trim();
            if (city.length() > 0) {
              parseAddress(StartType.START_PLACE, FLAG_ONLY_CITY | FLAG_ANCHOR_END, city, data);
            }
            data.strAddress = sAddress.substring(pt,pt2+1);
            String place = stripFieldStart(stripFieldStart(sAddress.substring(pt2+1).trim(), ":"), "@");
            data.strPlace = append(data.strPlace, " - ", place);
          }
        }

        else {
          match = APT_PTN.matcher(sAddress);
          if (match.matches()) {
            sAddress = match.group(1).trim();
            data.strApt = match.group(2).trim();
          }

          match = ADDR_CROSS_PTN.matcher(sAddress);
          if (match.matches()) {
            sAddress = match.group(1).trim();
            data.strCross = append(match.group(2).trim(), " / ", data.strCross);
            parsePlaceField(getOptGroup(match.group(3)), data, false);
          }

          match = SPECIAL_PTN.matcher(sAddress);
          if (match.matches()) {
            sAddress = match.group(1).trim();
            data.strSupp = append(match.group(2).trim(), "\n", match.group(3).trim());
          }
          String[] addrFlds = PLACE_MARK_PTN.split(sAddress, 3);
          if (addrFlds.length > 1) {
            sAddress = addrFlds[0].trim();
            String city = CITY_TABLE.getProperty(sAddress);
            if (city != null) {
              data.strCity = city;
              sAddress = addrFlds[1].trim();
            } else {
              parsePlaceField(addrFlds[1].trim(), data, true);
            }
            if (addrFlds.length > 2) data.strSupp = append(data.strSupp, "\n", addrFlds[2].trim());
            match = APT_PTN.matcher(sAddress);
            if (match.matches()) {
              sAddress = match.group(1).trim();
              data.strApt = append(match.group(2).trim(), "-", data.strApt);
            }
          }

          // If we already have a city, just parse the address
          if (!data.strCity.isEmpty()) {
            parseAddress(sAddress, data);
          }

          // Otherwise, we have so many city codes that many of them form part of legitimate
          // street names, which really messes things up.  To cut down on some of
          // the confusion, any double blank following a legitimate city code is
          // treated as the end of the address
          else {
            pt = -1;
            while (true) {
              pt = sAddress.indexOf("  ", pt+1);
              if (pt < 0) break;
              Result res = parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, sAddress.substring(0,pt));
              if (res.getCity().length() > 0) {
                res.getData(data);
                data.strPlace = append(sAddress.substring(pt+2).trim(), " - ", data.strPlace);
                break;
              }
            }
            if (data.strCity.length() == 0) {
              parseAddress(StartType.START_ADDR, FLAG_CROSS_FOLLOWS, sAddress, data);
              data.strPlace = append(getLeft(), " - ", data.strPlace);
            }
          }
        }
      }

      data.strCross = stripFieldStart(data.strCross, "/");
      data.strCross = stripFieldEnd(data.strCross, "/");

      data.strCode = props.getProperty("CODE", "");
      if (data.strCode.equals("default")) data.strCode = "";

      data.strCity = convertCodes(data.strCity, CITY_TABLE);
      String sTime = props.getProperty("TIME", "");
      match = TRAIL_MARK_PTN.matcher(sTime);
      if (match.find()) {
        parsePlaceField(sTime.substring(match.end()).trim(), data, false);
        sTime = sTime.substring(0,match.start()).trim();
      }
      if (sTime.length() > 5 && sTime.length() < 8) sTime = sTime.substring(0,5);
      if (sTime.length() >= 5) data.strTime = sTime;

      data.strCallId = props.getProperty("EVENT#", "");

      data.strSource = props.getProperty("AGENCY", "");
    }
    originalCall = data.strCall;
    if (!data.strCode.isEmpty()) {
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call != null) data.strCall = call;
    }
    return true;
  }

  private void parsePlaceField(String field, Data data, boolean reverse) {
    Matcher match2;
    if ((match2 = TRAIL_APT_PTN.matcher(field)).matches()) {
      data.strApt = append(data.strApt, "-", match2.group(1), reverse);
    } else if ((match2 = TRAIL_CALL_PTN.matcher(field)).matches()) {
      String call = match2.group(1);
      if (call.endsWith(" ##")) {
        int pt = call.indexOf(" ## ");
        data.strPlace = append(data.strPlace, " - ", call.substring(pt+4, call.length()-3).trim());
        call = call.substring(0, pt).trim();
      }
      data.strCall = append(data.strCall, " - ", call);
    } else {
      data.strPlace = append(data.strPlace, " - ", field, reverse);
    }
  }

  private String append(String field1, String connect, String field2, boolean reverse) {
    if (reverse) {
      return append(field2, connect, field1);
    } else {
      return append(field1, connect, field2);
    }
  }

  public String getOriginalCall() {
    return originalCall;
  }

  private static final StandardCodeTable CALL_CODES = new StandardCodeTable();

  static final String[] MWORD_STREET_LIST = new String[]{
      "ACRE VIEW",
      "AIR PARK",
      "AIRPORT PLAZA",
      "APAUCUCK COVE",
      "APAUCUCK POINT",
      "APPLE BLOSSOM",
      "ARTIST LAKE",
      "AUSTRIAN PINE",
      "BABE RUTH",
      "BAITING PLACE",
      "BARN SWALLOW",
      "BASKET NECK",
      "BAY 1ST",
      "BAY 2ND",
      "BAY 3RD",
      "BAY MEADOW",
      "BAY POINTE",
      "BAY SHORE",
      "BEACH PLUM",
      "BEACH VIEW",
      "BEACON FEED SERVICE",
      "BEAVER DAM",
      "BELLE HARBOR",
      "BELLE MEADE",
      "BELLE TERRE",
      "BELLOWS POND",
      "BELLPORT LANE",
      "BENNETTS POND",
      "BI COUNTY",
      "BIG CONE",
      "BIRCH CREEK",
      "BIRCH HILL",
      "BIRCH HOLLOW",
      "BIRCHWOOD PARK",
      "BISHOP MCGANN",
      "BLACK GUM TREE",
      "BLACK PINE",
      "BLUE BELL",
      "BLUE POINT",
      "BLUE RIDGE",
      "BLUE SPRUCE",
      "BLUEBERRY RIDGE",
      "BLUFF POINT",
      "BOX TREE",
      "BRANDON CREST",
      "BREEZE HILL",
      "BREEZY PINE",
      "BRETTON WOODS",
      "BRIDLE PATH",
      "BRIER HOLLOW",
      "BRISTOL DOWNS",
      "BROAD HOLLOW",
      "BROOK RUN",
      "BROWNS RIVER",
      "BRUSHY NECK",
      "BUCKS HILL",
      "CALICO TREE",
      "CAMP GROUND",
      "CAMP MINEOLA",
      "CAMP UPTON",
      "CAMP WOODBINE",
      "CANAL VIEW",
      "CAPTAIN KIDD",
      "CARLLS STRAIGHT",
      "CARMEN VIEW",
      "CEDAR BAY",
      "CEDAR GLEN",
      "CEDAR GROVE",
      "CEDAR OAKS",
      "CEDAR RIDGE",
      "CEDAR VALLEY",
      "CENTER GARDEN",
      "CENTER OAKS",
      "CENTRAL PARK",
      "CHAPEL HILL",
      "CHARM CITY",
      "CHARTER OAKS",
      "CHATHAM WOODS",
      "CHEESE HOLLOW",
      "CHERRY BLOSSOM",
      "CHESTNUT STUMP",
      "CHET SWEZEY",
      "CLAY PIT",
      "CLAY PITTS",
      "CLOVER GRASS",
      "CLUB HOUSE",
      "COACH HILL",
      "COLLEGE HILLS",
      "COLONIAL WOODS",
      "COLONY PRESERVE",
      "COPPER BEECH",
      "COUNTRY CLUB",
      "COUNTRY ESTATES",
      "COUNTRY GREENS",
      "COUNTRY SQUIRE",
      "COUNTRY VILLAGE",
      "COUNTY LINE",
      "COURT HOUSE",
      "COX CURVE",
      "COX NECK",
      "CRAIG B GARIEPY",
      "CRANE NECK",
      "CRISTI JOE",
      "CROOKED HILL",
      "CROOKED PINE",
      "CROSS MEADOW",
      "CROSS RIVER",
      "CROWN ACRES",
      "CRYSTAL BEACH",
      "CRYSTAL BROOK HOLLOW",
      "CRYSTAL BROOK",
      "CRYSTAL HILL",
      "CUBA HILL",
      "CYPRESS POINTE",
      "DARK HOLLOW",
      "DAVID OVERTON",
      "DEEP HOLE",
      "DEEP VALLEY",
      "DEER PARK",
      "DEER VALLEY",
      "DEW FLAG",
      "DIPPER POINT",
      "DIX HILLS",
      "DOVER HILL",
      "DR REED",
      "DUCK ISLAND",
      "DUCK POINT",
      "DUCK POND",
      "DUNCAN ELDER",
      "EAST GATE",
      "EAST NECK",
      "EAST POND",
      "EASTPORT MANOR",
      "EATONS NECK",
      "EDENVILLE PATCHOGUE",
      "EDMUND D PELLEGRINO",
      "ENCHANTED WOODS",
      "ETHAN ALLEN",
      "FAIRWAY VIEW",
      "FANNING LANDING",
      "FARM ACRE",
      "FARM HOUSE",
      "FD ACCESS",
      "FIDDLER CRAB",
      "FIELD DAISY",
      "FIFTH INDUSTRIAL",
      "FIFTY ACRE",
      "FIR GROVE",
      "FIRE ISLAND BEACH",
      "FIRE ISLAND",
      "FIRE ROAD",
      "FISH THICKET",
      "FLORAL PARK",
      "FLOWER HILL",
      "FLOYD BENNETT",
      "FORT SALONGA",
      "FOX HOLLOW RIDINGS",
      "FOX HOLLOW",
      "FOX POINT",
      "FRANCIS LANDAU",
      "FRANCIS MOONEY",
      "FRESH POND",
      "FROG HOLLOW",
      "FROST VALLEY",
      "GARDEN CITY",
      "GATHERING ROCKS",
      "GENERAL MCLEAN",
      "GEORGE LINK JR",
      "GEORGIA PINE",
      "GIANT OAK",
      "GIBBS POND",
      "GLEN HOLLOW",
      "GOLDEN GATE",
      "GOLF CLUB",
      "GOLF COURSE",
      "GRACE HALL",
      "GRACE PARK",
      "GRAND CENTRAL",
      "GRAND HAVEN",
      "GRAND OFFENBACH",
      "GRANT SMITH",
      "GRASSY POND",
      "GREAT COVE",
      "GREAT NECK",
      "GREAT RIVER",
      "GREEN ACRE",
      "GREEN KNOLL",
      "GREY BIRCH",
      "GRIST MILL",
      "GROVELAND PARK",
      "GULL DIP",
      "GULLY LANDING",
      "GUN CLUB",
      "HALF HOLLOW",
      "HALF MILE",
      "HALF MOON POND",
      "HALLOCK LANDING",
      "HALLS CREEK",
      "HALSEY MANOR",
      "HAMPTON VISTA",
      "HAMPTONS COURT",
      "HAPPY ACRES",
      "HARBOR BEACH",
      "HARBOR HILL",
      "HARBOR HILLS",
      "HARBOR VIEW",
      "HARBOUR POINT",
      "HARTMAN HILL",
      "HAYES HILL",
      "HEAD OF COVE",
      "HEAD OF LOTS",
      "HEAD OF THE NECK",
      "HEALTH SCIENCES",
      "HECKSCHER SPUR",
      "HELEN MARIE",
      "HIDDEN POND",
      "HIGH HILL",
      "HIGH MEADOW",
      "HILD RETH",
      "HILL DALE",
      "HILLS PARK",
      "HOLIDAY PARK",
      "HOLLOW OAK",
      "HOLLY HILL",
      "HONEY SUCKLE",
      "HORSE RACE",
      "HORSE SHOE",
      "HOT WATER",
      "HOWELLS POINT",
      "HUNT CLUB",
      "HUNTING HILL",
      "HUNTINGTON FARMS",
      "ICE POND",
      "IDLE HOUR",
      "INDIAN CLUB",
      "INDIAN HEAD",
      "INDIAN NECK",
      "INLET VIEW",
      "IRVING JOHNSON",
      "ISLAND BAY",
      "IVY COVERED",
      "IVY HILL",
      "JAMES HAWKINS",
      "JAMES NECK",
      "JANES NECK",
      "JERUSALEM HOLLOW",
      "JOHN O HARA",
      "JOHN ROE SMITH",
      "JOHN S TOLL",
      "JOHN VINCENT",
      "JOHNS NECK",
      "JOSIAH FOSTER",
      "JULIA GOLDBACH",
      "KETTLE HOLE",
      "KETTLE KNOLL",
      "KIMOGENER POINT",
      "KINGS PARK",
      "LA BONNE VIE",
      "LA RUE",
      "LA SALLE",
      "LADY JANES",
      "LAKE GROVE",
      "LAKE PARK",
      "LANDS END",
      "LAURA LEE",
      "LAUREL HILL",
      "LAUREL LAKE",
      "LAWRENCE AVIATION",
      "LITTLE EAST NECK",
      "LITTLE HARBOR",
      "LITTLE PECONIC BAY",
      "LITTLE PINE",
      "LITTLE PLAINS",
      "LITTLE TREASURE",
      "LLOYD HARBOR",
      "LOGAN HILL",
      "LONE OAK",
      "LONG BEACH",
      "LONG DRIVE",
      "LONG ISLAND",
      "LONG TREE",
      "LOOKOUT RIDGE",
      "LOUIS KOSSUTH",
      "LT JOHN A OLSEN",
      "MA BELL",
      "MAC ARTHUR",
      "MAJOR TRESCOTT",
      "MAKAMAH BEACH",
      "MANNY ROSE",
      "MANOR RUN",
      "MAPLE WING",
      "MAR KAN",
      "MARK TREE",
      "MARY ELLEN",
      "MARY LU",
      "MARY PITKIN",
      "MASTIC BEACH",
      "MAY HILL",
      "MEADOW BEACH",
      "MEADOW LARK",
      "MEADOW POND",
      "MEADOW RUE",
      "MEADOW WOOD",
      "MEETING HOUSE",
      "MIDDLE COUNTRY",
      "MIDDLE ISLAND",
      "MIDDLE LINE",
      "MIDLAND POND",
      "MILL CREEK",
      "MILL POND",
      "MILLER FARMS",
      "MILLER PLACE YAPHANK",
      "MILLER PLACE",
      "MILLER WOODS",
      "MISTY POND",
      "MOONEY POND",
      "MORICHES ISLAND",
      "MORICHES MIDDLE ISLAND",
      "MORICHES RIVERHEAD",
      "MOSS CREEK",
      "MOTTS HOLLOW RD",
      "MOUNT BEAR",
      "MOUNT BEULAH",
      "MOUNT COOK",
      "MOUNT MCKINLEY",
      "MOUNT PLEASANT",
      "MOUNT SINAI CORAM",
      "MOUNT SINAI",
      "MOUNT VERNON",
      "MOUNT WILSON",
      "MOUNTAIN RIDGE",
      "MOUNTAIN VIEW",
      "MYSTIC OAKS",
      "NEWPORT BEACH",
      "NISSEQUOGUE RIVER",
      "NORD PARK",
      "NORTHERN STATE",
      "O CONNELL",
      "OAK BLUFF",
      "OAK FOREST",
      "OAK TREE",
      "OAKDALE BOHEMIA",
      "OAKLAND HILLS",
      "OCEAN BAY",
      "OCEAN VIEW",
      "OFF MEADOW",
      "OLE JULE",
      "ORANGE TREE",
      "ORCHARD CREEK",
      "ORCHARD NECK",
      "OYSTER COVE",
      "PACIFIC DUNES",
      "PARDAM KNOLL",
      "PARK HILL",
      "PARSNIP POND",
      "PATCHOGUE HOLBROOK",
      "PATCHOGUE YAPHANK",
      "PAUMANACK VILLAGE",
      "PEBBLE BEACH",
      "PECONIC BAY",
      "PEPPERIDGE LAKE",
      "PERCY WILLIAMS",
      "PHEASANT RUN",
      "PICKET POINT",
      "PIDGEON HILL",
      "PILGRIM CENTER",
      "PINE ACRE",
      "PINE ACRES",
      "PINE AIRE",
      "PINE EDGE",
      "PINE GROVE",
      "PINE HILL",
      "PINE HILLS",
      "PINE NECK",
      "PINE OAK",
      "PINE RIDGE",
      "PINE TREE",
      "PIPE STAVE HOLLOW",
      "PLEASANT VALLEY",
      "PLEASANT VIEW",
      "PLUM TREE",
      "PLUME GRASS",
      "POINT O WOODS",
      "POLO GROUND",
      "PORT JEFFERSON",
      "PORT WASHINGTON",
      "POST OFFICE",
      "PRINCESS TREE",
      "QUAIL HOLLOW",
      "QUAIL RUN",
      "QUANTUCK BAY",
      "QUOGUE RIVERHEAD",
      "RALPH STEPHANI SENIOR",
      "RAM PASTURE",
      "RAOUL WALLENBERG",
      "RED BARN",
      "RED BRIDGE",
      "RED CREEK",
      "RED MAPLE",
      "RED ROCK",
      "REEVES BAY",
      "REGENTS PARK",
      "RHODE ISLAND",
      "RIDGE HAVEN",
      "RING NECK",
      "RIVER HEIGHTS",
      "RIVER HOLLOW",
      "ROBIN HILL",
      "ROCKY HILL",
      "ROCKY MOUNTAIN",
      "ROCKY POINT LANDING",
      "ROCKY POINT YAPHANK",
      "ROCKY POINT",
      "ROLLING ESTATES",
      "ROLLING HILL",
      "ROSE EXECUTIVE",
      "ROUND SWAMP",
      "ROW OFF BAYCREST",
      "ROYAL OAKS",
      "RUSTIC GATE",
      "RUTH CREEK",
      "RYE FIELD",
      "SADDLE COVE",
      "SADDLE HILL",
      "SADDLE RIDGE",
      "SADDLE ROCK",
      "SAGAMORE HILLS",
      "SAINT ANDREWS",
      "SAINT CHARLES",
      "SAINT GEORGE",
      "SAINT JAMES",
      "SAINT JOHNLAND",
      "SAINT JOHNS",
      "SAINT LAWRENCE",
      "SAINT LOUIS",
      "SAINT MARGARETS",
      "SAINT MARKS",
      "SAINT PAULS",
      "SAINT PETERS",
      "SAN JUAN",
      "SAND HILLS",
      "SANDY HILL",
      "SANDY HOLLOW",
      "SARAH ANNE",
      "SAW MILL",
      "SCHOOL HOUSE",
      "SCOTCH PINE",
      "SCRAGGY HILL",
      "SEA CLIFF",
      "SEA COURT",
      "SEA COVE",
      "SEAMAN NECK",
      "SEATUCK COVE",
      "SHADE TREE",
      "SHADY TREE",
      "SHANG LEE",
      "SHEEP PASTURE",
      "SHORE VIEW",
      "SHORT BEACH",
      "SILAS CARTER",
      "SILLS GULLY",
      "SILVER PINE",
      "SILVER PONDS",
      "SKI RUN",
      "SKYES NECK",
      "SLEEPY HOLLOW",
      "SMITH HAVEN",
      "SMITHTOWN POLK",
      "SORREL HILL",
      "SOUND BEACH",
      "SOUND SHORE",
      "SOUTH COUNTRY",
      "SOUTHERN STATE",
      "SPEONK RIVERHEAD",
      "SPEONK SHORE",
      "SPINNING WHEEL",
      "SPRING HOLLOW",
      "SPRING LAKE",
      "ST ANDREWS",
      "ST JAMES",
      "ST JOHN",
      "ST JOHNS",
      "STAN HAVEN",
      "STEEP BANK",
      "STONE EDGE",
      "STONY BROOK",
      "STONY HOLLOW",
      "STRATHMORE COURT",
      "STRATHMORE ON GREEN",
      "STRATHMORE RIDGE",
      "STRAWBERRY PATCH",
      "SUGAR PINE",
      "SUN HAVEN",
      "SUN VALLEY",
      "SUNKEN MEADOW",
      "SUNRISE HIGHWAY SERVICE",
      "SUNRISE HWY S BERNSTEIN",
      "SUNRISE SERVICE",
      "SUNY FARMINGDALE",
      "SWAN LAKE",
      "SWAN RIVER",
      "SWEET HOLLOW",
      "TALL OAK",
      "TALL PINES",
      "TANNERS NECK",
      "TEE BOX",
      "THE HILLS",
      "THREE SISTERS",
      "TIMBER TRAIL",
      "TORREY PINE",
      "TOWER HILL",
      "TOWN BEACH",
      "TOWN COMMONS",
      "TRADE ZONE",
      "TREE HAVEN",
      "TRIPLE OAK",
      "TULIP GROVE",
      "TUTHILL CREEK",
      "TUTHILL POINT",
      "TWIN CEDAR",
      "TWIN FORK",
      "TWIN OAKS",
      "TWIN PINE",
      "VALLEY FORGE",
      "VALLEY STREAM",
      "VALLEY VIEW",
      "VAN BUREN",
      "VAN CEDAR",
      "VAN CLEVE",
      "VANDERBILT MOTOR",
      "VERNON VALLEY",
      "VETERANS MEMORIAL",
      "VICTORY KNOLL",
      "VIEW ACRE",
      "VILLA DEST",
      "VILLAGE GREEN",
      "VILLAGE HILL",
      "VIRGINIA PINE",
      "VON HAGEN",
      "WADING RIVER HOLLOW",
      "WADING RIVER MANOR",
      "WADING RIVER",
      "WAGON WHEEL",
      "WALDEN CT OAK",
      "WALT WHITMAN",
      "WATCH HILL",
      "WATERS EDGE",
      "WEST HAMPTON",
      "WHEAT PATH",
      "WHITE BIRCH",
      "WHITE DAISY",
      "WHITE DEER",
      "WHITE OAK",
      "WHITE PINE",
      "WILD CHERRY",
      "WILLIAM FLOYD PKWY RAMP",
      "WILLIAM FLOYD",
      "WILLIS CREEK",
      "WILLOW BROOK",
      "WILLOW POND",
      "WILLOW SHADE",
      "WILLOW WOOD",
      "WIND WATCH",
      "WINDING WOOD",
      "WINGAN HAUPPAUGE",
      "WOLF HILL",
      "WOOD ROAD",
      "WOODCHUCK HOLLOW",
      "WOODHULL LANDING",
      "WOODLAND PARK",
      "WOODS EDGE",
      "WOODS END",
      "YAPHANK MIDDLE ISLAND",
      "YAPHANK WOODS"
  };

  static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN / PROBLEMS",
      "ABDOMINAL PAINS",
      "A.C.N. (AUTOMATIC CRASH NOTIFICATION)",
      "ALARMS",
      "ALARMS - CO-DET",
      "ALARMS - FIRE",
      "ALARMS - HEAT-DET",
      "ALARMS - PULLBOX",
      "ALARMS - SMOKE-DETECTOR",
      "ALARMS - SMOKE-DETECTOR ZONE-167, UNSPECIFIED",
      "ALARMS - WTRFLW",
      "ALLERGIES (REACTION) / ENVENOMATION (STING, BITE)",
      "ANIMAL BITES / ATTACKS",
      "ASSAULT / SEXUAL ASSAULT / STUN GUN",
      "BACK PAIN",
      "BACK PAIN (NON-TRAUMATIC OR NON-RECENT TRAUMA)",
      "BACKCOUNTRY RESCUE",
      "BLEEDING / LACERATIONS",
      "BRUSH FIRE",
      "BURNS (SCALDS) / EXPLOSION (BLAST)",
      "BURNS (SCALDS) / EXPLOSION (BLAST) MASTIC",
      "BURNS / EXPLOSION",
      "C.O. / INHALATION / HAZMAT / CBRN",
      "CARBON MONOXIDE / INHALATION / HAZMAT / CBRN",
      "CARDIAC ARREST",
      "CARDIAC OR RESPIRATORY ARREST / DEATH",
      "CHEST PAIN",
      "CHEST PAIN / CHEST DISCOMFORT (NON-TRAUMATIC)",
      "CHOKING",
      "CITIZEN ASSIST / SERVICE CALL",
      "CITIZEN ASSIST/SERVICE CALL",
      "CONFINED SPACE / STRUCTURE COLLAPSE",
      "CONVULSIONS / SEIZURES",
      "DIABETIC PROBLEMS",
      "DROWNING/NEAR DROWNING / DIVING/SCUBA ACCIDENT",
      "DROWNING / NEAR DROWNING / DIVING / SCUBA ACCIDENT",
      "ELECTRICAL HAZARD",
      "ELEVATOR/ESCALATOR INCIDENT",
      "ELEVATOR / ESCALATOR RESCUE",
      "EXPLOSION",
      "EXTRICATION / ENTRAPPED (MACHINERY, VEHICLE - NON-MVA)",
      "EXTRICATION/ENTRAPMENT",
      "EYE PROBLEMS / INJURIES",
      "FAINTING (NEAR)",
      "FALLS",
      "FALLS I/F/O :",
      "FUEL SPILL / FUEL ODOR",
      "GAS LEAK / GAS ODOR (NATURAL & L.P. GASES)",
      "GAS LEAKS / GAS ODOR (NATURAL / L.P.G.)",
      "GAS ODOR (NATURAL  L.P.G.)",
      "HEADACHE",
      "HEART / A.I.C.D.",
      "HEART / AICD",
      "HEAT / COLD EXPOSURE",
      "HELICOPTER LANDING",
      "INACCESSIBLE INCIDENT / OTHER ENTRAPMENTS (NON-TRAFFIC)",
      "MARINE / BOAT FIRE",
      "MARINE ASSIST",
      "MOTOR VEHICLE ACCIDENT",
      "MOTOR VEHICLE COLLISION",
      "MUTUAL AID / ASSIST OUTSIDE AGENCY",
      "ODOR (STRANGE / UNK)",
      "ODOR (STRANGE / UNKNOWN)",
      "OPEN BURNING",
      "OUTSIDE FIRE",
      "OUTSIDE TANK FIRE",
      "OVERDOSE / POISONING (INGESTION)",
      "PANDEMIC",
      "PREGNANCY / CHILDBIRTH / MISCARRIAGE",
      "PSYCHIATRIC / ABNORMAL BEHAVIOR / SUICIDE",
      "PSYCHIATRIC / ABNORMAL BEHAVIOR / SUICIDE ATTEMPT",
      "RESPIRATORY",
      "RESPIRATORY / BREATHING PROBLEMS",
      "RESPIRATORY DIFFICULTY",
      "SEIZURES",
      "SERVICE CALL",
      "SICK",
      "SINKING VEHICLE / VEHICLE IN FLOODWATER",
      "SMOKE INVESTIGATION (OUTSIDE)",
      "SMOKE INVEST (OUTSIDE)",
      "STABBING / GUNSHOT / PENETRATING TRAUMA",
      "STROKE (CVA) / (TIA)",
      "STROKE (CVA) / TRANSIENT ISCHEMIC ATTACK (TIA)",
      "STRUCTURE FIRE",
      "STRUCTURE FIRE - PULLBOX",
      "SUSPICIOUS PACKAGE (LETTER, ITEM, SUBSTANCE) / EXPLOSIVES",
      "TEST",
      "TEST STRUCTURE FIRE",
      "TRAFFIC / TRANSPORTATION INCIDENTS",
      "TRAFFIC / TRANSPORTATION INCIDENTS JAMES",
      "TRAFFIC / TRANSPORTATION INCIDENTS MIDDLE",
      "TRAUMATIC INJ",
      "TRAUMATIC INJURY (SPECIFIC)",
      "UNCONSCIOUS / FAINTING (NEAR)",
      "UNK PROBLEM (PERSON DOWN)",
      "UNKNOWN PROBLEM",
      "UNKNOWN PROBLEM (PERSON DOWN)",
      "VEHICLE FIRE",
      "WATER / ICE / MUD RESCUE",
      "WATERCRAFT IN DISTRESS / COLLISION",
      "WATERCRAFT IN DISTRESS / COLLISION NICOLL ISLAND/CONNETQUOT RIVER",
      "WATER RESCUE / SINKING VEHICLE / VEHICLE IN FLOODWATER",
      "WORKING STRUCTURE FIRE"
  );

  static final Properties CITY_TABLE = buildCodeTable(new String[]{
      "AMAGAN",  "AMAGANSETT",
      "AMITYV",  "AMITYVILLE",
      "AQUEBO",  "AQUEBOGUE",
      "ASHARO",  "ASHAROKEN",
      "ATLANT",  "ATLANTIQUE",
      "BABYLO",  "BABYLON",
      "BAITIH",  "BAITING HOLLOW",
      "BARRET",  "BARRET HOLLOW",
      "BAYPOR",  "BAYPORT",
      "BAYSHO",  "BAY SHORE",
      "BAYVIL",  "BAYVILLE",
      "BAYWOO",  "BAYWOOD",
      "BELLET",  "BELLE TERRE",
      "BELLMO",  "BELLMORE",
      "BELLPO",  "BELLPORT",
      "BETHPA",  "BETHPAGE",
      "BLUEPB",  "BLUE POINT BEACH",
      "BLUEPO",  "BLUE POINT",
      "BOHEMI",  "BOHEMIA",
      "BRENTW",  "BRENTWOOD",
      "BRIDGE",  "BRIDGEHAMPTON",
      "BRIGHT",  "BRIGHTWATERS",
      "BROOKH",  "BROOKHAVEN",
      "CALVER",  "CALVERTON",
      "CAPTRE",  "CAPTREE",
      "CENTEM",  "CENTER MORICHES",
      "CENTPO",  "CENTERPORT",
      "CENTRE",  "CENTEREACH",
      "CENTRI",  "CENTRAL ISLIP",
      "CHERRG",  "CHERRY GROVE",
      "COLDSH",  "COLD SPRING HARBOR",
      "COMMAC",  "COMMACK",
      "COPIAG",  "COPIAGUE",
      "CORAM",   "CORAM",
      "CORNEE",  "CORNEILLE ESTATES",
      "CUTCHO",  "CUTCHOGUE",
      "DAVISP",  "DAVIS PARK",
      "DEERPA",  "DEER PARK",
      "DERINH",  "DERING HARBOR",
      "DIXHIL",  "DIX HILLS",
      "DUNEWO",  "DUNEWOOD",
      "EASTPO",  "EASTPORT",
      "EATONN",  "EATONS  NECK",
      "EFARMI",  "EAST FARMINGDALE",
      "EFIREI",  "EAST FIRE ISLAND",
      "EHAMPT",  "EAST HAMPTON",
      "EHAMPV",  "EAST HAMPTION VILLAGE",
      "EISLIP",  "EAST ISLIP",
      "ELWOOD",  "ELWOOD",
      "EMARIO",  "EAST MARION",
      "EMORIC",  "EAST MORICHES",
      "ENORTH",  "EAST NORTHPORT",
      "EPATCH",  "EAST PATCHOGUE",
      "EQUOGU",  "EAST QUOGUE",
      "ESHORE",  "EAST SHOREHAM",
      "FAIRHA",  "FAIR HARBOR",
      "FARMDA",  "FARMINGDALE",
      "FARMVI",  "FARMINGVILLE",
      "FINS",    "FI NATIONAL SEASHORE",
      "FIREIP",  "FIRE ISLAND PINES",
      "FISHEI",  "FISHER ISLAND",
      "FLANDE",  "FLANDERS",
      "FORTSA",  "FORT SALONGA",
      "GARDII",  "GARDINERS ISLAND",
      "GILGOB",  "GILGO BEACH",
      "GORDOH",  "GORDON HEIGHTS",
      "GRANGI",  "GRAND GULL ISLAND",
      "GREATR",  "GREAT RIVER",
      "GREENL",  "GREENLAWN",
      "GREENP",  "GREENPORT",
      "GREENW",  "GREENPORT WEST",
      "HALESI",  "HALESITE",
      "HAMPTB",  "HAMPTON BAYS",
      "HAUPPA",  "HAUPPAUGE",
      "HEADHA",  "HEAD OF THE HARBOR",
      "HICKSV",  "HICKSVILLE",
      "HOLBRO",  "HOLBROOK",
      "HOLTSV",  "HOLTSVILLE",
      "HUNTIB",  "HUNTINGTON BAY",
      "HUNTIN",  "HUNTINGTON",
      "HUNTIS",  "HUNTINGTON STATION",
      "ISLAND",  "ISLANDIA",
      "ISLIP",   "ISLIP",
      "ISLIPT",  "ISLIP TERRACE",
      "JAMESP",  "JAMESPORT",
      "JONESB",  "JONES BEACH",
      "KINGPP",  "KINGS PARK PSYCHIATRIC",
      "KINGSP",  "KINGS PARK",
      "KISMET",  "KISMET",
      "LAKEGR",  "LAKE GROVE",
      "LAKERO",  "LAKE RONKONKOMA",
      "LAUREL",  "LAUREL HOLLOW",
      "LEVITT",  "LEVIGTTOWN",
      "LINDEN",  "LINDENHURST",
      "LITTGI",  "LITTLE GULL ISLAND",
      "LLOYDH",  "LLOYD HARBOR",
      "LONELY",  "LONELYVILLE",
      "MA",      "MALVERNE",
      "MANO",    "MANORVILLE",
      "MANORV",  "MANORVILLE",
      "MASSAP",  "MASSAPEQUA",
      "MASSPA",  "MASSAPEQUA PARK",
      "MASTIB",  "MASTIC BEACH",
      "MASTIC",  "MASTIC",
      "MATTIT",  "MATTITUCK",
      "MEDFOR",  "MEDFORD",
      "MELVIL",  "MELVILLE",
      "MIDDLI",  "MIDDLE ISLAND",
      "MILLEP",  "MILLER PLACE",
      "MILLNE",  "MILL NECK",
      "MONTAU",  "MO NTAUK",
      "MORICH",  "MORICHES",
      "MOUNTS",  "MT SINAI",
      "MSPK",    "MATTITUCK",
      "NAMITY",  "NORTH AMITYVILLE",
      "NAPEAG",  "NAPEAGUE",
      "NBABYL",  "NORTH BABYLON",
      "NBAYSH",  "NORTH BAY SHORE",
      "NBELLP",  "NORTH BELLPORT",
      "NESCON",  "NESCONSET",
      "NEWSUF",  "NEW SUFFOLK",
      "NGREAR",  "NORTH GREAT RIVER",
      "NHAVEN",  "NORTH HAVEN",
      "NISSEQ",  "NISSEQUOGUE",
      "NLINDE",  "NORTH LINDENHURST",
      "NORTHP",  "NORTHPORT",
      "NORTHV",  "NORTHVILLE",
      "NORTVH",  "NORTHPORT VA",
      "NOYACK",  "NOYACK",
      "NPATCH",  "NORTH PATCHOGUE",
      "NSEA",    "NORTH SEA",
      "NWHARB",  "NORTH WEST HARBOR",
      "OAKBEA",  "OAK BEACH",
      "OAKDAL",  "OAKDALE",
      "OAKISL",  "OAK ISLAND",
      "OAKLEY",  "OAKLEYVILLE",
      "OCEABE",  "OCEAN BEACH",
      "OCEABP",  "OCEAN BAY PARK",
      "OCEANR",  "OCEAN RIDGE",
      "OLDBET",  "OLD BETHPAGE",
      "OLDFIE",  "OLD FIELD",
      "ORIENT",  "ORIENT",
      "OYSTERB", "OYSTER BAY",
      "PATCH",   "PATCHOGUE",
      "PATCHO",  "PATCHOGUE",
      "PECONI",  "PECONIC",
      "PILGRP",  "PILGRIM PSYCHIATRIC",
      "PLAINV",  "PLAINVIEW",
      "PLUMIS",  "PLUM ISLAND",
      "POINTO",  "POINT O’WOODS",
      "POOSIR",  "POOSPATUCK INDIAN RESERVATION",
      "POQUOT",  "POQUOTT",
      "PORTJE",  "PORT JEFFERSON",
      "PORTJS",  "PORT JEFFERSON STATION",
      "QUIOGU",  "QUIOGUE",
      "QUOGUE",  "QUOGUE",
      "REMSEN",  "REMSENBURG",
      "REMSES",  "REMSENBURG-SPEONK",
      "RIDGE",   "RIDGE",
      "RIVERH",  "RIVERHEAD",
      "RIVERS",  "RIVERSIDE",
      "ROBBII",  "ROBBINS ISLAND",
      "ROBBIR",  "ROBBINS REST",
      "ROBERM",  "ROBERT MOSES PARK",
      "ROCKYP",  "ROCKY POINT",
      "RONKON",  "RONKONKOMA",
      "SAGAPO",  "SAGAPONACK",
      "SAHGAR",  "SAG HARBOR",
      "SAILOH",  "SAILORS HAVEN",
      "SAINTJ",  "ST JAMES",
      "SALTAI",  "SALTAIRE",
      "SAYVIL",  "SAYVILLE",
      "SEAFOR",  "SEAFORD",
      "SEAVIE",  "SEAVIEW",
      "SELDEN",  "SELDEN",
      "SETAES",  "SETAKET-EAST SETAUKET",
      "SHELIH",  "SHELTHER ISLAND HEIGHTS",
      "SHELTI",  "SHELTER ISLAND",
      "SHINIR",  "SHINNECOCK INDIAN RESERVATION",
      "SHINNH",  "SHINNECOCK HILLS",
      "SHIRLE",  "SHIRLEY",
      "SHOREH",  "SHOREHAM",
      "SHUNTI",  "SOUTH HUNTINGTON",
      "SMITHP",  "SMITH POINT",
      "SMITHT",  "SMITHTOWN",
      "SOUNDB",  "SOUND BEACH",
      "SOUTHA",  "SOUTHAMPTON",
      "SOUTHO",  "SOUTHOLD",
      "SOUTHV",  "SOUTHAMPTON VILLAGE",
      "SPEONK",  "SPEONK",
      "SPRING",  "SPRINGS",
      "STONYB",  "STONY BROOK",
      "SUFFDC",  "SUFFOLK DEVELOPMENTAL CENTER",
      "SUNKEF",  "SUNKEN FOREST",
      "SUNYFA",  "SUNY FARMINGDALE",
      "SUNYSB",  "SUNY STONY BROOK",
      "SYOSSE",  "SYOSSET",
      "TALISM",  "TALISMAN",
      "TERRYV",  "TERRYVILLE",
      "TOBAY",   "TOBAY",
      "UPTON",   "UPTON",
      "USCGST",  "US COAST GUARD STATION",
      "VILLAB",  "VILLAGE OF THE BRANCH",
      "WADINR",  "WADING RIVER",
      "WAINSC",  "WAINSCOTT",
      "WANTAG",  "WANTAGH",
      "WATCHH",  "WATCH HILL",
      "WATERI",  "WATER ISLAND",
      "WATERM",  "WATER MILL",
      "WBABYL",  "WEST BABYLON",
      "WBAYSH",  "WEST BAY SHORE",
      "WFIREI",  "WEST FIRE ISLAND",
      "WHAMPB",  "WESTHAMPTON BEACH",
      "WHAMPD",  "WESTHAMPTON DUNES",
      "WHAMPT",  "WESTHAMPTON",
      "WHEATH",  "WHEATLEY HEIGHTS",
      "WHILLS",  "WEST HILLS",
      "WISLIP",  "WEST ISLIP",
      "WOODBU",  "WOODBURY",
      "WSAYVI",  "WEST SAYVILLE",
      "WYANDA",  "WYANDANCH",
      "YAPHAN",  "YAPHANK",

      "LB",      "ISLIP",
      "VS",      "VALLEY STREAM",

      "E NORTHPORT",    "E NORTHPORT",
      "SETAUKET",       "SETAUKET",
      "SO FARMINGDALE", "SOUTH FARMINGDALE"
  });
}
