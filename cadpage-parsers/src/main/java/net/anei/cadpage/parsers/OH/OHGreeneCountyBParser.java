package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHGreeneCountyBParser extends HtmlProgramParser {

  public OHGreeneCountyBParser() {
    super("GREENE COUNTY", "OH",
          "( SRC UNITS:UNIT! INCIDENT_NUMBER:ID? CALL_TYPE:CALL! LOCATION:ADDRCITY! ( CROSS_STREETS:X! ( NAME:NAME! QUADRANT:MAP! | QUADRANT:MAP! NAME:NAME? ) DATE:DATETIME! INCIDENT_NUMBER:ID? NARRATIVE:INFO/N+ " +
                                                                                   "| NARRATIVE:X! DATE:DATETIME! INCIDENT_NUMBER:ID? NARRATIVE:INFO INFO/N+? MAP:MAP! " +
                                                                                   ") " +
          "| Call:CODE_CALL! ( Place:ADDRCITY/SP! Cross:X! ID:ID! PRI:PRI! Date:DATETIME! Map:MAP? Units:UNIT! " +
                            "| Name:PLACE! Address:ADDRCITY! Cross:X! Units:UNIT! Incident_Number:ID! Call_Time:SKIP! Dispatch_Time:DATETIME! Quadrant:MAP! " +
                            ") Narrative:INFO/N+ " +
          ")");
    setupMultiWordStreets(MULTI_WORD_STREET_LIST);
  }

  @Override
  public String getFilter() {
    return "@xi.xenia.oh.us,@ci.xenia.oh.us,@beavercreekohio.gov,@ci.fairborn,@fairbornoh.gov";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Automatic R&R Notification:")) return false;
    body = body.replace("    INCIDENT NUMBER:", "<div/>INCIDENT NUMBER:");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("([A-Z0-9]{3,5})(?:-|  ) *(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        data.strCall = match.group(2);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?) +([A-Z]*\\d+[A-Z]*|[A-Z]{1,2})", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
      Matcher match = ADDR_APT_PTN.matcher(data.strCity);
      if  (match.matches()) {
        data.strCity = match.group(1);
        data.strApt = match.group(2);
        data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strApt);
      }
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\*+\\d\\d?/\\d\\d?/\\d{4}\\*+|\\d\\d:\\d\\d:\\d\\d|[a-z]+|-");
  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }

  private static final String[] MULTI_WORD_STREET_LIST = new String[]{
      "ALPHA BELLBROOK",
      "AMY LYNN",
      "ANKENEY MILL",
      "APPLE GROVE",
      "BASCULE BRIDGE",
      "BAYBERRY COVE",
      "BEAVER VALLEY",
      "BEAVER VU",
      "BIG WOODS",
      "BONNIE DALE",
      "BOULDER RIDGE",
      "BROWN BARK",
      "BRUSH ROW",
      "CEDAR VILLAGE",
      "CENTER POINT",
      "CLEAR VIEW",
      "COLONEL GLENN",
      "CORNERSTONE NORTH",
      "COUNTY LINE",
      "CREEKSIDE TRAIL BIKE",
      "DAYTON SPRINGFIELD",
      "DAYTON XENIA",
      "DAYTON YELLOW SPRINGS",
      "DOUBLE EAGLE",
      "EAGLE POINT",
      "EAGLE VIEW",
      "ECHO HILL",
      "EDEN ROC",
      "EDITH MARIE",
      "EL CAMINO",
      "FAIRFIELD COMMONS",
      "FALLING LEAF",
      "FOREST DELL",
      "FOX RIDGE",
      "GERMANY TREBEIN",
      "GRAND PORTAGE",
      "GRAND VISTA",
      "GRANGE HALL",
      "GRANGE VIEW",
      "GREENE VALLEY",
      "GREENE WAY",
      "HIDDEN CREEK",
      "HONEY JANE",
      "INDIAN RIPPLE",
      "KING ARTHUR",
      "KING EDWARD",
      "KING HENRY",
      "LITTLE MAIN",
      "LITTLE MIAMI",
      "LITTLE SUGARCREEK",
      "LONG ISLAND",
      "MEADOW BRIDGE",
      "MEADOW WOOD",
      "MIDDLE RUN",
      "MILL POND",
      "NAPA VALLEY",
      "NORTH FIELD",
      "NORTH HAVEN",
      "NUTTER PARK",
      "OFFICE PARK",
      "PARK OVERLOOK",
      "PEBBLE CREEK",
      "PINE BLUFF",
      "PINE VIEW",
      "POSSUM RUN",
      "RED APPLE",
      "RED OAK",
      "REGENT PARK",
      "RISING HILL",
      "RISING SPRING",
      "RIVER CREST",
      "RIVER EDGE",
      "RIVER RIDGE",
      "RONA PARKWAY",
      "SABLE RIDGE",
      "SENTINEL RIDGE",
      "SHADY OAK",
      "SOUTH POINT",
      "SPRING VALLEY ALPHA",
      "SPRING VALLEY PAINTERSVILLE",
      "STRAIGHT ARROW",
      "SUGAR RUN",
      "TOLL GATE",
      "VALLEY OAK",
      "VAN TRESS",
      "VILLAGE GREEN",
      "WEST PARK",
      "WHITES CORNER",
      "WILLOW RUN",
      "WILMINGTON DAYTON",
      "WOOD RIDGE",
      "WRIGHT CYCLE",
      "XENIA JAMESTOWN CONNECTOR BIKE",
      "XENIA TOWNE",
      "YELLOW BRICK",
      "YELLOW ROSE",
      "YELLOW SPRINGS FAIRFIELD"
  };
}
