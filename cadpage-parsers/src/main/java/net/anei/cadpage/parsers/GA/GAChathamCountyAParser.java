package net.anei.cadpage.parsers.GA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

/**
 * Chatham County, GA
 */

public class GAChathamCountyAParser extends DispatchProQAParser {

  public GAChathamCountyAParser() {
    super("CHATHAM COUNTY", "GA",
          "( SELECT/1 ID! ID2/L ADDR ( NAME PHONE CITY | APT_PLACE CITY ) GPS1 GPS2 UNIT CALL! CH? INFO/N+ " +
          "| CALL UNIT ADDR APT_PLACE CITY ST! INFO/N+ " +
          ") ", true);
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.startsWith("AUTOPAGE | ")) {
      setSelectValue("2");
      body = body.substring(11).trim();
      return parseFields(body.split("\\|"), data);
    } else {
      setSelectValue("1");
      if (!super.parseMsg(body, data)) return false;
      if (data.msgType == MsgType.RUN_REPORT && data.strSupp.startsWith("Alert:")) data.msgType = MsgType.GEN_ALERT;
      return true;
    }
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ID2")) return new IdField("[EF]20.*|\\d{4,}|", true);
    if (name.equals("APT_PLACE")) return new MyAptPlaceField();
    if (name.equals("CITY")) return new CityField("[ A-Z]+");
    if (name.equals("GPS1")) return new MyGPSField(1);
    if (name.equals("GPS2")) return new MyGPSField(2);
    if (name.equals("UNIT")) return new UnitField("\\d\\d- *(.*)", true);
    if (name.equals("CH")) return new ChannelField("TAC *\\d+", true);
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ST")) return new StateField("GA", true);
    return super.getField(name);
  }

  private static final Pattern APT_PLACE_PTN = Pattern.compile("(\\d+[A-Za-z])\\b *(.*)");
  private class MyAptPlaceField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PLACE_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
        field = match.group(2);
      }
      data.strPlace = field;
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE";
    }
  }

  private static final Pattern GPS_PTN = Pattern.compile("(.*?) [NSEW]");
  private class MyGPSField extends GPSField {
    public MyGPSField(int type) {
      super(type);
    }

    @Override
    public void parse(String field, Data data) {
      Matcher match = GPS_PTN.matcher(field);
      if (!match.matches()) abort();
      super.parse(match.group(1).replace("%", ""), data);
    }
  }

  private static final Pattern CH_INFO_PTN = Pattern.compile("(TAC[- ]*\\d+)\\b", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends BaseInfoField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CH_INFO_PTN.matcher(field);
      if (match.lookingAt()) {
        data.strChannel = match.group();
        field = field.substring(match.end()).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return "CH " + super.getFieldNames();
    }
  }
}
