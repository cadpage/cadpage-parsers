package net.anei.cadpage.parsers.FL;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class FLWashingtonCountyParser extends FieldProgramParser {

  public FLWashingtonCountyParser() {
    super("WASHINGTON COUNTY", "FL",
          "ADDR X_PLACE YN EMPTY CALL1! NAME CALL/SDS EMPTY EMPTY ( ID EMPTY | ) INFO/N+? TIMES TIMES/N+? ID END");
  }

  @Override
  public String getFilter() {
    return "dispatch@wcso.us";
  }

  private String times;

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("SQL Server Message")) data.msgType = MsgType.RUN_REPORT;
    times = "";
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.msgType == MsgType.RUN_REPORT) data.strSupp = append(times, "\n\n", data.strSupp);
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("X_PLACE")) return new MyCrossPlaceField();
    if (name.equals("YN")) return new SkipField("[YN]?", true);
    if (name.equals("CALL1")) return new CallField("(?:FIRE|EMS).*", true);
    if (name.equals("TIMES")) return new MyTimesField();
    if (name.equals("ID")) return new IdField("[A-Z]+\\d{2}CAD\\d{6}", true);
    return super.getField(name);
  }

  private static final Pattern TRAIL_APT_PTN = Pattern.compile("(.* (?:RD|ST))(\\S+)");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = TRAIL_APT_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        apt = match.group(2);
      }
      field = stripDupSuffix(field);
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyCrossPlaceField extends Field {

    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("X2[");
      if (pt >= 0) {
        field = stripFieldEnd(field.substring(pt+3), "]");
        field = stripDupSuffix(field);
        data.strCross = field;
        return;
      }
      if (field.equals(getRelativeField(-1))) return;

      data.strPlace = field;
    }

    @Override
    public String getFieldNames() {
      return "PLACE X";
    }
  }

  private String stripDupSuffix(String field) {
    int pt;
    pt = field.lastIndexOf(' ');
    if (pt >= 0) {
      String suffix = field.substring(pt);
      String tmpFld = field.substring(0,pt).trim();
      if (tmpFld.endsWith(suffix)) field = tmpFld;
    }
    return field;
  }

  private static final String TIMES_HDR = "Date and Time ";
  private class MyTimesField extends Field {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith(TIMES_HDR)) return false;
      times = append(times, "\n", field.substring(TIMES_HDR.length()).trim());
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "INFO";
    }

  }
}
