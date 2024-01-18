package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYPutnamCountyBParser extends FieldProgramParser {

  public NYPutnamCountyBParser() {
    super("PUTNAM COUNTY", "NY",
          "DATETIME! Fire:CALL! EMS:CALL/SLS! Location:ADDRCITY! Cross_Streets:X! Common_Name:PLACE! Box:BOX! INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    body = body.replace("=20", "\n").trim().replace("\nCross Streets;", "\nCross Streets:").replace("\nBox;", "\nBox:");
    return parseFields(body.split("\\n+"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ";");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DATE_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d \\w+ - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_DATE_PTN.matcher(field).matches()) return;
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }
}
