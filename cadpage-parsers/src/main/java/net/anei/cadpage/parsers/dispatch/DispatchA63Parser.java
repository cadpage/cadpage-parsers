package net.anei.cadpage.parsers.dispatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class DispatchA63Parser extends FieldProgramParser {

  private Properties cityCodes;

  public DispatchA63Parser(String defCity, String defState) {
    this(null, null, defCity, defState);
  }
  
  public DispatchA63Parser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, null, defCity, defState);
  }
  
  public DispatchA63Parser(String[] cityList, String defCity, String defState) {
    this(null, cityList, defCity, defState);
  }
  
  private boolean noCity;

  public DispatchA63Parser(Properties cityCodes, String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "( Juris:SRC! CFS:CALL! ( Location:ADDRCITYST! Call#:ID_DATE_TIME " + 
                                 "| Request#:ID! Report_Date/Time:DATETIME! Reporting_Period:SKIP! Location:ADDR! Notify_Type:SKIP Call#:SKIP Login_User:SKIP " +
                                 ") " +
          "| CFS:CALL! Location:ADDR! Call#:ID! Units_Dispatched:UNIT! Stations_Dispatched:SRC? Report_Date/Time:DATETIME! " +
          "| ID_DATE_TIME CODE_CALL_PRI ADDRCITYST Units:EMPTY! UNIT/C+ " +
          ") Comments:INFO/N+");

    this.cityCodes = cityCodes;
    noCity = cityCodes == null && cityList == null;
  }

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    if (body.startsWith("<html>")) {
      String[] flds = rebuildAlert(body);
      if (flds == null) return false;
      return parseFields(flds, data);
    } else {
      return super.parseHtmlMsg(subject, body, data);
    }
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = body.replace("\t", "");
    if (!body.contains("\n")) {
      return parseNoBreak(body, data);
    } else {
      return parseFields(body.split("\n"), data);
    }
  }

  private HtmlDecoder decoder = null;

  private String[] rebuildAlert(String body) {

    // If we haven't build an HTML decoder yet, do it now
    if (decoder == null) {
      decoder = new HtmlDecoder("tr");
    }

    // And use it to parse the html fields
    String[] flds = decoder.parseHtml(body);

    // Before we return this, we need to compress all of the fields in a row into one field
    List<String> result = new ArrayList<>();
    StringBuilder sb = null;
    for (String fld : flds) {
      if (fld.equals("<|tr|>")) {
        if (sb != null) return null;
        sb = new StringBuilder();
      }
      else if (fld.equals("<|/tr|>")) {
        if (sb == null) return null;
        result.add(sb.toString());
        sb = null;
      }
      else if (sb != null) {
        sb.append(fld);
      } else {
        result.add(fld);
      }
    }

    if (sb != null) result.add(sb.toString());

    return result.toArray(new String[0]);
  }
  
  private static final Pattern NO_BRK_PTN = 
      Pattern.compile("(\\S+ \\{\\d\\d?/\\d\\d?/\\d{4} \\d\\d:\\d\\d:\\d\\d(?: [AP]M)?\\}) ([^\\}]+\\}) (.*?) (Units:)(.*?) (Comments:) (.*)");

  private boolean parseNoBreak(String body, Data data) {
    Matcher match = NO_BRK_PTN.matcher(body);
    if (!match.matches()) return false;
    int fldCnt = match.groupCount();
    String[] flds = new String[fldCnt];
    for (int j = 0; j<fldCnt; j++) {
      flds[j] = match.group(j+1);
    }
    return parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}\\-\\d{6}", true);
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("ID_DATE_TIME")) return new BaseIdDateTimeField();
    if (name.equals("CODE_CALL_PRI")) return new BaseCodeCallPriorityField();
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(';');
      if (cityCodes != null) city = convertCodes(city.toUpperCase(), cityCodes);
      data.strCity = city;
      String apt = p.getLastOptional(',');
      if (apt.equals("BLDG") || apt.equals("LOT") || apt.equals("APT")) apt = "";
      super.parse(p.get(), data);
      data.strApt = append(data.strApt, "-", apt);
      return;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY";
    }
  }
  
  private static final Pattern ADDR_STATE_ZIP_PTN = Pattern.compile("(.*) ([A-Z]{2})(?: (\\d{5}))?");
  private class BaseAddressCityStateField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      if (!noCity) {
        Matcher match = ADDR_STATE_ZIP_PTN.matcher(field);
        if (match.matches()) {
          field = match.group(1).trim();
          data.strState = match.group(2);
          zip = match.group(3);
        }
      }
      int pt = field.lastIndexOf(',');
      String apt = "";
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
        if (apt.equals("BLDG") || apt.equals("LOT") || apt.equals("APT")) apt = "";
      }
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      if (data.strCity.isEmpty() && zip != null) data.strCity = zip;
      data.strApt = append(data.strApt, "-", apt);
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private static final Pattern UNIT_DELIM_PTN = Pattern.compile("[ ,]+");
  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = UNIT_DELIM_PTN.matcher(field).replaceAll(",");
    }
  }
  
  private static final Pattern ID_DATE_TIME_PTN = Pattern.compile("(\\S+) +\\{(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)\\}");
  private class BaseIdDateTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strDate = match.group(2);
      String time = match.group(3);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }

    @Override
    public String getFieldNames() {
      return "ID DATE TIME";
    }
  }
  
  private static final Pattern CODE_CALL_PRI_PTN = Pattern.compile("(\\S+) +- +(.*)\\{(\\d)\\}");
  
  private class BaseCodeCallPriorityField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PRI_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCode = match.group(1);
      data.strCall = match.group(2);
      data.strPriority = match.group(3);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL PRI";
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("\\{ *([A-Z0-9]+?) *\\}");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      // First pick out any unit designation in curly braces.
      // These are added to the unit field if they are not already present there
      int lastPt = 0;
      Matcher match = UNIT_PTN.matcher(field);
      while (match.find()) {
        String sUnit = match.group(1);
        lastPt = match.end();
        Pattern ptn = Pattern.compile("\\b" + sUnit + "\\b");
        if (!ptn.matcher(data.strUnit).find()) data.strUnit = append(data.strUnit, ",", sUnit);
      }

      // Ignore everything up to the last unit
      field = field.substring(lastPt).trim();
      field = stripFieldStart(field, ";");

      if (field.startsWith(",")) field = field.substring(1).trim();

      // Filter out stuff we aren't interested in
      if (field.contains("Units Recommended:")) return;
      if (field.contains("Toner Alert Instantiated")) return;
      int pt = field.indexOf("Update reviewed by dispatcher");
      if (pt >= 0) field = field.substring(0,pt).trim();

      // ANything else is information
      data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "UNIT? " + super.getFieldNames();
    }
  }
}
