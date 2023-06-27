package net.anei.cadpage.parsers.dispatch;

import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DispatchBCParser extends DispatchA3Parser {

  private HtmlDecoder decoder = new HtmlDecoder();
  private AuxA33Parser auxA33Parser;
  private boolean useAuxParser;

  public DispatchBCParser(String defCity, String defState) {
    this(null, defCity, defState, 0);
  }

  public DispatchBCParser(String defCity, String defState, int flags) {
    this(null, defCity, defState, flags);
  }

  public DispatchBCParser(String[] cityList, String defCity, String defState, int flags) {
    super(defCity, defState,
          "( Address:EMPTY! ADDR! Event_Number:EMPTY! ID! Category:EMPTY! CALL! COPY END " +
          "| Event_No:EMPTY! ID! Status:EMPTY! Disposition:EMPTY! Category:EMPTY! CALL " +
            "( Complaint_Numbers%EMPTY! Unit:EMPTY! UNIT Reporting_DSN:EMPTY Agency:EMPTY SRC | ) " +
            "Address:EMPTY! ADDR Precinct:EMPTY! MAP Sector:EMPTY! MAP/D GEO:EMPTY! MAP/D ESZ:EMPTY! MAP/D Ward:EMPTY! MAP/D Intersection:EMPTY! X " +
            "Date_/_Time%EMPTY Open:EMPTY! DATETIME1? Law_Enf.:EMPTY! SRC Dispatch:EMPTY! DATETIME1? Fire:EMPTY! SRC Enroute:EMPTY! DATETIME2? EMS:EMPTY! SRC Arrival:EMPTY! DATETIME2? " +
            "Source:EMPTY! Departure:EMPTY! DATETIME3? Closed:EMPTY! DATETIME3? " +
            "( Person(s)_Involved%EMPTY! Name_Address_Phone%EMPTY! NAME_PHONE Business%EMPTY! | ) " +
            "Incident_Notes:EMPTY INFO+ Event_Log%EMPTY )");

    auxA33Parser = new AuxA33Parser(cityList, defCity, defState, flags);
  }

  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Event Number *(\\d{4}-\\d+[A-Z]*\\d*)\\n");
  private static final Pattern BR_TAG = Pattern.compile("</?br/?>", Pattern.CASE_INSENSITIVE);

  private String times;

  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {

    useAuxParser = false;

    // Check for run report
    Matcher match = RUN_REPORT_PTN.matcher(body);
    if (match.lookingAt()) {
      setFieldList("CALL ID ADDR APT CITY ST INFO");
      data.strCall = subject;
      data.strCallId = match.group(1);

      boolean first = true;
      boolean parseData = true;
      for (String line : body.substring(match.end()).trim().split("\n")) {
        line = line.trim();
        if (first && line.equals("Log Date / TimeActivity Note")) {
          data.msgType = MsgType.RUN_REPORT;
          parseData = false;
        }
        first = false;
        if (parseData) {
          if (line.startsWith("Category")) {
            data.strCall = line.substring(8).trim();
            continue;
          }
          if (line.startsWith("Sub Category")) {
            data.strCall = append(data.strCall, " / ", line.substring(12).trim());
            continue;
          }
          if (line.startsWith("Street")) {
            parseAddress(line.substring(6).trim(), data);
            continue;
          }
          if (line.startsWith("City")) {
            data.strCity = line.substring(4).trim();
            continue;
          }
          if (line.startsWith("State")) {
            data.strState = line.substring(5).trim();
            continue;
          }
          if (line.startsWith("Zip Code")) {
            if (data.strCity.length() == 0) data.strCity = line.substring(8).trim();
            continue;
          }
          parseData = false;
        }
        if (line.contains("Event Closed Disposition")) data.msgType = MsgType.RUN_REPORT;
        data.strSupp = append(data.strSupp, "\n", line);
      }
      return true;
    }

    // Lately a lot of agencies have been mixing the standard HTML format we usually process with
    // a non-html version processed by DispatchA33Parser.  As a result, we split out non-html looking
    // messages and pass them to an auxiliary DispatchA33Parser subclass.
    if (!isHtmlMsg(body)) {
      useAuxParser = true;
      return auxA33Parser.parseThisMsg(subject,  body, data);
    }

    // Inappropriate <br> tags get inserted in the wierdest places, so we
    // will just get rid of them
    body = BR_TAG.matcher(body).replaceAll("");
    String[] flds = decoder.parseHtml(body);
    if (flds == null) return false;
    times = "";
    if (! parseFields(flds, data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n", data.strSupp);
   return true;
  }

  @Override
  public String getProgram() {
    if (useAuxParser) return auxA33Parser.getProgram();
    return super.getProgram();
  }

  private static final Pattern HTML_PTN = Pattern.compile("<html>", Pattern.CASE_INSENSITIVE);

  /**
   * Determine if message is legitimate HTML message
   * @param body message body
   * @return true if this looks like an HTML message
   */
  protected boolean isHtmlMsg(String body) {
    return HTML_PTN.matcher(body).lookingAt();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new BaseCallField();
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("DATETIME1")) return new BaseDateTimeField(1);
    if (name.equals("DATETIME2")) return new BaseDateTimeField(2);
    if (name.equals("DATETIME3")) return new BaseDateTimeField(3);
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("SRC")) return new BaseSourceField();
    if (name.equals("NAME_PHONE")) return new BaseNamePhoneField();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("COPY")) return new SkipField("©.*", true);
    return super.getField(name);
  }

  private static final Pattern DIGIT_PATTERN = Pattern.compile("([A-Z0-9]*\\d[A-Z0-9]*) / (.*)");
  private class BaseCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = DIGIT_PATTERN.matcher(field);
      if (m.matches()) {
        data.strCode = m.group(1);
        data.strCall = m.group(2).trim();
      }
      else
        data.strCall = field.trim();
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private static final Pattern STATE_CODE_PTN = Pattern.compile("[A-Z]{2}|");
  protected class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (STATE_CODE_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private class BaseDateTimeField extends DateTimeField {
    private int type;

    public BaseDateTimeField(int type) {
      super("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d|", true);
      this.type = type;
    }

    @Override
    public void parse(String field, Data data) {
      String title = getRelativeField(-1);
      String line = append(title, "   ", field);
      times = append(times, "\n", line);
      if (field.length() == 0) return;
      if (type == 1) {
        super.parse(field, data);
      } else if (type == 3) {
        data.msgType = MsgType.RUN_REPORT;
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " INFO?";
    }
  }

  private static final Pattern CROSS_DIR_PTN = Pattern.compile("[NSEW]/?B");
  protected class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (CROSS_DIR_PTN.matcher(field).matches()) {
        data.strAddress = append(data.strAddress, " ", field.replace("/", ""));
      }
      else {
        super.parse(field, data);
      }
    }
  }

  private class BaseSourceField extends SourceField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', '_');
      data.strSource = append(data.strSource, ",", field);
    }
  }

  private static final Pattern PHONE_PATTERN
    = Pattern.compile("(.*?)((?:\\(\\d{3}\\) ?)?\\d{3}\\-\\d{4}.*)");
  private class BaseNamePhoneField extends NameField {
    @Override
    public void parse(String field, Data data) {
      Matcher m = PHONE_PATTERN.matcher(field);
      if (m.matches()) {
        data.strName = m.group(1).trim();
        data.strPhone = m.group(2).trim();
      }
      else {
        data.strName = field.trim();
      }
    }

    @Override
    public String getFieldNames() {
      return "NAME PHONE";
    }
  }

  private static final Pattern INFO_DASHES_PTN = Pattern.compile("-*");
  private class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_DASHES_PTN.matcher(field).matches()) return;
      data.strSupp = append(data.strSupp, " ", field);
    }
  }

  private class AuxA33Parser extends DispatchA33Parser {

    public AuxA33Parser(String[] cityList, String defCity, String defState, int flags) {
      super(cityList, defCity, defState, flags);
    }

    public boolean parseThisMsg(String subject, String body, Data data) {
      return super.parseMsg(subject, body, data);
    }

    @Override
    protected String trimCrossField(String field) {
      return DispatchBCParser.this.trimCrossField(field);
    }
  }

  protected String trimCrossField(String field) {
    return field;
  }
}
