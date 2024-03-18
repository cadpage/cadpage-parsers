package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COBoulderCountyCParser extends FieldProgramParser {

  public COBoulderCountyCParser() {
    super("BOULDER COUNTY", "CO",
          "EVT#:ID! ECOD:CODE! ETYP:CALL! NAME:NAME! ADR1:ADDR! ADR2:APT! CITY:CITY! STCD:ST? LATI:GPS1! LONG:GPS2! PRIO:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! RADIO:CH END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST")) return new StateField("([A-Z]{2})(?: +\\d{5})?");
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern DATE_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)");
  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

}
