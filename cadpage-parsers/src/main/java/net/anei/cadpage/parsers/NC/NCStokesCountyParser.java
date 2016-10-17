package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 *Stokes County, NC
 */
public class NCStokesCountyParser extends FieldProgramParser {

  public NCStokesCountyParser() {
    super(CITY_CODES, "STOKES COUNTY", "NC", 
          "SRC ( STA ID TIME/RN+ | SRC CALL ADDRCITY UNIT! INFO/N+ )");
  }
  
  @Override
  public String getFilter() {
    return "informationsystems@co.stokes.nc.us";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Stokes County Call")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("STA")) return new MySourceField("(?:STA|SP)[^ ]+");
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("ID")) return new IdField("F?\\d{2}-\\d{5,6}|\\d+", true);
    if (name.equals("TIME")) return new TimeField();
    if (name.equals("INFO")) return new MyInfoField();
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

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      
      Matcher mat = ADDR_SEMICOLON_PLACE.matcher(field);
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
  
  private class TimeField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('.', ':');
      super.parse(field, data);
    }
  }

  private static final Pattern ID_PTN = Pattern.compile("F\\d{2}-\\d{5,6}");
  private static final Pattern ID_TRUNC_PTN = Pattern.compile("F[-0-9]*");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d{4}) (\\d\\d:\\d\\d)");
  private static final Pattern DATE_TIME_TRUNC_PTN = Pattern.compile("\\d[ :/0-9]+");

  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      
      if (ID_PTN.matcher(field).matches()) {
        data.strCallId = field;
        return;
      }
      
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (match.matches()) {
        data.strDate = match.group(1);
        data.strTime = match.group(2);
        return;
      }
      
      if (isLastField(+1)) {
        if (ID_TRUNC_PTN.matcher(field).matches()) return;
        if (DATE_TIME_TRUNC_PTN.matcher(field).matches()) return;
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO ID DATE TIME";
    }
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "DAN", "Danbury",
      "GER", "Germanton",
      "KIN", "King",
      "LAW", "Lawsonville",
      "MAD", "Madison",
      "PIN", "Pinnacle",
      "SAN", "Sandy Ridge",
      "WAL", "Walnut Cove",
      "WC",  "Walnut Cove",
      "WES", "Westfield"
  });
}
