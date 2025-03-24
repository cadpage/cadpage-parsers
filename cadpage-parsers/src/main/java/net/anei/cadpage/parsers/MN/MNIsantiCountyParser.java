package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Isanti County, MN
 */

public class MNIsantiCountyParser extends FieldProgramParser {

  public MNIsantiCountyParser() {
    super("ISANTI COUNTY", "MN",
          "ID DATETIME SRC ADDR CITY ST ZIP PRI CODE CALL GPS1 GPS2 INFO UNIT! END");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Active911:")) return false;
    return parseFields(body.split(";"), data);
  }

  @Override
  public Field getField(String name) {
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
      int pt = field.indexOf(" - ");
      if (pt < 0) abort();
      super.parse(field.substring(0, pt).trim(), data);
      String place = field.substring(pt+3).trim();
      if (!place.equals("null")) data.strPlace = place;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
}
