/**
 *
 */
package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * @author ken
 *
 */
public class TXGillespieCountyParser extends FieldProgramParser {

  public TXGillespieCountyParser() {
    super("GILLESPIE COUNTY", "TX",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDRCITY! DATE:DATETIME! MAP:MAP! UNIT:UNIT! NATURE_OF_CALL:CALL/SDS! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "cadpage@gillespiecounty.org,no-reply@rr.com,gillespiecountycad@gcwebview.net";
  }

  private static final Pattern DELIM = Pattern.compile("\n+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("!LERMS!")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) +(\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
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

}
