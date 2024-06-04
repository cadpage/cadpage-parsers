package net.anei.cadpage.parsers.CT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CTNewLondonCountyEParser extends FieldProgramParser {

  public CTNewLondonCountyEParser() {
    super("NEW LONDON COUNTY", "CT",
          "COMPLAINT_TYPE:CALL! ADDRESS:ADDR! DISPATCH_TIME:TIME! NARRATIVE:INFO!");
  }

  @Override
  public String getFilter() {
    return "CADPaging@waterfordct.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(",(?=\\[\\d\\])");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("[1] [1] ")) field = field.substring(4);
      data.strSupp = INFO_BRK_PTN.matcher(field).replaceAll("\n");
    }
  }
}
