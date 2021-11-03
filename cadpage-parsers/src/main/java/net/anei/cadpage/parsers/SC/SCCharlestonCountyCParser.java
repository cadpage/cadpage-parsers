package net.anei.cadpage.parsers.SC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCCharlestonCountyCParser extends FieldProgramParser {

  public SCCharlestonCountyCParser() {
    super(CITY_LIST, "CHARLESTON COUNTY", "SC",
          "TICKET:EMPTY! EMPTY! RESPONDING_UNITS:EMPTY! UNIT! ADDRESS:EMPTY! " +
    //      " PLACE? ADDR? APT? X? CITY? END_MARK
            "( ( PLACE ADDR/Z APT! | ADDR/Z APT! ) ( X/Z CITY/Z END_MARK! | CITY END_MARK | X/Z END_MARK ) " +
            "| PLACE ADDR/Z X/Z CITY/Z END_MARK " +
            "| PLACE ADDR/Z X END_MARK " +
            "| ADDR/Z X CITY? END_MARK " +
            "| INTERSECT CITY? END_MARK " +
            "| PLACE ADDR/Z CITY/Z END_MARK " +
            "| PLACE? ADDR CITY END_MARK ) " +
          "INCIDENT_TYPE:EMPTY! CALL! EMPTY! INCIDENT_CHANNEL:EMPTY! CH? EMPTY! INCIDENT_/_DATE_/_TIME:EMPTY! ID! DATETIME! END");
    setupProtectedNames("LEWIS AND CLARK", "SALT AND PEPPER");
  }

  @Override
  public String getFilter() {
    return "FSAS@charlestoncounty.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Charleston Fire Department Notification")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strCity.equalsIgnoreCase("UNINCORPORATED")) data.strCity = "";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("APT +(.*)|BLDG .*|.* CONNECTOR");
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INTERSECT")) return new MyIntersectionField();
    if (name.equals("END_MARK")) return new MyEndMarkField();
    if (name.equals("ID")) return new IdField("\\d\\d-\\d{7}", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.contains(" AND ")) return false;
      return super.checkParse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(" AND ") && !field.contains("Dead End"))  return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  // Intersection is a cross street field that parses into the address field
  private class MyIntersectionField extends MyCrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (!super.checkParse(field, data)) return false;
      data.strAddress = data.strCross.replace(" AND ", " & ");
      data.strCross = "";
      return true;
    }

    @Override
    public String getFieldNames() {
      return "ADDR";
    }
  }

  private class MyEndMarkField extends EmptyField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() > 0) return false;
      String prevField = getRelativeField(-2);
      return (!prevField.equals("INCIDENT TYPE:") && !prevField.equals("INCIDENT CHANNEL"));
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "CHARLESTON",
      "FOLLY BEACH",
      "ISLE OF PALMS",
      "NORTH CHARLESTON",

      // Towns
      "AWENDAW",
      "HOLLYWOOD",
      "JAMES ISLAND",
      "JOHNS ISLAND",
      "KIAWAH ISLAND",
      "LINCOLNVILLE",
      "MCCLELLANVILLE",
      "MEGGETT",
      "MOUNT PLEASANT",
      "RAVENEL",
      "ROCKVILLE",
      "SEABROOK ISLAND",
      "SULLIVANS ISLAND",
      "SUMMERVILLE",
      "WEST ASHLEY",

      // Census-designated place
      "LADSON",

      "UNINCORPORATED"
  };
}
