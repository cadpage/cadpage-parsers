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
          "ID CALL ADDRCITY PLACE X CH/L+? UNIT! INFO+? X:GPS1 Y:GPS2");
  }

  @Override
  public String getFilter() {
    return "skucad@fire.ca.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equalsIgnoreCase("CAD Page")) return false;
    if (!super.parseFields(body.split(";"), data)) return false;

    int pt = data.strCity.indexOf('/');
    if (pt >= 0) {
      data.strPlace = append(data.strPlace, " - ", data.strCity.substring(0,pt));
      data.strCity = data.strCity.substring(pt+1);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Inc# +(.*)", true);
    if (name.equals("PLACE")) return new MyPlace();
    if (name.equals("ADDRCITY")) return new MyAddrCity();
    if (name.equals("CH")) return new ChannelField("(?:Cmd|Tac): *(.*)", true);
    if (name.equals("UNIT")) return new MyUnit();
    return super.getField(name);
  }

  private static final Pattern PLACE_PATTERN = Pattern.compile("^\\#(.+)");
  private class MyPlace extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = field.trim();
      Matcher m = PLACE_PATTERN.matcher(field);
      if (m.matches()) {
        data.strApt = m.group(1);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return (append(super.getFieldNames(), " ", "APT"));
    }
  }

  private class MyUnit extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replaceFirst("^(?i)<a.+<\\/a>", "");
      super.parse(field, data);
    }
  }

  private static final Pattern ADDRCITY_PATTERN_1 = Pattern.compile("(.*)\\((.*)\\)");
  private static final Pattern ADDRCITY_PATTERN_2 = Pattern.compile("(.*?)\\@(.*)");
  private class MyAddrCity extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      // Let super handle anything with "@L(..."
      if (field.contains("@ =L(")) {
        super.parse(field, data);
        return;
      }
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
      "PLEASENT VALLEY",  "DORRIS",
      "RED ROCK",         "MACDOEL",
      "SAMS NECK",        "DORRIS",
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
      "TENNANT",      "TENNANT/MACDOEL",
      "TULELAKE",     "TULELAKE",
      "WEED",         "WEED",
      "WESTVALLEY",   "WEST VALLEY",
      "YREKA",        "YREKA"
  });
}
