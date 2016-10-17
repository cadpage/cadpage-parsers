package net.anei.cadpage.parsers.PA;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;



public class PABlairCountyAParser extends FieldProgramParser {
  
  public PABlairCountyAParser() {
    super("BLAIR COUNTY", "PA",
           "UNIT ADDRCITY/SXa X/Z+? ( DATETIME! INFO+? GPS END | PDATETIME! END )");
  }
  
  @Override
  public String getFilter() {
    return "Blair Alerts,alerts@blairalerts.com";
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.length() == 0) return false;
    data.strCall = subject;
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public String getProgram() {
    return "CALL " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("DATETIME")) return new DateTimeField(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa"), true);
    if (name.equals("PDATETIME")) return new PartDateTimeField();
    if (name.equals("GPS")) return new MyGPSField();
    return super.getField(name);
  }
  
  private static final Pattern ADDR_AT_SIGN_PTN = Pattern.compile(" *@ *");
  private static final Pattern CITY_BORO_PTN =  Pattern.compile(" +(?:BORO|[A-Z]+ +COUNTY)$", Pattern.CASE_INSENSITIVE);
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      super.parse(field, data);
      data.strAddress = ADDR_AT_SIGN_PTN.matcher(data.strAddress).replaceAll(" & ").trim();
      Matcher match = CITY_BORO_PTN.matcher(data.strCity);
      if (match.find()) data.strCity = data.strCity.substring(0,match.start());
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  /**
   * Complicated class whose job is to confirm that the field passed to it is a valid truncated date/time
   * and to call abort() if it is not
   */
  private class PartDateTimeField extends SkipField {
    
    private String field;
    private int pos;
    
    @Override
    public void parse(String field, Data data) {
      this.field = field;
      this.pos = 0;
      
      checkDigit();
      checkDigit(true);
      checkNextChar("/");
      checkDigit();
      checkDigit(true);
      checkNextChar("/");
      checkDigit();
      checkDigit();
      checkDigit();
      checkDigit();
      checkNextChar(" ");
      checkDigit();
      checkDigit(true);
      checkNextChar(":");
      checkDigit();
      checkDigit();
      checkNextChar(":");
      checkDigit();
      checkDigit();
      checkNextChar(" ");
      checkNextChar("AP");
      checkNextChar("M");
      checkEnd();
    }
    
    private void checkDigit() {
      checkDigit(false);
    }
    
    private void checkDigit(boolean optional) {
      checkNextChar("0123456789", optional);
    }
    
    private void checkNextChar(String validChars) {
      checkNextChar(validChars, false);
    }
    
    private void checkNextChar(String validChars, boolean optional) {
      if (pos >= field.length()) return;
      char chr = field.charAt(pos);
      if (validChars.indexOf(chr) >= 0) pos++;
      else if (!optional) abort();
    }
    
    private void checkEnd() {
      if (pos < field.length()) abort();
    }
  }
  
  private static final String GPS_PREFIX = "http://maps.google.com/?q=";
  private class MyGPSField extends GPSField {
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (field.startsWith(GPS_PREFIX)) {
        super.parse(field.substring(GPS_PREFIX.length()).trim(), data);
        return true;
      }
      return field.length() > 0 && GPS_PREFIX.startsWith(field);
    }
  }
}
