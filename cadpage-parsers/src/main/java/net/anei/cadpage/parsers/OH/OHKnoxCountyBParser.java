package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OHKnoxCountyBParser extends FieldProgramParser {

  public OHKnoxCountyBParser() {
    super("KNOX COUNTY", "OH",
          "( ID " +
          "| ADDR/Z? ADDR_CITY_ST X/Z MISC+? ID! " +
          ") INFO/N+? UNIT! END");
  }

  @Override
  public String getFilter() {
    return "Zuercher@co.knox.oh.us";
  }

  private static final Pattern DELIM = Pattern.compile("(?: |(?<= ))/(?: |$)|/{2,}");

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
    if (name.equals("ADDR_CITY_ST")) return new MyAddressCityStateField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MISC")) return new MyMiscField();
    if (name.equals("ID")) return new IdField("CFS\\d+", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +(\\d{5}))?");
  private class MyAddressCityStateField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains(",")) return false;
      parse(field, data);
      return true;
    }

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

  private static final Pattern MISC_DELIM_PTN = Pattern.compile(" *(?:(?<!1)/(?!2)|\\\\) *");
  private static final Pattern CH_PTN = Pattern.compile("\\b(?:\\d* *(?:EMS|OPS) *\\d*)$", Pattern.CASE_INSENSITIVE);
  private static final Pattern APT_PTN = Pattern.compile("\\b(?:(?:APT|ROOM|RM|LOT|UNIT)(?!S) *(.*)|\\d+|[A-Z]|1/2)$", Pattern.CASE_INSENSITIVE);
  private class MyMiscField extends Field {
    @Override
    public void parse(String field, Data data) {
      for (String part : MISC_DELIM_PTN.split(field)) {
        if (part.equals("None") || part.isEmpty()) continue;
        Matcher match = CH_PTN.matcher(part);
        if (match.find()) {
          data.strChannel = append(data.strChannel, "/", match.group().trim());
          part = part.substring(0,match.start()).trim();
        }
        String apt = "";
        while (true) {
          match = APT_PTN.matcher(part);
          if (!match.find()) break;
          String tmp = match.group(1);
          if (tmp == null) tmp = match.group();
          apt = append(tmp, "-", apt);
          part = part.substring(0, match.start()).trim();
        }
        data.strApt = append(data.strApt, "-", apt);
        data.strPlace = append(data.strPlace, " - ", part);
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT CH";
    }
  }

  private static final Pattern INFO_DELIM_PTN = Pattern.compile("[; ]*\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None") || field.isEmpty()) return;
      field = INFO_DELIM_PTN.matcher(field).replaceAll("\n").trim();
      if (field.equals(data.strCity)) return;
      super.parse(field, data);
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("(?:\\b(?:(?:[A-Z]{3,}-)?(?:[A-Z]+\\d+|\\d{3,4})(?:-[-A-Z0-9]+)?|DISPATCH)\\b[; ]*)+", Pattern.CASE_INSENSITIVE);
  private static final Pattern UNIT_DELIM_PTN = Pattern.compile(" *; *");
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // Only last field can be unit field
      if (!isLastField()) return false;
      if (!UNIT_PTN.matcher(field).matches()) return false;
      field = UNIT_DELIM_PTN.matcher(field).replaceAll(",");
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
