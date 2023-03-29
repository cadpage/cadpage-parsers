package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXTarrantCountyEParser extends FieldProgramParser {

  public TXTarrantCountyEParser() {
    super("TARRANT COUNTY", "TX",
          "CALL ADDRCITYST ( SELECT/1 X UNIT INFO ID/L DATETIME! SKIP " +
                          "| SELECT/2 UNIT INFO DATETIME ID! ID/L " +
                          "| INFO ) END");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com";
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:CAD Call|Major Incident -) *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      setSelectValue(subject.startsWith("Major") ? "3" : "1");
      data.strCallId = match.group(1);
    }
    else {
      if (subject.isEmpty()) return false;
      setSelectValue("2");
      body = subject + " | " + body;
    }
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public String getProgram() {
    return "ID? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyIdField extends IdField {
    public MyIdField() {
      super("(?:\\b(?:[A-Z]{2,3}\\d{8})\\b[; ]*)+", true);
    }

    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", "/");
      super.parse(field, data);
    }
  }

  private static final Pattern TX_DD_SR_PTN = Pattern.compile("\\b(TX \\d+) SR\\b");

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return TX_DD_SR_PTN.matcher(sAddress).replaceAll("$1");
  }

}
