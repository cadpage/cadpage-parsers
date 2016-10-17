package net.anei.cadpage.parsers.NC;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Rocky Mount, NC
 */
public class NCRockyMountParser extends FieldProgramParser {
  
  public NCRockyMountParser() {
    super("ROCKY MOUNT", "NC",
          "ADDR CALL X! INFO+");
  }

  @Override
  public String getFilter() {
    return "send.bot@rockymountnc.gov";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Rocky Mount 911 Incident")) return false;
    return parseFields(body.split(", "), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (!field.contains("/")) abort();
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
