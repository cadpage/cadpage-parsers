package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VABedfordCountyAParser extends FieldProgramParser {

  public VABedfordCountyAParser() {
    super(CITY_LIST, "BEDFORD COUNTY", "VA",
          "CALL#:ID! LOC:ADDR/S! COMP:CALL!");
  }

  @Override
  public String getFilter() {
    return "Bedford";
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d : \\S+ : \\S+ +");
  private static final Pattern TRAIL_UNIT_PTN = Pattern.compile("(.*?) ((?:\\b(?:[A-Z]*\\d+[A-Z]?(?:-\\d+)?|[A-Z]{1,4}FD|BRRS|COMM|FMO|HMAT|SOC|VDOF)\\b,?)+)");

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("Bedford911:")) return false;
    body = body.substring(11).trim();
    body = body.replace('\n', ' ');

    Parser p = new Parser(body);
    data.strPlace = p.getLastOptional(" Line20=");
    data.strSupp = p.getLastOptional(" Line19=");
    body = p.get();

    boolean first = true;
    for (String part : INFO_BRK_PTN.split(body)) {
      part = part.trim();
      if (first) {
        body = part;
        first = false;
      } else {
        data.strSupp = append(data.strSupp, "\n", part);
      }
    }

    Matcher match = TRAIL_UNIT_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      data.strUnit = match.group(2);
    }

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " UNIT INFO PLACE";
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\**(\\d+)\\**", true);
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("//", "/");
      super.parse(field, data);
      String  fixCity = MISSPELLED_CITY.getProperty(data.strCity.toUpperCase());
      if (fixCity != null) data.strCity = fixCity;
    }
  }

  private static final Properties MISSPELLED_CITY = buildCodeTable(new String[]{
      "LYNCHUBRG",   "LYNCHBURG",
      "PINHOOK",     "PENHOOK",
      "WERTZ",       "WIRTZ"
  });

  private static final String[] CITY_LIST = new String[]{
    "BEDFORD",
    "COLEMAN FALLS",
    "LYNCHBURG",
    "BIG ISLAND",
    "CHAMBLISSBURG",
    "FOREST",
    "GOODE",
    "GOODVIEW",
    "HARDY",
    "HUDDLESTON",
    "MONETA",
    "MONTVALE",
    "NEW LONDON",
    "STEWARTSVILLE",
    "THAXTON",

    // Amherst County
    "AMHERST",
    "CLIFFORD",
    "ELON",
    "MADISON HEIGHTS",
    "SWEET BRIAR",
    "MONROE",
    "RIVERVILLE",
    "STAPLETON",

    // Botetourt County,
    "BOTETOURT",
    "BUCHANAN",
    "FINCASTLE",
    "TROUTVILLE",
    "EAGLE ROCK",
    "BLUE RIDGE",
    "CLOVERDALE",
    "SPRINGWOOD",
    "ORISKANY",
    "HOLLINS",
    "DALEVILLE",
    "LITHIA",
    "ARCADIA",
    "GLEN WILTON",
    "NACE",

    // Campbell County
    "CAMPBELL",
    "ALTAVISTA",
    "BROOKNEAL",
    "CONCORD",
    "LONG ISLAND",
    "LYNCH STATION",
    "RUSTBURG",
    "TIMBERLAKE",
    "GLADYS",
    "EVINGTON",
    "CASTLE CRAIG",
    "KINGSTON",

    // Franklin County
    "FRANKLIN",
    "BOONES MILL",
    "ROCKY MOUNT",
    "CALLAWAY",
    "FERRUM",
    "GLADE HILL",
    "HALES FORD",
    "NORTH SHORE",
    "PENHOOK",
    "PINHOOK",   // Misspelled
    "REDWOOD",
    "SNOW CREEK",
    "UNION HALL",
    "WESTLAKE CORNER",
    "WIRTZ",
    "WERTZ",     // Misspelled?

    // Pittsylvania County
    "PITTSYLVANIA",
    "CHATHAM",
    "GRETNA",
    "HURT",
    "BACHELORS HALL",
    "BLAIRS",
    "BROSVILLE",
    "CHALK LEVEL",
    "CLIMAX",
    "DRY FORK",
    "GRIT",
    "JAVA",
    "KEELING",
    "MOUNT AIRY",
    "MT HERMON",
    "MOTLEY",
    "PICKERALS CROSSING",
    "PITTSVILLE",
    "RENAN",
    "RINGGOLD",
    "SONANS",
    "STRAIGHTSTONE",
    "WHITTLES DEPOT",
    "TIGHTSQUEEZE",
    "CALLANDS",

    // Roanoke County
    "ROANOKE",
    "VINTON",
    "BACK CREEK",
    "BENT MOUNTAIN",
    "BONSACK",
    "CATAWBA",
    "CAVE SPRING",
    "CLEARBROOK",
    "FORT LEWIS",
    "GLENVAR",
    "HANGING ROCK",
    "HOLLINS",
    "MASONS COVE",
    "MOUNT PLEASANT",
    "OAK GROVE",
    "PENN FOREST",
    "POAGES MILL",
    "READ MOUNTAIN",

    // Rockbridge County
    "ROCKBRIDGE",
    "GLASGOW",
    "GOSHEN",
    "BROWNSBURG",
    "RAPHINE",
    "FAIRFIELD",
    "NATURAL BRIDGE STATION",

    // Independent Cities
    "LYNCHBURG",
    "LYNCHUBRG"  // Misspelled
  };
}
