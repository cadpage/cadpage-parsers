package net.anei.cadpage.parsers.WI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Eau Claire, WI
 */
public class WIEauClaireParser extends FieldProgramParser {
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("Case# (\\d\\d-\\d+)");
  
  public WIEauClaireParser() {
    super("EAU CLAIRE", "WI",
           "TIME CITY ADDR CALL INFO+");
  }
  
  @Override
  public String getFilter() {
    return "tfddispatch@att.net,messaging@iamresponding.com";
  }
  
  @Override
  public boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf('@');
    if (pt < 0) return false;
    String sub = body.substring(0,pt).trim();
    body = body.substring(pt+1).trim();
    
    if (!subject.startsWith("Case#")) subject = sub;
    if (sub.length() > 0 && !sub.equals(subject)) return false;
    
    if (subject.length() > 0) {
      Matcher  match = SUBJECT_PTN.matcher(subject);
      if (!match.matches()) return false;
      data.strCallId = match.group(1);
    }
    
    body = body.replace("\n", " ").replaceAll("  +", " ");
    return parseFields(body.split("//"), data);
  }
  
  private static final DateFormat TIME_FMT = new SimpleDateFormat("hh:mm:ss aa");
  private class MyTimeField extends TimeField {
    
    public MyTimeField() {
      setPattern("\\d\\d?:\\d\\d:\\d\\d(?: [AP]M)?", true);
    }
    
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 8) {
        data.strTime = field;
      } else {
        setTime(TIME_FMT, field, data);
      }
    }
  }
  
  private static final Pattern CITY_COUNTY_PTN = Pattern.compile("(.*)\\((.*)\\)");
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_COUNTY_PTN.matcher(field);
      String county = "";
      if (match.find()) {
        field = match.group(1).trim();
        county = match.group(2).trim();
      }
      if (field.toUpperCase().endsWith(" TWP")) {
        field = field.substring(0, field.length()-4).trim();
      }
      field = append(field, ", ", county);
      super.parse(field, data);
    }
  }
  
  private static final Pattern BLOCK_PTN = Pattern.compile("\\bBLOCK\\b", Pattern.CASE_INSENSITIVE);
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      field = BLOCK_PTN.matcher(field).replaceAll("BLK");
      super.parse(field, data);
    }
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new MyTimeField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  @Override
  public String getProgram() {
    return "ID " + super.getProgram();
  }
}