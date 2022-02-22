package net.anei.cadpage.parsers.AR;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ARBentonCountyBParser extends FieldProgramParser {

  public ARBentonCountyBParser() {
    super("BENTON COUNTY", "AR",
          "INC_TYPE:CALL! COMPANIES:UNIT! ADDRESS:ADDRCITY/S6! Map:MAP! END");
  }

  @Override
  public String getFilter() {
    return "Dispatch@rogersar.gov";
  }

  @Override
  public int getMapFlags() {
    return MsgInfo.MAP_FLG_SUPPR_AND_ADJ;
  }

  private static Pattern INFO_BRK_PTN = Pattern.compile("\\s*\\n\\s*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) return false;
    if (!body.startsWith("***")) return false;
    body = body.substring(3).trim();
    if (body.endsWith("\n ***END OF TRANSMISSION***")) {
      body = body.substring(0,body.length()-27).trim();
    } else {
      data.expectMore = true;
    }

    int pt = body.indexOf("\n\n :");
    if (pt < 0) return false;
    String info = body.substring(pt+4).trim();
    body = body.substring(0,pt).trim();
    if (!super.parseMsg(body, data)) return false;
    data.strSupp = INFO_BRK_PTN.matcher(info).replaceAll("\n");
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " INFO";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      super.parse(field, data);
    }
  }

  private class MyAddressCityField extends AddressCityField {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("    ");
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt);
        field = field.substring(pt+4).trim();
      }
      super.parse(field, data);;
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }
}
