package net.anei.cadpage.parsers.OR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class ORYamhillCountyCParser extends FieldProgramParser {

  public ORYamhillCountyCParser() {
    super("YAMHILL COUNTY", "OR",
          "( SELECT/1 CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! ID:ID! Cross_Street:X? MAP:MAP% ZIP_CODE:ZIP? UNIT:UNIT%  NARR:INFO/N INFO/N+" +
          "| CALL ADDR PLACE! Caller:NAME! Caller_#:PHONE! Units:UNIT! ) END");
  }

  @Override
  public String getFilter() {
    return "ycom@ycom911.org";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public int splitBreakLength() { return 400; }
      @Override public int splitBreakPad() { return 5; }
    };
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("\\* ?CALL TIMES ?\\* ?Run #:((?:[A-Z]+-)?[-0-9]*) *(?:ADDR|Add): *?(.*?) +?((?:Call Rec:|Call Received:|Disp:).*)");
  private static final Pattern RR_BRK_PTN = Pattern.compile("(?:[* ]+|(?<!(?:^|[* \n])))(?=(?:Disp|Enr|Onscene|Avail|Unit):)");
  private static final Pattern DELIM = Pattern.compile("\\* | +(?=NARR:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      parseAddress(match.group(2), data);
      data.strSupp = RR_BRK_PTN.matcher(match.group(3)).replaceAll("\n");
      return true;
    }

    if (body.startsWith("*CALL INFORMATION*")) {
      setSelectValue("2");
      body = body.substring(18).trim();
      return parseFields(body.split(";"), data);
    }

    setSelectValue("1");
    body = body.replace("*Cross Street:", "* Cross Street:").replace("*ZIP CODE", " * ZIP CODE");
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("YAMHILL COUN")) field = "YAMHILL COUNTY";
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("(?=\\[\\d{1,2}\\] )");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\[\\d{1,2}\\]");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        line =  line.trim();
        line = stripFieldEnd(line, ",");
        line = stripFieldEnd(line, "[Shared]");
        if (INFO_JUNK_PTN.matcher(line).matches()) continue;
        super.parse(line, data);
      }
    }
  }
}
