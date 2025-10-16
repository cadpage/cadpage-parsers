package net.anei.cadpage.parsers.ID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class IDNezPerceCountyParser extends FieldProgramParser {

  public IDNezPerceCountyParser() {
    super("NEZ PERCE COUNTY", "ID",
          "Address:ADDR! Nature:CODE_CALL! Comments:INFO! EMPTY! END");
  }

  @Override
  public String getFilter() {
    return "FRN-cityoflewistonid@email.getrave.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("PreAlert")) return false;
    int pt = body.indexOf("\n\nClick the following link");
    if (pt >= 0) body = body.substring(0,pt);
    return parseFields(body.split("\\| ", -1), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("INFO"))  return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(.*?)[; ]*(?:\\bSPC |#) *(.*)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String place = "";
      int pt = field.indexOf(';');
      if (pt >= 0) {
        place = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      field = stripTrailApt(field, data);
      super.parse(field, data);
      data.strPlace = stripTrailApt(place, data);
    }

    private String stripTrailApt(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strApt = append(data.strApt, "-", match.group(2));
      }
      return field;
    }

    @Override
    public String getFieldNames() {
      return "ADDR APT PLACE";
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2})(.*)");
  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        data.strCode = match.group(1);
        field = match.group(2).trim();
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d .*");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        if (INFO_JUNK_PTN.matcher(line).matches()) continue;
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
