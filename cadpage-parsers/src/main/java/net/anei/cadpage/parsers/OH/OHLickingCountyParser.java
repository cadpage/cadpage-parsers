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
          "SequenceNumber:ID! Nature:CALL! Talkgroup:CH! FreeFormatAddress:ADDR! AddressType:SKIP! Business:PLACE! CAD_Zone:MAP! Notes:INFO/N+");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "notif@domain.com,messaging@iamresponding.com,notif@mecc911.org,WASST@MIFFLIN-OH.GOV";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    body = body.replace("<tr>", "<br>");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    if (body.startsWith("SequenceNumber:")) {
      return parseFields(body.split("\n+"), data);
    }

    setFieldList("CALL ADDR APT CITY X PLACE");

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
  private static final Pattern ADDR_CITY_APT_ZIP_X_PTN = Pattern.compile("([^,]*), ([ A-Za-z]+)(?:, \\d{5})?(?: #APT ([^ ]+))?(?: +\\((.*?)\\)?)?");
  private static final Pattern ADDR_CITY_INTERSECT_PTN = Pattern.compile("([^,]*?), ([^,/]+?)/([^,]+?), ([ A-Z]+)");
  private static final Pattern MASTER_PTN3 = Pattern.compile("[^,\\(\\)]+");

  private boolean parseCallAddress(boolean parseCall, String field, Data data) {
    String addr;
    String addr2 = null;
    String apt = null;
    Matcher match = ADDR_CITY_APT_ZIP_X_PTN.matcher(field);
    if (match.matches()) {
      
      addr = match.group(1).trim();
      data.strCity = match.group(2).trim();
      apt = match.group(3);
      String cross = getOptGroup(match.group(4));
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
    
    else if (MASTER_PTN3.matcher(field).matches()){
      addr = field;
    }
    
    else return false;
    
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
      parseAddress(addr, data);
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
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (!parseCallAddress(false, field, data)) abort();
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY X";
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
    }
  }
  
  
  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("NORTH ST RD", "NORTH ST");
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "BASH LANE",
    "BEAVER RUN",
    "BELL CHURCH",
    "BELLE VISTA",
    "BENNINGTON CHAPEL",
    "BLUE BONNET",
    "BLUE JAY",
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
    "CHERRY VALLEY",
    "CHESTNUT HILLS",
    "COUNTY LINE",
    "CRISTLAND HILL",
    "D KING",
    "DEER RUN",
    "DERBY DOWNS",
    "DOG HOLLOW",
    "DON THORP",
    "DORSEY MILL",
    "DOUGLAS LANE",
    "DRY CREEK",
    "DUNCAN PLAINS",
    "DUTCH LANE",
    "EDEN CHURCH",
    "EDGEWATER BEACH",
    "EL RANCHO",
    "FIRE HOUSE",
    "FLINT RIDGE",
    "FOX CHASE",
    "FOX RUN",
    "FREEMAN MEMORIAL",
    "GEORGE ICE",
    "GINGER HILL",
    "GIRL SCOUT",
    "GLYN CARIN",
    "GRACELAND LANE",
    "GREEN MEADOW",
    "HALLIE LANE",
    "HARBOR VIEW",
    "HAZEL DELL",
    "HIGH POINT",
    "HONDA HILLS",
    "HONEY CREEK",
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
    "MCKINNEY CROSSING",
    "MEGGIN MELANNE",
    "MILL DAM",
    "MISTY MEADOWS",
    "MOON RIVER",
    "MOOTS RUN",
    "MORGAN CENTER",
    "MOUNT HERMAN",
    "MT OLIVE",
    "MT VERNON",
    "NORTH BANK",
    "NORTH SHORE LANDING",
    "NORTH ST",
    "NORTH VILLAGE",
    "OAK CANYON",
    "PAINTER RUN",
    "PARADISE VALLEY",
    "PARK VIEW",
    "PHIL LINN",
    "PHILLIPS GLEN",
    "PINE BLUFF",
    "PINE VIEW",
    "PINEWOOD TRAIL",
    "PLEASANT CHAPEL",
    "PLEASANT LEE",
    "PLEASANT VIEW",
    "QUAIL CREEK",
    "RACCOON VALLEY",
    "RAIN ROCK",
    "RAMP COLUMBUS",
    "RAMP LANCASTER",
    "RANGE LINE",
    "RED STONE",
    "RIDGELY TRACT",
    "ROCK HAVEN SERVICE",
    "ROCK HAVEN",
    "ROCKY FORK",
    "ROCKY RIDGE",
    "ROLEY HILLS",
    "ROSE VIEW",
    "SADIE THOMAS",
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
    "SPRING VALLEY",
    "ST CLAIR",
    "ST JOSEPH",
    "STADDENS BRIDGE",
    "STONE CREEK",
    "STONE HOUSE",
    "STONE PILE",
    "SUNSET HILL",
    "SWICK HOLTON",
    "TERRY LINN",
    "UNION STATION",
    "VALLEY VIEW",
    "WELSH HILLS",
    "WESLEYAN CHURCH",
    "WEST BANK",
    "WEST VIEW",
    "WESTLEY CHAPEL",
    "WHISPERING PINES",
    "WHITE CHAPEL",
    "WILKINS RUN",
    "WILLOW RIDGE",
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN-EMS",
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
      "NON BREATHER / ARREST-EMS",
      "OVERDOSE-EMS",
      "PERSONAL ASSIST-EMS",
      "PERSON DOWN-EMS",
      "POLICE ASSIST-FIRE",
      "RESCUE ELEVATOR-FIRE",
      "RESCUE WATER-FIRE",
      "SEIZURE-EMS",
      "SERVICE RUN",
      "SHOOTING-EMS",
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
  };
}
