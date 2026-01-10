package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class ALLeeCountyCParser extends FieldProgramParser {

  public ALLeeCountyCParser() {
    super(ALLeeCountyParser.CITY_LIST, "LEE COUNTY", "AL",
          "Call_Type:CALL! Caller_Name:NAME! Call_Back_Number:PHONE! Address_of_Call:ADDRCITYST/S6! Notes:INFO! INFO/N+ NOM_Agent:SKIP!");
  }

  @Override
  public String getFilter() {
    return "messages@tascalls.com";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ADDRCITYST")) return new MyAddressCityStateField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -") || field.equals("N/A")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern PLACE_ADDR_UNIT_PTN = Pattern.compile("(.*?)\\((.*)\\)[- ]*(?:(?:APT|LOT|ROOM|UNIT) )?(.*)", Pattern.CASE_INSENSITIVE);
  private static final Pattern TRAIL_ZIP_PTN = Pattern.compile(" +\\d{5}$");
  private class MyAddressCityStateField extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -") || field.equals("N/A")) return;
      String apt = "";
      Matcher match = PLACE_ADDR_UNIT_PTN.matcher(field);
      if (match.matches()) {
        data.strPlace = match.group(1).trim();
        field = match.group(2).trim();
        apt = match.group(3).trim();
      }

      field = TRAIL_ZIP_PTN.matcher(field).replaceFirst("");
      if (field.toUpperCase().endsWith(" AL")) {
        data.strState = "AL";
        field = field.substring(0, field.length()-3).trim();
        field = stripFieldEnd(field, ",");
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "PLACE ADDR CITY ST APT";
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -") || field.equals("N/A")) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("- -") || field.equals("N/A")) return;
      super.parse(field, data);
    }
  }
}
