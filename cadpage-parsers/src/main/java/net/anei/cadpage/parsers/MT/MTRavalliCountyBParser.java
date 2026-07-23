package net.anei.cadpage.parsers.MT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MTRavalliCountyBParser extends FieldProgramParser {

  public MTRavalliCountyBParser() {
    super("RAVALLI COUNTY", "MT",
          "CFS:ID! ( NOTES:MASH! " +
                  "| TITLE:CALL! PRI:PRI! PLACE:PLACE! ADDRESS:ADDR! CROSS:X! CITY:CITY! LAT:GPS1! LNG:GPS2! UNIT:UNIT! NOTES:INFO! " +
                  ") END");
  }

  @Override
  public String getFilter() {
    return "no-reply-zuercher-portal@rc.mt.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("PAGE")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("MASH")) return new MyMashField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyMashField extends Field {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCall = p.get(';');
      data.strPlace = p.get(';');
      parseAddress(p.get(';'), data);
      data.strCity = p.get(';');
      data.strSupp = p.get();
    }

    @Override
    public String getFieldNames() {
      return "CALL PLACE ADDR APT CITY INFO";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[ ;]*\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
