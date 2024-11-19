package net.anei.cadpage.parsers.WA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class WAThurstonCountyParser extends FieldProgramParser {

  private String addressFld, cityFld;

  public WAThurstonCountyParser() {
    super(CITY_CODES, "THURSTON COUNTY", "WA",
           "EMPTY CODE CALL ( GPS1 GPS2! | ADDR CITY? ) X+? SRC ( MAPPAGE XXXX | MAP% ) INFO+ Units:UNIT+");
  }

  @Override
  public String getFilter() {
    return "50911,70359,79516,MBLFD,@medic.one";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    addressFld = cityFld = "";
    body = body.replace(" Unit:", " Units:");
    return parseFields(body.split(","), 5, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SRC")) return new SourceField("FD\\d+|[A-Z]{1,2}[FP]D|TCSO", true);
    if (name.equals("MAPPAGE")) return new SkipField("mappage", true);
    if (name.equals("XXXX")) return new SkipField("XXXX", true);
    if (name.equals("MAP")) return new MapField("[A-Z]{1,2}\\d+", true);
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSField extends GPSField {

    public MyGPSField(int type) {
      super(type);
      setPattern(GPS_PTN, true);
    }

  }

  private class MyAddressField extends AddressField {

    // Save the address field for future checks against the cross street field
    @Override
    public void parse(String field, Data data) {
      addressFld = field;
      super.parse(field, data);
    }

    // Address field might contain a place name.  Though we won't figure that out
    // until we process the cross street field
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private class MyCityField extends CityField {

    // Save the city field for future checks against the cross street field
    @Override
    public boolean checkParse(String field, Data data) {
      if (!super.checkParse(field, data)) return false;
      cityFld = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      cityFld = field;
      super.parse(field, data);
    }
  }

  private static final Pattern X_BTWN_AT_PTN = Pattern.compile("btwn (.*) and (.*)");
  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      // Cross street field contains a number of different things
      // Block numbers starting with angle brackets.  Or duplicating
      // the city field with angle brackets are ignored
      int pt = field.indexOf('<');
      if (pt >= 0) {
        if (pt == 0) return true;
        String tmp = field.substring(0,pt).trim();
        if (tmp.equals(cityFld)) return true;
      }

      // Anything matching the city field is ignored
      if (field.equals(cityFld)) return true;

      // btwn ... and ... turn into high and low cross streets
      Matcher match = X_BTWN_AT_PTN.matcher(field);
      if (match.matches()) {
        super.parse(match.group(1).trim() + " / " + match.group(2).trim(), data);
        return true;
      }

      // at ...
      // can be a single cross street, or the actual address of a place name
      // that was in the address field
      if (field.startsWith("at ")) {
        field = field.substring(3).trim();
        if (field.equals(addressFld)) return true;
        if (data.strPlace.length() == 0 && !isValidAddress(data.strAddress)) {
          data.strPlace = data.strAddress;
          data.strAddress = "";
          parseAddress(field, data);
        } else {
          super.parse(field, data);
        }
        return true;
      }

      // Anything else we do not know how to handle
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, ", ", field);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BU", "BUCODA",
      "CE", "CENTRALIA",
      "LA", "LACEY",
      "OL", "OLYMPIA",
      "RA", "RANIER",
      "RO", "ROCHESTER",
      "TC", "",
      "TE", "TENINO",
      "TU", "TUMWATER",
      "YE", "YELM"
  });
}
