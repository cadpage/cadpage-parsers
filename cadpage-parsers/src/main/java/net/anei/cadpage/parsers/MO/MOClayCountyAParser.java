package net.anei.cadpage.parsers.MO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MOClayCountyAParser extends FieldProgramParser {

  public MOClayCountyAParser() {
    super("CLAY COUNTY", "MO",
          "( SELECT/1 Call_Location:ADDRCITYST! Location_Details:APT! Address:SKIP! Address_Name:PLACE! Responding_Units:UNIT! " +
                "Details:INFO! Message:EMPTY! CFS_Latitude:GPS1! CFS_Longitude:GPS2! CFS_Number:ID! Incident_Type:CALL1/SDS! Caller:NAME! " +
                "Dispatcher:SKIP! Call_Time:DATETIME! Cross_Streets:X " +
          "| ID CALL CALL/L+? NONE ADDR_PFX2+? ADDRCITYST2 EMPTY EMPTY! " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "zuercher@gladstone.mo.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CAD Call")) {
      setSelectValue("2");
      return parseFields(body.split("/", -1), data);
    }

    setSelectValue("1");
    data.strCall = subject;
    body = stripFieldEnd(body, " Please respond immediately.");
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "CALL? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("CALL1")) return new MyCall1Field();
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d/\\d\\d \\d\\d:\\d\\d", true);
    if (name.equals("NONE")) return new SkipField("None", true);
    if (name.equals("ADDR_PFX2")) return new MyAddressPrefix2Field();
    if (name.equals("ADDRCITYST2")) return new MyAddressCityState2Field();
    return super.getField(name);
  }

  private static final Pattern INFO_BRK_PTN = Pattern.compile("[; ]*\\b\\d\\d?/\\d\\d?/\\d\\d \\d\\d:\\d\\d:\\d\\d - *");

  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      field = INFO_BRK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private class MyCall1Field extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      data.strCall = append(data.strCall, " - ", field);
    }
  }

  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      super.parse(field, data);
    }
  }

  private class MyAddressPrefix2Field extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (getRelativeField(+1).isEmpty()) return false;
      data.strAddress = append(data.strAddress, " & ", field);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private class MyAddressCityState2Field extends AddressCityStateField {
    @Override
    public void parse(String field, Data data) {
      field = append(data.strAddress, " & ", field);
      data.strAddress = "";
      super.parse(field, data);
    }
  }
}
