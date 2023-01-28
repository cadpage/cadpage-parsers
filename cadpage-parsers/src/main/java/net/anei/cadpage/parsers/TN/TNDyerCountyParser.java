package net.anei.cadpage.parsers.TN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TNDyerCountyParser extends FieldProgramParser {


  public TNDyerCountyParser() {
    super("DYER COUNTY","TN",
          "DATE:DATE! TIME:TIME! EVENT_NUM:ID! ADDRESS:ADDR! EVENT_TYPE:CODE! REMARKS:INFO! INFO/N+ Incident_Type:CALL! Incident_Type:SKIP INTERSECTION:X END");
  }

  @Override
  public String getFilter() {
    return "alerts@tailorbuilt.app";
  }

  private static final Pattern DELIM = Pattern.compile("\n| (?=(?:TIME|EVENT NUM|Incident Type):)");
  private static final Pattern CALL_GUIDE_PTN =  Pattern.compile(".*?\\*+CALL GUIDE\\*+ *(.*)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("System Alert")) return false;
    int pt = body.indexOf("\n<img width=");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!parseFields(DELIM.split(body), data)) return false;
    Matcher match = CALL_GUIDE_PTN.matcher(data.strCall);
    if (match.matches()) data.strCall = match.group(1);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }

  private static final Pattern BY_PTN = Pattern.compile("\\bBY\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String adjustMapAddress(String addr) {
    addr = BY_PTN.matcher(addr).replaceAll("BYPASS");
    return super.adjustMapAddress(addr);
  }
}
