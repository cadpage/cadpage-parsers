package net.anei.cadpage.parsers.AL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Dothan AL
 */
public class ALDothanAParser extends FieldProgramParser {


  public ALDothanAParser() {
    super("DOTHAN", "AL",
          "TIME CALL CALL2/L+? ADDR/SXa CITY! PLACE+? ( ID | UNIT ) INFO+");
    removeWords("ESTATES");
  }

  @Override
  public String getFilter() {
    return "Robot.ALERT@dothan.org,777802230001";
  }

  private static final Pattern MARKER = Pattern.compile("CITY OF DOTHAN:? +");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) body = body.substring(match.end());
    return parseFields(body.split("/"), 4, data);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      if (data.strApt.endsWith("INTERSECTN")) {
        data.strApt = data.strApt.substring(0, data.strApt.length()-10).trim();
      }
      if (data.strApt.startsWith("APT")) data.strApt = data.strApt.substring(3).trim();
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("CALL2")) return new CallField("Assist|Crit|Critical Injury|Reckless Driving|Sexual Offense", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyPlaceField extends PlaceField {

    @Override
    public void parse(String field, Data data) {
      if (isValidAddress(field)) {
        data.strCross = append(data.strCross, " & ", field);
      } else {
        data.strPlace = append(data.strPlace, "/", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "X PLACE";
    }
  }

  private static final Pattern ID_PTN = Pattern.compile("\\d{8,9}");
  private class MyIdField extends IdField {

    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // checkParse is called when we are checking to see if this is a valid
      // ID field or a place field.  Here we make the normal pattern validation
      if (!ID_PTN.matcher(field).matches()) return false;
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {

      // parse is called if we have already parsed a place name and are looking
      // for the real ID field.  Generally we should succeed if this matches the
      // normal ID pattern and fail if it does not.
      // One important exception, if this is a mutual aid call, the call ID
      // may be missing entirely, in which case we will treat this as an info
      // field
      if (ID_PTN.matcher(field).matches()) {
        super.parse(field, data);
      } else if (data.strCall.startsWith("MAID-")) {
        data.strSupp = field;
      } else abort();
    }
  }

  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z0-9]{1,5}");
  private class MyUnitField extends UnitField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // Unit field has to match unit pattern
      Matcher match = UNIT_PTN.matcher(field);
      if (!match.matches()) return false;

      // And there can not be a ID field anywhere behind us
      for (int ndx = 1; !isLastField(ndx); ndx++) {
        if (ID_PTN.matcher(getRelativeField(ndx)).matches()) return false;
      }

      // Otherwise OK
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }
}
