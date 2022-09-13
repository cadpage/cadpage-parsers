package net.anei.cadpage.parsers.NC;


import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;



public class NCJohnstonCountyParser extends DispatchOSSIParser {

  private static final Pattern ENROUTE_PTN = Pattern.compile("CAD:([A-Z0-9]+),Enroute,.*");

  private String lastCrossPlaceFld;

  public NCJohnstonCountyParser() {
    super(CITY_CODES, "JOHNSTON COUNTY", "NC",
          "( UNIT ENROUTE ADDR CITY CALL_CODE! " +
          "| ( CALL ADDR/Z CITY! X_PLACE_INFO+? DATETIME UNIT " +
            "| CH? SRC CODE? CALL_CODE ADDR X_PLACE_INFO+? CITY/Y ( DATETIME | NAME PLACE? DATETIME ) UNIT! " +
            ") " +
          ") END");
  }

  @Override
  public String getFilter() {
    return "CAD@johnstonnc.com,duane1409@gmail.com,93001";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }

  @Override
  public boolean parseMsg(String body, Data data) {

    if (!body.startsWith("CAD:")) body = "CAD:" + body;

    boolean enroute = ENROUTE_PTN.matcher(body).matches();
    if (enroute) body = body.replace(',', ';');

    lastCrossPlaceFld = "";
    if (!super.parseMsg(body, data)) return false;
    if (!data.strCode.isEmpty()) {
      String call = CALL_CODES.getCodeDescription(data.strCode.replace("-", ""));
      if (call != null) data.strCall = call;
    }
    if (enroute) {
      data.msgType = MsgType.GEN_ALERT;
      data.strCall = append("enroute", " - ",  data.strCall);
    }
    return true;
  }

  @Override
  protected Field getField(String name) {
    if (name.equals("ENROUTE")) return new SkipField("Enroute", true);
    if (name.equals("CALL_CODE")) return new MyCallCodeField();
    if (name.equals("CH")) return new ChannelField("OPS.*|.*FR|VPR.*|2ND", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{2,5}");
    if (name.equals("NAME")) return new MyNameField();
    if (name.equals("X_PLACE_INFO")) return new MyCrossPlaceInfoField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    return super.getField(name);
  }

  private static final Pattern CODE_CALL_PTN = Pattern.compile("(.*?) +(\\d{1,2}-?[A-Z]-?\\d{1,2}(?:-?[A-Z])?)");
  private class MyCallCodeField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CODE_CALL_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1);
        data.strCode = match.group(2);
      }
      data.strCall = field;
    }

    @Override
    public String getFieldNames() {
      return "CALL CODE";
    }
  }

  private class MyNameField extends NameField {

    @Override
    public void parse(String field, Data data) {
      if (field.endsWith(" CO") && data.strCity.isEmpty()) {
        data.strCity = field + "UNTY";
      } else {
        super.parse(field, data);
      }
    }

    @Override
    public String getFieldNames() {
      return "CITY NAME";
    }
  }

  private static final Pattern NS_CROSS_PTN = Pattern.compile("(.*?) *\\(S\\) *(.*?)(?: *\\(N\\))?|(?:EMS)?DIST:.*");
  private static final Pattern APT_PTN = Pattern.compile("(?:APT|LOT|RM|ROOOM) *(.*)");
  private class MyCrossPlaceInfoField extends Field {
    @Override
    public void parse(String field, Data data) {

      // see if it has the expected (N)..(S) pattern
      // Or looks like a decimal number
      // If it is, put it in the place or cross street field
      // depending on whether or not it looks like an address
      Matcher match = NS_CROSS_PTN.matcher(field);
      if (match.matches()) {
        String tmp = match.group(2);
        if (tmp != null) {
          String prefix = match.group(1);
          if (prefix.length() > 0) {
            if (NUMERIC.matcher(prefix).matches()) {
              data.strApt = append(data.strApt, "-", match.group(1));
            } else {
              data.strPlace = append(data.strPlace, " - ", prefix);
            }
          }
          field = tmp;
        }
        data.strPlace = append(data.strPlace, " - ", field);
        return;
      }

      // If duplicate of last field, skip it
      if (field.equals(lastCrossPlaceFld)) return;
      lastCrossPlaceFld = field;

      // See if it looks like a cross street or a place name
      match = APT_PTN.matcher(field);
      if (match.matches()) {
        data.strApt = append(data.strApt, "-", match.group(1));
      }
      else if (field.contains("70 BUS HWY") || isValidAddress(field)) {
        data.strCross = append(data.strCross, " / ", field);
      } else {
        data.strSupp = append(data.strSupp, " / ", cleanWirelessCarrier(field));
      }
    }

    @Override
    public String getFieldNames() {
      return "APT PLACE X INFO";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("\\d\\d/[\\d:/ ]*");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // Looser pattern match standard than the default date/time field
      // if we only have the first 3 characters of what looks like a date/time
      // field, that is good enough
      if (!DATE_TIME_PTN.matcher(field).matches()) return false;
      parse(field, data);
      return true;
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    return US70BUSHWY_PTN.matcher(addr).replaceAll("70 BUS");
  }
  Pattern US70BUSHWY_PTN = Pattern.compile("\\b70 BUS HWY\\b", Pattern.CASE_INSENSITIVE);

  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "ANGI", "ANGIER",
      "ARCH", "ARCHER LODGE",
      "ARCL", "ARCHER LODGE",
      "BENS", "BENSON",
      "CLAY", "CLAYTON",
      "DUNN", "DUNN",
      "ERWN", "ERWIN",
      "FOUR", "FOUR OAKS",
      "GARN", "GARNER",
      "HARN", "HARNETT COUNTY",
      "KENL", "KENLY",
      "MICR", "MICRO",
      "MIDD", "MIDDLESEX",
      "NEWT", "NEWTON GROVE",
      "OUTS", "",
      "PIKE", "PIKEVILLE",
      "PINE", "PINE LEVEL",
      "PRIN", "PRINCETON",
      "RALE", "RALEIGH",
      "SELM", "SELMA",
      "SMIT", "SMITHFIELD",
      "WAKE", "WAKE COUNTY",
      "WAYN", "WAYNE COUNTY",
      "WISM", "WILSON'S MILLS",
      "WEND", "WENDELL",
      "WILL", "WILLOW SPRING",
      "WILS", "WILSON",
      "WISM", "WILSON'S MILLS",
      "ZEBU", "ZEBULON"
  });

  private static final CodeTable CALL_CODES = new StandardCodeTable();
}