package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COElPasoCountyBParser extends FieldProgramParser {

  public COElPasoCountyBParser() {
    super("EL PASO COUNTY", "CO",
          "CODE_UNIT ADDR PLACE? ( MAP_TIME_ID! | MAP CH? CALL ID TIME! ) END");
  }

  @Override
  public String getFilter() {
    return "CAD@eptpaging.info";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE_UNIT")) return new MyCodeUnitField();
    if (name.equals("MAP_TIME_ID")) return new MyMapTimeIdField();
    if (name.equals("MAP")) return new MapField("Map +(.+)", true);
    if (name.equals("CH")) return new ChannelField("Radio +(.+)", true);
    if (name.equals("ID")) return new IdField("Report +(.+)", true);
    if (name.equals("TIME")) return new TimeField("Time +(\\d\\d:\\d\\d:\\d\\d)", true);
    return super.getField(name);
  }

  private static final Pattern CODE_UNIT_PTN = Pattern.compile("([A-Z0-9 ]+?) +([A-Z0-9,]+)");
  private class MyCodeUnitField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strUnit =  match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "CALL? CODE UNIT";
    }
  }

  private static final Pattern MAP_TIME_ID_PTN = Pattern.compile("(?:Map +(\\S+) +)?(\\d\\d:\\d\\d:\\d\\d)(?: +Call +(\\S+))?");
  private class MyMapTimeIdField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = MAP_TIME_ID_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strMap = getOptGroup(match.group(1));
      data.strTime = match.group(2);
      data.strCallId = getOptGroup(match.group(3));
      data.strCall = data.strCode;
      data.strCode = "";
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "MAP TIME ID";
    }

  }
}
