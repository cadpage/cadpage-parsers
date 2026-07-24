package net.anei.cadpage.parsers.WV;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WVSummitBechtelReserveCParser extends FieldProgramParser {

  public WVSummitBechtelReserveCParser() {
    super("SUMMIT BECHTEL RESERVE", "WV",
          "GLocation:ADDR! Location:PLACE! Threatening/Near:CALL? Type:CALL2! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "noreply@jotform.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n+"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CALL2")) return new MyCall2Field();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_PTN = Pattern.compile("(.*) \\(([-+]?\\d\\d\\.\\d{6}, [-+]?\\d\\d\\.\\d{6})\\)[- ]*");

  private class MyAddressField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strAddress = match.group(1);
        setGPSLoc(match.group(2), data);
      } else {
        data.strAddress = field;
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR GPS";
    }
  }

  private class MyCall2Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      data.strCall = append(field, " Threatening/Near: ", data.strCall);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(":")) return;
      if (field.startsWith("Caller:")) {
        data.strName = field.substring(7).trim();
      } else if (field.startsWith("Phone:")) {
        data.strPhone = field.substring(6).trim();
      } else {
        field = stripFieldStart(field, "Details:");
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "INFO NAME PHONE";
    }
  }
}
