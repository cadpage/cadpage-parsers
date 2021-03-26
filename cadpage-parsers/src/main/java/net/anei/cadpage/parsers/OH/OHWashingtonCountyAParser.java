package net.anei.cadpage.parsers.OH;

import net.anei.cadpage.parsers.FieldProgramParser;

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
    super("WASHINGTON COUNTY", "OH",
        "( CALL EMPTY ADDR EMPTY ( EMPTY EMPTY EMPTY | ) DATE TIME EMPTY SRC! | ID? ADDR DATETIME CALL ) UNIT X X END");
  }

  @Override
  public String getFilter() {
    return "notifications@washingtoncountysheriff.or,notifications@wcso84.us";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public int splitBreakLength() { return 180; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PATTERN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = getOptGroup(match.group(1));
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{6}", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
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
}