package net.anei.cadpage.parsers.MD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MDFrederickCountyBParser extends FieldProgramParser {

  public MDFrederickCountyBParser() {
    super("FREDERICK COUNTY", "MD",
          "EMPTY! Inc_#:ID! Type:CALL! Pri:PRI! Loc:PLACE! Desc:INFO! Addr:ADDR! Flr:APT! Apt:APT! XSt:X! Rmks:INFO! INFO/N+ Units:UNIT! Time:TIMEDATE! DASHES END");
  }

  @Override
  public String getFilter() {
    return "ECC@frederickcountymd.gov";
  }

  private static final Pattern DELIM = Pattern.compile("[|\n]");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Dispatch Notification for Incident ")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIMEDATE")) return new MyTimeDateField();
    if (name.equals("DASHES")) return new SkipField("-{5,}", true);
    return super.getField(name);
  }

  private static final Pattern TIME_DATE_PTN = Pattern.compile("(\\d\\d?:\\d\\d:\\d\\d [AP]M) (\\d\\d?/\\d\\d?/\\d{4})");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyTimeDateField extends TimeDateField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_DATE_PTN.matcher(field);
      if (!match.matches()) abort();
      setTime(TIME_FMT, match.group(1), data);
      data.strDate =  match.group(2);
    }
  }
}
