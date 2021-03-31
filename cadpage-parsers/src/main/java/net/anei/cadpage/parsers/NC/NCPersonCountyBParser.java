package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class NCPersonCountyBParser extends DispatchOSSIParser {

  public NCPersonCountyBParser() {
    super(CITY_CODES, "PERSON COUNTY", "NC",
          "( SELECT/2 CALL ADDR X X CITY/Y UNIT! UNIT+? " +
          "| CANCEL ADDR! CITY " +
          "| FYI? ( PLACE ADDR/Z CITY | ADDR/Z CITY | ADDR/Z EMPTY | PLACE ADDR/Z EMPTY ) X X CALL DATETIME! UNIT/S+? ) INFO/CS+");
    setupCities(NCPersonCountyParser.CITY_LIST);
  }

  @Override
  public String getFilter() {
    return "CAD@personcountync.gov";
  }

  private static final Pattern MASTER = Pattern.compile("(\\S+)\\sCAD\\s+");

  @Override
  protected boolean parseMsg(String body, Data data) {
    Matcher match = MASTER.matcher(body);
    if (match.lookingAt()) {
      setSelectValue("1");
      data.strSource = match.group(1);
      body = body.substring(match.end());
    } else {
      setSelectValue("2");
    }
    body = "CAD:" + body.replace(',', ';');
    return super.parseMsg(body, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CANCEL")) return new BaseCancelField("CANCEL NOTIFICATION");
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("CH")) return new ChannelField("OPS\\d|DOT\\d|HMFD\\d", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *\\[\\d\\d/[^\\]]*(?:\\] *|$)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_JUNK_PTN.matcher(field).replaceAll("\n").trim();
      super.parse(field, data);
    }
  }

  private static final Pattern CH_PTN = Pattern.compile("OPS\\d|DOT\\d");
  private static final Pattern UNIT_PTN = Pattern.compile("[A-Z]+\\d+|[A-Z]{2,4}FD|\\d{4}|[A-Z]{1,3}POV|ALMTST|HP|NCFOR|PCRES");
  private class MyUnitField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CH_PTN.matcher(field);
      if (match.matches()) {
        data.strChannel = append(data.strChannel, " ", field);
        return true;
      }
      match = UNIT_PTN.matcher(field);
      if (match.matches()) {
        data.strUnit = append(data.strUnit, " ", field);
        return true;
      }
      return false;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "UNIT CH";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "ROUG", "ROUGEMONT",
      "ROXB", "ROXBORO",
      "TIMB", "TIMBERLAKE"
  });
}
