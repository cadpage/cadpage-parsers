package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA97Parser extends FieldProgramParser {

  public DispatchA97Parser(String defCity, String defState) {
    super(defCity, defState,
          "EVT#:ID! ECOD:CODE! ETYP:CALL! NAME:NAME! ADR1:ADDR! ADR2:APT! CITY:CITY! STCD:ST? LATI:GPS1! LONG:GPS2! PRIO:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! RADIO:CH END");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new BaseCodeField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ST")) return new StateField("([A-Z]{2})(?: +\\d{5})?");
    if (name.equals("DATE")) return new BaseDateField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d|", true);
    if (name.equals("UNIT")) return new BaseUnitField();
    return super.getField(name);
  }

  private class BaseCodeField extends CodeField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/DEFAULT");
      super.parse(field, data);
    }
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, " -");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)");
  private class BaseDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

}
