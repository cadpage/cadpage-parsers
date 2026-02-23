package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXBurlesonCountyBParser extends FieldProgramParser {

  public TXBurlesonCountyBParser() {
    super("BURLESON COUNTY", "TX",
          "ADDRCITY! ID_UNIT EMPTY INFO/N+");
  }

  @Override
  public String getFilter() {
    return "crimes.helpdesk@gmail.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;
    data.strCall = subject;

    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID_UNIT")) return new MyIdUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("-(.*)- +(.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPlace = match.group(1).trim();
      field = match.group(2).replace("&,", ",");
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames();
    }
  }

  private static final Pattern ID_UNIT_PTN = Pattern.compile("(\\d{10})=(.*)");
  private class MyIdUnitField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = ID_UNIT_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strCallId = match.group(1);
      data.strUnit = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "ID UNIT";
    }
  }

  private static final Pattern INFO_PTN = Pattern.compile("(?:[A-Za-z]+: *)?(.*?)(?: *\\[\\d\\d:\\d\\d:\\d\\d\\])?");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      Matcher match = INFO_PTN.matcher(field);
      if (!match.matches()) abort();     // Not possible
      field = match.group(1);
      super.parse(field, data);
    }
  }
}
