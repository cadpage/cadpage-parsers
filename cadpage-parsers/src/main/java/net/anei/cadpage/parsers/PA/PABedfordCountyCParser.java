package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PABedfordCountyCParser extends FieldProgramParser {

  public PABedfordCountyCParser() {
    super("BEDFORD COUNTY", "PA",
          "DATETIME CALL ADDRCITY PLACE X! APT_CH UNIT INFO/N+");
  }

  @Override
  public String getFilter() {
    return "alerts@centre.ealert911.com,dispatch@mg.sac911.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals(".") && !subject.equals("| !!")) return false;
    if (!parseFields(body.split("\n"), data)) return false;
    data.strCity = stripFieldEnd(data.strCity, " BORO");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("APT_CH")) return new MyAptChannelField();
    if (name.equals("UNIT")) return new UnitField("[-A-Z0-9,]*", true);
    return super.getField(name);
  }

  private static final Pattern CH_PTN = Pattern.compile("OPS \\d");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) +(.*)");

  private class MyAptChannelField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = CH_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = field;
        return;
      }
      match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
        return;
      }
      abort();
    }

    @Override
    public String getFieldNames() {
      return "APT CH";
    }
  }
}
