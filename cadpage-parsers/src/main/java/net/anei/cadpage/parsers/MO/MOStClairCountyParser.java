package net.anei.cadpage.parsers.MO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOStClairCountyParser extends FieldProgramParser {

  public MOStClairCountyParser() {
    super("ST CLAIR COUNTY", "MO",
          "CAD CALL:CALL! ADDR:ADDRCITYST! X:X? ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT! ( INFO:INFO INFO/N+? URL! | URL! ) END");
  }

  @Override
  public String getFilter() {
    return "DoNotReply@cadmus-cad.app";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CAD")) return new SkipField("!CAD!", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("URL")) return new InfoUrlField("https:.*", true);
    return super.getField(name);
  }
}
