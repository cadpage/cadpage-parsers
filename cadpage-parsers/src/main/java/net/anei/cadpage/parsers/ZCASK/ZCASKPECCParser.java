package net.anei.cadpage.parsers.ZCASK;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class ZCASKPECCParser extends FieldProgramParser {

  public ZCASKPECCParser() {
    super(CITY_LIST, "", "SK",
          "( SELECT/NEW DEAR ( SRC_CALL ( ADDR | PLACE ADDR | ADDR ) APT? CITY? INFO/N+? EOD " +
                            "| INFO/G EOD " +
                            ") " +
          "| ( CALLADDR CITY? " +
            "| CALLADDR2 " +
            "| ADDR CITY? CALL! " +
            "| RESPOND ( ADDR CITY? CALL/S PLACE " +
                      "| CALL/S PLACE? ADDR! CITY? " +
                      ") " +
            "| CALL! CALL/CS+? ADDR! CITY? " +
            ") INFO/CS+ " +
          ")");
  }

  @Override
  public String getFilter() {
    return "@alerts.pa911.com,@athocalerts.com";
  }

  @Override
  public String getLocName() {
    return "PECC";
  }

  private static final Pattern DELIM = Pattern.compile("[ .]*\n+\\s*");
  private static final Pattern SUBJECT_TRAIL_PTN = Pattern.compile("(?: +ALERT)?[; ]*$", Pattern.CASE_INSENSITIVE);
  private static final Pattern SUBJECT_FD_PTN = Pattern.compile(" +(?:FD|FIRE|FIRE/RESCUE|RESCUE|RESCUE/FD)", Pattern.CASE_INSENSITIVE);
  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("^ *(?:FD|FIRE|FIRE/RESCUE|RESCUE|RESCUE/FD)?[-;, ]*", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = subject.indexOf(',');
    if (pt >= 0) subject = subject.substring(0,pt).trim();

    if (subject.isEmpty()) return false;
    if (body.contains("\n")) {
      setSelectValue("NEW");
      return parseFields(DELIM.split(body), data);

    } else {
      setSelectValue("OLD");
      subject = SUBJECT_TRAIL_PTN.matcher(subject).replaceFirst("");
      Matcher match = SUBJECT_FD_PTN.matcher(subject);
      if (match.find()) subject = subject.substring(0, match.start());
      if (!body.toUpperCase().replace("ASSINIBOAI", "ASSINIBOIA").replace("ASSINBOIA", "ASSINIBOIA").startsWith(subject.toUpperCase())) return false;
      data.strSource = subject;
      body = body.substring(subject.length());
      body = LEAD_JUNK_PTN.matcher(body).replaceFirst("");

      parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, subject, data);
      if (!parseFields(body.split(","), data)) return false;
      if (data.strCity.equals("SC")) data.strCity = "Swift Current";
      return true;
    }
  }

  @Override
  public String getProgram() {
    return "SRC CITY? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DEAR")) return new SkipField("Dear .*", true);
    if (name.equals("SRC_CALL")) return new MySourceCallField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT"))  return new AptField("(?i)APT +(.*)", true);
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("EOD")) return new SkipField("Please respond with.*|Thank you.*", true);

    if (name.equals("RESPOND")) return new CallField("(?i:RESPOND|STAND DOWN)", true);
    if (name.equals("CALLADDR")) return new MyCallAddressField();
    if (name.equals("CALLADDR2")) return new MyCallAddress2Field();
    return super.getField(name);
  }

  private static final Pattern SRC_CALL_PTN = Pattern.compile("(.*) Respond (.*)");

  private class MySourceCallField extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = SRC_CALL_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strSource = match.group(1).trim();
      data.strCall = match.group(2).trim();
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "SRC CALL";
    }
  }

  private static final Pattern CALL_ADDR_PTN =
      Pattern.compile("((?:RESPOND|REPSOND|STAND DOWN|STRUCTURE)\\b.*?)(?: AT |(?=\\b(?:[NS][EW] )?\\d)) *(.*?)(?: IN (.*?))?(?: FOR (.*))?", Pattern.CASE_INSENSITIVE);

  private class MyCallAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_ADDR_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCall = append(match.group(1).trim(), " ", getOptGroup(match.group(4)));
      parseAddress(match.group(2).trim(), data);
      String city = match.group(3);
      if (city != null) data.strCity = city;
      return true;
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY";
    }
  }

  private static final Pattern NOT_ADDR_PTN = Pattern.compile("\\d (?:VEH|CAR).*|MUTUAL AID.*", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_PTN = Pattern.compile(".*\\d.*", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      field = splitPlaceAddress(field, data, false);
      if (field ==  null) return false;
      parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {

      field = splitPlaceAddress(field, data, true);
      parseAddress(field, data);
    }

    private String splitPlaceAddress(String field, Data data, boolean force) {
      // See if this contains a place field
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        String fld1 = field.substring(0,pt).trim();
        String fld2 = field.substring(pt+3).trim();
        if (isValidAddress(fld1)) {
          data.strPlace = append(data.strPlace, " - ", fld2);
          return fld1;
        } else if (isValidAddress(fld2)) {
          data.strPlace = append(data.strPlace, " - ", fld1);
          return fld2;
        } else if (!force) {
          return null;
        }
      } else {
        if (!force && !isValidAddress(field)) field = null;
      }
      return field;
    }

    private boolean isValidAddress(String field) {
      // Very!!! expansive definition of what constitutes an address
      // Anything with a number counts, except for multi car colision reports
      if (NOT_ADDR_PTN.matcher(field).matches()) return false;
      if (! ADDR_PTN.matcher(field).matches() && ! parseAddress(StartType.START_ADDR, FLAG_NO_CITY, field).isValid()) return false;
      return !field.toUpperCase().endsWith("ALARM");
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }

  private static final Pattern IN_CITY_PTN = Pattern.compile("IN +(.*)", Pattern.CASE_INSENSITIVE);

  private class MyCityField extends CityField {
    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = IN_CITY_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      return super.checkParse(field, data);
    }
  }

  private class MyCallAddress2Field extends Field {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!isLastField()) return false;
      Result res = parseAddress(StartType.START_CALL, FLAG_START_FLD_REQ | FLAG_ANCHOR_END, field);
      if (!res.isValid()) return false;
      res.getData(data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return "CALL ADDR APT CITY";
    }
  }

  private static final String[] CITY_LIST = new String[] {
      "AIR RONGE",
      "ARCHERWILL",
      "ASQUITH",
      "ASSINIBOIA",
      "BALGONIE",
      "BATTLEFORD",
      "BUENA VISTA",
      "CARROT RIVER",
      "CUVIER",
      "DAVIN",
      "DELISLE",
      "DELMAS",
      "DONAVAN",
      "EMERALD PARK",
      "FISKE",
      "HARRIS",
      "HENDON",
      "HERSCHEL",
      "HIGHGATE",
      "KATHRINTAL COLONY",
      "KEVINGTON",
      "KITSAKIE IR",
      "KRONAU",
      "LA RONGE",
      "LAJORD",
      "LAURA",
      "MEADOW LAKE",
      "MONTROSE",
      "NORTH BATTLEFORD",
      "PIKE LAKE",
      "PILOT BUTTE",
      "PORTER",
      "PRINCE",
      "PRONGUA",
      "REGINA",
      "REGINA BEACH",
      "RICHARDSON",
      "ROSETOWN",
      "ROSE VALLEY",
      "ROSTHERN",
      "SAINT-FRONT",
      "SCRIP",
      "SOVERIEGN",
      "SUCKER RIVER",
      "TOBIN LAKE",
      "VADE",
      "VANSCOY",
      "WHITE CITY",
      "ZEALANDIA"
  };
}
