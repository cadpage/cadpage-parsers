package net.anei.cadpage.parsers.NC;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCRowanCountyParser extends DispatchOSSIParser {

  public NCRowanCountyParser() {
    super(CITY_CODES, "ROWAN COUNTY", "NC",
          "( CANCEL ADDR! CITY? XPLACE+ " +
          "| FYI? ( BAD_ID " +
                 "| ADDR/Z CITY! " +
                 "| ( ADDR | CALL P? ADDR! UNIT? ( CITY | X/Z CITY | X/Z X/Z CITY | ) ) " +
                 ") XPLACE+? INFO/N INFO/NZ+? NAME PH " +
          ")");
    setupSpecialStreets("NEW ST");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "9300,CAD,CAD@rowancountync.gov,CAD@co.rowan.nc.us,messaging@iamresponding.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean noParseSubjectFollow() { return true; }
    };
  }

  private static Set<String> unitSet = new HashSet<>();

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    boolean ok = body.startsWith("CAD:");
    if (!ok) body = "CAD:" + body;
    if (subject.equals("Cleveland FD")) {
      data.expectMore = true;
      body = body.replace('\n', ';');
    }
    body = body.replace("SECOND DISPATCH/", "SECOND DISPATCH;");
    body = body.replace("WORKING FIRE/", "WORKING FIRE;");
    unitSet.clear();
    lastIdField = false;
    if (!super.parseMsg(body, data)) return false;

    // If we didn't have the CAD: prefix and don't have a city, this is just
    // to chancy to accept.  Unless this was flagged as a prealert.  We will take that.
    if (!ok && data.strCity.length() == 0 && !data.strCall.endsWith("(PREALERT)")) return false;

    // If the Apt looks like an NCDavidsonCountyA city code, reject
    if (data.strPlace.length() == 0 && data.strApt.length() > 0 && NCDavidsonCountyAParser.isCityCode(data.strApt)) return false;
    if (data.strApt.equals("CSI")) return false;

    if (data.strCity.equals("OUT OF COUNTY")) {
      data.defCity = "";
    }

    return data.strAddress.length() > 0;
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equals("OUT OF COUNTY")) return "";
    return city;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("CANCEL")) return new BaseCancelField("COMMND STFF NOTFID\\b.*|.*\\bCOMMAND ESTABLISHED|CONFIRMED DOA|CPR IN PROGRESS|DUPLICATE PAGE|OFF DUTY PERSONNEL NOTIFY|.*\\bRADIO ACTIVATED|SECOND DISPATCH");
    if (name.equals("BAD_ID")) return new MyBadIdField();
    if (name.equals("P")) return new MyPrealertField();
    if (name.equals("CALL_PAGE")) return new CallField("PAGE / CALL .*", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("XPLACE")) return new MyCrossPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("PH")) return new PhoneField("\\d{10}", true);
    return super.getField(name);
  }

  /*
   * If we find an ID field, the means this is really a NCStanlyCounty alert, and we should reject it.
   */
  private class MyBadIdField extends SkipField {
    MyBadIdField() {
      super("\\d{3,}", true);
    }

    @Override
    public void parse(String field, Data data) {
      abort();
    }
  }

  private class MyPrealertField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.equals("P")) return false;
      data.strCall = append(data.strCall, " ", "(PREALERT)");
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern BAD_CALL_PTN = Pattern.compile("[^\\(]*[^ ]/[^ ]+/[^ ].*|[A-Z]\\d+[A-Z]?-.*");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (BAD_CALL_PTN.matcher(field).matches()) abort();
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*)(?: - |~)(.*)");
  private static final Pattern BAD_CITY_PTN = Pattern.compile("\\b(?:[NSEW]|[NS][EW])\\b");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = match.group(2).trim();

        // Cities that look like directions is a feature of Davidson County alerts
        if (BAD_CITY_PTN.matcher(data.strCity).find()) abort();
        if (isValidCrossStreet(data.strCity)) abort();
        if (data.strCity.endsWith(" CO")) data.strCity += "UNTY";
      }
      if (field.endsWith(" DR DR") || field.endsWith(" RD RD")) {
        field = field.substring(0,field.length()-3);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  // Check for city append with following cross/place field :(
  private static final Pattern CITY_PLACE_PTN = Pattern.compile("([A-Z]{3,5})((?:DIST:|\\(S\\)).*)");
  private class MyCityField extends MyCrossPlaceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      field = field.replace("`", "");
      String city;
      if (field.length() <= 5) {
        city = field;
        field = "";
      } else {
        Matcher match = CITY_PLACE_PTN.matcher(field);
        if (!match.matches()) return false;
        city = match.group(1);
        field = match.group(2);
      }
      city = CITY_CODES.getProperty(city);
      if (city == null) return false;
      data.strCity = city;
      if (field.length() > 0) super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CITY " + super.getFieldNames();
    }
  }

  private static final Pattern CODE_DESC_PTN = Pattern.compile("(\\d{1,3}[A-Z]\\d{1,2}) +(.*)");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|ROOM|LOT) *(.*)");
  private class MyCrossPlaceField extends MyPlaceField {

    @Override
    public void parse(String field, Data data) {

      // If it looks like a phone number (also 10 digits) accept it
      if (field.length() == 10 && NUMERIC.matcher(field).matches()) {
        if (field.startsWith("20")) abort();
        data.strPhone = field;
        return;
      }

      // This is a catchall field that can contains a lot of things
      // See if it is a call code followed by a description
      Matcher match = CODE_DESC_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strSupp = append(data.strSupp, "\n", match.group(2));
        return;
      }

      // See if is an apt/lot number
      match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        return;
      }

      // See if this looks like a set of cross streets
      if (field.endsWith(" DR DR") || field.endsWith(" RD RD")) {
        field = field.substring(0,field.length()-3);
      }
      if (field.endsWith("CREEK") || field.endsWith("XING") || isValidAddress(field)) {
        data.strCross = append(data.strCross, " & ", field);
        return;
      }

      // Otherwise it is a place field
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE INFO? " + super.getFieldNames() + " X PHONE";
    }
  }

  /**
   * Handles the common place field processinging common to both the MyOptionalPlaceField and
   * MyCrossPlaceField classes
   */
  private static final Pattern PLACE_PTN = Pattern.compile("(.*)\\(S\\)(.*)\\(N\\)(.*)");
  private abstract class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_PTN.matcher(field);
      if (match.matches()) {
        for (int ii = 1; ii<=3; ii++) {
          processPart(match.group(ii).trim(), data);
        }
      }

      else {
        if (data.strCall.length() == 0 || data.strCall.startsWith("PAGE / CALL")) {
          data.strCall = field;
        } else {
          processPart(field, data);
        }
      }
    }

    private void processPart(String part, Data data) {
      if (part.length() == 0) return;

      boolean apt = false;

      Matcher match = APT_PTN.matcher(part);
      if (match.matches()) {
        apt = true;
        part = match.group(1);
      }

      else if (data.strCross.length() == 0 && part.length() <= 5 && !part.contains(" ") && !part.equals("MM")) {
        apt = true;
      }

      if (apt) {
        if (!part.equals(data.strApt)) data.strApt = append(data.strApt, "-", part);
      }
      else if (!data.strPlace.endsWith(part)) {
        data.strPlace = append(data.strPlace, " - ", part);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE? APT";
    }
  }

  private class MyUnitField extends Field {

    public MyUnitField() {
      setPattern(UNIT_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      addUnit(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH UNIT";
    }
  }

  private boolean lastIdField = false;

  private static final Pattern MAP_PTN = Pattern.compile("\\d{3,4}");
  private static final Pattern UNIT_PTN = Pattern.compile("OPS.*|tac.*|\\d\\d|[A-Z]+\\d+[A-Z]?|\\d+[A-Z]+\\d|[A-z0-9]+,[-A-Z0-9,]+|[A-Z]{2}|DCC");
  private static final Pattern ID_PTN = Pattern.compile("\\d{6,9}");
  private static final Pattern OPT_INFO_PTN = Pattern.compile("(?![A-Z]{0,4}DIST:|\\d{1,3}[A-Z]\\d{1,2} ).*(?:[a-z\\{'`]|\\bACTIVATION\\b|\\bHOLD\\b|\\bON THE\\b|\\bCALLER\\b - |\\bLOCATED\\b|\\bUDTS:|\\bREF\\b|\\bREFERENCE\\b|\\bREQUESTING\\b).*");
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("Radio Channel: *(.*)");
  private static final Pattern SHORT_PLACE_PTN = Pattern.compile("[- A-Z0-9()#&`']{1,40}");
  private class MyInfoField extends InfoField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return checkParse(field, data, false);
    }

    @Override
    public void parse(String field, Data data) {
      checkParse(field, data, true);
    }

    private boolean checkParse(String field, Data data, boolean force) {

      if (field.length() == 0) {
        lastIdField = false;
        return force;
      }

      if (field.startsWith("{FROM ") || field.startsWith("{Call created") ||
          field.equals("}") || field.startsWith("Event spawned")) {
        lastIdField = false;
        return true;
      }

      if (ID_PTN.matcher(field).matches()) {
        if (data.strCallId.length() == 0) data.strCallId = field;
        lastIdField = true;
        return true;
      }
      boolean setPlace = lastIdField;
      lastIdField = false;

      if (data.strMap.length() == 0) {
        Matcher match = MAP_PTN.matcher(field);
        if (match.matches()) {
          data.strMap = field;
          return true;
        }
      }

      Matcher match = UNIT_PTN.matcher(field);
      if (match.matches()) {
        addUnit(field, data);
        return true;
      }

      if (field.startsWith("**")) {
        data.strSupp = append(data.strSupp, "\n", field);
        return true;
      }

      if (! force) {
        if (field.contains("(S)")) return false;
        if (!field.contains(" ")) return false;
        if (field.length() < 50 && !OPT_INFO_PTN.matcher(field).matches()) return false;
      }

      if (isValidAddress(field)) {
        data.strCross = append(data.strCross, " & ", field);
        return true;
      }

      match = INFO_CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        addUnit(match.group(1), data);
        return true;
      }

      if (data.strCall.length() == 0 || data.strCall.startsWith("PAGE / CALL ")) {
        data.strCall = field;
        return true;
      }

      if (setPlace && SHORT_PLACE_PTN.matcher(field).matches()) {
        data.strPlace = append(data.strPlace, " - ", field);
      } else {
        super.parse(field, data);
      }
      return true;
    }

    @Override
    public String getFieldNames() {
      return "CALL? MAP CH UNIT ID PLACE INFO X";
    }
  }

  private void addUnit(String field, Data data) {
    for (String unit : field.split(",")) {
      unit = unit.trim();
      if (unit.length() == 0) continue;
      if (!unitSet.add(unit)) continue;
      if (unit.startsWith("OPS") || unit.startsWith("tac")) {
        data.strChannel = append(data.strChannel, ",", unit);
      } else {
        data.strUnit = append(data.strUnit, ",", unit);
      }
    }
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "485 CEDAR SPRINGS RD",                 "+35.630400,-80.534079"
  });

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CHGV", "CHINA GROVE",
      "CITYL","COMM_L",
      "CLEV", "CLEVELAND",
      "CLVD", "CLEVELAND",
      "COOL", "COOLEEMEE",
      "ESPN", "EAST SPENCER",
      "FATH", "FAITH",
      "GOLD", "GOLD HILL",
      "GRQY", "GRANITE QUARRY",
      "KAN",  "KANNAPOLIS",
      "KANN", "KANNAPOLIS",
      "LAND", "LANDIS",
      "MOCK", "MOCKSVILLE",
      "MOOR", "MOORESVILLE",
      "MTUL", "MT ULLA",
      "NWLN", "NEW LONDON",
      "RICH", "RICHFIELD",
      "ROCK", "ROCKWELL",
      "SALS", "SALISBURY",
      "SALSD","SALISBURY",
      "SPEN", "SPENCER",
      "WOOD", "WOODLEAF",

      // Cabarrus County, NC
      "CON",  "CONCORD",
      "CONC", "CONCORD",
      "MP",   "MT PLEASANT",

      // Iredell County, NC
      "STA",  "STATESVILLE",

      // Out of County
      "OOC",  "OUT OF COUNTY"
  });
}
