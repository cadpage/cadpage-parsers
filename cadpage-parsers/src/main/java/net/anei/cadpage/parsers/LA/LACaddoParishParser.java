package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class LACaddoParishParser extends FieldProgramParser {

  public LACaddoParishParser() {
    super(CITY_CODES, "CADDO PARISH", "LA",
          "EV:ID! Location:ADDR/S? TYPE_CODE:CODE_CALL! CLNAME:NAME TIME:TIME! Comments:INFO! CALLER_ADDR:SKIP EID:ID END");
  }

  @Override
  public String getFilter() {
    return "@caddo911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    data.strSource = stripFieldEnd(subject, " Incident Notification");
    body = body.replace("EID:", " EID:");
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      super.parse(p.get(": @"), data);
      data.strPlace = p.get();
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strCode = p.get(' ');
      data.strCall = p.get();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern INFO_PHONE_GPS_PTN = Pattern.compile("\\d+ +ALT# (\\S+) +([-+]?\\d{2}\\.\\d{4,}) \\d+ ([-+]?\\d{2}\\.\\d{4,})\\b *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PHONE_GPS_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strPhone = match.group(1);
        setGPSLoc(match.group(3)+','+match.group(2), data);
        field = field.substring(match.end());
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PHONE GPS INFO";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CADD", "CADDO PARISH",
      "GWD",  "GREENWOOD",
      "SHV",  "SHREVEPORT"
  });
}
