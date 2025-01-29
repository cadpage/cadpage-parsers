package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA95Parser extends FieldProgramParser {

  public DispatchA95Parser(String defCity, String defState) {
    super(defCity, defState,
          "CFS_Number:ID! Incident_Type:SKIP! Caller:NAME? Dispatcher:SKIP? Call_Time:DATETIME! Call_Location:ADDRCITYST! ( Location_Details:INFO! Address:SKIP! Address_Name:PLACE! | ) ( Responding_Units:UNIT! | Responding_Agencies:UNIT! ) Details:INFO! ( END | Message:INFO/N! CFS_Latitude:GPS1! CFS_Longitude:GPS2! Hazmat_Alert:ALERT END )");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    body = stripFieldStart(body, " Please respond immediately.");
    if (!super.parseMsg(body, data)) return false;
    data.strCity = data.strCity.replace(".", "");
    return true;
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("GPS2")) return new BaseGPS2Field();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_BRK_PTN = Pattern.compile(" *; *");
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = UNIT_BRK_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
    }
  }

  private class BaseGPS2Field extends GPSField {
    public BaseGPS2Field() {
      super(2);
    }

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get("From Number /"), data);
      data.strPhone = p.get();
    }

    @Override
    public String getFieldNames() {
      return "GPS PHONE";
    }
  }
}
