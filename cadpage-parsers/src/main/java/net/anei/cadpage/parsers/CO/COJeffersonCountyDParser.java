package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COJeffersonCountyDParser extends FieldProgramParser {

  public COJeffersonCountyDParser() {
    super(CITY_LIST, "JEFFERSON COUNTY", "CO",
          "CALL ADDR APT ( SELECT/1 EMPTY? CITY X MAP? GPS1/d GPS2/d UNIT UNIT/C+? CH ID DATETIME! " +
                        "| PLACE X UNIT UNIT/C+? ID DATETIME! " +
                        ") INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CADPage@jeffcom911.org,messaging@iamresponding.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    // They **REALLY** mangled this
    int pt = body.indexOf("\n\n<html>");
    if (pt >= 0) body = body.substring(0, pt).trim();
    return super.parseHtmlMsg(subject, body, data);
  }

  private static final Pattern DELIM = Pattern.compile(" *\\| *|\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Message")) data.strSource = subject;

    if (body.startsWith("CAUTION: ")) {
      int pt = body.indexOf('\n');
      if (pt < 0) return false;
      body = body.substring(pt+1).trim();
    }

    // Reject COJeffersonCountyE
    if (body.startsWith("Unit:")) return false;

    if (body.startsWith("Incident Notification:")) {
      setSelectValue("2");
      body = body.substring(22).trim();
    } else {
      setSelectValue("1");
    }

    int pt = body.indexOf("\n\nThis email");
    if (pt >= 0) body = body.substring(0,pt).trim();
    String[] flds = DELIM.split(body);
    if (flds.length < 8) flds = body.split(",");
    return parseFields(flds, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Za-z0-9]+)-(\\S.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(" - ");
      if (pt >= 0) {
        data.strPlace = field.substring(0, pt).trim();
        field = field.substring(pt+3).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|SUITE|LOT) *(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Apt:");
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strSource.equals("Fairmount Fire") && !isCity(field)) {
        Matcher match = APT_PTN.matcher(field);
        if (match.matches()) {
          data.strApt = append(data.strApt, "-", match.group(1));
        } else {
          data.strPlace = field;
        }
      }
      else if (field.equals("UNINC JEFFERSON")) {
        data.strCity = "JEFFERSON COUNTY";
      }
      else {
        data.strCity = stripFieldStart(field, "UNINC ");;
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY APT PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("Unk Cross Street", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern NOT_MAP_PTN = Pattern.compile("\\d{8,}");
  private class MyMapField extends MapField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (NOT_MAP_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.equals("NOT FOUND")) return;
      super.parse(field, data);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(" - ");
      if (pt >= 0) field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d(?:/\\d{4})?) (\\d\\d:\\d\\d:\\d\\d)|(\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      String date =  match.group(1);
      if (date != null) {
        data.strDate = date;
        data.strTime = match.group(2);
      } else {
        data.strTime = match.group(3);
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Unincoprporated jefferson county
      "UNINC JEFFERSON",

      // Cities
      "ARVADA",
      "EDGEWATER",
      "GOLDEN",
      "LAKEWOOD",
      "LITTLETON",
      "WESTMINSTER",
      "WHEAT RIDGE",
      "TOWNS",
      "BOW MAR",
      "LAKESIDE",
      "MORRISON",
      "MOUNTAIN VIEW",
      "SUPERIOR",

      // Census-designated places
      "APPLEWOOD",
      "ASPEN PARK",
      "BROOK FOREST",
      "COAL CREEK",
      "COLUMBINE",
      "DAKOTA RIDGE",
      "EAST PLEASANT VIEW",
      "EVERGREEN",
      "FAIRMOUNT",
      "GENESEE",
      "IDLEDALE",
      "INDIAN HILLS",
      "KEN CARYL",
      "KITTREDGE",
      "WEST PLEASANT VIEW",

      // Unincorporated communities
      "BUFFALO CREEK",
      "CONIFER",
      "FOXTON",
      "PINE",
      "PINE JUNCTION",
      "PLASTIC"
  };
}
