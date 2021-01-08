package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA65Parser extends FieldProgramParser {

  public DispatchA65Parser(String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "CFS:ID! MSG:CALL! CALL/SDS+? EMPTY EMPTY+? ( SRC END | CITY? ADDR DUP? APT? CS:X? EMPTY+? SRC END )");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("E911-Page")) return false;
    body = body.trim();
    body = body.replace("\nX:", "\nCS:").replace("\nApt:", "\nAPT:");
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.length() == 0) data.msgType = MsgType.GEN_ALERT;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DUP")) return new MyDupField();
    if (name.equals("APT")) return new AptField("(?:APT|LOT|RM|ROOM|STE)[:# ]+(.*)|([A-Z]?\\d{1,4} *[A-Z]?)|[A-Z]{1,2}");
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("SRC")) return new MySourceField();
    return super.getField(name);
  }

  private static final Pattern CITY_ST_PTN = Pattern.compile("(.*), *([A-Z]{2})");
  private static final Pattern BORO_PTN = Pattern.compile("([ A-Z]+) (?:BORO|BOROUGH)");
  private static final Pattern TWP_PTN = Pattern.compile(".* (?:TWP|TOWNSHIP)");
  private static final Pattern COUNTY_PTN = Pattern.compile("(?:(.*) )?([A-Z]+) (?:COUNTY|COUTNY)");
  private class MyCityField extends CityField {
    @Override
    public boolean canFail() {
      return true;
    }

    public boolean checkParse(String field, Data data) {

      field = field.toUpperCase();
      field = stripFieldEnd(field, ",");

      boolean force = false;
      Matcher match = CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        force = true;
      }

      match = BORO_PTN.matcher(field);
      if (match.matches()) {
        data.strCity = match.group(1).trim();
        return true;
      }

      match = TWP_PTN.matcher(field);
      if (match.matches()) {
        data.strCity = field;
        return true;
      }

      match = COUNTY_PTN.matcher(field);
      if (match.matches()) {
        String city = match.group(1);
        if (city != null) {
          data.strCity = city.trim();
        } else {
          data.strCity = match.group(2).trim() + "COUNTY";
        }
        return true;
      }

      if (force) {
        super.parse(field, data);
        return true;
      }

      else {
        return super.checkParse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.equals("NOTIFY")) {
        data.strCall = append(data.strCall, " - ", field);
      } else {
        super.parse(field, data);
      }
    }
  }

  private class MyDupField extends SkipField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      return field.length() > 0 && field.equals(getRelativeField(-1));
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "* /");
      field = stripFieldEnd(field, "/ *");
      if (field.equals("*")) return;
      super.parse(field, data);
    }
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(':');
      if (pt >= 0) field = field.substring(0,pt).trim();
      super.parse(field, data);
    }
  }
}
