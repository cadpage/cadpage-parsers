package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYCayugaCountyAParser extends FieldProgramParser {

  private static final Pattern DELIM = Pattern.compile("  +|\n");

  public NYCayugaCountyAParser() {
    super(MISTYPED_CITIES, "CAYUGA COUNTY", "NY",
          "ADDR_PFX+? ADDR/iSC INFO/N+? ( UNIT DATETIME% | DATETIME_UNIT% )");
    setupCities(CITY_LIST);
    setupCallList(CALL_LIST);
    setupMultiWordStreets(
        "BAPTIST CORNERS",
        "BAPTIST HILL",
        "BROOK HOLLOW",
        "BUCK POINT",
        "BUCKLEY HILL",
        "BURTIS POINT",
        "CENTER STREET",
        "CHERRY STREET",
        "CHESTNUT RIDGE",
        "COUNTY HOUSE",
        "COUNTY LINE",
        "FAIR HAVEN",
        "FRANKLIN STREET",
        "GENESEE STREET",
        "GLEN COVE",
        "GRANT AVENUE",
        "HIDDEN BROOK",
        "JOHN SMITH",
        "MUTTON HILL",
        "PINE RIDGE",
        "SAND BEACH",
        "SILVER STREET",
        "STATE PARK",
        "SUNSET BEACH",
        "TOWN HALL",
        "TWELVE CORNERS",
        "WEEDSPORT SENNETT",
        "WEST LAKE",
        "WHITE BIRCH",
        "WHITE BRIDGE",
        "WOOD HOLLOW"

    );
  }

  @Override
  public String getFilter() {
    return "911cad@cayugacounty.us,support@digitalfirehouse.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("From 911 Center")) return false;
    if (!parseFields(DELIM.split(body), data)) return false;
    return data.strTime.length() > 0 || data.strCity.length() > 0;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR_PFX")) return new MyAddressPrefixField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("DATETIME_UNIT")) return new MyDateTimeUnitField();
    return super.getField(name);
  }

  // Directions in the street address tend to be followed by an extraneous
  // double blank which splits the address into multiple fields that we have to
  // merge back together
  private static final Pattern TRAIL_DIR_PTN = Pattern.compile(".* [NSEW]");
  private class MyAddressPrefixField extends SkipField {

    @Override
    public boolean canFail() {
      return true;
    }

    public boolean checkParse(String field, Data data) {
      if (!TRAIL_DIR_PTN.matcher(field).matches()) return false;
      data.strAddress = append(data.strAddress, " ", field);
      return true;
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("E911 Info - .*|.* Uncertainity:|Confidence:.*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_UNIT_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d) +(.*)");
  private class MyDateTimeUnitField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_UNIT_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strUnit = match.group(3);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT";
    }

  }

  private static final CodeSet CALL_LIST = new CodeSet(
      "ACCIDENT - OTHER",
      "ALARM - CO",
      "ALARM - CO - ILLNESS",
      "ALARM - FIRE",
      "ALARM - MEDICAL",
      "ALLERGIC REACTION",
      "ASSIST BY EMS",
      "ASSIST BY FIRE",
      "BLEEDING",
      "BURNS",
      "CARDIAC",
      "CHECK WELFARE W/MEDICAL",
      "CHOKING",
      "CITIZEN ASSIST - FIRE",
      "DIFFICULTY BREATHING/SOB",
      "DIABETIC",
      "DOA",
      "FAINTING",
      "FUEL SPILL",
      "GAS LEAK",
      "GENERAL ILLNESS",
      "HAZARD - FIRE",
      "INJURY FROM A FALL",
      "INVESTIGATE - FIRE",
      "LOCKOUT",
      "MATERNITY",
      "MEDICAL EMERGENCY",
      "MVAPI",
      "NEW",
      "OUTSIDE FIRE",
      "OVERDOSE",
      "PAIN - GENERAL",
      "POISONING",
      "PERSONAL INJURY",
      "SEIZURES",
      "SERVICE CALL",
      "SPECIAL DETAIL - FIRE",
      "STROKE",
      "STRUCTURE FIRE",
      "SUICIDE",
      "UNCONSCIOUS PERSON",
      "UTILITY POLE FIRE",
      "VEHICLE FIRE",
      "WATER RESCUE"
  );

  private static final String[] CITY_LIST = new String[]{
      "AUBURN",
      "AURELIUS",
      "CAYUGA",
      "BRUTUS",
      "WEEDSPORT",
      "CATO",
      "MERIDIAN",
      "CONQUEST",
      "FLEMING",
      "GENOA",
      "IRA",
      "LEDYARD",
      "AURORA",
      "LOCKE",
      "MENTZ",
      "PORT BYRON",
      "MONTEZUMA",
      "MORAVIA",
      "NILES",
      "OWASCO",
      "SCIPIO",
      "SEMPRONIUS",
      "SENNETT",
      "SPRINGPORT",
      "UNION SPRINGS",
      "STERLING",
      "FAIR HAVEN",
      "SUMMERHILL",
      "THROOP",
      "VENICE",
      "VICTORY",

      "ONONDAGA COUNTY",
      "SKANEATELES",
      "MOTTVILLE",

      "WAYNE COUNTY",
      "WOLCOTT"
  };

  private static Properties MISTYPED_CITIES = buildCodeTable(new String[]{
      "MORAVIL",                        "MORAVIA",
      "SKANEATELES ONONDAGA COUNTY",    "SKANEATELES",
      "TOWN OF WOLCOTT WAYNE COUNTY",   "WOLCOTT"
  });
}
