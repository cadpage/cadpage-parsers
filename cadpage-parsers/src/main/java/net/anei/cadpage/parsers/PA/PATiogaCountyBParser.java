package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class PATiogaCountyBParser extends FieldProgramParser {

  public PATiogaCountyBParser() {
    this("TIOGA COUNTY", "PA");
  }

  PATiogaCountyBParser(String defCity, String defState) {
    super(defCity, defState,
          "DISPATCHED:DATETIME? CODE_CALL! ADDRCITY! Cross_Streets:X? http:GPS!");
  }

  @Override
  public String getFilter() {
    return "911@tiogacountypa.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("CAD Page for CFS (\\S+)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    Matcher match =  SUBJECT_PTN.matcher(subject);
    if (!match.matches()) return false;
    data.strCallId = match.group(1);

    int pt = body.indexOf("\n____");
    if (pt >= 0) body = body.substring(0,pt).trim();

    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d\\d \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CODE_CALL")) return new MyCodeCallField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }

  private class MyCodeCallField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.equals(":")) return;
      int pt = field.indexOf(" : ");
      if (pt < 0) abort();
      data.strCode = field.substring(0,pt).trim();
      data.strCall = field.substring(pt+3).trim();
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      field = stripFieldStart(field, "U:");
      String[] parts = field.split(",", -1);
      if (parts.length != 3) abort();
      parseAddress(parts[0].trim(), data);
      data.strApt = append(data.strApt, "-", stripFieldStart(parts[1].trim(), "Apt"));
      data.strCity = parts[2].trim();
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('*', '/');
      super.parse(field, data);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf("?q=");
      if (pt < 0) return;
      field = field.substring(pt+3).replace("%2C", ",");
      super.parse(field, data);
    }
  }
}
