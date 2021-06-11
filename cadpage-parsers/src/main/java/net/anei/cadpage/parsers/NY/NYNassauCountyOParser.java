package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYNassauCountyOParser extends FieldProgramParser {

  public NYNassauCountyOParser() {
    super("NASSAU COUNTY", "NY",
          "CENTER:SKIP! FDID:SRC DATESQL:DATE! TIMESQL:TIME! COUNTYNUM:ID! LANDMARK:PLACE! CALLTYPE:CALL! SUBTYPE:CALL/SDS! ADDINFO:CALL/SDS! HYDRANT:LINFO! " +
              "ADDRESS:ADDR! PSTRNUM:SKIP! PSTREET:SKIP! TOWN:CITY! CROSSSQL:SKIP! CROSS1:X! CROSS2:X! HAZMAT:LINFO! MISC:MAP! OPERATOR:SKIP! POSITION:SKIP! " +
              "MODIFIED:SKIP! TRANSCOUNT:SKIP! TRANS1:SKIP! UNIT1:UNIT! UNIT2:UNIT! UNIT3:UNIT! UNIT4:UNIT! ID:ID/L! CAD:ID/L! ROWID:SKIP! END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new MyDateField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern DATE_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)T.*");
  private class MyDateField extends DateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      data.strUnit = append(data.strUnit, ",", field);
    }
  }

}
