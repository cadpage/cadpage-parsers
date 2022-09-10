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

  public DispatchA63Parser(Properties cityCodes, String[] cityList, String defCity, String defState) {
    super(cityList, defCity, defState,
          "( Juris:SRC! CFS:CALL! ( Location:ADDRCITYST! Call#:ID_DATE_TIME " + 
                                 "| Request#:ID! Report_Date/Time:DATETIME! Reporting_Period:SKIP! Location:ADDR! Notify_Type:SKIP Call#:SKIP Login_User:SKIP " +
                                 ") " +
          "| CFS:CALL! Location:ADDR! Call#:ID! Units_Dispatched:UNIT! Stations_Dispatched:SRC? Report_Date/Time:DATETIME! ) Comments:INFO/N+");
    this.cityCodes = cityCodes;
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
    return parseFields(body.split("\n"), data);
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

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}\\-\\d{6}", true);
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    if (name.equals("UNIT")) return new BaseUnitField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("ID_DATE_TIME")) return new BaseIdDateTimeField();
    return super.getField(name);
  }

  private static final SimpleDateFormat DATE_TIME_FMT
    = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      setDateTime(DATE_TIME_FMT, field, data);
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
      Matcher match = ADDR_STATE_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
        zip = match.group(3);
      }
      parseAddress(StartType.START_ADDR, FLAG_ANCHOR_END, field, data);
      if (data.strCity.isEmpty() && zip != null) data.strCity = zip;
    }
    
    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private class BaseUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      data.strUnit = field.replace(" ", "");
    }
  }
  
  private static final Pattern ID_DATE_TIME_PTN = Pattern.compile("(\\S+) +\\{(\\d\\d?/\\d\\d?/\\d{4} +\\d\\d?:\\d\\d:\\d\\d [AP]M)\\}");
  private class BaseIdDateTimeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ID_DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      setDateTime(DATE_TIME_FMT, match.group(2), data);
    }

    @Override
    public String getFieldNames() {
      return "ID DATE TIME";
    }
  }
}
