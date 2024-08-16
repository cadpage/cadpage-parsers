package net.anei.cadpage.parsers.WI;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WILaCrosseCountyParser extends FieldProgramParser {

  public WILaCrosseCountyParser() {
    super("LA CROSSE COUNTY", "WI",
          "ADDR! Apt/Suite:APT! Flr/Bldg:APT! CITY! Lower_Cross_St:X! Upper_Cross_St:X! INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }


  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Onalaska Fire")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private Pattern CODE_PTN = Pattern.compile("\\d*[A-Z][A-Z0-9]*");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("NBH:")) {
        data.strPlace = append(data.strPlace, " - ", field.substring(4).trim());
        return;
      }
      if (field.startsWith("Landmark Comment:")) {
        field = field.substring(17).trim();
        int pt = field.indexOf("NBH:");
        if (pt >= 0) {
          data.strPlace = append(data.strPlace, " - ", field.substring(pt+4).trim());
          field = field.substring(0,pt).trim();
        }
        data.strSupp = append(data.strSupp, "\n", field);
        return;
      }
      if (data.strCode.isEmpty() && CODE_PTN.matcher(field).matches()) {
        data.strCode = field;
        return;
      }
      if (field.startsWith("Line11:")) {
        field = field.substring(7).trim();
        int pt = field.indexOf("ZIP Code:");
        if (pt >= 0) field = field.substring(0,pt).trim();
        data.strCall = append(data.strCall, " - ", field);
        return;
      }
      if (field.startsWith("Line21:")) return;

      if (field.startsWith("Line22:")) {
        field = field.substring(7).trim();
        data.strCallId = append(data.strCallId,  ",",  field);
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE CODE CALL ID INFO";
    }
  }
}
