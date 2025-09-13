package net.anei.cadpage.parsers.GA;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class GAPierceCountyBParser extends FieldProgramParser {

  public GAPierceCountyBParser() {
    super("PIERCE COUNTY", "GA",
          "CadCallCaseNo:ID! IncidentLocation:PLACE!  IncidentAddNo:ADDR1 IncidentStreet:ADDR2 IncidentCity:CITY PrimUnitNo:UNIT! DispatchTime:DATETIME! " +
              "IncidentType:CALL! IncidentComment:INFO! IncidentLat:GPS1 IncidentLon:GPS2 StationId:SRC! FirstName:NAME LastName:NAME/S Phone:PHONE",
          FLDPROG_XML);
  }

  @Override
  public String getFilter() {
    return "pierce.ga@ez911map.net,pierce.ga@ryzyliant.com";
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (!super.parseHtmlMsg(subject, body, data)) return false;
    if (data.strAddress.isEmpty() && !data.strPlace.isEmpty()) {
      parseAddress(data.strPlace, data);
      data.strPlace = "";
    }
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      String[] flds = field.split(" *\\| *");
      data.strPlace = flds[0];
      if (flds.length >= 2) data.strApt = flds[1];
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT ADDR?";
    }
  }

  private class MyAddress1Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      data.strAddress = field;
    }
  }

  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }
}
