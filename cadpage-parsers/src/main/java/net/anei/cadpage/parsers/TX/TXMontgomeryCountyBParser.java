package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Montgomery County, TX (MCHD EMS)
 */
public class TXMontgomeryCountyBParser extends DispatchProQAParser {

  public TXMontgomeryCountyBParser() {
    super(CITY_LIST, "MONTGOMERY COUNTY", "TX",
          "( Comment:INFO/G! ID:ID2! ( UNIT:UNIT2! | UNIT2! ) " +
          "| ID:ID! PRI:PRI? UNIT:UNIT! PRI:PRI? CALL:CALL! ( NOTES:INFO/R! INFO/N+" +
                                                           "| PLACE:PLACE! APT:APT? ADDR:ADDR! ( X-STREETS:X! MAP:MAP! CITY:CITY! CROSS_STREETS:SKIP? | CROSS_STREETS:X! MAP:MAP! CITY:CITY! CHANNEL:CH! | CITY:CITY! ( MAP:MAP! | ) | ) ( INFO:INFO! | GPS1! GPS2! ) ) " +
          "| ID UNIT! PRI:PRI! CALL! PLACE:PLACE! APT:APT? ADDR:ADDR! X-STREETS:X? MAP:MAP? CITY:CITY% GPS1 GPS2 )");
  }

  @Override
  public String getFilter() {
    return "tritechcad@mchd-tx.org,ALARMSQLServer";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_REMOVE_EXT | MAP_FLG_PREFER_GPS;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 175; }
      @Override public int splitBreakPad() { return 1; }
    };
  }


  private static final Pattern RUN_REPORT_PTN1 = Pattern.compile("ID#:(\\d{2}-\\d{6}) *; *Unit:([^ ]+) *;[ ;]*(AC - Assignment Complete *; *.*|Disp:.*)");
  private static final Pattern RUN_REPORT_PTN2 = Pattern.compile("ID[#:](\\d{4}-\\d{6}) *[-;]([A-Z][ A-Za-z]+:\\d\\d:\\d\\d:\\d\\d\\b.*)");
  private static final Pattern RUN_REPORT_PTN3 = Pattern.compile("(\\d{4}-\\d{6}) (Time at Destination:\\d\\d:\\d\\d:\\d\\d)(.*?)-Destination Address:(.*) -");
  private static final Pattern RUN_REPORT_PTN4 = Pattern.compile("ID: *(\\S+) +; *(\\S*) +(Call Complete:(?: \\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d)?) *(.*)");
  private static final Pattern RUN_REPORT_PTN5 = Pattern.compile("ID:(\\S+) +; *(Time Cancelled:.*)");
  private static final Pattern NOTIFICATION_PTN1 = Pattern.compile("(\\d\\d-\\d{6}) - \\d+\\) (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) [\\d:]+\\.000-\\[\\d+\\] \\[Notification\\] +(.*?)(?: +\\[Shared\\])?");
  private static final Pattern NOTIFICATION_PTN2 = Pattern.compile("ID#:(\\d\\d-\\d{6}) +; +\\d+\\) (.*)");

  private static final Pattern MISSING_SEMI_PTN = Pattern.compile("(?<!;) ++(?=\\d{8}\\b|NOTES:)|(?<![ 0-9])(?=\\d{8} +\\d{8}\\b)");
  private static final Pattern COMMA_ID_PTN = Pattern.compile(", *ID:|,(?=\\d\\d-\\d{6}\\b)");
  private static final Pattern DELIM = Pattern.compile("[ ,]*; *");

  @Override
  protected boolean parseMsg(String body, Data data) {

    body = stripFieldStart(body, ",");

    Matcher match = RUN_REPORT_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      for (String time : match.group(3).split(";")) {
        data.strSupp = append(data.strSupp, "\n", time.trim());
      }
      return true;
    }

    match = RUN_REPORT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      for (String time : match.group(2).split("[-;]")) {
        data.strSupp = append(data.strSupp, "\n", time.trim());
      }
      return true;
    }

    match = RUN_REPORT_PTN3.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO PLACE ADDR");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).trim();
      data.strPlace = match.group(3).trim();
      data.strAddress = match.group(4).trim();
      return true;
    }

    match = RUN_REPORT_PTN4.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strSupp = append(match.group(3), " ", match.group(4));
      return true;
    }

    match = RUN_REPORT_PTN5.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2);
      return true;
    }

    match = NOTIFICATION_PTN1.matcher(body);
    if (match.matches()) {
      setFieldList("ID DATE TIME INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCallId = match.group(1);
      data.strDate = match.group(2);
      data.strTime = match.group(3);
      data.strSupp = match.group(4);
      return true;
    }

    match = NOTIFICATION_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2).trim();
      return true;
    }

    // See if we can use the regular semicolon delimited form
    String body2 = MISSING_SEMI_PTN.matcher(body).replaceAll(";");
    boolean comment = body2.startsWith("Comment:");
    if (comment) body2 = COMMA_ID_PTN.matcher(body2).replaceFirst(";ID:");
    String[] flds = DELIM.split(body2);
    if (comment || flds.length >= 4) {
      return parseFields(flds, data);
    }

    // Otherwise, we no longer support otherwise
    return false;
  }

  public void fixOutOfCountyResponse(Data data) {
    if (data.strCall.startsWith("Out of County Respon")) {
      data.defCity = "";
      if (data.strCall.length() > 20) {
        data.strCity = data.strCall.substring(20);
        data.strCall = "Out of County Respon";
      }
    }
  }


  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField(false);
    if (name.equals("ID2")) return new MyIdField(true);
    if (name.equals("UNIT2")) return new MyUnit2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("GPS1")) return new MyGpsField(1);
    if (name.equals("GPS2")) return new MyGpsField(2);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ID_PTN = Pattern.compile("(\\d{2}-\\d{6}|\\d{4}-\\d{6})|(POST-\\d{7}/\\d{2})(\\d\\d:\\d\\d:\\d\\d)");
  private class MyIdField extends IdField {

    boolean permissive;

    public MyIdField(boolean permissive) {
      this.permissive = permissive;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      Matcher match = ID_PTN.matcher(field);
      if (!match.matches()) {
        if (permissive) return;
        abort();
      }
      String id = match.group(1);
      if (id != null) {
        data.strCallId = id;
      } else {
        data.strCallId = match.group(2);
        data.strTime = match.group(3);
      }
    }

    @Override
    public String getFieldNames() {
      return "ID TIME";
    }
  }

  private class MyUnit2Field extends UnitField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt < 0) pt = 0;
      field = field.substring(0, pt).trim();
      super.parse(field, data);
    }
  }


  private static final Pattern CALL_PTN = Pattern.compile("([#\\*][A-Z]*\\d+[A-Z]?|\\d{1,3}[A-Z]\\d{0,2}[A-Z]?) *-[- ]*(.*)");
  private static final Pattern MBLANK_PTN = Pattern.compile(" {2,}");
  private class MyCallField extends CallField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }

      else {
        field = MBLANK_PTN.matcher(field).replaceAll(" ");
        field = field.replace("( ", "(").replace(" )", ")");
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("Not Found", "");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern CITY_TGPS_PTN = Pattern.compile("(.*?) +\\d+");
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_TGPS_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  private class MyGpsField extends GPSField {

    public MyGpsField(int type) {
      super(type, "\\d{8}|0", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.length() == 8) {
        field = field.substring(0,2) + '.' + field.substring(2);
      }
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = MBLANK_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private static final String[] CITY_LIST = new String[]{

    // Cities
    "CONROE",
    "CUT AND SHOOT",
    "HOUSTON",
    "MAGNOLIA",
    "MONTGOMERY",
    "OAK RIDGE NORTH",
    "PANORAMA VILLAGE",
    "PATTON VILLAGE",
    "ROMAN FOREST",
    "SHENANDOAH",
    "SPLENDORA",
    "STAGECOACH",
    "WOODBRANCH",
    "WOODLOCH",
    "WILLIS",

    // Census designated places
    "PINEHURST",
    "PORTER HEIGHTS",
    "THE WOODLANDS",

    // Unincorporated areas
    "DOBBIN",
    "DECKER PRAIRIE",
    "IMPERIAL OAKS",
    "NEW CANEY",
    "PORTER",
    "RIVER PLANTATION",
    "TAMINA",
    "THE WOODLANDS",

    // Harris County
    "SPRING"
  };
}
