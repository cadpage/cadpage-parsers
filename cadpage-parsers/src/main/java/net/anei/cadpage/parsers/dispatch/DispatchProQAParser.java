package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Parser for ProQA Dispatch software
 */
public class DispatchProQAParser extends FieldProgramParser {
  
  private final Pattern delimPattern;
  
  protected DispatchProQAParser(String defCity, String defState, String program) {
    this(defCity, defState, program, false);
  }
  
  protected DispatchProQAParser(Properties cityCodes, String defCity, String defState, String program) {
    this(cityCodes, defCity, defState, program, false);
  }
  
  protected DispatchProQAParser(String[] cityList, String defCity, String defState, String program) {
    this(cityList, defCity, defState, program, false);
  }

  protected DispatchProQAParser(String defCity, String defState, String program, boolean singleSlashDelim) {
    super(defCity, defState, fixProgram(program));
    delimPattern = Pattern.compile(singleSlashDelim ? "/" : "/+");
  }
  
  protected DispatchProQAParser(Properties cityCodes, String defCity, String defState, String program, boolean singleSlashDelim) {
    super(cityCodes, defCity, defState, fixProgram(program));
    delimPattern = Pattern.compile(singleSlashDelim ? "/" : "/+");
  }
  
  protected DispatchProQAParser(String[] cityList, String defCity, String defState, String program, boolean singleSlashDelim) {
    super(cityList, defCity, defState, fixProgram(program));
    delimPattern = Pattern.compile(singleSlashDelim ? "/" : "/+");
  }
  
  private static final Pattern ID_PTN = Pattern.compile("\\bID!");
  
  private static String fixProgram(String program) {
    // There is always a required ID field.  If one wasn't included 
    // add it as the first field
    if (!ID_PTN.matcher(program).find()) {
      program = "ID! " + program;
    }
    return program;
  }
  
  private static final Pattern MARKER = Pattern.compile("(?:- part 1 of 1[\\s/]+)?(?:RC: *(?:\\d*(?:-[A-Z])?/)?)? *");
  private static final Pattern UNASSIGNED_MARKER = Pattern.compile("Job# *[^ ]* *\\(Run# (\\d+)\\) at [0-9:]+ was unassigned\\.");
  private static final Pattern RUN_REPORT_MARKER1 = Pattern.compile("(?:(?:Job# *)?\\d+(?:-[A-Z])?/ *)?Run# *(\\d+) */ *(?:(was Canceled: .*?)/)? *((?:CALL:)?\\d\\d:\\d\\d/ ?(?:DISP:)?\\d\\d:\\d\\d/ ?.*)");
  private static final Pattern RUN_REPORT_MARKER2 = Pattern.compile("Inc# *[^ ]* */ *Run# *(\\d+) was (?:cancelled|completed) */ *([A-Za-z0-9]+) */ *(.*)");
  private static final Pattern GEN_ALERT_PTN1 = Pattern.compile("Go to post .*");
  private static final Pattern GEN_ALERT_PTN2 = Pattern.compile("Job# *[^ ]* *\\(Run# (\\d+)\\) *(.*)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = MARKER.matcher(body);
    if (!match.lookingAt()) return false;
    body = body.substring(match.end());

    match = UNASSIGNED_MARKER.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = "was unassigned";
      return true;
    }
  
    match = RUN_REPORT_MARKER1.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = append(getOptGroup(match.group(2)), "\n", 
                            match.group(3).replace('/', '\n').trim());
      return true;
    }
    
    match = RUN_REPORT_MARKER2.matcher(body);
    if (match.matches()) {
      setFieldList("ID UNIT INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
      for (String time : match.group(3).split("/")) {
        data.strSupp = append(data.strSupp, "\n", time.trim());
      }
      return true;
    }
    
    if (GEN_ALERT_PTN1.matcher(body).matches()) {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }
        
    match = GEN_ALERT_PTN2.matcher(body);
    if (match.matches()) {
      setFieldList("ID INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strCallId = match.group(1);
      data.strSupp = match.group(2);
      return true;
    }

    // Everything else is variable
    body = body.replace("ProQA comments:", "/");
    String[] lines = delimPattern.split(body);
    return parseFields(lines, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("(?:Run|Inc)# *(\\d+)", true);
    if (name.equals("INFO")) return new BaseInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CROSS_PATTERN = Pattern.compile("(?:.*?X=|XST\\b *)(.*)");
  private static final Pattern UNKNOWN_PATTERN = Pattern.compile("(?i).*<unknown>.*");
  protected class BaseInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("<PROQA_DET>")) return;
      if (field.equals("<PROQA_SCRIPT>")) return;
      Matcher m = UNKNOWN_PATTERN.matcher(field);
      if (m.matches())
        return;
      m = CROSS_PATTERN.matcher(field);
      if (m.matches()) {
        String[] cross = m.group(1).split("&");
        for (int i=0; i<cross.length; i++)
          addCross(cross[i].trim(), data);
        return;
      }
      super.parse(field, data);
    }
  
    private void addCross(String field, Data data) {
      if (!field.contains("Unknown Street"))
        data.strCross = append(data.strCross, "/", field);
    }
    
    @Override
    public String getFieldNames() {
      return append(super.getFieldNames(), " ", "X");
    }
  }

  /**
   * General call to clean up ProQA style data when it is handled by other CAD vendors
   * @param forceCall true for force parsing call information from parsed data
   * @param field data field to be parsed
   * @param data data object returning parsed information
   */
  public static void parseProQAData(boolean forceCall, String field, Data data) {
    int lastCol = 0;
    Matcher match = PROQA_TERM_PTN.matcher(field);
    while (match.find()) {
      String term = match.group(1);
      if (term == null) term = match.group(2);
      if (term != null) {
        if (forceCall || data.strCall.length() == 0) data.strCall = term.trim();
      }
      else if ((term = match.group(3)) != null) {
        if (data.strCode.length() == 0) data.strCode = term;
      }
      
      data.strSupp = append(data.strSupp, "\n", field.substring(lastCol, match.start()).trim());
      lastCol = match.end();
    }
    String last = field.substring(lastCol).trim();
    match = PROQA_TRUNC_PTN.matcher(last);
    if (match.find()) last = last.substring(0,match.start()).trim();
    else if ("Disp Dispatch Code:".startsWith(last)) last = "";
    data.strSupp = append(data.strSupp, "\n", last);
  }
  private static final Pattern PROQA_TERM_PTN = Pattern.compile("\\bProblem: (.*?)\\.  Patient Info:|\\bCaller Statement: (.*?)\\.|\\b(?:ProQA d|Reconfigured d|D)ispatch (?:Code|code|Level): +(\\d[^ ;]+);?|\\bProQA (?:(?:Medical|Fire|Police) )?(?:Dispatch Message Sent|Key Questions have been completed|Questionnaire Completed);| New ProQA (?:(?:Medical|Fire|Police) )?Case Number has been assigned;|Dispatch (?:Code|Level): --: -\\d+|\\bE911 Info -.*|Chief Complaint Number: \\d+;|\\*\\* Case number \\d+ has been assigned for \\d+:CNTY \\*\\*|>+ by: [ A-Z]+ on terminal: .*|\\bResponder script:|\\bNew ProQA Case Number has been assigned: -\\d+");
  private static final Pattern PROQA_TRUNC_PTN = Pattern.compile("(?:\\bNew )?ProQA[ A-Za-z]*$"); 

}
