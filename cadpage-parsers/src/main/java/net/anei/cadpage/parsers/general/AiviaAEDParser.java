package net.anei.cadpage.parsers.general;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class AiviaAEDParser extends FieldProgramParser {

  public AiviaAEDParser() {
    super("", "",
          "( AIVIA_alert:SKIP! AIVIA_Serial_Num.:ID! CALL_ADDR Event:SKIP! Date_/_Time:DATETIME! Location:EMPTY! ADDR2 EMPTY CITY2 EMPTY PLACE2! " +
          "| CALL1! Date/Hour:DATETIME! CALL/SDS ADDR ZIP_CITY! " +
          ")");
  }

  @Override public String getFilter() {
    return "supervisionaivianet@hd1py.com";
  }

  @Override
  public String getLocName() {
    return "Avida AED";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.toUpperCase().startsWith("AIVIA ")) return false;
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ZIP_CITY")) return new MyZipCityField();

    if (name.equals("CALL_ADDR")) return new MyCallAddressField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("CITY2")) return new MyCity2Field();
    if (name.equals("PLACE2")) return new MyPlace2Field();
    return super.getField(name);
  }

  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "Alarm on Aivia ");
      super.parse(field, data);
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d{2}-\\d{2}) (\\d\\d?:\\d\\d:\\d\\d)\\b.*");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2).replace('-', '/') + '/' + match.group(1);
      data.strTime = match.group(3);
    }
  }

  private class MyAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt < 0) abort();
      data.strPlace = field.substring(pt+1).trim();
      parseAddress(field.substring(0,pt).trim(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE";
    }
  }

  private class MyZipCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf('-');
      if (pt < 0) abort();
      data.strCity = field.substring(pt+1).trim();
      if (data.strCity.length() == 0) data.strCity = field.substring(0,pt).trim();
    }
  }

  private class MyCallAddressField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCall = p.get(" - ");
      data.strPlace = p.get(',');
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "CALL " + super.getFieldNames() + " PLACE";
    }
  }

  private class MyAddress2Field extends AddressField {
    @Override
    public void parse(String field, Data data) {
      if (data.strAddress.isEmpty()) super.parse(field, data);
    }
  }

  private class MyCity2Field extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.isEmpty()) {
        int pt = field.indexOf('(');
        if (pt >= 0) field = field.substring(0,pt).trim();
        super.parse(field, data);
      }
    }
  }

  private class MyPlace2Field extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (data.strPlace.isEmpty()) super.parse(field, data);
    }
  }

}
