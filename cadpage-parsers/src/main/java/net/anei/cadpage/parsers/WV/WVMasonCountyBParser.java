package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVMasonCountyBParser extends FieldProgramParser {

  public WVMasonCountyBParser() {
    super("MASON COUNTY", "WV",
          "CALL! CFS#:ID! CALL_TYPE:CODE! LOC:ADDRCITY! LAT/LONG:GPS! RESP:UNIT! END");
  }

  @Override
  public String getFilter() {
    return "no-reply@zuercherportal.com,zuercher@masoncountyoes.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (body.startsWith("CFS #:")) {
      if (subject.length() == 0) return false;
      body = subject + " CFS#: " + body.substring(6);
    } else {
      body = body.replace("CFS# ", "CFS#:");
    }
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String zip =  null;
      String city = p.getLastOptional(',');
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        zip = match.group(2);
        city = p.getLastOptional(',');
      }
      if (city.length() == 0 && zip != null) city = zip;
      data.strCity = city;
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }
}
