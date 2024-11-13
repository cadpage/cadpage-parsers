package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NCStanlyCountyCParser extends FieldProgramParser {

  public NCStanlyCountyCParser() {
    super("STANLY COUNTY", "NC",
          "( SELECT/1 DISP ID CALL ADDRCITYST1 X! ( END | GPS EMPTY UNIT! ) " +
          "| CALL CALL/SDS ADDRCITYST2! " +
          ") END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile("~ ");

  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Subject: '' Message: 'UDTS: ")) {
      setSelectValue("2");
      body = body.substring(28).trim();
      body = stripFieldEnd(body, "'");
    } else {
      setSelectValue("1");
      if (!body.endsWith("~")) return false;
      body = body.substring(0, body.length()-1).trim();
    }
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DISP")) return new SkipField("DISP|CIN", true);
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITYST1")) return new MyAddressCityStateField1();
    if (name.equals("ADDRCITYST2")) return new MyAddressCityStateField2();
    if (name.equals("X")) return new MyCrossField();

    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:#|APT|LOT|APARTMENT|RM|ROOM) *(.*)");
  private static final Pattern STATE_PTN = Pattern.compile("[A-Z]{2}");
  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private class MyAddressCityStateField1 extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(';', ':');
      Parser p = new Parser(field);
      String place = p.get(':');
      if (place.equals("EMS")) place = p.get(':');
      data.strPlace = place;

      String trailApt = p.getLastOptional(':');
      if (trailApt.equals(place)) {
        trailApt = "";
      }
      else if (!trailApt.isEmpty()) {
        Matcher match = APT_PTN.matcher(trailApt);
        if (match.matches()) trailApt = match.group(1);
      }

      parseAddress(p.get(','), data);
      if (data.strAddress.isEmpty()) abort();
      String city = p.get(',');
      if (city.startsWith("#")) {
        data.strApt = append(data.strApt, "-", city.substring(1).trim());
        city = p.get(',');
      }
      data.strCity = city;

      data.strApt = append(data.strApt, "-", trailApt);

      String state =  p.get(',');
      if (state.isEmpty()) return;
      if (!STATE_PTN.matcher(state).matches()) abort();
      data.strState = state;

      String zip = p.get(',');
      if (zip.isEmpty()) return;
      if (!ZIP_PTN.matcher(zip).matches()) abort();
      if (data.strCity.isEmpty()) data.strCity = zip;

      data.strApt = append(data.strApt, "-", p.get());
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY ST";
    }
  }
  private class MyAddressCityStateField2 extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      parseAddress(p.get(','), data);
      if (data.strAddress.isEmpty()) abort();
      String city = p.get(',');
      if (city.startsWith("#")) {
        data.strApt = append(data.strApt, "-", city.substring(1).trim());
        city = p.get(',');
      }
      data.strCity = city;

      String state =  p.get(',');
      if (state.isEmpty()) return;
      if (!STATE_PTN.matcher(state).matches()) abort();
      data.strState = state;

      String zip = p.get(',');
      if (zip.isEmpty()) return;
      if (!ZIP_PTN.matcher(zip).matches()) abort();
      if (data.strCity.isEmpty()) data.strCity = zip;

      data.strApt = append(data.strApt, "-", p.get());
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT CITY ST";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
