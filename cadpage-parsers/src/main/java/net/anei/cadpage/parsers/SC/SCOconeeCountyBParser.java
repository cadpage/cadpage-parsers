package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCOconeeCountyBParser extends FieldProgramParser {

  public SCOconeeCountyBParser() {
    super("OCONEE COUNTY", "SC",
          "ID CALL CALL2/SLS+? ADDR ADDR2? INFO! INFO/N+? ID/L Unit:UNIT? Map:GPS? END");
  }

  @Override
  public String getFilter() {
    return "zuercher@oconeelaw.com,no-reply@zuercherportal.com";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern DELIM = Pattern.compile(" /(?= |$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    subject = stripFieldStart(subject, "[DISPATCH] ");
    if (!subject.startsWith("Call Dispatched:")) return false;
    return super.parseFields(DELIM.split(body),  data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("Behavioral|Contusion|No Transport|Stabbing|Strain|Stroke|TIA");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("ADDR2")) return new MyAddress2Field();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("ID")) return new IdField("[A-Z]+\\d\\d-\\d{6}", true);
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?");
  private static final Pattern ADDR_GPS_PTN = Pattern.compile("[-+]?\\d{2,3}\\.\\d{6,}");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher match = ADDR_CITY_ZIP_PTN.matcher(city);
      if (match.matches()) {
        data.strState = match.group(1);
        city = p.getLastOptional(',');
      }
      String addr = p.get();
      if (ADDR_GPS_PTN.matcher(city).matches()) {
        addr = append(addr, ", ", city);
      } else {
        data.strCity = city;
      }
      super.parse(addr, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }

  private static final Pattern ADDRESS_CITY_ST_PTN = Pattern.compile(".*, *[A-Z ]+, *SC(?: +\\d{5})?" );
  private class MyAddress2Field extends MyAddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      if (!data.strCity.isEmpty()) return false;
      if (field.equals("None") || INFO_DATE_TIME_PTN.matcher(field).matches()) return false;
      if (!ADDRESS_CITY_ST_PTN.matcher(field).matches()) {
        if (checkAddress(data.strAddress) != STATUS_STREET_NAME &&
            checkAddress(new Parser(field).get(',')) != STATUS_STREET_NAME) return false;
      }
      field = data.strAddress + " / " + field;
      data.strAddress = "";
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) - (.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("None")) return;
      String connect = " / ";
      for (String part : field.split("; ")) {
        part = part.trim();
        if (part.length() == 0) continue;
        Matcher match = INFO_DATE_TIME_PTN.matcher(part);
        if (match.matches()) part = match.group(3);
        part = stripFieldStart(part, "CFS Log note -");

        data.strSupp = append(data.strSupp, connect, part);
        connect = "\n";
      }
    }
  }
}
