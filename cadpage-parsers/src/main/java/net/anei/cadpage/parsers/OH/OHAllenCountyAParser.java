package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Allen County, OH
 */
public class OHAllenCountyAParser extends FieldProgramParser {

  public OHAllenCountyAParser() {
    super(CITY_LIST, "ALLEN COUNTY", "OH",
          "( Call:CODE_CALL! Place:PLACE! Addr:ADDRCITY/S6! ( Cross:X! | ID:ID! Pri:PRI! ) Date:DATETIME! Map:MAP! Unit:UNIT! Info:INFO! INFO/N+ Radio_Channel:CH! Alarm_Level:PRI/L! " +
          "| ADDRCITY2/S6 ID DATETIME CALL X UNIT MAP MAP_INFO GPS! ) END");
  }

  @Override
  public String getFilter() {
    return "interface@acso-oh.us";
  }

  private static final Pattern ALARM_LEVEL_PTN = Pattern.compile("Alarm Level (\\d) +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("ACSO Paging")) return false;

    Matcher match = ALARM_LEVEL_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strPriority = match.group(1);
      body = body.substring(match.end());
    }

    if (body.startsWith("Call:")) {
      if (!super.parseFields(body.split("\n"), data)) return false;
    } else {
      if (!super.parseFields(body.split("\\|", -1), data)) return false;
    }
    data.strAddress = data.strAddress.replace("LAKERIDGE DR", "DEERCREEK CIR");
    data.strCross = data.strCross.replace("LAKERIDGE DR", "DEERCREEK CIR");
    return true;
  }

  @Override
  public String getProgram() {
    return "PRI? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ADDRCITY2")) return new MyAddressCity2Field();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("MAP_INFO")) return new MyMapInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([EF]\\d\\d[A-Z]?) +(.*)");

  private class MyCodeCallField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2);
      } else if (field.equals(">New Call<")) {
        data.strCall = field;
      } else abort();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '/');
      super.parse(field, data);
    }
  }

  private class MyAddressCity2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(',');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();

        parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, city, data);
        if (data.strCity.length() == 0) {
          data.strCity = city;
        } else {
          data.strPlace = getLeft();
        }
      }

      field = field.replace('@', '/');
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY PLACE";
    }
  }

  private static final Pattern MAP_INFO_PTN = Pattern.compile("([A-Z]{3,4} \\d(?:-\\d)?|[A-Z][a-z]+ FD|[A-Z]{1,3}FD) *(.*)");
  private class MyMapInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = MAP_INFO_PTN.matcher(field);
      if (match.matches()) {
        data.strMap = append(data.strMap, "/", match.group(1));
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "MAP " + super.getFieldNames();
    }

  }

  private static final String[] CITY_LIST = new String[] {

      //Cities
      "DELPHOS",
      "LIMA",

      // Villages
      "BEAVERDAM",
      "BLUFFTON",
      "CAIRO",
      "ELIDA",
      "HARROD",
      "LAFAYETTE",
      "SPENCERVILLE",

      // Townships
      "AMANDA TWP",
      "AMERICAN TWP",
      "AUGLAIZE TWP",
      "BATH TWP",
      "JACKSON TWP",
      "MARION TWP",
      "MONROE TWP",
      "PERRY TWP",
      "RICHLAND TWP",
      "SHAWNEE TWP",
      "SPENCER TWP",
      "SUGAR CREEK TWP",

      // Census-designated places
      "FORT SHAWNEE",

      // Other unincorporated communities
      "ALLENTOWN",
      "CONANT",
      "GOMER",
      "HUME",
      "JOLIET",
      "KEMP",
      "LANDECK",
      "MAYSVILLE",
      "NEEDMORE",
      "OAKVIEW",
      "ROCKPORT",
      "ROUSCULP",
      "SCOTTS CROSSING",
      "SLABTOWN",
      "SOUTH WARSAW",
      "SOUTHWORTH",
      "WEST NEWTON",
      "WESTMINSTER",
      "YODER"
  };
}
