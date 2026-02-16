package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.ReverseCodeSet;

public class DispatchA50Parser extends FieldProgramParser {

  private Properties callCodes;
  private ReverseCodeSet citySet;

  public DispatchA50Parser(Properties cityCodes, String defCity, String defState) {
    this(null, cityCodes, null, defCity, defState);
  }

  public DispatchA50Parser(Properties callCodes, Properties cityCodes, String defCity, String defState) {
    this(callCodes, cityCodes, null, defCity, defState);
  }

  public DispatchA50Parser(Properties cityCodes, String[] cityList, String defCity, String defState) {
    this(null, cityCodes, cityList, defCity, defState);
  }

  public DispatchA50Parser(Properties callCodes, Properties cityCodes, String[] cityList, String defCity, String defState) {
    super(cityCodes, defCity, defState,
          "Location:ADDR/2S? ( X_CORD:GPS1! Y_CORD:GPS2! | ) ( EID:ID! | EVENT__ID:ID! EVENT__NUMBER:ID/L! ) " +
              "TYPE__CODE:CALL! CALLER__NAME:NAME CALLER__ADDR:ADDR/S TIME:TIME Comments:INFO  " +
              "SPECIAL__ADDRESS__COMMENT:INFO/N Disp:UNIT END", FLDPROG_DOUBLE_UNDERSCORE);
    this.callCodes =  callCodes;
    this.citySet = (cityList == null ? null : new ReverseCodeSet(cityList));
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private static final Pattern PLACE_MARKER = Pattern.compile(" *:? *@ *| *: *alias *| *\\*+ *");
  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*?) +(\\d{5})");
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.length() > 0) return;

      String sPlace = "";
      Matcher match = PLACE_MARKER.matcher(field);
      if (match.find()) {
        if (match.start() == 0) {
          field = field.substring(match.end()).trim();
        } else {
          sPlace = field.substring(match.end()).trim();
          field = field.substring(0,match.start()).trim();
        }
      }
      String apt = "";
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      field = stripFieldEnd(field, ": EST");

      String zip = null;
      match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        zip = match.group(2);
      }

      super.parse(field, data);

      if (citySet != null) {
        String city = citySet.getCode(data.strAddress);
        if (city != null) {
          data.strCity = city;
          data.strAddress = stripFieldEnd(data.strAddress, city);
        }
      }

      if (data.strCity.isEmpty() && zip != null) data.strCity = zip;

      data.strAddress = stripFieldEnd(data.strAddress, "-");
      data.strApt = append(data.strApt, "-", apt);

      Result res = parseAddress(StartType.START_OTHER, FLAG_IGNORE_AT | FLAG_ONLY_CITY | FLAG_ANCHOR_END, sPlace);
      if (res.isValid()) {
        res.getData(data);
        sPlace = res.getStart();
      }
      data.strPlace = sPlace;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() >= 2 && field.charAt(1) == '-') {
        data.strPriority = field.substring(0,1).trim();
        field = field.substring(2).trim();
      }
      if (callCodes != null) field = convertCodes(field, callCodes);
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "PRI CODE CALL";
    }
  }

  private static final Pattern INFO_TRIM_PTN = Pattern.compile("[- *=_]{3,}$");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_TRIM_PTN.matcher(field);
      if (match.find()) field = field.substring(0,match.start());
      super.parse(field, data);
    }
  }
}
