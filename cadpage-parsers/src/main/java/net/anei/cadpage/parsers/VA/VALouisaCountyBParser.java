package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VALouisaCountyBParser extends FieldProgramParser {

  public VALouisaCountyBParser() {
    super("LOUISA COUNTY", "VA",
          "CALL CH? ADDRCITY/Z PLACE! Addtl_Location_Info:INFO! BOX! UNIT! INFO/N+? ID END");
  }

  @Override
  public String getFilter() {
    return "Dispatch@louisa.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Information")) return false;
    body = body.replace("; LON:", " LON:");
    if (!parseFields(body.split(";"), data)) return false;
    data.strCity = stripFieldStart(data.strCity, "Town of ");
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new PlaceField("At *(.*)", true);
    if (name.equals("BOX")) return new BoxField("Box +(.*)|()", true);
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{8}\\b.*", true);
    return super.getField(name);
  }

}
