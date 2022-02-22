package net.anei.cadpage.parsers.TX;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/*
Brazos County, TX
*/

public class TXBrazosCountyParser extends FieldProgramParser {
  public TXBrazosCountyParser() {
    super("BRAZOS COUNTY", "TX",
          "DATETIME! Type:TYPE! Location:LOCATION! LocCross:X? Info:PLACE? PrimeUnit:PRIMEUNIT! Agency:AGENCY! INFO+");
  }

  @Override
  public String getFilter() {
    return "@bc911.org,paging.bc911.org,noreply@omnigo.com";
  }

  private static final Pattern ICUNIT_PTN = Pattern.compile("ICUnit:\\S*\\s+(?=PrimeUnit:)");
  private static final Pattern DELIM = Pattern.compile("\n(?! |\n)");
  private static final Pattern MULTI_SPACE = Pattern.compile("\\s{2,}");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch Call Notes")) return false;
    body = ICUNIT_PTN.matcher(body).replaceFirst("");
    String[] field = DELIM.split(body);
    for (int i=0; i<field.length; i++) {
      field[i] = MULTI_SPACE.matcher(field[i].trim()).replaceAll(" ");
    }
    return parseFields(field, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d? \\d\\d?\\d\\d:\\d\\d");
    if (name.equals("TYPE")) return new TypeField();
    if (name.equals("LOCATION")) return new LocationField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PRIMEUNIT")) return new PrimeUnitField();
    if (name.equals("AGENCY")) return new AgencyField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern TYPE_PATTERN
    = Pattern.compile("(.*)Priority:(.*)");
  private class TypeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher m = TYPE_PATTERN.matcher(field);
      if (m.matches()) {
        parseCall(m.group(1).trim(), data);
        data.strPriority = m.group(2).trim();
      }
    }

    private void parseCall(String field, Data data) {
      int hPtr = field.indexOf('-');
      if (hPtr > -1) {
        data.strCode = field.substring(0, hPtr).trim();
        data.strCall = field.substring(hPtr+1).trim();
      }
      else
        data.strCode = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL PRI";
    }
  }

  private static final Pattern ADDR_PTN = Pattern.compile("(.*?)(?:[- ]+at +(.*?))?(?:[- ]+btwn +(.*?))?(?:[- ]*<.*)?");
  private class LocationField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher m = ADDR_PTN.matcher(field);
      if (!m.matches()) abort(); // Cannot happen!!!
      field = m.group(1);
      String atTerm = m.group(2);
      String btwnTerm = m.group(3);

      field = stripCity(field, data);
      if (atTerm != null) {
        data.strPlace = field;
        field = stripCity(atTerm, data);
      }
      if (btwnTerm != null) {
        data.strCross = stripCity(btwnTerm, data).replaceAll(" and ", " & ");
      }

      parseAddress(field, data);
    }

    private String stripCity(String field, Data data) {
      field = field.trim();
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = convertCodes(field.substring(pt+1).trim(), CITY_CODES);
        field = field.substring(0,pt).trim();
      }
      return field;
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT X MAP PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "btwn ");
      field = field.replace(" and ", " & ");
      super.parse(field, data);
    }
  }

  private static final Pattern PRIME_UNIT_PATTERN
    = Pattern.compile("(.*?)Final Type:(.*)");
  private class PrimeUnitField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher m = PRIME_UNIT_PATTERN.matcher(field);
      if (!m.matches()) abort();
      data.strUnit = m.group(1).trim();
      parseFinalCall(m.group(2).trim(), data);
    }

    private void parseFinalCall(String field, Data data) {
      int hPtr = field.indexOf('-');
      if (hPtr > -1) {
        String sf1 = field.substring(0, hPtr).trim(),
               sf2 = field.substring(hPtr+1).trim();
        if (!data.strCode.equals(sf1)) data.strCode = sf1;
        if (!data.strCall.equals(sf2)) data.strCall = sf2;
      }
      else {
        if (!data.strCode.equals(field)) data.strCode = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "UNIT CODE CALL";
    }
  }

  private static final Pattern AGENCY_PATTERN
    = Pattern.compile("(.*?)(?:Group|Chan):(.*?)(?:(?:Station|Beat):(.*?))?(?:Box:(.*))?");
  private class AgencyField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher m = AGENCY_PATTERN.matcher(field);
      if (!m.matches()) abort();
      data.strSource = m.group(1).trim();
      data.strMap = append(data.strMap, "-", m.group(2).trim());
      data.strMap = append(data.strMap, "-", getOptGroup(m.group(3)));
      data.strBox = getOptGroup(m.group(4));
    }

    @Override
    public String getFieldNames() {
      return "SRC MAP BOX";
    }
  }

  private static final String TIMESTAMP_PATTERN_S
    = "\\d{1,2}:\\d{2}:\\d{2}(?:c(?:s|d)t)? +",
    FLOAT_PATTERN_S
      = "([+-]?\\d{2}\\.\\d+)";
  private static final Pattern NAME_PHONE_PATTERN
    = Pattern.compile("Name:(.*)Phone:(.*)RPaddr:.*");
  private static final Pattern GPS_PATTERN
    = Pattern.compile(TIMESTAMP_PATTERN_S+".*AliLong:.*?"+FLOAT_PATTERN_S+".*?AliLatitude:.*?"+FLOAT_PATTERN_S+".*?Uncertainty:.*");
  private static final Pattern UNIT_PATTERN
    = Pattern.compile(TIMESTAMP_PATTERN_S+".*Unit:(.*?)(?:(?:UnmetReq|Dispo|Comment):.*)?");
  private static final Pattern INFO_PATTERN1
    = Pattern.compile(TIMESTAMP_PATTERN_S+"PROQA Case#:\\d+ +Classify:.*?Service:.*?Comment:(.*)");
  private static final Pattern INFO_PATTERN2
  = Pattern.compile(TIMESTAMP_PATTERN_S+"-?[A-Z]+ +Comment:(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = NAME_PHONE_PATTERN.matcher(field);
      if (m.matches()) {
        data.strName = append(data.strName, "/", m.group(1).trim());
        data.strPhone = append(data.strPhone, "/", m.group(2).trim());
        return;
      }
      m = GPS_PATTERN.matcher(field);
      if (m.matches()) {
        setGPSLoc(m.group(2)+","+m.group(1), data);
        return;
      }
      m = UNIT_PATTERN.matcher(field);
      if (m.matches()) {
        data.strUnit = addUnit(data.strUnit, m.group(1).trim());
        return;
      }
      m = INFO_PATTERN1.matcher(field);
      if (m.matches()) {
        data.strSupp = append(data.strSupp, "\n", m.group(1).trim());
        return;
      }
      m = INFO_PATTERN2.matcher(field);
      if (m.matches()) {
        String info = m.group(1).trim();
        if (!info.equals("(none)")) {
          data.strSupp = append(data.strSupp, "\n", m.group(1).trim());
        }
        return;
      }
    }

    /*
     * Add a space-delimited unit list to another, removing duplicates
     */
    private String addUnit(String units, String u) {
      String[] unitStrings = units.split(" "),
               uStrings = u.split(" ");
      for (int i=0; i<uStrings.length; i++) {
        if (!containsUnit(unitStrings, uStrings[i])) {
          units = append(units, " ", uStrings[i]);
        }
      }
      return units;
    }

    private boolean containsUnit(String[] us, String u) {
      for (int i=0; i<us.length; i++)
        if (us[i].equals(u)) return true;
      return false;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames()+" UNIT NAME PHONE GPS";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
    "BC", "BRAZOS COUNTY",
    "BR", "BRYAN",
    /*
    College Station
    Kurten
    Millican
    Wixon Valley
    Benchley
    Boonville
    */
  });
}
