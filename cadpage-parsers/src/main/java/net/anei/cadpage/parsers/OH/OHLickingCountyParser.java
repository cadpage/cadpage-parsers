package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SmartAddressParser;

public class OHLickingCountyParser extends SmartAddressParser {
  
  private static final Pattern IAR_MASTER_PTN = Pattern.compile("([-/ A-Z0-9]+)\n(.*)");
  private static final Pattern MASTER_PTN1 = Pattern.compile("([^,]*), ([ A-Za-z]+)(?:, \\d{5})?(?: #APT ([^ ]+))?(?: +\\((.*)\\)?)?");
  private static final Pattern MASTER_PTN2 = Pattern.compile("([^,]*?), ([^,/]+?)/([^,]+?), ([ A-Z]+)");
  private static final Pattern MASTER_PTN3 = Pattern.compile("[^,\\(\\)]+");

  public OHLickingCountyParser() {
    super(CITY_LIST, "LICKING COUNTY", "OH");
    setFieldList("CALL ADDR APT CITY X PLACE");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "notif@domain.com,messaging@iamresponding.com,notif@mecc911.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
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
    
    String addr;
    String addr2 = null;
    String apt = null;
    Matcher match = MASTER_PTN1.matcher(body);
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
    
    else if ((match = MASTER_PTN2.matcher(body)).matches()) {
      addr =  match.group(1).trim();
      String city1 = match.group(2).trim();
      addr2 = match.group(3).trim();
      String city2 = match.group(4).trim();
      if (city1.equals(city2)) data.strCity = city1;
    }
    
    else if (MASTER_PTN3.matcher(body).matches()){
      addr = body;
    }
    
    else return false;
    
    if (data.strCall.length() > 0) {
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
  public String adjustMapAddress(String addr) {
    return addr.replace("NORTH ST RD", "NORTH ST");
  }
  
  private static final String[] MWORD_STREET_LIST = new String[]{
    "A BAKER",
    "A ROCKINGHAM",
    "BEAVER RUN",
    "BELLE VISTA",
    "BENNINGTON CHAPEL",
    "BLUE BONNET",
    "BLUE JAY",
    "BUENA VISTA",
    "CAMBRIA MILL",
    "CANYON VILLA",
    "CARREG CAIN",
    "CHERRY VALLEY",
    "CHESTNUT HILLS",
    "CRISTLAND HILL",
    "DERBY DOWNS",
    "DOG HOLLOW",
    "DON THORP",
    "DORSEY MILL",
    "DOUGLAS LANE",
    "DRY CREEK",
    "EDGEWATER BEACH",
    "EL RANCHO",
    "FIRE HOUSE",
    "FOX CHASE",
    "HIGH POINT",
    "HONDA HILLS",
    "INDIAN MILL",
    "IRVING WICK",
    "JEFFERSON RIDGE",
    "LAKE SHORE",
    "LICKING SPRINGS",
    "LICKING TRAIL",
    "LOUDON STREET",
    "LUNDYS LANE",
    "MCKINNEY CROSSING",
    "MILL DAM",
    "MISTY MEADOWS",
    "MORGAN CENTER",
    "MT VERNON",
    "NORTH BANK",
    "NORTH ST",
    "NORTH VILLAGE",
    "PHIL LINN",
    "PINE BLUFF",
    "PINEWOOD TRAIL",
    "PLEASANT CHAPEL",
    "PLEASANT VIEW",
    "QUAIL CREEK",
    "RAIN ROCK",
    "RAMP COLUMBUS",
    "RED STONE",
    "RIDGELY TRACT",
    "ROCKY FORK",
    "ROCKY RIDGE",
    "ROSE VIEW",
    "SADIE THOMAS",
    "SHARON VALLEY",
    "SHARON VIEW",
    "SHERWOOD DOWNS",
    "SLEEPY HOLLOW",
    "SMITHS MILL", 
    "SOUTH BANK",
    "SOUTH FORK",
    "SPORTSMAN CLUB",
    "SPRING VALLEY",
    "ST CLAIR",
    "ST JOSEPH",
    "STONE CREEK",
    "STONE HOUSE",
    "TERRY LINN",
    "UNION STATION",
    "VALLEY VIEW",
    "WELSH HILLS",
    "WESLEYAN CHURCH",
    "WEST VIEW",
    "WESTLEY CHAPEL",
    "WHITE CHAPEL"


    
  };
  
  private static final CodeSet CALL_LIST = new CodeSet(
      "ABDOMINAL PAIN-EMS",
      "ALARM COMMERCIAL FIRE-FIRE",
      "ALARM HIGH LIFE / VALUE-FIRE",
      "ALARM MEDICAL-EMS",
      "ALARM RESIDENTIAL FIRE-FIRE",
      "ALARM WATERFLOW-FIRE",
      "ALLERGIC REACTION-EMS",
      "ASSAULT-EMS",
      "ATTEMPT SUICIDE-EMS",
      "ATTEMPT THREAT-EMS",
      "BEHAVIORAL EMERGENCY-EMS",
      "BITE ANIMAL / HUMAN-EMS",
      "BURNS-EMS",
      "CHEST PAIN",
      "CHEST PAIN-EMS",
      "CHILDBIRTH / OB-EMS",
      "CO ALARMS / CHECK-FIRE",
      "BREATHING PROBLEMS-EMS",
      "DIABETIC PROBLEMS-EMS",
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
      "HEART PROBLEMS-EMS",
      "HEMORRHAGE-EMS",
      "ILLNESS",
      "ILLNESS-EMS",
      "INJURY-EMS",
      "INVESTIGATION/SERVICE RUN-FIRE",
      "LOCK IN-FIRE",
      "LOCK OUT-FIRE",
      "NATURAL GAS LEAK",
      "NATURAL GAS LEAK-FIRE",
      "NATURAL GAS ODOR OUTSIDE-FIRE",
      "NON BREATHER / ARREST-EMS",
      "OVERDOSE-EMS",
      "PERSONAL ASSIST-EMS",
      "PERSON DOWN-EMS",
      "SEIZURE-EMS",
      "SERVICE RUN",
      "SHOOTING-EMS",
      "STROKE / CVA-EMS",
      "TRAFFIC ACCIDENT",
      "TRAFFIC ACCIDENT-EMS",
      "TRAFFIC ACCIDENT HIGH SPEED / ENTRAPMENT-EMS",
      "TRAFFIC ACCIDENT PEDESTRIAN STRUCK-EMS",
      "TRAFFIC ACCIDENT VEHICLE IN STRUCTURE-EMS",
      "TRAFFIC/TRANSPORTATION ACCIDENT 131",
      "UNCONSCIOUS PERSON-EMS",
      "UNKNOWN EMERGENCY-EMS",
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
