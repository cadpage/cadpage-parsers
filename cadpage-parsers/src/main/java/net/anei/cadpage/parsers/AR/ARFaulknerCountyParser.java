package net.anei.cadpage.parsers.AR;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARFaulknerCountyParser extends FieldProgramParser {

  public ARFaulknerCountyParser() {
    super("FAULKNER COUNTY", "AR",
          "Incident:EMPTY! Nature:CALL! Address:ADDRCITY! Cross_Streets:X? Priority:PRI! Coordinates:GPS? ID:ID! Units:UNIT! Comments:EMPTY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  private static final Pattern SRC_PTN = Pattern.compile("(.*?) +CAD Incident");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    Matcher match = SRC_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strSource = match.group(1);
    body = body.replace("\n\t", "\n").replace(": : ", ": ");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "message:");
      super.parse(field, data);
    }
  }
}
