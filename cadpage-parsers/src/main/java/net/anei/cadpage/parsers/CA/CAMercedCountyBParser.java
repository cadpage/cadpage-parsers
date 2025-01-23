package net.anei.cadpage.parsers.CA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class CAMercedCountyBParser extends FieldProgramParser {

  public CAMercedCountyBParser() {
    super("MERCED COUNTY", "CA",
          "ID CODE CALL ADDRCITY UNIT INFO/N+? GPS! END");
  }

  @Override
  public String getFilter() {
    return "messaging@iamresponding.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    int pt = subject.indexOf('#');
    if (pt >= 0) subject = subject.substring(0, pt).trim();
    data.strSource = subject;
    return parseFields(body.split(";,?"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("Inc# (\\S+)", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("GPS")) return new GPSField("<a href=.*/\\?q=(.*)", true);
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCity = data.strCity.replace('_', ' ').trim();
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', ',');
      super.parse(field, data);
    }
  }
}
