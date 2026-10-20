package net.anei.cadpage.parsers.LA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class LAStJohnTheBaptistParishParser extends FieldProgramParser {

  public LAStJohnTheBaptistParishParser() {
    super("ST JOHN THE BAPTIST PARISH", "LA",
          "MASH! Dispatched:TIME! Onscene:TIME! Cleared:TIME! Cross_Streets:X! Current_Command_Message:CMD_MSG! Call_Details:INFO/N! Message:SKIP! Responder_Units:UNIT!");
  }

  @Override
  public String getFilter() {
    return "no-reply@ledsportal.com";
  }

  private String times;

  private static final String MARK = "This is a notification in reference to a ";
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Notification")) return false;
    if (!body.startsWith(MARK)) return false;
    times = "";
    try {
      if (!super.parseMsg(body.substring(MARK.length()), data)) return false;
      if (data.msgType == MsgType.RUN_REPORT) {
        data.strSupp = append(times, "\n", data.strSupp);
      }
      return true;
    } finally {
      times = null;
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MyMashField();
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("CMD_MSG")) return new MyCmdMessageField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern MASH_PTN = Pattern.compile("([A-Z/ ])*\\bat (.*?)\\. (\\d+) : *(.*)\\.");

  private class MyMashField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = MASH_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = getOptGroup(match.group(1));
      super.parse(match.group(2).trim(), data);
      data.strCallId = match.group(3);
      String place =  match.group(4);
      if (!place.equals("None")) data.strPlace = place;
    }

    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames() + " ID PLACE";
    }
  }

  private static final Pattern DISPATCH_TIME_PTN = Pattern.compile("Dispatched: *(\\d\\d?/\\d\\d?/\\d\\d) _(\\d\\d?:\\d\\d:\\d\\d)");
  private class MyTimeField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      field = getRelativeField(0);
      times = append(times, "\n", field);
      Matcher match = DISPATCH_TIME_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
      }
      else if (field.startsWith("Cleared:")) {
        data.msgType = MsgType.RUN_REPORT;
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }

  private class MyCmdMessageField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(".")) return;
      field = stripFieldEnd(field, " .");
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d?:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ".");
      super.parse(field, data);
    }
  }
}
