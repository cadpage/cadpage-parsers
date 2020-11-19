package net.anei.cadpage.parsers.NJ;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class NJBergenCountyCParser extends FieldProgramParser {

  public NJBergenCountyCParser() {
    super("BERGEN COUNTY", "NJ",
          "CAD#:ID! Date:DATE! Time:TIME! Address:ADDR! Type_of_Fire_Response:CALL! Type_of_Service:CITY! Notes:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "srpd2srfd@saddleriver.org,DoNotReply@lawsoftweb.onmicrosoft.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Fire Page")) return false;

    return super.parseFields(body.split("\\n+"), data);
  }

  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{2}-\\d{5}", true);
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("CITY")) return new MyCityField();
    return super.getField(name);
  }

  private static final Pattern SHORT_TIME_PTN = Pattern.compile("(\\d\\d?:\\d\\d) ([AP]M)");
  private class MyTimeField extends TimeField {
    public MyTimeField() {
      super(TIME_FMT, true);
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = SHORT_TIME_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1)+":00 " + match.group(2);
      }
      super.parse(field, data);
    }

  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "In ");
      super.parse(field, data);
    }
  }
}
