package net.anei.cadpage.parsers.SC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class SCOconeeCountyBParser extends FieldProgramParser {

  public SCOconeeCountyBParser() {
    super("OCONEE COUNTY", "SC",
          "ID CALL CALL2/SLS+? ADDR INFO! INFO+");
  }

  @Override
  public String getFilter() {
    return "zuercher@oconeelaw.com,no-reply@zuercherportal.com";
  }

  private static final Pattern DELIM = Pattern.compile(" /(?= |$)");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("Call Dispatched:")) return false;
    return super.parseFields(DELIM.split(body),  data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL2")) return new CallField("TIA|Stroke");
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ADDR_CITY_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?");
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
      data.strCity = city;
      super.parse(p.get(), data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY ST";
    }
  }

  private static final Pattern INFO_DATE_TIME_PTN = Pattern.compile("(\\d\\d/\\d\\d/\\d\\d) (\\d\\d:\\d\\d:\\d\\d) - (.*)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      String connect = " / ";
      for (String part : field.split("; ")) {
        part = part.trim();
        if (part.length() == 0) continue;
        Matcher match = INFO_DATE_TIME_PTN.matcher(part);
        if (match.matches()) {
          data.strDate = match.group(1);
          data.strTime = match.group(2);
          part = match.group(3);
        }

        data.strSupp = append(data.strSupp, connect, part);
        connect = "\n";
      }
    }

    @Override
    public String getFieldNames() {
      return "DATE TIME INFO";
    }
  }
}
