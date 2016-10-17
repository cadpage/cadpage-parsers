package net.anei.cadpage.parsers.VA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VALoudounCountyBParser extends FieldProgramParser {
  
  public VALoudounCountyBParser() {
    super("LOUDOUN COUNTY", "VA",
          "CALL CODE! ADDR! Location:PLACE? CITY Box:BOX! X/Z+? Units:SKIP! UNIT/S+ call_dispatched:TIME!  URL? Inc_Id:ID! SCANNER? INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "lcfr@adveng.tk";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    int pt = body.indexOf("\n-- \n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    return super.parseFields(body.split("\n+"), 5, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("X")) return new CrossField("XST: *(.*)", true);
    if (name.equals("TIME")) return new TimeField("\\[(\\d\\d:\\d\\d)\\]", true);
    if (name.equals("URL")) return new InfoUrlField("<A HREF=\"(.*)\">Interactive Map</A>", true);
    if (name.equals("SCANNER")) return new SkipField("<A HREF=\"(.*)\">Scanner</A>", true);
    return super.getField(name);
  }
  
  private class MyCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "(");
      field = stripFieldEnd(field, ")");
      super.parse(field, data);
    }
  }
}
