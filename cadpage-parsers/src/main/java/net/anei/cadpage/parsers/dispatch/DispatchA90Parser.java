package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA90Parser extends FieldProgramParser {

  public DispatchA90Parser(String defCity, String defState) {
    super(defCity, defState,
          "INCIDENT:ID! TITLE:CALL! PLACE:PLACE? ADDRESS:ADDR? CITY:CITY? STATE:ST? GPS:GPS? ( BOX:BOX! | Box:BOX! | ) NOTES:INFO/N+");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (!parseFields(body.split("\n"), data)) return false;
    return (!data.strAddress.isEmpty() || !data.strGPSLoc.isEmpty() || !data.strSupp.isEmpty());
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CALL_CLOSE_PTN = Pattern.compile("[\\[\\(] *CLOSED *[\\]\\)]", Pattern.CASE_INSENSITIVE);

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (CALL_CLOSE_PTN.matcher(field).find()) data.msgType = MsgType.RUN_REPORT;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *(?:Incident created|Incident closed|\\S+ changed)$");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
}
