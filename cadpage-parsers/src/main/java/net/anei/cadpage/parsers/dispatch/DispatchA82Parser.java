package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class DispatchA82Parser extends FieldProgramParser {

  public DispatchA82Parser(String defCity, String defState) {
    super(defCity, defState,
          "( SELECT/1 ID | ID2 | CALL/SDS ID2 | CALL/SDS CALL/SDS ID2 | CALL/SDS CALL/SDS CALL/SDS ID2 | ) " +
          "( ADDRCITYST PLACE X MASH1+? EMPTY! UNITS:UNIT! EMPTY+? St_Rmk:MAP/C? INFO/N+? Grid_Map:MAP/L? Lat:GPS1? Lon:GPS2? EMPTY? INFO/ZN+? URL END " +
          "| CALL/SDS! CALL/SDS+? ADDRCITYST! X MASH2? UNITS:UNIT? ST_RMK:MAP/C? INFO/N+ " +
          ")");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_SUPPR_LA;
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.equals("CFS Page")) {
      setSelectValue("1");
    } else if (subject.equals("Message from Dispatch")) {
      setSelectValue("2");
    } else if (!subject.isEmpty()) {
      setSelectValue("2");
      data.strCall = subject;
    }
    body = body.replace("\nUNITS\n", "\nUNITS:\n").replace(" Lon:", "\nLon:");
    if (!parseFields(body.split("\n"), data)) return false;

    // Calls without a call ID are rare and somewhat questionable.  So let's confirm that they at
    // least found a legitimate mash field
    return !data.strCallId.isEmpty() || !data.strSource.isEmpty();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID1")) return new IdField("\\d{8}", true);
    if (name.equals("ID2")) return new BaseId2Field();
    if (name.equals("ADDRCITYST")) return new BaseAddressCityStateField();
    if (name.equals("X")) return new BaseCrossField();
    if (name.equals("MASH1")) return new BaseMash1Field();
    if (name.equals("MASH2")) return new BaseMash2Field();
    if (name.equals("INFO")) return new BaseInfoField();
    if (name.equals("URL")) return new InfoUrlField("https?:.*");
    return super.getField(name);
  }

  private static final Pattern ID2_PTN = Pattern.compile("(?:(.*?) +)?(\\d{8})(?!\\d)[- ]*(.*)");

  private class BaseId2Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = ID2_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCall = append(getOptGroup(match.group(1)), " - ", match.group(3));
      data.strCallId = match.group(2);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "ID CALL?";
    }
  }

  private class BaseAddressCityStateField extends AddressCityStateField {
    @Override
    public boolean checkParse(String field, Data data) {

      // Pretty much can count on legitimate address fields containing a comma.  But there are rare exceptions
      // which we identify by looking to see if there is a legitimate MASH1 field 3 fields ahead, and ensuring
      // that the next two fields do not look like address fields
      if (!field.contains(",")) {
        String tmp = getRelativeField(+3);
        if (!tmp.startsWith("[") || !tmp.endsWith("]")) return false;
        if (getRelativeField(+1).contains(",") || getRelativeField(+2).contains(",")) return false;
      }
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (field.endsWith("]")) {
        int pt = field.indexOf('[');
        if (pt >= 0) {
          data.strPlace = field.substring(pt+1, field.length()-1).trim();
          field = field.substring(0,pt).trim();
        }
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private class BaseCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "/");
      field = stripFieldEnd(field, "/");
      super.parse(field, data);
    }
  }

  private static final Pattern MASH1_PTN = Pattern.compile("\\[([A-Z0-9 ]+) \\((.+?)\\) (?:\\(Pri:(\\d+)\\) )?(?:\\(Esc:(\\d+)\\) )?- (?:DIST: (\\S+) - )?GRID: (\\S+)\\]");
  private class BaseMash1Field extends Field {
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
      Matcher match = MASH1_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strSource = merge(data.strSource, match.group(1));
      if (data.strCall.length() == 0) data.strCall = match.group(2).trim();
      if (data.strPriority.length() == 0) data.strPriority = append(getOptGroup(match.group(3)), "/", getOptGroup(match.group(4)));
      data.strBox = merge(data.strBox, match.group(5));
      data.strMap = merge(data.strMap, match.group(6));
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL PRI BOX MAP";
    }
  }

  private static final Pattern MASH2_SEP_PTN = Pattern.compile("\\] *\\[");
  private static final Pattern MASH2_PTN = Pattern.compile("(?:(\\S+) )?(?:DIST: ?(\\S*) )?GRID: ?(\\S*) *");
  private class BaseMash2Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.startsWith("[") || !field.endsWith("]")) return false;
      field = field.substring(1, field.length()-1).trim();
      field = MASH2_SEP_PTN.matcher(field).replaceAll(" ");
      while (!field.isEmpty()) {
        Matcher match = MASH2_PTN.matcher(field);
        if (!match.lookingAt()) abort();
        data.strSource = merge(data.strSource, match.group(1));
        data.strBox = merge(data.strBox, match.group(2));
        data.strMap = merge(data.strMap, match.group(3));
        field = field.substring(match.end());
      }
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "SRC BOX MAP";
    }
  }

  private static String merge(String base, String field) {
    if (field == null) return base;
    if (base.contains(field)) return base;
    return append(base, ",", field);
  }

  private Pattern INFO_PFX_PTN = Pattern.compile("Rmk\\d+: \\[\\d\\d?:\\d\\d:\\d\\d\\]: *|CFS RMK \\d\\d:\\d\\d *");
  private Pattern INFO_JUNK_PTN = Pattern.compile("\\{\\S+ +\\d\\d:\\d\\d\\}|<NO CALL REMARKS>|\\[SENT: \\d\\d:\\d\\d:\\d\\d\\]");
  private class BaseInfoField extends InfoField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith("http")) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_PFX_PTN.matcher(field);
      if (match.lookingAt()) field = field.substring(match.end());
      if (INFO_JUNK_PTN.matcher(field).matches()) return;
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
