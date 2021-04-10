package net.anei.cadpage.parsers.dispatch;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA11Parser extends FieldProgramParser {

  private Properties cityCodes;

  public DispatchA11Parser(String defCity, String defState) {
    this(null, defCity, defState);
  }

  public DispatchA11Parser(Properties cityCodes, String defCity, String defState) {
    super(defCity, defState,
          "( SRC/Z UNIT/Z ID/Z PAGE_TIME! | SRC UNIT? CALL PAGED? ADDR! UNIT/S UNIT/S? INFO/N+? ( ID X/Z? DATETIME | X DATETIME | DATETIME ) )");
    this.cityCodes = cityCodes;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    return super.parseFields(body.split("\n"), 5, data);
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("PAGE_TIME")) return new BasePageTimeField();
    if (name.equals("SRC")) return new SourceField("[-A-Z0-9]+", true);
    if (name.equals("PAGED")) return new SkipField("PAGED|ASSGN|ENRT", true);
    if (name.equals("ADDR")) return new BaseAddressField();
    if (name.equals("UNIT")) return new UnitField("[-A-Z0-9]+", false);
    if (name.equals("ID")) return new IdField("\\d{2}[A-Z]{1,4}-?\\d+");
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("DATETIME")) return new BaseDateTimeField();
    return super.getField(name);
  }

  private class BasePageTimeField extends Field {
    BasePageTimeField() {
      super("PAGED- \\d\\d\\.\\d\\d\\.\\d\\d \\d\\d/\\d\\d/\\d\\d", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strCall = "RUN REPORT";
      data.strPlace = append(field, "\n", getRelativeField(+1));
    }

    @Override
    public String getFieldNames() {
      return "CALL PLACE";
    }
  }

  private static final Pattern APT_PTN = Pattern.compile("(?:APT|RM|ROOM)(.*)|(?:BLDG|BUILDING|SUITE).*");
  private class BaseAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (cityCodes != null) city = convertCodes(city, cityCodes);
      data.strCity = city;
      while (true) {
        String fld = p.getLastOptional(';');
        if (fld.length() == 0) break;
        Matcher match = APT_PTN.matcher(fld);
        if (match.matches()) {
          String apt = match.group(1);
          if (apt == null) apt = match.group();
          else apt = apt.trim();
          data.strApt = append(apt, "-", data.strApt);
        } else {
          data.strPlace = append(fld, " - ", data.strPlace);
        }
      }
      super.parse(p.get(), data);
    }

    public String getFieldNames() {
      return super.getFieldNames() + " PLACE CITY";
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return true;
      return super.checkParse(field, data);
    }

    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Intersection of:")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern SLOPPY_DATE_TIME_PTN = Pattern.compile("[ /:0-9]+");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d");
  private class BaseDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      if (!SLOPPY_DATE_TIME_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (DATE_TIME_PTN.matcher(field).matches()) {
        super.parse(field, data);
      }
    }
  }
}
