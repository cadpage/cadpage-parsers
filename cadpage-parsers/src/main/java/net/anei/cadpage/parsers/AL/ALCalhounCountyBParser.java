package net.anei.cadpage.parsers.AL;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class ALCalhounCountyBParser extends FieldProgramParser {

  public ALCalhounCountyBParser() {
    super("CALHOUN COUNTY", "AL",
          "SRC ( UNIT/Z ID TIMES! TIMES+ | CALL ADDRCITY MAP UNIT! INFO/CS+ )");
  }

  @Override
  public String getFilter() {
    return "administrator@911.calhouncountyal.gov,cc911@911.calhouncountyal.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Active 911 Alert")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("ID")) return new IdField("[-0-9]+");
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("MAP")) return new MapField("[A-Z]+\\d+[A-Z]?|[A-Z]{3}", true);
    if (name.equals("UNIT")) return new UnitField("\\S+", true);
    return super.getField(name);
  }

  private class MyTimesField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      data.msgType = MsgType.RUN_REPORT;
      field = field.replace('.', ':');
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*), *([A-Z]{3})");
  private static final Pattern ADDR_APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM) *(.*)|\\d{1,5}[A-Z]?");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strCity = convertCodes(match.group(2).trim(), CITY_CODES);
      }
      for (String part : field.split(";")) {
        part = part.trim();
        if (data.strAddress.length() == 0) {
          parseAddress(part, data);
        } else {
          match = ADDR_APT_PTN.matcher(part);
          if (match.matches()) {
            String apt = match.group(1);
            if (apt == null) apt = part;
            data.strApt = append(data.strApt, "-", apt);
          } else {
            data.strPlace = append(data.strPlace, " - ", part);
          }
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ALE", "ALEXANDRIA",
      "ANC", "ANNISTON",
      "ANN", "ANNISTON",
      "EAS", "EASTABOGA",
      "GAD", "GADSDEN",
      "GLE", "GLENCOE",
      "HOB", "HOBSON CITY",
      "HOC", "HOBSON CITY",
      "JAC", "JACKSONVILLE",
      "JAU", "JACKSONVILLE",
      "OHA", "OHATCHEE",
      "OHC", "OHATCHEE",
      "OXC", "OXFORD",
      "OXF", "OXFORD",
      "PIC", "PIEDMONT",
      "PIE", "PIEDMONT",
      "PIH", "PIEDMONT",
      "WEA", "WEAVER",
      "WEC", "WEAVER",
      "WEL", "WELLINGTON"

  });

}
