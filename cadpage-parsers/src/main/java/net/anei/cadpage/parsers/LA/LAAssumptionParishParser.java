package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAAssumptionParishParser extends FieldProgramParser {

  public LAAssumptionParishParser() {
    super("ASSUMPTION PARISH", "LA",
          "ADDRCITYST! Call_Details:INFO!");
  }

  @Override
  public String getFilter() {
    return "no-reply@csprosuite.centralsquarecloudgov.com,leds@assumptionsheriff.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(\\d+) - *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);
    data.strCall = match.group(2);

    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "ID CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_ST_EXTRA_PTN = Pattern.compile("(.*, *LA(?: +\\d{5})?+) +(.*)");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_ST_EXTRA_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strSupp = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " INFO?";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
}
