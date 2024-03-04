package net.anei.cadpage.parsers.GA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class GALibertyCountyParser extends FieldProgramParser {

  public GALibertyCountyParser() {
    super("LIBERTY COUNTY", "GA",
          "DATETIME CALL ADDRCITY X EMPTY UNIT! ID INFO/N+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" / ");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0, pt).trim();
      } else {
        field = stripFieldEnd(field, "/");
      }
      field = field.replace('@', '&');
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = EG_MILES_PTN.matcher(addr).replaceAll("ELMA G MILES");
    return super.adjustMapAddress(addr);
  }
  private static final Pattern EG_MILES_PTN = Pattern.compile("\\bE ?G MILES\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("FAIRHAVEN ACRES TP")) city = "MIDWAY";
    return super.adjustMapCity(city);
  }
}
