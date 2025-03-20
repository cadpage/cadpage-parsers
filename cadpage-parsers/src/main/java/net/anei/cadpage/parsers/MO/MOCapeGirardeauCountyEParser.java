package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOCapeGirardeauCountyEParser extends FieldProgramParser {

  public MOCapeGirardeauCountyEParser() {
    super("CAPE GIRARDEAU COUNTY", "MO",
          "CFS_Number:ID! Address:ADDRCITY/S6! Common_Name:PLACE! Additional_Location_Information:INFO1! Incident_Type:CALL! Notes:INFO! Cross_Streets:X! Fire_Zone:MAP! Latitude:GPS1! Longitude:GPS2!");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,no-replyzuercher@cityofcapegirardeau.org";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_ST_ZIP_PTN = Pattern.compile("(.*?), *([A-Z]{2})(?: (\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ST_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      super.parse(field,  data);
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\b\\d\\d/\\d\\d/\\d\\d \\d\\d?:\\d\\d:\\d\\d - +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      for (String line : INFO_BRK_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }

}
