package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCOrangeCountyDParser extends FieldProgramParser {

  public NCOrangeCountyDParser() {
    super("ORANGE COUNTY", "NC",
          "( SELECT/1 CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY_ST! ID:ID! DATE:DATE! TIME:TIME! ( UNIT:UNIT! | UNITS:UNIT! ) " +
                "( RADIO_CHANNEL:CH! | RADIO:CH! ) ( CROSS_STREETS:X! | CROSS:X! ) X+ Subdivision:MAP NEIGHBORHOOD:EMPTY! INFO:INFO! INFO/S+ " +
          "| CALL:CALL! ADDR:ADDR! ID:ID! UNITS:UNIT! NOTES:EMPTY INFO/N+ " +
          ")");
  }

  @Override
  public String getFilter() {
    return "disp@sors.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CAD Dispatch Report")) {
      setSelectValue("1");
    } else if (subject.equals("CAD Dispatch")) {
      setSelectValue("2");
    } else {
      return false;
    }
    body = body.replace(" Subdivision:", "\nSubdivision:");
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CITY_ST")) return new MyCityStateField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("(.*?) +(\\d\\d[A-Z]\\d\\d[A-Z]?)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(2);
        data.strCall = CALL_CODES.getCodeDescription(data.strCode);
        if (data.strCall == null) data.strCall = match.group(1);
      } else {
        data.strCall = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private class MyCityStateField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt =  field.indexOf(',');
      if (pt >= 0) {
        data.strState = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      data.strCity = field;
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\[\\d\\d/\\d\\d/\\d\\d(?:\\d\\d)? +\\d\\d:\\d\\d:\\d\\d (?:[A-Z][A-Z0-9]*|Unit:\\S+)\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static CodeTable CALL_CODES = new StandardCodeTable();
}
