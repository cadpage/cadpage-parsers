package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DispatchA38Parser extends FieldProgramParser {

  public DispatchA38Parser(String defCity, String defState) {
    super(defCity, defState,
          "CFS#:ID! CallType:CALL! Address:ADDR+ Units:UNIT/C+ Details:INFO/CS+ Phone:PHONE Reporting_Party:NAME Latitude/Longitude:GPS END");
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    String[] flds = body.split("\n");
    if (flds.length >= 3) return parseFields(flds, data);
    return super.parseMsg(body, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{4}-\\d{5}|\\d{8}|\\d{10}|\\d{2}-\\d+|\\d{2}[A-Z]{3}\\d{6}|[A-Z]{3,4}\\d{6}|\\d{6}", true);
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("GPS")) return new GPSField("https?:.*\\?q=(.*)", true);
    return super.getField(name);
  }

  private static final Pattern CITY_ZIP_PTN = Pattern.compile("([A-Z]{2}|M)(?: *\\d{3,5})?|");
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field.replace(" apt:", " Apt:"));
      String city = p.getLastOptional(',');
      Matcher match = CITY_ZIP_PTN.matcher(city);
      if (match.matches()) {
        if (city.length() > 0) {
          data.strState = match.group(1);
          if (data.strState.equals("M")) data.strState = "MN";
        }
        city = p.getLastOptional(',');
      }
      String apt = p.getLastOptional("Apt:");
      String addr = p.get();
      if (city.startsWith("-")) {
        addr = addr + ',' + city;
        city = "";
      }
      addr = addr.replace('@', '&');
      data.strCity = city;
      parseAddress(addr, data);
      if (!apt.equals(data.strApt)  && !apt.equals(addr)) {
        data.strApt = append(data.strApt, "-", apt);
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[^\\s,]+");

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Unit:");
      Matcher match = UNIT_PTN.matcher(field);
      if (match.lookingAt()) field = match.group();
      super.parse(field, data);
    }
  }
}
