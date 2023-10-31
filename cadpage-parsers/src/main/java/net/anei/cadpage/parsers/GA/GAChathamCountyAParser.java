package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Chatham County, GA
 */

public class GAChathamCountyAParser extends FieldProgramParser {

  public GAChathamCountyAParser() {
    super("CHATHAM COUNTY", "GA",
          "( SELECT/1 CRN:ID! UNIT:UNIT! ADD:ADDR! CITY:CITY! APT/UNIT:APT! XSTREET:X! BUSINESS:PLACE! DET:CODE! PRIORITY:PRI! PROBLEM:CALL! COMMENTS:INFO! END " +
          "| SELECT/2 CRN:ID! ADD:ADDR! INFO/N+ " +
          "| SELECT/3 UNIT_ASSIGNED:UNIT! POST_LOCATION:ADDR! END " +
          "| COMMENT_NOTIFICATION:INFO END " +
          ")");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM1_PTN = Pattern.compile("(?: +|(?<![ /]))(?=(?:UNIT|ADD|CITY|APT/UNIT|XSTREET|BUSINESS|DET|PRIORITY|PROBLEM|COMMENTS):)");

  @Override
  protected boolean parseMsg(String body, Data data) {

    if (body.startsWith("CALL INFO:")) {
      setSelectValue("1");
      return parseFields(DELIM1_PTN.split(body.substring(10).trim()), data);
    }

    if (body.startsWith("TIMES:")) {
      setSelectValue("2");
      data.msgType = MsgType.RUN_REPORT;
      body = body.substring(6).trim().replace(" DISPATCHED:", ";DISPATCHED:");
      return parseFields(body.split(";"), data);
    }

    if (body.startsWith("UNIT POST:")) {
      setSelectValue("3");
      data.strCall = "UNIT ASSIGNED TO";
      return super.parseMsg(body.substring(10).trim(), data);
    }

    if (body.startsWith("EXTR BEGIN: EXTR COMP:")) {
      return parseExtraInfo(body, data);
    }

    if (body.startsWith("COMMENT NOTIFICATION:")) {
      setSelectValue("4");
      return super.parseMsg(body, data);
    }
    return false;
  }

  @Override
  public String getProgram() {
    String result = super.getProgram();
    if (result.startsWith("UNIT ")) result = "CALL " + result;
    return result;
  }


  private static final Pattern EXTRA_BRK_PTN = Pattern.compile(" +(?=\\d\\) )");

  private boolean parseExtraInfo(String body, Data data) {
    setFieldList("INFO");
    String[] parts = EXTRA_BRK_PTN.split(body);
    for (String part : parts[0].split(":")) {
      data.strSupp = append(data.strSupp, "\n", part.trim());
    }
    for (int ndx = 1; ndx < parts.length; ndx++) {
      data.strSupp = append(data.strSupp, "\n", parts[ndx]);
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() >= 2 && field.charAt(1) == 'P') field = field.substring(0,1);
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[, ]*(?=\\[\\d\\])");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
