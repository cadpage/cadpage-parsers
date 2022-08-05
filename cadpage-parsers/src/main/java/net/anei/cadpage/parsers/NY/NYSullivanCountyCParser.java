package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYSullivanCountyCParser extends FieldProgramParser {

  public NYSullivanCountyCParser() {
    super(CITY_LIST, "SULLIVAN COUNTY", "NY",
          "Fire_Call_Type:CALL! EMS_Call_Type:CALL! Address:ADDRCITY/S! Common_Name:PLACE! Closest_Intersection:X! Additional_Location_Info:INFO?! Assigned_Units:UNIT! Narrative:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "911@co.sullivan.ny.us.911@sullivanny.us,777";
  }

  private static final String MARKER = "Sullivan County 911: (911 Page) ";
  private static final Pattern DELIM = Pattern.compile("\n| +(?=(?:Fire Call Type|EMS Call Type|Address|Common Name|Closest Intersection|Additional Location Info|Assigned Units|Narrative):)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("911 Page")) break;

      if (body.startsWith(MARKER)) {
        body = body.substring(MARKER.length()).trim();
        break;
      }

      // If we fall through to here, someone has been using IAR local edits to "improve" the
      // alert message and we have to fix things :(
      data.strSource = subject;

    } while (false);

    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) {
        data.strCall = field;
      }
      else {
        if (data.strCall.startsWith(field)) return;
        if (field.startsWith(data.strCall)) return;
        data.strCall = data.strCall + "/" + field;
      }
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*) APT/BLD\\b *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{

      // Cities
      "BETHEL",
      "CALLICOON",
      "COCHECTON",
      "DELAWARE",
      "FALLSBURG",
      "FORESTBURGH",
      "FREMONT",
      "HIGHLAND",
      "LIBERTY",
      "LUMBERLAND",
      "MAMAKATING",
      "NEVERSINK",
      "ROCKLAND",
      "THOMPSON",
      "TUSTEN",

      // Villages
      "BLOOMINGBURG",
      "JEFFERSONVILLE",
      "LIBERTY",
      "MONTICELLO",
      "WOODRIDGE",
      "WURTSBORO",

      // Census-designated places
      "BARRYVILLE",
      "BRIDGEVILLE",
      "CALLICOON",
      "ELDRED",
      "FALLSBURG",
      "GRAHAMSVILLE",
      "HANKINS",
      "HORTONVILLE",
      "HURLEYVILLE",
      "KAUNEONGA LAKE",
      "KIAMESHA LAKE",
      "LAKE HUNTINGTON",
      "LIVINGSTON MANOR",
      "LOCH SHELDRAKE",
      "MONGAUP VALLEY",
      "MOUNTAIN DALE",
      "NARROWSBURG",
      "ROCK HILL",
      "ROSCOE",
      "SMALLWOOD",
      "SOUTH FALLSBURG",
      "SWAN LAKE",
      "WHITE LAKE",
      "WOODBOURNE",
      "WURTSBORO HILLS",

      // Hamlets
      "DEBRUCE",
      "FERNDALE",
      "GLEN SPEY",
      "HANDSOME EDDY",
      "HARRIS",
      "HAVEN",
      "LEW BEACH",
      "MINISINK FORD",
      "SPRING GLEN",
      "SUMMITVILLE",
      "TREADWELL",
      "WHITE SULPHUR SPRINGS"
  };
}
