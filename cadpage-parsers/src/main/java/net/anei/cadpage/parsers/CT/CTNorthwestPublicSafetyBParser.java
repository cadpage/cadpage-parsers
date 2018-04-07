package net.anei.cadpage.parsers.CT;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Northwest Public Safety, CT
 */
public class CTNorthwestPublicSafetyBParser extends FieldProgramParser {

  public CTNorthwestPublicSafetyBParser() {
    super("", "CT", 
          "ADDR UNIT CITY PLACE CALL UNIT DATETIME/d! ID GPS1 GPS2 INFO/N+");
  }
  
  @Override
  public String getLocName() {
    return "Northwest Public Safety, CT";
  }

  @Override
  public String getFilter() {
    return "SMTP@nowestps.org";
  }
  
  @Override
  public boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\\|"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new AptField("Unit# *(.*)|", true);
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d-\\d\\d-\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
  
  private String saveAddressField;
  
  private Pattern ADDR_LEAD_ZERO_PTN = Pattern.compile("^0+(?=\\d)");
  private Pattern ADDR_MBLANK_PTN = Pattern.compile(" {2,}");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = ADDR_MBLANK_PTN.matcher(field).replaceAll(" ");
      saveAddressField = field;
      field = ADDR_LEAD_ZERO_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }
  
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      field = ADDR_MBLANK_PTN.matcher(field).replaceAll(" ");
      if (field.equals(saveAddressField)) return;
      field = stripFieldStart(field, data.strAddress);
      field = stripFieldStart(field, "-");
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
