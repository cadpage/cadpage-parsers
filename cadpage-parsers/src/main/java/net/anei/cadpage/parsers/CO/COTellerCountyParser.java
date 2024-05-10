package net.anei.cadpage.parsers.CO;

import java.util.Properties;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;


public class COTellerCountyParser extends FieldProgramParser {

  public COTellerCountyParser() {
    super("TELLER COUNTY", "CO", 
          "Add:ADDR! Problem:CALL! Apt:APT! Loc:PLACE! Code:CODE! ( RP_Ph:PHONE! | RP_Phone:PHONE! ) Caution/Access_Info:ACC_INFO");
    setupGpsLookupTable(GPS_LOOKUP_TABLE);
  }

  @Override
  public String getFilter() {
    return "alerts@eptpaging.info";
  }
  
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=(?:Problem|Apt|Loc|Code|RP Ph|RP Phone|Caution/Access Info):)");

  @Override
  protected boolean parseMsg(String body, Data data) {
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    return super.parseMsg(body, data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("ACC_INFO")) return new MyAccessInfoField();
    return super.getField(name);
  }
  
  private static final Pattern ALI_INFO_PTN = Pattern.compile(" *911ALI Info[': ]*");
  private static final Pattern START_INFO_JUNK_PTN = Pattern.compile("^[-&. A-Za-z0-9]+, Wireless, ?\\d*,? *");
  private static final Pattern END_INFO_JUNK_PTN = Pattern.compile("[, ]*(?:\\[[^\\]]*|Multi-[^,]*|Automatic Case[^,]*)$");
  private static final Pattern INFO_JUNK_PTN = Pattern.compile(" *(?:Multi-(?:Agency|Jurisdiction) [ A-Za-z]+? Incident #: [A-Z]*[-0-9]*|A cellular re-bid has occurred, check the ANI/ALI Viewer for details|Automatic Case Number\\(s\\)[^,]+?,|\\[Shared\\]|\\[ProQA: Case Entry Complete\\]|\\[ProQA Session Aborted\\]),? *");
  
  private class MyAccessInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "False");
      field = stripFieldStart(field, "True");
      
      for (String part : ALI_INFO_PTN.split(field)) {
        data.strSupp = append(data.strSupp, "\n", cleanInfo(part));
      }
    }
    
    private String cleanInfo(String field) {
      field = START_INFO_JUNK_PTN.matcher(field).replaceFirst("");
      field = END_INFO_JUNK_PTN.matcher(field).replaceFirst("");
      StringBuilder sb = new StringBuilder();
      for (String part : INFO_JUNK_PTN.split(field)) {
        if (!part.isEmpty() && !part.equals("q")) {
          if (sb.length() > 0) sb.append(' ');
          sb.append(part);
        }
      }
      return stripFieldEnd(sb.toString(), ",");
    }
  }
    

  @Override
  protected String adjustGpsLookupAddress(String address) {
    return address.toUpperCase();
  }

  private static final Properties GPS_LOOKUP_TABLE = buildCodeTable(new String[]{
      "1364 CR 75",                           "+39.045712,-105.094569",
      "27668 HWY 67",                         "+39.029401,-105.070055",
      "28541 N HWY 67",                       "+39.052499,-105.094141",
      "16006 W HWY 24",                       "+38.974977,-105.075896",
      "16420 W HWY 24",                       "+38.975533,-105.074088"
 });
}
