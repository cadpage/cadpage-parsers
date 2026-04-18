package net.anei.cadpage.parsers.WA;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class WAIslandCountyParser extends FieldProgramParser {

  public WAIslandCountyParser() {
    super(CITY_CODES, "ISLAND COUNTY", "WA",
          "SRC ( UNIT/Z ID TIMES! TIMES+ | CALL ADDRCITY MAP UNIT! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "Amcom-Messenger@mail.comcast.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Message from Amcom Messenger:")) return false;
    body = body.replace("\n", "");
    return parseFields(body.split(":"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new SourceField("[A-Z0-9]+", true);
    if (name.equals("UNIT")) return new UnitField("[A-Z0-9]+", true);
    if (name.equals("ID")) return new IdField("\\d\\d-\\d+|C\\d{3,}", true);
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      int pt = data.strAddress.indexOf(';');
      if (pt >= 0) {
        data.strPlace = data.strAddress.substring(pt+1).trim();
        data.strAddress = data.strAddress.substring(0,pt).trim();
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.msgType = MsgType.RUN_REPORT;
      field = field.replace('.', ':');
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CLI", "CLINTON",
      "FRE", "FREELAND",
      "LAN", "LANGLEY"
  });
}
