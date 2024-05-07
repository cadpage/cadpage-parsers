package net.anei.cadpage.parsers.OK;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class OKGarfieldCountyBParser extends FieldProgramParser {

  public OKGarfieldCountyBParser() {
    super("GARFIELD COUNTY", "OK",
          "CFS_Number:ID! Incident_Type:CALL! NONE Details:INFO! Caller:NAME! Call_Time:DATETIME! Call_Location:ADDRCITYST Location_Details:FINFO/N! " +
                "Address:SKIP! Address_Name:NAME! Responding_Units:UNIT! Message:INFO/N CFS_Latitude:GPS1! CFS_Longitude:GPS2! TRAILER! END");
  }

  @Override
  public String getFilter() {
    return "911firedispatch@enid.org";
  }

  private static final Pattern DELIM = Pattern.compile(" +\\|+ +");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strCall = subject;
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("NONE")) return new SkipField("None", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("FINFO")) return new MyFrontInfoField();
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("TRAILER")) return new SkipField("Please respond immediately.", true);
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      data.strSupp = append(data.strSupp, "\n", field);
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None") || field.isEmpty()) return;
      super.parse(field, data);
    }
  }

  private class MyFrontInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("; ", ",");
      super.parse(field, data);
    }
  }
}
