package net.anei.cadpage.parsers.NC;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;


public class NCDurhamCountyParser extends DispatchOSSIParser {

  public NCDurhamCountyParser() {
    super(CITY_CODES, "DURHAM COUNTY", "NC",
           "CALL ADDR! ( CITY!  | CODE CITY! | CH CODE? CITY! | X/Z CITY! | X/Z CODE CITY! | X/Z CH CODE? CITY! | X/Z X/Z CITY! |  X/Z X/Z CODE CITY/Y! | X/Z X/Z CH CODE? CITY! | X+? ) UNIT? CH? INFO/N+");
  }

  @Override
  public String getFilter() {
    return "CAD@durhamnc.gov";
  }

  @Override
  public boolean parseMsg(String body, Data data) {
    if (!body.startsWith("CAD:")) body = "CAD:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (!isPositiveId() && data.strCity.length() == 0
        && data.strTime.length() == 0
        && data.strCross.length() == 0) return false;
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CODE")) return new MyCodeField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("UNIT")) return new UnitField("(?:[A-Z]+\\d+|\\d+BR|\\d+SQ|[A-Z]+FD)(?:,[,A-Z0-9]+)?", true);
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern CODE_PTN = Pattern.compile("\\d{1,2}[A-Z]\\d{1,2}[A-Z]?");
  private class MyCodeField extends CodeField {
    MyCodeField() {
      setPattern(CODE_PTN, true);
    }
  }

  private static final Pattern CHANNEL_PTN = Pattern.compile("[+*]*(OPS\\d+)[+*]*", Pattern.CASE_INSENSITIVE);
  private class MyChannelField extends ChannelField {
    MyChannelField() {
      setPattern(CHANNEL_PTN, true);
    }
  }

  private static final Pattern INFO_PHONE_PTN = Pattern.compile("\\d{3} ?- ?\\d{3} ?- ?\\d{4}");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {

      if (field.length() == 0) return;

      Matcher match = CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        String code = match.group(1);
        if (!data.strChannel.contains(code)) {
          data.strChannel = append(data.strChannel, "/", match.group(1));
        }
        return;
      }

      if (data.strPhone.length() == 0 && INFO_PHONE_PTN.matcher(field).matches()) {
        data.strPhone = field.replace(" ", "");
        return;
      }

      if (data.strCode.length() == 0 && CODE_PTN.matcher(field).matches()) {
        data.strCode = field;
        return;
      }

      if (isValidCrossStreet(field)) {
        data.strCross = append(data.strCross, " & ", field);
        return;
      }

      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "X PHONE CODE INFO CH";
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "BAHA", "BAHAMA",
      "BUTN", "BAHAMA",   // ????
      "CARY", "CARY",
      "CHAP", "CHAPEL HILL",
      "CHAT", "CHATHAM COUNTY",
      "DURH", "DURHAM",
      "MORR", "MORRISVILLE",
      "HILL", "HILLSBOROUGH",
      "PERS", "PERSON COUNTY",
      "RALE", "RALEIGH",
      "ROUG", "ROUGEMONT",
      "RTP",  "RESEARCH TRIANGLE PARK",
      "WAKE", "WAKE COUNTY",

      "DURHAM",    "DURHAM"
  });
}
