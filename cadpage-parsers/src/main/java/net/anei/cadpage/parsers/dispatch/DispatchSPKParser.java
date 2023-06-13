package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchSPKParser extends HtmlProgramParser {

  private static final Map<String, Field> FIELD_MAP = new HashMap<String, Field>();

  public DispatchSPKParser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchSPKParser(Properties cityCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
         "( SELECT/1 Incident_Information%EMPTY! Event_Code:CALL! Incident_Number:ID! Location_Information%EMPTY! LOCATION! LOCATION_X? ( Remarks/Narratives%EMPTY! INFO1/N+? | ) Responding_Units%EMPTY! UNIT! SKIP+? LINE_MARK! " +
         "| CURDATETIME? Incident_Information%EMPTY! CAD_Incident:ID? ( Event_Code:CALL! THRD_PRTY_INFO+? | Event_Code_Description:CALL! | ) DATA<+? )",
         "table|tr");

    Field addrCityField = getField("ADDRCITY");
    Field alertField = getField("ALERT");
    Field aptField = getField("APT");
    Field bldgField = getField("BLDG");
    Field callerLocField = getField("CALLER_LOC");
    Field cityField = getField("CITY");
    Field crossField = getField("X");
    Field emptyField = getField("EMPTY");
    Field idField = getField("ID");
    Field GPSField = getField("GPS");
    Field mapField = getField("MAP");
    Field nameField = getField("NAME");
    Field phoneField = getField("PHONE");
    Field placeField = getField("PLACE");
    Field priorityField = getField("PRI");
    Field skipField = getField("SKIP");
    Field unitField = getField("UNIT");
    Field zipField = getField("ZIP");

    Field infoField = getField("INFO");

    FIELD_MAP.put("Alias", placeField);
    FIELD_MAP.put("ANI/ALI Info", skipField);
    FIELD_MAP.put("Apartment", aptField);
    FIELD_MAP.put("Apt", aptField);
    FIELD_MAP.put("Areas", mapField);
    FIELD_MAP.put("Bldg", bldgField);
    FIELD_MAP.put("Building", bldgField);
    FIELD_MAP.put("Caller information", emptyField);
    FIELD_MAP.put("Caller Location", callerLocField);
    FIELD_MAP.put("Caller Name",  nameField);
    FIELD_MAP.put("Caller Phone", phoneField);
    FIELD_MAP.put("Caller Source", skipField);
    FIELD_MAP.put("Community", cityField);
    FIELD_MAP.put("Created By", skipField);
    FIELD_MAP.put("Cross Street", crossField);
    FIELD_MAP.put("Driveway Safety Info", alertField);
    FIELD_MAP.put("EMS Information", infoField);
    FIELD_MAP.put("Incident Creation Date",  skipField);
    FIELD_MAP.put("Incident Disposition", skipField);
    FIELD_MAP.put("Incident Number", idField);
    FIELD_MAP.put("Intersection", skipField);
    FIELD_MAP.put("Intersections", skipField);
    FIELD_MAP.put("L/L", GPSField);
    FIELD_MAP.put("Location", addrCityField);
    FIELD_MAP.put("Location and POI Information", placeField);
    FIELD_MAP.put("Location Information", placeField);
    FIELD_MAP.put("Media Attached To Incident", skipField);
    FIELD_MAP.put("Name", skipField);
    FIELD_MAP.put("Notes", skipField);
    FIELD_MAP.put("Notices", skipField);
    FIELD_MAP.put("Persons", skipField);
    FIELD_MAP.put("POI Information", placeField);
    FIELD_MAP.put("Priors", skipField);
    FIELD_MAP.put("Priority", priorityField);
    FIELD_MAP.put("Responding Units", unitField);
    FIELD_MAP.put("Service Requests", skipField);
    FIELD_MAP.put("Wrecker Info", skipField);
    FIELD_MAP.put("Zip Code", zipField);

    for (String infoKeyword : INFO_KEYWORDS.keySet()) {
      FIELD_MAP.put(infoKeyword, infoField);
    }
  }

  private String callerLocField;

  private boolean dispatchTime;
  private String times;
  private Set<String> unitSet = new HashSet<String>();

  private enum InfoType { CAD_TIMES, REMARKS, UNIT_INFO, UNIT_INFO2, UNIT_STATUS };
  private InfoType infoType;
  private int colNdx;

  private static final Pattern SUBJECT_UNIT_PTN = Pattern.compile("Unit (.*?) from .*");
  private static final Pattern DELIM = Pattern.compile(" *\n[* ]*");

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    if (!subject.contains(" reaches status ") &&
        !subject.contains(" gets ") &&
        !subject.contains("has a Service Request status change")) return false;

    unitSet.clear();
    callerLocField = null;
    dispatchTime = false;
    times = null;
    infoType = null;
    colNdx = -1;

    prevFld = procFld = null;

    if (body.startsWith("Incident Information\n")) {
      setSelectValue("1");
      if (!parseFields(DELIM.split(body), data)) return false;
    } else {
      setSelectValue("2");
      if (!super.parseHtmlMsg(subject, body, data)) return false;
    }

    if (data.strCall.length() == 0) data.strCall = "ALERT";

    Matcher match = SUBJECT_UNIT_PTN.matcher(subject);
    if (match.matches()) {
      addUnit(match.group(1).trim(), data);
    }

    if (data.strAddress.length() == 0 && callerLocField != null) parseAddress(callerLocField, data);
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
    unitSet.clear();
    return true;
  }

  @Override
  public String getProgram() {
    return "UNIT? " + super.getProgram() + " CALL";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("LINE_MARK")) return new SkipField("_{5,}", true);
    if (name.equals("LOCATION")) return new AddressCityField("Location +(.*)", true);
    if (name.equals("LOCATION_X")) return new CrossField("Location Information +(.*)", true);
    if (name.equals("INFO1")) return new BaseInfo1Field();

    if (name.equals("CURDATETIME")) return new BaseDateTimeField();
    if (name.equals("ID")) return new IdField("\\d[-0-9]{8,}(?:\\.\\d{3})?|", true);
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("THRD_PRTY_INFO")) return new BaseThirdPartyInfoField();
    if (name.equals("DATA")) return new BaseDataField();

    if (name.equals("ADDRCITY")) return new BaseAddressCityField();
    if (name.equals("CALLER_LOC")) return new BaseCallerLocField();
    if (name.equals("CITY")) return new BaseCityField();
    if (name.equals("ZIP")) return new BaseZipField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("BLDG")) return new BaseBuildingField();
    if (name.equals("APT")) return new BaseAptField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("PHONE")) return new BasePhoneField();

    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PREFIX_PTN = Pattern.compile("\\d\\d?/\\d\\d?/\\d\\d +\\d\\d?:\\d\\d:\\d\\d +");
  private class BaseInfo1Field extends InfoField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.equals("Responding Units")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PREFIX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }


  private static final Pattern DATE_TIME_PTN = Pattern.compile("As of (\\d\\d?/\\d\\d?/\\d\\d) (\\d\\d:\\d\\d:\\d\\d(?: [AP]M)?)");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      data.strTime = convertTime(match.group(2));
    }
  }

  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strCode = field.substring(0,pt).trim();
        field = field.substring(pt+3).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class BaseThirdPartyInfoField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      if (field.startsWith("3rd Party Complaint: Medical Complaint: ")) {
        data.strCall = field.substring(40).trim();
        return true;
      }

      if (field.startsWith("3rd Party Code: Dispatch Code: ")) {
        data.strCode = field.substring(31).trim();
        return true;
      }

      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private Field procFld;
  private Field prevFld;

  /**
   * This class handles a large collection of data field that can come in any order.
   */
  private class BaseDataField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("This message")) return false;
      parse(field, data);
      return true;
    }


    @Override
    public void parse(String field, Data data) {

      if (field.startsWith("<|") && field.endsWith("|>")) {
        if (procFld != null) {
          if (procFld instanceof BaseInfoField) {
            procFld.parse(field, data);
          } else {
            if (field.equals("<|/table|>")) procFld = null;
          }
          return;
        }
      }

      // See if field contains a recognized keyword
      // There are some conflicts between the general field recognized keywords and
      // the info field recognized keywords.  So we first check to see if  we are processing
      // info field and if this is info keyword, do not check for the general data keyword
      field = stripFieldStart(field, "- ");
      if (!(procFld instanceof BaseInfoField &&
          ( field.equals("Name") || field.startsWith("Priority:")|| INFO_KEYWORD_PTN.matcher(field).matches()))) {
        Field tmpFld;
        int pt = field.indexOf(':');
        if (pt >= 0) {
          tmpFld = FIELD_MAP.get(field.substring(0,pt));
          if (tmpFld != null  && !(tmpFld instanceof BaseInfoField)) field = field.substring(pt+1).trim();
        } else {
          tmpFld = FIELD_MAP.get(field);
          if (tmpFld != null && !(tmpFld instanceof BaseInfoField)) field = "";
        }

        if (tmpFld != null) procFld = tmpFld;
      }

      // If we have a field processor, invoke it to process this field
      if (procFld != null && field.length() > 0) {
        procFld.parse(field, data);
        prevFld = procFld;
        if (!(procFld instanceof CrossField || procFld instanceof BaseInfoField)) procFld = null;
      }
    }

    @Override
    public String getFieldNames() {
      // should never be called
      throw new RuntimeException("BaseDataField.getFieldNames() should never be called");
    }

    @Override
    public Field getProcField() {
      return prevFld;
    }
  }

  private static final Pattern ADDR_APT_PTN = Pattern.compile("(.*) (?:Apartment|Building): *(.*)");
  private class BaseAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {

      // There can be multiple Location: keywords, but the second
      // always seems to be a cell tower location that we can ignore
      // Unless the first address is UNKNOWN in which case, accept the second one
      if (data.strAddress.length() == 0 || data.strAddress.equals("UNKNOWN")) {
        Matcher match = ADDR_APT_PTN.matcher(field);
        String apt = "";
        if (match.matches()) {
          field = match.group(1).trim();
          apt = match.group(2);
        }
        data.strAddress = "";
        super.parse(field, data);
        data.strApt = append(data.strApt, "-", apt);
      }
    }
  }

  private class BaseCallerLocField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      callerLocField = field;
    }
  }

  private class BaseCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      super.parse(field, data);
    }
  }

  private class BaseZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      data.strCity = field;
    }
  }

  private class BaseCrossField extends CrossField {

    @Override
    public void parse(String field, Data data) {

      // Cross streets tend to be duplicated a lot :(
      if (field.length() == 0) return;
      if (data.strAddress.contains("&")) return;
      if (data.strAddress.contains(field)) return;
      if (data.strCross.contains(field)) return;
      super.parse(field, data);
    }
  }

  private class BaseBuildingField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strApt = append(data.strApt, " ", "Bldg:" + field);
    }
  }

  private class BaseAptField extends AptField {
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      String apt = data.strApt;
      String bldg = "";
      int pt = apt.indexOf("Bldg:");
      if (pt >= 0) {
        bldg = apt.substring(pt);
        apt = apt.substring(0,pt).trim();
      }
      apt = append(apt, "-", field);
      apt = append(apt, " ", bldg);
      data.strApt = apt;
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      for (String unit : field.split(",")) {
        addUnit(unit.trim(), data);
      }
    }
  }

  private class BasePhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("-")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern TIMES_PTN = Pattern.compile("Call (.*?) Time: +(\\d\\d?/\\d\\d?/\\d\\d) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final Pattern INFO_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d\\d) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final Pattern INFO_KEYWORD_PTN = Pattern.compile("(Callback|Caller Name|Problem|Responding Units)(?:: *(.*))?");
  private static final Pattern CALL_STATUS_TIME_PTN = Pattern.compile("Call [A-Za-z ]+ Time");
  private class BaseInfoField extends InfoField {

    boolean unitTag = false;
    String unit = null;
    List<String> statusList = null;
    boolean statusLock = false;
    String infoKeyword = null;

    public BaseInfoField() {
      setQual("N");
    }

    @Override
    public void parse(String field, Data data) {

      // Special Html tag processing
      if (field.startsWith("<|") && field.endsWith("|>")) {
        if (field.equals("<|/table|>") && infoType != InfoType.UNIT_INFO2) {
          infoType = InfoType.REMARKS;
          infoKeyword = null;
          colNdx = -1;
        }
        else if (field.equals("<|tr|>")) {
          colNdx = 0;
          if (statusList != null) statusLock = true;
        }
        return;
      }
      if (colNdx >= 0) colNdx++;

      String key = stripFieldEnd(field, ":");
      InfoType tmp = INFO_KEYWORDS.get(key);
      if ((tmp != null || INFO_KEYWORDS.containsKey(key)) &&
          (infoType != InfoType.UNIT_INFO2 || !field.equals("Case Numbers")) ||
          field.startsWith("POI Information:")) {
        infoType = tmp;
        if (infoType == InfoType.UNIT_INFO && !field.endsWith(":")) infoType = InfoType.UNIT_INFO2;
        if (infoType != null) {
          statusList = null;
          switch (infoType) {
          case CAD_TIMES:
            if (times == null) times = field;
            else times = times + '\n' + field;
            break;

          case UNIT_INFO:
            colNdx = -1;
            break;

          case UNIT_STATUS:
            if (times == null) times = field;
            else times = times + '\n' + field;
            colNdx = -1;
            break;

          case UNIT_INFO2:
            if (times == null) times = field;
            else times = times + '\n' + field;
            colNdx = -1;
            unitTag = false;
            unit = null;
            break;

          case REMARKS:
            infoKeyword = null;

          default:
          }
        }
        return;
      }

      if (infoType == null) return;

      switch (infoType) {
      case CAD_TIMES:
        if (colNdx == 1 && CALL_STATUS_TIME_PTN.matcher(field).matches()) {
          statusList = new ArrayList<String>();
          statusLock = false;
        }

        if (statusList != null) {
          if (!statusLock) {
            statusList.add(field);
          } else if (INFO_TIME_PTN.matcher(field).matches()) {
            if (colNdx <= statusList.size()) {
              String status = statusList.get(colNdx-1);
              times = append(times, "\n", status + ": " + field);
              if (status.equals("Call Closed Time") || status.equals("Available")) data.msgType = MsgType.RUN_REPORT;
              if (status.equals("Call Reopened Time")) data.msgType = MsgType.PAGE;
            }
          }
        }

        else {
          Matcher match = TIMES_PTN.matcher(field);
          if (match.matches()) {
            String type = match.group(1);
            if (!dispatchTime && type.equalsIgnoreCase("Dispatched")) {
              dispatchTime = true;
              data.strDate = match.group(2);
              data.strTime = convertTime(match.group(3));
            } else if (type.equals("On Scene") ||
                       type.equals("Closed") ||
                       type.equalsIgnoreCase("Available")) data.msgType = MsgType.RUN_REPORT;
          }
          times = times + '\n' + field;
        }
        return;

      case REMARKS:
        if (INFO_TIME_PTN.matcher(field).matches()) return;
        Matcher match = INFO_KEYWORD_PTN.matcher(field);
        if (match.matches()) {
          String keyword = match.group(1);
          field = match.group(2);
          if (field != null) {
            parseInfoField(keyword, field, data);
          } else {
            infoKeyword = keyword;
          }
        }
        else if (infoKeyword != null) {
          parseInfoField(infoKeyword, field, data);
          infoKeyword = null;
          return;
        } else if (!field.equals("Number of patients: 1")) {
          super.parse(field, data);
        }
        return;

      case UNIT_INFO:
        if (colNdx != 1) return;
        if (!field.equals("Unit")) addUnit(field, data);
        return;

      case UNIT_STATUS:
        if (field.equals("Date/Time")) return;
        if (field.equals("Unit") || field.equals("Name")) return;
        if (field.equals("Status")) return;
        if (field.equals("Unit Location/Remarks")) return;
        String delim = colNdx == 1 ? "\n" : "   ";
        times = times + delim + field;

        if (colNdx == 2) addUnit(field, data);

        if (!dispatchTime) {
          match = INFO_TIME_PTN.matcher(getRelativeField(-2));
          if (match.matches()) {
            dispatchTime = true;
            data.strDate = match.group(1);
            data.strTime = convertTime(match.group(2));
          }
        }

        if (field.equalsIgnoreCase("On Scene") ||
            field.equalsIgnoreCase("Available")) data.msgType = MsgType.RUN_REPORT;
        return;

      case UNIT_INFO2:
        if (colNdx == 1) {
          if (field.equals("Unit")) {
            unitTag = true;
            statusList = null;
            return;
          }

          if (unit != null && UNIT_STATUS_SET.contains(field.toUpperCase())) {
            statusList = new ArrayList<>();
            statusList.add(field);
            statusLock = false;
            return;
          }
        }

        if (unitTag) {
          unitTag = false;
          unit = field;
          addUnit(field, data);
          return;
        }

        if (statusList != null) {
          if (!statusLock) {
            statusList.add(field);
          } else if (INFO_TIME_PTN.matcher(field).matches()) {
            if (colNdx <= statusList.size()) {
              String status = statusList.get(colNdx-1);
              String line = field + "   " + unit + "   " + status;
              times = append(times, "\n", line);
              status = status.toUpperCase();
              if (status.equals("ON SCENE") || status.equals("AVAILABLE") ||
                  status.equals("READYFORDISPATCH")) data.msgType = MsgType.RUN_REPORT;
            }
          }
        }
      }
    }

    private void parseInfoField(String keyword, String field, Data data) {
      switch (keyword) {
      case "Callback":
        data.strPhone = field;
        break;

      case "Caller Name":
        data.strName = cleanWirelessCarrier(field);
        break;

      case "Problem":
        if (data.strCall.equals("PRO QA IN PROGESS")) data.strCall = field;
        else data.strCall = append(data.strCall, " - ", field);
        break;

      case "Responding Units":
        for (String unit : field.split(",")) {
          addUnit(unit.trim(), data);
        }
        break;

        default:
          throw new RuntimeException("unexpected keyword:" + keyword);
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO UNIT CALL NAME PHONE";
    }
  }

  private void addUnit(String unit, Data data) {
    unit = unit.replace(' ', '_');
    if (unitSet.add(unit)) data.strUnit = append(data.strUnit, ",", unit);
  }

  private static String convertTime(String time) {
    if (time.endsWith("M")) {
      if (Integer.parseInt(time.substring(0, 2)) > 12) return time.substring(0, 8);
      try {
        time = TIME_FMT2.format(TIME_FMT1.parse(time));
      } catch (ParseException ex) {
        throw new  RuntimeException(ex);
      }
    }
    return time;
  }
  private static final DateFormat TIME_FMT1 = new SimpleDateFormat("hh:mm:ss aa");
  private static final DateFormat TIME_FMT2 = new SimpleDateFormat("HH:mm:ss");

  private static final Set<String> UNIT_STATUS_SET = new HashSet<String>(Arrays.asList(
      "DISPATCHED", "EN ROUTE", "ON SCENE", "AVAILABLE"
  ));

  private static final Map<String, InfoType> INFO_KEYWORDS = new HashMap<String,InfoType>();
  static {
    INFO_KEYWORDS.put("CAD Times", InfoType.CAD_TIMES);

    INFO_KEYWORDS.put("Remarks/Narratives", InfoType.REMARKS);
    INFO_KEYWORDS.put("Notices", InfoType.REMARKS);
    INFO_KEYWORDS.put("Notes", InfoType.REMARKS);
    INFO_KEYWORDS.put("EMS DISPATCH PROTOCOL", InfoType.REMARKS);
    INFO_KEYWORDS.put("Dispatch", InfoType.REMARKS);

    INFO_KEYWORDS.put("Unit Information", InfoType.UNIT_INFO);

    INFO_KEYWORDS.put("Unit Status", InfoType.UNIT_STATUS);
    INFO_KEYWORDS.put("All Unit Activity", InfoType.UNIT_STATUS);

    INFO_KEYWORDS.put("POI Information", null);
    INFO_KEYWORDS.put("Priors", null);
    INFO_KEYWORDS.put("Case Numbers", null);
    INFO_KEYWORDS.put("Incident Log", null);
  }

  @Override
  protected boolean parseFields(String[] fields, Data data) {
    fixKeywords(fields);
    return super.parseFields(fields, data);
  }

  private static void fixKeywords(String[] fields) {
    for (int ndx = 0; ndx < fields.length; ndx++) {
      String field = fields[ndx];
      if (field.startsWith("Autosend.") || field.startsWith("autosend.") || field.startsWith("call")) {
        CodeTable.Result res = FIX_KEYWORD_TABLE.getResult(field);
        if (res != null) {
          field = res.getDescription() + field.substring(res.getCode().length());
          fields[ndx] = field;
        }
      }
    }
  }

  private static final CodeTable FIX_KEYWORD_TABLE = new CodeTable(
    "autosend.incident.header",                 "As of",
    "autosend.incident.incidentInformation",    "Incident Information",
    "autosend.incident.tracking.number",        "CAD Incident ",
    "Autosend.Options.eventCode",               "Event Code",
    "autosend.incident.location",               "Location",
    "autosend.incident.community",              "Community",
    "Autosend.Options.locationInformation",     "Location Information",
    "Autosend.Options.crossStreet",             "Cross Street",
    "Autosend.Options.apartment",               "Apartment",
    "Autosend.Options.building",                "Building",
    "autosend.incident.callerInformation",      "Caller information",
    "Autosend.Options.callerPhone",             "Caller Phone",
    "Autosend.Options.callerName",              "Caller Name",
    "Autosend.Options.callerLocation",          "Caller Location",
    "autosend.incident.location",               "Location",
    "autosend.incident.community",              "Community",
    "Autosend.Options.incidentTimes",           "CAD Times",
    "callCreatedTime",                          "Call Created Time",
    "callStatusTime",                           "Call Dispatched Time",
    "Autosend.Options.time",                    "Incident Creation Time",
    "Autosend.Options.unitStatus",              "Unit Status",
    "autosend.incident.logs.dateTime",          "Date/Time",
    "autosend.unit.history.unit",               "Unit",
    "autosend.unit.history.unitStatus",         "Status",
    "autosend.unit.history.unitLocation",       "Unit Location/Remarks",
    "Autosend.Options.unitInformation",         "Unit Information",
    "autosend.unit.history.unit",               "Unit",
    "autosend.unit.information.unitOrg",        "Org",
    "autosend.unit.information.name",           "Name",
    "autosend.unit.information.area",           "Area",
    "autosend.unit.information.types",          "Types",
    "Autosend.Options.caseNumbers",             "Case Numbers",
    "Autosend.Options.log",                     "Incident Log",
    "Autosend.Options.respondingUnits",         "Responding Units",
    "Autosend.Options.narratives",              "Remarks/Narratives",
    "Autosend.Options.notices",                 "Notices"
  );
}
