package net.anei.cadpage.parsers.FL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class FLColumbiaCountyParser extends FieldProgramParser {

  public FLColumbiaCountyParser() {
    super("COLUMBIA COUNTY", "FL",
          "HDR! Complaint:CALL! Address:ADDR! Address:ADDR2! Place:PLACE! Additional_Info:INFO! INFO/N+ Cross_Street:X! Incident_number:ID! Responding_Unit:UNIT! INFO/RN+");
  }

  @Override
  public String getFilter() {
    return "smartcopadmin@columbiasheriff.org";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("SQL Server Message")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("HDR")) return new SkipField("EMS AUTO NOTIFICATION FROM .*", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("PLACE")) return new MyPlaceField();
    return super.getField(name);
  }

  private String address;

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      address = field;
      super.parse(field, data);
    }
  }

  private Pattern APT_PTN = Pattern.compile("(.*?) *\\b(?:APARTMENT|APT|ROOM|RM|LOT) *(.*)");
  private class MyAddress2Field extends Field {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "]");
      int pt = field.indexOf("X2[");
      if (pt >= 0) {
        data.strCross = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strApt = match.group(2);
      }
      if (!field.equals(address)) data.strPlace = field;
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT X";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty() || field.equals(data.strPlace)|| field.equals(address)) return;
      super.parse(field, data);
    }
  }
}
