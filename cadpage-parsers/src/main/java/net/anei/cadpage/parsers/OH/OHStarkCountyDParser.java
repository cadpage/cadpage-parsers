package net.anei.cadpage.parsers.OH;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHStarkCountyDParser extends FieldProgramParser {

  public OHStarkCountyDParser() {
    super("STARK COUNTY", "OH",
          "CALL ADDR:ADDR! CITY:CITY! TIME:TIME! MAP:MAP! INFO:INFO! COMMENTS:INFO/N! SENT_TO:SKIP END");
  }

  @Override
  public String getFilter() {
    return "uadispatch@seormc.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Work Repair Request")) return false;
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return parseFields(body.split("\n"), data);
  }

  private static DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField(TIME_FMT, true);

    return super.getField(name);
  }

}
