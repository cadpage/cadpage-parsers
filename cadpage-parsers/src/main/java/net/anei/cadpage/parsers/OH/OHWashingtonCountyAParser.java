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

  private static final Pattern SUBJECT_PATTERN = Pattern.compile("CAD +Page(?: +(\\d\\d-\\d{6}))?");

  public OHWashingtonCountyAParser () {
    super(WV_CITY_LIST, "WASHINGTON COUNTY", "OH",
        "( CALL EMPTY ADDRCITYST/S EMPTY ( EMPTY EMPTY EMPTY | ) DATE TIME EMPTY SRC! | ID? ADDRCITYST/S DATETIME CALL ) UNIT X X END");
  }

  @Override
  public String getFilter() {
    return "notifications@washingtoncountysheriff.or,notifications@wcso84.us";
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

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PATTERN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = getOptGroup(match.group(1));
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
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*), *(?:APT|RM|ROOM|LOT) *(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
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

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) *(\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
    }
  }

  private static final String[] WV_CITY_LIST = new String[] {
      "BELMONT",
      "ST MARYS",
      "SAINT MARYS"
  };

  private static final Set<String> WV_CITY_SET = new HashSet<>(Arrays.asList(WV_CITY_LIST));
}