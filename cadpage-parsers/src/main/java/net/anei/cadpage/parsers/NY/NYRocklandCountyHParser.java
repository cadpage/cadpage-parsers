package net.anei.cadpage.parsers.NY;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYRocklandCountyHParser extends FieldProgramParser {

  public NYRocklandCountyHParser() {
    super("ROCKLAND COUNTY", "NY",
          "SRC ADDRCITY! CROSS_STREET:X NAME:NAME FLOOR:FLR CALL_TYPE:CALL CALLBACK:PHONE CALLER:PHONE! CAD:ID! UNITS:UNIT? INFO/N+? GPS! END");
  }

  @Override
  public String getFilter() {
    return "8336651600";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("\n|(?=(?:CROSS STREET|NAME|CALLER):)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("FLR")) return new MyFloorField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("GPS")) return new GPSField("http://maps\\.google\\.com/maps\\?daddr=(.*)", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*) (\\d{5})");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        zip = match.group(2);
      }
      super.parse(field, data);
      if (zip != null && data.strCity.isEmpty()) data.strCity = zip;
    }
  }

  private class MyFloorField extends AptField {
    @Override
    public void parse(String field, Data data) {
      data.strApt = append(data.strApt, " ", getRelativeField(0));
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty() || field.equals("()-")) return;
      if (field.equals(data.strPhone)) return;
      data.strPhone = append(data.strPhone, " / ", field);
    }
  }
}
