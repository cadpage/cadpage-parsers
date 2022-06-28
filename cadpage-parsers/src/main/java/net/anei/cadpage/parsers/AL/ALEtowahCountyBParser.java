package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALEtowahCountyBParser extends FieldProgramParser {

  public ALEtowahCountyBParser() {
    super("ETOWAH COUNTY", "AL",
          ":SKIP! CFS:ID! ( EVT:CALL! CMT:INFO! | EVENT:CALL! COMMENT:INFO! ) LOC:ADDRCITY! ( SELECT_NO_GPS ( ESN:UNIT! GPS? | ) | ) SRC/C+");
  }

  @Override
  public String getFilter() {
    return "dispatch@911comm2.info,dispatch@etowah911.info";
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
    if (name.equals("GPS")) return new GPSField("[-+]?\\d{2,3}\\.\\d{6,}, *[-+]?\\d{2,3}\\.\\d{6,}", true);
    if (name.equals("SRC")) return new MySourceField();
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
  
  private class MySourceField extends SourceField {
    public MySourceField() {
      super("([^:]+):(?: +NR)?", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      super.parse(field, data);
    }
  }
}
