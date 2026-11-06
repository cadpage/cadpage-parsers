package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class VAAlleghanyCountyParser extends FieldProgramParser {

  public VAAlleghanyCountyParser() {
    super("ALLEGANY COUNTY", "VA",
          "UNIT_ID CALL ADDRCITYST! XST1:X! XST2:X! NOTES:INFO! END");
  }

  public String getFilter() {
    return "cadnotifications@idnetworks.com";
  }

  private static final Pattern DELIM = Pattern.compile(" \\| ");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.equals("TIMES")) {
      setFieldList("CALL INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCall = subject;
      data.strSupp = body;
      return true;
    }

    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if(name.equals("UNIT_ID")) return new MyUnitIdField();
    return super.getField(name);
  }

  private static final Pattern UNIT_ID_PTN = Pattern.compile("([A-Z0-9]+): +([A-Z][A-Z0-9]*\\d\\d-\\d{6})");

  private class MyUnitIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = match.group(1);
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "UNIT ID";
    }
  }
}
