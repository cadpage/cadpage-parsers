package net.anei.cadpage.parsers.AR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARBentonCountyEParser extends FieldProgramParser {

  public ARBentonCountyEParser() {
    super("BENTON COUNTY", "AR",
          "CALL:CALL! ADDR:ADDR! CITY:CITY! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! RESPONSE:PRI? INFO:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "copier@bellavistaar.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active 911")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      field = field.substring(0,1);
      data.strPriority = append(data.strPriority, "-", field);
    }
  }

  private static final Pattern UNIT_TRAIL_COMMA_PTN = Pattern.compile("[, ]+");
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_TRAIL_COMMA_PTN.matcher(field).replaceFirst("");
      if (field.equals("0")) field = "";
      super.parse(field, data);
    }
  }
}
