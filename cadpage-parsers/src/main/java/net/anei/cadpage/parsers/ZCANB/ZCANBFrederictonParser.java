package net.anei.cadpage.parsers.ZCANB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ZCANBFrederictonParser extends FieldProgramParser {

  public ZCANBFrederictonParser() {
    super("FREDERICTON", "NB",
          "CALL_TIME ADDR_MAP X UNIT_INFO! Sent_by:SKIP! END");
  }

  @Override
  public String getFilter() {
    return "alerts@wrnotify.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Station Dispatch - Dispatch Alert")) return false;
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL_TIME")) return new MyCallTimeField();
    if (name.equals("ADDR_MAP")) return new MyAddressMapField();
    if (name.equals("UNIT_INFO")) return new MyUnitInfoField();
    return super.getField(name);
  }

  private static final Pattern CALL_TIME_PTN = Pattern.compile("(.*) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyCallTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCall = match.group(1).trim();
      data.strTime = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CALL TIME";
    }
  }

  private static final Pattern ADDR_MAP_PTN = Pattern.compile("(.*?) Dt: *(\\S*) Zne: *(\\S*) Gd: *(\\S*)");
  private static final Pattern APT_ADDR_PTN = Pattern.compile("(\\d+[A-Z]?)-(\\d+.*)");
  private class MyAddressMapField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_MAP_PTN.matcher(field);
      if (!match.matches()) abort();
      field = match.group(1).trim();
      data.strMap = append(append(match.group(2), "-", match.group(3)), "-", match.group(4));
      match = APT_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
        field = match.group(2);
      }
      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return "APT ADDR MAP";
    }
  }

  private static final Pattern UNIT_INFO_PTN = Pattern.compile("(.+?) (FF #\\d+)");
  private class MyUnitInfoField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = UNIT_INFO_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strUnit = match.group(1).trim();
      data.strSupp = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "UNIT INFO";
    }
  }
}
