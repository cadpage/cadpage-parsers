package net.anei.cadpage.parsers.dispatch;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class DispatchA55Parser extends FieldProgramParser {
  
  public DispatchA55Parser(String defCity, String defState) {
    super(defCity, defState,
          "Call_Type:CALL/SDS? Common_Place:PLACE? Address:ADDR? Apartment:APT? City_State_County:CITY? Disposition:SKIP Lat/Long:GPS Zip:ZIP Subgrid_Grid_District:MAP Notes:INFO/N+");
  }
  
  private static final Pattern SUBJECT_PTN = Pattern.compile("(?:DISPATCH ALERT|OUT TAPS)[- ]*", Pattern.CASE_INSENSITIVE);

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    
    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.lookingAt()) subject = subject.substring(match.end());
    data.strCall = subject;
    int pt = body.indexOf("\n_____");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!parseFields(body.split("\n"), data)) return false;
    return data.strAddress.length() > 0 || data.strSupp.length() > 0;
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static final Pattern CITY_ST_PTN = Pattern.compile("([A-Z][ A-Za-z]+), *([A-Z]{2}), *[A-Z]{2}");
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = CITY_ST_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strState = match.group(2);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY ST";
    }
  }
  
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() > 0) return;
      data.strCity = field;
    }
  }
  
  private static final Pattern INFO_CHANNEL_PTN = Pattern.compile("(.*)\\b(OPS *\\d+)", Pattern.CASE_INSENSITIVE);
  private class MyInfoField extends InfoField {
    
    @Override
    public void parse(String field, Data data) {
      Matcher match = INFO_CHANNEL_PTN.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        data.strChannel = match.group(2).toUpperCase();
      }
    
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "INFO CH";
    }
  }
}
