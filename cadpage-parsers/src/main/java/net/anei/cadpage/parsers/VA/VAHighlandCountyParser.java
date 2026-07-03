package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAHighlandCountyParser extends FieldProgramParser {

  public VAHighlandCountyParser() {
    super("HIGHLAND COUNTY", "VA",
          "Nature:CALL! Address:ADDR! City:CITY! Cross_Streets:X! Reported:TIMEDATE! ID! Priority:PRI! Type:SKIP! Zone:MAP! Responding_Units:UNIT! Comments:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "hcsorapid@highlandcova.org";
  }

  private static final Pattern DELIM = Pattern.compile("\n| +(?=City:|Incident #|Type:|Zone:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Station Alerting")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new TimeDateField("\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("ID")) return new IdField("Incident # *(.*)", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d{4} +.*:");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
}
