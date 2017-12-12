package net.anei.cadpage.parsers.dispatch;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.HtmlDecoder;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class DispatchA5Parser extends FieldProgramParser {
  
  private CodeTable callCodes;
  
  public DispatchA5Parser(String defCity, String defState) {
    this(null, null, defCity, defState);
  }
  
  public DispatchA5Parser(Properties cityCodes, String defCity, String defState) {
    this(cityCodes, null, defCity, defState);
  }
  
  public DispatchA5Parser(Properties cityCodes, CodeTable callCodes, String defCity, String defState) {
    super(cityCodes, defCity, defState,
           "( SELECT/2 Incident_Type:CALL2! " + 
                      "Priority:PRI? " + 
                      "Incident_Number:ID! " + 
                      "Incident_Location:ADDRCITY! " + 
                      "Common_Name:PLACE! " + 
                      "Located_Between:X! " + 
                      "Call_Date_/_Time:SKIP! " + 
                      "Dispatch:DATETIME! " + 
                      "Caller:NAME! " +
                      "Nature_of_Call:INFO! " +
                      "Narrative:EMPTY! NARRATIVE+ " +
                      "Hazards:INFO_KEY! " +
                      "Lat/Lon:GPS! " + 
                      "Map:SKIP! " +
                      "Times:EMPTY! TIMES2+ " +
                      "Units_Assigned:UNIT! " +
                      "Radio_Channel:CH! " +
           "| Incident_Number:ID! ORI:UNIT/S! Station:SRC! " +
             "Incident_Type:CALL1! Priority:PRI! " +
             "Incident_Location:ADDR! Venue:CITY! " +
             "Located_Between:X? Cross_Street:X? Common_Name:PLACE? " +
             "Google_Maps:SKIP " + 
             "Call_Time:TIME! Date:DATE! " +
             "Dispatch:TIMES1! En-route:TIMES1! On-scene:TIMES1! Depart_1:TIMES1! " +
             "Arrive_2:TIMES1! Depart_2:TIMES1! In-statn:TIMES1! Cleared:TIMES1? " +
             "Area:MAP! Section:MAP! Beat:SKIP! Map:SKIP? " +
             "Grid:SKIP! Quadrant:MAP! District:MAP! " +
             "Phone_Number:PHONE! Call_Source:SKIP! " +
             "Caller:NAME? " +
             "Units_sent:UNIT? " +
             "Nature_of_Call:INFO " + 
           ")");
    this.callCodes = callCodes;
  }

  private HtmlDecoder decoder = null;
  
  private String times;

  private static final Pattern MISSING_BRK_PTN = Pattern.compile(" (?=Priority:|Common Name:)");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    times = "";
    
    if (body.startsWith("<")) {
      if (decoder == null) decoder = new HtmlDecoder();
      body = MISSING_BRK_PTN.matcher(body).replaceAll("<div>");
      String[] flds = decoder.parseHtml(body);
      if (flds == null) return false;
      setSelectValue("2");
      if (!parseFields(flds, data)) return false;
    }

    else {
      setSelectValue("1");
      if (!super.parseHtmlMsg(subject, body, data)) return false;
    }
    
    // Clean up any duplicates in unit field
    StringBuilder sb = new StringBuilder();
    Set<String> unitSet = new HashSet<String>();
    for (String unit : data.strUnit.split(" +")) {
      if (unitSet.add(unit)) {
        if (sb.length() > 0) sb.append(' ');
        sb.append(unit);
      }
    }
    data.strUnit = sb.toString();

    // If run report, append Times info
    if (data.msgType == MsgType.RUN_REPORT) {
      data.strSupp = append(times, "\n", data.strSupp);
    }

    return true;
  }
    

  public static final String SUBJECT_SIGNATURE = "Automatic R&R Notification";
  public static final Pattern RUN_REPORT_MARKER = Pattern.compile("[ *]+FINAL REPORT[ *]+\n+\\s*");
  
  private static final Pattern TERMINATOR_PTN = Pattern.compile("\n(?:Additional Info|Address Checks|Additional Inc#s:|Narrative|The Call Taker is)");
  private static final Pattern KEYWORD_TRAIL_PTN = Pattern.compile("[ \\.]+:|(?: \\.){2,}(?=\n)");
  private static final Pattern KEYWORD_DASH_PTN = Pattern.compile("(Call Time|Date|Dispatch|En-route|On-scene|Depart [12]|Arrive [12]|In-statn|Cleared) *-");
  private static final Pattern LINE_PTN = Pattern.compile("(.*)(?:\n|$)");
  private static final Pattern TRIM_TRAIL_BLANKS = Pattern.compile(" +$");
  private static final Pattern TRIM_EXTRA_INFO = Pattern.compile("(?:\nAddress Checks *)?(?:\nAdditional Inc#s: *)?$");
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith(SUBJECT_SIGNATURE)) return false;
    
    Matcher match = RUN_REPORT_MARKER.matcher(body);
    if (match.lookingAt()) {
      data.msgType = MsgType.RUN_REPORT;
      body = body.substring(match.end());
    }
    
    match = TERMINATOR_PTN.matcher(body);
    if (!match.find()) return false;
    String extra = body.substring(match.start()+1);
    body = body.substring(0,match.start()).trim();
    
    body = body.replace('\n', ' ');
    body = KEYWORD_TRAIL_PTN.matcher(body).replaceAll(":");
    body = KEYWORD_DASH_PTN.matcher(body).replaceAll("$1:");
    body = body.replaceAll("  +", " ");
    
    if (!super.parseMsg(body, data)) return false;
    
    // Add additional information from trailing data
    boolean display = false;
    boolean trimLead = false;
    match = LINE_PTN.matcher(extra);
    while (match.find()) {
      String line = match.group(1);
      boolean skip = false;
      if (line.startsWith("Additional Info")) {
        display = true;
        trimLead = true;
        skip = true;
      } else if (line.startsWith("Address Checks")) {
        display = true;
        trimLead = false;
      } else if (line.startsWith("Narrative") || line.startsWith("The Call Taker is")) {
        display = false;
        trimLead = false;
      }
      if (display && !skip && line.length() > 0) {
        if (trimLead) line = line.trim();
        else line = TRIM_TRAIL_BLANKS.matcher(line).replaceFirst("");
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
    
    // Trim off any Empty titles from the extra information
    data.strSupp = TRIM_EXTRA_INFO.matcher(data.strSupp).replaceFirst("");
    
    return true;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL1")) return new MyCallField(CALL_CODE1_PTN);
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("TIMES1")) return new MyTimes1Field();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("PHONE")) return new MyPhoneField();
    if (name.equals("UNIT")) return new MyUnitField();
    
    if (name.equals("CALL2")) return new MyCallField(CALL_CODE2_PTN);
    if (name.equals("NARRATIVE")) return new MyNarrativeField();
    if (name.equals("INFO_KEY")) return new MyInfoKeyField();
    if (name.equals("TIMES2")) return new MyTimes2Field(); 
    return super.getField(name);
  }
  
  private static final Pattern CALL_CODE1_PTN = Pattern.compile("([^ ]+) ([EF] .*)");
  private static final Pattern CALL_CODE2_PTN = Pattern.compile("(\\d{1,2}[A-Z]\\d{1,2}[A-Z]?) : +(.*)");
  
  private class MyCallField extends CallField {
    
    private Pattern callCodePtn;
    
    public MyCallField(Pattern callCodePtn) {
      this.callCodePtn = callCodePtn;
    }
    
    @Override public void parse(String field, Data data) {
      Matcher match = callCodePtn.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
        String call = null;
        if (callCodes != null) call = callCodes.getCodeDescription(data.strCode);
        if (call != null) field = call;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      field = stripFieldStart(field, "and ");
      field = stripFieldEnd(field, " and");
      if (field.equals("and")) field = "";
      super.parse(field, data);
    }
  }
  
  private class MyTimes1Field extends InfoField {

    @Override
    public void parse(String field, Data data) {
      times = append(times, "\n", getRelativeField(0));
    }
  }
  
  private static final Pattern MAP_TRAIL_PTN = Pattern.compile("[ +]+$");
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      field = MAP_TRAIL_PTN.matcher(field).replaceFirst("");
      data.strMap = append(field, "/", data.strMap);
    }
  }
  
  private class MyPhoneField extends PhoneField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("(000) 000-0000")) return;
      super.parse(field, data);
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      field = field.replace(", ", " ").replace(',', ' ');
      data.strUnit = append(data.strUnit, " ", field);
    }
  }
  
  private static final Pattern NARRATIVE_JUNK_PTN = Pattern.compile("\\*{3}\\d\\d?/\\d\\d?/\\d{4}\\*{3}|\\d\\d:\\d\\d:\\d\\d|[A-Z]+\\\\[a-z]+|-");
  
  private class MyNarrativeField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (NARRATIVE_JUNK_PTN.matcher(field).matches()) return;
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  private class MyInfoKeyField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      data.strSupp = append(data.strSupp, "\n", getRelativeField(0));
    }
  }

  private static final Pattern TIMES2_TITLE_PTN = Pattern.compile("[A-Z]+: [A-Z ]+");
  private class MyTimes2Field extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      String delim = "\n";
      if (TIMES2_TITLE_PTN.matcher(field).matches()) {
        delim = "\n\n";
      }
      
      else if (field.startsWith("Unit:")) {
        data.strUnit = append(data.strUnit, " ", field.substring(5).trim());
      }
      
      else if (field.startsWith("Cleared at:")) {
        data.msgType = MsgType.RUN_REPORT;
      }
      
      times = append(times, delim, field);
    }
  }
}
