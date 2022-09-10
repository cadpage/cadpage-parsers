package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class NCCumberlandCountyParser extends FieldProgramParser {

  public NCCumberlandCountyParser() {
    super(CITY_CODES, "CUMBERLAND COUNTY", "NC",
          "( CANCEL ADDR CITY PLACEX END " +
          "| PLACEX DATETIME CALL UNIT? ADDR! X PLACE " +
          "| DATETIME CALL UNIT? ADDR! X PLACE " +
          "| ADDR CALL ( UNIT/Z! END | PLACE X/Z UNIT/Z! END | ( X | PLACE ) UNIT! END ) )");
  }

  @Override
  public String getFilter() {
    return "cad@co.cumberland.nc.us,messaging@iamresponding.com,@ci.fay.nc.us,777";
  }

  private static final Pattern SRC_PTN = Pattern.compile("Station .*");

  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    if (subject.endsWith("|N")) {
      subject = subject.substring(0,subject.length()-2);
      body = "(N) " + body;
    }
    if (subject.equals("S")) body = "(S)" + body;

    if (SRC_PTN.matcher(subject).matches()) data.strSource = subject;

    boolean cadPrefix = body.startsWith("CAD:");
    if (cadPrefix) body = body.substring(4).trim();
    setSelectValue(cadPrefix ? "CAD" : "");

    String[] flds = body.split("\n");
    if (flds.length < 3) flds = body.split(";");
    return parseFields(flds, data);
  }

  @Override
  public String getProgram() {
    return "SRC? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new MyCancelField();
    if (name.equals("PLACEX")) return new MyPlaceXField();
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private static final Pattern CANCEL_PTN = Pattern.compile("(?:\\{(\\S+)\\} *)?(CANCEL.*|SCENE SECURE|SLOW UNITS TO ROUTINE|UNDER CONTROL|UNIT WILL STAGE|TAC .*)");
  private class MyCancelField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CANCEL_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = getOptGroup(match.group(1));
        String call = match.group(2);
        if (call.startsWith("TAC ")) {
          data.strChannel = call;
        } else {
          data.strCall = call;
        }
        return true;
      }

      // We will (reluctantly) accept an empty field only if there is recognized city field
      // two places ahead
      if (field.length() == 0 && isCity(getRelativeField(+2))) return true;

      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT CALL CH";
    }
  }

  private static final Pattern PLACEX_UNIT_PTN = Pattern.compile("[A-Z]\\d{1,3}");
  private class MyPlaceXField extends Field {

    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      data.strPlace = append(p.getOptional("(S)"), " - ", p.getOptional("(N)"));
      field = p.get();

      if (field.length() > 0) {
        if (PLACEX_UNIT_PTN.matcher(field).matches()) {
          data.strUnit = field;
        } else if (NUMERIC.matcher(field).matches()) {
          data.strApt = field;
        } else {
          data.strPlace = append(data.strPlace, " - ", field);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "PLACE APT? UNIT?";
    }
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(\\d{1,2}(?:[A-Z]\\d{1,2}[A-Z]?)?) +(.*)");
  private class MyCallField extends CallField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCode = match.group(1);
      data.strCall = match.group(2);
      return true;
    }

    @Override
    public void parse(String field,  Data data) {
      if (checkParse(field, data)) return;
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(?:(.+?) +)??(?:([0-9/]+) +)?(\\b\\d\\d:\\d\\d:\\d\\d)");
  private static final Pattern DATE_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4}");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strPlace = append(data.strPlace, " / ", getOptGroup(match.group(1)));
      String sDate = match.group(2);
      data.strTime = match.group(3);
      if (sDate != null && DATE_PTN.matcher(sDate).matches()) data.strDate = sDate;
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "PLACE DATE TIME";
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[,A-Z0-9]+");
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!UNIT_PTN.matcher(field).matches()) return false;
      StringBuilder src = new StringBuilder();
      StringBuilder unit = new StringBuilder(data.strUnit);
      for (String token : field.split(",")) {
        StringBuilder sb = (token.startsWith("ST") ? src : unit);
        if (sb.length() > 0) sb.append(',');
        sb.append(token);
      }
      if (src.length() > 0) data.strSource = src.toString();
      data.strUnit = unit.toString();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "FAY", "FAYETTEVILLE",
      "HM",  "HOPE MILLS",
      "PAR", "PARKTON",
      "ROS", "ROSEBORO",
      "STD", "STEDMAN",
      "WA",  "WADE"

  });
}
