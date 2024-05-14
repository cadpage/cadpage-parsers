package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NYSuffolkCountyMParser extends FieldProgramParser {

  public NYSuffolkCountyMParser() {
    super(CITY_CODES, "SUFFOLK COUNTY", "NY",
          "Location:ADDR/S? EV_NUM:ID! TYPE_CODE:CALL! CALLER_NAME:NAME? CALLER_ADDR:CADDR/S? TIME:TIME! END");
  }

  @Override
  public String getFilter() {
    return "scpdcad@suffolkcountyny.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Msg - Do Not Reply")) return false;
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("CADDR")) return new MyCallerAddressField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*), *([^,;]+);[A-Z]+");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(": @");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0, pt).trim();
      }
      String apt = "";
      Matcher match = ADDR_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        apt = match.group(2);
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR CITY APT PLACE";
    }
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class MyCallerAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (!data.strAddress.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "AQUEBO",             "AQUEBOGUE",
      "AQUEBO RIVERHEAD",   "AQUEBOGUE",
      "BAITIH",             "BAITING HOLLOW",
      "BAITIH CALVERTON",   "BAITING HOLLOW",
      "CALVER",             "CALVERTON",
      "CALVER CALVERTON",   "CALVERTON",
      "CALVER MANORVILLE",  "CALVERTON",
      "CALVER RIVERHEAD",   "CALVERTON",
      "JAMESP",             "JAMESPORT",
      "JAMESP JAMESPORT",   "JAMESPORT",
      "NORTHV",             "NORTHVILLE",
      "NORTHV RIVERHEAD",   "NORTHVILLE",
      "RIVERH",             "RIVERHEAD",
      "RIVERH RIVERHEAD",   "RIVERHEAD",
      "RIVERS",             "RIVERSIDE",
      "RIVERS RIVERHEAD",   "RIVERSIDE",
      "WADINR",             "WADING RIVER",
      "WADINR CALVER",      "WADING RIVER"
  });
}
