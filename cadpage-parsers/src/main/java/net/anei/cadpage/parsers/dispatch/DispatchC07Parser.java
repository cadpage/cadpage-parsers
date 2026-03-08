package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchC07Parser extends FieldProgramParser {

  public DispatchC07Parser(String defCity, String defState) {
    super(defCity, defState,
          "DATE:DATE! TIME:TIME! CAD#:SKIP? ADDR:ADDR! ADDR ( INC_TYPE:CALL! | CROSS:EMPTY! X+ ) REMARKS:INFO! INFO/N+ REPORT#:SKIP");
  }

  @Override
  public String getFilter() {
    return "cadpage@adsisoftware.com";
  }

  private static final Pattern RR_DELIM = Pattern.compile("\n+| +(?=TIME:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert Recieved")) return false;

    if (body.contains("INC TYPE:")) {
      return parseFields(body.split("\n"), data);
    }
    data.msgType = MsgType.RUN_REPORT;
    return parseFields(RR_DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
