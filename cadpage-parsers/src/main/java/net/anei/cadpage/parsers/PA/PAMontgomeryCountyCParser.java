package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;



public class PAMontgomeryCountyCParser extends FieldProgramParser {

  public PAMontgomeryCountyCParser() {
    super(PAMontgomeryCountyParser.CITY_CODES, "MONTGOMERY COUNTY", "PA",
        "( SELECT/RR INCIDENT:ID! CODE:CALL! PLACE ADDR! Cross_Street:X! ESZ:MAP! MUN:CITY! INFO/N+ " +
        "| ADDR/S TRUCKS:UNITADDR? XST:X! MUN:CITY? NAT:CALL! BOX:BOX ADC:MAP I#:ID TIME:TIME NOTES:INFO TRUCKS:UNIT )");
   }

  @Override
  public String getFilter() {
    return "mcdps@mcad911.com";
  }

  private static final Pattern LEAD_ID_PTN = Pattern.compile("(\\d{7}) +");
  private static final Pattern LEAD_EVENT_PTN = Pattern.compile("EVENT: *([EF]\\d{7})(?: +/([A-Z]{4})| HYDRANT OOS -| [A-Z ]+:)? +");
  private static final Pattern LEAD_APT_PTN = Pattern.compile("#(\\S+) +");
  private static final Pattern SPECIAL_ALERT1_PTN = Pattern.compile("^#([A-Z]\\d+) at (.*?), Note: (.*)");
  private static final Pattern SPECIAL_CITY_PTN = Pattern.compile("(.*?) +([A-Z]{4})");
  private static final Pattern COMMA_DELIM = Pattern.compile(",(?=BOX:|TIME:|NOTES:)");
  private static final Pattern CITY_COLON_PTN = Pattern.compile(" ([A-Z]{4}):");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (!subject.equals("MCDPS CAD MESSAGE")) return false;

    // Process run reports
    if (body.startsWith("------ Clear Report")) {
      return parseRunReport(body, data);
    } else {
      return parseAlert(body, data);
    }
  }

  private static final Pattern RR_DELIM_PTN = Pattern.compile("\n| +(?=INCIDENT:|ESZ:|MUN:)");

  private boolean parseRunReport(String body, Data data) {
    setSelectValue("RR");
    data.msgType = MsgType.RUN_REPORT;
    return parseFields(RR_DELIM_PTN.split(body), data);
  }

  private boolean parseAlert(String body, Data data) {

    setSelectValue("");

    Matcher match = LEAD_ID_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end());
    } else if ((match = LEAD_EVENT_PTN.matcher(body)).lookingAt()) {
      data.strCallId = match.group(1);
      body = body.substring(match.end());
    } else {
      body = stripFieldStart(body, "MCDPS CAD MESSAGE ");
    }

    // If body ends with dash,remove it
    body = stripFieldEnd(body, "-");

    // Process special general alerts
    match = SPECIAL_ALERT1_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("ID ADDR CITY APT PLACE CALL");
      data.strCallId = match.group(1);
      parseAddress(match.group(2).trim(), data);
      data.strCall = match.group(3).trim();

      checkDoubleCity(data);

      return true;
    }

    // Process regular alerts

    match = LEAD_APT_PTN.matcher(body);
    if (match.lookingAt()) {
      data.strApt = match.group(1);
      body = body.substring(match.end());
    }

    body = body.replace(",MAP/BOX-PLAN:", " BOX:");
    body = body.replace(", MAP/BOX:", " BOX:");
    body = body.replace(",I#", " I#:");
    body = COMMA_DELIM.matcher(body).replaceAll(" ");
    if (!super.parseMsg(body, data)) return false;
    int pt = data.strAddress.indexOf(" - ");
    if (pt >= 0) {
      data.strCity = data.strAddress.substring(pt+3).trim();
      data.strAddress = data.strAddress.substring(0,pt).trim();
    }
    if (data.strAddress.isEmpty() || data.strAddress.equals("&")) {
      data.strAddress = "";
      parseAddress(data.strCross, data);
      data.strCross = "";
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "ID? APT? ADDR? " + super.getProgram();
  }

  private static final Pattern APT_PTN = Pattern.compile(", *(?:APT:? *)?([A-Z0-9]+)$");
  private static final Pattern APT_PLACE_PTN = Pattern.compile(": *([^@: ]*?) +@");
  private static final Pattern MSPACE_PTN = Pattern.compile(" {2,}");

  protected void parseAddress(String addr, Data data) {

    // Check for trailing apt pattern
    String apt = "";
    Matcher match = APT_PTN.matcher(addr);
    if (match.find()) {
      apt = match.group(1);
      addr = addr.substring(0,match.start()).trim();
    }

    // Break up address by second apt/place pattern
    match = APT_PLACE_PTN.matcher(addr);
    if (match.find()) {
      String newAddr = "";
      String apt2 = "";
      int lastPt = 0;
      do {
        String temp = addr.substring(lastPt, match.start()).trim();
        if (lastPt == 0) {
          newAddr = temp;
        } else {
          data.strPlace = append(data.strPlace, " - ", temp);
        }
        apt2 = append(apt2, "-", match.group(1));
        lastPt = match.end();
      } while (match.find());
      data.strPlace = append(data.strPlace, " - ", addr.substring(lastPt).trim());
      addr = newAddr;
      apt = append(apt2, "-", apt);
    }

    // Strip off trailing city
    parseAddress(StartType.START_OTHER, FLAG_ONLY_CITY | FLAG_ANCHOR_END, addr, data);
    addr = getStart();

    addr = stripFieldEnd(addr, ": EST");
    addr = MSPACE_PTN.matcher(addr).replaceAll(" ");
    super.parseAddress(addr, data);

    data.strApt = append(data.strApt, "-", apt);
    match = APT_PTN.matcher(data.strPlace);
    if (match.find()) {
      data.strApt = append(data.strApt, "-", apt);
      data.strPlace = data.strPlace.substring(0,match.start()).trim();
    }
    match = CITY_COLON_PTN.matcher(data.strAddress);
    if (match.find()) {
      String city = PAMontgomeryCountyParser.CITY_CODES.getProperty(match.group(1));
      if (city != null) {
        data.strCity = city;
        data.strAddress = data.strAddress.substring(0,match.start()).trim() + data.strAddress.substring(match.end(1));
      }
    }
    if (data.strAddress.equals("&")) data.strAddress = "";
    else if (data.strAddress.startsWith("&")) {
      String city = PAMontgomeryCountyParser.CITY_CODES.getProperty(data.strAddress.substring(1).trim());
      if (city != null) {
        data.strCity = city;
        data.strAddress = "";
      }
    }

    checkDoubleCity(data);

    int pt = data.strAddress.toUpperCase().indexOf(": ALIAS ");
    if (pt >= 0) {
      String alias = data.strAddress.substring(pt+8).trim();
      data.strAddress = data.strAddress.substring(0,pt).trim();
      if (!alias.equalsIgnoreCase(data.strAddress)) {
        data.strAddress = append(data.strAddress, " ", '('+alias+')');
      }
    }
  }

  private void checkDoubleCity(Data data) {

    if (data.strCity.length() == 0) return;
    Matcher match;
    // Check for a double city code :(
    match = SPECIAL_CITY_PTN.matcher(data.strAddress);
    if (match.matches()) {
      String city = PAMontgomeryCountyParser.CITY_CODES.getProperty(match.group(2));
      if (city != null) {
        data.strCity = city;
        data.strAddress = match.group(1);
      }
    }
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("UNITADDR")) return new MyUnitAddressField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("BOX")) return new MyBoxField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      parseAddress(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " APT PLACE CITY";
    }
  }

  private class MyUnitAddressField extends MyAddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strUnit = p.get(' ');
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "UNIT " + super.getFieldNames();
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

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }

  private class MyBoxField extends BoxField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldEnd(field, "/");
      if (field.equals("-")) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = setGPSLoc(field, data);
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "GPS INFO";
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    int pt = addr.indexOf(':');
    if (pt >= 0) addr = addr.substring(0,pt).trim();
    addr = RT309_EXPY_PTN.matcher(addr).replaceAll("RT 309");
    return addr;
  }
  private static final Pattern RT309_EXPY_PTN = Pattern.compile("\\bRT ?309 EXPY\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, PAMontgomeryCountyParser.MAP_CITIES);
  }
}
