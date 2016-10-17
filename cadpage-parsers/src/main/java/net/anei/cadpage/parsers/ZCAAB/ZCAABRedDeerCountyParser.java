package net.anei.cadpage.parsers.ZCAAB;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchA51Parser;
/**
 * Red Deer County, AB, CA
 */
public class ZCAABRedDeerCountyParser extends DispatchA51Parser {
  
  private static final Pattern CLEAN_CITY_PTN = Pattern.compile("(.*?) +RURAL(?: AREA)?", Pattern.CASE_INSENSITIVE);
  
  public ZCAABRedDeerCountyParser() {
    this("RED DEER COUNTY");
  }
  
  protected ZCAABRedDeerCountyParser(String defCity) {
    super(defCity, "AB");
  }
  
  @Override
  public String getAliasCode() {
    return "ZCABRedDeerCounty";
  }

  @Override
  public String getFilter() {
    return "@reddeer.ca,messaging@iamresponding.com,@cremonafire.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_CR_CRES;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CAD Message") && !subject.equals("Incident Message")) {
      data.strSource = subject;
    }
    
    if (!super.parseMsg(body,  data)) return false;
    
    Matcher match = CLEAN_CITY_PTN.matcher(data.strCity);
    if (match.matches()) data.strCity = match.group(1);
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
 
  @Override
  public String adjustMapAddress(String sAddress) {
    sAddress = RNG_PATTERN.matcher(sAddress).replaceAll("Range");
    sAddress = RANGE_RD_DASH_PTN.matcher(sAddress).replaceAll("$1$2");
    return sAddress;
  }
  private static final Pattern RNG_PATTERN = Pattern.compile("\\bRng\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern RANGE_RD_DASH_PTN = Pattern.compile("\\b(RANGE +RD +\\d+)-(\\d+)\\b", Pattern.CASE_INSENSITIVE);

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return HWY_NN_PTN.matcher(sAddress).replaceAll("ALBERTA $1");  
  }
  private static final Pattern HWY_NN_PTN = Pattern.compile("\\bHWY +(\\d+)\\b", Pattern.CASE_INSENSITIVE);
}
