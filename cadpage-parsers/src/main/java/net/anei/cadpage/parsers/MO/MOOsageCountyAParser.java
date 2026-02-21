package net.anei.cadpage.parsers.MO;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOOsageCountyAParser extends FieldProgramParser {

  public MOOsageCountyAParser() {
    super(CITY_CODES, "OSAGE COUNTY", "MO",
          "CFS_#:ID! Call_Date:DATETIME! Agency_#'s:SKIP! Incident_Code:CALL! Address:ADDRCITYST! Nearest_intersection:X! Lat/Long:GPS! " +
                "Location_Alerts:ALERT! Units:UNIT! Phone_Number:PHONE! Call_Details:INFO! END");
  }

  @Override
  public String getFilter() {
    return "noreplyosage911@gmail.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR1_PTN = Pattern.compile("(.*?, *[A-Z]{2}(?: \\d{5})?)\\b *(?:0\\b *)?(.*)");
  private static final Pattern ADDR2_PTN = Pattern.compile("(.*?, *[A-Z]+)\\b *(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT) *(.*)|\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "None");
      String apt = "";
      if (field.endsWith(" None")) {
        field = field.substring(0, field.length()-5).trim();
      } else {
        Matcher match = ADDR1_PTN.matcher(field);
        boolean good = match.matches();
        if (!good) {
          match = ADDR2_PTN.matcher(field);
          good = match.matches();
        }
        if (good) {
          field = match.group(1).trim();
          String extra = match.group(2).trim();
          match = APT_PTN.matcher(extra);
          if (match.matches()) {
            apt = match.group(1);
            if (apt == null) apt = extra;
          } else {
            data.strPlace = extra;
          }
        }
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY ST APT PLACE";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "None");
      super.parse(field, data);
    }
  }

  private class MyUnitField  extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "None");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "None");
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = field;
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "FRE",  "FREEBURG",
      "LNN",  "LINN"
  });
}
