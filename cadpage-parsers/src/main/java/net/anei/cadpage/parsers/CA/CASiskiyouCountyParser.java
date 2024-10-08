package net.anei.cadpage.parsers.CA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Siskiyou County, CA
 */
public class CASiskiyouCountyParser extends FieldProgramParser {
  public CASiskiyouCountyParser() {
    super(CITY_CODES, "SISKIYOU COUNTY", "CA",
          "ID CODE_CALL ADDRCITY X INFO! Remarks:INFO! INFO/N+? GPS/Z! Resources:UNIT! Cmd:CH! Tac:CH/L! END");
  }

  @Override
  public String getFilter() {
    return "skucad@fire.ca.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equalsIgnoreCase("CAD Page")) return false;
    if (!super.parseFields(body.split("\n"), data)) return false;

    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", data.strCity.substring(0,pt));
      data.strCity = data.strCity.substring(pt+1);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Incident #(\\d+)", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddrCity();
    if (name.equals("GPS")) return new GPSField("https?://maps.google.com/\\?q=(.*)|()", true);
    if (name.equals("UNIT")) return new MyUnit();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) {
        data.strCode = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ADDRCITY_PATTERN_1 = Pattern.compile("(.*)\\((.*)\\)");
  private static final Pattern ADDRCITY_PATTERN_2 = Pattern.compile("(.*?)\\@(.*)");
  private class MyAddrCity extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.trim();
      // Anything in () at end is PLACE
      Matcher m = ADDRCITY_PATTERN_1.matcher(field);
      if (m.matches()) {
        field = m.group(1).trim();
        data.strPlace = append(data.strPlace, " - ", m.group(2).trim());
      }
      // Anything before 1st "@" is PLACE
      m = ADDRCITY_PATTERN_2.matcher(field);
      if (m.matches()) {
        data.strPlace = append(m.group(1).trim(), " - ", data.strPlace);
        field = m.group(2).trim();
      }
      super.parse(field, data);
    }

    @Override public String getFieldNames() {
      return append(super.getFieldNames(), " ", "PLACE");
    }
  }

  private class MyUnit extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', ',');
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, MAP_CITY_TABLE);
  }

  private static final Properties MAP_CITY_TABLE = buildCodeTable(new String[]{
      "BIG SPRINGS",      "MONTAGUE",
      "EDGEWOOD",         "WEED",
      "HAMBURG",          "KLAMATH RIVER",
      "KLAMATH NF",       "",
      "LAKE SHASTINA",    "WEED",
      "NEWL",             "TULELAKE",
      "NORTH OROVILLE",   "OROVILLE",
      "PLEASENT VALLEY",  "DORRIS",
      "RED ROCK",         "MACDOEL",
      "SAMS NECK",        "DORRIS",
      "SOUTHWEST JACKSON","JACKSON",
      "TENNANT",          "MACDOEL"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BELLAVISTA",   "BELLA VISTA",
      "BIG_SPGS",     "BIG SPRINGS",
      "CSTELA",       "CASTELLA",
      "DNSMR",        "DUNSMUIR",
      "EDGWD",        "EDGEWOOD",
      "FORKS_SALMON", "FORKS OF SALMON",
      "FORT_JONES",   "FORT JONES",
      "GRENADA",      "GRENADA",
      "GZEL",         "GAZELLE",
      "HAMBURG",      "HAMBURG",
      "HAPPY_CAMP",   "HAPPY CAMP",
      "HRNBRK",       "HORNBROOK",
      "JONESVALLEY",  "JONES VALLEY",
      "KNF",          "KLAMATH NF",
      "KLAMATH_RIVER","KLAMATH RIVER",
      "LK_SHASTINA",  "LAKE SHASTINA",
      "LK SHASTINA",  "LAKE SHASTINA",
      "MACDOEL",      "MACDOEL",
      "MC_CLOUD",     "MCCLOUD",
      "MODSHAKLA_FOR","MCCLOUD",
      "MONTGOMERYCK", "MONTGOMERY CREEK",
      "MONTGOMERY_CRK","MONTGOMERY CREEK",
      "MS",           "MT SHASTA",
      "MSA",          "MT SHASTA",
      "MTGUE",        "MONTAGUE",
      "MTNGATE",      "MOUNTAIN GATE",
      "N_OROVILLE",   "NORTH OROVILLE",
      "NEWL",         "NEWL",
      "PALOCEDRO",    "PALO CEDRO",
      "PLEASANT_VLY", "PLEASENT VALLEY",
      "RE",           "REDDING",
      "RED­_ROCK",     "RED ROCK",
      "REDDINGCTY",   "REDDING",
      "SAMS_NECK",    "SAMS NECK",
      "SCOTT_BAR",    "SCOTT BAR",
      "SEIAD",        "SEIAD VALLEY",
      "SHASTACOLL",   "SHASTA COLLEGE",
      "SHASTALKCTY",  "SHASTA LAKE",
      "SOUTHWEST_JACKSON","SOUTHWEST JACKSON",
      "TENNANT",      "TENNANT/MACDOEL",
      "TULELAKE",     "TULELAKE",
      "WEED",         "WEED",
      "WESTVALLEY",   "WEST VALLEY",
      "YREKA",        "YREKA"
  });
}
