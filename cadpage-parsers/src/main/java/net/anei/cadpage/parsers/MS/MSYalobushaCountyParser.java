package net.anei.cadpage.parsers.MS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MSYalobushaCountyParser extends FieldProgramParser {

  public MSYalobushaCountyParser() {
    super("YALOBUSHA COUNTY", "MS",
          "( DATE:DATE! TIME:TIME! num:SKIP! ADDR:ADDR! CROSS:X! INC_TYPE:CALL! REMARKS:INFO! INFO/N+ EQUIPMENT:UNIT! STATUS_DATE:SKIP! END " +
          "| RPT_NUMBER:ID! EQUIPMENT:UNIT! ADDR:ADDR! INC_TYPE:CALL! INFO/R! INFO/N+ " +
          ")");
  }

  @Override
  public String getFilter() {
    return "cadpage@adsisoftware.com";
  }

  private static final Pattern DELIM = Pattern.compile("\n| +(?=TIME:|num:)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Alert Recieved")) return false;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATE")) return new DateField("\\d\\d?/\\d\\d?/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d?:\\d\\d", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern ADDR_SFX_PTN = Pattern.compile("(.* (?:CR|HWY) .*) (?:HW|RD)");

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ADDR_SFX_PTN.matcher(field);
      if (match.matches()) field = match.group(1).trim();
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf("||");
      if (pt >= 0) {
        if (data.strCallId.isEmpty()) data.strCallId = field.substring(pt+2).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ID";
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, ",");
      field = stripFieldEnd(field, ",");
      super.parse(field, data);
    }
  }
}
