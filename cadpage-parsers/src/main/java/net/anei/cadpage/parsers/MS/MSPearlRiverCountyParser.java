package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MSPearlRiverCountyParser extends FieldProgramParser {

  public MSPearlRiverCountyParser() {
    super("PEARL RIVER COUNTY", "MS",
          "CALL ADDRCITY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "Dispatch@PearlRiverMSe911.info";
  }

  private static final Pattern PREFIX = Pattern.compile("CAD #(\\d{8}-\\d{5}):\n");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD DISPATCH")) return false;
    Matcher match = PREFIX.matcher(body);
    if (!match.lookingAt()) return false;
    data.strCallId = match.group(1);
    body = body.substring(match.end());
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private static final Pattern ADDRESS_PTN = Pattern.compile("(.*?)(?: *\\[(.*?)\\])?(?: *\\((.*)\\))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDRESS_PTN.matcher(field);
      if (!match.matches()) abort(); //  Can't happen
      super.parse(match.group(1), data);
      data.strBox = getOptGroup(match.group(2));
      String gps = match.group(3);
      if (gps != null) setGPSLoc(gps.trim(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY BOX GPS";
    }
  }
}
