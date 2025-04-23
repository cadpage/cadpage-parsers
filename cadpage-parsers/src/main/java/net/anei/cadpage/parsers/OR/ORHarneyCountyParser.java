package net.anei.cadpage.parsers.OR;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class ORHarneyCountyParser extends FieldProgramParser {

  public ORHarneyCountyParser() {
    super("HARNEY COUNTY", "OR",
          "Date:DATE! Time:TIME! From_Officer:SKIP! Message_Type:CALL! Message:EMPTY! GPS? ADDR INFO/N+");
  }

  @Override
  public String getFilter() {
    return "alerts@justicedatasolutions.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Radio Message Export")) return false;
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace("Message:", "Message:\n");
    if (! parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.isEmpty()) data.msgType = MsgType.GEN_ALERT;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("GPS")) return new GPSField("; *(.*?) *;", true);
    return super.getField(name);
  }

}
