package net.anei.cadpage.parsers.CO;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COElPasoCountyBParser extends FieldProgramParser {

  public COElPasoCountyBParser() {
    super("EL PASO COUNTY", "CO",
          "ADDRCITY X? UNIT CALL CH? DATETIME! END");
  }

  @Override
  public String getFilter() {
    return "no_reply@coloradosprings.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Page")) return false;
    return super.parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new CrossField(".*/.*|", true);
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
}
