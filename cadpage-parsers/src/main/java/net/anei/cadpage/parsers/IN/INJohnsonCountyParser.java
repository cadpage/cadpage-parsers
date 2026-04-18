package net.anei.cadpage.parsers.IN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class INJohnsonCountyParser extends FieldProgramParser {

  public INJohnsonCountyParser() {
    super("JOHNSON COUNTY", "IN",
          "Locution_Dispatch%EMPTY! Units:UNIT! Incident_#:ID! Incident:CALL! Address:ADDR! Common_Place:PLACE! Apartment:APT! Cross:X! City:CITY! " +
              "Map:MAP! Radio:CH! Comments:INFO! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CADVoice";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Locution Dispatch")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM|LOT|UNIT) +(.*)|([A-Z]?\\d{1,4}[A-Z]?|[A-Z])");
  private static final Pattern ADDR_EXT_PTN = Pattern.compile("MM\\d+|[NSEW]B");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      String addressExt = "";
      Parser p = new Parser(field);
      while (true) {
        String part = p.getLastOptional(';');
        if (!p.isFound()) break;
        if (p.isEmpty()) continue;
        Matcher match = APT_PTN.matcher(part);
        if (match.matches()) {
          part = match.group(1);
          if (part == null) part = match.group(2);
          apt = append(part, "-", apt);
        } else if (ADDR_EXT_PTN.matcher(part).matches()) {
          addressExt = append(part, " ", addressExt);
        } else {
          data.strPlace = append(part, " - ", data.strPlace);
        }
      }
      super.parse(p.get(), data);
      data.strAddress = append(data.strAddress, " ", addressExt);
      data.strApt = append(data.strApt, "-", apt);
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE APT";
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d +\\d\\d/\\d\\d/\\d{4} - .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (INFO_HDR_PTN.matcher(field).matches()) return;
      super.parse(field, data);

    }
  }
}
