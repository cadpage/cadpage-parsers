package net.anei.cadpage.parsers.MD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDCarolineCountyBParser extends FieldProgramParser {

  public MDCarolineCountyBParser() {
    super("CAROLINE COUNTY", "MD",
          "Call_Type:CALL! Call_Date/Time:DATETIME! Call_Location:ADDRCITY! ( Cross_Streets:X! | X_Streets:X! ) Common_Name:PLACE! ( Latitude:GPS1! Longitude:GPS2! | ) Box_Area:BOX! Radio_Channel:CH! ( Map:MAP! https:SKIP! Incident_Number:ID! Units:UNIT! | Units:UNIT! CFS_#:ID! ) END");
  }

  @Override
  public String getFilter() {
    return "carolinemfp@carolinemd.org";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*?) +(\\d{1,4}[A-Z]?|[A-Z])");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.contains(",")) {
        String apt = "";
        Matcher match = ADDR_APT_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1);
          apt = match.group(2);
        }
        super.parse(field, data);
        data.strApt = append(data.strApt, "-", apt);
      } else {
        parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
}
