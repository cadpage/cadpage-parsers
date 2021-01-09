package net.anei.cadpage.parsers.OH;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Trumble County, OH
 */
public class OHTrumbullCountyParser extends FieldProgramParser {

  public OHTrumbullCountyParser() {
    super("TRUMBULL COUNTY", "OH",
           "ADDR PLACE? CALL! UNIT X! INFO+");
  }

  @Override
  public String getFilter() {
    return "911no@co.trumbull.oh.us";
  }

  private static final Pattern DELIM = Pattern.compile("\\*{2,}");

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("*")) return false;
    body = body.substring(1).trim();
    return parseFields(DELIM.split(body), data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.equals("OH")) city = p.getLastOptional(',');
      data.strCity = city;
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("/")) return false;

      field = field.replace("//", "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
