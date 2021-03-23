package net.anei.cadpage.parsers.ZCASK;

import net.anei.cadpage.parsers.MsgInfo.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;

public class ZCASKPECCParser extends FieldProgramParser {

  public ZCASKPECCParser() {
    super(CITY_LIST, "", "SK",
          "( CALLADDR CITY? " +
          "| ADDR CITY? CALL! " +
          "| CALL! CALL/CS+? ADDR! CITY? " +
          ") INFO/CS+");
  }

  @Override
  public String getFilter() {
    return "@alerts.pa911.com";
  }

  @Override
  public String getLocName() {
    return "PECC";
  }

  private static final Pattern SUBJECT_TRAIL_PTN = Pattern.compile("(?: +ALERT)?[; ]*$", Pattern.CASE_INSENSITIVE);
  private static final Pattern LEAD_JUNK_PTN = Pattern.compile("^ *(?:FD)?[-;, ]*");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {

    int pt = subject.indexOf(',');
    if (pt >= 0) subject = subject.substring(0,pt).trim();

    if (subject.isEmpty()) return false;
    subject = SUBJECT_TRAIL_PTN.matcher(subject).replaceFirst("");
    if (!body.toUpperCase().startsWith(subject.toUpperCase())) return false;
    data.strSource = subject;
    body = body.substring(subject.length());
    body = LEAD_JUNK_PTN.matcher(body).replaceFirst("");

    parseAddress(StartType.START_ADDR, FLAG_ONLY_CITY, subject, data);
    return parseFields(body.split(","), data);
  }

  @Override
  public String getProgram() {
    return "SRC CITY? " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALLADDR")) return new MyCallAddressField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }

  private static final Pattern CALL_ADDR_PTN =
      Pattern.compile("((?:RESPOND|REPSOND|STAND DOWN|STRUCTURE)\\b.*?)(?: AT |(?=\\b(?:[NS][EW] )?\\d)) *(.*?)(?: IN (.*))?", Pattern.CASE_INSENSITIVE);

  private class MyCallAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      Matcher match = CALL_ADDR_PTN.matcher(field);
      if (!match.matches()) return false;
      data.strCall = match.group(1).trim();
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

  private static final Pattern ADDR_PTN = Pattern.compile("IN FRONT OF .*|.*\\d.*", Pattern.CASE_INSENSITIVE);
  private static final Pattern ADDR_CITY_PTN = Pattern.compile("(.*) in (.*)", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {

      // Very!!! expansive definition of what constitutes an address
      // Anything with a number counts
      if (! ADDR_PTN.matcher(field).matches() && ! parseAddress(StartType.START_ADDR, FLAG_NO_CITY, field).isValid()) return false;
      if (field.toUpperCase().endsWith("ALARM")) return false;

      // See if there is an identified city here
      Matcher match = ADDR_CITY_PTN.matcher(field);
      if (match.matches()) {
        field =  match.group(1).trim();
        data.strCity = match.group(2).trim();
      }

      parseAddress(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final String[] CITY_LIST = new String[] {
      "ARCHERWILL",
      "ASQUITH",
      "BALGONIE",
      "BATTLEFORD",
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
      "KRONAU",
      "LAJORD",
      "LAURA",
      "NORTH BATTLEFORD",
      "PIKE LAKE",
      "PILOT BUTTE",
      "PORTER",
      "PRINCE",
      "PRONGUA",
      "REGINA",
      "RICHARDSON",
      "ROSETOWN",
      "ROSE VALLEY",
      "SAINT-FRONT",
      "SCRIP",
      "SOVERIEGN",
      "TOBIN LAKE",
      "VADE",
      "VANSCOY",
      "WHITE CITY",
      "ZEALANDIA"
  };
}
