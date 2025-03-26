package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHCoshoctonCountyParser extends FieldProgramParser {

  public OHCoshoctonCountyParser() {
    super("COSHOCTON COUNTY", "OH",
          "Cfs#:ID! External_Agency_Numbers:SKIP! Location_Address:ADDRCITYST! Location_Name:PLACE! Cross_Street:X! Location_Notes:INFO3/N? " +
              "cfs_location_details:APT_PLACE! Incident_Code:CALL! Call_time:DATETIME! Command_Message:ALERT? Responding_Units:UNIT! " +
              "External_Number:SKIP? Call_Details:INFO1/N! 911_information:SKIP! map_information:MAP! " +
              "cfs_lat:GPS1 cfs_long:GPS2! proqa_cmds:INFO/N! Command_Message:ALERT! Location_Notes:INFO3/N? END");

  }

  @Override
  public String getFilter() {
    return "no-reply@coshoctoncounty.net";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO1")) return new MyInfo1Field();
    if (name.equals("INFO3")) return new MyInfo3Field();
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

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO1_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfo1Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO1_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern INFO3_BRK_PTN = Pattern.compile("[, ]*\\b(?:Medical:|Premise Information:) *");
  private class MyInfo3Field extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO3_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }
}
