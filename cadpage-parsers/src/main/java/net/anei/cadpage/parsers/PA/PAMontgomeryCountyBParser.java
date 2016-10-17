package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA8Parser;




public class PAMontgomeryCountyBParser extends DispatchA8Parser {

  public PAMontgomeryCountyBParser() {
    super(PAMontgomeryCountyParser.CITY_CODES, "MONTGOMERY COUNTY", "PA");
  }
  
  @Override
  public String getFilter() {
    return "Beryl0908@comcast.net,adi53@comcast.net.BHVFC@fdcms.info,worcesterfd@comcast.net";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField();  // Sometimes time field will not validate properly :(  
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d\\d|\\d{5}-20", true); // Accept garbled date format :(
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private final class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n+")) {
        line = line.trim();
        Matcher match = MARKER_PTN.matcher(line);
        if (match.matches()) line = match.group(1);
        
        if (PHONE_PTN.matcher(line).matches()) {
          data.strPhone = append(data.strPhone, " / ", line);
          continue;
        }
        
        if (GPS_PTN.matcher(line).matches()) {
          setGPSLoc(line, data);
          continue;
        }
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
    
    @Override
    public String getFieldNames() {
      return "PHONE GPS INFO";
    }
  }
  private static final Pattern MARKER_PTN = Pattern.compile("^(?:SPECIAL ADDRESS COMMENT:|\\+) *(.*?)");
  private static final Pattern PHONE_PTN = Pattern.compile("\\d{3}[-\\.]?\\d{3}[-\\.]?\\d{4}");
  private static final Pattern GPS_PTN = Pattern.compile("[+-]\\d{3}\\.\\d{6} [+-]\\d{3}\\.\\d{6}");
  
  @Override
  protected void parseSpecialField(String field, Data data) {
    boolean grab = false;
    for (String line : field.split("\n+")) {
      line = line.trim();
      if (line.equals("Additional Info:")) {
        grab = true;
      } else if (grab) {
        if (SKIP_PTN.matcher(line).matches()) continue;
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
    if (data.strSupp.endsWith("\nPrior Events:")) {
      data.strSupp = data.strSupp.substring(0,data.strSupp.length()-14).trim();
    }
  }
  private static final Pattern SKIP_PTN = Pattern.compile("[- ]*|\\*\\*\\* (?:ADDITIONAL INFO NOT ON FILE FOR THIS LOCATION|NOT FOUND) \\*\\*\\*");
  
  @Override
  protected String specialFieldNames() {
    return "INFO";
  }
}
	