package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CORockyMountainRescueParser extends FieldProgramParser {

  public CORockyMountainRescueParser() {
    super("", "CO",
          "ID_CALL! ADD:ADDR! BLD:APT! APT:APT! LOC:NAME! INFO:INFO! TIME:TIME! UNITS:UNIT! EMD:CODE! ZIP:ZIP! TAC:CH! END");
  }

  @Override
  public String getLocName() {
    return "Rocky Mountain Rescue";
  }

  @Override
  public String getFilter() {
    return "bretsa@bretsaps.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("INFO:", " INFO:").replace("TIME:", " TIME:").replace("UNITS:", " UNITS:").replace("EMD:", " EMD:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID_CALL")) return new MyIdCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d", true);
    if (name.equals("CODE")) return new MyCodeField();
    return super.getField(name);
  }

  private static final Pattern ID_CALL_PTN = Pattern.compile("([A-Z]{3,4}\\d{6}-\\d{6}) +(?:([A-Z]+)-)?(\\S.*)");
  private class MyIdCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_CALL_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strCode = getOptGroup(match.group(2));
      data.strCall = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "ID CODE CALL";
    }
  }

  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      super.parse(field, data);
    }
  }
}
