package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHKnoxCountyBParser extends FieldProgramParser {

  public OHKnoxCountyBParser() {
    super("KNOX COUNTY", "OH",
          "ADDR1? ADDR_CITY_ST X CALL/SDS ID! INFO/N+ EMPTY END");
  }

  @Override
  public String getFilter() {
    return "Zuercher@co.knox.oh.us";
  }

  private static final Pattern DELIM = Pattern.compile("(?: |(?<= ))/(?: |$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR1")) return new MyAddress1Field();
    if (name.equals("ADDR_CITY_ST")) return new MyAddressCityStateField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("CFS\\d+", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddress1Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.contains(",")) return false;
      String next = getRelativeField(+1);
      if (next.length() == 0)  return false;
      if (!next.contains(",") && checkAddress(field) != STATUS_STREET_NAME) return false;
      data.strAddress = field;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityStateField extends Field {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = ST_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        String zip = match.group(2);
        city = p.getLastOptional(',');
        if (city.length() == 0 && zip != null) city = zip;
      }
      data.strCity = city;

      String addr1 = data.strAddress;
      data.strAddress = "";
      parseAddress(StartType.START_ADDR, FLAG_RECHECK_APT | FLAG_ANCHOR_END, p.get(), data);
      data.strAddress = append(addr1, " & ", data.strAddress);
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT CITY ST";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern INFO_DELIM_PTN = Pattern.compile("[; ]*\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_DELIM_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }
}
