package net.anei.cadpage.parsers.SD;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SDMinnehahaCountyCParser extends FieldProgramParser {

  public SDMinnehahaCountyCParser() {
    super("MINNEHAHA COUNTY", "SD",
          "ID! PD_Pri:PRI! CALL ADDR APT CITY PLACE GPS1 GPS2 X DATETIME UNIT! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "nexgenalerts@oldsaybrookpolice.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d+-\\d+", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = MSPACE_PTN.matcher(field).replaceAll(" ");
      super.parse(field, data);
    }
  }

  private static final Pattern LEAD_ZEROS_PTN = Pattern.compile("^0+");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (clean(field).equals(clean(getRelativeField(-3)))) return;
      super.parse(field, data);
    }

    private String clean(String field) {
      field = field.replace(" ", "");
      field = LEAD_ZEROS_PTN.matcher(field).replaceFirst("");
      return field;
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {

      // when the address is an intersection, this field simply duplicates it
      // and should be ignored
      if (data.strAddress.contains("&")) return;
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(" ", "");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
