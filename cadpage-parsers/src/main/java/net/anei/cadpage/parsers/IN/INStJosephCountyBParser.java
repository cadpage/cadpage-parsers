package net.anei.cadpage.parsers.IN;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeSet;
import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class INStJosephCountyBParser extends HtmlProgramParser {

  public INStJosephCountyBParser() {
    super("ST JOSEPH COUNTY", "IN",
          "( SELECT/2 ADDRCITY1 CALL X PLACE SIMPLE CH UNIT2! INFO/N+? ( ID2 INFO/N+? GPS1 GPS2 | GPS1 GPS2 ) " +
          "| SELECT/3 TIMEDATE3! Location:EMPTY! ADDR! Loc_Name:EMPTY! PLACE " +
                    "( Priority:EMPTY! PRI Descr:EMPTY! CALL! Building:EMPTY! APT Subdivision:EMPTY! APT Floor:EMPTY! APT Apt/Unit:EMPTY! APT " +
                      "Cross_Strs:EMPTY! X? UNITS_DISPATCHED:EMPTY! UNIT! COMMENTS:EMPTY! INFO/N+ PREMISE_HAZARD:EMPTY! ALERT " +
                      "Location:EMPTY! Loc_Name:EMPTY! Loc_Descr:EMPTY! INFO City:EMPTY! CITY Zip_Code:EMPTY " +
                      "Area:EMPTY! MAP Sector:EMPTY! MAP/D Beat:EMPTY! MAP/D Map_Book:EMPTY! BOX3+? INCIDENT:EMPTY! Inc_#:EMPTY! SKIP Inc_#:EMPTY! ID! Priority:EMPTY! " +
                      "Inc_Type:EMPTY! CODE3 Descr:EMPTY! Mod_Circum:EMPTY! Created:EMPTY! Caller:EMPTY! NAME Phone:EMPTY! " +
                    "| Loc_Descr:EMPTY! INFO City:EMPTY! CITY Building:EMPTY! APT Subdivision:EMPTY! APT Floor:EMPTY! APT Apt/Unit:EMPTY! APT " +
                      "Zip_Code:EMPTY! ZIP Cross_Strs:EMPTY! X " +
                      "Area:EMPTY! MAP Sector:EMPTY! MAP/D Beat:EMPTY! MAP/D Map_Book:EMPTY! BOX3+? Inc_#:EMPTY! SKIP Inc_#:EMPTY! ID! Priority:EMPTY! PRI " +
                      "Inc_Type:EMPTY! CODE3 Descr:EMPTY! CALL! Mod_Circum:EMPTY! Created:EMPTY! Caller:EMPTY! NAME Phone:EMPTY! PHONE UNITS_DISPATCHED:EMPTY! UNIT! " +
                      "COMMENTS:EMPTY! INFO PREMISE_HAZARD:EMPTY! ALERT " +
                    ") " +
          "| SELECT/4 Location:ADDR! Loc_Name:PLACE! Priority:PRI! Descr:CALL! Building:APT! Subdivision:APT! Floor:APT! Apt/Unit:APT! Cross_Strs:X! " +
                    "UNITS_DISPATCHED:EMPTY! UNIT! COMMENTS:EMPTY! INFO/N+ PREMISE_HAZARD:ALERT! Location:SKIP! Loc_Name:SKIP! City:CITY! Zip_Code:SKIP! " +
                    "Area:MAP! Sector:MAP/D! Beat:MAP/D! Map_Book:BOX4! INCIDENT:SKIP! Inc_#:SKIP! Inc_#:ID! Priority:PRI! Descr:SKIP! Mod_Circum:SKIP! " +
                    "Created:SKIP! Caller:NAME! Phone:PHONE! " +
          "| ( ADDRCITY1 CALL DUP_CALL? " +
            "| CALL DUP_CALL? ADDRCITY2? " +
            ") " +
            "( UNIT2! " +
            "| DATEMARK " +
            "| X? PLACE? SIMPLE? CH/L+? ( UNIT2! | DATEMARK ) " +
            ") INFO2/N+? ( ID1 INFO/N+? GPS1 GPS2 | GPS1 GPS2 ) END " +
          ")");
  }

  @Override
  public String getFilter() {
    return "@sjc911.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private boolean getInfo = false;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    int pt = body.indexOf("\nIMPORTANT NOTICE!");
    if (pt >= 0) body = body.substring(0, pt).trim();
    getInfo = false;

    if (subject.startsWith("Automatic R&R Notification: ")) {
      setSelectValue("1");
      return super.parseHtmlMsg(subject, body, data);
    }
    else if (subject.equals("!")) {
      setSelectValue("2");
      if (!parseFields(body.split("\n"), data)) return false;
      if (data.strUnit.length() == 0) data.msgType = MsgType.RUN_REPORT;
      return true;
    }
    else if (subject.startsWith("Dispatch Notification for Incident ")) {
      setSelectValue("4");
      return parseFields(body.split("\n"), data);
    }
    else if (subject.length() > 0) {
      data.strSource = subject;
      setSelectValue("3");
      return super.parseHtmlMsg(subject, body, data);
    }
    else return false;
  }

  @Override
  public String getProgram() {
    return "SRC? " + super.getProgram();
  }

  private static final Pattern DASHES = Pattern.compile("-{3,}");

  @Override
  protected boolean parseFields(String[] fields, Data data) {

    // Remove dash fields from variants 3 and 4
    if (getSelectValue().equals("3") || getSelectValue().equals("4")) {
      List<String> fieldList = new ArrayList<>();
      for (String fld : fields) {
        if (!DASHES.matcher(fld).matches()) fieldList.add(fld);
      }
      fields = fieldList.toArray(new String[0]);
    }
    return super.parseFields(fields, data);
  }

  private static final String GPS_PTN = "-?\\d{2}\\.\\d{4,}|-361";

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DUP_CALL")) return new MyDupCallField();
    if (name.equals("ADDRCITY1")) return new MyAddressCity1Field();
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("SIMPLE")) return new SkipField("Simple|Complex|Low|Medium|High", true);
    if (name.equals("UNIT2")) return new UnitField("|(?:(?:[A-Z]+\\d+[A-Z]?|[A-Z]+(?:FD|FDEN|FDCH|FDGR|FDRE|FDTA|FDTR|FOAM)|(?:NLF|OSOLO|POR)[A-Z]+|BTFDTA|BUTWPTA|CFDMULE|CLETWPCH|CLETWPTR|EDWEN|EDWTA|NLFDPOLK|SBFINVST|SBPConv|SMCAS|STARKEAMB|X+|mobile\\d+|Mutual Aid)\\b[, ]*)+", true);
    if (name.equals("DATEMARK")) return new SkipField("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}", true);
    if (name.equals("INFO2")) return new MyInfo2Field();
    if (name.equals("GPS1")) return new GPSField(1, GPS_PTN, true);
    if (name.equals("GPS2")) return new GPSField(2, GPS_PTN, true);
    if (name.equals("ID1")) return new IdField("\\[.*\\]", true);
    if (name.equals("ID2")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
    if (name.equals("TIMEDATE3")) return new MyTimeDate3Field();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("BOX3")) return new MyBox3Field();
    if (name.equals("BOX4")) return new MyBox4Field();
    if (name.equals("CODE3")) return new MyCode3Field();
    if (name.equals("ALERT")) return new MyAlertField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripProQA(field);
      super.parse(field, data);
    }
  }

  private class MyDupCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals(getRelativeField(-1))) return true;
      if (field.length() == 1) return true;
      int fldLen = field.length();
      field = stripProQA(field);
      if (field.length() == fldLen && CALL_LIST.getCode(field) == null) return false;
      data.strCall = append(data.strCall, " - ", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern VALID_ADDRESS_PTN = Pattern.compile("(?:<UNKNOWN>|\\d|LAT:).*|.*(?:,| at |\\bMM\\d+\\b).*|.*/.*");

  private class MyAddressCity1Field extends MyAddressCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (PROQA_PTN.matcher(field).matches()) return false;
      if (!PROQA_PTN.matcher(getRelativeField(+1)).matches() &&
          !VALID_ADDRESS_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }

  private class MyAddressCity2Field extends MyAddressCityField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_PLACE_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      String tmp = stripFieldStart(field, "<UNKNOWN>");
      if (tmp.length() > 0) field = tmp;
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      if (field.equals("No Cross Streets Found")) return true;
      if (!field.contains("/")) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern NOT_PLACE_PTN = Pattern.compile("Simple|Complex|[A-Z]+\\d+_\\d+");
  private class MyPlaceField extends PlaceField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_PLACE_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }
  }

  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (getInfo) {
        data.strSupp = append(data.strSupp, "\n", field);
        getInfo = false;
      } else if (field.equals("-")) getInfo = true;
    }
  }

  private static final Pattern PROQA_PTN = Pattern.compile("(.*?) *\\(PROQA\\)", Pattern.CASE_INSENSITIVE);
  private static String stripProQA(String field) {
    Matcher match = PROQA_PTN.matcher(field);
    if (match.matches()) field = match.group(1);
    return field;
  }

  private static final Pattern TIME_DATE_3_PTN = Pattern.compile("(\\d\\d?:\\d\\d:\\d\\d [AP]M) (\\d\\d?/\\d\\d?/\\d{4})");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyTimeDate3Field extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_3_PTN.matcher(field);
      if (!match.matches()) abort();
      setTime(TIME_FMT, match.group(1), data);
      data.strDate = match.group(2);
    }
  }

  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        data.strCity = field;
      }
    }
  }

  private class MyBox3Field extends BoxField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      if (!field.equals("RESPONSEGRID") && !field.equals(",")) {
        super.parse(field, data);
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyBox4Field extends BoxField {
    @Override
    public void parse(String field, Data data) {
      for (String part : field.split(",")) {
        part = part.trim();
        if (!part.equals("RESPONSEGRID")) {
          data.strBox = append(data.strBox, "-", part);
        }
      }
    }
  }

  private class MyCode3Field extends CodeField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('-');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private class MyAlertField extends AlertField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("TYPE")) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr, boolean cross) {
    if (cross) {
      int pt = addr.indexOf('/');
      if (pt > 0) addr = addr.substring(0,pt).trim();
      if (checkAddress(addr) == STATUS_FULL_ADDRESS) {
        pt = addr.indexOf(' ');
        if (pt >= 0) addr = addr.substring(pt+1).trim();
      }
    } else {
      addr = stripFieldStart(addr, "<UNKNOWN>");
    }
    return super.adjustMapAddress(addr);
  }

  private final static CodeSet CALL_LIST = new CodeSet(
      "Abdominal Pain/Problems",
      "Accident",
      "Allergies/Envenomations",
      "Animal Bites/Attacks",
      "Assault/Sexual Assault",
      "Assist",
      "Back Pain",
      "Breathing Problems",
      "Cardiac Arrest",
      "Chest Pain",
      "Childbirth",
      "Choose Call Type",
      "Citizen Assist",
      "CO Investigation",
      "Commercial Fire",
      "Diabetic",
      "Electrical",
      "Falls",
      "Fire Assist",
      "Gas Leak",
      "Headache",
      "Heart",
      "Heat Exposure",
      "Hemorrhage/Laceration",
      "Investigation",
      "Man Down",
      "Medical",
      "Mutual Aid",
      ">New Call<",
      "Outside Fire",
      "Overdose",
      "Penetrating Trauma",
      "Psych/Suicidal/Attempt",
      "Residential Fire Alarm",
      "Seizure",
      "Sick Person",
      "Smoke",
      "Stabbing",
      "Stroke",
      "Structure Fire",
      "Transfer/Interfacility",
      "Traumatic Injuries",
      "Unconscious/Fainting",
      "Unknown Medical",
      "Vehicle Fire"
  );
}
