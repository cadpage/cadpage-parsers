package net.anei.cadpage.parsers.ZCABC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ZCABCVancouverIslandParser extends FieldProgramParser {

  public ZCABCVancouverIslandParser() {
    this("", "BC");
  }

  public ZCABCVancouverIslandParser(String defCity, String defState) {
    super(defCity, defState, "CALL? ADDR/ZS7C CITY DATETIME!");
    setupCallList(CALL_LIST);
    setupMultiWordStreets(MWORD_STREET_LIST);
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "paging@ni911.ca";
  }


  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 255; }
      @Override public int splitBreakPad() { return 2; }
    };
  }

  @Override
  public String getLocName() {
    if (this.getClass() == ZCABCVancouverIslandParser.class) return "Vancouver Island, BC";
    return super.getLocName();
  }

  private static final Pattern SRC_PTN = Pattern.compile("(ARRAS|BEAVER CREEK|CAMPBELL RIVER|CHERRY CREEK|CHETWYND|COWICHAN BAY|COMOX|COURTENAY|CUMBERLAND|DAWSON CREEK|DENMAN ISLAND|FANNY BAY|HORNBY ISLAND|LADYSMITH|MALAHAT|MILL BAY|MOBERLY LAKE|NORTH COWICHAN|NORTH OYSTER|OYSTER RIVER|POUCE COUPE|PT ALBERNI|PT HARDY|SOINTULA|SPROAT LAKE|TAHSIS|TAYLOR|TOFINO|TOMSLAKE|UCLUELET|UNION BAY|YUU-THLU-ILTH-AHT) *(.*)");
  private static final Pattern GPS_PTN = Pattern.compile("\\(?([-+]?[\\d:\\.]+),([-+]?[\\d:\\.]+)\\)?");
  private static final Pattern TRAIL_GPS_PTN = Pattern.compile("(.*)\\{(.*)\\}");
  private static final Pattern GPS_PTN2 = Pattern.compile("([-+]?\\d+)(\\d{6}),([-+]?\\d+)(\\d{6})");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if(!subject.equals("Fire Dispatch")) return false;

    // Clean the body of any email text
    int newLine = body.indexOf('\n');
    if(newLine >= 0) {
      body = body.substring(0, newLine);
    }

    // Strip off leading source name
    Matcher match = SRC_PTN.matcher(body);
    if (match.matches()) {
      data.strSource = match.group(1);
      body = match.group(2);
    }

    // Process trailing GPS coordinates
    match = TRAIL_GPS_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      String gps = match.group(2).replace(" ", "");
      gps = gps.replace(" ", "");
      match = GPS_PTN2.matcher(gps);
      if (match.matches()) {
        setGPSLoc(match.group(1)+'.'+match.group(2)+','+match.group(3)+'.'+match.group(4), data);
      }
    }

    // GPS coordinates contain a comma which must be escaped
    body = GPS_PTN.matcher(body).replaceAll("($1|$2)");

    String[] fields = body.split(",");
    return parseFields(fields, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " GPS";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern ADDR_GPS_PTN = Pattern.compile("(.*?) *((?:\\b[Ll]+)?\\([^\\)A-Za-z]+\\)?)");
  private static final Pattern ADDR_SPEC_PTN = Pattern.compile("(.*)\\{(.*)\\}");
  private class MyAddressField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_GPS_PTN.matcher(field);
      if (match.matches()) {
        data.strCall = append(data.strCall, " / ", match.group(1));
        data.strAddress = match.group(2).replace('|', ',');
      }

      else if ((match = ADDR_SPEC_PTN.matcher(field)).matches()) {
        data.strCall = append(data.strCall, " / ", match.group(1).trim());
        parseAddress(match.group(2).trim(), data);
      }

      else if(data.strCall.length() > 0) {
        parseAddress(field, data);
      }
      else {
        super.parse(field, data);
      }
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(BC)\\b *)?(?:(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d(?: [ap]m)?))? *(.*)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strState = getOptGroup(match.group(1));
      String date = match.group(2);
      if (date == null && data.strState.isEmpty()) return false;
      if (date != null) {
        data.strDate = date;
        String time = match.group(3);
        if (time.endsWith("m")) {
          setTime(TIME_FMT, time, data);
        } else {
          data.strTime = time;
        }
      }
      Parser p = new Parser(match.group(4));
      data.strPriority = p.getLastOptional("Alarm Level:");
      String cross = p.getLastOptional("X-ST:");
      cross = cross.replace("No X-Street", "");
      cross = stripFieldStart(cross, "/");
      cross = stripFieldEnd(cross, "/");
      data.strCross = cross;
      data.strUnit = p.get();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if(!checkParse(field, data)) super.abort();
    }

    @Override
    public String getFieldNames() {
      return "ST DATE TIME UNIT X PRI";
    }
  }

  private static final String[] MWORD_STREET_LIST = new String[]{
    "ACLE BEACH",
    "ALPINE VIEW",
    "ARBUTUS BAY",
    "AVRO ARROW",
    "BAY VIEW",
    "BEAR CAT",
    "BEAVER CK",
    "BEAVER CREEK",
    "BEAVER HARBOUR",
    "BEN HAPPNER",
    "BIG BAM",
    "BIG BEAR",
    "BLACK BEAR",
    "BLACK CREEK",
    "BLUE GROUSE",
    "BLUE JAY",
    "BOMBER BASE",
    "BOUCHER LAKE",
    "BRENTON PAGE",
    "BRIND AMOUR",
    "BUCKLEY BAY FRONTAGE",
    "BUCKLEY BAY",
    "BUENA VISTA",
    "CAMERON TAGGART",
    "CAMPBELL RIVER",
    "CAPTAIN TRISTAN DEKONINCK",
    "CENTRAL LAKE",
    "CENTRAL LK",
    "CHERRY CK",
    "CHERRY CREEK",
    "CHERRY POINT",
    "CHESTERMAN BEACH",
    "CHETWYND PULPMILL",
    "CIFIC RIM",
    "CIVIC CORE",
    "COAL HARBOUR",
    "COAST GUARD",
    "COBBLE HILL",
    "COLLEGE CAMPUS",
    "COMOX LAKE",
    "COMOX LOGGING",
    "COMOX VALLEY",
    "COUGAR SMITH",
    "COUNTRY CLUB",
    "COUNTRY PLACE",
    "COWICHAN BAY",
    "COWICHAN STATION",
    "COWICHAN VALLEY",
    "CROWN ISLAND",
    "CROWN ISLE",
    "DEER POINT",
    "DISCOVERY HARBOUR",
    "DOLLY VARDEN",
    "DOVE CREEK",
    "DUKE S",
    "ELMA BAY",
    "ESOWISTA IR",
    "FINLAYSON ARM",
    "FORBIDDEN PLATEAU",
    "FORD S",
    "FOREST GROVE",
    "FOREST LAWN",
    "GARTLEY POINT",
    "GEORGIA STRAIT",
    "GLACIER VIEW",
    "GLEN EAGLE",
    "GOLD RIVER",
    "GOLDSTREAM HEIGHTS",
    "GOOSE SPIT",
    "GORDON CALDER",
    "HAH CH AA AHT",
    "HARBOUR VIEW",
    "HARDY BAY",
    "HART WABI",
    "HATCH POINT",
    "HEAD BAY",
    "HENRY ROETHEL",
    "HIGH COURT",
    "HIGH SALAL",
    "HILLVIEW ACCESS",
    "HOLIDAY BEACH",
    "HORNE LAKE",
    "HOWARD TOM",
    "HUL NUTZ TUN",
    "HYOU SUN UP SHILTH",
    "INLAND ISLAND",
    "IRACLE BEACH",
    "IRON RIVER",
    "JACKFISH LAKE",
    "JAMES PAUL",
    "JENSEN COVE",
    "JENSENS BAY",
    "JIM CRAM",
    "KEITH WAGNER",
    "KULLEET BAY",
    "KWUNEW KWASUN",
    "KYE BAY",
    "LACEY LAKE",
    "LACY LAKE",
    "LAGUNA VISTA",
    "LAKE TRAIL",
    "LAUREL S",
    "LEA SMITH",
    "LITTLE BEAR",
    "LITTLE RIVER",
    "LITTLE TRIBUNE BAY",
    "LONE CONE",
    "LONG BEACH PARK ACCESS",
    "LONG LAKE",
    "LORNE WHITE",
    "MACKENZIE BEACH",
    "MAPLE LEAF",
    "MAPLE RIDGE",
    "MARINE VISTA",
    "MARTIN PARK",
    "MCCOY LAKE",
    "MCIVOR LAKE",
    "MEDICINE WOMAN",
    "MEDICINE WOMEN",
    "MIDDLE POINT",
    "MILE END",
    "MILL BAY",
    "MIRACLE BEACH",
    "MOBERLY DUMP",
    "MOBERLY HEIGHTS",
    "MOBERLY LAKE",
    "MOX VALLEY",
    "MT BAKER",
    "MYSTERY BEACH",
    "NANCY GREENE",
    "NELSON POINT",
    "NORTH ACCESS",
    "NORTH WATTS",
    "NORTHWIND TRAIL",
    "OCEAN GROVE",
    "OCEAN PARK",
    "OCEAN TERRACE",
    "OCEAN VIEW",
    "ORANGE POINT",
    "OYSTER BAY",
    "OYSTER GARDEN",
    "OYSTER RIVER",
    "OYSTER STO LO",
    "PACIFIC RIM",
    "PANORAMA RIDGE",
    "PAPER MILL",
    "PARK ACCESS",
    "PARK VIEW",
    "PASS OF MELFORT",
    "PIDGEON LAKE",
    "PINE RIDGE",
    "PINGEL CREEK",
    "PIONEER SQUARE",
    "PORT AGUSTA",
    "PORT ALBERNI",
    "PORT AUGUSTA",
    "PT ALBERNI",
    "R RIVER",
    "RADAR HILL",
    "RALPH HUTTON",
    "RED OAK",
    "RIP TIDE",
    "RIVER BEND",
    "ROCK BAY",
    "ROCKY MOUNTAIN",
    "ROLLIE ROSE",
    "ROY CREEK",
    "ROYSTON SEASIDE",
    "SALMON POINT",
    "SAND PINES",
    "SATELLITE PARK",
    "SEA LION",
    "SEA TERRACE",
    "SEAPLANE BASE",
    "SHAWNIGAN LAKE",
    "SHAWNIGAN MILL BAY",
    "SHELL BEACH",
    "SHINGLE SPIT",
    "SHIPS POINT",
    "SHOEMAKER BAY",
    "SHOOTING STAR",
    "ST ANDREWS",
    "ST ANN'S",
    "ST ANNS",
    "ST JACQUES",
    "ST JOHN S POINT",
    "ST JOHN'S POINT",
    "ST JOHNS POINT",
    "STAMP FALLS PROVINCIAL",
    "STIRLING ARM",
    "STZ UMINUS",
    "T BAR C",
    "TATE CREEK",
    "TAYLOR ARM",
    "THE POINT",
    "THUQ MIN",
    "TONQUIN PARK",
    "TRADING POST",
    "TRANS CANADA",
    "TRANSFER BEACH",
    "TRIBUNE BAY PROVINCIAL",
    "TSOLUM RIVER",
    "TSULQUATE IR",
    "TWIN CEDARS",
    "VALLEY VIEW",
    "VETERANS MEMORIAL",
    "VIC TURNER AIRPORT",
    "VILLAGE CONNECTER",
    "VISTA BAY",
    "WAGON WHEEL",
    "WALKER FRONTAGE",
    "WALTER GAGE",
    "WARDENS OFFICE",
    "WEST CARMICHAEL",
    "WEST FRASER",
    "WEST MOBERLY",
    "WILLIAMS BEACH",
    "WILLIAMS BEACH",
    "YELLOW POINT",
    "YEW WOOD"

  };

  private static final CodeSet CALL_LIST = new CodeSet(
      "911 ANSWER",
      "ABANDONED 911",
      "ALARMS",
      "ALARMS COMMERCIAL",
      "ALARMS HAZMAT",
      "ALARMS NON EMERGENCY",
      "ALARMS RESIDENTIAL",
      "ASSIST OTHER AGENCY",
      "AVIATION INCIDENT",
      "BEACH/BRUSH",
      "BEACH/BRUSH NON EMERG",
      "BEACH/BRUSH/MISC OUT EMERG",
      "BEACH/BRUSH/MISC OUT NON EMERG",
      "BEACH/BRUSH/MISC OUT",
      "BOMB THREAT",
      "BURNING COMPLAINT",
      "CARBON MONOXIDE NON EMERG",
      "CARBON MONOXIDE NON EMERGENCY",
      "CARBON MONOXIDE",
      "CHIMNEY FIRE",
      "DUPLICATE",
      "DUTY INVESTIGATION",
      "DUTY OFFICER",
      "ELECTRICAL TROUBLE",
      "EVACUATION",
      "FIRE MVI",
      "FIRE STRUCTURE - FIRE",
      "FIRST ALARM - A",
      "FIRST ALARM - B",
      "FIRST ALARM - C",
      "FIRST RESP A",
      "FIRST RESP ASSIST D/E",
      "FIRST RESP ASSIST EMERGENCY",
      "FIRST RESP ASSIST ROUTINE",
      "FIRST RESP B",
      "FIRST RESP C",
      "FIRST RESP D",
      "FIRST RESP DELAY B/C",
      "FIRST RESP DELAY D/E",
      "FIRST RESP E",
      "FIRST RESP RED",
      "FIRST RESP ORANGE",
      "FIRST RESP YELLOW",
      "FIRST RESP BLUE",
      "FIRST RESP PURPLE",
      "FIRST RESPONDER DELAY B/C",
      "FIRST RESPONDER DELAY D/E",
      "FIRST RESPONDER UNKNOWN",
      "FUEL - LEAK/SPILL/OTH",
      "FUEL - LEAK/SPILL/OTH NON EMER",
      "FUEL - LEAK/SPILL/OTHER NON EMERG",
      "FUEL - LEAK/SPILL/OTHER",
      "GARBAGE CONTAINER",
      "HAZMAT",
      "HAZMAT NON EMERGENCY",
      "HELP",
      "HYDRO NON-EMERGENCY",
      "HYDRO TROUBLE",
      "HYDRO TROUBLE NON EMERGENCY",
      "MARINE INCIDENT",
      "MARINE",
      "MOTOR VEHICLE ACCIDENT",
      "MOTOR VEHICLE FIRE",
      "MUTUAL AID",
      "MV FIRE",
      "MVI",
      "MVI / EXTRICATION",
      "MVI PED STRUCK",
      "MVI PORT",
      "MVI/EXTRICATION",
      "NATURAL GAS LINE BREAK",
      "NATURAL GAS/PROP NON EMERG",
      "NATURAL GAS/PROPANE EMERGENCY",
      "NATURAL GAS/PROPANE NON EMERG",
      "NATURAL GAS/PROPANE",
      "OUTDOOR FIRE",
      "PUBLIC SERVICE",
      "RESCUE -ELEVATOR",
      "RESCUE - ROAD",
      "RESCUE -CONFINED",
      "RESCUE -HIGH ANGLE",
      "RESCUE -LOW ANGLE/BCAS ASSIST",
      "RESCUE -MARINE",
      "RESCUE -SWIFT WATER",
      "RESCUE LOW ANGLE/BCAS ASSIST",
      "RESCUE - ROAD",
      "RESCUE ROAD",
      "STRUCTURE - SMOKE",
      "STRUCTURE - SMOKE(FIRE IS OUT)",
      "STRUCTURE  - FIRE",
      "STRUCTURE - ELECTRICAL TROUBLE",
      "STRUCTURE - FIRE",
      "STRUCTURE - SMOKE ODOUR",
      "STRUCTURE - SMOKE",
      "STRUCTURE FIRE",
      "STRUCTURE ODOUR",
      "STRUCTURE SMOKE",
      "TEST",
      "TRAIN DERAILMENT",
      "TSUNAMI WATCH",
      "TSUNAMI WARNING",
      "WILDLAND",
      "WILDLAND FIRE"
  );

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "HMCS QUADRA",            "+49.662330,-124.914198",
      "10696 TAYLOR ARM DR",    "+49.275430,-124.985284"

  });
}
