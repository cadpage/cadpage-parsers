package net.anei.cadpage.parsers.WV;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Fayette County, WV
 */
public class WVFayetteCountyParser extends FieldProgramParser {

  public WVFayetteCountyParser() {
    super(CITY_CODES, "FAYETTE COUNTY", "WV", "SRC ( STA UNIT INFO/RN+ | CALL ADDRCITY UNIT STA! INFO/N+ )");
  }

  @Override
  public String getFilter() {
    return "@fayettecounty911wv.org,Fayette_911@wv.org";
  }

  private static Pattern DELIM = Pattern.compile(" *\n *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("SRC")) return new MySourceField("[^ ]*");
    if (name.equals("STA")) return new MySourceField("(?:STA|SP)[^ ]+");
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    return super.getField(name);
  }

  private class MySourceField extends SourceField {

    public MySourceField(String pattern) {
      super(pattern, true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strSource = append(data.strSource, " ", field);
    }
  }

  private static Pattern ADDR_SEMICOLON_PLACE = Pattern.compile("(.*?); *(.*?)(,.*)?");
  private static Pattern IDD_MILE_MARKER = Pattern.compile("(?:I[ -]?)?(\\d+)[/ ]+(NORTH|SOUTH|N|S)B?[/ ]+(?:MM[/ ]*)?(\\d+)(?:[/ ]*MM)?([,;].*)?", Pattern.CASE_INSENSITIVE);

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = IDD_MILE_MARKER.matcher(field);
      if (mat.matches()) {
        field = "I " + mat.group(1) + " " + mat.group(2) + " " + mat.group(3) + " MM" + getOptGroup(mat.group(4));
      }

      mat = ADDR_SEMICOLON_PLACE.matcher(field);
      if (mat.matches()) {
        data.strPlace = mat.group(2);
        field = mat.group(1) + getOptGroup(mat.group(3));
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "ADDR PLACE CITY";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BEC", "BECKLEY",    // ????
      "FAY", "FAYETTEVILLE",
      "GAL", "GAULEY BRIDGE",
      "GLJ", "GLEN JEAN",
      "KIN", "KINGSTON",
      "MIN", "MINDEN",
      "MTH", "MT HOPE",
      "NAL", "NALLEN",
      "OAK", "OAK HILL",
      "PAX", "PAX",
      "PRI", "PRINCE",
      "SCA", "SCARBRO",
      "THU", "THURMOND"
  });
}
