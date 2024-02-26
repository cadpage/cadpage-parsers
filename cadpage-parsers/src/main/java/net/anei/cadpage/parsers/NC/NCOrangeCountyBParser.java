package net.anei.cadpage.parsers.NC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class NCOrangeCountyBParser extends FieldProgramParser {

  private static final Pattern SUBJECT_PTN = Pattern.compile("([-0-9]+) *[-\\(]([A-Z ]+)\\)?");

  public NCOrangeCountyBParser() {
    super("ORANGE COUNTY", "NC",
           "URL EMPTY? ( TG:CH! | CH! | CODE! ) EMPTY? ADDRCITYST EMPTY? ( District:SRC! | APT? CALL! INFO+ OCPC:SKIP SUBD:PLACE INFO/N+ District:SRC )");
  }

  @Override
  public String getFilter() {
    return "ptpage@pgtrksrv01.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    do {
      if (subject.equals("CHFD")) {
        break;
      }
      if (subject.equals("EMS")) {
        data.strCall = subject;
        break;
      }
      Matcher match = SUBJECT_PTN.matcher(subject);
      if (match.matches()) {
        data.strCallId = match.group(1);
        data.strCall = match.group(2);
        break;
      }
    } while (false);
    if (!parseFields(body.split("\n"), 4, data)) return false;
    if (!data.strCode.isEmpty()) {
      String call = CALL_CODES.getCodeDescription(data.strCode);
      if (call !=  null) data.strCall = call;
    }
    return true;
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("URL")) return new InfoUrlField("https?://[A-Za-z0-9]+\\.net/.*", true);
    if (name.equals("CH")) return new ChannelField("OPS.*", true);
    if (name.equals("CODE")) return new CodeField("\\d{1,3}[A-E]\\d{1,2}[A-Z]?", true);
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("^(?:BLDG|APT): *");
  private class MyAptField extends AptField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      boolean first = true;
      for (String part : field.split(" *, *")) {
        Matcher match = APT_PTN.matcher(part);
        if (match.find()) {
          data.strApt = append(data.strApt, "-", part.substring(match.end()));
        } else {
          if (first) return false;
          data.strApt = append(data.strApt, ", ", part);
        }
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern CALL_CODE_PTN = Pattern.compile("^(\\d+[A-Z]\\d+[A-Z]?) +");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CALL_CODE_PTN.matcher(field);
      if (match.find()) {
        data.strCode = match.group(1);
        field = field.substring(match.end());
      }
      data.strCall = append(data.strCall, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }
  
  private CodeTable CALL_CODES = new StandardCodeTable();
}
