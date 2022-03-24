package net.anei.cadpage.parsers.OR;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Marion County, OR
 */
public class ORMarionCountyAParser extends FieldProgramParser {

  public ORMarionCountyAParser() {
    super("MARION COUNTY", "OR",
          "( SELECT/1 INFO:CALL! CALL1/SDS LOC:ADDRCITY1! CFS:SKIP! DATETIME! Units:UNITZ! ID INFO/N+ " +
          "| CALL ADDRCITY2 ( MAP EMPTY+? CH? EMPTY+? UNIT? " +
                           "| ( APT UNIT | UNIT ) ( MAP | INFO MAP | INFO PLACE/Z MAP | ) ( CH | ALRM/SDS | ) " +
                           "| ( PLACE/Z APT INFO MAP | APT PLACE/Z MAP | APT_PLACE MAP | MAP | ) EMPTY+? CH? EMPTY+? UNIT? " +
                           ") " +
          ") INFO+");
    setupSaintNames("PAUL");
    removeWords("ESTATES", "LANE", "ROAD");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
    addRoadSuffixTerms("WY");
  }

  @Override
  public String getFilter() {
    return "Dispatch@ci.woodburn.or.us";
  }

  private static final Pattern MAP_PTN = Pattern.compile(":MAP[-:]:(\\d+[A-Z]?):");
  private static final Pattern DELIM = Pattern.compile("(?<!LAT|LON|Alarm):");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    do {
      if (subject.equals("Incident") || subject.equals("!")) break;

      if (body.startsWith("Incident / ")) {
        body = body.substring(11).trim();
        break;
      }

      if (body.startsWith("! / ")) {
        body = body.substring(4).trim();
        break;
      }

      return false;
    } while (false);

    partGPS = null;

    if (body.startsWith("INFO:")) {
      setSelectValue("1");
      return parseFields(body.split("\n"), data);
    }

    setSelectValue("2");

    body = body.replace('\n', ' ');

    // And a MAP::<code> construct
    body = MAP_PTN.matcher(body).replaceFirst(":MAP-$1:");

    String[] flds = DELIM.split(body, -1);
    if (flds.length == 1) {
      setFieldList("ADDR APT CALL");
      parseAddress(StartType.START_ADDR, body, data);
      data.strCall = getLeft();
      return isValidAddress();
    }
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new SkipField("\\*{2} *(.*?) *\\*{2}", true);
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("CH")) return new ChannelField("N\\d|", true);
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("UNIT")) return new MyUnitField(false);
    if (name.equals("UNITZ")) return new MyUnitField(true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("MAP")) return new MapField("MAP\\b-?(.*)", true);
    if (name.equals("ALRM")) return new CallField("\\.(.* ALRM)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern VALID_ADDR1_PTN = Pattern.compile("(.*?),([ A-Z]*)-(.*)");
  private class MyAddressCity1Field extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = VALID_ADDR1_PTN.matcher(field);
      if (match.matches()) {
        parseAddress(match.group(1).trim(), data);
        data.strCity = match.group(2).trim();
        data.strPlace = match.group(3).trim();
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY PLACE";
    }

  }

  private class MyAddressCity2Field extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      if (field.contains("I5") || field.contains(" MP ")) {
        parseAddress(field, data);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }

  private static final String UNIT_PTN_STR = "(?:[A-Z]+[0-9]+(?:-[A-Z]+)?|\\d{3}|AC|BLJ|DS|DT-LK-STPARK|JT|MCDUTY|MCSO|MP|ODF|PUBLWRKS|RCO|SEND_MAPD|SEND_[A-Z]+|TONE-ON-HOUSE|[A-Z]*TONE|Respond-[A-Z0-9]+|.*-FD)(?:,.*)?";
  private static final Pattern UNIT_PTN = Pattern.compile(UNIT_PTN_STR);
  private static final Pattern UNITZ_PTN = Pattern.compile("|" + UNIT_PTN_STR);
  private static final Pattern STATION_PTN = Pattern.compile("\\bSTA\\d+$");
  private class MyUnitField extends UnitField {
    public MyUnitField(boolean emptyOK) {
      setPattern(emptyOK ? UNITZ_PTN : UNIT_PTN, true);
    }

    @Override
    public void parse(String fld, Data data) {
      fld = fld.replace("Respond-", "");
      Matcher match = STATION_PTN.matcher(fld);
      if (match.find()) {
        data.strSource = match.group();
        fld = fld.substring(0,match.start()).trim();
        fld = stripFieldEnd(fld, ",");
      }
      super.parse(fld, data);
    }

    @Override
    public String getFieldNames() {
      return "UNIT SRC";
    }
  }

  private static final Pattern APT1_PTN = Pattern.compile("(?:#[ #]*|(?:APT|LOT|SP|SPACE|RM|ROOM|UNIT)[ #]+)(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT1_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private static final Pattern APT2_PTN = Pattern.compile("\\d{1,4}[A-Z]?|[A-DF-H]|BLDG.*", Pattern.CASE_INSENSITIVE);
  private class MyAptPlaceField extends MyPlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match =  APT1_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
      }
      else if (APT2_PTN.matcher(field).matches()) {
        data.strApt = append(data.strApt, "-", field);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT " + super.getFieldNames();
    }
  }

  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("(.*?) +(\\d{3}[- ]?\\d{3}[- ]?\\d{4})");
  private class MyPlaceField extends PlaceField {

    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strAddress)) return;
      Matcher match = PLACE_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE PHONE";
    }
  }

  private static final Pattern PHONE_PTN = Pattern.compile("(\\d{10})\\b *(.*)");
  private static final Pattern GPS_PTN = Pattern.compile("(.*?)(-?\\d{2,3}\\.\\d{6,})[/\\\\](-?\\d{2,3}\\.\\d{6,})");
  private static final Pattern GPS_PTN2 = Pattern.compile("-?\\d{2,3}\\.\\d{6,}");
  private static final Pattern ID1_PTN = Pattern.compile("\\d{4}-\\d{8}\\b.*");
  private static final Pattern ID2_PTN = Pattern.compile("CFS# *(\\S+)");
  private String partGPS = null;
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (data.strPhone.length() == 0) {

        Matcher match = PLACE_PHONE_PTN.matcher(field);
        if (match.matches()) {
          data.strPlace = append(data.strPlace, " - ", match.group(1));
          data.strPhone = match.group(2);
          return;
        }

        match = PHONE_PTN.matcher(field);
        if (match.matches()) {
          data.strPhone = match.group(1);
          field = match.group(2);
        }
      }

      if (field.equals(data.strAddress)) return;

      if (data.strMap.length() == 0) {
        if (field.startsWith("MAP-")) {
          data.strMap = field.substring(4).trim();
          return;
        }

        if ("MAP-".startsWith(field)) return;
      }

      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        setGPSLoc(match.group(2)+','+match.group(3), data);
      }

      if (GPS_PTN2.matcher(field).matches()) {
        if (partGPS == null) {
          partGPS = field;
        } else {
          setGPSLoc(partGPS+','+field, data);
        }
        return;
      }

      if (field.startsWith("BETWEEN ")) {
        data.strCross = append(data.strCross, "/", field.substring(8).trim());
        return;
      }

      int pt = field.indexOf(',');
      String tmp = (pt >= 0 ? field.substring(0,pt).trim() : field);
      if (isValidCrossStreet(tmp)) {
        data.strCross = append(data.strCross, "/", field);
        return;
      }

      if (ID1_PTN.matcher(field).matches()) {
        if (data.strCallId.isEmpty()) data.strCallId = field;
        return;
      }

      if ((match = ID2_PTN.matcher(field)).matches()) {
        if (data.strCallId.isEmpty()) data.strCallId = match.group(1);
        return;
      }

      if (field.startsWith("Alarm:#")) {
        data.strPriority = field.substring(7).trim();
        return;
      }

      if (data.strPlace.length() == 0) {
        match = PLACE_PHONE_PTN.matcher(field);
        if (match.matches()) {
          data.strPlace = match.group(1);
          data.strPhone = match.group(2);
          return;
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " X PLACE PHONE GPS MAP ID PRI";
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "286 HIGH ST",                          "+44.710392,-123.008581"
  });
}