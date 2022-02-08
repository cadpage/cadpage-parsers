package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA1Parser extends FieldProgramParser {

  boolean hasCityList = false;

  public DispatchA1Parser(String defCity, String defState) {
    this((Properties)null, defCity, defState);
  }

  public DispatchA1Parser(String[] cityList, String defCity, String defState) {
    this((Properties)null, defCity, defState);
    setupCities(cityList);
    hasCityList = true;
  }

  public DispatchA1Parser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
           "( CANCEL_INCIDENT:ID! LOC:ADDR/S! UNITS:UNIT! CALL! INFO/N+ " +
           "| ALRM_LVL:PRI? RUN_CARD:BOX? LOC:PLACE PLACE2? ADDR! APT? CITY BTWN:X EMPTY+? ( LAT/LONG:EMPTY GPS! | ) INCIDENT:ID? COM:INFO INFO+? CT:INFO INFO+? UNITS:UNIT INCIDENT:ID UNITS:UNIT RC:CH DATE/TIME:DATETIME http:GPS2 RPT_#:EMPTY ID )");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() == 0 && (body.startsWith("Alert:") || body.startsWith("Cancel Message"))) {
      int pt = body.indexOf('\n');
      if (pt >= 0) {
        subject = body.substring(0,pt).trim();
        body = body.substring(pt+1).trim();
      }
    }

    if (subject.startsWith("Alert:")) {
      data.strCall = subject.substring(6).trim();
      if (data.strCall.length() == 0) data.strCall = "ALERT";
      body = body.replace(", RUN CARD:", "\nRUN CARD:");
      body = body.replaceAll("https:", "http:");
      return parseFields(body.split("\n"), data);
    }

    if (subject.endsWith("Cancel Message")) {
      if (!parseFields(body.split("\n"), data)) return false;
      data.strCall = append("CANCEL", " - ", data.strCall);
      return true;
    }
    return false;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("PLACE2")) return new MyPlace2Field();
//    if (name.equals("ADDR1")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("GPS2")) return new MyGPS2Field();
    if (name.equals("ID")) return new IdField("[E|F]=(.*)");
    return super.getField(name);
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "BOX");
      super.parse(field, data);
    }
  }

  private class MyPlace2Field extends PlaceField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // If this looks like a box field, make do with that
      if (checkBoxField(field, data)) return true;

      // Otherwise there are a lot of things to check
      do {

        // If following field starts with BTWN: this must be an address
        String next = getRelativeField(1);
        if (next.startsWith("BTWN:")) return false;

        // Likewise if next field looks like a city
        if (checkCityField(next)) return false;

        // Or like an apt
        if (APT_PATTERN.matcher(next).matches()) return false;

        // Empty strings should be counted as an apt
        if (next.length() == 0) return false;

        // If the field beyond that looks like an apt, then the next field is the
        // address line and we are a place name.
        String next2 = getRelativeField(2);
        if (APT_PATTERN.matcher(next2).matches()) break;

        // Looks like a tossup.  We need to do an address comparison.  This will be
        // treated as the place name unless the we are a better address then the next field
        int chk1 = checkAddress(field);
        if (chk1 > STATUS_MARGINAL && chk1 > checkAddress(next)) return false;

      } while (false);

      // OK, parse this as a place field
      parse(field, data);
      return true;
    }

    @Override
    public String getFieldNames() {
      return "BOX PLACE";
    }
  }

//  // This should be the address field.  But check to see if the place field
//  // in front of it makes a better address
//  private class MyAddressField extends AddressField {
//    @Override
//    public boolean checkParse(String field, Data data) {
//      if (data.strPlace.length() > 0) {
//        int chk1 = checkAddress(data.strPlace);
//        if (chk1 > STATUS_MARGINAL && chk1 > checkAddress(field)) {
//          String tmp = data.strPlace;
//          data.strPlace = "";
//          super.parse(tmp, data);
//          return false;
//        }
//      }
//      super.parse(field, data);
//      return true;
//    }
//  }

  private static final Pattern APT_PATTERN = Pattern.compile("(?:APT|ROOM|RM|STE(?![A-Z])|SUITE?|UNIT)[-:]? *(.*)|(LOT *.*|.* (?:APT|ROOM|RM|STE(?![A-Z])|HALLWAY)\\b.*|\\d+[A-Z]?|[A-Z])");
  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private class MyAptField extends AptField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // See if this looks like a box field
      if (checkBoxField(field, data)) return true;

      // At this point, we are either an apt or a city, and there are different ways to figure out
      // just which it is
      do {

        // If it looks like an apt, so be it
        Matcher match = APT_PATTERN.matcher(field);
        if (match.matches()) {
          field = match.group(1);
          if (field == null) field = match.group(2);
          break;
        }

        // Check next field.  if it looks like a zip code, this field must be a city, not an apt
        String nextFld = getRelativeField(+1);
        if (ZIP_PTN.matcher(nextFld).matches()) return false;

        // If next field does not start with BTWN, it must be a city and we must be an apt
        if (!nextFld.startsWith("BTWN:")) break;

        // Otherwise see if this field looks like a city
        if (checkCityField(field)) return false;

      } while (false);

      parse(field, data);
      return true;
    }
  }

  private boolean checkBoxField(String field, Data data) {
    Matcher match = BOX_PTN.matcher(field);
    if (!match.find()) return false;
    if (match.start() == 0) field = field.substring(match.end()).trim();
    if (!data.strBox.contains(field)) data.strBox = append(data.strBox, " / ", field);
    return true;
  }
  private static final Pattern BOX_PTN = Pattern.compile("\\bBO?X\\b");

  /**
   * Determine if field looks like a city field
   * @param field  field to be checked
   * @return true if it looks like a city field
   */
  private boolean checkCityField(String field) {

    // Check to see if this could possibly be a city
    if (!CITY_PATTERN.matcher(field).matches()) return false;

    // If we were not given a city set, assume this is a city
    if (!hasCityList) return true;

    // Otherwise, use it to check to see if this is a city or not
    return (isCity(field));
  }

  private static final Pattern CITY_PATTERN = Pattern.compile("[ A-Za-z]+");

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("N/A ")) field = field.substring(4).trim();
      if (field.endsWith(" N/A")) field = field.substring(0,field.length()-4).trim();
      if (field.startsWith("&")) field = field.substring(1).trim();
      if (field.endsWith("&")) field = field.substring(0,field.length()-1).trim();
      field = field.replaceAll("  +", " ");
      super.parse(field, data);
    }
  }

  private static final Pattern SKIP_INFO_PTN = Pattern.compile("[A-Z]+[0-9]* at(?: POS \\d+)?");
  private class MyInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (SKIP_INFO_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  private static final Pattern GPS_PTN = Pattern.compile("//maps\\.google\\.com.*?q=loc:([-+]?\\d{2,3}\\.\\d{6,})\\+([-+]?\\d{2,3}\\.\\d{6,})");
  private class MyGPS2Field extends GPSField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (match.matches()) {
        super.parse(match.group(1)+','+match.group(2), data);
      }
    }
  }
}
