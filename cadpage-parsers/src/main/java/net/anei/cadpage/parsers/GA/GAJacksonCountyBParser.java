package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class GAJacksonCountyBParser extends FieldProgramParser {

  public GAJacksonCountyBParser() {
    super("JACKSON COUNTY", "GA",
          "DateTime:DATETIME Call_Type:CALL! Address:ADDRCITY! Common_Name:PLACE! Narrative:INFO! INFO/N+? ID! ID2+");
  }

  @Override
  public String getFilter() {
    return "cadpage@jacksoncountygov.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom() {
      @Override public boolean splitBreakIns() { return true; }
    };
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    do {
      if (subject.equals("CAD Page")) break;

      if (body.startsWith("CAD Page\n")) {
        body = body.substring(9).trim();
        break;
      }
      return false;
    } while (false);
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
    if (name.equals("ID2")) return new MyId2Field();
    return super.getField(name);
  }

  private class MyId2Field extends IdField {
    @Override
    public void parse(String field, Data data) {
      data.strCallId = data.strCallId + field;
    }
  }
}
