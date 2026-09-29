package net.anei.cadpage.parsers.dispatch;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchC11Parser extends FieldProgramParser {

  public final static int C11_UNIT = 1;
  public final static int C11_INFO = 2;

  private final Properties cityCodes;
  private Pattern codePtn =  null;
  private Set<String> codeList = null;

  public DispatchC11Parser(String defCity, String defState, int flags) {
    this(null, defCity, defState, flags);
  }

  public DispatchC11Parser(Properties cityCodes, String defCity, String defState, int flags) {
    super(cityCodes, defCity, defState,
          "( SELECT/1 DISP ID CODE? CALL EMPTY? ADDRCITYST1 EMPTY? X! ( END | GPS! MAP " +
                                                                     ((flags & C11_UNIT) != 0 ? "UNIT! " : "") +
                                                                     ((flags & C11_INFO) != 0 ? "INFO/N+ " : "") +
                                                                     ") " +
          "| CALL CALL/SDS ADDRCITYST2! " +
          ") END");
    this.cityCodes = cityCodes;
  }

  public void setCodePattern(String codePtn) {
    this.codePtn = (codePtn == null ? null : Pattern.compile(codePtn));
  }

  public void setCodeList(String ... args) {
    this.codeList = new HashSet<String>(Arrays.asList(args));
  }

  private static final Pattern DELIM = Pattern.compile("~ ");

  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("Subject: '' Message: 'UDTS: ")) {
      setSelectValue("2");
      body = body.substring(28).trim();
      body = stripFieldEnd(body, "'");
    } else {
      setSelectValue("1");
      if (body.endsWith("~")) body = body.substring(0, body.length()-1);
    }
    return parseFields(DELIM.split(body, -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DISP")) return new SkipField("DISP|CIN", true);
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITYST1")) return new MyAddressCityStateField1();
    if (name.equals("ADDRCITYST2")) return new MyAddressCityStateField2();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("INFO")) return new MyInfoField();

    return super.getField(name);
  }

  private class MyCodeField extends CodeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      do {
        if (codePtn != null && codePtn.matcher(field).matches()) break;
        if (codeList != null && codeList.contains(field)) break;
        return false;
      } while (false);
      super.parse(field, data);
      return true;
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:#|APT|LOT|APARTMENT|RM|ROOM) *(.*)");
  private static final Pattern STATE_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
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

      String addr = p.get(',');
      String city = p.get(',');
      if (isValidAddress(city)) {
        data.strPlace = append(data.strPlace, " - ", addr);
        addr = city;
        city = p.get(',');
      }
      parseAddress(addr, data);
      if (data.strAddress.isEmpty()) abort();
      if (city.startsWith("#")) {
        data.strApt = append(data.strApt, "-", city.substring(1).trim());
        city = p.get(',');
      }

      if (city.startsWith("BLDG")) {
        data.strApt = append(city, " #", data.strApt);
        city = p.get(',');
      }

      if (cityCodes != null) city = convertCodes(city, cityCodes);
      data.strCity = city;

      data.strApt = append(data.strApt, "-", trailApt);

      String zip = null;
      String state =  p.get(',');
      if (state.isEmpty()) return;
      Matcher match = STATE_ZIP_PTN.matcher(state);
      if (!match.matches()) abort();
      data.strState = match.group(1);
      zip = match.group(2);

      if (zip ==  null) {
        zip = p.get(',');
        if (zip.isEmpty()) return;
        if (!ZIP_PTN.matcher(zip).matches()) abort();
      }
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
      if (cityCodes != null) city = convertCodes(city, cityCodes);
      data.strCity = city;

      String state =  p.get(',');
      if (state.isEmpty()) return;
      if (!STATE_ZIP_PTN.matcher(state).matches()) abort();
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

  private static final Pattern INFO_BRK_PTN = Pattern.compile(" +(?:-{3,}|/{2}) +");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("CO=")) {
        int pt = field.indexOf(',');
        if (pt < 0) return;
        field = field.substring(pt+1).trim();
      }
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n");
      super.parse(field, data);
    }
  }
}
