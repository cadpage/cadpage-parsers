package net.anei.cadpage.parsers.VA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAHighlandCountyParser extends FieldProgramParser {

  public VAHighlandCountyParser() {
    super("HIGHLAND COUNTY", "VA",
          "Nature:CALL! Address:ADDR! City:CITY! Cross_Streets:X! Reported:TIMEDATE! ID! Priority:PRI! Type:SKIP! Zone:MAP! Responding_Units:UNIT! END");
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
    return super.getField(name);
  }
}
