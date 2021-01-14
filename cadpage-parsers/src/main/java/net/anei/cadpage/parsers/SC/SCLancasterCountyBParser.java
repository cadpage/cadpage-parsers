package net.anei.cadpage.parsers.SC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCLancasterCountyBParser extends FieldProgramParser {

  public SCLancasterCountyBParser() {
    super("LANCASTER COUNTY", "SC",
          "DATETIME ADDRCITY CH PLACE BOX SPEC X CALL EMPTY UNIT! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "dispatch@lanc911.com";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Notification")) data.strSource = subject;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("SPEC")) return new MySpecialField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d?/\\d\\d?/\\d{4}) (\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?)");
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(1);
      String time = match.group(2);
      if (time.endsWith("M")) {
        setTime(TIME_FMT, time, data);
      } else {
        data.strTime = time;
      }
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }

  private static final Pattern SPEC_CITY_ST_PTN = Pattern.compile("(.*) ([NS]C)");
  private class MySpecialField extends Field {
    @Override
    public void parse(String field, Data data) {

      // This can be all kinds of things
      // Check for nothing
      if (field.length() == 0) return;

      // Try city/state
      Matcher match = SPEC_CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        data.strCity = match.group(1).trim();
        data.strState = match.group(2);
        return;
      }

      // If it is entirely contained in the following cross street field, ignore it
      if (getRelativeField(+1).indexOf(field) >= 0) return;

      // If it looks like a legitimate address, put it in the cross stree field
      if (isValidAddress(field)) {
        data.strCross = field;
        return;
      }

      // Otherwise treat it as a place field
      data.strPlace = append(data.strPlace, " - ", field);
    }

    @Override
    public String getFieldNames() {
      return "PLACE X CITY ST";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      data.strCross = append(data.strCross, ", ", field);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);

      // Use the parsed unit information to strip the extraneous unit stuff
      // off the end of the call field :(
      data.strUnit = data.strUnit.replace(" ", "");
      String[] units = data.strUnit.split(",");
      Arrays.sort(units, new Comparator<String>(){
        @Override
        public int compare(String s1, String s2) {
          return s2.length() - s1.length();
        }
      });

      while (true) {
        boolean found = false;
        for (String unit : units) {
          if (unit.length() == 0) continue;
          if (data.strCall.endsWith(unit)) {
            data.strCall = data.strCall.substring(0,data.strCall.length()-unit.length()).trim();
            found = true;
            break;
          }
        }
        if (!found) return;
        if (!data.strCall.endsWith(",")) return;
        data.strCall = data.strCall.substring(0,data.strCall.length()-1).trim();
      }
    }
  }

  @Override
  public String adjustMapCity(String city) {
    if (city.equalsIgnoreCase("INDIAN LAND")) return "FORT MILL";
    if (city.equalsIgnoreCase("HEATH SPRINGS")) return "LANCASTER";
    return city;
  }
}
