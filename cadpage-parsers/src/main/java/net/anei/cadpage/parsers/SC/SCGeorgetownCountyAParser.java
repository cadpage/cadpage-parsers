package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Georgetown County, SC
 */
public class SCGeorgetownCountyAParser extends FieldProgramParser {

  public SCGeorgetownCountyAParser() {
    super("GEORGETOWN COUNTY", "SC",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR! CITY:CITY! LAT:GPS1! LONG:GPS2! AREA:MAP! ID:ID! PRI:PRI! DATE:DATE! TIME:TIME! UNIT:UNIT? COMMENT:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "notify@somahub.io";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String adjustMapAddress(String addr) {
    return addr.replace("BY-PASS",  "BYPASS");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d?:\\d\\d?", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern STATE_PTN = Pattern.compile(", *(?:South Carolina|SC)\\b");
  private class MyPlaceField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = STATE_PTN.matcher(field);
      if (match.find()) {
        field = field.substring(0, match.start()).trim();
        super.parse(field, data);
        data.strState = "SC";
      } else {
        data.strPlace = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY ST";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strAddress.isEmpty()) return;
      if (field.isEmpty()) {
        parseAddress(data.strPlace, data);
        data.strPlace = "";
      } else {
        super.parse(field, data);
      }
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strCity.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Unknown")) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "COMMENT:");
      super.parse(field, data);
    }
  }
}
