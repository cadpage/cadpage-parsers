package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class PAArmstrongCountyCParser extends FieldProgramParser {

  public PAArmstrongCountyCParser() {
    super("ARMSTRONG COUNTY", "PA",
          "CALL! CALL+ CALL_INFO_AND_RADIO_TAC:CH! ADDRESS:ADDRCITY! Latitude:GPS1! Longitude:GPS2! " +
                 "( ADD'L_LOCALE_INFO:PLACE! PLACE! PLACE+? DATETIME! | ) " +
                "CFS_NUMBER:ID! FIRE_QUAD:MAP! EMS_DISTRICT/L! INTERSECTION:X! X/L UNITS:UNIT! END");
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public String getFilter() {
    return "911dispatch@co.armstrong.pa.us";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile(" *(?:(?<!\\b\\d?\\d)/|/(?!\\d)) *");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Dispatch")) {
      if (!body.startsWith("Dispatch:")) return false;
      body = body.substring(9).trim();
    }
    body = body.replace(" ADD'L ", "/ADD'L ").replace('\n', ' ');
    return parseFields(DELIM.split(body), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("CH")) return new MyCallChannelField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("PLACE")) return new MyPlaceField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d?/\\d\\d?/\\d{4} \\d\\d?:\\d\\d:\\d\\d", true);
    if (name.equals("EMS_DISTRICT")) return new MapField("EMS DISTRICT\\b *(.*)", true);
    return super.getField(name);
  }

  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.contains(field)) return;
      data.strCall = append(data.strCall, "/", field);
    }
  }

  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strCity = stripFieldEnd(data.strCity, " BORO");
      if (data.strCity.equals("KITTG")) data.strCity = "KITTANNING";
    }
  }

  private static final Pattern CALL_CH_PTN1 = Pattern.compile("(.*?)[- ]+([A-Z]{2,4} ?\\d|BUTLE FIRE|FIRE ADMIN|FIRE SOUTH)");
  private static final Pattern CALL_CH_PTN2 = Pattern.compile("([A-Z]{2,4} ?\\d)[- ]+(.*?)");
  private class MyCallChannelField extends Field {
    @Override
    public void parse(String field, Data data) {
      int ndx = 0;
      while (true) {
        String next = getRelativeField(++ndx);
        if (next.startsWith("ADDRESS:")) break;
        field = append(field, "/", next);
        if (ndx > 3) abort();
      }

      if (field.isEmpty()) return;
      Matcher match = CALL_CH_PTN1.matcher(field);
      if (match.matches()) {
        data.strCall = append(data.strCall, " - ", match.group(1));
        data.strChannel = match.group(2);
      } else if ((match = CALL_CH_PTN2.matcher(field)).matches()) {
        data.strChannel = match.group(1);
        data.strCall = append(data.strCall, " - ", match.group(2));
      } else {
        data.strCall = append(data.strCall, " - ", field);
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL CH";
    }
  }

  private class MyPlaceField extends PlaceField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("http:") || field.equals("https:")) return;
      if (field.startsWith("http:") || field.startsWith("https:")) {
        data.strInfoURL = field;
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "URL PLACE";
    }
  }
}
