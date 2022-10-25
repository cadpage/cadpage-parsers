package net.anei.cadpage.parsers.IL;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ILCookCountyBParser extends FieldProgramParser {

  public ILCookCountyBParser() {
    super("COOK COUNTY", "IL",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:EMPTY! CITY! ID:ID! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@skokie.org";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith("- Dispatch")) {
      data.strSource = subject.substring(0,subject.length()-10).trim();
    }
    else if (!subject.equals("Dispatch"))  return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{4}", true);
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("]")) {
        int pt = field.lastIndexOf("[");
        if (pt >= 0) field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
    }
  }
}
