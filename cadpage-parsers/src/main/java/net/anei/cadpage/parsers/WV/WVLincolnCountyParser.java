package net.anei.cadpage.parsers.WV;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class WVLincolnCountyParser extends FieldProgramParser {

  public WVLincolnCountyParser() {
    super("LINCOLN COUNTY", "WV",
          "Unit:UNIT! Incident:ID! Location:ADDR Place:PLACE! Apt/Lot:APT! Run_Zone:MAP! City:CITY! Cross_Street_1:X! Cross_Street_2:X! Complaint:CALL! Map:GPS!");
  }

  @Override
  public String getFilter() {
    return "lincowv911@e911.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatched Unit Email Notification")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("GPS")) return new GPSField("https://maps.google.com/\\?q=(-\\d+\\.\\d+,\\d+\\.\\d+)", false);
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

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(data.strAddress)) return;
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("FEZ")) city = "BRANCHLAND";
    return city;
  }
}
