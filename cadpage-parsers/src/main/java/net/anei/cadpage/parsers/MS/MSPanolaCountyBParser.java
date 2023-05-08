package net.anei.cadpage.parsers.MS;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MSPanolaCountyBParser extends FieldProgramParser {

  public MSPanolaCountyBParser() {
    super("PANOLA COUNTY", "MS",
          "DATE:DATE! TIME:TIME! ADDR:ADDR! INC_TYPE:CALL! REMARKS:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cadpage@adsisoftware.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert Recieved")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

}
