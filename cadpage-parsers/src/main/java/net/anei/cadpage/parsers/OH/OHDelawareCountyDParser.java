package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHDelawareCountyDParser extends FieldProgramParser {

  public OHDelawareCountyDParser() {
    super("DELAWARE COUNTY", "OH",
          "DATETIME CALL_PRI! Run_Card:LINFO! Talk_Group:CH! SRC! ADDRCITY! X! UNIT! INFO/N+? GPS ID END");
  }

  @Override
  public String getFilter() {
    return "DEL-911@co.delaware.oh.us,nwsysadmin@westerville.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL_PRI")) return new MyCallPriorityField();
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
    return super.getField(name);
  }

  private static final Pattern CALL_PRI_PTN = Pattern.compile("(.*) Alarm: (\\d+)");
  private class MyCallPriorityField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_PRI_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strPriority = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CALL PRI";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("([-+]?\\d{2,3}\\.\\d{6,})\\|([-+]?\\d{2,3}\\.\\d{6,})");
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) return false;
      setGPSLoc(match.group(1)+','+match.group(2), data);
      return true;
    }
  }
}
