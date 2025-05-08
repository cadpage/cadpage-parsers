package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAChesterCountyD1Parser extends PAChesterCountyBaseParser {

  private static final Pattern DELIM = Pattern.compile("\\*{2,}");

  public PAChesterCountyD1Parser() {
    super("( EMPTY UNIT TIME CALL ADDR X PLACE_APT EMPTY NAME PHONE BOX ID EMPTY CITY | TIME! CALL ADDR ) INFO1 INFO+");
  }

  @Override
  public String getFilter() {
    return "@ridgefirecompany.com,dispatch@berwynfireco.org,ADI64@norco.com,37@modena37.com,@alerts.stationcad.com,@alerts2.stationcad.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    // And all of the should treat line breaks as spaces
    body = body.replace('\n', ' ');

    // Split and parse by double asterisk delimiters
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PLACE_APT")) return new MyPlaceAptField();
    if (name.equals("BOX")) return new BoxField("\\d{4}", true);
    if (name.equals("ID")) return new IdField("F\\d{8}", true);
    if (name.equals("INFO1")) return new MyInfoField(true);
    if (name.equals("INFO")) return new MyInfoField(false);
    return super.getField(name);
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "*");
      field = stripFieldEnd(field, "-");
      super.parse(field, data);
    }
  }

  private static final Pattern PLACE_PHONE_PTN = Pattern.compile("(.*?)[-/ ]+(\\d{3}-\\d{3}-\\d{4})");
  private static final Pattern APT_PREFIX_PTN = Pattern.compile("(?:APT|SUITE|ROOM|RM|LOT)[- ]*(.*)");
  private class MyPlaceAptField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = PLACE_PHONE_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strPhone = match.group(2);
      } else {
        int pt = field.lastIndexOf('-');
        if (pt >= 0) {
          String apt = field.substring(pt+1).trim();
          match = APT_PREFIX_PTN.matcher(apt);
          if (match.matches()) apt = match.group(1);
          data.strApt = append(data.strApt, "-", apt);
          field = field.substring(0,pt).trim();
        }
      }
      field = stripFieldStart(field, "-");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT PHONE";
    }
  }

  private static final Pattern BOX_PTN = Pattern.compile("\\d{4}");
  private static final Pattern ID_PTN = Pattern.compile("F\\d{8}", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|SUITE)[- /]+([^-]+?)-+(.*)");
  private static final Pattern PHONE_PTN = Pattern.compile(".*\\b(?:CP)?\\d{3}[-\\.]?\\d{3}[-\\.]?\\d{4}\\b.*");
  private class MyInfoField extends InfoField {
    private boolean place;

    public MyInfoField(boolean place) {
      this.place = place;
    }

    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "-");
      if (field.length() == 0) return;
      if (BOX_PTN.matcher(field).matches()) {
        data.strBox = append(data.strBox, "/", field);
        return;
      }
      if (PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field;
        return;
      }
      if (ID_PTN.matcher(field).matches()) {
        data.strCallId = field;
        return;
      }
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1).trim());
        data.strPlace = match.group(2).trim();
        return;
      }
      if (field.length() <= 11) {
        String city = CITY_CODES.getProperty(field);
        if (city != null) {
          if (data.strCity.length() == 0) data.strCity = city;
          return;
        }
      }
      if (place) {
        data.strPlace = field;
        return;
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE PHONE BOX INFO";
    }
  }
}
