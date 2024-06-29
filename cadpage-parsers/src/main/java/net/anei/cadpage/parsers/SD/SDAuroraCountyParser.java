package net.anei.cadpage.parsers.SD;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SDAuroraCountyParser extends FieldProgramParser {

  public SDAuroraCountyParser() {
    super("AURORA COUNTY", "SD",
          "Assigned_Unit:UNIT! Date/Time:DATETIME! Location:ADDRCITY! Incident:CALL! END");
  }

  @Override
  public String getFilter() {
    return "ithelp@cityofmitchell.org,zuercherled@mitchelldps.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("ALERT")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }


}
