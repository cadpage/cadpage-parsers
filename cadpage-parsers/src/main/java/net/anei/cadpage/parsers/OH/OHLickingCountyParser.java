package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLickingCountyParser extends FieldProgramParser {

  private static final Pattern IAR_MASTER_PTN = Pattern.compile("([-/ A-Z0-9]+)\n(.*)");

  public OHLickingCountyParser() {
    super(CITY_LIST, "LICKING COUNTY", "OH",
          "SequenceNumber:ID? Nature:CALL! Talkgroup:CH! FreeFormatAddress:ADDR! AddressType:SKIP! Business:PLACE! XCoordinate:GPS1? YCoordinate:GPS2? CAD_Zone:MAP! Units:UNIT/S+ Notes:INFO/N+");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "notif@domain.com,messaging@iamresponding.com,notif@mecc911.org,WASST@MIFFLIN-OH.GOV,911dispatch@lcounty.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.replace("<tr>", "<br>").replace("<td>", "<br>");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (body.startsWith("SequenceNumber:") || body.startsWith("Nature:")) {
      return parseFields(body.split("\n+"), data);
    }

    setFieldList("CALL ADDR APT CITY PLACE X");

    do {
      if (subject.equals("Notification") && body.startsWith("? ")) {
        body = body.substring(2).trim();
        break;
      }

      if (isPositiveId()) {
        Matcher match = IAR_MASTER_PTN.matcher(body);
        if (match.matches()) {
          data.strCall = match.group(1).trim();
          body = match.group(2).trim();
          break;
        }
      }

      return false;

    } while (false);

    return parseCallAddress(data.strCall.length() == 0, body, data);
  }

  private static final Pattern BASE_ADDR_X_PTN = Pattern.compile("([^,/;]+?(?:1/2)?) *(?:\\(([^()]*)\\))?(?: *; *(.*))?");
  private static final Pattern BASE_ADDR_INTERSECT_PTN = Pattern.compile("[^,/()]+/[^,()]*");
  private static final Pattern ADDR_CITY_APT_X_ZIP_PTN = Pattern.compile("([^,]*), ([^,]*?)(?:, \\d{5})?(?: #(?:APT)? ([^ ]+))?(?: +\\(([^()]*)\\)?)?(?: +(\\d{5}))?");
  private static final Pattern NOT_LEAD_PLACE_PTN = Pattern.compile("(?:\\d|RAMP|SR\\b).*");
  private static final Pattern ADDR_CITY_INTERSECT_PTN = Pattern.compile("([^,]*?), ([^,/]+?)/([^,]+?), ([ A-Z]+)");

  private boolean parseCallAddress(boolean parseCall, String field, Data data) {

    Matcher match = BASE_ADDR_X_PTN.matcher(field);
    if (match.matches()) {
      String addr = match.group(1).trim();
      String cross = getOptGroup(match.group(2));
      cross = stripFieldStart(cross, "/");
      cross = stripFieldEnd(cross, "/");
      data.strCross = cross;
      data.strPlace = getOptGroup(match.group(3));
      StartType st = parseCall ? StartType.START_CALL : StartType.START_ADDR;
      int flags = parseCall ? FLAG_START_FLD_REQ : 0;
      addr = stripFieldEnd(addr, "#");
      parseAddress(st, flags, addr, data);
      String place = getLeft();
      int pt = place.indexOf('#');
      if (pt >= 0) {
        data.strApt = append(data.strApt, "-", place.substring(pt+1).trim());
        place = place.substring(0,pt).trim();
      }
      data.strPlace = append(place, " - ", data.strPlace);

      if (data.strCity.length() > 0 && data.strAddress.contains("&")) {
        data.strAddress = data.strAddress.replace(' ' + data.strCity + " &", " &");
      }
      return true;
    }

    if (BASE_ADDR_INTERSECT_PTN.matcher(field).matches()) {
      String addr = "";
      StartType st = parseCall ? StartType.START_CALL : StartType.START_ADDR;
      for (String part : field.split("/")) {
        part = part.trim();
        int flags = (st == StartType.START_CALL ? FLAG_START_FLD_REQ : 0);
        parseAddress(st, flags, part, data);
        st = StartType.START_ADDR;
        addr = append(addr, " & ", data.strAddress);
        data.strAddress = "";
      }
      data.strAddress = addr;
      return true;
    }

    String addr;
    String addr2 = null;
    String apt = null;
    match = ADDR_CITY_APT_X_ZIP_PTN.matcher(field);
    if (match.matches()) {

      addr = match.group(1).trim();
      data.strCity = match.group(2).trim();
      apt = match.group(3);
      String cross = getOptGroup(match.group(4));
      String zip = match.group(5);
      if (data.strCity.length() == 0) data.strCity = zip;
      int pt = cross.indexOf(';');
      if (pt >= 0) {
        data.strPlace = cross.substring(pt+1).trim();
        cross = cross.substring(0,pt).trim();
      }
      cross = stripFieldStart(cross, "/");
      cross = stripFieldEnd(cross, "/");
      data.strCross = cross;
    }

    else if ((match = ADDR_CITY_INTERSECT_PTN.matcher(field)).matches()) {
      addr =  match.group(1).trim();
      String city1 = match.group(2).trim();
      addr2 = match.group(3).trim();
      String city2 = match.group(4).trim();
      if (city1.equals(city2)) data.strCity = city1;
    }

    else return false;

    if (data.strPlace.length() == 0) {
      String city = data.strCity;
      data.strCity = "";
      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
      if (data.strCity.length() > 0) {
        data.strPlace = getLeft();
      } else {
        data.strCity = city;
      }
    }

    addr = stripFieldEnd(addr, "#");
    if (!parseCall) {
      if (addr.startsWith("@")) {
        addr = addr.substring(1).trim();
        if (addr.endsWith(")")) {
          int pt = addr.lastIndexOf("(");
          if (pt >= 0) {
            data.strPlace = append(addr.substring(0,pt).trim(), " - ", data.strPlace);
            addr = addr.substring(pt+1, addr.length()-1).trim();
          }
        }
      }
      StartType st = data.strPlace.length() > 0 ? StartType.START_ADDR : StartType.START_PLACE;
      parseAddress(st, FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
      if (data.strAddress.length() == 0 || NOT_LEAD_PLACE_PTN.matcher(data.strPlace).matches()) {
        data.strPlace = data.strAddress = "";
        parseAddress(addr, data);
      }
    } else {
      String token = null;
      if (addr.endsWith(")")) {
        int pt = addr.indexOf("(");
        if (pt >= 0) {
          token = addr.substring(pt);
          addr = addr.substring(0,pt).trim();
        }
      }
      parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_CITY | FLAG_ANCHOR_END, addr, data);
      if (token != null) data.strAddress = data.strAddress + ' ' + token;
    }
    if (addr2 != null) data.strAddress = append(data.strAddress, " & ", addr2);
    if (apt != null) data.strApt = append(data.strApt, "-", apt);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (!parseCallAddress(false, field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE X";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (data.strPlace.length() > 0 && field.length() > 0) {
        if (field.startsWith(data.strPlace) || data.strPlace.startsWith(field)) return;
        if (field.endsWith(data.strPlace) || data.strPlace.endsWith(field)) return;
      }
      super.parse(field, data);
      data.strApt = stripFieldEnd(data.strApt, field);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("DISPATCHED")) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("NORTH ST RD", "NORTH ST");
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "AULD RIDGE",
    "BASH LANE",
    "BEAVER RUN",
    "BELL CHURCH",
    "BELLE VISTA",
    "BENNINGTON CHAPEL",
    "BERGER HOLLOW",
    "BLACK HORSE",
    "BLUE BONNET",
    "BLUE JAY",
    "BRISTOL DOWNS",
    "BRUSHY FORK",
    "BRYAN ORR",
    "BRYN DU",
    "BRYN MAWR",
    "BUENA VISTA",
    "C IRVING WICK",
    "CAMBRIA MILL",
    "CAMP OHIO",
    "CANYON VILLA",
    "CARREG CAIN",
    "CEDAR RUN",
    "CHERRY BARK",
    "CHERRY BEND",
    "CHERRY BOTTOM",
    "CHERRY GROVE",
    "CHERRY VALLEY",
    "CHESTNUT HILLS",
    "CHURCHILL DOWNS",
    "CLIFTON CURTIS",
    "CONN WAY",
    "COOKS HILL",
    "COUNTY LINE",
    "CRISTLAND HILL",
    "CUMBERLAND MEADOWS",
    "CUSTERS POINT",
    "D KING",
    "DEER RUN",
    "DERBY DOWNS",
    "DOG HOLLOW",
    "DON THORP",
    "DONALD ROSS",
    "DORSEY MILL",
    "DOUGLAS LANE",
    "DRY CREEK",
    "DUNCAN PLAINS",
    "DUTCH LANE",
    "EDEN CHURCH",
    "EDGEWATER BEACH",
    "EL RANCHO",
    "FAIRFIELD BEACH",
    "FIRE HOUSE",
    "FLINT RIDGE",
    "FLOYD BOYER",
    "FOREST HILLS",
    "FOX CHASE",
    "FOX RUN",
    "FREEMAN MEMORIAL",
    "GEORGE ICE",
    "GINGER HILL",
    "GIRL SCOUT",
    "GLYN CARIN",
    "GOOSE LANE",
    "GRACELAND LANE",
    "GREEN MEADOW",
    "GREEN VALLEY",
    "HALLIE LANE",
    "HARBOR VIEW",
    "HAZEL DELL",
    "HEADLEYS MILL",
    "HIDDEN SPRINGS",
    "HIGH POINT",
    "HONDA HILLS",
    "HONEY CREEK",
    "HOPEWELL INDIAN",
    "HORNS HILL",
    "INDIAN MILL",
    "IRVING WICK",
    "JEFFERSON RIDGE",
    "JERSEY MILL",
    "JOHN ALFORD",
    "JUG RUN",
    "JUG STREET",
    "LAKE DR",
    "LAKE SHORE",
    "LAUREL HILL",
    "LEGEND LANE",
    "LEIBS ISLAND",
    "LICKING SPRINGS",
    "LICKING TRAIL",
    "LICKING VALLEY",
    "LICKING VIEW",
    "LOG POND",
    "LOOKERS LANE",
    "LOUDON STREET",
    "LUNDYS LANE",
    "MCKEE HILL",
    "MCKINNEY CROSSING",
    "MEGGIN MELANNE",
    "MIDLAND OIL",
    "MILL DAM",
    "MILL RACE",
    "MISTY MEADOW",
    "MISTY MEADOWS",
    "MOON RIVER",
    "MOOTS RUN",
    "MORGAN CENTER",
    "MOUND BUILDERS",
    "MOUNT HERMAN",
    "MT OLIVE",
    "MT PERRY",
    "MT VERNON",
    "NICHOLS LANE",
    "NORTH BANK",
    "NORTH SHORE LANDING",
    "NORTH ST",
    "NORTH VILLAGE",
    "OAK CANYON",
    "OPEN WOODS",
    "OXFORD DOWNS",
    "PAINTER RUN",
    "PARADISE VALLEY",
    "PARK VIEW",
    "PERT HILL",
    "PHIL LINN",
    "PHILLIPS GLEN",
    "PHILLIPS LANE",
    "PINE BLUFF",
    "PINE CREST",
    "PINE HILLS",
    "PINE VIEW",
    "PINEWOOD TRAIL",
    "PLEASANT CHAPEL",
    "PLEASANT LEE",
    "PLEASANT VALLEY",
    "PLEASANT VIEW",
    "QUAIL CREEK",
    "RACCOON VALLEY",
    "RAIN ROCK",
    "RAMP COLUMBUS",
    "RAMP CREEK",
    "RAMP LANCASTER",
    "RANGE LINE",
    "RED MAPLE",
    "RED STONE",
    "REDDINGTON VILLAGE",
    "RIDGELY TRACT",
    "RIVER OAKS",
    "ROCK HAVEN SERVICE",
    "ROCK HAVEN",
    "ROCK RUN",
    "ROCKY FORK",
    "ROCKY RIDGE",
    "ROLEY HILLS",
    "ROSE VIEW",
    "SADIE THOMAS",
    "SAND HOLLOW",
    "SHARON VALLEY",
    "SHARON VIEW",
    "SHELL BEACH",
    "SHERWOOD DOWNS",
    "SLEEPY HOLLOW",
    "SMITHS MILL",
    "SMOKEY ROW",
    "SOUTH BANK",
    "SOUTH FORK",
    "SPORTSMAN CLUB",
    "SPRING HILL",
    "SPRING VALLEY",
    "ST CLAIR",
    "ST JOSEPH",
    "STADDENS BRIDGE",
    "STONE CREEK",
    "STONE HOUSE",
    "STONE PILE",
    "SUNSET HILL",
    "SWICK HOLTON",
    "TAMMY LOUISE",
    "TERRY LINN",
    "UNION STATION",
    "UPSON DOWNS",
    "VALLEY VIEW",
    "VAN FOSSEN",
    "WELSH HILLS",
    "WESLEYAN CHURCH",
    "WEST BANK",
    "WEST VIEW",
    "WESTLEY CHAPEL",
    "WHISPERING PINES",
    "WHITE CHAPEL",
    "WILKINS RUN",
    "WILLOW RIDGE"
  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN-EMS",
      "ALARM COMMERCIAL / HIGH LIFE-FIRE",
      "ALARM COMMERCIAL FIRE-FIRE",
      "ALARM LIMITED RESOURCE-FIRE",
      "ALARM HIGH LIFE / VALUE-FIRE",
      "ALARM MEDICAL-EMS",
      "ALARM RESIDENTIAL FIRE-FIRE",
      "ALARM WATERFLOW-FIRE",
      "ALARMS 104",
      "ALLERGIC REACTION-EMS",
      "ANIMAL 105",
      "ASSAULT-EMS",
      "ASSIST OTHER AGENCY 107",
      "ATTEMPT JUMPER-EMS",
      "ATTEMPT SUICIDE-EMS",
      "ATTEMPT THREAT-EMS",
      "BEHAVIORAL EMERGENCY-EMS",
      "BITE ANIMAL / HUMAN-EMS",
      "BURNS-EMS",
      "CHEST PAIN",
      "CHEST PAIN-EMS",
      "CHILDBIRTH / OB-EMS",
      "CHOKING-EMS",
      "CO ALARMS / CHECK-FIRE",
      "CO POISONING-EMS",
      "BREATHING PROBLEMS-EMS",
      "DIABETIC PROBLEMS-EMS",
      "DROWNING-EMS",
      "FIRE BARN-FIRE",
      "FIRE BRUSH-FIRE",
      "FIRE CHIMNEY-FIRE",
      "FIRE COMMERCIAL STRUCTURE-FIRE",
      "FIRE HIGH LIFE / VALUE STRUCTURE-FIRE",
      "FIRE OIL WELL-FIRE",
      "FIRE OTHER / UNKNOWN-FIRE",
      "FIRE OUT-FIRE",
      "FIRE RESIDENTIAL STRUCTURE-FIRE",
      "FIRE UTILITIES-FIRE",
      "FIRE SHED / OUT BUILDING-FIRE",
      "FIRE TRASH-FIRE",
      "FUEL SPILL-FIRE",
      "HEART PROBLEMS-EMS",
      "HEMORRHAGE-EMS",
      "ILLNESS",
      "ILLNESS-EMS",
      "INJURY-EMS",
      "INVESTIGATION/SERVICE RUN-FIRE",
      "LACERATION-EMS",
      "LOCK IN-FIRE",
      "LOCK OUT-FIRE",
      "NATURAL GAS LEAK",
      "NATURAL GAS LEAK-FIRE",
      "NATURAL GAS ODOR OUTSIDE-FIRE",
      "NATURAL GAS RUPTURE-FIRE",
      "NON BREATHER / ARREST-EMS",
      "OVERDOSE-EMS",
      "PERSONAL ASSIST-EMS",
      "PERSON DOWN-EMS",
      "POLICE ASSIST-FIRE",
      "RESCUE COLLAPSE / CONFINED SPACE-FIRE",
      "RESCUE ELEVATOR-FIRE",
      "RESCUE EXTRICATION /  ENTRAPMENT-FIRE",
      "RESCUE WATER-FIRE",
      "SEIZURE-EMS",
      "SERVICE RUN",
      "SHOOTING-EMS",
      "SICK PERSON-EMS",
      "STROKE / CVA-EMS",
      "TEST CALL",
      "TRAFFIC ACCIDENT",
      "TRAFFIC ACCIDENT-EMS",
      "TRAFFIC ACCIDENT HIGH SPEED / ENTRAPMENT-EMS",
      "TRAFFIC ACCIDENT MOTORCYCLE-EMS",
      "TRAFFIC ACCIDENT PEDESTRIAN STRUCK-EMS",
      "TRAFFIC ACCIDENT VEHICLE IN STRUCTURE-EMS",
      "TRAFFIC/TRANSPORTATION ACCIDENT 131",
      "UNCONSCIOUS PERSON-EMS",
      "UNKNOWN EMERGENCY-EMS",
      "UNKNOWN (3RD PARTY) 134",
      "VEHICLE FIRE BUS-FIRE",
      "VEHICLE FIRE COMMERCIAL-FIRE",
      "VEHICLE FIRE-FIRE",
      "WIRES DOWN-FIRE",
      "WORKING FIRE COMMERCIAL-FIRE",
      "WORKING FIRE RESIDENTIAL-FIRE"
  );

  private static final String[] CITY_LIST = new String[]{

      "LICKING COUNTY",

      "ALEXANDRIA",
      "BUCKEYE LAKE",
      "GRANVILLE",
      "GRATIOT",
      "HANOVER",
      "HARTFORD",
      "HEATH",
      "HEBRON",
      "JOHNSTOWN",
      "KIRKERSVILLE",
      "NEWARK",
      "NEW ALBANY",
      "PATASKALA",
      "REYNOLDSBURG",
      "ST LOUISVILLE",
      "UTICA",

      "BENNINGTON TWP",
      "BOWLING GREEN TWP",
      "BURLINGTON TWP",
      "EDEN TWP",
      "ETNA TWP",
      "FALLSBURY TWP",
      "FRANKLIN TWP",
      "GRANVILLE TWP",
      "HANOVER TWP",
      "HARRISON TWP",
      "HARTFORD TWP",
      "HOPEWELL TWP",
      "JERSEY TWP",
      "LIBERTY TWP",
      "LICKING TWP",
      "MADISON TWP",
      "MARY ANN TWP",
      "MCKEAN TWP",
      "MONROE TWP",
      "NEWARK TWP",
      "NEWTON TWP",
      "PERRY TWP",
      "ST ALBANS TWP",
      "UNION TWP",
      "WASHINGTON TWP",

      "BEECHWOOD TRAILS",
      "GRANVILLE SOUTH",
      "HARBOR HILLS",
      "BROWNSVILLE",
      "ETNA",
      "HOMER",
      "JACKSONTOWN",

      // Coshocton County
      "COSHOCTON COUNTY",
      "PIKE TWP",

      // Delaware County
      "HARLEM TWP",
      "DELAWARE COUNTY",
      "PORTER TWP",
      "TRENTON TWP",

      // Fairfield County
      "FAIRFIELD COUNTY",
      "BALTIMORE",
      "COLUMBUS",
      "PICKERTON",
      "THURSTON",
      "WINCHESTER",
      "MILLERSPORT",
      "LIBERTY TWP",
      "VIOLET TWP",
      "WALNUT TWP",

      // Franklin County
      "FRANKLIN COUNTY",
      "JEFFERSON",
      "FRANKLIN",
      "NEW ALBANY",
      "JEFFERSON TWP",
      "PLAIN TWP",

      // Knox County
      "KNOX COUNTY",
      "CENTERBURG",
      "MARTINSBURG",
      "CLAY TWP",
      "HILLIAR TWP",
      "JACKSON TWP",
      "MARTINSBURG TWP",
      "MILFORD TWP",
      "MILLER TWP",

      // Muskingum County
      "MUSKINGUM COUNTY",
      "FRAZEYSBURG",
      "JACKSON TWP",
      "LICKING TWP",
      "HOPEWELL TWP",

      // Perry County
      "PERRY COUNTY",
      "GLENFORD",
      "THORNVILLE",
      "HOPEWELL TWP",
      "MADISON TWP",
      "THORN TWP"
  };
}
