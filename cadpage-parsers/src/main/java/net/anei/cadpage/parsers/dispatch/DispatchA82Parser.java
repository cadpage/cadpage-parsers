package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA82Parser extends FieldProgramParser {

  public DispatchA82Parser(String defCity, String defState) {
    super(defCity, defState,
          "ID ADDRCITY PLACE X MASH MASH+? EMPTY! UNITS:UNIT! EMPTY! INFO/N+? URL END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CFS Page")) return false;
    return parseFields(body.split("\n"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{8}", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MASH"))  return new MyMashField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("URL")) return new InfoUrlField("https?:.*");
    return super.getField(name);
  }

  private static final Pattern ADDR_ZIP_PTN = Pattern.compile("(.*?) +(\\d{5})");
  private static final Pattern ADDR_ST_PTN = Pattern.compile("[A-Z]{2}");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ZIP_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        zip = match.group(2);
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      if (ADDR_ST_PTN.matcher(city).matches()) {
        data.strState = city;
        city = p.getLastOptional(',');
      }
      data.strCity = city;
      if (city.length() == 0 && zip != null) data.strCity = zip;
      parseAddress(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }

  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern MASH_PTN = Pattern.compile("\\[([A-Z]+) \\(([^()]+)\\) (?:\\(Pri:(\\d+)\\) )?(?:\\(Esc:(\\d+)\\) )?- DIST: (\\S+) - GRID: (\\S+)\\]");
  private class MyMashField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("[") || !field.endsWith("]")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = MASH_PTN.matcher(field);
      if (!match.matches()) abort();
      if (data.strSource.length() == 0) data.strSource = match.group(1);
      if (data.strCall.length() == 0) data.strCall = match.group(2).trim();
      if (data.strPriority.length() == 0) data.strPriority = append(getOptGroup(match.group(3)), "/", getOptGroup(match.group(4)));
      if (data.strBox.length() == 0) data.strBox = match.group(5);
      if (data.strMap.length() == 0) data.strMap = match.group(6);
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL PRI BOX MAP";
    }
  }

  private Pattern INFO_PFX_PTN = Pattern.compile("Rmk\\d+: \\[\\d\\d?:\\d\\d:\\d\\d\\]: *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PFX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    return SMALL_BLOCK_PTN1.matcher(sAddress).replaceAll("$1_$2");
  }
  private static final Pattern SMALL_BLOCK_PTN1 = Pattern.compile("(SMALL) +(BLOCK)", Pattern.CASE_INSENSITIVE);

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return SMALL_BLOCK_PTN2.matcher(sAddress).replaceAll("$1 $2");
  }
  private static final Pattern SMALL_BLOCK_PTN2 = Pattern.compile("(SMALL)_(BLOCK)", Pattern.CASE_INSENSITIVE);
}
