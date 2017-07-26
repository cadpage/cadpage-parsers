package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCCherokeeCountyBParser extends FieldProgramParser {
  
  public NCCherokeeCountyBParser() {
    super("CHEROKEE COUNTY", "NC", 
          "CALL:CALL! PLACE:PLACE ADDR:ADDR! CITY:CITY! XY:GPS ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT INFO:X_INFO! END");
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("X_INFO")) return new MyCrossInfoField();
    return super.getField(name);
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
  
  private static Pattern CROSS_INFO_PTN1 = Pattern.compile("([A-Z0-9 ]*@ +(?:(?:I|ST|NC) \\d+|[A-Z0-9]+(?: [A-Z ]+?)? (?:AV|CT|DR|LN|PL|ST|TER))) *(.*)");
  private static Pattern CROSS_INFO_PTN2 = Pattern.compile("([A-Z0-9 ]*@ +(?:(?:I|ST|NC) \\d+|[A-Z0-9]+)) *(.*)");
  
  private class MyCrossInfoField extends Field {

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("*")) {
        field = field.substring(1).trim();
        field = field.replace(" (Verify) ", " ");
      }
      Matcher match = CROSS_INFO_PTN1.matcher(field);
      if (!match.matches()) {
        match = CROSS_INFO_PTN2.matcher(field);
        if (!match.matches()) {
          data.strSupp = field;
          return;
        }
      }
      String cross = match.group(1).replace('@',  '/');
      data.strCross = stripFieldStart(cross, "/");
      data.strSupp = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "X INFO";
    }
  }

}
