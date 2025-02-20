package net.anei.cadpage.parsers.SD;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Lincoln County, SD
 */
public class SDLincolnCountyParser extends FieldProgramParser {

  private static final Pattern GEN_ALERT_PTN = Pattern.compile("MEETING|TRAINING|DISREGARD", Pattern.CASE_INSENSITIVE);
  private static final Pattern SUBJECT_MSG_PTN = Pattern.compile("([ /A-Z0-9]+):(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern SRC_PTN = Pattern.compile("NONE||(?:[A-Z0-9 ]+, +)?(?:(?:CANTON|CENTERVILLE|CHANCELLOR|DAVIS|HARRISBURG|HURLEY|LENNOX|MARION|MONROE|PARKER|SIOUX FALLS|TEA|VIBORG|WORTHINGTON) (?:AMB|AMBULANCE|FD|FIRE|FIRE DEPARTMENT|POLICE)|.* SHERIFF's OFFICE|PARAMEDICS PLUS|TCSO)(?:;[ A-Z;']+)?", Pattern.CASE_INSENSITIVE);
  private static final Pattern LEAD_NUMBER = Pattern.compile("^\\d+ +(?!Y/O |YO ).*");
  private static final Pattern CALL_ID_PTN = Pattern.compile("^\\{?(\\d\\d-\\d+)\\b\\}?");
  private static final Pattern MASTER_PTN = Pattern.compile("\\{?(.*?)\\}? *(\n| - )(.*)");
  private static final Pattern STANDBY_PTN = Pattern.compile("^STANDBY +(?:AT +)", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("^# *([^,]+?) *,");
  private static final Pattern CITY_ST_PTN = Pattern.compile("^([A-Z ]+)\\b *, *([A-Z]{2})(?: +\\d{5})?", Pattern.CASE_INSENSITIVE);
  private static final Pattern INFO_JUNK_PTN = Pattern.compile("[- ]*Please respond immediately\\.? *", Pattern.CASE_INSENSITIVE);
  private static final Pattern SUB_SRC_PTN = Pattern.compile("[A-Z ]+ AMB(?:ULANCE)?", Pattern.CASE_INSENSITIVE);

  private String version;

  public SDLincolnCountyParser() {
    super(CITY_LIST, "LINCOLN COUNTY", "SD",
          "( SELECT/2 ADDR INFO CALL! | SRC ADDR! ) DATETIME? INFO+ ");
    addInvalidWords("-", ":");
  }

  @Override
  public String getFilter() {
    return "no-reply@ledsportal.com,leds@lincolncountysd.org,zuercher@lincolncountysd.gov,74121";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() == 0) {
      Matcher match = SUBJECT_MSG_PTN.matcher(body);
      if (match.matches()) {
        subject = match.group(1).trim();
        body = match.group(2).trim();
      }
    }
    if (subject.equals("MESSAGE PAGE")) return false;

    // Some words identify this as a general alert
    if (GEN_ALERT_PTN.matcher(body).find()) return false;

    // Too many different formats. :(

    String tmp = body;
    int pt = tmp.indexOf('\n');
    if (pt >= 0) tmp = tmp.substring(0,pt).trim();
    if (tmp.startsWith("-")) tmp = ' ' + tmp;
    if (tmp.endsWith("-")) tmp = tmp + ' ';

    String[] flds = tmp.split(" - ", -1);
    if (subject.length() > 0 && subject.equals(flds[0])) {
      version = "2";
      return super.parseFields(flds, data);
    }

    flds[0] = flds[0].trim();
    if (SRC_PTN.matcher(flds[0]).matches()) {
      version = "1";
      data.strCall = subject;
      return parseFields(flds, data);
    }

    if (subject.length() == 0 || flds.length >= 3) return false;
    version = "0";

    // See if subject contains the address
    // with possible city and state qualifier
    Parser p = new Parser(subject);
    String addr = p.get(',');
    Result res = parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, addr);
    if (res.getStatus() >= STATUS_INTERSECTION || LEAD_NUMBER.matcher(addr).matches()) {
      setFieldList("ADDR CITY ST CALL");
      res.getData(data);
      if (data.strCity.length() == 0) data.strCity = p.get(',');
      data.strState = p.get(',');
      data.strCall = body;
      cleanup(data);
      return true;
    }

    if (SUB_SRC_PTN.matcher(subject).matches()) {
      if (body.startsWith(subject+',')) {
        res = parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_NO_IMPLIED_APT, body.substring(subject.length()+1).trim());
        if (res.isValid()) {
          setFieldList("SRC CALL ADDR APT CITY ST INFO");
          data.strSource = subject;
          res.getData(data);
          data.strSupp = res.getLeft();
          cleanup(data);
          return true;
        }
      }
    }

    setFieldList("ID CALL ADDR APT CITY ST INFO");
    boolean good = false;
    Matcher match = CALL_ID_PTN.matcher(subject);
    if (match.find()) {
      data.strCallId = match.group(1);
      subject = subject.substring(match.end()).trim();
      if (subject.startsWith("-")) subject = subject.substring(1).trim();
      if (subject.startsWith("{")) subject = subject.substring(1).trim();
      if (subject.endsWith("}")) subject = subject.substring(subject.length()-1).trim();
      if (subject.length() == 0) subject = "ALERT";
      good = true;
    }
    data.strCall = subject;

    String info = "";
    boolean hardBreak = false;
    match = MASTER_PTN.matcher(body);
    if (match.matches()) {
      body = match.group(1).trim();
      hardBreak = match.group(2).equals("\n");
      info = match.group(3).trim();
    }

    // Check for leading STANDBY qualifier
    match = STANDBY_PTN.matcher(body);
    if (match.find()) {
      data.strCall = "STANDBY: " + data.strCall;
      body = body.substring(match.end());
    }

    // See if address consists of GPS coordinates
    match = GPS_PATTERN.matcher(body);
    if (match.find()) {
      data.strAddress = body.substring(0,match.end());
      info =  append(body.substring(match.end()).trim(), " - ", info);
    }

    else {

      // See if there is an comma or = terminating the address
      pt = body.indexOf(',');
      if (pt < 0) {

        // Use the smart address parser to try and find and address
        // if unsuccessful, return false.  If successful, mark as good
        if (hardBreak) {
          parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS | FLAG_ANCHOR_END, body, data);
        } else {
          parseAddress(StartType.START_ADDR, FLAG_CHECK_STATUS, body, data);
          info = append(getLeft(), " - ", info);
        }
        if (true && !good && !isValidAddress() &&
             (!isPositiveId() || info.length() == 0)) return false;
      }

      else {

        // Otherwise, we have found an address, so parse it as best we can
        parseAddress(body.substring(0,pt).trim(), data);
        body = body.substring(pt+1).trim();

        // Might be followed by an apartment
        match = APT_PTN.matcher(body);
        if (match.find()) {
          data.strApt = match.group(1);
          body = body.substring(match.end()).trim();
        }

        // See if what is left can be identified as a city, st combination
        match = CITY_ST_PTN.matcher(body);
        if (match.find()) {
          data.strCity = match.group(1);
          data.strState = getOptGroup(match.group(2));
          if (!CITY_SET.contains(data.strCity.toUpperCase())) return false;
          body = body.substring(match.end()).trim();
          if (info == null) info = body;
          else info = append(body, " / ", info);
        } else if (info.length() > 0) {
          data.strCity = body;
          if (!CITY_SET.contains(data.strCity.toUpperCase())) return false;
        } else {
          parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, body, data);
          if (data.strCity.length() == 0) return false;
          info = getLeft();
        }
      }
    }

    info = INFO_JUNK_PTN.matcher(info).replaceAll(" ").trim();
    if (info.endsWith("/")) info = info.substring(0,info.length()-1).trim();
    data.strSupp = info;

    cleanup(data);
    return true;
  }

  private void cleanup(Data data) {
    if (data.strCity.equalsIgnoreCase("CA")) data.strCity = "CANTON";
    if (data.strCity.equalsIgnoreCase("INWOOD")) data.strState = "IA";
  }

  // New format(s) uses a FieldProgramParser program

  @Override
  protected String getSelectValue() {
    return version;
  }

  @Override
  public String getProgram() {
    if (version.equals("1")) return "CALL " + super.getProgram();
    return super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();

    return super.getField(name);
  }

  private class MySourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        data.strUnit = field.substring(0,pt).trim();
        field = field.substring(pt+1).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CALL UNIT SRC";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (city.startsWith("SD")) city = p.getLastOptional(',');
      parseAddress(p.get(), data);
      data.strCity = city;
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, " - ", field);
    }
  }

  private static final String[] CITY_LIST = new String[] {
    "BERESFORD",
    "CA",
    "CANTON",
    "FAIRVIEW",
    "HARRISBURG",
    "HUDSON",
    "LENNOX",
    "MOE",
    "NORWAY CENTER",
    "SHINDLER",
    "SIOUX FALLS",
    "TEA",
    "WORTHING",

    // Turner County
    "HURLEY",
    "MONROE",
    "PARKER",

    // Iowa
    "INWOOD"
  };

  private static final Set<String> CITY_SET  = new HashSet<String>(Arrays.asList(CITY_LIST));
}
