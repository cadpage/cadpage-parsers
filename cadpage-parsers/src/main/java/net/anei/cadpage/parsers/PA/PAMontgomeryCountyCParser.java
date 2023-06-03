package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PAMontgomeryCountyCParser extends FieldProgramParser {

  public PAMontgomeryCountyCParser() {
    super(PAMontgomeryCountyParser.CITY_CODES, "MONTGOMERY COUNTY", "PA",
        "ADDR/S TRUCKS:UNITADDR? XST:X! MUN:CITY? NAT:CALL! BOX:BOX ADC:MAP I#:ID TIME:TIME NOTES:INFO TRUCKS:UNIT");
   }

  @Override
  public String getFilter() {
    return "@c-msg.net,eoccomm@montcopa.org,montcopage@comcast.net";
  }

  private static final Pattern LEAD_ID_PTN = Pattern.compile("(\\d{7}) +");
  private static final Pattern LEAD_EVENT_PTN = Pattern.compile("EVENT: *([EF]\\d{7}) +/([A-Z]{4}) +");
  private static final Pattern LEAD_APT_PTN = Pattern.compile("#(\\S+) +");
  private static final Pattern MASTER1 = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d) #(F\\d{7}) at (.*?), Note:(.*) -");
  private static final Pattern DATE_TIME_MARKER = Pattern.compile("^(\\d\\d:\\d\\d:\\d\\d) (?:(\\d\\d-\\d\\d-\\d\\d) )?+(?:EVENT: *([A-Z]\\d+) +)?");
  private static final Pattern SPECIAL_ALERT1_PTN = Pattern.compile("^#([A-Z]\\d+) at (.*?), Note: (.*)");
  private static final Pattern SPECIAL_CITY_PTN = Pattern.compile("(.*?) +([A-Z]{4})");
  private static final Pattern APT_PTN = Pattern.compile(", *(?:APT:? *)?([A-Z0-9]+)$");
  private static final Pattern COMMA_DELIM = Pattern.compile(",(?=BOX:|TIME:|NOTES:)");
  private static final Pattern START_UNIT_MARK_PTN = Pattern.compile("(?:[A-Z0-9,]+,)?\\d+-\\d[ ,].*");
  private static final Pattern CITY_COLON_PTN = Pattern.compile(" ([A-Z]{4}):");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

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

    // Check for an uncommon variant
    match = MASTER1.matcher(body);
    if (match.matches()) {
      setFieldList("TIME ID ADDR APT CITY PLACE CALL");
      data.strTime = match.group(1);
      data.strCallId = match.group(2);
      parseAddress(match.group(3).trim(), data);
      data.strCall = match.group(4).trim();
      return true;
    }

    // If body ends with dash,remove it
    body = stripFieldEnd(body, "-");

    // Process Date/Time/ID marker
    boolean dateTimeMark = false;
    match = DATE_TIME_MARKER.matcher(body);
    if (match.find()) {
      dateTimeMark = true;
      data.strTime = match.group(1);
      data.strDate = getOptGroup(match.group(2)).replace('-', '/');
      data.strCallId = getOptGroup(match.group(3));
      body = body.substring(match.end());
    }

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

    // One agency seems to have dropped the TRUCKS: label, figuring out when it
    // is needed is a hassle
    if (subject.length() == 0 && !body.contains("TRUCKS:") &&
        START_UNIT_MARK_PTN.matcher(body).matches()) {
      body = "TRUCKS:" + body;
    }

    body = body.replace(",MAP/BOX-PLAN:", " BOX:");
    body = body.replace(", MAP/BOX:", " BOX:");
    body = body.replace(",I#", " I#:");
    body = COMMA_DELIM.matcher(body).replaceAll(" ");
    if (super.parseMsg(body, data)) {
      if (data.strAddress.isEmpty() || data.strAddress.equals("&") || data.strAddress.startsWith("EVENT:")) {
        data.strAddress = "";
        parseAddress(data.strCross, data);
        data.strCross = "";
      }
      return true;
    }

    // If parse failed, but we have a good date/time marker
    // process this as a general alert
    if (dateTimeMark) {
      data.strCall = "GENERAL ALERT";
      data.strPlace = body;
      data.strAddress = "";
      return true;
    }
    return false;
  }

  protected void parseAddress(String addr, Data data) {

    String apt = "";
    Matcher match = APT_PTN.matcher(addr);
    if (match.find()) {
      apt = match.group(1);
      addr = addr.substring(0,match.start()).trim();
    }

    int pt = addr.indexOf(": @");
    if (pt >= 0) {
      data.strPlace = addr.substring(pt+3).trim();
      addr = addr.substring(0,pt).trim();
    }

    match = APT_PTN.matcher(addr);
    if (match.find()) {
      apt = append(match.group(1), "-", apt);
      addr = addr.substring(0,match.start()).trim();
    }

    parseAddress(StartType.START_ADDR, FLAG_EMPTY_ADDR_OK | FLAG_ANCHOR_END, addr, data);

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

    pt = data.strAddress.toUpperCase().indexOf(": ALIAS ");
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
  public String getProgram() {
    return "TIME? DATE? ID? ADDR " + super.getProgram();
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
