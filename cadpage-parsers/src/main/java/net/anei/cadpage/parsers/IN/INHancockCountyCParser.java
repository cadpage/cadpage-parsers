package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INHancockCountyCParser extends FieldProgramParser {

  public INHancockCountyCParser() {
    super("HANCOCK COUNTY", "IN",
          "PLACE:PLACE! ADDR:ADDRCITY! CROSS_STREETS:X! FIRE_TYPE:CALL! UNIT:UNIT! ALARM_LEVEL:PRI! INFO:INFO! FIRE_RD:BOX! " +
              "PRIMARY_INCIDENT:ID! GPS_LAT:GPS1! GPS_LON:GPS2! MARK! NARRATIVE:INFO/N! INFO/N+ Call_Type:SKIP! END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("MARK")) return new SkipField("#{10,}", true);
    return super.getField(name);
  }

  private static final Pattern CITY_PLACE_PTN = Pattern.compile("([A-Z][a-z]+(?:Palestine|Creek)?)\\b *(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      Matcher match = CITY_PLACE_PTN.matcher(data.strCity);
      if (match.matches()) {
        data.strCity = match.group(1);
        data.strPlace = append(data.strPlace, " - ", match.group(2));
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("::");
      String fld1 = field.substring(0,pt).trim();
      String fld2 = field.substring(pt+2).trim();
      if (fld1.equals(fld2)) field = fld1;
      super.parse(field, data);
    }
  }

}
