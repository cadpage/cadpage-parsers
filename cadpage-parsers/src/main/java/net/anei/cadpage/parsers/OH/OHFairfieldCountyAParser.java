package net.anei.cadpage.parsers.OH;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class OHFairfieldCountyAParser extends FieldProgramParser {

  public OHFairfieldCountyAParser() {
    super("FAIRFIELD COUNTY", "OH",
          "CALL1? CALL/CS ( GPSADDR GPSADDR/CS GPS? X! | ADDR ( X! | CITY X! | CITY ST GPS? X! ) ) INFO/CS+");
  }

  @Override
  public String getFilter() {
    return "dispatch@co.fairfield.oh.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("#([A-Z]{2,5}\\d{8})(?:Dispatch)?");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    // reject OHFairfieldCountyB alerts
    if (subject.startsWith("Automatic")) return false;

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) data.strCallId = match.group(1);

    body = stripFieldEnd(body, ",}");
    return parseFields(body.split(","), data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("GPSADDR")) return new MyGPSAddressField();
    if (name.equals("CALL1")) return new CallField("FIELD", true);
    if (name.equals("ST")) return new StateField("([A-Z]{2})(?: +\\d{5})?", true);
    if (name.equals("GPS")) return new MyGPSField();
    if (name.equals("X")) return new MyCrossField();
    return super.getField(name);
  }

  private static final Pattern GPS_ADDR_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyGPSAddressField extends AddressField {
    public MyGPSAddressField() {
      setPattern(GPS_ADDR_PTN, true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, ",", field);
    }
  }

  private class MyGPSField extends GPSField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace("-", ",-");
      super.parse(field, data);
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("//")) return false;
      field = field.replace("//", "/");
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
