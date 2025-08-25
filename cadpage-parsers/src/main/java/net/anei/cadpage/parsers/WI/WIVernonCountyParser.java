package net.anei.cadpage.parsers.WI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WIVernonCountyParser extends FieldProgramParser {

  public WIVernonCountyParser() {
    super("VERNON COUNTY", "WI",
          "UNIT UNIT/C? CH/C+? CALL ADDRCITYST PLACE MISC SKIP ID! DATETIME PHONE! INFO/N!");
  }

  @Override
  public String getFilter() {
    return "centralsquare@vernoncountywi.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split(";", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d|\\d{1,2}|REMS|RISO", true);
    if (name.equals("CH")) return new ChannelField("V[FQ][A-Z0-9]+|\\d{3}", true);
    if (name.equals("MISC")) return new MiscField();
    if (name.equals("ID")) return new IdField("CFS\\d\\d-\\d+", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new InfoField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *(.*)", false);
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|LOT) +(.*)");
  private class MiscField extends Field {
    @Override
    public void parse(String field, Data data) {

      if (field.equals("None")) return;

      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = match.group(1);
        return;
      }

      String tmp = setGPSLoc(field, data);
      if (tmp.equals(field)) return;

      data.strApt = field;
    }

    @Override
    public String getFieldNames() {
      return "APT GPS";
    }
  }
}
