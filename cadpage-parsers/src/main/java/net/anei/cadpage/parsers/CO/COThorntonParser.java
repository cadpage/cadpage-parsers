package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class COThorntonParser extends FieldProgramParser {

  public COThorntonParser() {
    super(CITY_CODES, "THORNTON", "CO",
          "Location:ADDR/S? EID:ID? TYPE_CODE:CALL! SUB_TYPE:CALL/SDS! TIME:TIME! Comments:INFO Disp:UNIT");
  }

  @Override
  public String getFilter() {
    return "dispatch@cityofthornton.net,dispatch@thorntonco.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("Thornton Page Notification")) break;

      if (body.startsWith("Thornton Page Notification ")) {
        body = body.substring(27).trim();
        break;
      }
      return false;
    } while (false);

    body = body.replace("TIME:", " TIME:");
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID:")) return new IdField("\\d+");
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d");
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PLACE_APT_PTN = Pattern.compile("(.*)(,|: *@?)(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      while (true) {
        Matcher match = ADDR_PLACE_APT_PTN.matcher(field);
        if (!match.matches()) break;
        field = match.group(1).trim();
        String tmp = match.group(3).trim();
        if (match.group(2).equals(",")) {
          apt = append(tmp, " - ", apt);
        } else {
          data.strPlace = append(tmp, " - ", data.strPlace);
        }
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern INFO_GPS_PTN = Pattern.compile("(-\\d{2,3}.\\d{6} \\+\\d{2,3}.\\d{6}) *(.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_GPS_PTN.matcher(field);
      if (match.matches()) {
        setGPSLoc(match.group(1), data);
        field = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS " + super.getFieldNames();
    }
  }

  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "ADAM",   "ADAMS COUNTY",
      "BPD",    "BRIGHTON",
      "FHPD",   "FEDERAL HEIGHTS",
      "NPD",    "NORTHGLENN",
      "TPD",    "THORNTON"
  });
}
