package net.anei.cadpage.parsers.MD;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDSaintMarysCountyCParser extends FieldProgramParser {

  public MDSaintMarysCountyCParser() {
    super("SAINT MARYS COUNTY", "MD",
          "DATETIME CALL UNIT ADDRCITY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "DoNotReply@stmaryscountymd.gov";
  }

  private Set<String> infoLineSet = null;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Page")) return false;
    infoLineSet = new HashSet<String>();
    try {
      return parseFields(body.split("\n"), data);
    } finally {
      infoLineSet = null;
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_CODE_PTN = Pattern.compile("Dispatch Code: *(\\S+)\\b.*");
  private static final Pattern INFO_PRI_PTN = Pattern.compile("Response: *([A-Z]).*");
  private static final Pattern INFO_ANS_BRK_PTN = Pattern.compile(" *(\\d+\\.) {3,}");
  private static final Pattern INFO_HDR_PTN = Pattern.compile("^(?:CC Text|Caller Statement): *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      Matcher match = INFO_CODE_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        return;
      }

      if ((match = INFO_PRI_PTN.matcher(field)).matches()) {
        data.strPriority = match.group(1);
        return;
      }

      if (field.startsWith("ANSWERS:")) {
        field = field.substring(8).trim();
        match = INFO_ANS_BRK_PTN.matcher(field);
        if (match.find()) {
          StringBuilder sb = new StringBuilder();
          do {
            match.appendReplacement(sb, "\n$1");
          } while (match.find());
          match.appendTail(sb);
          data.strSupp = append(data.strSupp, "\n", sb.toString().trim());
          return;
        }
      }

      field = INFO_HDR_PTN.matcher(field).replaceFirst("");
      if (infoLineSet.add(field)) data.strSupp = append(data.strSupp, "\n", field);
    }

    @Override
    public String getFieldNames() {
      return "INFO CODE PRI";
    }
  }
}
