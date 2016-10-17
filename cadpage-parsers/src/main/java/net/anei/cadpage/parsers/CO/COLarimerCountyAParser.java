package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class COLarimerCountyAParser extends FieldProgramParser {
  
  private static final Pattern TEXT_MARKER_PTN = Pattern.compile("\\b([A-Z]+:?) +\\(([A-Z]{3,5})\\) *");
  private static final Pattern RUN_REPORT_PTN = Pattern.compile(".{30} *(?:- )?([A-Z0-9]+) +\\(TIMES\\) +((?:Rec'd|Received).*)// *Ambu Run ?#[- ]+(\\d+)\\b.*");
  
  private static final Pattern SEPARATOR = Pattern.compile(" *// *");

  public COLarimerCountyAParser() {
    super("LARIMER COUNTY", "CO",
           "CALL ADDR APT! Radio:CH! PLACE MAP UNIT");
  }
  
  public String getFilter() {
    return "@notifyall.com,2082524629,4702193642,CommPaging@ci.loveland.co.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    // Text pages are split into two different messages, with different formats
    // They have a different header that tips us off that a second message should
    // be expected.  Which is duplicated in the second message which must be
    // identified and removed.
    Matcher match = TEXT_MARKER_PTN.matcher(body);
    if (match.find()) {
      String marker = match.group(1);
      String tSubject = match.group(2);
      if (match.start() == 0) {
        body = body.substring(match.end());
        marker = ' ' + marker + ' ';
        int pt = body.indexOf(marker);
        if (pt >= 0) {
          String part1 = body.substring(0,pt);
          String part2 = body.substring(pt+marker.length());
          body = part1 + ' ' + part2;
        } else {
          data.expectMore = true;
        }
        subject = tSubject;
      } else {
        String part1 = body.substring(0,match.start());
        String part2 = body.substring(match.end());
        if (part1.endsWith(" ")) {
          part1 = part1.trim();
          marker = marker + ' ';
          if (part1.startsWith(marker)) {
            part1 = part1.substring(marker.length()).trim();
            body = part2 + ' ' + part1;
            subject = tSubject;
          }
        }
      }
    }

    // Resume normal processing
    Parser p = new Parser(subject);
    data.strSource = p.getOptional('|');
    
    // Check for run report
    match = RUN_REPORT_PTN.matcher(body);
    if (match.matches()) {
      data.msgType = MsgType.RUN_REPORT;
      setFieldList("UNIT INFO ID");
      data.strUnit = match.group(1);
      data.strSupp = match.group(2).replace("//", "\n").trim();
      data.strCallId = match.group(3);
      return true;
    }
    
    if (!p.get().equals("CFS")) return false;
    if (data.strSource.toLowerCase().startsWith("notifyall")) data.strSource = "";

    String[] flds = SEPARATOR.split(body);
    if (!parseFields(flds, data)) return false;
    if (data.strMap.equals("NOT FOUND")) data.strMap = "";
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new AptField("Apt/Lot *(.*)", true);
    return super.getField(name);
  }
}
