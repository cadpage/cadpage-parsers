package net.anei.cadpage.parsers.GA;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class GASpaldingCountyParser extends DispatchOSSIParser {

  public GASpaldingCountyParser() {
    super(CITY_LIST, "SPALDING COUNTY", "GA",
          "FYI? CALL ADDR ( CITY2 | CITY? ( DATETIME! | X/Z+? PLACE DATETIME! ) ) INFO/N+? UNIT");
    setupCities(CITY_CODES);
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    body = "CAD:" + body;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY2")) return new CityField("[A-Z]{4}", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private String saveAddress;

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      saveAddress = field;
      super.parse(field, data);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(saveAddress)) return;
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("(?:(?:\\b[A-Z]+\\d+\\b),?)+");
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!UNIT_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final String[] CITY_LIST = new String[] {

      // Cities
      "GRIFFIN",

      // Town
      "ORCHARD HILL",

      //Census-designated places
      "EAST GRIFFIN",
      "EXPERIMENT",
      "HERON BAY",
      "SUNNY SIDE",

      // Fayette County
      "BROOKS",

      // Henry County
      "HAMPTON",

      // Pike County
      "WILLIAMSON"

  };

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "GRIF", "GRIFFIN"
  });

}
