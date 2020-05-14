package net.anei.cadpage.parsers.IN;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class INHarrisonCountyParser extends FieldProgramParser {
  
  public INHarrisonCountyParser() {
    this("HARRISON COUNTY", "IN");
  }
  
  @Override
  public String getAliasCode() {
    return "INHarrisonCounty";
  }
  
  public INHarrisonCountyParser(String defCity, String defState) {
    super(defCity, defState, 
          "CALL PAGED? ADDRCITY UNIT MAP! INFO/N+");
  }
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("(\\d{2,4}[A-Z]?)\n(\\d{2}[A-Z]-\\d+)\n(.*)", Pattern.DOTALL);
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("MAP ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strMap = match.group(1);
      data.strCallId = match.group(2);
      data.strSupp = match.group(3);
      return true;
    }
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PAGED")) return new SkipField("PAGED", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("UNIT")) return new UnitField("[A-Z]+\\d+|[A-Z]{3}", true);
    if (name.equals("MAP")) return new MapField("\\d{2,4}[A-Z]?|0|\\d{5}|HCSD\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|LOT|ROOM) +(.*)|[A-Z]?\\d{1,4}[A-Z]?|[A-Z]", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      data.strCity = convertCodes(city, CITY_CODES);
      while (true) {
        String place = p.getLastOptional(';');
        if (place.length() == 0) break;
        Matcher match = APT_PTN.matcher(place);
        if (match.matches()) {
          String tmp = match.group(1);
          if (tmp != null) place = tmp;
          apt = append(place, "-", apt);
        } else {
          data.strPlace = append(place, " - ", data.strPlace);
        }
      }
      parseAddress(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }
  
  private static final Pattern CALLBACK_PTN = Pattern.compile("CALLBACK=(.*?) LAT=(.*?) LON=(.*?) UNC=.*");
  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CALLBACK_PTN.matcher(field);
      if (match.matches()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(2)+','+match.group(3), data);
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PHONE GPS";
    }
    
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "CEN", "CENTRAL",
      "COR", "CORYDON",
      "CRA", "CRANDALL",
      "DEP", "DEPAUW",
      "ELI", "ELIZABETH",
      "LAC", "LACONIA",
      "LAN", "LANESVILLE",
      "MAU", "MAUCKPORT",
      "MIL", "MILLTOWN",
      "NMT", "NEW MIDDLETOWN",
      "NSA", "NEW SALISBURY",
      "PAL", "PALMYRA",
      "RAM", "RAMSEY"
  });

}
