package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOCapeGirardeauCountyCParser extends FieldProgramParser {

  public MOCapeGirardeauCountyCParser() {
    super("CAPE GIRARDEAU COUNTY", "MO",
          "CFS_Number:ID! External_Agency_Number:SKIP! Address:ADDRCITYST! Common_Name:PLACE! Additional_Location_Information:APT_PLACE! " +
                "Incident_Type:CALL! Notes:INFO! Cross_Streets:X! Fire_Zone:MAP! Latitude:GPS1! Longitude:GPS2! Location_Notes:PLACE! Unit:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "no-replyzuercher@cityofcapegirardeau.org,no-reply@zuercherportal.com";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM|SUITE) *(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) data.strApt = append(data.strApt, "-", match.group(1));
      if (field.length() <= 4) {
        data.strApt = append(data.strApt, "-", field);
      } else {
        data.strPlace = append(data.strPlace, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
