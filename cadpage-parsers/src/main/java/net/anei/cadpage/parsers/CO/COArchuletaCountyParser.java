package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COArchuletaCountyParser extends FieldProgramParser {

  public COArchuletaCountyParser() {
    super("ARCHULETA COUNTY", "CO",
          "ID DATETIME SRC ADDR CITY ST ZIP PRI CODE CALL GPS1 GPS2! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@archuletacounty.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Active911: ")) return false;
    return parseFields(body.split(";", -1), data);
  }

  @Override
  protected boolean parseFields(String[] flds, Data data) {
    for (int ii = 0; ii < flds.length; ii++) {
      if (flds[ii].trim().equals("null")) flds[ii] = "";
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}-\\d{5}|", true);
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PRI")) return new PriorityField("\\d", true);
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)T(\\d\\d:\\d\\d:\\d\\d)\\.\\d+Z");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String apt = p.getLastOptional('#');
      String place = p.getLastOptional(" - ");
      field = p.get();
      if (!place.equals("null")) data.strPlace = place;
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT";
    }
  }
}
