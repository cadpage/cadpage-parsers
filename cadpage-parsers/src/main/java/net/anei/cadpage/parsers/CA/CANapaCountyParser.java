package net.anei.cadpage.parsers.CA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Napa County, CA
 */
public class CANapaCountyParser extends FieldProgramParser {

  public CANapaCountyParser() {
    super("NAPA COUNTY", "CA",
          "( SELECT/1 CALL ADDRCITY ID INFO! RA:UNIT! GPS UNIT Cmd:SRC Tac:CH " +
          "| CALL ADDRCITY! EMPTY? Cross-Street:X RA:SRC? ID! Remarks:INFO! INFO/N+? GPS! Cross-Street:X? Resources:UNIT! INFO2/N+ ) END");
  }

  @Override
  public String getFilter() {
    return "Lnucad@fire.ca.gov";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA | MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("CAD Page")) return false;

    // Don't know what this is, but it gets in the way
    int pt = body.lastIndexOf(" CB#:");
    if (pt >= 0) body = body.substring(0,pt).trim();

    // Two formats, older one separated by semicolons, new one by line breaks
    String[] flds1 = body.split(";");
    String[] flds2 =  body.split("\n");
    if (flds1.length > flds2.length) {
      setSelectValue("1");
    } else {
      setSelectValue("2");
      flds1 = flds2;
    }
    return parseFields(flds1, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("GPS")) return new GPSField("X:.* Y:.*");
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('@');
      if (pt >= 0) {
        data.strPlace = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      if (!field.startsWith("=L(") && field.endsWith(")")) {
        pt = field.lastIndexOf('(');
        data.strPlace = append(data.strPlace, " - ", field.substring(pt+1, field.length()-1).trim());
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, "_CITY").replace('_', ' ').trim();
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames() + " CITY";
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      if (!field.startsWith("Inc# ")) abort();
      field = field.substring(5).trim();
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = append(data.strUnit, " ", field);
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("[-A-Za-z]+:|http://maps.google.com.*");
  private class MyInfo2Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
}
