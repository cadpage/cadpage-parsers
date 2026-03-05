package net.anei.cadpage.parsers.LA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LAWestFelicianaParishParser extends FieldProgramParser {

  public LAWestFelicianaParishParser() {
    super("WEST FELICIANA PARISH", "LA",
          "Event_Date:DATETIME! UNIT_CAD! Address:ADDR! Intersection:X! Jurisdiction:CITY? ( Lat:GPS1! Long:GPS2! | ) Event_Type:CALL! ID! Remarks:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "alerts@wfpalerts.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern FLD_BRK_PTN = Pattern.compile("\n| *\\.? +(?=Unit# |Intersection:|Jurisdiction:|Long:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("System Alert")) return false;
    String[] flds = FLD_BRK_PTN.split(body);
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT_CAD")) return new UnitField("Unit# +(\\S+) +CAD# *.*", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("Report# *(.*)", true);
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "0 ");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "0 ");
      if (field.isEmpty()) return;
      if (checkAddress(data.strAddress) == STATUS_STREET_NAME ) {
        parseAddress(field, data);
      } else {
        data.strCross = field;
      }
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("(")) {
        int pt = field.indexOf(')');
        if (pt >= 0) {
          data.strCode = field.substring(1,pt).trim();
          field = field.substring(pt+1).trim();
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
