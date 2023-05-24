package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/*
Livingston Parish, LA
*/

public class LALivingstonParishParser extends FieldProgramParser {

  public LALivingstonParishParser() {
    super("LIVINGSTON PARISH","LA",
          "Event_Date:DATE_TIME_UNIT_ID! Address:ADDR! Intersection:X! Event_Type:CALL! Remarks:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "alerts@tailorbuilt.app,ses@tailorbuilt.com,alerts@pssalerts.info";
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("CAD# (\\d+) +Unit (\\S+) ?, cleared from (.*?) at (.*?)\\. Event Started (\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d)\nUnit Times:\n");
  private static final Pattern RUN_REPORT_BRK = Pattern.compile("\\s*\n\\s*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("System Alert")) return false;

    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.lookingAt()) {
      setFieldList("ID UNIT CALL ADDR APT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      data.strCall = match.group(3).trim();
      parseAddress(match.group(4).trim(), data);
      data.strSupp = RUN_REPORT_BRK.matcher(body.substring(match.end()).trim()).replaceAll("\n");
      return true;
    }

    body = body.replace(" Intersection:", "\nIntersection:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE_TIME_UNIT_ID")) return new MyDateTimeUnitIdField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_UNIT_ID_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d:\\d\\d) +Unit# (\\S+) +CAD# (\\d+)\\.");
  private class MyDateTimeUnitIdField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_UNIT_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = match.group(2);
      data.strUnit = match.group(3);
      data.strCallId = match.group(4);

    }

    @Override
    public String getFieldNames() {
      return "DATE TIME UNIT ID";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Incident Type:")) return;
      super.parse(field, data);
    }
  }
}
