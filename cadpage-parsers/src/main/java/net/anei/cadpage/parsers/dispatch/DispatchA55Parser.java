package net.anei.cadpage.parsers.dispatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA55Parser extends FieldProgramParser {

  private DispatchA64Parser auxParser;
  private boolean useAuxParser;

  public DispatchA55Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Number:ID Call_Type:CALL/SDS Common_Place:PLACE Address:ADDR Apartment:APT " +
                  "( City:CITY! Postal_Code:ZIP "  +
                  "| City_State_County:CITY Disposition:SKIP How_Reported:SKIP Lat/Long:GPS Zip:ZIP MilePost:MP Subgrid_Grid_District:MAP " +
                  ") Notes:INFO/N+");
    auxParser = new DispatchA64Parser(defCity, defState);
  }

  private static final Pattern AUX_PTN = Pattern.compile("(?:Call Type|City|Address):");
  private static final Pattern NOT_AUX_PTN = Pattern.compile("\n|City State County:|Notes:");
  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:DISPATCH ALERT|OUT TAPS)[- ]*", Pattern.CASE_INSENSITIVE);
  private static final Pattern NOTES_PTN = Pattern.compile("\nNOTES(?:\n|$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    useAuxParser = AUX_PTN.matcher(body).lookingAt() && !NOT_AUX_PTN.matcher(body).find();
    if (useAuxParser) return auxParser.parseMsg(subject,  body, data);

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.lookingAt()) subject = subject.substring(match.end());
    data.strCall = subject;
    int pt = body.indexOf("\n_____");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = NOTES_PTN.matcher(body).replaceAll("Notes:\n");
    body = body.replace("\nNOTES\n", "\nNotes:\n");
    body = body.replace("\nFIRST NOTE:", "\nNotes:");
    if (!parseFields(body.split("\n"), data)) return false;
    if (data.strAddress.isEmpty() && data.strSupp.isEmpty()) return false;
    return true;
  }

  @Override
  public String getProgram() {
    if (useAuxParser) return auxParser.getProgram();
    return "CALL " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("MP")) return new MyMilePostField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CITY_JUNK_PTN = Pattern.compile("\\(.*?\\)");
  private static final Pattern CITY_ST_PTN = Pattern.compile("([ A-Za-z/]+), *([A-Z]{2})(?:[\\( ,]+.*)?");
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      field = CITY_JUNK_PTN.matcher(field).replaceAll("").trim();
      Matcher match = CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }

  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      data.strCity = field;
    }
  }

  private class MyMilePostField extends Field {
    @Override
    public void parse(String field, Data data) {
      if (field.length() > 0 && !data.strAddress.contains(" MM")) {
        if (checkAddress(data.strAddress) < STATUS_INTERSECTION) {
          data.strAddress = append(data.strAddress, " ", "MM " + field);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "ADDR?";
    }
  }

  private static final Pattern DATE_TIME_OPER_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}),? +(\\d\\d?:\\d\\d(?::\\d\\d)?(?: [AP]M)?) \\(.*\\)", Pattern.CASE_INSENSITIVE);
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm aa");
  private static final DateFormat TIME_SEC_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("(.*)\\b(OPS *\\d+)", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {

    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_OPER_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        String time = match.group(2).toUpperCase();
        if (!time.endsWith("M")) {
          data.strTime = time;
        } else {
          DateFormat fmt = time.length() <= 8 ? TIME_FMT : TIME_SEC_FMT;
          setTime(fmt, time, data);
        }
        return;
      }

      match = INFO_CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strChannel = match.group(2).toUpperCase();
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO CH";
    }
  }
}
