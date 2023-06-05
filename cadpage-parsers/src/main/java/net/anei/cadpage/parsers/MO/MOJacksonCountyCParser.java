package net.anei.cadpage.parsers.MO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOJacksonCountyCParser extends FieldProgramParser {

  public MOJacksonCountyCParser() {
    super("JACKSON COUNTY", "MO",
          "( Sent:DATETIME1! INFO/G! INFO/N+ " +
          "| Date:DATETIME2! ID! Type:CALL? Location:ADDR! CITY! ( Latitude:GPS1! Longitude:GPS2! | ) Mapgrid:MAP? Units_Responding:UNIT UNIT/C+ " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "firecomm@cityofls.net";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident")) return false;
    return parseFields(body.split("[,\n]"), data);
  }

  private static final DateFormat DATE_TIME1_FMT = new SimpleDateFormat("yyyy/MMM/dd HH:mm");

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME1")) return new DateTimeField("\\d{4}/[A-Z][a-z]{2}/\\d\\d \\d\\d:\\d\\d", DATE_TIME1_FMT, true);
    if (name.equals("DATETIME2")) return new MyDateTime2Field();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("#(\\d*)", true);
    if (name.equals("MAP")) return new MapField("\\d{3}[A-Z]", true);
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d-\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTime2Field extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2).replace('-', '/') + '/' + match.group(1);
      data.strTime = match.group(3);
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\S+) - (.*)");
  private class MyCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
}
