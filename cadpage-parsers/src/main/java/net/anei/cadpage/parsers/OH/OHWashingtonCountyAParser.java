package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class OHWashingtonCountyAParser extends FieldProgramParser {

  /**
   * Washington County, OH
   */

  public OHWashingtonCountyAParser () {
    super(OH_CITY_LIST, "WASHINGTON COUNTY", "OH",
        "( CALL ADDRCITY/Z DATETIME/Z ID! SKIP END " +
        "| ( ID? ( ADDRCITYST/ZS DATETIME! CALL CALL2? " +
                "| CALL ADDRCITY/S DATETIME CALL/SDS " +
                ") " +
          ") INFO/N+? ( NAME/Z UNIT | UNIT | EMPTY EMPTY? ) X X2% END " +
        ")");
    setupCities(WV_CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "notifications@washingtoncountysheriff.or,notifications@wcso84.us,local442@mariettaoh.net";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean splitKeepLeadBreak() { return true; }
      @Override public int splitBreakLength() { return 180; }
      @Override public int splitBreakPad() { return 2; }
      @Override public boolean insConditionalBreak() { return true; }
    };
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD +Page(?: +(\\d\\d-\\d{6}))?");
  private static final Pattern START_PTN = Pattern.compile("(?<!\n\\d\\d?/\\d\\d?/\\d{4}, \\d\\d:\\d\\d:\\d\\d)\n\\d\\d-\\d{6}\n");
  private static final Pattern TRAIL_ID_PTN = Pattern.compile(" CAD +Page(?: \\d\\d-\\d{6})?$");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = getOptGroup(match.group(1));

    match = START_PTN.matcher(body);
    if (match.find()) body = body.substring(match.start()+1);

    match = TRAIL_ID_PTN.matcher(body);
    if (match.find()) body = body.substring(0,match.start()).trim();

    if (!parseFields(body.split("\n"), data)) return false;
    if (! data.strCity.isEmpty() && WV_CITY_SET.contains(data.strCity.toUpperCase())) data.strState = "WV";
    return true;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram().replace("CITY", "CITY ST");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{6}", true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("UNIT")) return new UnitField("(?:(?:[A-Z]{4}|\\d{2,4}|[A-Z]+\\d+|DEVDRO|MFD)\\b ?)+", true);
    if (name.equals("X2")) return new MyCross2Field();
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*), *(?:APT|RM|ROOM|LOT) *(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" WV")) {
        data.strState = "WV";
        field = field.substring(0, field.length()-3).trim();
      }
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      field = field.replace('@', '&');
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4})[, ]*(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("-")) return false;
      data.strCall = append(data.strCall, " ", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyCross2Field extends CrossField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        String city = field.substring(pt+1).trim();
        field = field.substring(0,pt);
        if (data.strCity.isEmpty()) data.strCity = city;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "X CITY";
    }
  }

  private static final String[] OH_CITY_LIST = new String[] {
      "ADAMS",
      "ATHENS COUNTY",
      "DUNHAM",
      "MONROE COUNTY"
  };

  private static final String[] WV_CITY_LIST = new String[] {
      "PLEASANTS COUNTY",

      "BELMONT",
      "DAVISVILLE",
      "PARKERSBURG",
      "ST MARYS",
      "SAINT MARYS",
      "VIENNA"
  };

  private static final Set<String> WV_CITY_SET = new HashSet<>(Arrays.asList(WV_CITY_LIST));
}