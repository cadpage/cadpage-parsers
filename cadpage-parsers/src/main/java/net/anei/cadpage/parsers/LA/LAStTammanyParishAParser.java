package net.anei.cadpage.parsers.LA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * St Tammany Parish, LA
 * NOTES: There is an Apartment field that is not always present.  This means that
 * the SRC_X field must have check logic in order for the FieldProgramParser to know
 * whether the apartment field exits or not.
 */
public class LAStTammanyParishAParser extends FieldProgramParser {

  private static final Pattern DELIM = Pattern.compile("\\|");

  public LAStTammanyParishAParser() {
    super("ST TAMMANY PARISH", "LA",
        "CALL! PLACE ADDRCITY APT? SRC_X TIME! ID? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "@dapage.net,@stfd.dapage.net,@stfpd1.dapage.net,stfpd1@stfpd1.dapage.net";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    String[] fields = DELIM.split(body);

    // Check to see subject and first field match, this is just so we
    // don't have a greedy parser
    if(!subject.equals(fields[0])) return false;

    if(!super.parseFields(fields, data)) return false;

    if (data.strAddress.length() == 0) {
      data.strAddress = "";
      parseAddress(data.strCross, data);
      data.strCross = "";
    }

    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("SRC_X")) return new MySourceCrossField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("ID")) return new IdField("(?:EVT# +)?(\\d{10})", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOM) *(.*)");
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }

  /***
   * MySourceCrossField - Extracts from the field that contains the source the
   * cross streee and source.  It also checks for an optional GRID field.  Grid is
   * then stored in MAP.
   */
  private static final Pattern SOURCE_X_PTN = Pattern.compile("((?:STA )?[^ ]+) ?(?:GRID (\\d+))? XS");
  private class MySourceCrossField extends SourceField {

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher sourceMatch = SOURCE_X_PTN.matcher(field);
      if(sourceMatch.find()) {
        parse(field, data);
        return true;
      }
      else return false;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher sourceMatch = SOURCE_X_PTN.matcher(field);
      if(sourceMatch.lookingAt()) {
        data.strSource = sourceMatch.group(1).trim();
        data.strMap = getOptGroup(sourceMatch.group(2));
        data.strCross = field.substring(sourceMatch.end()).trim();
      }
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " MAP X";
    }

    @Override
    public boolean canFail() {
      return true;
    }
  }

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "NARR");
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapCity(String city) {
    return convertCodes(city, CITY_TABLE);
  }

  // Lookup table for Google city designations
  private static final Properties CITY_TABLE = buildCodeTable(new String[]{
    "ST JOE", "PEARL RIVER"
  });
}
