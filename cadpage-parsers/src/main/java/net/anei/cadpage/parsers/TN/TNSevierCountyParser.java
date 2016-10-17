package net.anei.cadpage.parsers.TN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchSouthernParser;

public class TNSevierCountyParser extends DispatchSouthernParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(\\d{4}-\\d{7}); [A-Z0-9]+\\(DP\\).*");
  
  public TNSevierCountyParser() {
    super(CITY_LIST, "SEVIER COUNTY", "TN",
          "CANCEL? ADDR X/Z? ID! TIME? CALL/SDS INFO");
    removeWords("PARKWAY");
    setupSpecialStreets("NBOUND SPUR", "PARKWAY");
    setupMultiWordStreets(
        "APPLE VALLEY",
        "AUTUMN OAKS",
        "AUTUMN RIDGE",
        "BARN DOOR",
        "BASKINS CREEK",
        "BEACH VIEW",
        "BEAR CAMP",
        "BEAR CROSSING",
        "BEECH BRANCH",
        "BERRY CLARK",
        "BERRY TRAIL",
        "BIG BEAR RIDGE",
        "BIG BUCK",
        "BIRD RIDGE",
        "BIRDS CREEK",
        "BLACK BEAR CUB",
        "BLACK BEAR RIDGE",
        "BLACK OAK RIDGE",
        "BLOWING CAVE",
        "BLUE RIDGE",
        "BLUFF MOUNTAIN",
        "BLUFF RIDGE",
        "BOARDLY HILLS",
        "BOAT LAUNCH",
        "BOYDS CREEK",
        "BRANAM HOLLOW",
        "BUCKEYE KNOB",
        "BURDEN HILL",
        "BUTLER BRANCH",
        "CAMPBELL LEAD",
        "CANEY CREEK",
        "CANYON HILLS",
        "CATON FARM",
        "CENTER VIEW",
        "CLABO MOUNTAIN",
        "COCKE COUNTY LINE",
        "COMMUNITY CENTER",
        "CONNER HEIGHTS",
        "COUNTRY OAKS",
        "COUNTY LINE",
        "COVE CREEK",
        "COVE MEADOWS",
        "COVE MOUNTAIN",
        "COVE VIEW",
        "CREEK SIDE",
        "CREEKSIDE VILLAGE",
        "CUMMINGS CHAPEL",
        "DEER PATH",
        "DIXON BRANCH",
        "DOLLY PARTON",
        "DOUBLE D",
        "DOUGLA DAM",
        "DOUGLAS DAM",
        "DUDLEY CREEK",
        "DUMPLIN VALLEY",
        "EAGLE DEN",
        "ELLIS OGLE",
        "EMERALD SPRINGS",
        "ENGLISH HILLS",
        "ERNEST MCMAHAN",
        "FALLEN OAK",
        "FLAT CREEK",
        "FOREST COURT",
        "FOREST HILLS",
        "FORKS OF THE RIVER",
        "FRANKE HOLLOW",
        "FRENCH BROAD RIVER",
        "GARNER HOLLOW",
        "GEORGE DAVIS",
        "GISTS CREEK",
        "GOLDEN HARVEST",
        "GOOSE CREEK",
        "GOOSE GAP",
        "GRASSY BRANCH",
        "GRASSY MEADOWS",
        "HAG HOLLOW",
        "HALF HIGH",
        "HAPPY HOLLOW",
        "HARRISBURG MILL",
        "HEAVENS PATH",
        "HENDERSON CHAPEL",
        "HENRY TOWN",
        "HICKORY KNOLL",
        "HICKORY LODGE",
        "HICKORY TREE",
        "HIDDEN HARBOR",
        "HIDDEN HILLS",
        "HIDDEN VALLEY",
        "HIGH VIEW",
        "HILLS GATE",
        "HISTORIC NATURE",
        "HOLLIDAY HILLS",
        "HUNTERS RIDGE",
        "INDIAN GAP",
        "INGLE HOLLOW",
        "JACK SHARP",
        "JAKE THOMAS",
        "JAMESENA MILLER",
        "JESS WILSON",
        "JIM HICKMAN",
        "JIM PARTON",
        "JOBEY GREEN HOLLOW",
        "JOHN BARBER",
        "JONES COVE",
        "KATY HOLLAR",
        "KELLUM CREEK",
        "KING BRANCH",
        "KNOB CREEK",
        "LANE HOLLOW",
        "LAUGHING PINE",
        "LECONTE LANDING",
        "LEO SHARP",
        "LEXINGTON PARK",
        "LITTLE COVE",
        "LITTLE FALL",
        "LITTLE VALLEY",
        "LONES BRANCH",
        "LORI ELLEN",
        "LOST BRANCH",
        "LUMBER JACK",
        "MANIS HOLLOW",
        "MAPLES BRANCH",
        "MARIAN LAKE",
        "MARY LEE",
        "MATHIS BRANCH",
        "MATTOX CEMETERY",
        "MCCLEARY BEND",
        "MIDDLE CREEK",
        "MILL CREEK",
        "MILLERS RIDGE",
        "MILLICAN GROVE",
        "MISTY SHADOWS",
        "MITCHELL FARM",
        "MOOSE RIDGE",
        "MOUNTAIN BROOK",
        "MOUNTAIN PRESERVE",
        "MOUNTAIN VIEW",
        "MURRELL MEADOWS",
        "MYERS HOLLOW",
        "NAILS CREEK",
        "NELL ROSE",
        "NEWFOUND GAP",
        "NICHOLS BRANCH",
        "NORTHVIEW ACADEMY",
        "OAK HAVEN",
        "OGLES VIEW",
        "OLDHAM CREEK",
        "OMA LEE",
        "OTTO WILLIAMS",
        "OUTDOOR SPORTSMANS",
        "PANTHER PATH",
        "PARK HEADQUARTERS",
        "PARKSIDE VILLAGE",
        "PEARL VALLEY",
        "PINE HAVEN",
        "PINE MOUNTAIN",
        "PINE PEAK",
        "PINNACLE VISTA",
        "PITTMAN CENTER",
        "PORTERFIELD GAP",
        "RAYFIELD HOLLOW",
        "REAGAN SPRINGS",
        "RED BUD",
        "RED CEDAR RIDGE",
        "RICER DIVIDE",
        "RICHARDSON COVE",
        "RIPPLING WATERS",
        "RIVER BANK",
        "RIVER BEND",
        "RIVER DIVIDE",
        "RIVER MEADOWS",
        "RIVERS EDGE",
        "ROBINSON GAP",
        "ROCKING CHAIR",
        "ROCKY FLATS",
        "ROCKY GROVE",
        "ROY ELDER",
        "RUBY MAES",
        "RUSSELL HOLLOW",
        "SAGE GRASS",
        "SAINT ANDREWS",
        "SANDY BOTTOM",
        "SCENIC LOOP",
        "SCENIC MOUNTAIN",
        "SCOTTISH HIGHLAND",
        "SHELL MOUNTAIN",
        "SINGING BIRD",
        "SKI MOUNTAIN",
        "SMOKY MOUNTAIN VIEW",
        "SMOKY RIDGE",
        "STARDUST MOUNTAIN",
        "SUGAR LOAF",
        "SUGAR MAPLE LOOP",
        "SUMMIT TRAILS",
        "TATTLE BRANCH",
        "TEN POINT",
        "THE ISLAND",
        "THOMAS CROSS",
        "TWIN HOLLOW",
        "UNCLE HARVEY",
        "UNION VALLEY",
        "VALLEY HOME",
        "VALLEY VIEW",
        "VIC KING",
        "WA FLOY",
        "WALDENS CREEK",
        "WALT PRICE",
        "WALTER WEBB",
        "WEARS MOUNTAIN",
        "WEARS VALLEY",
        "WEAS VALLEY",
        "WESTGATE RESORTS",
        "WESTSIDE HILLS",
        "WHIPOORWILL HILL",
        "WHISTLING WIND",
        "WHITE OAK",
        "WHITE SCHOOL",
        "WHITES SCHOOL",
        "WILLIAMS HOLLOW",
        "WINDFIELD DUNN",
        "WINDSWEPT VIEW",
        "WINFEILD DUNN",
        "WINFIELD DUNN",
        "WOODS VIEW",
        "YARBERRY EDGE",
        "YELLOW BREECHES",
        "YELLOW BREECHES CREEK",
        "ZION HILL"
   );
  }
  
  @Override
  public String getFilter() {
    return "Central_Dispatch@mydomain.com,Central_Dispatch@seviercountytn.org";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      data.strCallId = match.group(1);
      return true;
    }
    
    if (!super.parseMsg(body, data)) return false;
    if (data.strCall.length() == 0) data.strCall = "ALERT";
    data.strCity = convertCodes(data.strCity, CITY_CODES);
    return true;
  }
  
  @Override
  public String getProgram() {
    return super.getProgram() + " CALL";
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new MyCancelField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private static final Pattern CANCEL_PTN = Pattern.compile("((?:PER [ A-Z]+ )?(?:CANCEL|CXC?L|CSL)[/ ]*(?:ANY )?(?:FURTHER RES?PONSE|RESPONSE|ALL RESPONSE|ALL UNITS|CALL(?: CXL| CANCEL)?)?(?:[ /]*(?:FALSE ALARM|NON-INJ))?(?:[ /]*PER (?:EMS ON SCENE|ALARM CO|[^ ]+))?)[-:/ ]*");
  private class MyCancelField extends CallField {
    public MyCancelField() {
      setPattern(CANCEL_PTN, true);
    }
  }
  
  private static final Pattern AREA_OF_PTN = Pattern.compile("AREA(?: OF)?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher match = CANCEL_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strCall = match.group(1).trim();
        field = field.substring(match.end());
      }
      
      // Leading zeros are normally stripped from the beginning.  But we want to take the extra
      // step of not looking for a place field if we had to strip a leading zero.
      StartType st = StartType.START_PLACE;
      if (field.startsWith("0 ")) {
        st = StartType.START_ADDR;
        field = field.substring(2).trim();
      }
      parseAddress(st, FLAG_IGNORE_AT | FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      
      if (data.strAddress.length() == 0) {
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      }
      
      if (AREA_OF_PTN.matcher(data.strApt).matches()) {
        data.strPlace = append(data.strPlace, " - ", data.strApt);
        data.strApt = "";
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY";
    }
  }

  @Override
  protected boolean isNotExtraApt(String apt) {
    if ( apt.startsWith("@") || 
         apt.startsWith("&") || 
         apt.startsWith("MM ") || 
         apt.endsWith(" MM") ||
         apt.equals("MM")) return true;
    return super.isNotExtraApt(apt);
  }
  
  @Override
  public String adjustMapCity(String city) {
    return city.equals("NEWPORT") ? "COSBY" : city;
  }

  private static final String[] CITY_LIST = new String[]{
    "ALDER BRANCH",
    "BEECH SPRINGS",
    "BOYDS CREEK",
    "CATLETTSBURG",
    "CATON",
    "CHEROKEE HILLS",
    "COUNTRY CASCADES",
    "COSBY",
    "DUPONT",
    "GATLINBURG",
    "KODAK",
    "LAUREL",
    "NEWPORT",
    "OLDHAM",
    "PIGEON FORGE",
    "PITTMAN CENTER",
    "REAGANTOWN",
    "SEVIERVILLE",
    "SEYMOUR",
    "SHADY GROVE",
    "STRAWBERRY PLAINS",
    "TRUNDLES CROSSROADS",
    "WEARS VALLEY",

    "OM",
    "PIG",
    
    // Jefferson County
    "DANDRIDGE"
  };
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "OM",     "SEYOUR",
      "PIG",    "PIGEON FORGE"
  });

}
