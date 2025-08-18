package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PAArmstrongCountyCParser extends FieldProgramParser {

  public PAArmstrongCountyCParser() {
    super("ARMSTRONG COUNTY", "PA",
          "CALL! CALL+ CALL_INFO_AND_RADIO_TAC:CH! ADDRESS:ADDRCITY! ADD'L_LOCALE_INFO:PLACE! PLACE! PLACE+? DATETIME! CFS_NUMBER:ID! " +
                "FIRE_QUAD:MAP! EMS_DISTRICT/L! INTERSECTION:X! X/L UNITS:UNIT! END");
  }

  private static final Pattern DELIM = Pattern.compile(" *(?:(?<!\\b\\d?\\d)/|/(?!\\d)) *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) {
      if (!body.startsWith("Dispatch:")) return false;
      body = body.substring(9).trim();
    }
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("EMS_DISTRICT")) return new MapField("EMS DISTRICT\\b *(.*)", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.contains(field)) return;
      data.strCall = append(data.strCall, "/", field);
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("http:") || field.equals("https:")) return;
      if (field.startsWith("http:") || field.startsWith("https:")) {
        data.strInfoURL = field;
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "URL PLACE";
    }
  }
}
