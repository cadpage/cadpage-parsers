package net.anei.cadpage.parsers.WA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.HtmlProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class WASnohomishCountyCParser extends HtmlProgramParser {

  public WASnohomishCountyCParser() {
    super("SNOHOMISH COUNTY", "WA",
          "UNIT PRI! RESPOND_TO:ADDR! ROOM:APT! INFO! INFO/N+ FOR:FOR_INFO! INFO/N+ NAME:NAME!");
  }
  
  private static final Pattern SUBJECT_PATTERN
    = Pattern.compile("Trip Notification TRIP-(\\d{5})");
  
  @Override
  protected boolean parseHtmlMsg(String subject, String body, Data data) {
    Matcher m = SUBJECT_PATTERN.matcher(subject);
    if (!m.matches()) return false;
    data.strCallId = m.group(1);
    body = body.replace("\n", "<br/>");
    return super.parseHtmlMsg(subject, body, data);
  }

  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("UNIT")) return new UnitField("([A-Z0-9]+) YOU HAVE A CALL.*", true);
    if (name.equals("PRI")) return new PriorityField("Priority (\\d+).*", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("FOR_INFO")) return new ForInfoField();
    if (name.equals("NAME")) return new MyNameField();
    return super.getField(name);
  }
  
  private static final Pattern STATE_ZIP_PATTERN = Pattern.compile("([A-Z]{2})?\\b *\\b(\\d{5})?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int ndx = field.lastIndexOf(" - ");
      if (ndx > -1) {
        data.strPlace = field.substring(0, ndx).trim();
        field = field.substring(ndx+3).trim();
      }
      Parser p = new Parser(field);
      String city = p.getLastOptional(',');
      Matcher m = STATE_ZIP_PATTERN.matcher(city);
      if (m.matches()) {
        data.strState = getOptGroup(m.group(1));
        data.strCity = getOptGroup(m.group(2));
        city = p.getLastOptional(',');
      }
      if (city.length() > 0) data.strCity = city;
      super.parse(p.get(), data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE " + super.getFieldNames()+" CITY ST";
    }
  }
  
  private static final Pattern UNKNOWN_PTN = Pattern.compile("(?:[ \\w]+: *)(?:Unknown|)");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (UNKNOWN_PTN.matcher(field).matches()) return;
      super.parse(field, data);
    }
  }
  
  private static final Pattern APT_PTN = Pattern.compile("(?:ROOM|RM|APT|SUITE|LOT)?[ #]*(.*)", Pattern.CASE_INSENSITIVE);
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = APT_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
      super.parse(field, data);
    }
  }
  
  private class ForInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Unknown")) return;
      data.strSupp = append(field, "\n", data.strSupp);
    }
  }
  
  private class MyNameField extends NameField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Unknown")) return;
      super.parse(field, data);
    }
  }
}
