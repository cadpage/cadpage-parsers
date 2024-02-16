package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class VAPetersburgParser extends FieldProgramParser {

  public VAPetersburgParser() {
    super("PETERSBURG", "VA",
          "Number:ID Incident_Type:CALL Call_Location:ADDRCITYST! Location_Details:PLACE Cross_Street:X! Address:SKIP! Responding_Units:UNIT! " +
               "Details:INFO! Message:EMPTY! CFS_Latitude:GPS1! CFS_Longitude:GPS2! Business:PLACE! Time_of_Dispatch:DATETIME");
  }

  @Override
  public String getFilter() {
    return "cadnoreply@petersburg-va.org";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = stripFieldEnd(body, "*");
    return super.parseMsg(body, data);
  }

  @Override
  public boolean parseFields(String[] flds, Data data) {
    for (int j = 0; j < flds.length; j++) {
      flds[j] = stripFieldEnd(flds[j], "None");
    }
    return super.parseFields(flds, data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(.*?) *\\bAPT\\b *(.*)");
  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strApt = append(data.strApt, "-", match.group(2));
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT";
    }
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }

  }
}
