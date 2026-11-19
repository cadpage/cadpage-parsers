package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHLorainCountyBParser extends FieldProgramParser {

  public OHLorainCountyBParser() {
    super("LORAIN COUNTY", "OH",
          "CALL:CALL! PLACE:PLACE! ADDR:ADDR/S6! CITY:CITY! ( LATITUDE:GPS1! LONGITUDE:GPS2 | ) ID:ID! UNIT:UNIT! INFO:INFO! INFO/N+ XSTREET:X WS:SKIP END");
  }

  @Override
  public String getFilter() {
    return "sunsrv@sundance-sys.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (!super.parseFields(body.split("\n"), data)) return false;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private Pattern CODE_CALL_PTN = Pattern.compile("([A-Za-z0-9]+)-(.*)");
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field =  match.group(2).trim();
        match = CODE_CALL_PTN.matcher(field);
        if (match.matches()) {
          String code = match.group(1);
          field = match.group(2).trim();
          if (!code.equals(data.strCode)) data.strCode = data.strCode + '-' + code;
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "0 ");
      field =  stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\[\\d\\d?:\\d\\d:\\d\\d\\] *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_HDR_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      if (field.startsWith("FireNo")) return;
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }
}
