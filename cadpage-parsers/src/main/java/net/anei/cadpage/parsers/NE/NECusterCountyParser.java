package net.anei.cadpage.parsers.NE;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NECusterCountyParser extends FieldProgramParser {

  public NECusterCountyParser() {
    super("CUSTER COUNTY", "NE",
          "( Call_Type:CALL! Remarks:INFO! INFO/N+ Date:DATETIME! Agency:SRC! Caller_Name:NAME! Phone:PHONE! END " +
          "| Date:DATETIME! Caller_Name:NAME! Phone:PHONE! CALL:CALL! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strSource = subject;
    body = body.replace(" Caller Name:", "\nCaller Name:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
