package net.anei.cadpage.parsers.DE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DENewCastleCountyDParser extends FieldProgramParser {

  public DENewCastleCountyDParser() {
    super("NEW CASTLE COUNTY", "DE",
          "CALL:CALL! ADDR:ADDR/S6! ( DCITY:CITY DCITY:ST | CITY:CITY ST:ST | ) APT:APT? PL:PLACE? XST:X? UNIT:UNIT? INFO:INFO? INFO+? DCITY:SKIP CITY:SKIP");
    setupSpecialStreets("LANDERS SP");
  }

  @Override
  public String getFilter() {
    return "@c-msg.net";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (body.contains("\nADDR:")) {
      if (!parseFields(body.split("\n"), data)) return false;
    } else {
      if (!super.parseMsg(body, data)) return false;
    }
    DENewCastleCountyEParser.fixCity(data);
    return true;
  }

  @Override
  public String getProgram() {
    return super.getProgram().replace("CITY", "PLACE CITY ST");
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      field = DENewCastleCountyEParser.checkDashCity(field, data);
      super.parse(field, data);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.toUpperCase().startsWith("INTERSTATE")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN1 = Pattern.compile("((?:APT|LOT|RM|ROOM|SUITE|UNIT)?[- #]*)(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN2 = Pattern.compile("\\d{1,4}[A-Z]?|[A-Z]|BLDG +.*", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      boolean forceApt = false;
      Matcher match = APT_PTN1.matcher(field);
      if (match.matches()) {  // Always matches
        if (match.group(1).length() > 0) forceApt = true;
        field = match.group(2);
      }
      if (data.strApt.equalsIgnoreCase(field)) return;

      if (forceApt || APT_PTN2.matcher(field).matches()) {
        data.strApt = append(data.strApt, " - ", field);
      } else {
        data.strPlace = field;
      }
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (data.strPlace.length() > 0) {
        data.strApt = append(data.strApt, " - ", data.strPlace);
      }
      data.strPlace = field;
    }
  }

  private class MyInfoField extends InfoField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("CITY:") || field.startsWith("DCITY:")) return false;
      data.strSupp = append(data.strSupp, "\n", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern NOT_EXTRA_APT_PTN = Pattern.compile("ONRAMP|OFFRAMP|EXIT\\b.*|[/&].*", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean isNotExtraApt(String apt) {
    return NOT_EXTRA_APT_PTN.matcher(apt).matches();
  }

  @Override
  public String adjustMapAddress(String addr) {
    return DENewCastleCountyEParser.adjustMapAddressStatic(addr);
  }

  @Override
  public String adjustMapCity(String city) {
    return DENewCastleCountyEParser.adjustMapCityStatic(city);
  }
}
