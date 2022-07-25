package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA89Parser extends FieldProgramParser {

  public DispatchA89Parser(String defCity, String defState) {
    super(defCity, defState,
          ":SKIP! CFS:ID? ( EVT:CALL! ( CMT:INFO! | COM:INFO! ) | EVENT:CALL! COMMENT:INFO! ) LOC:ADDRCITY! CITY? ( SELECT_NO_GPS ( ESN:UNIT! GPS? | ) | ) SRC_ID+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD DISPATCH") && !subject.equals("CAD INCIDENT")) return false;
    return parseFields(body.split("\n\\s*"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("SELECT_NO_GPS")) return new MySelectNoGPSField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("GPS")) return new GPSField("[-+]?\\d{2,3}\\.\\d{6,}, *[-+]?\\d{2,3}\\.\\d{6,}", true);
    if (name.equals("SRC_ID")) return new MySourceIdField();
    return super.getField(name);
  }

  private static final Pattern ADDR_UNIT_GPS_PTN = Pattern.compile("(.*) \\[(\\d+)\\] \\((.*)\\)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_UNIT_GPS_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strUnit = match.group(2);
        setGPSLoc(match.group(3), data);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " UNIT GPS";
    }
  }

  private class MySelectNoGPSField extends SelectField {
    @Override
    public boolean checkParse(String field, Data data) {
      return data.strUnit.isEmpty() && data.strGPSLoc.isEmpty();
    }
  }

  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("ESN:")) return false;
      if (!data.strCity.isEmpty()) return false;
      super.parse(field, data);
      return true;
    }
  }

  private static final Pattern SRC_ID_PTN = Pattern.compile("([^:]+): *(\\S*)");
  private class MySourceIdField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = SRC_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      String src = match.group(1).replace(' ', '_');
      String id = match.group(2);
      data.strSource = append(data.strSource, ",", src);
      if (!id.equals("NR")) data.strCallId = append(data.strCallId, ",", id);
    }

    @Override
    public String getFieldNames() {
      return "SRC ID";
    }
  }
}
